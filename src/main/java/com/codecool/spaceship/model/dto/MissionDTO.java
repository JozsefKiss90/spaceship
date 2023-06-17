package com.codecool.spaceship.model.dto;

import com.codecool.spaceship.model.mission.Event;
import com.codecool.spaceship.model.mission.Mission;
import com.codecool.spaceship.model.mission.MissionStatus;
import com.codecool.spaceship.model.mission.MissionType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public record MissionDTO(long id, MissionType missionType, MissionStatus status, LocalDateTime currentObjectiveTime, LocalDateTime approxEndTime, List<EventDTO> reports, String location, String shipName, Long shipId) {

    public MissionDTO(Mission mission) {
        this(mission.getId(), mission.getMissionType(), mission.getCurrentStatus(), mission.getCurrentObjectiveTime(),
                mission.getApproxEndTime(), formatEventLog(mission.getEvents()), mission.getLocation().getName(), mission.getShip().getName(), mission.getShip().getId());
    }

    private static List<EventDTO> formatEventLog(List<Event> events) {
        return events.stream()
                .filter(event -> Objects.nonNull(event.getEventMessage()))
                .map(event -> new EventDTO(event.getEndTime(), event.getEventMessage()))
                .toList();
    }
}
