package com.codecool.spaceship.model.ship.shipparts;

import com.codecool.spaceship.model.Level;
import com.codecool.spaceship.model.exception.InvalidLevelException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.Upgradeable;

import java.util.List;
import java.util.Map;

public class DrillManager implements Upgradeable {

    private static final List<Level<Integer>> LEVELS = List.of(
            new Level<>(1, 5, Map.of()),
            new Level<>(2, 10,
                    Map.of(
                            ResourceType.METAL, 20,
                            ResourceType.CRYSTAL, 10
                    )),
            new Level<>(3, 20,
                    Map.of(
                            ResourceType.METAL, 100,
                            ResourceType.CRYSTAL, 50
                    )),
            new Level<>(4, 35,
                    Map.of(
                            ResourceType.METAL, 200,
                            ResourceType.CRYSTAL, 100
                    )),
            new Level<>(5, 50,
                    Map.of(
                            ResourceType.METAL, 400,
                            ResourceType.CRYSTAL, 150,
                            ResourceType.PLUTONIUM, 10
                    ))
    );
    private static final int MAX_LEVEL_INDEX = LEVELS.size() - 1;
    private int currentLevelIndex;

    public DrillManager() {
        currentLevelIndex = 0;
    }

    public DrillManager(int currentLevelIndex) {
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

    public int getEfficiency() {
        return LEVELS.get(currentLevelIndex).effect();
    }
}
