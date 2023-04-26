package com.codecool.spaceship.model.station;

import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.exception.NoSuchPartException;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.model.ship.shipparts.Drill;
import com.codecool.spaceship.model.ship.shipparts.ShipPart;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpaceStationTest {

    @Mock
    MinerShip ship;
    @Mock
    MinerShip ship2;
    @Mock
    MinerShip ship3;

    @Test
    void addNewShipSuccess() {
        SpaceStation spaceStation = new SpaceStation("test");
        when(ship.getCost()).thenReturn(Map.of());
        try {
            assertTrue(spaceStation.addNewShip(ship));
        } catch (StorageException ignored) {
        }
    }

    @Test
    void addNewShipNotEnoughResource() {
        SpaceStation spaceStation = new SpaceStation("test");
        when(ship.getCost()).thenReturn(Map.of(Resource.METAL, 1));
        assertThrows(StorageException.class, () -> spaceStation.addNewShip(ship));
        assertFalse(spaceStation.getAllShips().contains(ship));
    }

    @Test
    void addNewShipNotEnoughDocks() {
        SpaceStation spaceStation = new SpaceStation("test");
        when(ship.getCost()).thenReturn(Map.of());
        when(ship2.getCost()).thenReturn(Map.of());
        when(ship3.getCost()).thenReturn(Map.of());
        try {
            spaceStation.addNewShip(ship);
            spaceStation.addNewShip(ship2);
        } catch (StorageException ignored) {
        }
        assertThrows(StorageException.class, () -> spaceStation.addNewShip(ship3));
    }

    @Test
    void addNewShipShipAlreadyAdded() {
        SpaceStation spaceStation = new SpaceStation("test");
        when(ship.getCost()).thenReturn(Map.of());
        try {
            spaceStation.addNewShip(ship);
            assertFalse(spaceStation.addNewShip(ship));
        } catch (StorageException ignored) {
        }
    }

    @Test
    void deleteShipSuccess() {
        SpaceStation spaceStation = new SpaceStation("test");
        when(ship.getCost()).thenReturn(Map.of());
        try {
            spaceStation.addNewShip(ship);
        } catch (StorageException ignored) {
        }
        assertTrue(spaceStation.deleteShip(ship));
    }

    @Test
    void deleteShipNoSuchShip() {
        SpaceStation spaceStation = new SpaceStation("test");
        assertFalse(spaceStation.deleteShip(ship));
    }

    @Test
    void getAllShipsEmpty() {
        SpaceStation spaceStation = new SpaceStation("test");
        Set<SpaceShip> expected = Set.of();
        assertEquals(expected, spaceStation.getAllShips());
    }

    @Test
    void getAllShipsNotEmpty() {
        SpaceStation spaceStation = new SpaceStation("test");
        when(ship.getCost()).thenReturn(Map.of());
        try {
            spaceStation.addNewShip(ship);
        } catch (StorageException ignored) {
        }
        Set<SpaceShip> expected = Set.of(ship);
        assertEquals(expected, spaceStation.getAllShips());
    }

    @Mock
    Drill drill;

    @Test
    void upgradeShipPartSuccess() {
        SpaceStation spaceStation = new SpaceStation("test");
        try {
            when(ship.getCost()).thenReturn(Map.of());
            spaceStation.addNewShip(ship);
            when(ship.isAvailable()).thenReturn(true);
            when(ship.getPart(ShipPart.DRILL)).thenReturn(drill);
            when(drill.getUpgradeCost()).thenReturn(Map.of());
        } catch (Exception ignored) {
        }
        try {
            assertTrue(spaceStation.upgradeShipPart(ship, ShipPart.DRILL));
        } catch (Exception ignored) {
        }
        verify(drill, times(1)).upgrade();
    }

    @Test
    void upgradeShipPartShipNotInStation() {
        SpaceStation spaceStation = new SpaceStation("test");
        assertThrows(StorageException.class, () -> spaceStation.upgradeShipPart(ship, ShipPart.DRILL));
    }

    @Test
    void upgradeShipPartShipNotAvailable() {
        SpaceStation spaceStation = new SpaceStation("test");
        when(ship.getCost()).thenReturn(Map.of());
        when(ship.isAvailable()).thenReturn(false);
        try {
            spaceStation.addNewShip(ship);
        } catch (Exception ignored) {
        }
        assertThrows(UpgradeNotAvailableException.class, () -> spaceStation.upgradeShipPart(ship, ShipPart.DRILL));
    }

    @Test
    void upgradeShipPartNotEnoughResource() {
        SpaceStation spaceStation = new SpaceStation("test");
        when(ship.getCost()).thenReturn(Map.of());
        when(ship.isAvailable()).thenReturn(true);
        try {
            spaceStation.addNewShip(ship);
            when(ship.getPart(ShipPart.DRILL)).thenReturn(drill);
            when(drill.getUpgradeCost()).thenReturn(Map.of(Resource.METAL, 1));
        } catch (Exception ignored) {
        }
        assertThrows(StorageException.class, () -> spaceStation.upgradeShipPart(ship, ShipPart.DRILL));
    }

    @Test
    void addResourceSuccess() {
        SpaceStation spaceStation = new SpaceStation("test");
        try {
            assertTrue(spaceStation.addResource(Resource.METAL, 1));
        } catch (Exception ignored) {
        }
    }

    @Test
    void addResourceNotEnoughSpace() {
        SpaceStation spaceStation = new SpaceStation("test");
        assertThrows(StorageException.class, () -> spaceStation.addResource(Resource.METAL, 21));
    }

    @Test
    void upgradeStorageSuccess() {
        SpaceStation spaceStation = new SpaceStation("test");
        try {
            spaceStation.addResource(Resource.METAL, 5);
        } catch (Exception ignored) {
        }
        assertDoesNotThrow(spaceStation::upgradeStorage);
    }

    @Test
    void upgradeStorageNotEnoughResource() {
        SpaceStation spaceStation = new SpaceStation("test");
        assertThrows(StorageException.class, spaceStation::upgradeStorage);
    }

    @Test
    void upgradeHangarSuccess() {
        SpaceStation spaceStation = new SpaceStation("test");
        try {
            spaceStation.addResource(Resource.METAL, 5);
        } catch (Exception ignored) {
        }
        assertDoesNotThrow(spaceStation::upgradeHangar);
    }

    @Test
    void upgradeHangarNotEnoughResource() {
        SpaceStation spaceStation = new SpaceStation("test");
        assertThrows(StorageException.class, spaceStation::upgradeHangar);
    }
}