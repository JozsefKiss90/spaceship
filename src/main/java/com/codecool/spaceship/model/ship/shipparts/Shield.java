package com.codecool.spaceship.model.ship.shipparts;


import com.codecool.spaceship.model.Level;
import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.Upgradeable;

import java.util.List;
import java.util.Map;

public class Shield implements Upgradeable {

    private static final List<Level<Integer>> LEVELS = List.of(
            new Level<>(1, 20, Map.of()),
            new Level<>(2, 50,
                    Map.of(
                            Resource.CRYSTAL, 20
                    )),
            new Level<>(3, 100,
                    Map.of(
                            Resource.CRYSTAL, 40,
                            Resource.SILICONE, 10
                    )),
            new Level<>(4, 150,
                    Map.of(
                            Resource.CRYSTAL, 100,
                            Resource.SILICONE, 20
                    )),
            new Level<>(
                    5, 200,
                    Map.of(
                            Resource.CRYSTAL, 150,
                            Resource.SILICONE, 40,
                            Resource.PLUTONIUM, 5
                    ))
    );
    private int currentLevelIndex;
    private int currentEnergy;

    public Shield() {
        currentLevelIndex = 0;
        currentEnergy = LEVELS.get(0).effect();
    }

    @Override
    public Map<Resource, Integer> getUpgradeCost() throws UpgradeNotAvailableException {
        if (getCurrentLevel() == LEVELS.size()) {
            throw new UpgradeNotAvailableException("Already at max level");
        } else {
            return LEVELS.get(currentLevelIndex + 1).cost();
        }
    }

    @Override
    public void upgrade() {
        if (getCurrentLevel() < LEVELS.size()) {
            currentLevelIndex++;
        }
    }

    @Override
    public int getCurrentLevel() {
        return LEVELS.get(currentLevelIndex).level();
    }

    public int getMaxEnergy() {
        return LEVELS.get(currentLevelIndex).effect();
    }

    public int getCurrentEnergy() {
        return currentEnergy;
    }

    public void repair(int amount) {
        currentEnergy = Math.min(currentEnergy + amount, getMaxEnergy());
    }

    public void damage(int amount) {
        currentEnergy = Math.max(currentEnergy - amount, 0);
    }
}
