package com.codecool.spaceship.model.station;

import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.ship.MinerShipService;
import com.codecool.spaceship.model.ship.SpaceShipService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class HangarServiceTest {
    @Mock
    MinerShipService ship1;
    @Mock
    MinerShipService ship2;
    @Mock
    SpaceShipService ship3;
    @Test
    void addShipSuccess() {
        HangarService hangarService = new HangarService();
        try {
            hangarService.addShip(ship1);
        } catch (StorageException ignored) {
        }
        assertEquals(Set.of(ship1), hangarService.getAllShips());
    }

    @Test
    void addShipError() {
        HangarService hangarService = new HangarService();
        try {
            hangarService.addShip(ship1);
            hangarService.addShip(ship2);
        } catch (Exception ignored) {
        }
        assertThrows(StorageException.class, () -> hangarService.addShip(ship3));
    }

    @Test
    void getAllShipsTwo() {
        HangarService hangarService = new HangarService();
        try {
            hangarService.addShip(ship1);
            hangarService.addShip(ship3);
        } catch (StorageException ignored) {
        }
        Set<SpaceShipService> expected = Set.of(ship1, ship3);
        assertEquals(expected, hangarService.getAllShips());
    }

    @Test
    void getAllShipsEmpty() {
        HangarService hangarService = new HangarService();
        Set<SpaceShipService> expected = Set.of();
        assertEquals(expected, hangarService.getAllShips());
    }

    @Test
    void getUpgradeCostLevel1() {
        HangarService hangarService = new HangarService();
        Map<Resource, Integer> expected = new HashMap<>() {{
            put(Resource.METAL, 5);
        }};
        Map<Resource, Integer> actual = null;
        try {
            actual = hangarService.getUpgradeCost();
        } catch (UpgradeNotAvailableException ignored) {
        }
        assertEquals(expected, actual);
    }

    @Test
    void getUpgradeCostLevel3() {
        HangarService hangarService = new HangarService();
        hangarService.upgrade();
        hangarService.upgrade();
        Map<Resource, Integer> expected = new HashMap<>() {{
            put(Resource.METAL, 100);
            put(Resource.SILICONE, 100);
            put(Resource.CRYSTAL, 50);
        }};
        Map<Resource, Integer> actual = null;
        try {
            actual = hangarService.getUpgradeCost();
        } catch (UpgradeNotAvailableException ignored) {
        }
        assertEquals(expected, actual);
    }

    @Test
    void getUpgradeCostThrows() {
        HangarService hangarService = new HangarService();
        hangarService.upgrade();
        hangarService.upgrade();
        hangarService.upgrade();
        hangarService.upgrade();
        assertThrows(UpgradeNotAvailableException.class, hangarService::getUpgradeCost);
    }

    @Test
    void upgradeSuccess() {
        HangarService hangarService = new HangarService();
        hangarService.upgrade();
        int expected = 2;
        assertEquals(expected, hangarService.getCurrentLevel());
    }

    @Test
    void upgradeMaxLevelExceeded() {
        HangarService hangarService = new HangarService();
        hangarService.upgrade();
        hangarService.upgrade();
        hangarService.upgrade();
        hangarService.upgrade();
        hangarService.upgrade();
        hangarService.upgrade();
        int expected = 5;
        assertEquals(expected, hangarService.getCurrentLevel());
    }
}