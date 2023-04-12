package com.codecool.spaceship.model;

import java.util.Map;

public interface Upgradeable {
    Map<Resource,Integer> getUpgradeCost() throws UpgradeNotAvailable;
    void upgrade();

    int getCurrentLevel();

}
