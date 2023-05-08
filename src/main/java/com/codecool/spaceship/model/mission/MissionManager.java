package com.codecool.spaceship.model.mission;

import com.codecool.spaceship.model.Location;
import com.codecool.spaceship.model.exception.IllegalOperationException;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.MinerShipManager;
import com.codecool.spaceship.model.ship.SpaceShipManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class MissionManager {

    private final Mission mission;
    private MinerShipManager minerShip;

    public MissionManager(Mission mission) {
        this.mission = mission;
    }

    public static Mission startMiningMission(MinerShip minerShip, Location location, long activityDurationInSecs) throws IllegalOperationException {
        MinerShipManager minerShipManager = new MinerShipManager(minerShip);
        if (!minerShipManager.isAvailable()) {
            throw new IllegalOperationException("This ship is already on mission");
        }
        LocalDateTime startTime = LocalDateTime.now();
        long travelDurationInSecs = calculateTravelDurationInSecs(minerShipManager, location);

        Mission mission = Mission.builder()
                .startTime(startTime)
                .activityDurationInSecs(activityDurationInSecs)
                .travelDurationInSecs(travelDurationInSecs)
                .currentStatus(MissionStatus.EN_ROUTE)
                .currentObjectiveTime(startTime.plusSeconds(travelDurationInSecs))
                .missionType(MissionType.MINING)
                .ship(minerShip)
                .location(location)
                .events(new ArrayList<>())
                .build();
        minerShipManager.setCurrentMission(mission);
        return mission;
    }

    public boolean updateStatus() {
        if (mission.getEvents().isEmpty()) {
            addStartEvent();
        }
        Event lastEvent = peekLastEvent();
        if (mission.getCurrentStatus() == MissionStatus.OVER
                || mission.getCurrentStatus() == MissionStatus.ARCHIVED
                || lastEvent.getEndTime().isAfter(LocalDateTime.now())) {
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

    public void abortMission() {
        if (mission.getCurrentStatus() == MissionStatus.RETURNING
                || mission.getCurrentStatus() == MissionStatus.OVER) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        Event abortedEvent = popLastEvent();
        Event abortEvent = Event.builder()
                .eventType(EventType.ABORT)
                .endTime(now)
                .eventMessage("<%tF %<tT> Mission aborted by Command. Returning to station.".formatted(now))
                .build();

        if (abortedEvent.getEventType() == EventType.MINING_COMPLETE) {
            setMinerShipManagerIfNull();
            long abortedEventProgressInSecs = Duration.between(peekLastEvent().getEndTime(), now).getSeconds();
            int minedResources = calculateMinedResources(minerShip, abortedEventProgressInSecs);
            ResourceType resourceType = mission.getLocation().getResourceType();
            try {
                minerShip.addResourceToStorage(resourceType, minedResources);
            } catch (StorageException e) {
                throw new RuntimeException(e);
            }
        }
        pushNewEvent(abortEvent);
        startReturnTravel();
    }

    private void addStartEvent() {
        Event startEvent = Event.builder()
                .endTime(mission.getStartTime())
                .eventType(EventType.START)
                .eventMessage("<%tF %<tT> Left station for mining mission on %s.".formatted(mission.getStartTime(), mission.getLocation().getName()))
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
            peekLastEvent().setEventMessage("<%tF %<tT> Arrived on %s. Starting mining operation.".formatted(lastEventTime, mission.getLocation().getName()));
            setMinerShipManagerIfNull();
            long miningDurationInSecs = calculateMiningDurationInSecs(minerShip, mission.getActivityDurationInSecs());
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
        setMinerShipManagerIfNull();
        int minedResources;
        LocalDateTime lastEventTime = peekLastEvent().getEndTime();
        if (mission.getCurrentObjectiveTime().isEqual(lastEventTime)) {
            minedResources = calculateMinedResources(minerShip, mission.getActivityDurationInSecs());
        } else {
            minedResources = minerShip.getEmptyStorageSpace();
        }

        ResourceType resourceType = mission.getLocation().getResourceType();
        try {
            minerShip.addResourceToStorage(resourceType, minedResources);
        } catch (StorageException e) {
            throw new RuntimeException(e);
        }
        if (minerShip.getEmptyStorageSpace() > 0) {
            peekLastEvent().setEventMessage("<%tF %<tT> Mining complete. Mined %d %s(s). Returning to station.".formatted(lastEventTime, minedResources, resourceType));
        } else {
            peekLastEvent().setEventMessage("<%tF %<tT> Storage is full. Mined %d %s(s). Returning to station.".formatted(lastEventTime, minedResources, resourceType));
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
        peekLastEvent().setEventMessage("<%tF %<tT> Returned to station.".formatted(peekLastEvent().getEndTime()));
        mission.setCurrentStatus(MissionStatus.OVER);
        setMinerShipManagerIfNull();
        minerShip.endMission();
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

    private void setMinerShipManagerIfNull() {
        if (minerShip == null && mission.getShip() instanceof MinerShip) {
            minerShip = new MinerShipManager((MinerShip) mission.getShip());
        }
    }

    protected void setMinerShipManager(MinerShipManager minerShip) {
        this.minerShip = minerShip;
    }

    private Event peekLastEvent() {
        List<Event> events = mission.getEvents();
        return events.get(events.size()-1);
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
