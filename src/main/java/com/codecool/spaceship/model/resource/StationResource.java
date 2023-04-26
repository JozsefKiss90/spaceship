package com.codecool.spaceship.model.resource;

import jakarta.persistence.*;
import lombok.*;


@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StationResource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    private ResourceType resourceType;
    private int quantity;
}
