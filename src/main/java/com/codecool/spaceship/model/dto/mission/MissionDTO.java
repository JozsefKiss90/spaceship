package com.codecool.spaceship.model.dto.mission;

import com.codecool.spaceship.model.mission.MiningMission;
import com.codecool.spaceship.model.mission.Mission;
import com.codecool.spaceship.model.mission.MissionStatus;
import com.codecool.spaceship.model.mission.MissionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class MissionDTO {

    private final long id;
    private final String missionTitle;
    private final MissionType missionType;
    private final MissionStatus status;
    private final LocalDateTime currentObjectiveTime;
    private final LocalDateTime approxEndTime;

    public MissionDTO(Mission mission) {
        this(mission.getId(), generateTitle(mission), mission.getMissionType(), mission.getCurrentStatus(), mission.getCurrentObjectiveTime(), mission.getApproxEndTime());
    }

    private static String generateTitle(Mission mission) {
        if (mission instanceof MiningMission) {
            return "Mining mission on %s".formatted(((MiningMission) mission).getLocation().getName());
        } else {
            throw new RuntimeException("Mission type not recognized");
        }
    }
}
