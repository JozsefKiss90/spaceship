package com.codecool.spaceship.model.ship;

import com.codecool.spaceship.model.dto.MinerShipDTO;
import com.codecool.spaceship.model.exception.NoSuchPartException;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.resource.ShipResource;
import com.codecool.spaceship.model.ship.shipparts.*;

import java.util.*;

public class MinerShipManager extends SpaceShipManager {

    private static final List<ShipPart> PARTS = List.of(ShipPart.ENGINE, ShipPart.SHIELD, ShipPart.DRILL, ShipPart.STORAGE);
    private static final Map<ResourceType, Integer> COST = new HashMap<>() {{
        put(ResourceType.METAL, 50);
        put(ResourceType.CRYSTAL, 20);
        put(ResourceType.SILICONE, 20);
    }};

    public MinerShip minership;

    public MinerShipManager(MinerShip minerShip) {
        super(minerShip);
        this.minership = minerShip;
    }

    public static MinerShip createNewMinerShip(String name, Color color) {
        MinerShip ship = new MinerShip();
        ship.setName(name);
        ship.setColor(color);
        ship.setEngineLevel(1);
        ship.setShieldLevel(1);
        ship.setShieldEnergy(new ShieldManager(1, 0).getMaxEnergy());
        ship.setDrillLevel(1);
        ship.setStorageLevel(1);
        ship.setResources(new HashSet<>());
        return ship;
    }

    public MinerShipDTO getMinerShipDTO() {
        return new MinerShipDTO(
                minership.getId(),
                minership.getName(),
                minership.getColor(),
                isAvailable() ? "In dock" : "On mission",
                minership.getEngineLevel(),
                getSpeed(),
                minership.getShieldLevel(),
                getShieldEnergy(),
                getShieldMaxEnergy(),
                minership.getDrillLevel(),
                getDrillEfficiency(),
                minership.getStorageLevel(),
                getMaxStorageCapacity(),
                getMaxStorageCapacity()-getEmptyStorageSpace(),
                getStorageContents()

        );
    }

    public int getDrillEfficiency() {
        DrillManager drill = new DrillManager(((MinerShip) spaceShip).getDrillLevel());
        return drill.getEfficiency();
    }

    public int getMaxStorageCapacity() {
        ShipStorageManager storage = new ShipStorageManager(((MinerShip) spaceShip).getStorageLevel(), ((MinerShip) spaceShip).getResources());
        return storage.getMaxCapacity();
    }

    public double getSpeed() {
        EngineManager engine = new EngineManager(((MinerShip) spaceShip).getEngineLevel());
        return engine.getSpeed();
    }

    public int getEmptyStorageSpace() {
        ShipStorageManager storage = new ShipStorageManager(((MinerShip) spaceShip).getStorageLevel(), ((MinerShip) spaceShip).getResources());
        return storage.getEmptySpace();
    }

    public Set<ShipResource> getStorageContents() {
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
    public Map<ResourceType, Integer> getUpgradeCost(ShipPart part) throws UpgradeNotAvailableException, NoSuchPartException {
        switch (part) {
            case ENGINE -> {
                return new EngineManager(spaceShip.getEngineLevel()).getUpgradeCost();
            }
            case SHIELD -> {
                return new ShieldManager(spaceShip.getShieldLevel(), spaceShip.getShieldEnergy()).getUpgradeCost();
            }
            case DRILL -> {
                return new DrillManager(((MinerShip) spaceShip).getDrillLevel()).getUpgradeCost();
            }
            case STORAGE -> {
                return new ShipStorageManager(((MinerShip) spaceShip).getStorageLevel(), ((MinerShip) spaceShip).getResources()).getUpgradeCost();
            }
            default -> throw new NoSuchPartException("No such part on this ship");
        }
    }

    @Override
    public boolean upgradePart(ShipPart part) throws NoSuchPartException {
        MinerShip minerShip = (MinerShip) spaceShip;
        switch (part) {
            case ENGINE -> {
                EngineManager engine = new EngineManager(spaceShip.getEngineLevel());
                engine.upgrade();
                minerShip.setEngineLevel(engine.getCurrentLevel());
            }
            case SHIELD -> {
                ShieldManager shield = new ShieldManager(spaceShip.getShieldLevel(), spaceShip.getShieldEnergy());
                shield.upgrade();
                minerShip.setShieldLevel(shield.getCurrentLevel());
                minerShip.setShieldEnergy(shield.getCurrentEnergy());
            }
            case DRILL -> {
                DrillManager drill = new DrillManager((minerShip).getDrillLevel());
                drill.upgrade();
                minerShip.setDrillLevel(drill.getCurrentLevel());
            }
            case STORAGE -> {
                ShipStorageManager storage = new ShipStorageManager((minerShip).getStorageLevel(), (minerShip).getResources());
                storage.upgrade();
                minerShip.setStorageLevel(storage.getCurrentLevel());
            }
            default -> throw new NoSuchPartException("No such part on this ship");
        }
        return true;
    }

    @Override
    public Map<ResourceType, Integer> getCost() {
        return new HashMap<>(COST);
    }
}
