package com.codecool.spaceship.model.dto;

import java.time.LocalDateTime;

public record EventDTO(LocalDateTime time, String message) {
}
