package com.codecool.spaceship.model.ship;


import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.UpgradeNotAvailable;
import com.codecool.spaceship.model.Upgradeable;

import java.util.List;
import java.util.Map;

public class Shield implements Upgradeable {

    private static final int MAX_LEVEL = 5;
    private static final List<Map<Resource, Integer>> UPGRADE_COST = List.of(
            //Level 1
            Map.of(),
            //Level 2
            Map.of(
                    Resource.CRYSTAL, 20
            ),
            //Level 3
            Map.of(
                    Resource.CRYSTAL, 40,
                    Resource.SILICONE, 10
            ),
            //Level 4
            Map.of(
                    Resource.CRYSTAL, 100,
                    Resource.SILICONE, 20
            ),
            //Level 5
            Map.of(
                    Resource.CRYSTAL, 150,
                    Resource.SILICONE, 40,
                    Resource.PLUTONIUM, 5
            )
    );
    private static final List<Integer> MAX_ENERGY_VALUES = List.of(
            //Level 1
            20,
            //Level 2
            50,
            //Level 3
            100,
            //Level 4
            150,
            //Level 5
            200
    );
    private int currentLevelIndex;
    private int currentEnergy;

    public Shield() {
        currentLevelIndex = 0;
        currentEnergy = MAX_ENERGY_VALUES.get(0);
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

    public int getMaxEnergy() {
        return MAX_ENERGY_VALUES.get(currentLevelIndex);
    }

    public int getCurrentEnergy() {
        return currentEnergy;
    }

    public void repair(int amount) {
        currentEnergy = Math.min(currentEnergy + amount ,getMaxEnergy());
    }

    public void damage(int amount) {
        currentEnergy = Math.max(currentEnergy - amount, 0);
    }
}
