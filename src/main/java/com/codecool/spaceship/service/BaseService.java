package com.codecool.spaceship.service;

import com.codecool.spaceship.model.dto.SpaceStationDTO;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.station.SpaceStation;
import com.codecool.spaceship.model.station.SpaceStationManager;
import com.codecool.spaceship.repository.SpaceStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BaseService {

    private final SpaceStationRepository spaceStationRepository;
    @Autowired
    public BaseService(SpaceStationRepository spaceStationRepository) {

        this.spaceStationRepository = spaceStationRepository;
    }

    public Optional<SpaceStationDTO> getBase() {
        return spaceStationRepository.findAll()
                .stream().map(SpaceStationDTO::new).findFirst();
    }
//
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
//
//    public boolean addShip(SpaceShipService ship) throws StorageException {
//        base.addNewShip(ship);
//        return true;
//    }
//
//    public boolean upgradeStorage() throws UpgradeNotAvailableException, StorageException {
//        base.upgradeStorage();
//        return true;
//    }
//
//    public boolean upgradeHangar() throws UpgradeNotAvailableException, StorageException {
//        base.upgradeHangar();
//        return true;
//    }

}
