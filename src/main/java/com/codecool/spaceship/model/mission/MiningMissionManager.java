package com.codecool.spaceship.model.mission;

import com.codecool.spaceship.model.location.Location;
import com.codecool.spaceship.model.dto.mission.MiningMissionDTO;
import com.codecool.spaceship.model.dto.mission.MissionDetailDTO;
import com.codecool.spaceship.model.exception.IllegalOperationException;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.MinerShipManager;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class MiningMissionManager extends MissionManager {

    public MiningMissionManager(MiningMission mission, Clock clock, Random random, MinerShipManager minerShipManager) {
        super(mission, clock, random, minerShipManager);
    }

    public MiningMissionManager(MiningMission mission, MinerShipManager minerShipManager) {
        this(mission, Clock.systemUTC(), new Random(), minerShipManager);
    }

    public static MiningMission startMiningMission(MinerShipManager minerShipManager, Location location, long activityDurationInSecs, Clock clock) throws IllegalOperationException {
        if (!minerShipManager.isAvailable()) {
            throw new IllegalOperationException("This ship is already on a mission");
        }
        if (location.getCurrentMission() != null) {
            throw new IllegalOperationException("There is a mission already in progress at this location");
        }
        LocalDateTime startTime = LocalDateTime.now(clock);
        long travelDurationInSecs = calculateTravelDurationInSecs(minerShipManager, location.getDistanceFromStation());
        long approxMissionDurationInSecs = travelDurationInSecs * 2 + activityDurationInSecs;

        MiningMission mission = MiningMission.builder()
                .startTime(startTime)
                .activityDurationInSecs(activityDurationInSecs)
                .travelDurationInSecs(travelDurationInSecs)
                .currentStatus(MissionStatus.EN_ROUTE)
                .currentObjectiveTime(startTime.plusSeconds(travelDurationInSecs))
                .approxEndTime(startTime.plusSeconds(approxMissionDurationInSecs))
                .ship(minerShipManager.getShip())
                .location(location)
                .user(minerShipManager.getShip().getUser())
                .events(new ArrayList<>())
                .build();
        minerShipManager.setCurrentMission(mission);
        location.setCurrentMission(mission);
        return mission;
    }

    public static MiningMission startMiningMission(MinerShipManager minerShipManager, Location location, long activityDurationInSecs) throws IllegalOperationException {
        return startMiningMission(minerShipManager, location, activityDurationInSecs, Clock.systemUTC());
    }

    @Override
    public MissionDetailDTO getDetailedDTO() {
        return new MiningMissionDTO((MiningMission) mission);
    }

    @Override
    public boolean abortMission() throws IllegalOperationException {
        switch (mission.getCurrentStatus()) {
            case OVER, ARCHIVED -> throw new IllegalOperationException("Mission is already over.");
            case RETURNING -> throw new IllegalOperationException("Mission is already returning.");
        }
        LocalDateTime now = LocalDateTime.now(clock);
        Event abortedEvent = popLastEvent();
        Event abortEvent = Event.builder()
                .eventType(EventType.ABORT)
                .endTime(now)
                .build();

        if (abortedEvent.getEventType() == EventType.ACTIVITY_COMPLETE) {
            long abortedEventProgressInSecs = Duration.between(peekLastEvent().getEndTime(), now).getSeconds();
            int minedResources = calculateMinedResources(((MinerShipManager) shipManager), abortedEventProgressInSecs);
            ResourceType resourceType = ((MiningMission) mission).getLocation().getResourceType();
            ((MinerShipManager) shipManager).addResourceToStorage(resourceType, minedResources);
            abortEvent.setEventMessage("Mission aborted by Command. Mined %d %s(s). Returning to station.".formatted(minedResources, resourceType));
        } else {
            abortEvent.setEventMessage("Mission aborted by Command. Returning to station.");
        }

        pushNewEvent(abortEvent);
        mission.setApproxEndTime(now.plusSeconds(mission.getTravelDurationInSecs()));
        startReturnTravel();
        return true;
    }

    @Override
    protected void addStartEvent() {
        Event startEvent = Event.builder()
                .endTime(mission.getStartTime())
                .eventType(EventType.START)
                .eventMessage("Left station for mining mission on %s.".formatted(((MiningMission) mission).getLocation().getName()))
                .build();
        pushNewEvent(startEvent);
    }

    @Override
    protected void startActivity() {
        mission.setCurrentStatus(MissionStatus.IN_PROGRESS);

        LocalDateTime lastEventTime = peekLastEvent().getEndTime();
        mission.setCurrentObjectiveTime(lastEventTime.plusSeconds(mission.getActivityDurationInSecs()));
        peekLastEvent().setEventMessage("Arrived on %s. Starting mining operation.".formatted(((MiningMission) mission).getLocation().getName()));
        long miningDurationInSecs = calculateMiningDurationInSecs(((MinerShipManager) shipManager), mission.getActivityDurationInSecs());
        Event activityEvent = Event.builder()
                .eventType(EventType.ACTIVITY_COMPLETE)
                .endTime(lastEventTime.plusSeconds(miningDurationInSecs))
                .build();
        pushNewEvent(activityEvent);

    }

    @Override
    protected void finishActivity() {
        int minedResources;
        LocalDateTime lastEventTime = peekLastEvent().getEndTime();
        if (mission.getCurrentObjectiveTime().isEqual(lastEventTime)) {
            minedResources = calculateMinedResources(((MinerShipManager) shipManager), mission.getActivityDurationInSecs());
        } else {
            minedResources = ((MinerShipManager) shipManager).getEmptyStorageSpace();
        }

        ResourceType resourceType = ((MiningMission) mission).getLocation().getResourceType();
        try {
            ((MinerShipManager) shipManager).addResourceToStorage(resourceType, minedResources);
        } catch (StorageException e) {
            throw new RuntimeException(e);
        }
        if (((MinerShipManager) shipManager).getEmptyStorageSpace() > 0) {
            peekLastEvent().setEventMessage("Mining complete. Mined %d %s(s). Returning to station.".formatted(minedResources, resourceType));
        } else {
            peekLastEvent().setEventMessage("Storage is full. Mined %d %s(s). Returning to station.".formatted(minedResources, resourceType));
            mission.setApproxEndTime(LocalDateTime.now(clock).plusSeconds(mission.getTravelDurationInSecs()));
        }

        startReturnTravel();
    }

    @Override
    protected void endMission() {
        peekLastEvent().setEventMessage("Returned to station.");
        mission.setCurrentStatus(MissionStatus.OVER);
        shipManager.endMission();
        ((MiningMission) mission).getLocation().setCurrentMission(null);
    }

    private void simulatePirateAttack() {
        //TODO
        generateEnRouteEvents();
    }

    private void simulateMeteorStorm() {
        //TODO
        generateEnRouteEvents();
    }

    private static long calculateMiningDurationInSecs(MinerShipManager minerShip, long activityDurationInSecs) {
        double activityDurationInHours = activityDurationInSecs / 60.0 / 60.0;
        int resourceMinedPerHour = minerShip.getDrillEfficiency();
        int emptyStorageSpace = minerShip.getEmptyStorageSpace();
        if (Math.floor(resourceMinedPerHour * activityDurationInHours) <= emptyStorageSpace) {
            return activityDurationInSecs;
        } else {
            double hoursNeededToFillStorage = (double) emptyStorageSpace / resourceMinedPerHour;
            return (long) Math.ceil(hoursNeededToFillStorage * 60 * 60);
        }
    }

    private static int calculateMinedResources(MinerShipManager minerShip, long activityDurationInSecs) {
        double activityDurationInHours = activityDurationInSecs / 60.0 / 60.0;
        int resourceMinedPerHour = minerShip.getDrillEfficiency();
        return (int) Math.floor(resourceMinedPerHour * activityDurationInHours);
    }

}
