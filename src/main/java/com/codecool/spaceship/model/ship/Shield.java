package com.codecool.spaceship.model.ship;


import com.codecool.spaceship.model.Resource;

import java.util.List;
import java.util.Map;

public class Shield {

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
                    Resource.SILICON, 10
            ),
            //Level 4
            Map.of(
                    Resource.CRYSTAL, 100,
                    Resource.SILICON, 20
            ),
            //Level 5
            Map.of(
                    Resource.CRYSTAL, 150,
                    Resource.SILICON, 40,
                    Resource.PLUTONIUM, 5
            )
    );
    private static final List<Integer> UPGRADE_VALUES = List.of(
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
    private int currentLevel;

}
