package com.greenart.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api")
@RestController
public class TestController {

    @GetMapping("/user")
    public ResponseEntity<Object> getUser() {
        return ResponseEntity.ok("Hello World");
    }
}
