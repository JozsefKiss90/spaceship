package com.codecool.spaceship.service;

import com.codecool.spaceship.model.UserEntity;
import com.codecool.spaceship.model.dto.HangarDTO;
import com.codecool.spaceship.model.dto.SpaceStationDTO;
import com.codecool.spaceship.model.dto.SpaceStationStorageDTO;
import com.codecool.spaceship.model.exception.ShipNotFoundException;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.MinerShipManager;
import com.codecool.spaceship.model.ship.ShipType;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.station.SpaceStation;
import com.codecool.spaceship.model.station.SpaceStationManager;
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

    @Autowired
    public StationService(SpaceStationRepository spaceStationRepository) {
        this.spaceStationRepository = spaceStationRepository;
    }

    public SpaceStationDTO getBaseById(long stationId) throws ShipNotFoundException {
        SpaceStation station = getStationByIdAndCheckAccess(stationId);

        SpaceStationManager stationManager = new SpaceStationManager(station);
        return stationManager.getStationDTO();
    }

    public boolean addResources(long stationId, Map<ResourceType, Integer> resources) throws StorageException, ShipNotFoundException {
        SpaceStation station = getStationByIdAndCheckAccess(stationId);

        SpaceStationManager stationManager = new SpaceStationManager(station);
        for (ResourceType resource : resources.keySet()) {
            stationManager.addResource(resource, resources.get(resource));
        }
        spaceStationRepository.save(station);
        return true;
    }

    public boolean addShip(long stationId, String name, Color color, ShipType shipType) throws StorageException, ShipNotFoundException {
        SpaceStation station = getStationByIdAndCheckAccess(stationId);

        SpaceShip ship;
        if (shipType == ShipType.MINER) {
            ship = MinerShipManager.createNewMinerShip(name, color);
            ship.setUser(station.getUser());
        } else {
            return false;
        }

        SpaceStationManager stationManager = new SpaceStationManager(station);
        stationManager.addNewShip(ship, shipType);

        spaceStationRepository.save(station);
        return true;
    }

    public SpaceStationStorageDTO getStationStorage(long stationId) throws ShipNotFoundException {
        SpaceStation station = getStationByIdAndCheckAccess(stationId);

        SpaceStationManager stationManager = new SpaceStationManager(station);
        return stationManager.getStorageDTO();
    }

    public HangarDTO getStationHangar(long stationId) throws ShipNotFoundException {
        SpaceStation station = getStationByIdAndCheckAccess(stationId);

        SpaceStationManager stationManager = new SpaceStationManager(station);
        return stationManager.getHangarDTO();
    }

    public Map<ResourceType, Integer> getStoredResources(long stationId) throws ShipNotFoundException {
        SpaceStation station = getStationByIdAndCheckAccess(stationId);

        SpaceStationManager stationManager = new SpaceStationManager(station);
        return stationManager.getStoredResources();
    }

    public Map<ResourceType, Integer> getStorageUpgradeCost(long stationId) throws UpgradeNotAvailableException, ShipNotFoundException {
        SpaceStation station = getStationByIdAndCheckAccess(stationId);

        SpaceStationManager stationManager = new SpaceStationManager(station);
        return stationManager.getStorageUpgradeCost();
    }

    public boolean upgradeStorage(long stationId) throws UpgradeNotAvailableException, StorageException, ShipNotFoundException {
        SpaceStation station = getStationByIdAndCheckAccess(stationId);

        SpaceStationManager stationManager = new SpaceStationManager(station);
        if (stationManager.upgradeStorage()) {
            spaceStationRepository.save(station);
            return true;
        } else {
            return false;
        }
    }

    public Map<ResourceType, Integer> getHangarUpgradeCost(long stationId) throws UpgradeNotAvailableException, ShipNotFoundException {
        SpaceStation station = getStationByIdAndCheckAccess(stationId);

        SpaceStationManager stationManager = new SpaceStationManager(station);
        return stationManager.getHangarUpgradeCost();
    }

    public boolean upgradeHangar(long stationId) throws UpgradeNotAvailableException, StorageException, ShipNotFoundException {
        SpaceStation station = getStationByIdAndCheckAccess(stationId);

        SpaceStationManager stationManager = new SpaceStationManager(station);
        if (stationManager.upgradeHangar()) {
            spaceStationRepository.save(station);
            return true;
        } else {
            return false;
        }
    }

    private SpaceStation getStationByIdAndCheckAccess(long stationId) throws ShipNotFoundException {
        SpaceStation station = spaceStationRepository.findById(stationId).orElseThrow(() -> new ShipNotFoundException("No station found with id %d".formatted(stationId)));
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
                || Objects.equals(user.getId(), station.getUser().getId())) {
            return station;
        } else {
            throw new SecurityException("You don't have authority to access this station");
        }
    }
}
