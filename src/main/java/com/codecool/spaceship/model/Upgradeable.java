package com.codecool.spaceship.model;

import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.resource.ResourceType;

import java.util.Map;

public interface Upgradeable {
    Map<ResourceType,Integer> getUpgradeCost() throws UpgradeNotAvailableException;
    void upgrade();
    int getCurrentLevel();
}
