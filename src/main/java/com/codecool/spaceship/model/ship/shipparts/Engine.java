package com.codecool.spaceship.model.ship.shipparts;

import com.codecool.spaceship.model.Level;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.Upgradeable;

import java.util.List;
import java.util.Map;

public class Engine implements Upgradeable {

    private static final List<Level<Double>> LEVELS = List.of(
            new Level<>(1, 1.0, Map.of()),
            new Level<>(2, 2.5,
                    Map.of(
                            ResourceType.SILICONE, 10,
                            ResourceType.CRYSTAL, 5
                    )),
            new Level<>(3, 5.0,
                    Map.of(
                            ResourceType.SILICONE, 25,
                            ResourceType.CRYSTAL, 5
                    )),
            new Level<>(4, 7.5,
                    Map.of(
                            ResourceType.SILICONE, 100,
                            ResourceType.CRYSTAL, 10
                    )),
            new Level<>(5, 10.0,
                    Map.of(
                            ResourceType.SILICONE, 150,
                            ResourceType.CRYSTAL, 20,
                            ResourceType.PLUTONIUM, 5
                    ))
    );
    private int currentLevelIndex;

    public Engine() {
        currentLevelIndex = 0;
    }

    @Override
    public Map<ResourceType, Integer> getUpgradeCost() throws UpgradeNotAvailableException {
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

    public double getSpeed() {
        return LEVELS.get(currentLevelIndex).effect();
    }
}
