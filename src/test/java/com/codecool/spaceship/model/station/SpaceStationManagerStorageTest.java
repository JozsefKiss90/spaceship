package com.codecool.spaceship.model.station;

import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SpaceStationManagerStorageTest {

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
            spaceStationStorage.addResource(ResourceType.METAL, 5);
        } catch (StorageException ignored) {
        }

        Map<ResourceType, Integer> expected = new HashMap<>() {{
            put(ResourceType.METAL, 5);
        }};
        assertEquals(expected, spaceStationStorage.getStoredItems());
    }

    @Test
    void addResourceSuccessMultiple() {
        SpaceStationStorage spaceStationStorage = new SpaceStationStorage();
        try {
            spaceStationStorage.addResource(ResourceType.METAL, 5);
            spaceStationStorage.addResource(ResourceType.METAL, 10);
            spaceStationStorage.addResource(ResourceType.SILICONE, 2);
        } catch (StorageException ignored) {
        }

        Map<ResourceType, Integer> expected = new HashMap<>() {{
            put(ResourceType.METAL, 15);
            put(ResourceType.SILICONE, 2);
        }};
        assertEquals(expected, spaceStationStorage.getStoredItems());
    }

    @Test
    void addResourceError() {
        SpaceStationStorage spaceStationStorage = new SpaceStationStorage();
        assertThrows(StorageException.class, () -> spaceStationStorage.addResource(ResourceType.METAL, 25));
    }

    @Test
    void hasResourceTrue() {
        SpaceStationStorage spaceStationStorage = new SpaceStationStorage();
        try {
            spaceStationStorage.addResource(ResourceType.METAL, 5);
        } catch (StorageException ignored) {
        }
        assertTrue(spaceStationStorage.hasResource(ResourceType.METAL, 5));
    }

    @Test
    void hasResourceFalse() {
        SpaceStationStorage spaceStationStorage = new SpaceStationStorage();
        assertFalse(spaceStationStorage.hasResource(ResourceType.METAL, 5));
    }

    @Test
    void removeResourceSuccess() {
        SpaceStationStorage spaceStationStorage = new SpaceStationStorage();
        try {
            spaceStationStorage.addResource(ResourceType.METAL, 5);
            spaceStationStorage.removeResource(ResourceType.METAL, 4);
        } catch (StorageException ignored) {
        }
        Map<ResourceType, Integer> expected = new HashMap<>() {{
            put(ResourceType.METAL, 1);
        }};
        assertEquals(expected, spaceStationStorage.getStoredItems());
    }

    @Test
    void removeResourceError() {
        SpaceStationStorage spaceStationStorage = new SpaceStationStorage();
        assertThrows(StorageException.class, () -> spaceStationStorage.removeResource(ResourceType.METAL, 1));
    }

    @Test
    void getUpgradeCostSuccess() {
        SpaceStationStorage spaceStationStorage = new SpaceStationStorage();
        Map<ResourceType, Integer> actual = null;
        try {
            actual = spaceStationStorage.getUpgradeCost();
        } catch (UpgradeNotAvailableException ignored) {
        }

        Map<ResourceType, Integer> expected = new HashMap<>() {{
            put(ResourceType.METAL, 5);
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