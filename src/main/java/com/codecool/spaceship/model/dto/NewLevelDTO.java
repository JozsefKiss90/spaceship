package com.codecool.spaceship.model.dto;

import com.codecool.spaceship.model.UpgradeableType;
import com.codecool.spaceship.model.resource.ResourceType;

import java.util.Map;

public record NewLevelDTO(UpgradeableType type, int effect, Map<ResourceType, Integer> cost) {
}
