package com.codecool.spaceship.model;

import com.codecool.spaceship.model.station.SpaceStation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class UserEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   @Column(unique = true)
   private String username;
   @Column(unique = true)
   private String email;
   private String password;
   @ManyToMany
   private Set<UserRole> roles;
   @OneToOne(mappedBy = "user", cascade = CascadeType.MERGE)
   private SpaceStation spaceStation;
}
