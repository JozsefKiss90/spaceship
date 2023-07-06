package com.codecool.spaceship.model.dto.mission;

import com.codecool.spaceship.model.Location;
import com.codecool.spaceship.model.mission.Event;
import com.codecool.spaceship.model.mission.MiningMission;
import com.codecool.spaceship.model.mission.MissionStatus;
import com.codecool.spaceship.model.mission.MissionType;
import com.codecool.spaceship.model.ship.SpaceShip;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MiningMissionDTO extends MissionDetailDTO {

    private final String location;

    public MiningMissionDTO(long id, String title, MissionType missionType, MissionStatus status, LocalDateTime currentObjectiveTime,
                            LocalDateTime approxEndTime, List<Event> events, SpaceShip ship, Location location) {
        super(id, title, missionType, status, currentObjectiveTime, approxEndTime, events, ship);
        this.location = location.getName();
    }

    public MiningMissionDTO(MiningMission mission) {
        this(mission.getId(), generateMissionTitle(mission), mission.getMissionType(), mission.getCurrentStatus(), mission.getCurrentObjectiveTime(),
                mission.getApproxEndTime(), mission.getEvents(), mission.getShip(), mission.getLocation());
    }

    private static String generateMissionTitle(MiningMission mission) {
        return "Mining mission on %s".formatted(mission.getLocation().getName());
    }
}
