package com.example.gerenciamento.Services;

import com.example.gerenciamento.DTOs.AuthResponse;
import com.example.gerenciamento.DTOs.UserRegistrationRequest;
import com.example.gerenciamento.Entities.Role;
import com.example.gerenciamento.Entities.User;
import com.example.gerenciamento.repositories.RoleRepository;
import com.example.gerenciamento.repositories.UserRepository;
import com.example.gerenciamento.Security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        // Arrange
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername("testuser");
        request.setPassword("123456");
        request.setEmail("test@example.com");
        request.setRole("ROLE_USER");

        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);
        when(jwtTokenProvider.generateToken(eq("testuser"), anyList())).thenReturn("fake-jwt-token");

        // Act
        AuthResponse response = userService.registerUser(request);

        // Assert
        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }
}
