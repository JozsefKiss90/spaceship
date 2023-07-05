package com.codecool.spaceship.model.mission;

import com.codecool.spaceship.model.Location;
import com.codecool.spaceship.model.exception.IllegalOperationException;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.MinerShipManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MissionManagerTest {

    @Mock
    MinerShip minerShipMock;
    @Mock
    MinerShipManager minerShipManagerMock;

    @Mock
    Location locationMock;
    @Mock
    Event eventMock;

    @Test
    void missionCreationTest() throws IllegalOperationException {
        when(minerShipManagerMock.isAvailable()).thenReturn(true);
        when(minerShipManagerMock.getSpeed()).thenReturn(2);
        when(minerShipManagerMock.getMinerShip()).thenReturn(minerShipMock);
        when(locationMock.getDistanceFromStation()).thenReturn(5);
        Clock clock = Clock.fixed(Instant.parse("2022-08-15T09:00:00.00Z"), ZoneId.of("UTC"));
        LocalDateTime now = LocalDateTime.now(clock);

        Mission expected = Mission.builder()
                .startTime(now)
                .currentObjectiveTime(now.plusSeconds(9000L))
                .approxEndTime(now.plusSeconds(19800L))
                .currentStatus(MissionStatus.EN_ROUTE)
                .missionType(MissionType.MINING)
                .location(locationMock)
                .ship(minerShipMock)
                .travelDurationInSecs(9000L)
                .activityDurationInSecs(1800L)
                .events(new ArrayList<>())
                .build();

        Mission actual = MissionManager.startMiningMission(minerShipManagerMock, locationMock, 1800L, clock);

        assertEquals(expected, actual);
    }

    @Test
    void noUpdateStatusBeforeNextUpdateTest() {
        when(minerShipManagerMock.getMinerShip()).thenReturn(minerShipMock);
        Mission mission = Mission.builder()
                .ship(minerShipMock)
                .events(new ArrayList<>())
                .build();
        mission.getEvents().add(eventMock);
        when(eventMock.getEndTime()).thenReturn(LocalDateTime.now().plusSeconds(1));

        MissionManager missionManager = new MissionManager(mission, minerShipManagerMock);
        missionManager.updateStatus();

        verify(eventMock, never()).getEventType();
    }

    @Test
    void noUpdateStatusWhenMissionIsOverTest() {
        when(minerShipManagerMock.getMinerShip()).thenReturn(minerShipMock);
        Mission mission = Mission.builder()
                .ship(minerShipMock)
                .currentStatus(MissionStatus.OVER)
                .events(new ArrayList<>())
                .build();
        mission.getEvents().add(eventMock);

        MissionManager missionManager = new MissionManager(mission, minerShipManagerMock);
        missionManager.updateStatus();

        verify(eventMock, never()).getEventType();
    }

    @Test
    void updateStatusAfterStartTest() {
        when(minerShipManagerMock.getMinerShip()).thenReturn(minerShipMock);
        Clock clock = Clock.fixed(Instant.parse("2022-08-15T09:00:00.00Z"), ZoneId.of("UTC"));
        LocalDateTime now = LocalDateTime.now(clock);

        Mission expected = Mission.builder()
                .ship(minerShipMock)
                .currentStatus(MissionStatus.EN_ROUTE)
                .currentObjectiveTime(now.plusSeconds(5))
                .events(new ArrayList<>())
                .build();
        Event event1 = Event.builder()
                .eventType(EventType.START)
                .endTime(now.minusSeconds(5))
                .build();
        expected.getEvents().add(event1);
        Event event2 = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.plusSeconds(5))
                .build();
        expected.getEvents().add(event2);

        Mission actual = Mission.builder()
                .ship(minerShipMock)
                .currentStatus(MissionStatus.EN_ROUTE)
                .currentObjectiveTime(now.plusSeconds(5))
                .events(new ArrayList<>())
                .build();
        actual.getEvents().add(event1);

        MissionManager missionManager = new MissionManager(actual, clock, minerShipManagerMock);
        missionManager.updateStatus();

        assertEquals(expected, actual);
    }

    @Test
    void updateStatusArriveAtLocationWithTimeToFillTest() {
        when(locationMock.getName()).thenReturn("Test Planet");
        when(minerShipManagerMock.getMinerShip()).thenReturn(minerShipMock);
        when(minerShipManagerMock.getDrillEfficiency()).thenReturn(5);
        when(minerShipManagerMock.getEmptyStorageSpace()).thenReturn(8);
        Clock clock = Clock.fixed(Instant.parse("2022-08-15T09:00:00.00Z"), ZoneId.of("UTC"));
        LocalDateTime now = LocalDateTime.now(clock);

        Mission expected = Mission.builder()
                .currentStatus(MissionStatus.IN_PROGRESS)
                .currentObjectiveTime(now.plusSeconds(7195))
                .activityDurationInSecs(7200L)
                .missionType(MissionType.MINING)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new ArrayList<>())
                .build();
        Event expectedEvent1 = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.minusSeconds(5))
                .eventMessage("Arrived on Test Planet. Starting mining operation.")
                .build();
        expected.getEvents().add(expectedEvent1);
        Event expectedEvent2 = Event.builder()
                .eventType(EventType.MINING_COMPLETE)
                .endTime(now.plusSeconds(5755))
                .build();
        expected.getEvents().add(expectedEvent2);

        Mission actual = Mission.builder()
                .currentStatus(MissionStatus.EN_ROUTE)
                .currentObjectiveTime(now.minusSeconds(5))
                .activityDurationInSecs(7200L)
                .missionType(MissionType.MINING)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new ArrayList<>())
                .build();
        Event actualEvent = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.minusSeconds(5))
                .build();
        actual.getEvents().add(actualEvent);

        MissionManager missionManager = new MissionManager(actual, clock, minerShipManagerMock);
        missionManager.setMinerShipManager(minerShipManagerMock);
        missionManager.updateStatus();

        assertEquals(expected, actual);
    }

    @Test
    void updateStatusArriveAtLocationWithNoTimeToFillTest() {
        when(locationMock.getName()).thenReturn("Test Planet");
        when(minerShipManagerMock.getMinerShip()).thenReturn(minerShipMock);
        when(minerShipManagerMock.getDrillEfficiency()).thenReturn(1);
        when(minerShipManagerMock.getEmptyStorageSpace()).thenReturn(8);
        Clock clock = Clock.fixed(Instant.parse("2022-08-15T09:00:00.00Z"), ZoneId.of("UTC"));
        LocalDateTime now = LocalDateTime.now(clock);

        Mission expected = Mission.builder()
                .currentStatus(MissionStatus.IN_PROGRESS)
                .currentObjectiveTime(now.plusSeconds(7195))
                .activityDurationInSecs(7200L)
                .missionType(MissionType.MINING)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new ArrayList<>())
                .build();
        Event expectedEvent1 = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.minusSeconds(5))
                .eventMessage("Arrived on Test Planet. Starting mining operation.")
                .build();
        expected.getEvents().add(expectedEvent1);
        Event expectedEvent2  = Event.builder()
                .eventType(EventType.MINING_COMPLETE)
                .endTime(now.plusSeconds(7195))
                .build();
        expected.getEvents().add(expectedEvent2);

        Mission actual = Mission.builder()
                .currentStatus(MissionStatus.EN_ROUTE)
                .currentObjectiveTime(now.minusSeconds(5))
                .activityDurationInSecs(7200L)
                .missionType(MissionType.MINING)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new ArrayList<>())
                .build();
        Event actualEvent = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.minusSeconds(5))
                .build();
        actual.getEvents().add(actualEvent);

        MissionManager missionManager = new MissionManager(actual, clock, minerShipManagerMock);
        missionManager.setMinerShipManager(minerShipManagerMock);
        missionManager.updateStatus();

        assertEquals(expected, actual);
    }

    @Test
    void finishMiningWithTimeToFillTest() throws StorageException {
        when(locationMock.getResourceType()).thenReturn(ResourceType.CRYSTAL);
        when(minerShipManagerMock.getMinerShip()).thenReturn(minerShipMock);
        when(minerShipManagerMock.getEmptyStorageSpace()).thenReturn(8, 0);
        Clock clock = Clock.fixed(Instant.parse("2022-08-15T09:00:00.00Z"), ZoneId.of("UTC"));
        LocalDateTime now = LocalDateTime.now(clock);

        Mission expected = Mission.builder()
                .currentStatus(MissionStatus.RETURNING)
                .currentObjectiveTime(now.plusSeconds(7196))
                .approxEndTime(now.plusSeconds(7200L))
                .travelDurationInSecs(7200L)
                .missionType(MissionType.MINING)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new ArrayList<>())
                .build();
        Event expectedEvent1 = Event.builder()
                .eventType(EventType.MINING_COMPLETE)
                .endTime(now.minusSeconds(4))
                .eventMessage("Storage is full. Mined 8 CRYSTAL(s). Returning to station.")
                .build();
        expected.getEvents().add(expectedEvent1);
        Event expectedEvent2 = Event.builder()
                .eventType(EventType.RETURNED_TO_STATION)
                .endTime(now.plusSeconds(7196))
                .build();
        expected.getEvents().add(expectedEvent2);

        Mission actual = Mission.builder()
                .currentStatus(MissionStatus.IN_PROGRESS)
                .currentObjectiveTime(now.minusSeconds(5))
                .travelDurationInSecs(7200L)
                .missionType(MissionType.MINING)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new ArrayList<>())
                .build();
        Event actualEvent = Event.builder()
                .eventType(EventType.MINING_COMPLETE)
                .endTime(now.minusSeconds(4))
                .build();
        actual.getEvents().add(actualEvent);

        MissionManager missionManager = new MissionManager(actual, clock, minerShipManagerMock);
        missionManager.setMinerShipManager(minerShipManagerMock);
        missionManager.updateStatus();

        verify(minerShipManagerMock, times(1)).addResourceToStorage(ResourceType.CRYSTAL, 8);
        assertEquals(expected, actual);
    }

    @Test
    void finishMiningWithNoTimeToFillTest() throws StorageException {
        when(locationMock.getResourceType()).thenReturn(ResourceType.CRYSTAL);
        when(minerShipManagerMock.getMinerShip()).thenReturn(minerShipMock);
        when(minerShipManagerMock.getDrillEfficiency()).thenReturn(1);
        when(minerShipManagerMock.getEmptyStorageSpace()).thenReturn(8, 6);
        Clock clock = Clock.fixed(Instant.parse("2022-08-15T09:00:00.00Z"), ZoneId.of("UTC"));
        LocalDateTime now = LocalDateTime.now(clock);

        Mission expected = Mission.builder()
                .currentStatus(MissionStatus.RETURNING)
                .currentObjectiveTime(now.plusSeconds(7196))
                .activityDurationInSecs(7200L)
                .travelDurationInSecs(7200L)
                .missionType(MissionType.MINING)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new ArrayList<>())
                .build();
        Event expectedEvent1 = Event.builder()
                .eventType(EventType.MINING_COMPLETE)
                .endTime(now.minusSeconds(4))
                .eventMessage("Mining complete. Mined 2 CRYSTAL(s). Returning to station.")
                .build();
        expected.getEvents().add(expectedEvent1);
        Event expectedEvent2 = Event.builder()
                .eventType(EventType.RETURNED_TO_STATION)
                .endTime(now.plusSeconds(7196))
                .build();
        expected.getEvents().add(expectedEvent2);

        Mission actual = Mission.builder()
                .currentStatus(MissionStatus.IN_PROGRESS)
                .currentObjectiveTime(now.minusSeconds(4))
                .activityDurationInSecs(7200L)
                .travelDurationInSecs(7200L)
                .missionType(MissionType.MINING)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new ArrayList<>())
                .build();
        Event actualEvent = Event.builder()
                .eventType(EventType.MINING_COMPLETE)
                .endTime(now.minusSeconds(4))
                .build();
        actual.getEvents().add(actualEvent);

        MissionManager missionManager = new MissionManager(actual, clock, minerShipManagerMock);
        missionManager.setMinerShipManager(minerShipManagerMock);
        missionManager.updateStatus();

        verify(minerShipManagerMock, times(1)).addResourceToStorage(ResourceType.CRYSTAL, 2);
        assertEquals(expected, actual);
    }

    @Test
    void updateStatusMissionEndTest() {
        when(minerShipManagerMock.getMinerShip()).thenReturn(minerShipMock);
        Clock clock = Clock.fixed(Instant.parse("2022-08-15T09:00:00.00Z"), ZoneId.of("UTC"));
        LocalDateTime now = LocalDateTime.now(clock);

        Mission expected = Mission.builder()
                .ship(minerShipMock)
                .currentStatus(MissionStatus.OVER)
                .currentObjectiveTime(now.minusSeconds(5))
                .location(locationMock)
                .events(new ArrayList<>())
                .build();
        Event expectedEvent = Event.builder()
                .eventType(EventType.RETURNED_TO_STATION)
                .endTime(now.minusSeconds(5))
                .eventMessage("Returned to station.")
                .build();
        expected.getEvents().add(expectedEvent);

        Mission actual = Mission.builder()
                .ship(minerShipMock)
                .currentStatus(MissionStatus.RETURNING)
                .currentObjectiveTime(now.minusSeconds(5))
                .location(locationMock)
                .events(new ArrayList<>())
                .build();
        Event actualEvent = Event.builder()
                .eventType(EventType.RETURNED_TO_STATION)
                .endTime(now.minusSeconds(5))
                .build();
        actual.getEvents().add(actualEvent);

        MissionManager missionManager = new MissionManager(actual, clock, minerShipManagerMock);
        missionManager.setMinerShipManager(minerShipManagerMock);
        missionManager.updateStatus();

        assertEquals(expected, actual);
        verify(locationMock, times(1)).setCurrentMission(null);
    }

    @Test
    void updateStatusStartToFinishTest() throws StorageException {
        when(locationMock.getName()).thenReturn("Test Planet");
        when(locationMock.getResourceType()).thenReturn(ResourceType.CRYSTAL);
        when(minerShipManagerMock.getMinerShip()).thenReturn(minerShipMock);
        when(minerShipManagerMock.getDrillEfficiency()).thenReturn(5);
        when(minerShipManagerMock.getEmptyStorageSpace()).thenReturn(20,20, 12);
        Clock clock = Clock.fixed(Instant.parse("2022-08-15T09:00:00.00Z"), ZoneId.of("UTC"));
        LocalDateTime now = LocalDateTime.now(clock);

        Mission expected = Mission.builder()
                .startTime(now.minusSeconds(20000L))
                .currentObjectiveTime(now.minusSeconds(2900L))
                .currentStatus(MissionStatus.OVER)
                .missionType(MissionType.MINING)
                .location(locationMock)
                .ship(minerShipMock)
                .travelDurationInSecs(5400L)
                .activityDurationInSecs(6300L)
                .events(new ArrayList<>())
                .build();
        Event expectedEvent1 = Event.builder()
                .eventType(EventType.START)
                .endTime(now.minusSeconds(20000L))
                .eventMessage("Left station for mining mission on Test planet.")
                .build();
        expected.getEvents().add(expectedEvent1);
        Event expectedEvent2 = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.minusSeconds(14600L))
                .eventMessage("Arrived on Test Planet. Starting mining operation.")
                .build();
        expected.getEvents().add(expectedEvent2);
        Event expectedEvent3 = Event.builder()
                .eventType(EventType.MINING_COMPLETE)
                .endTime(now.minusSeconds(8300L))
                .eventMessage("Mining complete. Mined 8 CRYSTAL(s). Returning to station.")
                .build();
        expected.getEvents().add(expectedEvent3);
        Event expectedEvent4 = Event.builder()
                .eventType(EventType.RETURNED_TO_STATION)
                .endTime(now.minusSeconds(2900L))
                .eventMessage("Returned to station.")
                .build();
        expected.getEvents().add(expectedEvent4);


        Mission actual = Mission.builder()
                .startTime(now.minusSeconds(20000L))
                .currentObjectiveTime(now.minusSeconds(14600L))
                .currentStatus(MissionStatus.EN_ROUTE)
                .missionType(MissionType.MINING)
                .location(locationMock)
                .ship(minerShipMock)
                .travelDurationInSecs(5400L)
                .activityDurationInSecs(6300L)
                .events(new ArrayList<>())
                .build();
        Event actualEvent = Event.builder()
                .eventType(EventType.START)
                .endTime(now.minusSeconds(20000L))
                .eventMessage("Left station for mining mission on Test planet.")
                .build();
        actual.getEvents().add(actualEvent);

        MissionManager missionManager = new MissionManager(actual, clock, minerShipManagerMock);
        missionManager.setMinerShipManager(minerShipManagerMock);
        missionManager.updateStatus();

        verify(minerShipManagerMock, times(1)).addResourceToStorage(ResourceType.CRYSTAL, 8);
        assertEquals(expected, actual);
    }

}