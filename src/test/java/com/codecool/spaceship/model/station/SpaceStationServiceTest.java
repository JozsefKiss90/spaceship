package com.codecool.spaceship.model.station;

import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.ship.MinerShipService;
import com.codecool.spaceship.model.ship.SpaceShipService;
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
class SpaceStationServiceTest {

    @Mock
    MinerShipService ship;
    @Mock
    MinerShipService ship2;
    @Mock
    MinerShipService ship3;

    @Test
    void addNewShipSuccess() {
        SpaceStationService spaceStationService = new SpaceStationService("test");
        when(ship.getCost()).thenReturn(Map.of());
        try {
            assertTrue(spaceStationService.addNewShip(ship));
        } catch (StorageException ignored) {
        }
    }

    @Test
    void addNewShipNotEnoughResource() {
        SpaceStationService spaceStationService = new SpaceStationService("test");
        when(ship.getCost()).thenReturn(Map.of(Resource.METAL, 1));
        assertThrows(StorageException.class, () -> spaceStationService.addNewShip(ship));
    }

    @Test
    void addNewShipNotEnoughDocks() {
        SpaceStationService spaceStationService = new SpaceStationService("test");
        when(ship.getCost()).thenReturn(Map.of());
        when(ship2.getCost()).thenReturn(Map.of());
        when(ship3.getCost()).thenReturn(Map.of());
        try {
            spaceStationService.addNewShip(ship);
            spaceStationService.addNewShip(ship2);
        } catch (StorageException ignored) {
        }
        assertThrows(StorageException.class, () -> spaceStationService.addNewShip(ship3));
    }

    @Test
    void addNewShipShipAlreadyAdded() {
        SpaceStationService spaceStationService = new SpaceStationService("test");
        when(ship.getCost()).thenReturn(Map.of());
        try {
            spaceStationService.addNewShip(ship);
            assertFalse(spaceStationService.addNewShip(ship));
        } catch (StorageException ignored) {
        }
    }

    @Test
    void deleteShipSuccess() {
        SpaceStationService spaceStationService = new SpaceStationService("test");
        when(ship.getCost()).thenReturn(Map.of());
        try {
            spaceStationService.addNewShip(ship);
        } catch (StorageException ignored) {
        }
        assertTrue(spaceStationService.deleteShip(ship));
    }

    @Test
    void deleteShipNoSuchShip() {
        SpaceStationService spaceStationService = new SpaceStationService("test");
        assertFalse(spaceStationService.deleteShip(ship));
    }

    @Test
    void getAllShipsEmpty() {
        SpaceStationService spaceStationService = new SpaceStationService("test");
        Set<SpaceShipService> expected = Set.of();
        assertEquals(expected, spaceStationService.getAllShips());
    }

    @Test
    void getAllShipsNotEmpty() {
        SpaceStationService spaceStationService = new SpaceStationService("test");
        when(ship.getCost()).thenReturn(Map.of());
        try {
            spaceStationService.addNewShip(ship);
        } catch (StorageException ignored) {
        }
        Set<SpaceShipService> expected = Set.of(ship);
        assertEquals(expected, spaceStationService.getAllShips());
    }

    @Mock
    Drill drill;

    @Test
    void upgradeShipPartSuccess() {
        SpaceStationService spaceStationService = new SpaceStationService("test");
        try {
            when(ship.getCost()).thenReturn(Map.of());
            spaceStationService.addNewShip(ship);
            when(ship.isAvailable()).thenReturn(true);
            when(ship.getPart(ShipPart.DRILL)).thenReturn(drill);
            when(drill.getUpgradeCost()).thenReturn(Map.of());
        } catch (Exception ignored) {
        }
        try {
            assertTrue(spaceStationService.upgradeShipPart(ship, ShipPart.DRILL));
        } catch (Exception ignored) {
        }
        verify(drill, times(1)).upgrade();
    }

    @Test
    void upgradeShipPartShipNotInStation() {
        SpaceStationService spaceStationService = new SpaceStationService("test");
        assertThrows(StorageException.class, () -> spaceStationService.upgradeShipPart(ship, ShipPart.DRILL));
    }

    @Test
    void upgradeShipPartShipNotAvailable() {
        SpaceStationService spaceStationService = new SpaceStationService("test");
        when(ship.getCost()).thenReturn(Map.of());
        when(ship.isAvailable()).thenReturn(false);
        try {
            spaceStationService.addNewShip(ship);
        } catch (Exception ignored) {
        }
        assertThrows(UpgradeNotAvailableException.class, () -> spaceStationService.upgradeShipPart(ship, ShipPart.DRILL));
    }

    @Test
    void upgradeShipPartNotEnoughResource() {
        SpaceStationService spaceStationService = new SpaceStationService("test");
        when(ship.getCost()).thenReturn(Map.of());
        when(ship.isAvailable()).thenReturn(true);
        try {
            spaceStationService.addNewShip(ship);
            when(ship.getPart(ShipPart.DRILL)).thenReturn(drill);
            when(drill.getUpgradeCost()).thenReturn(Map.of(Resource.METAL, 1));
        } catch (Exception ignored) {
        }
        assertThrows(StorageException.class, () -> spaceStationService.upgradeShipPart(ship, ShipPart.DRILL));
    }

    @Test
    void addResourceSuccess() {
        SpaceStationService spaceStationService = new SpaceStationService("test");
        try {
            assertTrue(spaceStationService.addResource(Resource.METAL, 1));
        } catch (Exception ignored) {
        }
    }

    @Test
    void addResourceNotEnoughSpace() {
        SpaceStationService spaceStationService = new SpaceStationService("test");
        assertThrows(StorageException.class, () -> spaceStationService.addResource(Resource.METAL, 21));
    }

    @Test
    void upgradeStorageSuccess() {
        SpaceStationService spaceStationService = new SpaceStationService("test");
        try {
            spaceStationService.addResource(Resource.METAL, 5);
        } catch (Exception ignored) {
        }
        assertDoesNotThrow(spaceStationService::upgradeStorage);
    }

    @Test
    void upgradeStorageNotEnoughResource() {
        SpaceStationService spaceStationService = new SpaceStationService("test");
        assertThrows(StorageException.class, spaceStationService::upgradeStorage);
    }

    @Test
    void upgradeHangarSuccess() {
        SpaceStationService spaceStationService = new SpaceStationService("test");
        try {
            spaceStationService.addResource(Resource.METAL, 5);
        } catch (Exception ignored) {
        }
        assertDoesNotThrow(spaceStationService::upgradeHangar);
    }

    @Test
    void upgradeHangarNotEnoughResource() {
        SpaceStationService spaceStationService = new SpaceStationService("test");
        assertThrows(StorageException.class, spaceStationService::upgradeHangar);
    }
}