package com.codecool.spaceship.model.ship.shipparts;

import com.codecool.spaceship.model.Level;
import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.UpgradeNotAvailableException;
import com.codecool.spaceship.model.Upgradeable;

import java.util.List;
import java.util.Map;

public class Drill implements Upgradeable {

    private static final List<Level<Integer>> LEVELS = List.of(
            new Level<>(1, 5, Map.of()),
            new Level<>(2, 10,
                    Map.of(
                            Resource.METAL, 20,
                            Resource.CRYSTAL, 10
                    )),
            new Level<>(3, 20,
                    Map.of(
                            Resource.METAL, 100,
                            Resource.CRYSTAL, 50
                    )),
            new Level<>(4, 35,
                    Map.of(
                            Resource.METAL, 200,
                            Resource.CRYSTAL, 100
                    )),
            new Level<>(5, 50,
                    Map.of(
                            Resource.METAL, 400,
                            Resource.CRYSTAL, 150,
                            Resource.PLUTONIUM, 10
                    ))
    );
    private int currentLevelIndex;


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

    public int getEfficiency() {
        return LEVELS.get(currentLevelIndex).effect();
    }
}
