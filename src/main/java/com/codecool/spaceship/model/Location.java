package com.codecool.spaceship.model;

import com.codecool.spaceship.model.mission.Mission;
import com.codecool.spaceship.model.resource.ResourceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int distanceFromStation;
    @Enumerated(value = EnumType.STRING)
    private ResourceType resourceType;
    @OneToOne
    private Mission currentMission;
}
