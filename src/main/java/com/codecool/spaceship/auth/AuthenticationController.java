package com.codecool.spaceship.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public AuthenticationResponse register(@RequestBody RegisterRequest request) {
            return authenticationService.register(request);
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
            AuthenticationResponse authResponse = authenticationService.authenticate(request);
            Cookie jwtCookie = new Cookie("jwt", authResponse.getToken());
            jwtCookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(1));
            jwtCookie.setDomain("localhost");
            jwtCookie.setPath("/");
            response.addCookie(jwtCookie);
            return authResponse;
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setMaxAge(0);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);
    }
}
