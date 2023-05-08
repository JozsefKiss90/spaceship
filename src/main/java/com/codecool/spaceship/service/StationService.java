package com.codecool.spaceship.service;

import com.codecool.spaceship.model.dto.HangarDTO;
import com.codecool.spaceship.model.dto.SpaceStationDTO;
import com.codecool.spaceship.model.dto.SpaceStationStorageDTO;
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
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class StationService {

    private final SpaceStationRepository spaceStationRepository;

    @Autowired
    public StationService(SpaceStationRepository spaceStationRepository) {

        this.spaceStationRepository = spaceStationRepository;
    }

    public Optional<SpaceStationDTO> getBaseById(long baseId) {
        SpaceStation station = spaceStationRepository.findById(baseId).orElse(null);
        if (station == null) {
            return Optional.empty();
        }

        SpaceStationManager stationManager = new SpaceStationManager(station);
        return Optional.of(stationManager.getStationDTO());
    }

    public boolean addResource(long baseId, ResourceType resource, int quantity) throws StorageException {
        SpaceStation station = spaceStationRepository.findById(baseId).orElse(null);
        if (station == null) {
            return false;
        }
        SpaceStationManager stationManager = new SpaceStationManager(station);
        stationManager.addResource(resource, quantity);
        spaceStationRepository.save(station);
        return true;
    }

    public boolean addShip(long baseId, String name, Color color, ShipType shipType) throws StorageException {
        SpaceStation station = spaceStationRepository.findById(baseId).orElse(null);
        if (station == null) {
            return false;
        }

        SpaceShip ship;
        if (shipType == ShipType.MINER) {
            ship = MinerShipManager.createNewMinerShip(name, color);
        } else {
            return false;
        }

        SpaceStationManager stationManager = new SpaceStationManager(station);
        stationManager.addNewShip(ship, shipType);

        spaceStationRepository.save(station);
        return true;
    }

    public SpaceStationStorageDTO getStationStorage(long baseId) {
        SpaceStation station = spaceStationRepository.findById(baseId).orElse(null);
        if (station == null) {
            return null;
        }

        SpaceStationManager stationManager = new SpaceStationManager(station);
        return stationManager.getStorageDTO();
    }

    public HangarDTO getStationHangar(long baseId) {
        SpaceStation station = spaceStationRepository.findById(baseId).orElse(null);
        if (station == null) {
            return null;
        }

        SpaceStationManager stationManager = new SpaceStationManager(station);
        return stationManager.getHangarDTO();
    }

    public Map<ResourceType, Integer>  getStoredResources(long baseId) {
        SpaceStation station = spaceStationRepository.findById(baseId).orElse(null);
        if (station == null) {
            return null;
        }

        SpaceStationManager stationManager = new SpaceStationManager(station);
        return stationManager.getStoredResources();
    }

    public Map<ResourceType, Integer> getStorageUpgradeCost(long baseId) throws UpgradeNotAvailableException {
        SpaceStation station = spaceStationRepository.findById(baseId).orElse(null);
        if (station == null) {
            return null;
        }

        SpaceStationManager stationManager = new SpaceStationManager(station);
        return stationManager.getStorageUpgradeCost();
    }

    public boolean upgradeStorage(long baseId) throws UpgradeNotAvailableException, StorageException {
        SpaceStation station = spaceStationRepository.findById(baseId).orElse(null);
        if (station == null) {
            return false;
        }

        SpaceStationManager stationManager = new SpaceStationManager(station);
        if(stationManager.upgradeStorage()) {
            spaceStationRepository.save(station);
            return true;
        } else {
            return false;
        }
    }

    public Map<ResourceType, Integer> getHangarUpgradeCost(long baseId) throws UpgradeNotAvailableException {
        SpaceStation station = spaceStationRepository.findById(baseId).orElse(null);
        if (station == null) {
            return null;
        }

        SpaceStationManager stationManager = new SpaceStationManager(station);
        return stationManager.getHangarUpgradeCost();
    }

    public boolean upgradeHangar(long baseId) throws UpgradeNotAvailableException, StorageException {
        SpaceStation station = spaceStationRepository.findById(baseId).orElse(null);
        if (station == null) {
            return false;
        }

        SpaceStationManager stationManager = new SpaceStationManager(station);
        if(stationManager.upgradeHangar()) {
            spaceStationRepository.save(station);
            return true;
        } else {
            return false;
        }
    }
}
