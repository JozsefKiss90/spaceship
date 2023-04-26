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
        Event event1 = Event.builder()
                .eventType(EventType.START)
                .endTime(now)
                .eventMessage("Left station for mining mission on Test planet.")
                .build();
        Event event2 = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.plusSeconds(7200L))
                .build();
        expected.getEvents().push(event1);
        expected.getEvents().push(event2);

        Mission actual = missionManager.startMiningMission(minerShipMock, locationMock, 1800L);

        assertEquals(expected, actual);
    }

    @Test
    void noUpdateStatusBeforeNextUpdate() {
        Mission mission = Mission.builder()
                .events(new Stack<>())
                .build();
        mission.getEvents().push(eventMock);
        when(eventMock.getEndTime()).thenReturn(LocalDateTime.now().plusSeconds(1));

        missionManager.updateStatus(mission);
        verify(eventMock, never()).getEventType();
    }

    @Test
    void noUpdateStatusWhenMissionIsOver() {
        Mission mission = Mission.builder()
                .currentStatus(MissionStatus.OVER)
                .events(new Stack<>())
                .build();
        mission.getEvents().push(eventMock);

        missionManager.updateStatus(mission);
        verify(eventMock, never()).getEventType();
    }

    @Test
    void updateStatusAfterStart() {
        LocalDateTime now = LocalDateTime.now();

        Event event1 = Event.builder()
                .eventType(EventType.START)
                .endTime(now.minusSeconds(5))
                .build();
        Event event2 = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.plusSeconds(5))
                .build();

        Mission expected = Mission.builder()
                .currentStatus(MissionStatus.EN_ROUTE)
                .currentObjectiveTime(now.plusSeconds(5))
                .events(new Stack<>())
                .build();
        expected.getEvents().push(event1);
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
    void updateStatusArriveAtLocationWithTimeToFill() {
        when(locationMock.getName()).thenReturn("Test Planet");
        when(minerShipMock.getDrillEfficiency()).thenReturn(5);
        when(minerShipMock.getEmptyStorageSpace()).thenReturn(8);
        LocalDateTime now = LocalDateTime.now();
        Event event1 = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.minusSeconds(5))
                .build();
        Event event2 = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.minusSeconds(5))
                .eventMessage("Arrived on Test Planet. Starting mining operation.")
                .build();
        Event event3  = Event.builder()
                .eventType(EventType.MINING_COMPLETE)
                .endTime(now.plusSeconds(5755))
                .build();

        Mission expected = Mission.builder()
                .currentStatus(MissionStatus.IN_PROGRESS)
                .currentObjectiveTime(now.plusSeconds(7195))
                .activityDurationInSecs(7200L)
                .missionType(MissionType.MINING)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new Stack<>())
                .build();
        expected.getEvents().push(event2);
        expected.getEvents().push(event3);


        Mission actual = Mission.builder()
                .currentStatus(MissionStatus.EN_ROUTE)
                .currentObjectiveTime(now.minusSeconds(5))
                .activityDurationInSecs(7200L)
                .missionType(MissionType.MINING)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new Stack<>())
                .build();
        actual.getEvents().push(event1);

        missionManager.updateStatus(actual);

        assertEquals(expected, actual);
    }

    @Test
    void updateStatusArriveAtLocationWithNoTimeToFill() {
        when(locationMock.getName()).thenReturn("Test Planet");
        when(minerShipMock.getDrillEfficiency()).thenReturn(1);
        when(minerShipMock.getEmptyStorageSpace()).thenReturn(8);
        LocalDateTime now = LocalDateTime.now();
        Event event1 = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.minusSeconds(5))
                .build();
        Event event2 = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.minusSeconds(5))
                .eventMessage("Arrived on Test Planet. Starting mining operation.")
                .build();
        Event event3  = Event.builder()
                .eventType(EventType.MINING_COMPLETE)
                .endTime(now.plusSeconds(7195))
                .build();

        Mission expected = Mission.builder()
                .currentStatus(MissionStatus.IN_PROGRESS)
                .currentObjectiveTime(now.plusSeconds(7195))
                .activityDurationInSecs(7200L)
                .missionType(MissionType.MINING)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new Stack<>())
                .build();
        expected.getEvents().push(event2);
        expected.getEvents().push(event3);


        Mission actual = Mission.builder()
                .currentStatus(MissionStatus.EN_ROUTE)
                .currentObjectiveTime(now.minusSeconds(5))
                .activityDurationInSecs(7200L)
                .missionType(MissionType.MINING)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new Stack<>())
                .build();
        actual.getEvents().push(event1);

        missionManager.updateStatus(actual);

        assertEquals(expected, actual);
    }

    @Test
    void finishMiningWithTimeToFill() throws StorageException {
        when(locationMock.getResourceType()).thenReturn(Resource.CRYSTAL);
        when(minerShipMock.getEmptyStorageSpace()).thenReturn(8, 0);
        LocalDateTime now = LocalDateTime.now();

        Event event1 = Event.builder()
                .eventType(EventType.MINING_COMPLETE)
                .endTime(now.minusSeconds(4))
                .build();
        Event event2 = Event.builder()
                .eventType(EventType.MINING_COMPLETE)
                .endTime(now.minusSeconds(4))
                .eventMessage("Storage is full. Mined 8 CRYSTAL(s). Returning to station.")
                .build();
        Event event3= Event.builder()
                .eventType(EventType.RETURNED_TO_STATION)
                .endTime(now.plusSeconds(7196))
                .build();

        Mission expected = Mission.builder()
                .currentStatus(MissionStatus.RETURNING)
                .currentObjectiveTime(now.plusSeconds(7196))
                .travelDurationInSecs(7200L)
                .missionType(MissionType.MINING)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new Stack<>())
                .build();
        expected.getEvents().push(event2);
        expected.getEvents().push(event3);

        Mission actual = Mission.builder()
                .currentStatus(MissionStatus.IN_PROGRESS)
                .currentObjectiveTime(now.minusSeconds(5))
                .travelDurationInSecs(7200L)
                .missionType(MissionType.MINING)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new Stack<>())
                .build();
        actual.getEvents().push(event1);


        missionManager.updateStatus(actual);

        verify(minerShipMock, times(1)).addResourceToStorage(Resource.CRYSTAL, 8);
        assertEquals(expected, actual);
    }

    @Test
    void finishMiningWithNoTimeToFill() throws StorageException {
        when(locationMock.getResourceType()).thenReturn(Resource.CRYSTAL);
        when(minerShipMock.getDrillEfficiency()).thenReturn(1);
        when(minerShipMock.getEmptyStorageSpace()).thenReturn(8, 6);
        LocalDateTime now = LocalDateTime.now();

        Event event1 = Event.builder()
                .eventType(EventType.MINING_COMPLETE)
                .endTime(now.minusSeconds(4))
                .build();
        Event event2 = Event.builder()
                .eventType(EventType.MINING_COMPLETE)
                .endTime(now.minusSeconds(4))
                .eventMessage("Mining complete. Mined 2 CRYSTAL(s). Returning to station.")
                .build();
        Event event3= Event.builder()
                .eventType(EventType.RETURNED_TO_STATION)
                .endTime(now.plusSeconds(7196))
                .build();

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
        expected.getEvents().push(event2);
        expected.getEvents().push(event3);

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
        actual.getEvents().push(event1);


        missionManager.updateStatus(actual);

        verify(minerShipMock, times(1)).addResourceToStorage(Resource.CRYSTAL, 2);
        assertEquals(expected, actual);
    }

}