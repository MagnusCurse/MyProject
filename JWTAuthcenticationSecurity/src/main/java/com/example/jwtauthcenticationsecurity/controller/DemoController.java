package com.example.jwtauthcenticationsecurity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/demo")
@RestController
public class DemoController {
    @GetMapping
    public ResponseEntity<String> sayHi() {
        return ResponseEntity.ok("Hi from secured endpoint");
    }
}
