package com.codecool.spaceship.base_controller;

import com.codecool.spaceship.base_controller.DTO.BaseDTO;
import com.codecool.spaceship.model.Base;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/base/")
public class BaseController {

 private Base base;
 @Autowired
 public BaseController(Base base) {
   this.base = base;
 }

 @GetMapping("getBase")
 public BaseDTO getBase() {
   return new BaseDTO(getBase().name());
 }
}
