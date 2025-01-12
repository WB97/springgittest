package com.greenart.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class UserController {

    @GetMapping("/user")
    public ResponseEntity<Object> getUser() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("user");
    }
}
