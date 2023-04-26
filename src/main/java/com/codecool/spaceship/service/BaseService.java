package com.codecool.spaceship.service;

import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.station.SpaceStationService;
import com.codecool.spaceship.model.ship.SpaceShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseService {

    private final SpaceStationService base;
    @Autowired
    public BaseService(SpaceStationService base) {
        this.base = base;
    }

    public SpaceStationService getBase() {
        return base;
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
