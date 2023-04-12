package com.codecool.spaceship.model;

import java.util.Map;

public record Level<T>(int level, T effect, Map<Resource, Integer> cost) {
}
