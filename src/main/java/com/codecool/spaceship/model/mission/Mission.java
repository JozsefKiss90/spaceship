package com.codecool.spaceship.model.mission;

import com.codecool.spaceship.model.Location;
import com.codecool.spaceship.model.ship.SpaceShip;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Stack;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mission {

    private LocalDateTime startTime;
    private LocalDateTime currentObjectiveTime;
    private MissionStatus currentStatus;
    private MissionType missionType;
    private Location location;
    private SpaceShip ship;
    private long travelDurationInSecs;
    private long activityDurationInSecs;
    private Stack<Event> events;

    public LocalDateTime getLastEventTime() {
        return events.peek().getEndTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mission mission = (Mission) o;
        return travelDurationInSecs == mission.travelDurationInSecs
                && activityDurationInSecs == mission.activityDurationInSecs
                && ((startTime != null && mission.startTime != null
                    && (ChronoUnit.SECONDS.between(startTime, mission.startTime) < 0.5
                        && ChronoUnit.SECONDS.between(startTime, mission.startTime) > -0.5))
                    || startTime == null && mission.startTime == null)
                && ((currentObjectiveTime != null && mission.currentObjectiveTime != null
                    && (ChronoUnit.SECONDS.between(currentObjectiveTime, mission.currentObjectiveTime) < 0.5
                        && ChronoUnit.SECONDS.between(currentObjectiveTime, mission.currentObjectiveTime) > -0.5))
                    || currentObjectiveTime == null && mission.currentObjectiveTime == null)
                && currentStatus == mission.currentStatus
                && missionType == mission.missionType
                && Objects.equals(location, mission.location)
                && Objects.equals(ship, mission.ship)
                && Objects.equals(events, mission.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, currentObjectiveTime, currentStatus, missionType, location, ship, travelDurationInSecs, activityDurationInSecs, events);
    }
}
