package com.ezsportswear.inventory.service;

import com.ezsportswear.inventory.dto.LoginRequest;
import com.ezsportswear.inventory.dto.LoginResponse;
import com.ezsportswear.inventory.entity.User;
import com.ezsportswear.inventory.repository.UserRepository;
import com.ezsportswear.inventory.security.JwtService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Email or password is incorrect"));

        boolean isPasswordMatch = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!isPasswordMatch) {
            throw new BadCredentialsException("Email or password is incorrect");
        }

        String token = jwtService.generateToken(user);

        return new LoginResponse(token);
    }
}