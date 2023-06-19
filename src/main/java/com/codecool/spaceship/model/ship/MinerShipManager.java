package com.codecool.spaceship.model.ship;

import com.codecool.spaceship.model.dto.MinerShipDTO;
import com.codecool.spaceship.model.exception.NoSuchPartException;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.shipparts.*;

import java.util.*;

public class MinerShipManager extends SpaceShipManager {

    private static final List<ShipPart> PARTS = List.of(ShipPart.ENGINE, ShipPart.SHIELD, ShipPart.DRILL, ShipPart.STORAGE);
    private static final Map<ResourceType, Integer> COST = new HashMap<>() {{
        put(ResourceType.METAL, 50);
        put(ResourceType.CRYSTAL, 20);
        put(ResourceType.SILICONE, 20);
    }};
    private final MinerShip minerShip;
    private ShipStorageManager storage;
    private DrillManager drill;

    public MinerShipManager(MinerShip minerShip) {
        super(minerShip);
        this.minerShip = minerShip;
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
                minerShip.getId(),
                minerShip.getName(),
                minerShip.getColor(),
                isAvailable() ? 0L : minerShip.getCurrentMission().getId(),
                minerShip.getEngineLevel(),
                getSpeed(),
                minerShip.getShieldLevel(),
                getShieldEnergy(),
                getShieldMaxEnergy(),
                minerShip.getDrillLevel(),
                getDrillEfficiency(),
                minerShip.getStorageLevel(),
                getMaxStorageCapacity(),
                getStorageContents()
        );
    }

    public int getDrillEfficiency() {
        createDrillIfNotExists();
        return drill.getEfficiency();
    }

    public int getMaxStorageCapacity() {
        createStorageIfNotExists();
        return storage.getMaxCapacity();
    }

    public double getSpeed() {
        EngineManager engine = new EngineManager(minerShip.getEngineLevel());
        return engine.getSpeed();
    }

    public int getEmptyStorageSpace() {
        createStorageIfNotExists();
        return storage.getEmptySpace();
    }

    public Map<ResourceType, Integer> getStorageContents() {
        createStorageIfNotExists();
        return storage.getStoredResources();
    }

    public boolean hasResourcesInStorage(Map<ResourceType, Integer> resources) {
        createStorageIfNotExists();
        return resources.entrySet().stream()
                .allMatch(entry ->  storage.hasResource(entry.getKey(), entry.getValue()));
    }

    public boolean addResourceToStorage(ResourceType resourceType, int amount) throws StorageException {
        createStorageIfNotExists();
        return storage.addResource(resourceType, amount);
    }

    public boolean removeResourceFromStorage(ResourceType resourceType, int amount) throws StorageException {
        createStorageIfNotExists();
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
                return new EngineManager(minerShip.getEngineLevel()).getUpgradeCost();
            }
            case SHIELD -> {
                return new ShieldManager(minerShip.getShieldLevel(), minerShip.getShieldEnergy()).getUpgradeCost();
            }
            case DRILL -> {
                createDrillIfNotExists();
                return drill.getUpgradeCost();
            }
            case STORAGE -> {
                createStorageIfNotExists();
                return storage.getUpgradeCost();
            }
            default -> throw new NoSuchPartException("No such part on this ship");
        }
    }

    @Override
    public boolean upgradePart(ShipPart part) throws NoSuchPartException {
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
                createDrillIfNotExists();
                drill.upgrade();
                minerShip.setDrillLevel(drill.getCurrentLevel());
            }
            case STORAGE -> {
                createStorageIfNotExists();
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

    private void createStorageIfNotExists() {
        if (storage == null) {
            storage = new ShipStorageManager(minerShip.getStorageLevel(), minerShip.getResources());
        }
    }

    private void createDrillIfNotExists() {
        if (drill == null) {
            drill = new DrillManager((minerShip).getDrillLevel());
        }
    }
}
