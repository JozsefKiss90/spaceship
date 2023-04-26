package com.codecool.spaceship.model.mission;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Data
@Builder
public class Event {

    private LocalDateTime endTime;
    private EventType eventType;
    private String eventMessage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return ((endTime != null && event.endTime != null
                    && (ChronoUnit.SECONDS.between(endTime, event.endTime) < 0.5
                        && ChronoUnit.SECONDS.between(endTime, event.endTime) > -0.5))
                    || (endTime == null && event.endTime == null))
                && eventType == event.eventType
                && Objects.equals(eventMessage, event.eventMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endTime, eventType, eventMessage);
    }
}
