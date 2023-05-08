package com.codecool.spaceship.model.station;

import com.codecool.spaceship.model.resource.StationResource;
import com.codecool.spaceship.model.ship.SpaceShip;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Table(name = "spacestation")
public class SpaceStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int storageLevel;
    private int hangarLevel;
    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name ="station_id")
    private Set<SpaceShip> hangar;
    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name ="station_id")
    private Set<StationResource> resources;

}
