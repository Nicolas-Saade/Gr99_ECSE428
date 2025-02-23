package com.mcgill.ecse428.textbook_exchange.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.mcgill.ecse428.textbook_exchange.model.Admin;
import com.mcgill.ecse428.textbook_exchange.model.Account;
import com.mcgill.ecse428.textbook_exchange.model.Cart;
import com.mcgill.ecse428.textbook_exchange.model.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.boot.test.context.SpringBootTest;




@SpringBootTest
public class AdminRepositoryTest {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        adminRepository.deleteAll();
        cartRepository.deleteAll();
     
    }


    @Test
    public void testSaveAdminAccount() {
        // Create and save the customer
        String aEmail = "test@example.com";
        String aUsername = "AnthonySaber";
        String aPassword = "password";
        String aPhoneNumber = "+1 5142472029";
    

        // Create a new account and save it
        Admin account = new Admin( aEmail,  aUsername,  aPassword,  aPhoneNumber);
        account = adminRepository.save(account);

        Admin accountRetreived = adminRepository.findByEmail(aEmail);
        assertNotNull(accountRetreived);
        assertEquals(aEmail, accountRetreived.getEmail());
        assertEquals(aUsername, accountRetreived.getUsername());
        assertEquals(aPassword, accountRetreived.getPassword());
        assertEquals(aPhoneNumber, accountRetreived.getPhoneNumber());
        assertTrue(accountRetreived instanceof Admin);
        List<Account> allAdmins = new ArrayList<>();
        adminRepository.findAll().forEach(allAdmins::add);
        assertNotNull(allAdmins);
        assertEquals(1, allAdmins.size());
        
    }
    @Test
    public void testUpdateAdminAccount() {
        // Create and save the customer
        String aEmail = "test@example.com";
        String aUsername = "AnthonySaber";
        String aPassword = "password";
        String aPhoneNumber = "+1 5142472029";
        String aNewPhoneNumber = "+1 5142472028";
        String aNewPassword = "newPassword";
    

        // Create a new account and save it
        Admin account = new Admin( aEmail,  aUsername,  aPassword,  aPhoneNumber);
        account.setPhoneNumber(aNewPhoneNumber);
        account.setPassword(aNewPassword);
        account = adminRepository.save(account);

        Account accountRetreived = adminRepository.findByEmail(aEmail);
        assertNotNull(accountRetreived);
        assertEquals(aEmail, accountRetreived.getEmail());
        assertEquals(aUsername, accountRetreived.getUsername());
        assertEquals(aNewPassword, accountRetreived.getPassword());
        assertEquals(aNewPhoneNumber, accountRetreived.getPhoneNumber());
        assertNotEquals(aPhoneNumber, accountRetreived.getPhoneNumber());
        assertNotEquals(aPassword, accountRetreived.getPassword());
        assertTrue(accountRetreived instanceof Admin);
        
    }

}