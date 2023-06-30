package com.codecool.spaceship.model.mission;

import com.codecool.spaceship.model.Location;
import com.codecool.spaceship.model.exception.IllegalOperationException;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.MinerShipManager;
import com.codecool.spaceship.model.ship.SpaceShipManager;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MissionManager {

    private final Mission mission;
    private final Clock clock;
    private MinerShipManager minerShipManager;


    public MissionManager(Mission mission, Clock clock, MinerShipManager minerShipManager) {
        this.mission = mission;
        this.clock = clock;
        if (!mission.getShip().equals(minerShipManager.getMinerShip())) {
            throw new IllegalArgumentException("Ship is not the one on this mission.");
        }
        this.minerShipManager = minerShipManager;
    }

    public MissionManager(Mission mission, MinerShipManager minerShipManager) {
        this(mission, Clock.systemUTC(), minerShipManager);
    }

    public void setMinerShipManager(MinerShipManager minerShipManager) {
        if (!mission.getShip().equals(minerShipManager.getMinerShip())) {
            throw new IllegalArgumentException("Ship is not the one on this mission.");
        }
        this.minerShipManager = minerShipManager;
    }

    public static Mission startMiningMission(MinerShipManager minerShipManager, Location location, long activityDurationInSecs, Clock clock) throws IllegalOperationException {
        if (!minerShipManager.isAvailable()) {
            throw new IllegalOperationException("This ship is already on a mission");
        }
        if (location.getCurrentMission() != null) {
            throw new IllegalOperationException("There is a mission already in progress at this location");
        }
        LocalDateTime startTime = LocalDateTime.now(clock);
        long travelDurationInSecs = calculateTravelDurationInSecs(minerShipManager, location);
        long approxMissionDurationInSecs = travelDurationInSecs * 2 + activityDurationInSecs;

        Mission mission = Mission.builder()
                .startTime(startTime)
                .activityDurationInSecs(activityDurationInSecs)
                .travelDurationInSecs(travelDurationInSecs)
                .currentStatus(MissionStatus.EN_ROUTE)
                .currentObjectiveTime(startTime.plusSeconds(travelDurationInSecs))
                .approxEndTime(startTime.plusSeconds(approxMissionDurationInSecs))
                .missionType(MissionType.MINING)
                .ship(minerShipManager.getMinerShip())
                .location(location)
                .user(minerShipManager.getMinerShip().getUser())
                .events(new ArrayList<>())
                .build();
        minerShipManager.setCurrentMission(mission);
        location.setCurrentMission(mission);
        return mission;
    }

    public static Mission startMiningMission(MinerShipManager minerShipManager, Location location, long activityDurationInSecs) throws IllegalOperationException {
        return startMiningMission(minerShipManager, location, activityDurationInSecs, Clock.systemUTC());
    }

    public boolean updateStatus() {
        if (mission.getEvents().isEmpty()) {
            addStartEvent();
        }
        Event lastEvent = peekLastEvent();
        if (mission.getCurrentStatus() == MissionStatus.OVER
                || mission.getCurrentStatus() == MissionStatus.ARCHIVED
                || lastEvent.getEndTime().isAfter(LocalDateTime.now(clock))) {
            return false;
        }
        switch (lastEvent.getEventType()) {
            case START -> generateEnRouteEvents();
            case ARRIVAL_AT_LOCATION -> startActivity();
            case MINING_COMPLETE -> finishMining();
            case RETURNED_TO_STATION -> endMission();
            case PIRATE_ATTACK -> simulatePirateAttack();
            case METEOR_STORM -> simulateMeteorStorm();
        }
        updateStatus();
        return true;
    }

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

        if (abortedEvent.getEventType() == EventType.MINING_COMPLETE) {
            long abortedEventProgressInSecs = Duration.between(peekLastEvent().getEndTime(), now).getSeconds();
            int minedResources = calculateMinedResources(minerShipManager, abortedEventProgressInSecs);
            ResourceType resourceType = mission.getLocation().getResourceType();
            try {
                minerShipManager.addResourceToStorage(resourceType, minedResources);
            } catch (StorageException e) {
                throw new RuntimeException(e);
            }
            abortEvent.setEventMessage("Mission aborted by Command. Mined %d %s(s). Returning to station.".formatted(minedResources, resourceType));
        }

        if (abortEvent.getEventMessage() == null) {
            abortEvent.setEventMessage("Mission aborted by Command. Returning to station.");
        }

        pushNewEvent(abortEvent);
        mission.setApproxEndTime(now.plusSeconds(mission.getTravelDurationInSecs()));
        startReturnTravel();
        return true;
    }

    public boolean archiveMission() throws IllegalOperationException {
        if (mission.getCurrentStatus() == MissionStatus.ARCHIVED) {
            throw new IllegalOperationException("Mission is already archived");
        } else if (mission.getCurrentStatus() != MissionStatus.OVER) {
            throw new IllegalOperationException("Mission can't be archived until its over");
        } else {
            mission.setCurrentStatus(MissionStatus.ARCHIVED);
            return true;
        }
    }

    private void addStartEvent() {
        Event startEvent = Event.builder()
                .endTime(mission.getStartTime())
                .eventType(EventType.START)
                .eventMessage("Left station for mining mission on %s.".formatted(mission.getLocation().getName()))
                .build();
        pushNewEvent(startEvent);
    }

    private void generateEnRouteEvents() {
        //TODO add pirateAttack/Meteor Storm based on chance
        //else event for arrival at destination gets added
        EventType travelEventType = (mission.getCurrentStatus() == MissionStatus.EN_ROUTE)
                ? EventType.ARRIVAL_AT_LOCATION
                : EventType.RETURNED_TO_STATION;
        Event travelEvent = Event.builder()
                .endTime(mission.getCurrentObjectiveTime())
                .eventType(travelEventType)
                .build();
        pushNewEvent(travelEvent);
    }

    private void startActivity() {
        mission.setCurrentStatus(MissionStatus.IN_PROGRESS);

        LocalDateTime lastEventTime = peekLastEvent().getEndTime();
        mission.setCurrentObjectiveTime(lastEventTime.plusSeconds(mission.getActivityDurationInSecs()));
        if (mission.getMissionType() == MissionType.MINING) {
            peekLastEvent().setEventMessage("Arrived on %s. Starting mining operation.".formatted(mission.getLocation().getName()));
            long miningDurationInSecs = calculateMiningDurationInSecs(minerShipManager, mission.getActivityDurationInSecs());
            Event miningEvent = Event.builder()
                    .eventType(EventType.MINING_COMPLETE)
                    .endTime(lastEventTime.plusSeconds(miningDurationInSecs))
                    .build();
            pushNewEvent(miningEvent);
        } else {
            throw new RuntimeException("Unknown mission type");
        }
    }

    private void finishMining() {
        int minedResources;
        LocalDateTime lastEventTime = peekLastEvent().getEndTime();
        if (mission.getCurrentObjectiveTime().isEqual(lastEventTime)) {
            minedResources = calculateMinedResources(minerShipManager, mission.getActivityDurationInSecs());
        } else {
            minedResources = minerShipManager.getEmptyStorageSpace();
        }

        ResourceType resourceType = mission.getLocation().getResourceType();
        try {
            minerShipManager.addResourceToStorage(resourceType, minedResources);
        } catch (StorageException e) {
            throw new RuntimeException(e);
        }
        if (minerShipManager.getEmptyStorageSpace() > 0) {
            peekLastEvent().setEventMessage("Mining complete. Mined %d %s(s). Returning to station.".formatted(minedResources, resourceType));
        } else {
            peekLastEvent().setEventMessage("Storage is full. Mined %d %s(s). Returning to station.".formatted(minedResources, resourceType));
            mission.setApproxEndTime(LocalDateTime.now(clock).plusSeconds(mission.getTravelDurationInSecs()));
        }

        startReturnTravel();
    }

    private void startReturnTravel() {
        LocalDateTime lastEventTime = peekLastEvent().getEndTime();
        long returnDurationInSecs;
        if (mission.getCurrentStatus() == MissionStatus.EN_ROUTE) {
            returnDurationInSecs = Duration.between(mission.getStartTime(), lastEventTime).getSeconds();
        } else {
            returnDurationInSecs = mission.getTravelDurationInSecs();
        }
        mission.setCurrentObjectiveTime(lastEventTime.plusSeconds(returnDurationInSecs));
        mission.setCurrentStatus(MissionStatus.RETURNING);
        generateEnRouteEvents();
    }

    private void endMission() {
        peekLastEvent().setEventMessage("Returned to station.");
        mission.setCurrentStatus(MissionStatus.OVER);
        minerShipManager.endMission();
        mission.getLocation().setCurrentMission(null);
    }

    private void simulatePirateAttack() {
        //TODO
        generateEnRouteEvents();
    }

    private void simulateMeteorStorm() {
        //TODO
        generateEnRouteEvents();
    }


    private static long calculateTravelDurationInSecs(SpaceShipManager ship, Location location) {
        double speedInDistancePerHour = ship.getSpeed();
        int distanceFromBase = location.getDistanceFromStation();
        int hourToSec = 60 * 60;
        return (long) (distanceFromBase / speedInDistancePerHour * hourToSec);
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

    private Event peekLastEvent() {
        List<Event> events = mission.getEvents();
        return events.get(events.size() - 1);
    }

    private boolean pushNewEvent(Event event) {
        List<Event> events = mission.getEvents();
        return events.add(event);
    }

    private Event popLastEvent() {
        List<Event> events = mission.getEvents();
        Event event = events.get(events.size() - 1);
        events.remove(event);
        return event;
    }
}
