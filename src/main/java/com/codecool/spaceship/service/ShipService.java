package com.codecool.spaceship.service;

import com.codecool.spaceship.model.dto.ShipDTO;
import com.codecool.spaceship.model.exception.NoSuchPartException;
import com.codecool.spaceship.model.exception.ShipNotFoundException;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.ship.shipparts.ShipPart;
import com.codecool.spaceship.repository.MinerShipRepository;
import com.codecool.spaceship.repository.SpaceShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShipService {

    private MinerShipRepository minerShipRepository;
    private SpaceShipRepository spaceShipRepository;
    @Autowired
    public ShipService( MinerShipRepository minerShipRepository, SpaceShipRepository spaceShipRepository) {

        this.minerShipRepository = minerShipRepository;
        this.spaceShipRepository = spaceShipRepository;
    }

    public List<ShipDTO> getShips() {
        return spaceShipRepository.findAll().stream()
                .map(ShipDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<ShipDTO> getShipByID(long id) {

        return spaceShipRepository.findById(id).stream()
                .filter(ship -> ship.getId() == id)
                .map(ShipDTO::new)
                .findFirst();
    }

    public SpaceShip upgradeShip(Long id, ShipPart part) throws ShipNotFoundException, UpgradeNotAvailableException, NoSuchPartException, StorageException {
        SpaceShip ship = spaceShipRepository.findById(id).get();
        switch (part) {
            case ENGINE :
                ship.setEngineLevel(ship.getEngineLevel() + 1);
                spaceShipRepository.save(ship);
                break;
            case SHIELD:
                ship.setShieldLevel(ship.getShieldLevel() + 1);
                spaceShipRepository.save(ship);
                break;
        }
        return ship;
    }

    public boolean updateShipAttributes(Long id, String name, Color color) throws ShipNotFoundException {
        SpaceShip ship = spaceShipRepository.findById(id).get();
        if (name != null) {
            ship.setName(name);
            spaceShipRepository.save(ship);
        }
        if (color != null) {
            ship.setColor(color);
            spaceShipRepository.save(ship);
        }
        return true;
    }

    /*
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
 */
}
