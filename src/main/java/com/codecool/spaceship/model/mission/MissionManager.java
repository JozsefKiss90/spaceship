package com.codecool.spaceship.model.mission;

import com.codecool.spaceship.model.Location;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.SpaceShip;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Stack;

@Service
public class MissionManager {

    public Mission startMiningMission(MinerShip minerShip, Location location, long activityLengthInSecs) {
        LocalDateTime startTime = LocalDateTime.now();
        long travelTimeInSecs = calculateTravelTimeInSecs(minerShip, location);


        Mission mission = Mission.builder()
                .startTime(startTime)
                .activityTimeInSecs(activityLengthInSecs)
                .travelTimeInSecs(travelTimeInSecs)
                .currentStatus(MissionStatus.EN_ROUTE)
                .eventLog(new Stack<>())
                .missionType(MissionType.MINING)
                .ship(minerShip)
                .location(location)
                .build();

        Event startEvent = Event.builder()
                .endTime(startTime)
                .displayTime(startTime)
                .eventType(EventType.START)
                .displayType(EventType.START)
                .eventMessage("Left station for mining mission on %s".formatted(location.getName()))
                .build();

        mission.getEventLog().push(startEvent);
        updateStatus(mission);

        return mission;
    }

    public void updateStatus(Mission mission) {
        Event lastEvent = mission.getEventLog().peek();
        if (mission.getCurrentStatus() == MissionStatus.OVER
                || lastEvent.getEndTime().isAfter(LocalDateTime.now())) {
            return;
        }
        switch (lastEvent.getEventType()) {
            case START -> generateEnRouteEvents(mission);
            case ARRIVAL_AT_LOCATION -> startActivity(mission);
            case MINING_COMPLETE -> startReturnTravel(mission);
            case RETURNED_TO_STATION -> endMission(mission);
            case PIRATE_ATTACK -> simulatePirateAttack(mission);
            case METEOR_STORM -> simulateMeteorStorm(mission);
        }
        updateStatus(mission);
    }



    private void generateEnRouteEvents(Mission mission) {
        //TODO add pirateAttack/Meteor Storm based on chance
        //else event for arrival at destination gets added
        LocalDateTime arrivalTime = mission.getEventLog().peek().getDisplayTime();
        EventType travelEventType = (mission.getCurrentStatus() == MissionStatus.EN_ROUTE)
                ? EventType.ARRIVAL_AT_LOCATION
                : EventType.RETURNED_TO_STATION;
        Event travelEvent = Event.builder()
                .endTime(arrivalTime)
                .displayTime(arrivalTime)
                .eventType(travelEventType)
                .displayType(travelEventType)
                .build();
        mission.getEventLog().push(travelEvent);
    }

    private void startActivity(Mission mission) {
        mission.setCurrentStatus(MissionStatus.IN_PROGRESS);
        //TODO
    }

    private void startReturnTravel(Mission mission) {
        //TODO set mining message if needed
        mission.setCurrentStatus(MissionStatus.RETURNING);
        generateEnRouteEvents(mission);
    }

    private void endMission(Mission mission) {
        //TODO set return message
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


    private long calculateTravelTimeInSecs(SpaceShip ship, Location location) {
        double speedInDistancePerHour = ship.getSpeed();
        int distanceFromBase = location.getDistanceFromStation();
        int hourToSec = 60 * 60;
        return (long) (distanceFromBase / speedInDistancePerHour * hourToSec);
    }

}
