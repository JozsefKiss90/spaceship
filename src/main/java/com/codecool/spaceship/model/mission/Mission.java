package com.codecool.spaceship.model.mission;

import com.codecool.spaceship.model.Location;
import com.codecool.spaceship.model.UserEntity;
import com.codecool.spaceship.model.ship.SpaceShip;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime currentObjectiveTime;
    private LocalDateTime approxEndTime;
    @Enumerated(value = EnumType.STRING)
    private MissionStatus currentStatus;
    @Enumerated(value = EnumType.STRING)
    private MissionType missionType;
    private long travelDurationInSecs;
    private long activityDurationInSecs;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "ship_id")
    private SpaceShip ship;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "mission_id")
    private List<Event> events;
}
