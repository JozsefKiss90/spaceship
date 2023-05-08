package com.codecool.spaceship.model.station;

import com.codecool.spaceship.model.dto.HangarDTO;
import com.codecool.spaceship.model.dto.SpaceStationDTO;
import com.codecool.spaceship.model.dto.SpaceStationStorageDTO;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.ship.ShipType;
import com.codecool.spaceship.model.ship.SpaceShip;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SpaceStationManager {

    private final SpaceStation station;

    public SpaceStationManager(SpaceStation station) {
        this.station = station;
    }

    public static SpaceStation createNewSpaceStation(String name) {
        SpaceStation station = new SpaceStation();
        station.setName(name);
        station.setHangarLevel(1);
        station.setHangar(new HashSet<>());
        station.setStorageLevel(1);
        station.setResources(new HashSet<>());
        return station;
    }

    private boolean hasEnoughResource(Map<ResourceType, Integer> cost) {
        StationStorageManager storage = new StationStorageManager(station.getStorageLevel(), station.getResources());
        return cost.entrySet().stream()
                .allMatch(entry -> storage.hasResource(entry.getKey(), entry.getValue()));
    }

    private boolean removeResources(Map<ResourceType, Integer> cost) throws StorageException {
        StationStorageManager storage = new StationStorageManager(station.getStorageLevel(), station.getResources());
        if (hasEnoughResource(cost)) {
            for (ResourceType resource : cost.keySet()) {
                storage.removeResource(resource, cost.get(resource));
            }
            return true;
        }
        throw new StorageException("Not enough resource");
    }

    public boolean addNewShip(SpaceShip ship, ShipType shipType) throws StorageException {
        Map<ResourceType, Integer> cost = shipType.getCost();
        HangarManager hangar = new HangarManager(station.getHangarLevel(), station.getHangar());
        if (!hasEnoughResource(cost)) {
            throw new StorageException("Not enough resource");
        } else if (hangar.getCurrentAvailableDocks() == 0) {
            throw new StorageException("No more docks available");
        } else {
            return hangar.addShip(ship) && removeResources(cost); //throws storage exception if not enough resource or docks
        }
    }

    public boolean removeShip(SpaceShip ship) {
        HangarManager hangar = new HangarManager(station.getHangarLevel(), station.getHangar());
        return hangar.removeShip(ship);
    }

    public Set<SpaceShip> getAllShips() {
        return new HashSet<>(station.getHangar());
    }

    public boolean removeShipUpgradeResourcesFromStation(SpaceShip ship, Map<ResourceType, Integer> cost) throws StorageException {
        HangarManager hangar = new HangarManager(station.getHangarLevel(), station.getHangar());
        if (!hangar.getAllShips().contains(ship)) throw new StorageException("No such ship in storage");
        removeResources(cost);
        return true;
    }

    //
    public boolean addResource(ResourceType resourceType, int quantity) throws StorageException {
        StationStorageManager storage = new StationStorageManager(station.getStorageLevel(), station.getResources());
        return storage.addResource(resourceType, quantity);
    }

    public Map<ResourceType, Integer> getStorageUpgradeCost() throws UpgradeNotAvailableException {
        StationStorageManager storage = new StationStorageManager(station.getStorageLevel(), station.getResources());
        return storage.getUpgradeCost();
    }

    public Map<ResourceType, Integer> getHangarUpgradeCost() throws UpgradeNotAvailableException {
        HangarManager hangar = new HangarManager(station.getHangarLevel(), station.getHangar());
        return hangar.getUpgradeCost();
    }

    public boolean upgradeStorage() throws UpgradeNotAvailableException, StorageException {
        StationStorageManager storage = new StationStorageManager(station.getStorageLevel(), station.getResources());
        Map<ResourceType, Integer> cost = storage.getUpgradeCost();
        removeResources(cost);
        storage.upgrade();
        station.setStorageLevel(storage.getCurrentLevel());
        return true;
    }

    public boolean upgradeHangar() throws UpgradeNotAvailableException, StorageException {
        HangarManager hangar = new HangarManager(station.getHangarLevel(), station.getHangar());
        Map<ResourceType, Integer> cost = hangar.getUpgradeCost();
        removeResources(cost);
        hangar.upgrade();
        station.setHangarLevel(hangar.getCurrentLevel());
        return true;
    }

    public SpaceStationDTO getStationDTO() {
        return new SpaceStationDTO(station.getId(), station.getName(), getHangarDTO(), getStorageDTO());
    }

    public SpaceStationStorageDTO getStorageDTO() {
        return new SpaceStationStorageDTO(new StationStorageManager(station.getStorageLevel(), station.getResources()));
    }

    public HangarDTO getHangarDTO() {
        return new HangarDTO(new HangarManager(station.getHangarLevel(), station.getHangar()));
    }

    public Map<ResourceType, Integer> getStoredResources() {
        StationStorageManager storage = new StationStorageManager(station.getStorageLevel(), station.getResources());
        return storage.getStoredItems();
    }
}
