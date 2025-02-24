package com.mcgill.ecse428.textbook_exchange.repository;


import org.springframework.data.repository.CrudRepository;

import com.mcgill.ecse428.textbook_exchange.model.Account;
import com.mcgill.ecse428.textbook_exchange.model.User;

public interface UserRepository extends CrudRepository<Account, String> {
    public User findByEmail(String email);

    public User findByPhoneNumber(String phoneNumber);
    
}

