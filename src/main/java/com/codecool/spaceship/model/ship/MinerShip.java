package com.codecool.spaceship.model.ship;

import com.codecool.spaceship.model.resource.Resource;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "ship_id")
public class MinerShip extends SpaceShip {
    private int drillLevel;
    @OneToMany
    @JoinColumn(name ="ship_id")
    private Set<Resource> resources;
    private int storageLevel;
}
