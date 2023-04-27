package com.codecool.spaceship.model.ship;

import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.exception.NoSuchPartException;
import com.codecool.spaceship.model.Upgradeable;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.ship.shipparts.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinerShipManager extends SpaceShipManager {

    private static final List<ShipPart> PARTS = List.of(ShipPart.ENGINE, ShipPart.SHIELD, ShipPart.DRILL, ShipPart.STORAGE);
    private static final Map<ResourceType, Integer> COST = new HashMap<>() {{
        put(ResourceType.METAL, 50);
        put(ResourceType.CRYSTAL, 20);
        put(ResourceType.SILICONE, 20);
    }};

    public MinerShipManager(MinerShip minerShip) {
        super(minerShip);
    }

    public int getDrillEfficiency() {
        DrillManager drill = new DrillManager(((MinerShip) spaceShip).getDrillLevel());
        return drill.getEfficiency();
    }

    public int getMaxStorageCapacity() {
        ShipStorageManager storage = new ShipStorageManager(((MinerShip) spaceShip).getStorageLevel(), ((MinerShip) spaceShip).getResources());
        return storage.getMaxCapacity();
    }

    public int getEmptyStorageSpace() {
        ShipStorageManager storage = new ShipStorageManager(((MinerShip) spaceShip).getStorageLevel(), ((MinerShip) spaceShip).getResources());
        return storage.getEmptySpace();
    }

    public Map<ResourceType, Integer> getStorageContents() {
        ShipStorageManager storage = new ShipStorageManager(((MinerShip) spaceShip).getStorageLevel(), ((MinerShip) spaceShip).getResources());
        return storage.getStoredResources();
    }

    public boolean addResourceToStorage(ResourceType resourceType, int amount) throws StorageException {
        ShipStorageManager storage = new ShipStorageManager(((MinerShip) spaceShip).getStorageLevel(), ((MinerShip) spaceShip).getResources());
        return storage.addResource(resourceType, amount);
    }

    public boolean removeResourceFromStorage(ResourceType resourceType, int amount) throws StorageException {
        ShipStorageManager storage = new ShipStorageManager(((MinerShip) spaceShip).getStorageLevel(), ((MinerShip) spaceShip).getResources());
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
                return new EngineManager(spaceShip.getEngineLevel());
            }
            case SHIELD -> {
                return new ShieldManager(spaceShip.getShieldLevel(), spaceShip.getShieldEnergy());
            }
            case DRILL -> {
                return new DrillManager(((MinerShip) spaceShip).getDrillLevel());
            }
            case STORAGE -> {
                return new ShipStorageManager(((MinerShip) spaceShip).getStorageLevel(), ((MinerShip) spaceShip).getResources());
            }
            default -> throw new NoSuchPartException("No such part on this ship");
        }
    }

    @Override
    public Map<ResourceType, Integer> getCost() {
        return new HashMap<>(COST);
    }
}
