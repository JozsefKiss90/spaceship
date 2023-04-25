package com.codecool.spaceship.model.mission;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Event {

    private LocalDateTime endTime;
    private LocalDateTime displayTime;
    private EventType eventType;
    private EventType displayType;
    private String eventMessage;

}
