package com.atm.bank.atm_bank.service;

import com.atm.bank.atm_bank.entity.User;
import com.atm.bank.atm_bank.exception.AuthenticationException;
import com.atm.bank.atm_bank.repository.UserRepository;
import com.atm.bank.atm_bank.security.PasswordEncoderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoderService passwordEncoderService;

    @Test
    void createUser() {
        User user = new User("username", "password");
        when(userRepository.save(user)).thenReturn(user);
        User createdUser = userService.createUser(user);
        assertEquals(user, createdUser);
        verify(passwordEncoderService, never()).encodePassword(any());
    }

    @Test
    void authenticateUserTest() {
        User user = new User("username", "password");
        when(passwordEncoderService.matches("password", user.getPassword())).thenReturn(true);
        when(userRepository.findByUsername("username")).thenReturn(user);
        userService.authenticateUser("username", "password");
        verify(userRepository).findByUsername("username");
        verify(passwordEncoderService).matches("password", user.getPassword());
    }

    @Test
    void authenticateUserNotFoundTest() {
        when(userRepository.findByUsername("username")).thenReturn(null);
        assertThrows(AuthenticationException.class, () -> userService.authenticateUser("username", "password"));
    }
    @Test
    void invalidPasswordTest() {
        User user = new User("username", "password");
        when(userRepository.findByUsername("username")).thenReturn(user);
        when(passwordEncoderService.matches("invalid_password", user.getPassword())).thenReturn(false);

        assertThrows(AuthenticationException.class, () -> userService.authenticateUser("username", "invalid_password"));
    }
}