package com.atm.bank.atm_bank.service;

import com.atm.bank.atm_bank.entity.User;
import com.atm.bank.atm_bank.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

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

    public boolean authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null && user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }
}
