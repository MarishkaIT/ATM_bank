package com.atm.bank.atm_bank.repository;

import com.atm.bank.atm_bank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
