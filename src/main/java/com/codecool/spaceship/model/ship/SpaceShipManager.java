package com.codecool.spaceship.model.ship;

import com.codecool.spaceship.model.Mission;
import com.codecool.spaceship.model.exception.NoSuchPartException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.shipparts.EngineManager;
import com.codecool.spaceship.model.ship.shipparts.ShieldManager;
import com.codecool.spaceship.model.ship.shipparts.ShipPart;

import java.util.List;
import java.util.Map;


public abstract class SpaceShipManager {

    protected final SpaceShip spaceShip;

    protected SpaceShipManager(SpaceShip spaceShip) {
        this.spaceShip = spaceShip;
    }

    public boolean isAvailable() {
        return !spaceShip.isOnMission();
    }

    public void setCurrentMission(Mission mission) {
        //TODO
    }

    public void endMission() {
        //TODO
    }

    public int getShieldEnergy() {
        return spaceShip.getShieldEnergy();
    }

    public int getShieldMaxEnergy() {
        return spaceShip.getShieldEnergy();
    }

    public void repairShield(int amount) {
        ShieldManager shield = new ShieldManager(spaceShip.getShieldLevel(), spaceShip.getShieldEnergy());
        shield.repair(amount);
    }

    public void damageShield(int amount) {
        ShieldManager shield = new ShieldManager(spaceShip.getShieldLevel(), spaceShip.getShieldEnergy());
        shield.damage(amount);
    }

    public double getSpeed() {
        EngineManager engine = new EngineManager(spaceShip.getEngineLevel());
        return engine.getSpeed();
    }
    public abstract List<ShipPart> getPartTypes();

    public abstract boolean upgradePart(ShipPart part) throws NoSuchPartException;
    public abstract Map<ResourceType, Integer> getCost();

    public abstract Map<ResourceType, Integer> getUpgradeCost(ShipPart part) throws UpgradeNotAvailableException, NoSuchPartException;
}
