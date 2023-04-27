package com.codecool.spaceship.model.station;

import com.codecool.spaceship.model.Level;
import com.codecool.spaceship.model.resource.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StationPartLevels {

    public static final List<Level<Integer>> HANGAR_LEVELS = new ArrayList<>() {{
        add(new Level<>(1, 2, null));
        add(new Level<>(2, 4, new HashMap<>() {{
            put(ResourceType.METAL, 5);
        }}));
        add(new Level<>(3, 6, new HashMap<>() {{
            put(ResourceType.METAL, 20);
            put(ResourceType.SILICONE, 20);
        }}));
        add(new Level<>(4, 8, new HashMap<>() {{
            put(ResourceType.METAL, 100);
            put(ResourceType.SILICONE, 100);
            put(ResourceType.CRYSTAL, 50);
        }}));
        add(new Level<>(5, 10, new HashMap<>() {{
            put(ResourceType.METAL, 500);
            put(ResourceType.SILICONE, 150);
            put(ResourceType.CRYSTAL, 100);
        }}));
    }};

    public static final List<Level<Integer>> STORAGE_LEVELS = new ArrayList<>() {{
        add(new Level<>(1, 20, null));
        add(new Level<>(2, 50, new HashMap<>() {{
            put(ResourceType.METAL, 5);
        }}));
        add(new Level<>(3, 100, new HashMap<>() {{
            put(ResourceType.METAL, 20);
            put(ResourceType.SILICONE, 10);
        }}));
        add(new Level<>(4, 500, new HashMap<>() {{
            put(ResourceType.METAL, 200);
            put(ResourceType.SILICONE, 100);
        }}));
        add(new Level<>(5, 1000, new HashMap<>() {{
            put(ResourceType.METAL, 400);
            put(ResourceType.SILICONE, 150);
            put(ResourceType.PLUTONIUM, 50);
        }}));
    }};
}
