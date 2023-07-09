package com.codecool.spaceship.model.dto.mission;

import com.codecool.spaceship.model.mission.*;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.SpaceShip;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ScoutingMissionDTO extends MissionDetailDTO {

    private final ResourceType targetResource;
    private final boolean prioritizingDistance;

    public ScoutingMissionDTO(long id, String title , MissionStatus status, LocalDateTime currentObjectiveTime,
                              LocalDateTime approxEndTime, List<Event> events, SpaceShip ship, ResourceType targetResource, boolean prioritizeDistance) {
        super(id, title, MissionType.SCOUTING, status, currentObjectiveTime, approxEndTime, events, ship);
        this.targetResource = targetResource;
        this.prioritizingDistance = prioritizeDistance;
    }

    public ScoutingMissionDTO(ScoutingMission mission) {
        this(mission.getId(), generateMissionTitle(mission), mission.getCurrentStatus(), mission.getCurrentObjectiveTime(),
                mission.getApproxEndTime(), mission.getEvents(), mission.getShip(), mission.getTargetResource(), mission.isPrioritizingDistance());
    }

    private static String generateMissionTitle(ScoutingMission mission) {
        return "Exploration mission for %s".formatted(mission.getTargetResource().toString().toLowerCase());
    }
}
