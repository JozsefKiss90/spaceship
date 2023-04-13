package com.codecool.spaceship.model.base;

import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.Upgradeable;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Base {
    private String name;
    //private User user;
    private final UUID id;
    private final BaseStorage storage;
    private final Hangar hangar;

    public Base(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
        this.storage = new BaseStorage();
        this.hangar = new Hangar();
        addFirstShip();
    }

    private void addFirstShip() {
        try {
            hangar.addShip(new MinerShip("Eeny Meeny Miny Moe", Color.EMERALD));
        } catch (Exception ignored) {
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private boolean hasEnoughResource(Map<Resource, Integer> cost) {
        return cost.entrySet().stream().allMatch(entry -> storage.hasResource(entry.getKey(), entry.getValue()));
    }

    private void removeResources(Map<Resource, Integer> cost) {
        cost.forEach((key, value) -> {
            try {
                storage.removeResource(key, value);
            } catch (StorageException e) {
                //ToDo handleException
            }
        });

    }

    public boolean addNewShip(SpaceShip ship) {
        boolean hasEnoughDocks = hangar.getCurrentAvailableDocks() > 0;
        Map<Resource, Integer> cost = ship.getCost();
        boolean hasEnoughResource = hasEnoughResource(cost);
        if (hasEnoughDocks && hasEnoughResource) {
            removeResources(cost);
            try {
                hangar.addShip(ship);
            } catch (Exception ignored) {
            }
        }
        return hasEnoughDocks && hasEnoughResource;
    }

    public boolean deleteShip(SpaceShip ship) {
        try {
            hangar.removeShip(ship);
            return true;
        } catch (Exception e) {
            //ToDo write error to screen
            return false;
        }
    }

    public Set<SpaceShip> getAllShips() {
        return new HashSet<>(hangar.getAllShips());
    }

    public Set<MinerShip> getAvailableMinerShips() {
        return hangar.getMinerShips().stream().filter(SpaceShip::isAvailable).collect(Collectors.toSet());
    }

    public boolean upgradeShipPart(SpaceShip ship, ShipPart shipPart) {
        try {
            Upgradeable part = ship.getPart(shipPart);
            Map<Resource, Integer> cost = part.getUpgradeCost();
            boolean hasEnoughResource = hasEnoughResource(cost);
            if (hasEnoughResource) {
                removeResources(cost);
                part.upgrade();
                return true;
            } else return false;
        } catch (Exception e) {
            //ToDo write error to console
            return false;
        }
    }

    public boolean addResource(Resource resource, int quantity) {
        try {
            storage.addResource(resource, quantity);
            return true;
        } catch (Exception e) {
            //ToDo write error to screen
            return false;
        }
    }

    public boolean upgradeStorage() {
        try {
            Map<Resource,Integer> cost = storage.getUpgradeCost();
            boolean hasEnoughResource = hasEnoughResource(cost);
            if (hasEnoughResource) {
                removeResources(cost);
                storage.upgrade();
                return true;
            } else return false;
        } catch (Exception e) {
            //Todo
            return false;
        }
    }

    public boolean upgradeHangar() {
        try {
            Map<Resource,Integer> cost = hangar.getUpgradeCost();
            boolean hasEnoughResource = hasEnoughResource(cost);
            if (hasEnoughResource) {
                removeResources(cost);
                hangar.upgrade();
                return true;
            } else return false;
        } catch (Exception e) {
            return false;
        }
    }

}
