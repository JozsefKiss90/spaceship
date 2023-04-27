package com.codecool.spaceship.service;

import com.codecool.spaceship.model.dto.SpaceStationDTO;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.ShipType;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.model.ship.shipparts.Color;
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

    public boolean addShip(long baseId, String name, Color color, ShipType shipType) throws StorageException {
        SpaceStation station = spaceStationRepository.findById(baseId).orElse(null);
        if (station == null) {
            return false;
        }

        SpaceShip ship;
        if (shipType == ShipType.MINER) {
            ship = new MinerShip(name, color);
        } else {
            return false;
        }

        SpaceStationManager stationManager = new SpaceStationManager(station);
        stationManager.addNewShip(ship, shipType);

        spaceStationRepository.save(station);
        return true;
    }
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
