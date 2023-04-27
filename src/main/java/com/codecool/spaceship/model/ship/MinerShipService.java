package com.codecool.spaceship.model.ship;

import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.exception.NoSuchPartException;
import com.codecool.spaceship.model.Upgradeable;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.ship.shipparts.Drill;
import com.codecool.spaceship.model.ship.shipparts.ShipPart;
import com.codecool.spaceship.model.ship.shipparts.ShipStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinerShipService extends SpaceShipService {

    private static final List<ShipPart> PARTS = List.of(ShipPart.ENGINE, ShipPart.SHIELD, ShipPart.DRILL, ShipPart.STORAGE);
    private static final Map<ResourceType, Integer> COST = new HashMap<>() {{
        put(ResourceType.METAL, 50);
        put(ResourceType.CRYSTAL, 20);
        put(ResourceType.SILICONE, 20);
    }};
    private Drill drill;

    private final ShipStorage storage = new ShipStorage();;

    public MinerShipService(String name, Color color) {
        super(name, color);
        drill = new Drill();
    }

    public int getDrillEfficiency() {
        return drill.getEfficiency();
    }

    public int getMaxStorageCapacity() {
        return storage.getMaxCapacity();
    }

    public int getEmptyStorageSpace() {
        return storage.getEmptySpace();
    }
    public Map<ResourceType, Integer> getStorageContents() {
        return storage.getStoredResources();
    }

    public boolean addResourceToStorage(ResourceType resourceType, int amount) throws StorageException {
        return storage.addResource(resourceType, amount);
    }

    public boolean removeResourceFromStorage(ResourceType resourceType, int amount) throws StorageException {
        return storage.removeResource(resourceType, amount);
    }
    @Override
    public List<ShipPart> getPartTypes() {
        return PARTS;
    }

    @Override
    public Upgradeable getPart(ShipPart part) throws NoSuchPartException {
        switch (part) {
            case ENGINE -> {
                return engine;
            }
            case SHIELD -> {
                return shield;
            }
            case DRILL -> {
                return drill;
            }
            case STORAGE -> {
                return storage;
            }
            default -> throw new NoSuchPartException("No such part on this ship");
        }
    }

    @Override
    public Map<ResourceType, Integer> getCost() {
        return new HashMap<>(COST);
    }
}
