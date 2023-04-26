package com.codecool.spaceship.service;

import com.codecool.spaceship.model.dto.ShipDTO;
import com.codecool.spaceship.model.station.SpaceStationService;
import com.codecool.spaceship.repository.MinerShipRepository;
import com.codecool.spaceship.repository.SpaceShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShipService {
    private final SpaceStationService base;
    private MinerShipRepository minerShipRepository;
    private SpaceShipRepository spaceShipRepository;
    @Autowired
    public ShipService(SpaceStationService base, MinerShipRepository minerShipRepository, SpaceShipRepository spaceShipRepository) {
        this.base = base;
        this.minerShipRepository = minerShipRepository;
        this.spaceShipRepository = spaceShipRepository;
    }

    public List<ShipDTO> getShips() {
        return minerShipRepository.findAll().stream()
                .map(ShipDTO::new)
                .collect(Collectors.toList());
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
