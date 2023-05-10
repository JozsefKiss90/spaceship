package com.codecool.spaceship.service;

import com.codecool.spaceship.model.dto.MinerShipDTO;
import com.codecool.spaceship.model.dto.ShipDTO;
import com.codecool.spaceship.model.exception.*;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.MinerShipManager;
import com.codecool.spaceship.model.ship.ShipType;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.ship.shipparts.ShipPart;
import com.codecool.spaceship.model.station.SpaceStationManager;
import com.codecool.spaceship.repository.SpaceShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ShipService {
    private final SpaceShipRepository spaceShipRepository;

    @Autowired
    public ShipService(SpaceShipRepository spaceShipRepository) {
        this.spaceShipRepository = spaceShipRepository;
    }

    public List<ShipDTO> getShipsByStation(long stationId) {
        return spaceShipRepository.getSpaceShipsByStationId(stationId).stream()
                .map(ShipDTO::new)
                .collect(Collectors.toList());
    }

    public ShipDTO getShipByID(long id) throws DataNotFoundException {
        return spaceShipRepository.findById(id)
                .map(ShipDTO::new)
                .orElseThrow(() -> new DataNotFoundException("No ship found with id %d".formatted(id)));
    }

    public MinerShipDTO getMinerShipById(long id) throws DataNotFoundException, IllegalArgumentException {
        SpaceShip ship = spaceShipRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("No ship found with id %d".formatted(id)));
        if (ship instanceof MinerShip) {
            return new MinerShipDTO((MinerShip) ship);
        } else {
            throw new IllegalArgumentException("Ship is not a miner ship.");
        }
    }

    public MinerShipDTO upgradeMinerShip(Long id, ShipPart part) throws DataNotFoundException, UpgradeNotAvailableException, NoSuchPartException, StorageException {
        SpaceShip ship = spaceShipRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("No ship found with id %d".formatted(id)));

        if (ship instanceof MinerShip) {
            MinerShipManager minerShipManager = new MinerShipManager((MinerShip) ship);

            SpaceStationManager stationManager = new SpaceStationManager(ship.getStation());
            if (stationManager.hasShipAvailable(ship)) {
                stationManager.removeResources(minerShipManager.getUpgradeCost(part));
                minerShipManager.upgradePart(part);
                ship = spaceShipRepository.save(ship);
            }
            return new MinerShipDTO((MinerShip) ship);
        } else {
            throw new IllegalArgumentException("Ship is not a miner ship");
        }
    }

    public ShipDTO updateShipAttributes(Long id, String name, Color color) throws DataNotFoundException {
        SpaceShip ship = spaceShipRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("No ship found with id %d".formatted(id)));
        if (name != null && !name.equals("")) {
            ship.setName(name);
        }
        if (color != null) {
            ship.setColor(color);
        }
        ship = spaceShipRepository.save(ship);
        return new ShipDTO(ship);
    }

    public Map<ResourceType, Integer> getShipCost(ShipType shipType) {
        if (shipType != null) {
            return shipType.getCost();
        } else {
            return null;
        }
    }

    public boolean deleteShipById(Long id) throws DataNotFoundException, IllegalOperationException {
        SpaceShip ship = spaceShipRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("No ship found with id %d".formatted(id)));
        if (ship.getCurrentMission() != null) {
            throw new IllegalOperationException("Ship can't be deleted while on mission");
        }
        spaceShipRepository.delete(ship);
        return true;
    }

}
