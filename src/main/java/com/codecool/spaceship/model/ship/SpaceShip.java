package com.codecool.spaceship.model.ship;

import com.codecool.spaceship.model.Mission;
import com.codecool.spaceship.model.Upgradeable;

import java.util.List;

public abstract class SpaceShip {

    private String name;
    private Color color;
    private final Engine engine;
    private final Shield shield;
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
        currentMission = mission;
    }

    public int getShieldStrength() {
        return shield.getCurrentStrength();
    }

    public int getShieldMaxStrength() {
        return shield.getMaxStrength();
    }

    public void setShieldStrength(int newStrength) {
        shield.setCurrentStrength(newStrength);
    }

    public double getSpeed() {
        return engine.getSpeed();
    }

    public List<Upgradeable> getUpgradeables() {
        return List.of(shield, engine);
    }
}
