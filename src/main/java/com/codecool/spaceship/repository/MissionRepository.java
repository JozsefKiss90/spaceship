package com.codecool.spaceship.repository;

import com.codecool.spaceship.model.mission.Mission;
import com.codecool.spaceship.model.mission.MissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    List<Mission> getMissionsByCurrentStatusNot(MissionStatus missionStatus);
    List<Mission> getMissionsByCurrentStatus(MissionStatus missionStatus);
}
