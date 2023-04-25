package com.codecool.spaceship.model.mission;

import com.codecool.spaceship.model.Location;
import com.codecool.spaceship.model.ship.SpaceShip;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Stack;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mission {

    private LocalDateTime startTime;
    private Location location;
    private MissionType missionType;
    private SpaceShip ship;
    private long travelTimeInSecs;
    private long activityTimeInSecs;
    private MissionStatus currentStatus;
    private Stack<Event> eventLog;
}
