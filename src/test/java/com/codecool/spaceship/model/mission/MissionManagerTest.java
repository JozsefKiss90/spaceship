package com.codecool.spaceship.model.mission;

import com.codecool.spaceship.model.Location;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.MinerShipManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
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
    void missionCreationTest() {
        when(minerShipMock.getEngineLevel()).thenReturn(2);
        when(locationMock.getDistanceFromStation()).thenReturn(5);
        LocalDateTime now = LocalDateTime.now();

        Mission expected = Mission.builder()
                .startTime(now)
                .currentObjectiveTime(now.plusSeconds(7200L))
                .currentStatus(MissionStatus.EN_ROUTE)
                .missionType(MissionType.MINING)
                .location(locationMock)
                .ship(minerShipMock)
                .travelDurationInSecs(7200L)
                .activityDurationInSecs(1800L)
                .events(new ArrayList<>())
                .build();

        Mission actual = MissionManager.startMiningMission(minerShipMock, locationMock, 1800L);

        assertEquals(expected, actual);
    }

    @Test
    void noUpdateStatusBeforeNextUpdateTest() {
        Mission mission = Mission.builder()
                .events(new ArrayList<>())
                .build();
        mission.getEvents().add(eventMock);
        when(eventMock.getEndTime()).thenReturn(LocalDateTime.now().plusSeconds(1));

        MissionManager missionManager = new MissionManager(mission);
        missionManager.updateStatus();

        verify(eventMock, never()).getEventType();
    }

    @Test
    void noUpdateStatusWhenMissionIsOverTest() {
        Mission mission = Mission.builder()
                .currentStatus(MissionStatus.OVER)
                .events(new ArrayList<>())
                .build();
        mission.getEvents().add(eventMock);

        MissionManager missionManager = new MissionManager(mission);
        missionManager.updateStatus();

        verify(eventMock, never()).getEventType();
    }

    @Test
    void updateStatusAfterStartTest() {
        LocalDateTime now = LocalDateTime.now();

        Mission expected = Mission.builder()
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
                .currentStatus(MissionStatus.EN_ROUTE)
                .currentObjectiveTime(now.plusSeconds(5))
                .events(new ArrayList<>())
                .build();
        actual.getEvents().add(event1);

        MissionManager missionManager = new MissionManager(actual);
        missionManager.updateStatus();

        assertEquals(expected, actual);
    }

    @Test
    void updateStatusArriveAtLocationWithTimeToFillTest() {
        when(locationMock.getName()).thenReturn("Test Planet");
        when(minerShipManagerMock.getDrillEfficiency()).thenReturn(5);
        when(minerShipManagerMock.getEmptyStorageSpace()).thenReturn(8);
        LocalDateTime now = LocalDateTime.now();

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
                .eventMessage("<%tF %<tT> Arrived on Test Planet. Starting mining operation.".formatted(now.minusSeconds(5)))
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

        MissionManager missionManager = new MissionManager(actual);
        missionManager.setMinerShipManager(minerShipManagerMock);
        missionManager.updateStatus();

        assertEquals(expected, actual);
    }

    @Test
    void updateStatusArriveAtLocationWithNoTimeToFillTest() {
        when(locationMock.getName()).thenReturn("Test Planet");
        when(minerShipManagerMock.getDrillEfficiency()).thenReturn(1);
        when(minerShipManagerMock.getEmptyStorageSpace()).thenReturn(8);
        LocalDateTime now = LocalDateTime.now();

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
                .eventMessage("<%tF %<tT> Arrived on Test Planet. Starting mining operation.".formatted(now.minusSeconds(5)))
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

        MissionManager missionManager = new MissionManager(actual);
        missionManager.setMinerShipManager(minerShipManagerMock);
        missionManager.updateStatus();

        assertEquals(expected, actual);
    }

    @Test
    void finishMiningWithTimeToFillTest() throws StorageException {
        when(locationMock.getResourceType()).thenReturn(ResourceType.CRYSTAL);
        when(minerShipManagerMock.getEmptyStorageSpace()).thenReturn(8, 0);
        LocalDateTime now = LocalDateTime.now();

        Mission expected = Mission.builder()
                .currentStatus(MissionStatus.RETURNING)
                .currentObjectiveTime(now.plusSeconds(7196))
                .travelDurationInSecs(7200L)
                .missionType(MissionType.MINING)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new ArrayList<>())
                .build();
        Event expectedEvent1 = Event.builder()
                .eventType(EventType.MINING_COMPLETE)
                .endTime(now.minusSeconds(4))
                .eventMessage("<%tF %<tT> Storage is full. Mined 8 CRYSTAL(s). Returning to station.".formatted(now.minusSeconds(4)))
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

        MissionManager missionManager = new MissionManager(actual);
        missionManager.setMinerShipManager(minerShipManagerMock);
        missionManager.updateStatus();

        verify(minerShipManagerMock, times(1)).addResourceToStorage(ResourceType.CRYSTAL, 8);
        assertEquals(expected, actual);
    }

    @Test
    void finishMiningWithNoTimeToFillTest() throws StorageException {
        when(locationMock.getResourceType()).thenReturn(ResourceType.CRYSTAL);
        when(minerShipManagerMock.getDrillEfficiency()).thenReturn(1);
        when(minerShipManagerMock.getEmptyStorageSpace()).thenReturn(8, 6);
        LocalDateTime now = LocalDateTime.now();

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
                .eventMessage("<%tF %<tT> Mining complete. Mined 2 CRYSTAL(s). Returning to station.".formatted(now.minusSeconds(4)))
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

        MissionManager missionManager = new MissionManager(actual);
        missionManager.setMinerShipManager(minerShipManagerMock);
        missionManager.updateStatus();

        verify(minerShipManagerMock, times(1)).addResourceToStorage(ResourceType.CRYSTAL, 2);
        assertEquals(expected, actual);
    }

    @Test
    void updateStatusMissionEndTest() {
        LocalDateTime now = LocalDateTime.now();

        Mission expected = Mission.builder()
                .currentStatus(MissionStatus.OVER)
                .currentObjectiveTime(now.minusSeconds(5))
                .events(new ArrayList<>())
                .build();
        Event expectedEvent = Event.builder()
                .eventType(EventType.RETURNED_TO_STATION)
                .endTime(now.minusSeconds(5))
                .eventMessage("<%tF %<tT> Returned to station.".formatted(now.minusSeconds(5)))
                .build();
        expected.getEvents().add(expectedEvent);

        Mission actual = Mission.builder()
                .currentStatus(MissionStatus.RETURNING)
                .currentObjectiveTime(now.minusSeconds(5))
                .events(new ArrayList<>())
                .build();
        Event actualEvent = Event.builder()
                .eventType(EventType.RETURNED_TO_STATION)
                .endTime(now.minusSeconds(5))
                .build();
        actual.getEvents().add(actualEvent);

        MissionManager missionManager = new MissionManager(actual);
        missionManager.setMinerShipManager(minerShipManagerMock);
        missionManager.updateStatus();

        assertEquals(expected, actual);
    }

    @Test
    void updateStatusStartToFinishTest() throws StorageException {
        when(locationMock.getName()).thenReturn("Test Planet");
        when(locationMock.getResourceType()).thenReturn(ResourceType.CRYSTAL);
        when(minerShipManagerMock.getDrillEfficiency()).thenReturn(5);
        when(minerShipManagerMock.getEmptyStorageSpace()).thenReturn(20,20, 12);
        LocalDateTime now = LocalDateTime.now();

        Mission expected = Mission.builder()
                .startTime(now.minusSeconds(20000))
                .currentObjectiveTime(now.minusSeconds(2900))
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
                .endTime(now.minusSeconds(20000))
                .eventMessage("<%tF %<tT> Left station for mining mission on Test planet.".formatted(now.minusSeconds(20000)))
                .build();
        expected.getEvents().add(expectedEvent1);
        Event expectedEvent2 = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.minusSeconds(14600))
                .eventMessage("<%tF %<tT> Arrived on Test Planet. Starting mining operation.".formatted(now.minusSeconds(14600)))
                .build();
        expected.getEvents().add(expectedEvent2);
        Event expectedEvent3 = Event.builder()
                .eventType(EventType.MINING_COMPLETE)
                .endTime(now.minusSeconds(8300))
                .eventMessage("<%tF %<tT> Mining complete. Mined 8 CRYSTAL(s). Returning to station.".formatted(now.minusSeconds(8300)))
                .build();
        expected.getEvents().add(expectedEvent3);
        Event expectedEvent4 = Event.builder()
                .eventType(EventType.RETURNED_TO_STATION)
                .endTime(now.minusSeconds(2900))
                .eventMessage("<%tF %<tT> Returned to station.".formatted(now.minusSeconds(2900)))
                .build();
        expected.getEvents().add(expectedEvent4);


        Mission actual = Mission.builder()
                .startTime(now.minusSeconds(20000))
                .currentObjectiveTime(now.minusSeconds(14600))
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
                .endTime(now.minusSeconds(20000))
                .eventMessage("<%tF %<tT> Left station for mining mission on Test planet.".formatted(now.minusSeconds(20000)))
                .build();
        actual.getEvents().add(actualEvent);

        MissionManager missionManager = new MissionManager(actual);
        missionManager.setMinerShipManager(minerShipManagerMock);
        missionManager.updateStatus();

        verify(minerShipManagerMock, times(1)).addResourceToStorage(ResourceType.CRYSTAL, 8);
        assertEquals(expected, actual);
    }

}