package com.atm.bank.atm_bank.service;

import com.atm.bank.atm_bank.entity.User;
import com.atm.bank.atm_bank.repository.UserRepository;
import com.atm.bank.atm_bank.security.PasswordEncoderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        verify(passwordEncoderService, times(1)).encodePassword(user.getPassword());
    }
}