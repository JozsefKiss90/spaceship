package com.codecool.spaceship.model.ship;

import com.codecool.spaceship.model.resource.ShipResource;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class MinerShip extends SpaceShip {
    private int drillLevel;
    @OneToMany
    @JoinColumn(name ="ship_id")
    private Set<ShipResource> resources;
    private int storageLevel;
}
