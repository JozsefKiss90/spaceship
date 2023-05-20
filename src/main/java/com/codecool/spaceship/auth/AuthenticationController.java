package com.codecool.spaceship.auth;

import com.codecool.spaceship.controller.ControllerExceptionHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(authenticationService.register(request));
        } catch (Exception e) {
            return ResponseEntity.of(ControllerExceptionHandler.getProblemDetail(e)).build();
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
        try {
            AuthenticationResponse authResponse = authenticationService.authenticate(request);
//            Cookie jwtCookie = new Cookie("jwt", authResponse.getToken());
//            jwtCookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(1));
//            response.addCookie(jwtCookie);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.of(ControllerExceptionHandler.getProblemDetail(e)).build();
        }
    }
}
