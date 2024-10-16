package com.atm.bank.atm_bank.service;

import com.atm.bank.atm_bank.entity.User;
import com.atm.bank.atm_bank.exception.AuthenticationException;
import com.atm.bank.atm_bank.repository.UserRepository;
import com.atm.bank.atm_bank.security.PasswordEncoderService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoderService passwordEncoder;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUser(long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId()).orElseThrow();
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        return userRepository.save(existingUser);
    }
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    public void authenticateUser(String username, String password) {
        if (passwordEncoder == null) {
            throw new RuntimeException("Password Encoder not set");
        }
        User user = userRepository.findByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }

    }
}
