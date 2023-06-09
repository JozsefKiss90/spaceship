package com.codecool.spaceship.service;

import com.codecool.spaceship.model.Location;
import com.codecool.spaceship.model.UserEntity;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
    public List<MissionDTO> getAllActiveMissionsForCurrentUser() {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return missionRepository.getMissionsByUserIdAndCurrentStatusNot(user.getId(), MissionStatus.ARCHIVED).stream()
                .map(this::updateAndConvert)
                .toList();
    }

    public List<MissionDTO> getAllActiveMissionsByUserId(Long userId) {
        return missionRepository.getMissionsByUserIdAndCurrentStatusNot(userId, MissionStatus.ARCHIVED).stream()
                .map(this::updateAndConvert)
                .toList();
    }

    public List<MissionDTO> getAllArchivedMissionsForCurrentUser() {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return missionRepository.getMissionsByUserIdAndCurrentStatus(user.getId(), MissionStatus.ARCHIVED).stream()
                .map(MissionDTO::new)
                .toList();
    }

    public List<MissionDTO> getAllArchivedMissionsByUserId(Long userId) {
        return missionRepository.getMissionsByUserIdAndCurrentStatus(userId, MissionStatus.ARCHIVED).stream()
                .map(MissionDTO::new)
                .toList();
    }

    public MissionDTO getMissionById(Long id) throws DataNotFoundException {
        return updateAndConvert(getMissionByIdAndCheckAccess(id));
    }

    public MissionDTO startNewMission(long shipId, long locationId, long activityDurationInSecs) throws DataNotFoundException, IllegalOperationException {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SpaceShip spaceShip = spaceShipRepository.findById(shipId)
                .orElseThrow(() -> new DataNotFoundException("No ship found with id %d".formatted(shipId)));
        if (!Objects.equals(user.getId(), spaceShip.getUser().getId())) {
            throw new SecurityException("You don't have authority to send this ship");
        }
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

    public MissionDTO archiveMission(Long id) throws DataNotFoundException, IllegalOperationException {
        Mission mission = getMissionByIdAndCheckAccess(id);
        MissionManager missionManager = new MissionManager(mission);
        if(missionManager.archiveMission()) {
            mission = missionRepository.save(mission);
            return new MissionDTO(mission);
        }
        return null;
    }

    public MissionDTO abortMission(Long id) throws DataNotFoundException, IllegalOperationException {
        Mission mission = getMissionByIdAndCheckAccess(id);
        MissionManager missionManager = new MissionManager(mission);
        if (missionManager.abortMission()) {
            mission = missionRepository.save(mission);
            return new MissionDTO(mission);
        }
        return null;
    }

    private MissionDTO updateAndConvert(Mission mission) {
        MissionManager missionManager = new MissionManager(mission);
        if (missionManager.updateStatus()) {
            mission = missionRepository.save(mission);
        }
        return new MissionDTO(mission);
    }

    private Mission getMissionByIdAndCheckAccess(Long id) throws DataNotFoundException {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("No mission found with id %d".formatted(id)));
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
                || Objects.equals(user.getId(), mission.getUser().getId())) {
            return mission;
        } else {
            throw new SecurityException("You don't have authority to access this mission");
        }
    }

}
