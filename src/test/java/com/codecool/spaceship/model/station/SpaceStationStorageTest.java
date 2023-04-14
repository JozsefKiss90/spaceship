package com.codecool.spaceship.model.station;

import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SpaceStationStorageTest {

    @Test
    void getCurrentCapacityTestLevel1() {
        SpaceStationStorage spaceStationStorage = new SpaceStationStorage();
        int expected = 20;

        assertEquals(expected, spaceStationStorage.getCurrentCapacity());
    }

    @Test
    void getCurrentCapacityTestLevel3() {
        SpaceStationStorage spaceStationStorage = new SpaceStationStorage();
        spaceStationStorage.upgrade();
        spaceStationStorage.upgrade();
        int expected = 100;
        assertEquals(expected, spaceStationStorage.getCurrentCapacity());
    }

    @Test
    void addResourceSuccessOne() {
        SpaceStationStorage spaceStationStorage = new SpaceStationStorage();
        try {
            spaceStationStorage.addResource(Resource.METAL, 5);
        } catch (StorageException ignored) {
        }

        Map<Resource, Integer> expected = new HashMap<>() {{
            put(Resource.METAL, 5);
        }};
        assertEquals(expected, spaceStationStorage.getStoredItems());
    }

    @Test
    void addResourceSuccessMultiple() {
        SpaceStationStorage spaceStationStorage = new SpaceStationStorage();
        try {
            spaceStationStorage.addResource(Resource.METAL, 5);
            spaceStationStorage.addResource(Resource.METAL, 10);
            spaceStationStorage.addResource(Resource.SILICONE, 2);
        } catch (StorageException ignored) {
        }

        Map<Resource, Integer> expected = new HashMap<>() {{
            put(Resource.METAL, 15);
            put(Resource.SILICONE, 2);
        }};
        assertEquals(expected, spaceStationStorage.getStoredItems());
    }

    @Test
    void addResourceError() {
        SpaceStationStorage spaceStationStorage = new SpaceStationStorage();
        assertThrows(StorageException.class, () -> spaceStationStorage.addResource(Resource.METAL, 25));
    }

    @Test
    void hasResourceTrue() {
        SpaceStationStorage spaceStationStorage = new SpaceStationStorage();
        try {
            spaceStationStorage.addResource(Resource.METAL, 5);
        } catch (StorageException ignored) {
        }
        assertTrue(spaceStationStorage.hasResource(Resource.METAL, 5));
    }

    @Test
    void hasResourceFalse() {
        SpaceStationStorage spaceStationStorage = new SpaceStationStorage();
        assertFalse(spaceStationStorage.hasResource(Resource.METAL, 5));
    }

    @Test
    void removeResourceSuccess() {
        SpaceStationStorage spaceStationStorage = new SpaceStationStorage();
        try {
            spaceStationStorage.addResource(Resource.METAL, 5);
            spaceStationStorage.removeResource(Resource.METAL, 4);
        } catch (StorageException ignored) {
        }
        Map<Resource, Integer> expected = new HashMap<>() {{
            put(Resource.METAL, 1);
        }};
        assertEquals(expected, spaceStationStorage.getStoredItems());
    }

    @Test
    void removeResourceError() {
        SpaceStationStorage spaceStationStorage = new SpaceStationStorage();
        assertThrows(StorageException.class, () -> spaceStationStorage.removeResource(Resource.METAL, 1));
    }

    @Test
    void getUpgradeCostSuccess() {
        SpaceStationStorage spaceStationStorage = new SpaceStationStorage();
        Map<Resource, Integer> actual = null;
        try {
            actual = spaceStationStorage.getUpgradeCost();
        } catch (UpgradeNotAvailableException ignored) {
        }

        Map<Resource, Integer> expected = new HashMap<>() {{
            put(Resource.METAL, 5);
        }};
        assertEquals(expected, actual);
    }

    @Test
    void getUpgradeCostError() {
        SpaceStationStorage spaceStationStorage = new SpaceStationStorage();
        spaceStationStorage.upgrade();
        spaceStationStorage.upgrade();
        spaceStationStorage.upgrade();
        spaceStationStorage.upgrade();
        assertThrows(UpgradeNotAvailableException.class, spaceStationStorage::getUpgradeCost);
    }

    @Test
    void upgradeSuccess() {
        SpaceStationStorage spaceStationStorage = new SpaceStationStorage();
        spaceStationStorage.upgrade();
        int expected = 2;
        assertEquals(expected, spaceStationStorage.getCurrentLevel());
    }

    @Test
    void upgradeMaxLevelCannotBeExceeded() {
        SpaceStationStorage spaceStationStorage = new SpaceStationStorage();
        spaceStationStorage.upgrade();
        spaceStationStorage.upgrade();
        spaceStationStorage.upgrade();
        spaceStationStorage.upgrade();
        spaceStationStorage.upgrade();
        spaceStationStorage.upgrade();
        spaceStationStorage.upgrade();
        spaceStationStorage.upgrade();
        int expected = 5;
        assertEquals(expected, spaceStationStorage.getCurrentLevel());
    }
}