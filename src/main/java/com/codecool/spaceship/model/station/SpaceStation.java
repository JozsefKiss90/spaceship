package com.codecool.spaceship.model.station;

import com.codecool.spaceship.model.resource.ShipResource;
import com.codecool.spaceship.model.resource.StationResource;
import com.codecool.spaceship.model.ship.SpaceShip;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Table(name = "spacestation")
public class SpaceStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int storageLevel;
    @OneToMany
    @JoinColumn(name ="ship_id")
    private List<SpaceShip> hangar;
    @OneToMany
    @JoinColumn(name ="station_id")
    private Set<StationResource> resources;

}
