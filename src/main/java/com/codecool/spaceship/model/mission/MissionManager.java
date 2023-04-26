package com.codecool.spaceship.model.mission;

import com.codecool.spaceship.model.Location;
import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.SpaceShip;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Stack;

@Service
public class MissionManager {

    public Mission startMiningMission(MinerShip minerShip, Location location, long activityDurationInSecs) {
        LocalDateTime startTime = LocalDateTime.now();
        long travelDurationInSecs = calculateTravelDurationInSecs(minerShip, location);


        Mission mission = Mission.builder()
                .startTime(startTime)
                .activityDurationInSecs(activityDurationInSecs)
                .travelDurationInSecs(travelDurationInSecs)
                .currentStatus(MissionStatus.EN_ROUTE)
                .currentObjectiveTime(startTime.plusSeconds(travelDurationInSecs))
                .events(new Stack<>())
                .missionType(MissionType.MINING)
                .ship(minerShip)
                .location(location)
                .build();

        Event startEvent = Event.builder()
                .endTime(startTime)
                .eventType(EventType.START)
                .eventMessage("Left station for mining mission on %s.".formatted(location.getName()))
                .build();

        mission.getEvents().push(startEvent);
        updateStatus(mission);

        return mission;
    }

    public void updateStatus(Mission mission) {
        Event lastEvent = mission.getEvents().peek();
        if (mission.getCurrentStatus() == MissionStatus.OVER
                || lastEvent.getEndTime().isAfter(LocalDateTime.now())) {
            return;
        }
        switch (lastEvent.getEventType()) {
            case START -> generateEnRouteEvents(mission);
            case ARRIVAL_AT_LOCATION -> startActivity(mission);
            case MINING_COMPLETE -> finishMining(mission);
            case RETURNED_TO_STATION -> endMission(mission);
            case PIRATE_ATTACK -> simulatePirateAttack(mission);
            case METEOR_STORM -> simulateMeteorStorm(mission);
        }
        updateStatus(mission);
    }

    public void abortMission(Mission mission) {
        if (mission.getCurrentStatus() == MissionStatus.RETURNING
                || mission.getCurrentStatus() == MissionStatus.OVER) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        Event abortedEvent = mission.getEvents().pop();
        Event abortEvent = Event.builder()
                .eventType(EventType.ABORT)
                .endTime(now)
                .eventMessage("Mission aborted by Command. Returning to station.")
                .build();

        if (abortedEvent.getEventType() == EventType.MINING_COMPLETE) {
            MinerShip minerShip = (MinerShip) mission.getShip();
            long abortedEventProgressInSecs = Duration.between(mission.getEvents().peek().getEndTime(), now).getSeconds();
            int minedResources = calculateMinedResources(minerShip, abortedEventProgressInSecs);
            Resource resourceType = mission.getLocation().getResourceType();
            try {
                minerShip.addResourceToStorage(resourceType, minedResources);
            } catch (StorageException e) {
                throw new RuntimeException(e);
            }
        }
        mission.getEvents().push(abortEvent);
        startReturnTravel(mission);
    }



    private void generateEnRouteEvents(Mission mission) {
        //TODO add pirateAttack/Meteor Storm based on chance
        //else event for arrival at destination gets added
        EventType travelEventType = (mission.getCurrentStatus() == MissionStatus.EN_ROUTE)
                ? EventType.ARRIVAL_AT_LOCATION
                : EventType.RETURNED_TO_STATION;
        Event travelEvent = Event.builder()
                .endTime(mission.getCurrentObjectiveTime())
                .eventType(travelEventType)
                .build();
        mission.getEvents().push(travelEvent);
    }

    private void startActivity(Mission mission) {
        mission.setCurrentStatus(MissionStatus.IN_PROGRESS);

        LocalDateTime lastEventTime = mission.getLastEventTime();
        mission.setCurrentObjectiveTime(lastEventTime.plusSeconds(mission.getActivityDurationInSecs()));
        if (mission.getMissionType() == MissionType.MINING) {
            mission.getEvents().peek().setEventMessage("Arrived on %s. Starting mining operation.".formatted(mission.getLocation().getName()));
            long miningDurationInSecs = calculateMiningDurationInSecs((MinerShip) mission.getShip(), mission.getActivityDurationInSecs());
            Event miningEvent = Event.builder()
                    .eventType(EventType.MINING_COMPLETE)
                    .endTime(lastEventTime.plusSeconds(miningDurationInSecs))
                    .build();
            mission.getEvents().push(miningEvent);
        } else {
            throw new RuntimeException("Unknown mission type");
        }
    }

    private void finishMining(Mission mission) {
        MinerShip minerShip = (MinerShip) mission.getShip();
        int minedResources;
        if (mission.getCurrentObjectiveTime().isEqual(mission.getLastEventTime())) {
            minedResources = calculateMinedResources(minerShip, mission.getActivityDurationInSecs());
        } else {
            minedResources = minerShip.getEmptyStorageSpace();
        }

        Resource resourceType = mission.getLocation().getResourceType();
        try {
            minerShip.addResourceToStorage(resourceType, minedResources);
        } catch (StorageException e) {
            throw new RuntimeException(e);
        }
        if (minerShip.getEmptyStorageSpace() > 0) {
            mission.getEvents().peek().setEventMessage("Mining complete. Mined %d %s(s). Returning to station.".formatted(minedResources, resourceType));
        } else {
            mission.getEvents().peek().setEventMessage("Storage is full. Mined %d %s(s). Returning to station.".formatted(minedResources, resourceType));
        }

        startReturnTravel(mission);
    }

    private void startReturnTravel(Mission mission) {
        LocalDateTime lastEventTime = mission.getLastEventTime();
        long returnDurationInSecs;
        if (mission.getCurrentStatus() == MissionStatus.EN_ROUTE) {
            returnDurationInSecs = Duration.between(mission.getStartTime(), lastEventTime).getSeconds();
        } else {
            returnDurationInSecs = mission.getTravelDurationInSecs();
        }
        mission.setCurrentObjectiveTime(lastEventTime.plusSeconds(returnDurationInSecs));
        mission.setCurrentStatus(MissionStatus.RETURNING);
        generateEnRouteEvents(mission);
    }

    private void endMission(Mission mission) {
        mission.getEvents().peek().setEventMessage("Returned to base.");
        mission.setCurrentStatus(MissionStatus.OVER);
    }

    private void simulatePirateAttack(Mission mission) {
        //TODO
        generateEnRouteEvents(mission);
    }

    private void simulateMeteorStorm(Mission mission) {
        //TODO
        generateEnRouteEvents(mission);
    }


    private long calculateTravelDurationInSecs(SpaceShip ship, Location location) {
        double speedInDistancePerHour = ship.getSpeed();
        int distanceFromBase = location.getDistanceFromStation();
        int hourToSec = 60 * 60;
        return (long) (distanceFromBase / speedInDistancePerHour * hourToSec);
    }

    private long calculateMiningDurationInSecs(MinerShip minerShip, long activityDurationInSecs) {
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

    private int calculateMinedResources(MinerShip minerShip, long activityDurationInSecs) {
        double activityDurationInHours = activityDurationInSecs / 60.0 / 60.0;
        int resourceMinedPerHour = minerShip.getDrillEfficiency();
        return (int) Math.floor(resourceMinedPerHour * activityDurationInHours);
    }

}
