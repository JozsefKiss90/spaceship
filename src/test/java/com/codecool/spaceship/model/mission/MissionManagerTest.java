package com.codecool.spaceship.model.mission;

import com.codecool.spaceship.model.Location;
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
}