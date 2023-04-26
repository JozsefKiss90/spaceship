package com.codecool.spaceship.model.dto;

import com.codecool.spaceship.model.Resource;

import java.util.Map;

public record CostDTO(Map<Resource,Integer> cost) {
}
