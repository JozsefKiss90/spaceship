package com.codecool.spaceship.model;

import com.codecool.spaceship.model.station.SpaceStation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class UserEntity implements UserDetails {
   @Id
   @GeneratedValue
   private Long id;
   private String username;
   private String email;
   private String password;
   @OneToOne
   private SpaceStation spaceStation;

   @Enumerated(EnumType.STRING)
   private Role role;

   /*@OneToMany(mappedBy = "user")
   private List<Token> tokens;*/

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return role.getAuthorities();
   }

   @Override
   public String getPassword() {
      return password;
   }

   @Override
   public String getUsername() {
      return email;
   }

   @Override
   public boolean isAccountNonExpired() {
      return true;
   }

   @Override
   public boolean isAccountNonLocked() {
      return true;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }

   @Override
   public boolean isEnabled() {
      return true;
   }
}
