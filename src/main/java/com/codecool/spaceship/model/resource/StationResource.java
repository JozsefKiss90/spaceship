package com.codecool.spaceship.model.resource;

import jakarta.persistence.*;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StationResource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;
    private int quantity;
}
