package com.codecool.spaceship.model;

import com.codecool.spaceship.model.resource.ResourceType;

import java.util.Map;

public record Level<T>(int level, T effect, Map<ResourceType, Integer> cost) {
}
