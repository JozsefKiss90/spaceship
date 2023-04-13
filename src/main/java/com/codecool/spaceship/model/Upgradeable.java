package com.codecool.spaceship.model;

import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;

import java.util.Map;

public interface Upgradeable {
    Map<Resource,Integer> getUpgradeCost() throws UpgradeNotAvailableException;
    void upgrade();
    int getCurrentLevel();
}
