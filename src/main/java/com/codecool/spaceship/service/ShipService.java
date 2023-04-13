package com.codecool.spaceship.service;

import com.codecool.spaceship.model.base.Base;
import com.codecool.spaceship.model.exception.ShipNotFoundException;
import com.codecool.spaceship.model.dto.ShipDTO;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.ship.shipparts.ShipPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ShipService {

    private final Base base;

    @Autowired
    public ShipService(Base base) {
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

    public boolean upgradeShip(int id, ShipPart part) throws ShipNotFoundException {
        SpaceShip ship = findShipById(id);
        return base.upgradeShipPart(ship, part);
    }

    public boolean updateShipAttributes(int id, String name, Color color) throws ShipNotFoundException {
        SpaceShip ship = findShipById(id);
        if (name != null) {
            ship.setName(name);
        }
        if (color != null) {
            ship.setColor(color);
        }
        return true;
    }

    public boolean deleteShip(int id) throws ShipNotFoundException {
        SpaceShip ship = findShipById(id);
        return base.deleteShip(ship);
    }

    private SpaceShip findShipById(int id) throws ShipNotFoundException {
        return base.getAllShips().stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElseThrow(ShipNotFoundException::new);
    }
}
