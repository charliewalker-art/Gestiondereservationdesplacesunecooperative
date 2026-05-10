package com.example.Gestiondereservationdesplacesunecooperative.controller;

import com.example.Gestiondereservationdesplacesunecooperative.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        return authService.login(request.get("username"), request.get("password"));
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> request) {
        return authService.register(request.get("username"), request.get("password"));
    }
}