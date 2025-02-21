package com.mcgill.ecse428.textbook_exchange.repository;


import org.springframework.data.repository.CrudRepository;

import com.mcgill.ecse428.textbook_exchange.model.Account;
import com.mcgill.ecse428.textbook_exchange.model.Admin;


public interface AdminRepository extends CrudRepository<Account, String> {
    public Admin findByEmail(String email);
    
}

