package com.codecool.spaceship.model.station;

import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SpaceStationManagerStorageTest {

    @Test
    void getCurrentCapacityTestLevel1() {
        StationStorageManager stationStorageManager = new StationStorageManager();
        int expected = 20;

        assertEquals(expected, stationStorageManager.getCurrentCapacity());
    }

    @Test
    void getCurrentCapacityTestLevel3() {
        StationStorageManager stationStorageManager = new StationStorageManager(3, new HashSet<>());
        int expected = 100;
        assertEquals(expected, stationStorageManager.getCurrentCapacity());
    }

    @Test
    void addResourceSuccessOne() {
        StationStorageManager stationStorageManager = new StationStorageManager();
        try {
            stationStorageManager.addResource(ResourceType.METAL, 5);
        } catch (StorageException ignored) {
        }

        Map<ResourceType, Integer> expected = new HashMap<>() {{
            put(ResourceType.METAL, 5);
        }};
        assertEquals(expected, stationStorageManager.getStoredItems());
    }

    @Test
    void addResourceSuccessMultiple() {
        StationStorageManager stationStorageManager = new StationStorageManager();
        try {
            stationStorageManager.addResource(ResourceType.METAL, 5);
            stationStorageManager.addResource(ResourceType.METAL, 10);
            stationStorageManager.addResource(ResourceType.SILICONE, 2);
        } catch (StorageException ignored) {
        }

        Map<ResourceType, Integer> expected = new HashMap<>() {{
            put(ResourceType.METAL, 15);
            put(ResourceType.SILICONE, 2);
        }};
        assertEquals(expected, stationStorageManager.getStoredItems());
    }

    @Test
    void addResourceError() {
        StationStorageManager stationStorageManager = new StationStorageManager();
        assertThrows(StorageException.class, () -> stationStorageManager.addResource(ResourceType.METAL, 25));
    }

    @Test
    void hasResourceTrue() {
        StationStorageManager stationStorageManager = new StationStorageManager();
        try {
            stationStorageManager.addResource(ResourceType.METAL, 5);
        } catch (StorageException ignored) {
        }
        assertTrue(stationStorageManager.hasResource(ResourceType.METAL, 5));
    }

    @Test
    void hasResourceFalse() {
        StationStorageManager stationStorageManager = new StationStorageManager();
        assertFalse(stationStorageManager.hasResource(ResourceType.METAL, 5));
    }

    @Test
    void removeResourceSuccess() {
        StationStorageManager stationStorageManager = new StationStorageManager();
        try {
            stationStorageManager.addResource(ResourceType.METAL, 5);
            stationStorageManager.removeResource(ResourceType.METAL, 4);
        } catch (StorageException ignored) {
        }
        Map<ResourceType, Integer> expected = new HashMap<>() {{
            put(ResourceType.METAL, 1);
        }};
        assertEquals(expected, stationStorageManager.getStoredItems());
    }

    @Test
    void removeResourceError() {
        StationStorageManager stationStorageManager = new StationStorageManager();
        assertThrows(StorageException.class, () -> stationStorageManager.removeResource(ResourceType.METAL, 1));
    }

    @Test
    void getUpgradeCostSuccess() {
        StationStorageManager stationStorageManager = new StationStorageManager();
        Map<ResourceType, Integer> actual = null;
        try {
            actual = stationStorageManager.getUpgradeCost();
        } catch (UpgradeNotAvailableException ignored) {
        }

        Map<ResourceType, Integer> expected = new HashMap<>() {{
            put(ResourceType.METAL, 5);
        }};
        assertEquals(expected, actual);
    }

    @Test
    void getUpgradeCostError() {
        StationStorageManager stationStorageManager = new StationStorageManager(5, new HashSet<>());
        assertThrows(UpgradeNotAvailableException.class, stationStorageManager::getUpgradeCost);
    }

    @Test
    void upgradeSuccess() {
        StationStorageManager stationStorageManager = new StationStorageManager();
        stationStorageManager.upgrade();
        int expected = 2;
        assertEquals(expected, stationStorageManager.getCurrentLevel());
    }

    @Test
    void upgradeMaxLevelCannotBeExceeded() {
        StationStorageManager stationStorageManager = new StationStorageManager(5, new HashSet<>());
        int expected = 5;
        assertEquals(expected, stationStorageManager.getCurrentLevel());
    }
}