package com.codecool.spaceship.model.ship.shipparts;

import com.codecool.spaceship.model.Level;
import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.UpgradeNotAvailable;
import com.codecool.spaceship.model.Upgradeable;

import java.util.List;
import java.util.Map;

public class Engine implements Upgradeable {

    private static final List<Level<Double>> LEVELS = List.of(
            new Level<>(1, 1.0, Map.of()),
            new Level<>(2, 2.5,
                    Map.of(
                            Resource.SILICONE, 10,
                            Resource.CRYSTAL, 5
                    )),
            new Level<>(3, 5.0,
                    Map.of(
                            Resource.SILICONE, 25,
                            Resource.CRYSTAL, 5
                    )),
            new Level<>(4, 7.5,
                    Map.of(
                            Resource.SILICONE, 100,
                            Resource.CRYSTAL, 10
                    )),
            new Level<>(5, 10.0,
                    Map.of(
                            Resource.SILICONE, 150,
                            Resource.CRYSTAL, 20,
                            Resource.PLUTONIUM, 5
                    ))
    );
    private int currentLevelIndex;

    public Engine() {
        currentLevelIndex = 0;
    }

    @Override
    public Map<Resource, Integer> getUpgradeCost() throws UpgradeNotAvailable {
        if (getCurrentLevel() == LEVELS.size()) {
            throw new UpgradeNotAvailable("Already at max level");
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

    public double getSpeed() {
        return LEVELS.get(currentLevelIndex).effect();
    }
}
