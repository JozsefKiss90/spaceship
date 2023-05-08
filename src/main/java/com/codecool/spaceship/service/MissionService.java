package com.codecool.spaceship.service;

import com.codecool.spaceship.model.Location;
import com.codecool.spaceship.model.dto.MissionDTO;
import com.codecool.spaceship.model.exception.DataNotFoundException;
import com.codecool.spaceship.model.exception.IllegalOperationException;
import com.codecool.spaceship.model.mission.Mission;
import com.codecool.spaceship.model.mission.MissionManager;
import com.codecool.spaceship.model.mission.MissionStatus;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.repository.LocationRepository;
import com.codecool.spaceship.repository.MissionRepository;
import com.codecool.spaceship.repository.SpaceShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MissionService {

    private final MissionRepository missionRepository;
    private final SpaceShipRepository spaceShipRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public MissionService(MissionRepository missionRepository, SpaceShipRepository spaceShipRepository, LocationRepository locationRepository) {
        this.missionRepository = missionRepository;
        this.spaceShipRepository = spaceShipRepository;
        this.locationRepository = locationRepository;
    }

    public List<MissionDTO> getAllActiveMissions() {
        return missionRepository.getMissionsByCurrentStatusNot(MissionStatus.ARCHIVED).stream()
                .map(this::updateAndConvert)
                .toList();
    }

    public List<MissionDTO> getAllArchivedMissions() {
        return missionRepository.getMissionsByCurrentStatus(MissionStatus.ARCHIVED).stream()
                .map(this::updateAndConvert)
                .toList();
    }

    public MissionDTO getMissionById(Long id) throws DataNotFoundException {
        return missionRepository.findById(id)
                .map(MissionDTO::new)
                .orElseThrow(() -> new DataNotFoundException("No mission found with id %d".formatted(id)));
    }

    public MissionDTO startNewMission(long shipId, long locationId, long activityDurationInSecs) throws DataNotFoundException, IllegalOperationException {
        SpaceShip spaceShip = spaceShipRepository.findById(shipId).orElseThrow(() -> new DataNotFoundException("No ship found with id %d".formatted(shipId)));
        MinerShip minerShip;
        if (spaceShip instanceof MinerShip) {
            minerShip = (MinerShip) spaceShip;
        } else {
            throw new IllegalArgumentException("Ship is not a miner ship");
        }
        Location location = locationRepository.findById(locationId).orElseThrow(() -> new DataNotFoundException("No location found with id %d".formatted(locationId)));
        Mission mission = MissionManager.startMiningMission(minerShip, location, activityDurationInSecs);
        MissionManager missionManager = new MissionManager(mission);
        mission = missionRepository.save(mission);
        missionManager.updateStatus();
        mission = missionRepository.save(mission);
        return new MissionDTO(mission);
    }

    public boolean archiveMission(Long id) throws DataNotFoundException, IllegalOperationException {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("No mission found with id %d".formatted(id)));
        MissionManager missionManager = new MissionManager(mission);
        if(missionManager.archiveMission()) {
            missionRepository.save(mission);
            return true;
        }
        return false;
    }

    private MissionDTO updateAndConvert(Mission mission) {
        MissionManager missionManager = new MissionManager(mission);
        if (missionManager.updateStatus()) {
            mission = missionRepository.save(mission);
        }
        return new MissionDTO(mission);
    }
}
