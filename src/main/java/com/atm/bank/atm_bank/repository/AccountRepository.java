package com.atm.bank.atm_bank.repository;

import com.atm.bank.atm_bank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
