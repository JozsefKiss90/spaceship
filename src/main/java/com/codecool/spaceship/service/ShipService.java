package com.codecool.spaceship.service;

import com.codecool.spaceship.model.exception.NoSuchPartException;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.station.SpaceStationService;
import com.codecool.spaceship.model.exception.ShipNotFoundException;
import com.codecool.spaceship.model.dto.ShipDTO;
import com.codecool.spaceship.model.ship.SpaceShipService;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.ship.shipparts.ShipPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ShipService {

    private final SpaceStationService base;

    @Autowired
    public ShipService(SpaceStationService base) {
        this.base = base;
    }


    public Set<ShipDTO> getAllShipsFromBase() {
        return base.getAllShips().stream()
                .map(ShipDTO::new)
                .collect(Collectors.toSet());
    }

    public Optional<ShipDTO> getShipByID(int id) {
        return base.getAllShips().stream()
                .filter(ship -> ship.getId() == id)
                .map(ShipDTO::new)
                .findFirst();
    }

    public boolean upgradeShip(int id, ShipPart part) throws ShipNotFoundException, UpgradeNotAvailableException, NoSuchPartException, StorageException {
        SpaceShipService ship = findShipById(id);
        return base.upgradeShipPart(ship, part);
    }

    public boolean updateShipAttributes(int id, String name, Color color) throws ShipNotFoundException {
        SpaceShipService ship = findShipById(id);
        if (name != null) {
            ship.setName(name);
        }
        if (color != null) {
            ship.setColor(color);
        }
        return true;
    }

    public boolean deleteShip(int id) throws ShipNotFoundException {
        SpaceShipService ship = findShipById(id);
        return base.deleteShip(ship);
    }

    private SpaceShipService findShipById(int id) throws ShipNotFoundException {
        return base.getAllShips().stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElseThrow(ShipNotFoundException::new);
    }
}
