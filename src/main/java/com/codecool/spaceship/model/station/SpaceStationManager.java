package com.codecool.spaceship.model.station;

import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.resource.StationResource;

import java.util.Map;
import java.util.Set;

public class SpaceStationManager {

    /*
    public void addFirstShip() {
        try {
            hangar.addShip(new MinerShip("Eeny Meeny Miny Moe", Color.EMERALD));
        } catch (StorageException ignored) {
        }
    }*/
    private boolean hasEnoughResource(Set<StationResource> stationStorage, Map<ResourceType, Integer> cost) {
        for (ResourceType resourceType : cost.keySet()) {
            StationResource stationResource = stationStorage.stream()
                    .filter(sr -> sr.getResourceType() == resourceType)
                    .findFirst()
                    .orElse(null);
            if (stationResource == null || stationResource.getQuantity() < cost.get(resourceType)) {
                return false;
            }
        }
        return true;
    }

    private boolean removeResources(Set<StationResource> stationStorage, Map<ResourceType, Integer> cost) throws StorageException {
        if (hasEnoughResource(stationStorage, cost)) {
            for (ResourceType resourceType : cost.keySet()) {
                StationResource stationResource = stationStorage.stream()
                        .filter(sr -> sr.getResourceType() == resourceType)
                        .findFirst()
                        .orElse(null);
                stationResource.setQuantity(stationResource.getQuantity() - cost.get(resourceType));
            }
            return true;
        }
        throw new StorageException("Not enough resource");
    }

//    public boolean addNewShip(SpaceShipService ship) throws StorageException {
//        Map<ResourceType, Integer> cost = ship.getCost();
//        return hangarService.addShip(ship) && removeResources(cost); //throws storage exception if not enough resource or docks
//    }
//
//    public boolean deleteShip(SpaceShipService ship){
//        return hangarService.removeShip(ship);
//    }
//
//    public Set<SpaceShipService> getAllShips() {
//        return new HashSet<>(hangarService.getAllShips());
//    }
//
//    public boolean upgradeShipPart(SpaceShipService ship, ShipPart shipPart) throws NoSuchPartException, UpgradeNotAvailableException, StorageException {
//        if (!hangarService.getAllShips().contains(ship)) throw new StorageException("No such ship in storage");
//        if (!ship.isAvailable()) throw new UpgradeNotAvailableException("Ship is on a mission");
//        Upgradeable part = ship.getPart(shipPart);
//        Map<ResourceType, Integer> cost = part.getUpgradeCost();
//        removeResources(cost);
//        part.upgrade();
//        return true;
//    }
//
//    public boolean addResource(ResourceType resourceType, int quantity) throws StorageException {
//        return storage.addResource(resourceType, quantity);
//    }
//
//    public boolean upgradeStorage() throws UpgradeNotAvailableException, StorageException {
//        Map<ResourceType, Integer> cost = storage.getUpgradeCost();
//        removeResources(cost);
//        storage.upgrade();
//        return true;
//    }
//
//    public boolean upgradeHangar() throws UpgradeNotAvailableException, StorageException {
//        Map<ResourceType, Integer> cost = hangarService.getUpgradeCost();
//        removeResources(cost);
//        hangarService.upgrade();
//        return true;
//    }
}
