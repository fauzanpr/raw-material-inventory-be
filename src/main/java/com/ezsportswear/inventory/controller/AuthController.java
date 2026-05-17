package com.ezsportswear.inventory.controller;

import com.ezsportswear.inventory.dto.LoginRequest;
import com.ezsportswear.inventory.dto.LoginResponse;
import com.ezsportswear.inventory.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}