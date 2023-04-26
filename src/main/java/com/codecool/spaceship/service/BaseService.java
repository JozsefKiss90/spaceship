package com.codecool.spaceship.service;

import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.dto.SpaceStationDTO;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.station.SpaceStation;
import com.codecool.spaceship.model.station.SpaceStationService;
import com.codecool.spaceship.model.ship.SpaceShipService;
import com.codecool.spaceship.repository.SpaceShipRepository;
import com.codecool.spaceship.repository.SpaceStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BaseService {

    private final SpaceStationService base;
    private final SpaceStationRepository spaceStationRepository;
    @Autowired
    public BaseService(SpaceStationService base, SpaceStationRepository spaceStationRepository) {
        this.base = base;
        this.spaceStationRepository = spaceStationRepository;
    }

    public Optional<SpaceStationDTO> getBase() {
        return spaceStationRepository.findAll()
                .stream().map(SpaceStationDTO::new).findFirst();
    }

    public boolean addResource(Resource resource, int quantity) throws StorageException {
        base.addResource(resource, quantity);
        return true;
    }

    public boolean addShip(SpaceShipService ship) throws StorageException {
        base.addNewShip(ship);
        return true;
    }

    public boolean upgradeStorage() throws UpgradeNotAvailableException, StorageException {
        base.upgradeStorage();
        return true;
    }

    public boolean upgradeHangar() throws UpgradeNotAvailableException, StorageException {
        base.upgradeHangar();
        return true;
    }

}
