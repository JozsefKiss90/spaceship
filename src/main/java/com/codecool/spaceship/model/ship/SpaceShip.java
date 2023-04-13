package com.codecool.spaceship.model.ship;

import com.codecool.spaceship.model.Mission;
import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.exception.NoSuchPartException;
import com.codecool.spaceship.model.Upgradeable;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.ship.shipparts.Engine;
import com.codecool.spaceship.model.ship.shipparts.Shield;
import com.codecool.spaceship.model.ship.shipparts.ShipPart;

import java.util.List;
import java.util.Map;

public abstract class SpaceShip {

    private String name;
    private Color color;
    protected final Engine engine;
    protected final Shield shield;
    private Mission currentMission;

    protected SpaceShip(String name, Color color) {
        this.name = name;
        this.color = color;
        engine = new Engine();
        shield = new Shield();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isAvailable() {
        return currentMission == null;
    }

    public void setCurrentMission(Mission mission) {
        if (currentMission == null) {
            currentMission = mission;
        }
    }

    public void endMission() {
        currentMission = null;
    }

    public int getShieldEnergy() {
        return shield.getCurrentEnergy();
    }

    public int getShieldMaxEnergy() {
        return shield.getMaxEnergy();
    }

    public void repairShield(int amount) {
        shield.repair(amount);
    }

    public void damageShield(int amount) {
        shield.damage(amount);
    }

    public double getSpeed() {
        return engine.getSpeed();
    }
    public abstract List<ShipPart> getPartTypes();
    public abstract Upgradeable getPart(ShipPart part) throws NoSuchPartException;
    public abstract Map<Resource,Integer> getCost();

}
