package com.codecool.spaceship.model.ship;

import com.codecool.spaceship.model.ship.shipparts.Color;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "spaceship")
public abstract class SpaceShip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Color color;
    private int engineLevel;
    private int shieldLevel;
    private int shieldEnergy;

}
