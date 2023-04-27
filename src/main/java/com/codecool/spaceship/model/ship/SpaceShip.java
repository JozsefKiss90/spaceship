package com.codecool.spaceship.model.ship;

import com.codecool.spaceship.model.ship.shipparts.Color;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "spaceship")
@Transactional
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
