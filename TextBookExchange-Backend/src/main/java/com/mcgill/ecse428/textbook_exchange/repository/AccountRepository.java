package com.mcgill.ecse428.textbook_exchange.repository;


import org.springframework.data.repository.CrudRepository;

import com.mcgill.ecse428.textbook_exchange.model.Account;

public interface AccountRepository extends CrudRepository<Account, String> {
    public Account findByEmail(String email);
    
}
