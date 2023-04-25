package com.codecool.spaceship.model.mission;

import com.codecool.spaceship.model.Location;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.SpaceShip;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class MissionGenerator {

    public Mission startMiningMission(MinerShip minerShip, Location location, long activityLengthInMins) {
        Mission mission = Mission.builder()
                .missionType(MissionType.MINING)
                .ship(minerShip)
                .location(location)
                .build();

        long travelTimeInSecs = calculateTravelTimeInSecs(minerShip, location);
        setTimes(mission, activityLengthInMins, travelTimeInSecs);
        return mission;
    }

    private long calculateTravelTimeInSecs(SpaceShip ship, Location location) {
        double speedInDistancePerHour = ship.getSpeed();
        int distanceFromBase = location.getDistanceFromStation();
        int hourToSec = 60 * 60;
        return (long) (distanceFromBase / speedInDistancePerHour * hourToSec);
    }

    private void setTimes(Mission mission, long activityLengthInMins, long travelTimeInSecs) {
        LocalDateTime startTime = LocalDateTime.now();
        mission.setStartTime(startTime);

        LocalDateTime arrivalTime = startTime.plusSeconds(travelTimeInSecs);
        mission.setArrivalAtActivity(arrivalTime);

        LocalDateTime activityEndTime = arrivalTime.plusMinutes(activityLengthInMins);
        mission.setActivityEnd(activityEndTime);

        LocalDateTime returnTime = activityEndTime.plusSeconds(travelTimeInSecs);
        mission.setReturnToStation(returnTime);
    }
}
