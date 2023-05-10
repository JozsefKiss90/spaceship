package com.codecool.spaceship.controller;

import com.codecool.spaceship.model.exception.ShipNotFoundException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

public class ControllerExceptionHandler {

    public static ProblemDetail getProblemDetail(Exception e) {
        if (e instanceof SecurityException) {
            return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), e.getMessage());
        } else if (e instanceof ShipNotFoundException){
            return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), e.getMessage());
        } else {
            return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage());
        }
    }
}
