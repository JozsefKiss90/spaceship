package com.codecool.spaceship.model.base;

import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.SpaceShip;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HangarTest {
    @Mock
    MinerShip ship1;
    @Mock
    MinerShip ship2;
    @Mock
    SpaceShip ship3;
    @Test
    void addShipSuccess() {
        Hangar hangar = new Hangar();
        try {
            hangar.addShip(ship1);
        } catch (StorageException ignored) {
        }
        assertEquals(Set.of(ship1), hangar.getAllShips());
    }

    @Test
    void addShipError() {
        Hangar hangar = new Hangar();
        try {
            hangar.addShip(ship1);
            hangar.addShip(ship2);
        } catch (Exception ignored) {
        }
        assertThrows(StorageException.class, () -> hangar.addShip(ship3));
    }

    @Test
    void getAllShipsTwo() {
        Hangar hangar = new Hangar();
        try {
            hangar.addShip(ship1);
            hangar.addShip(ship3);
        } catch (StorageException ignored) {
        }
        Set<SpaceShip> expected = Set.of(ship1, ship3);
        assertEquals(expected, hangar.getAllShips());
    }

    @Test
    void getAllShipsEmpty() {
        Hangar hangar = new Hangar();
        Set<SpaceShip> expected = Set.of();
        assertEquals(expected, hangar.getAllShips());
    }

    @Test
    void getUpgradeCostLevel1() {
        Hangar hangar = new Hangar();
        Map<Resource, Integer> expected = new HashMap<>() {{
            put(Resource.METAL, 5);
        }};
        Map<Resource, Integer> actual = null;
        try {
            actual = hangar.getUpgradeCost();
        } catch (UpgradeNotAvailableException ignored) {
        }
        assertEquals(expected, actual);
    }

    @Test
    void getUpgradeCostLevel3() {
        Hangar hangar = new Hangar();
        hangar.upgrade();
        hangar.upgrade();
        Map<Resource, Integer> expected = new HashMap<>() {{
            put(Resource.METAL, 100);
            put(Resource.SILICONE, 100);
            put(Resource.CRYSTAL, 50);
        }};
        Map<Resource, Integer> actual = null;
        try {
            actual = hangar.getUpgradeCost();
        } catch (UpgradeNotAvailableException ignored) {
        }
        assertEquals(expected, actual);
    }

    @Test
    void getUpgradeCostThrows() {
        Hangar hangar = new Hangar();
        hangar.upgrade();
        hangar.upgrade();
        hangar.upgrade();
        hangar.upgrade();
        assertThrows(UpgradeNotAvailableException.class, hangar::getUpgradeCost);
    }

    @Test
    void upgradeSuccess() {
        Hangar hangar = new Hangar();
        hangar.upgrade();
        int expected = 2;
        assertEquals(expected, hangar.getCurrentLevel());
    }

    @Test
    void upgradeMaxLevelExceeded() {
        Hangar hangar = new Hangar();
        hangar.upgrade();
        hangar.upgrade();
        hangar.upgrade();
        hangar.upgrade();
        hangar.upgrade();
        hangar.upgrade();
        int expected = 5;
        assertEquals(expected, hangar.getCurrentLevel());
    }
}