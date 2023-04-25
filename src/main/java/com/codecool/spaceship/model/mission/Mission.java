package com.codecool.spaceship.model.mission;

import com.codecool.spaceship.model.Location;
import com.codecool.spaceship.model.ship.SpaceShip;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mission {

    private LocalDateTime startTime;
    private LocalDateTime arrivalAtActivity;
    private LocalDateTime activityEnd;
    private LocalDateTime returnToStation;
    private Location location;
    private MissionType missionType;
    private SpaceShip ship;
    
    public MissionStatus getMissionStatus() {
        LocalDateTime now = LocalDateTime.now();
        if (returnToStation.isBefore(now)) {
            return MissionStatus.COMPLETE;
        } else if (activityEnd.isBefore(now)) {
            return MissionStatus.TRAVELING_BACK_TO_BASE;
        } else if (arrivalAtActivity.isBefore(now)) {
            return MissionStatus.IN_PROGRESS;
        } else {
            return MissionStatus.TRAVELING_TO_MISSION;
        }
    }
}
