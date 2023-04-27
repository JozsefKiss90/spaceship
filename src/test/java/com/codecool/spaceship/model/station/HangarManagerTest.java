package com.codecool.spaceship.model.station;

import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.ship.MinerShipManager;
import com.codecool.spaceship.model.ship.SpaceShipManager;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.SpaceShip;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class HangarManagerTest {
    @Mock
    MinerShip ship1;
    @Mock
    MinerShip ship2;
    @Mock
    SpaceShip ship3;
    @Test
    void addShipSuccess() {
        HangarManager hangarManager = new HangarManager();
        try {
            hangarManager.addShip(ship1);
        } catch (StorageException ignored) {
        }
        assertEquals(Set.of(ship1), hangarManager.getAllShips());
    }

    @Test
    void addShipError() {
        HangarManager hangarManager = new HangarManager();
        try {
            hangarManager.addShip(ship1);
            hangarManager.addShip(ship2);
        } catch (Exception ignored) {
        }
        assertThrows(StorageException.class, () -> hangarManager.addShip(ship3));
    }

    @Test
    void getAllShipsTwo() {
        HangarManager hangarManager = new HangarManager();
        try {
            hangarManager.addShip(ship1);
            hangarManager.addShip(ship3);
        } catch (StorageException ignored) {
        }
        Set<SpaceShip> expected = Set.of(ship1, ship3);
        assertEquals(expected, hangarManager.getAllShips());
    }

    @Test
    void getAllShipsEmpty() {
        HangarManager hangarManager = new HangarManager();
        Set<SpaceShip> expected = Set.of();
        assertEquals(expected, hangarManager.getAllShips());
    }

    @Test
    void getUpgradeCostLevel1() {
        HangarManager hangarManager = new HangarManager();
        Map<ResourceType, Integer> expected = new HashMap<>() {{
            put(ResourceType.METAL, 5);
        }};
        Map<ResourceType, Integer> actual = null;
        try {
            actual = hangarManager.getUpgradeCost();
        } catch (UpgradeNotAvailableException ignored) {
        }
        assertEquals(expected, actual);
    }

    @Test
    void getUpgradeCostLevel3() {
        HangarManager hangarManager = new HangarManager();
        hangarManager.upgrade();
        hangarManager.upgrade();
        Map<ResourceType, Integer> expected = new HashMap<>() {{
            put(ResourceType.METAL, 100);
            put(ResourceType.SILICONE, 100);
            put(ResourceType.CRYSTAL, 50);
        }};
        Map<ResourceType, Integer> actual = null;
        try {
            actual = hangarManager.getUpgradeCost();
        } catch (UpgradeNotAvailableException ignored) {
        }
        assertEquals(expected, actual);
    }

    @Test
    void getUpgradeCostThrows() {
        HangarManager hangarManager = new HangarManager();
        hangarManager.upgrade();
        hangarManager.upgrade();
        hangarManager.upgrade();
        hangarManager.upgrade();
        assertThrows(UpgradeNotAvailableException.class, hangarManager::getUpgradeCost);
    }

    @Test
    void upgradeSuccess() {
        HangarManager hangarManager = new HangarManager();
        hangarManager.upgrade();
        int expected = 2;
        assertEquals(expected, hangarManager.getCurrentLevel());
    }

    @Test
    void upgradeMaxLevelExceeded() {
        HangarManager hangarManager = new HangarManager();
        hangarManager.upgrade();
        hangarManager.upgrade();
        hangarManager.upgrade();
        hangarManager.upgrade();
        hangarManager.upgrade();
        hangarManager.upgrade();
        int expected = 5;
        assertEquals(expected, hangarManager.getCurrentLevel());
    }
}