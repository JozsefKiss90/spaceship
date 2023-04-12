package com.codecool.spaceship.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseStorage {
    private static final List<Map<Resource, Integer>> UPGRADE_NEEDS = new ArrayList<>() {
        {
            add(null);
            add(new HashMap<>() {{
                put(Resource.METAL, 5);
            }});
            add(new HashMap<>() {
                {
                    put(Resource.METAL, 20);
                    put(Resource.SILICONE, 10);
                }
            });
            add(new HashMap<>() {{
                put(Resource.METAL, 200);
                put(Resource.SILICONE, 100);
            }});
            add(new HashMap<>() {{
                put(Resource.METAL, 400);
                put(Resource.SILICONE, 150);
                put(Resource.PLUTONIUM, 50);
            }});

        }

    };

    private static final List<Integer> UPGRADE_CAPACITIES = new ArrayList<>() {{
        add(20);
        add(50);
        add(100);
        add(500);
        add(1000);
    }};

    private Integer currentLevelIndex;

    public BaseStorage() {
        currentLevelIndex = 0;
    }

    public int getCurrentCapacity() {
        return UPGRADE_CAPACITIES.get(currentLevelIndex);
    }

}
