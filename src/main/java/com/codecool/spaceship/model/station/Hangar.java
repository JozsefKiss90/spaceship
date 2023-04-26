package com.codecool.spaceship.model.station;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hangar")
public class Hangar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
