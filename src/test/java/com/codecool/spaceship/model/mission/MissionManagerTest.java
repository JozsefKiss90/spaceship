package com.codecool.spaceship.model.mission;

import com.codecool.spaceship.model.Location;
import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.ship.MinerShip;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MissionManagerTest {

    @Mock
    MinerShip minerShipMock;
    @Mock
    Location locationMock;
    @Mock
    Event eventMock;
    MissionManager missionManager = new MissionManager();

    @Test
    void missionCreationTest() {
        when(minerShipMock.getSpeed()).thenReturn(2.5);
        when(locationMock.getDistanceFromStation()).thenReturn(5);
        when(locationMock.getName()).thenReturn("Test planet");
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
                .events(new Stack<>())
                .build();
        Event expectedEvent1 = Event.builder()
                .eventType(EventType.START)
                .endTime(now)
                .eventMessage("<%tF %<tT> Left station for mining mission on Test planet.".formatted(now))
                .build();
        Event expectedEvent2 = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.plusSeconds(7200L))
                .build();
        expected.getEvents().push(expectedEvent1);
        expected.getEvents().push(expectedEvent2);

        Mission actual = missionManager.startMiningMission(minerShipMock, locationMock, 1800L);

        assertEquals(expected, actual);
    }

    @Test
    void noUpdateStatusBeforeNextUpdateTest() {
        Mission mission = Mission.builder()
                .events(new Stack<>())
                .build();
        mission.getEvents().push(eventMock);
        when(eventMock.getEndTime()).thenReturn(LocalDateTime.now().plusSeconds(1));

        missionManager.updateStatus(mission);

        verify(eventMock, never()).getEventType();
    }

    @Test
    void noUpdateStatusWhenMissionIsOverTest() {
        Mission mission = Mission.builder()
                .currentStatus(MissionStatus.OVER)
                .events(new Stack<>())
                .build();
        mission.getEvents().push(eventMock);

        missionManager.updateStatus(mission);

        verify(eventMock, never()).getEventType();
    }

    @Test
    void updateStatusAfterStartTest() {
        LocalDateTime now = LocalDateTime.now();

        Mission expected = Mission.builder()
                .currentStatus(MissionStatus.EN_ROUTE)
                .currentObjectiveTime(now.plusSeconds(5))
                .events(new Stack<>())
                .build();
        Event event1 = Event.builder()
                .eventType(EventType.START)
                .endTime(now.minusSeconds(5))
                .build();
        expected.getEvents().push(event1);
        Event event2 = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.plusSeconds(5))
                .build();
        expected.getEvents().push(event2);

        Mission actual = Mission.builder()
                .currentStatus(MissionStatus.EN_ROUTE)
                .currentObjectiveTime(now.plusSeconds(5))
                .events(new Stack<>())
                .build();
        actual.getEvents().push(event1);

        missionManager.updateStatus(actual);

        assertEquals(expected, actual);
    }

    @Test
    void updateStatusArriveAtLocationWithTimeToFillTest() {
        when(locationMock.getName()).thenReturn("Test Planet");
        when(minerShipMock.getDrillEfficiency()).thenReturn(5);
        when(minerShipMock.getEmptyStorageSpace()).thenReturn(8);
        LocalDateTime now = LocalDateTime.now();

        Mission expected = Mission.builder()
                .currentStatus(MissionStatus.IN_PROGRESS)
                .currentObjectiveTime(now.plusSeconds(7195))
                .activityDurationInSecs(7200L)
                .missionType(MissionType.MINING)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new Stack<>())
                .build();
        Event expectedEvent1 = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.minusSeconds(5))
                .eventMessage("<%tF %<tT> Arrived on Test Planet. Starting mining operation.".formatted(now.minusSeconds(5)))
                .build();
        expected.getEvents().push(expectedEvent1);
        Event expectedEvent2 = Event.builder()
                .eventType(EventType.MINING_COMPLETE)
                .endTime(now.plusSeconds(5755))
                .build();
        expected.getEvents().push(expectedEvent2);

        Mission actual = Mission.builder()
                .currentStatus(MissionStatus.EN_ROUTE)
                .currentObjectiveTime(now.minusSeconds(5))
                .activityDurationInSecs(7200L)
                .missionType(MissionType.MINING)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new Stack<>())
                .build();
        Event actualEvent = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.minusSeconds(5))
                .build();
        actual.getEvents().push(actualEvent);

        missionManager.updateStatus(actual);

        assertEquals(expected, actual);
    }

    @Test
    void updateStatusArriveAtLocationWithNoTimeToFillTest() {
        when(locationMock.getName()).thenReturn("Test Planet");
        when(minerShipMock.getDrillEfficiency()).thenReturn(1);
        when(minerShipMock.getEmptyStorageSpace()).thenReturn(8);
        LocalDateTime now = LocalDateTime.now();

        Mission expected = Mission.builder()
                .currentStatus(MissionStatus.IN_PROGRESS)
                .currentObjectiveTime(now.plusSeconds(7195))
                .activityDurationInSecs(7200L)
                .missionType(MissionType.MINING)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new Stack<>())
                .build();
        Event expectedEvent1 = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.minusSeconds(5))
                .eventMessage("<%tF %<tT> Arrived on Test Planet. Starting mining operation.".formatted(now.minusSeconds(5)))
                .build();
        expected.getEvents().push(expectedEvent1);
        Event expectedEvent2  = Event.builder()
                .eventType(EventType.MINING_COMPLETE)
                .endTime(now.plusSeconds(7195))
                .build();
        expected.getEvents().push(expectedEvent2);

        Mission actual = Mission.builder()
                .currentStatus(MissionStatus.EN_ROUTE)
                .currentObjectiveTime(now.minusSeconds(5))
                .activityDurationInSecs(7200L)
                .missionType(MissionType.MINING)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new Stack<>())
                .build();
        Event actualEvent = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.minusSeconds(5))
                .build();
        actual.getEvents().push(actualEvent);

        missionManager.updateStatus(actual);

        assertEquals(expected, actual);
    }

    @Test
    void finishMiningWithTimeToFillTest() throws StorageException {
        when(locationMock.getResourceType()).thenReturn(Resource.CRYSTAL);
        when(minerShipMock.getEmptyStorageSpace()).thenReturn(8, 0);
        LocalDateTime now = LocalDateTime.now();

        Mission expected = Mission.builder()
                .currentStatus(MissionStatus.RETURNING)
                .currentObjectiveTime(now.plusSeconds(7196))
                .travelDurationInSecs(7200L)
                .missionType(MissionType.MINING)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new Stack<>())
                .build();
        Event expectedEvent1 = Event.builder()
                .eventType(EventType.MINING_COMPLETE)
                .endTime(now.minusSeconds(4))
                .eventMessage("<%tF %<tT> Storage is full. Mined 8 CRYSTAL(s). Returning to station.".formatted(now.minusSeconds(4)))
                .build();
        expected.getEvents().push(expectedEvent1);
        Event expectedEvent2 = Event.builder()
                .eventType(EventType.RETURNED_TO_STATION)
                .endTime(now.plusSeconds(7196))
                .build();
        expected.getEvents().push(expectedEvent2);

        Mission actual = Mission.builder()
                .currentStatus(MissionStatus.IN_PROGRESS)
                .currentObjectiveTime(now.minusSeconds(5))
                .travelDurationInSecs(7200L)
                .missionType(MissionType.MINING)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new Stack<>())
                .build();
        Event actualEvent = Event.builder()
                .eventType(EventType.MINING_COMPLETE)
                .endTime(now.minusSeconds(4))
                .build();
        actual.getEvents().push(actualEvent);

        missionManager.updateStatus(actual);

        verify(minerShipMock, times(1)).addResourceToStorage(Resource.CRYSTAL, 8);
        assertEquals(expected, actual);
    }

    @Test
    void finishMiningWithNoTimeToFillTest() throws StorageException {
        when(locationMock.getResourceType()).thenReturn(Resource.CRYSTAL);
        when(minerShipMock.getDrillEfficiency()).thenReturn(1);
        when(minerShipMock.getEmptyStorageSpace()).thenReturn(8, 6);
        LocalDateTime now = LocalDateTime.now();

        Mission expected = Mission.builder()
                .currentStatus(MissionStatus.RETURNING)
                .currentObjectiveTime(now.plusSeconds(7196))
                .activityDurationInSecs(7200L)
                .travelDurationInSecs(7200L)
                .missionType(MissionType.MINING)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new Stack<>())
                .build();
        Event expectedEvent1 = Event.builder()
                .eventType(EventType.MINING_COMPLETE)
                .endTime(now.minusSeconds(4))
                .eventMessage("<%tF %<tT> Mining complete. Mined 2 CRYSTAL(s). Returning to station.".formatted(now.minusSeconds(4)))
                .build();
        expected.getEvents().push(expectedEvent1);
        Event expectedEvent2 = Event.builder()
                .eventType(EventType.RETURNED_TO_STATION)
                .endTime(now.plusSeconds(7196))
                .build();
        expected.getEvents().push(expectedEvent2);

        Mission actual = Mission.builder()
                .currentStatus(MissionStatus.IN_PROGRESS)
                .currentObjectiveTime(now.minusSeconds(4))
                .activityDurationInSecs(7200L)
                .travelDurationInSecs(7200L)
                .missionType(MissionType.MINING)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new Stack<>())
                .build();
        Event actualEvent = Event.builder()
                .eventType(EventType.MINING_COMPLETE)
                .endTime(now.minusSeconds(4))
                .build();
        actual.getEvents().push(actualEvent);

        missionManager.updateStatus(actual);

        verify(minerShipMock, times(1)).addResourceToStorage(Resource.CRYSTAL, 2);
        assertEquals(expected, actual);
    }

    @Test
    void updateStatusMissionEndTest() {
        LocalDateTime now = LocalDateTime.now();

        Mission expected = Mission.builder()
                .currentStatus(MissionStatus.OVER)
                .currentObjectiveTime(now.minusSeconds(5))
                .events(new Stack<>())
                .build();
        Event expectedEvent = Event.builder()
                .eventType(EventType.RETURNED_TO_STATION)
                .endTime(now.minusSeconds(5))
                .eventMessage("<%tF %<tT> Returned to station.".formatted(now.minusSeconds(5)))
                .build();
        expected.getEvents().push(expectedEvent);

        Mission actual = Mission.builder()
                .currentStatus(MissionStatus.RETURNING)
                .currentObjectiveTime(now.minusSeconds(5))
                .events(new Stack<>())
                .build();
        Event actualEvent = Event.builder()
                .eventType(EventType.RETURNED_TO_STATION)
                .endTime(now.minusSeconds(5))
                .build();
        actual.getEvents().push(actualEvent);

        missionManager.updateStatus(actual);

        assertEquals(expected, actual);
    }

    @Test
    void updateStatusStartToFinishTest() throws StorageException {
        when(locationMock.getName()).thenReturn("Test Planet");
        when(locationMock.getResourceType()).thenReturn(Resource.CRYSTAL);
        when(minerShipMock.getDrillEfficiency()).thenReturn(5);
        when(minerShipMock.getEmptyStorageSpace()).thenReturn(20,20, 12);
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
                .events(new Stack<>())
                .build();
        Event expectedEvent1 = Event.builder()
                .eventType(EventType.START)
                .endTime(now.minusSeconds(20000))
                .eventMessage("<%tF %<tT> Left station for mining mission on Test planet.".formatted(now.minusSeconds(20000)))
                .build();
        expected.getEvents().push(expectedEvent1);
        Event expectedEvent2 = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.minusSeconds(14600))
                .eventMessage("<%tF %<tT> Arrived on Test Planet. Starting mining operation.".formatted(now.minusSeconds(14600)))
                .build();
        expected.getEvents().push(expectedEvent2);
        Event expectedEvent3 = Event.builder()
                .eventType(EventType.MINING_COMPLETE)
                .endTime(now.minusSeconds(8300))
                .eventMessage("<%tF %<tT> Mining complete. Mined 8 CRYSTAL(s). Returning to station.".formatted(now.minusSeconds(8300)))
                .build();
        expected.getEvents().push(expectedEvent3);
        Event expectedEvent4 = Event.builder()
                .eventType(EventType.RETURNED_TO_STATION)
                .endTime(now.minusSeconds(2900))
                .eventMessage("<%tF %<tT> Returned to station.".formatted(now.minusSeconds(2900)))
                .build();
        expected.getEvents().push(expectedEvent4);


        Mission actual = Mission.builder()
                .startTime(now.minusSeconds(20000))
                .currentObjectiveTime(now.minusSeconds(14600))
                .currentStatus(MissionStatus.EN_ROUTE)
                .missionType(MissionType.MINING)
                .location(locationMock)
                .ship(minerShipMock)
                .travelDurationInSecs(5400L)
                .activityDurationInSecs(6300L)
                .events(new Stack<>())
                .build();
        Event actualEvent = Event.builder()
                .eventType(EventType.START)
                .endTime(now.minusSeconds(20000))
                .eventMessage("<%tF %<tT> Left station for mining mission on Test planet.".formatted(now.minusSeconds(20000)))
                .build();
        actual.getEvents().push(actualEvent);

        missionManager.updateStatus(actual);

        verify(minerShipMock, times(1)).addResourceToStorage(Resource.CRYSTAL, 8);
        assertEquals(expected, actual);
    }

}