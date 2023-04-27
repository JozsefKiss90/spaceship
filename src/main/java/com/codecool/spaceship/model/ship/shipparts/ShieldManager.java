package com.codecool.spaceship.model.ship.shipparts;


import com.codecool.spaceship.model.Level;
import com.codecool.spaceship.model.exception.InvalidLevelException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.Upgradeable;

import java.util.List;
import java.util.Map;

public class ShieldManager implements Upgradeable {

    private static final List<Level<Integer>> LEVELS = List.of(
            new Level<>(1, 20, Map.of()),
            new Level<>(2, 50,
                    Map.of(
                            ResourceType.CRYSTAL, 20
                    )),
            new Level<>(3, 100,
                    Map.of(
                            ResourceType.CRYSTAL, 40,
                            ResourceType.SILICONE, 10
                    )),
            new Level<>(4, 150,
                    Map.of(
                            ResourceType.CRYSTAL, 100,
                            ResourceType.SILICONE, 20
                    )),
            new Level<>(
                    5, 200,
                    Map.of(
                            ResourceType.CRYSTAL, 150,
                            ResourceType.SILICONE, 40,
                            ResourceType.PLUTONIUM, 5
                    ))
    );
    private static final int MAX_LEVEL_INDEX = LEVELS.size() - 1;
    private int currentLevelIndex;
    private int currentEnergy;

    public ShieldManager() {
        currentLevelIndex = 0;
        currentEnergy = LEVELS.get(0).effect();
    }

    public ShieldManager(int currentLevelIndex, int currentEnergy) {
        if (currentLevelIndex < 0) {
            throw new InvalidLevelException("Level index can't be lower than 0");
        } else if (currentLevelIndex > MAX_LEVEL_INDEX) {
            throw new InvalidLevelException("Level index can't be higher than %d".formatted(MAX_LEVEL_INDEX));
        }
        this.currentLevelIndex = currentLevelIndex;
        if (currentEnergy < 0) {
            throw new IllegalArgumentException("Shield energy can't be lower than 0");
        } else if (currentEnergy > LEVELS.get(currentLevelIndex).effect()) {
            throw new InvalidLevelException("Shiled energy can't be higher than %d at this level".formatted(LEVELS.get(currentLevelIndex).effect()));
        }
        this.currentEnergy = currentEnergy;
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
