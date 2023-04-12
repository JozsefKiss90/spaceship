package com.codecool.spaceship.model.ship;

import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.UpgradeNotAvailable;
import com.codecool.spaceship.model.Upgradeable;

import java.util.List;
import java.util.Map;

public class Engine implements Upgradeable {

    private static final int MAX_LEVEL = 5;
    private static final List<Map<Resource, Integer>> UPGRADE_COST = List.of(
            //Level 1
            Map.of(),
            //Level 2
            Map.of(
                    Resource.SILICONE, 10,
                    Resource.CRYSTAL, 5
            ),
            //Level 3
            Map.of(
                    Resource.SILICONE, 25,
                    Resource.CRYSTAL, 5
            ),
            //Level 4
            Map.of(
                    Resource.SILICONE, 100,
                    Resource.CRYSTAL, 10
            ),
            //Level 5
            Map.of(
                    Resource.SILICONE, 150,
                    Resource.CRYSTAL, 20,
                    Resource.PLUTONIUM, 5
            )
    );
    private static final List<Double> SPEED_VALUES = List.of(
            //Level 1
            1.0,
            //Level 2
            2.5,
            //Level 3
            5.0,
            //Level 4
            8.0,
            //Level 5
            10.0
    );
    private int currentLevelIndex;

    public Engine() {
        currentLevelIndex = 0;
    }

    @Override
    public Map<Resource, Integer> getUpgradeCost() throws UpgradeNotAvailable {
        if (getCurrentLevel() == MAX_LEVEL) {
            throw new UpgradeNotAvailable("Already at max level");
        } else {
            return UPGRADE_COST.get(getCurrentLevel());
        }
    }

    @Override
    public void upgrade() {
        if (getCurrentLevel() < MAX_LEVEL) {
            currentLevelIndex++;
        }
    }

    @Override
    public int getCurrentLevel() {
        return currentLevelIndex + 1;
    }

    public double getSpeed() {
        return SPEED_VALUES.get(currentLevelIndex);
    }
}
