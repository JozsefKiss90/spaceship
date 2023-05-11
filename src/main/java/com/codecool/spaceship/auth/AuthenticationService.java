package com.codecool.spaceship.auth;

import com.codecool.spaceship.config.JwtService;
import com.codecool.spaceship.model.Role;
import com.codecool.spaceship.model.UserEntity;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.MinerShipManager;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.station.SpaceStation;
import com.codecool.spaceship.model.station.SpaceStationManager;
import com.codecool.spaceship.repository.SpaceShipRepository;
import com.codecool.spaceship.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final SpaceShipRepository spaceShipRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request){
        //TODO if username or email are taken, throw error
        UserEntity user = createUser(request);
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword())
        );
        UserEntity user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private UserEntity createUser(RegisterRequest request) {
        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        SpaceStation spaceStation = SpaceStationManager.createNewSpaceStation("%s's Station".formatted(request.getUsername()));
        MinerShip minerShip = MinerShipManager.createNewMinerShip("Miner Ship #1", Color.SAPPHIRE);

        user.setSpaceStation(spaceStation);
        spaceStation.setUser(user);
        spaceStation.setHangar(Set.of(minerShip));
        minerShip.setStation(spaceStation);
        minerShip.setUser(user);
        return userRepository.save(user);
    }
}