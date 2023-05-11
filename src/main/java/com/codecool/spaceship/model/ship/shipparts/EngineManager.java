package com.codecool.spaceship.model.ship.shipparts;

import com.codecool.spaceship.model.Level;
import com.codecool.spaceship.model.exception.InvalidLevelException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.Upgradeable;

import java.util.List;
import java.util.Map;

public class EngineManager implements Upgradeable {

    private static final List<Level<Double>> LEVELS = List.of(
            new Level<>(1, 400.0, Map.of()),
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
    private static final int MAX_LEVEL_INDEX = LEVELS.size() - 1;
    private int currentLevelIndex;

    public EngineManager() {
        currentLevelIndex = 0;
    }

    public EngineManager(int currentLevel) {
        int currentLevelIndex = currentLevel - 1;
        if (currentLevelIndex < 0) {
            throw new InvalidLevelException("Level index can't be lower than 0");
        } else if (currentLevelIndex > MAX_LEVEL_INDEX) {
            throw new InvalidLevelException("Level index can't be higher than %d".formatted(MAX_LEVEL_INDEX));
        }
        this.currentLevelIndex = currentLevelIndex;
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
