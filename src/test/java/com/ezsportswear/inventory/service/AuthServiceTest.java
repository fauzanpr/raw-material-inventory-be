package com.ezsportswear.inventory.service;

import com.ezsportswear.inventory.dto.LoginRequest;
import com.ezsportswear.inventory.dto.LoginResponse;
import com.ezsportswear.inventory.entity.User;
import com.ezsportswear.inventory.repository.UserRepository;
import com.ezsportswear.inventory.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private LoginRequest loginRequest(String email, String password) {
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);
        return request;
    }

    private User sampleUser() {
        return User.builder()
                .id(1L)
                .name("Admin Gudang")
                .email("admin@ezsportswear.com")
                .password("hashed-password")
                .build();
    }

    @Test
    void testLogin_EmailNotFound() {
        // Path 1: user tidak ditemukan
        LoginRequest request = loginRequest("unknown@ezsportswear.com", "password");

        when(userRepository.findByEmail("unknown@ezsportswear.com"))
                .thenReturn(Optional.empty());

        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> authService.login(request)
        );

        assertEquals("Email or password is incorrect", exception.getMessage());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void testLogin_WrongPassword() {
        // Path 2: user ditemukan, password salah
        LoginRequest request = loginRequest("admin@ezsportswear.com", "wrongpassword");
        User user = sampleUser();

        when(userRepository.findByEmail("admin@ezsportswear.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrongpassword", "hashed-password"))
                .thenReturn(false);

        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> authService.login(request)
        );

        assertEquals("Email or password is incorrect", exception.getMessage());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void testLogin_Success() {
        // Path 3: user ditemukan, password benar
        LoginRequest request = loginRequest("admin@ezsportswear.com", "password");
        User user = sampleUser();

        when(userRepository.findByEmail("admin@ezsportswear.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("password", "hashed-password"))
                .thenReturn(true);

        when(jwtService.generateToken(user))
                .thenReturn("dummy-access-token");

        LoginResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("dummy-access-token", response.getAccessToken());
        verify(jwtService, times(1)).generateToken(user);
    }
}