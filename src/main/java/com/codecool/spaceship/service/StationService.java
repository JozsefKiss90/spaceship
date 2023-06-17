package com.codecool.spaceship.service;

import com.codecool.spaceship.model.UserEntity;
import com.codecool.spaceship.model.dto.HangarDTO;
import com.codecool.spaceship.model.dto.SpaceStationDTO;
import com.codecool.spaceship.model.dto.SpaceStationStorageDTO;
import com.codecool.spaceship.model.exception.DataNotFoundException;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.MinerShipManager;
import com.codecool.spaceship.model.ship.ShipType;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.station.SpaceStation;
import com.codecool.spaceship.model.station.SpaceStationManager;
import com.codecool.spaceship.repository.SpaceShipRepository;
import com.codecool.spaceship.repository.SpaceStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public class StationService {

    private final SpaceStationRepository spaceStationRepository;
    private final SpaceShipRepository spaceShipRepository;

    @Autowired
    public StationService(SpaceStationRepository spaceStationRepository, SpaceShipRepository spaceShipRepository) {
        this.spaceStationRepository = spaceStationRepository;
        this.spaceShipRepository = spaceShipRepository;
    }

    public SpaceStationDTO getBaseById(long stationId) throws DataNotFoundException {
        SpaceStation station = getStationByIdAndCheckAccess(stationId);

        SpaceStationManager stationManager = new SpaceStationManager(station);
        return stationManager.getStationDTO();
    }

    public SpaceStationDTO getBaseByUserId(long userId) throws DataNotFoundException {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
                || Objects.equals(user.getId(), userId)) {
            SpaceStation station = spaceStationRepository.getSpaceStationByUserId(userId)
                    .orElseThrow(() -> new DataNotFoundException("No station found with user id %d".formatted(userId)));
            SpaceStationManager stationManager = new SpaceStationManager(station);
            return stationManager.getStationDTO();
        } else {
            throw new SecurityException("You don't have authority to access this station");
        }

    }

    public boolean addResources(long stationId, Map<ResourceType, Integer> resources) throws StorageException, DataNotFoundException {
        SpaceStation station = getStationByIdAndCheckAccess(stationId);

        SpaceStationManager stationManager = new SpaceStationManager(station);
        for (ResourceType resource : resources.keySet()) {
            stationManager.addResource(resource, resources.get(resource));
        }
        spaceStationRepository.save(station);
        return true;
    }

    public long addShip(long stationId, String name, Color color, ShipType shipType) throws StorageException, DataNotFoundException {
        SpaceStation station = getStationByIdAndCheckAccess(stationId);

        SpaceShip ship;
        if (shipType == ShipType.MINER) {
            ship = MinerShipManager.createNewMinerShip(name, color);
            ship.setUser(station.getUser());
        } else {
            return 0;
        }

        SpaceStationManager stationManager = new SpaceStationManager(station);
        stationManager.addNewShip(ship, shipType);

        ship = spaceShipRepository.save(ship);
        return ship.getId();
    }

    public SpaceStationStorageDTO getStationStorage(long stationId) throws DataNotFoundException {
        SpaceStation station = getStationByIdAndCheckAccess(stationId);

        SpaceStationManager stationManager = new SpaceStationManager(station);
        return stationManager.getStorageDTO();
    }

    public HangarDTO getStationHangar(long stationId) throws DataNotFoundException {
        SpaceStation station = getStationByIdAndCheckAccess(stationId);

        SpaceStationManager stationManager = new SpaceStationManager(station);
        return stationManager.getHangarDTO();
    }

    public Map<ResourceType, Integer> getStoredResources(long stationId) throws DataNotFoundException {
        SpaceStation station = getStationByIdAndCheckAccess(stationId);

        SpaceStationManager stationManager = new SpaceStationManager(station);
        return stationManager.getStoredResources();
    }

    public Map<ResourceType, Integer> getStorageUpgradeCost(long stationId) throws UpgradeNotAvailableException, DataNotFoundException {
        SpaceStation station = getStationByIdAndCheckAccess(stationId);

        SpaceStationManager stationManager = new SpaceStationManager(station);
        return stationManager.getStorageUpgradeCost();
    }

    public boolean upgradeStorage(long stationId) throws UpgradeNotAvailableException, StorageException, DataNotFoundException {
        SpaceStation station = getStationByIdAndCheckAccess(stationId);

        SpaceStationManager stationManager = new SpaceStationManager(station);
        if (stationManager.upgradeStorage()) {
            spaceStationRepository.save(station);
            return true;
        } else {
            return false;
        }
    }

    public Map<ResourceType, Integer> getHangarUpgradeCost(long stationId) throws UpgradeNotAvailableException, DataNotFoundException {
        SpaceStation station = getStationByIdAndCheckAccess(stationId);

        SpaceStationManager stationManager = new SpaceStationManager(station);
        return stationManager.getHangarUpgradeCost();
    }

    public boolean upgradeHangar(long stationId) throws UpgradeNotAvailableException, StorageException, DataNotFoundException {
        SpaceStation station = getStationByIdAndCheckAccess(stationId);

        SpaceStationManager stationManager = new SpaceStationManager(station);
        if (stationManager.upgradeHangar()) {
            spaceStationRepository.save(station);
            return true;
        } else {
            return false;
        }
    }
    public boolean moveResourceFromShipToStation(long stationId, long shipId, Map<ResourceType, Integer> resources) throws DataNotFoundException, StorageException {
        SpaceStation station = getStationByIdAndCheckAccess(stationId);

        SpaceShip ship = station.getHangar().stream().filter(s -> s.getId() == shipId).findFirst()
                .orElseThrow(() -> new StorageException("No such ship on this station"));

        SpaceStationManager stationManager = new SpaceStationManager(station);
        if (stationManager.addResourcesFromShip(ship, resources)) {
            spaceStationRepository.save(station);
            return true;
        }
        return false;
    }

    private SpaceStation getStationByIdAndCheckAccess(long stationId) throws DataNotFoundException {
        SpaceStation station = spaceStationRepository.findById(stationId)
                .orElseThrow(() -> new DataNotFoundException("No station found with id %d".formatted(stationId)));
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
                || Objects.equals(user.getId(), station.getUser().getId())) {
            return station;
        } else {
            throw new SecurityException("You don't have authority to access this station");
        }
    }

}
