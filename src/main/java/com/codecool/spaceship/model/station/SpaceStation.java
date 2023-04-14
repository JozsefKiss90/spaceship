package com.codecool.spaceship.model.station;

import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.Upgradeable;
import com.codecool.spaceship.model.exception.NoSuchPartException;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.ship.shipparts.ShipPart;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SpaceStation {
    private String name;
    //private User user;
    private final UUID id;
    private final SpaceStationStorage storage;
    private final Hangar hangar;

    public SpaceStation(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
        this.storage = new SpaceStationStorage();
        this.hangar = new Hangar();
    }
    /*
    public void addFirstShip() {
        try {
            hangar.addShip(new MinerShip("Eeny Meeny Miny Moe", Color.EMERALD));
        } catch (StorageException ignored) {
        }
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private boolean hasEnoughResource(Map<Resource, Integer> cost) {
        return cost.entrySet().stream().allMatch(entry -> storage.hasResource(entry.getKey(), entry.getValue()));
    }

    private boolean removeResources(Map<Resource, Integer> cost) throws StorageException {
        if (hasEnoughResource(cost)) {
            for (Resource resource : cost.keySet()) {
                storage.removeResource(resource, cost.get(resource));
            }
            return true;
        }
        throw new StorageException("Not enough resource");
    }

    public boolean addNewShip(SpaceShip ship) throws StorageException {
        Map<Resource, Integer> cost = ship.getCost();
        return hangar.addShip(ship) && removeResources(cost); //throws storage exception if not enough resource or docks
    }

    public boolean deleteShip(SpaceShip ship) throws StorageException {
        return hangar.removeShip(ship);
    }

    public Set<SpaceShip> getAllShips() {
        return new HashSet<>(hangar.getAllShips());
    }

    public boolean upgradeShipPart(SpaceShip ship, ShipPart shipPart) throws NoSuchPartException, UpgradeNotAvailableException, StorageException {
        if (!hangar.getAllShips().contains(ship)) throw new StorageException("No such ship in storage");
        if (!ship.isAvailable()) throw new UpgradeNotAvailableException("Ship is on a mission");
        Upgradeable part = ship.getPart(shipPart);
        Map<Resource, Integer> cost = part.getUpgradeCost();
        removeResources(cost);
        part.upgrade();
        return true;
    }

    public boolean addResource(Resource resource, int quantity) throws StorageException {
        return storage.addResource(resource, quantity);
    }

    public boolean upgradeStorage() throws UpgradeNotAvailableException, StorageException {
        Map<Resource, Integer> cost = storage.getUpgradeCost();
        removeResources(cost);
        storage.upgrade();
        return true;
    }

    public boolean upgradeHangar() throws UpgradeNotAvailableException, StorageException {
        Map<Resource, Integer> cost = hangar.getUpgradeCost();
        removeResources(cost);
        hangar.upgrade();
        return true;
    }
}
