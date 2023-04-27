package com.codecool.spaceship.model.ship;

import com.codecool.spaceship.model.resource.ShipResource;
import com.codecool.spaceship.model.ship.shipparts.Color;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class MinerShip extends SpaceShip {
    private int drillLevel;
    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name ="ship_id")
    private Set<ShipResource> resources;
    private int storageLevel;

    public MinerShip(String name, Color color) {
        super();
        super.setColor(color);
        super.setName(name);
    }
}
