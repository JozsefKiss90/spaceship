package com.codecool.spaceship.model.dto;

import com.codecool.spaceship.model.mission.Event;
import com.codecool.spaceship.model.mission.Mission;
import com.codecool.spaceship.model.mission.MissionStatus;
import com.codecool.spaceship.model.mission.MissionType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public record MissionDTO(long id, MissionType missionType, MissionStatus status, LocalDateTime currentObjectiveTime, LocalDateTime approxEndTime, String eventLog, String location) {

    public MissionDTO(Mission mission) {
        this(mission.getId(), mission.getMissionType(), mission.getCurrentStatus(), mission.getCurrentObjectiveTime(),
                mission.getApproxEndTime(), concatEventLog(mission.getEvents()), mission.getLocation().getName());
    }

    private static String concatEventLog(List<Event> events) {
        return events.stream()
                .map(Event::getEventMessage)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n"));
    }
}
