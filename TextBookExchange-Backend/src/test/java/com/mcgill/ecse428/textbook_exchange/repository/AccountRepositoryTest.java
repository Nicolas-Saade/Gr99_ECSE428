package com.mcgill.ecse428.textbook_exchange.repository;

import static org.junit.jupiter.api.Assertions.*;
import com.mcgill.ecse428.textbook_exchange.model.Account;
import com.mcgill.ecse428.textbook_exchange.model.Admin;
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
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        accountRepository.deleteAll();
        cartRepository.deleteAll();
     
    }

    @Test
    public void testSaveCustomerAccount() {
        // Create and save the customer
        String aEmail = "test@example.com";
        String aUsername = "AnthonySaber";
        String aPassword = "password";
        String aPhoneNumber = "+1 5142472029";
    

        // Create and save a new cart
        Cart aCart = new Cart();
        aCart = cartRepository.save(aCart);
        String cartId = aCart.getCartId();

        // Create a new account and save it
        User account = new User( aEmail,  aUsername,  aPassword,  aPhoneNumber,  aCart);
        account = accountRepository.save(account);

        Account accountRetreived = accountRepository.findByEmail(aEmail);
        assertNotNull(accountRetreived);
        assertEquals(aEmail, accountRetreived.getEmail());
        assertEquals(aUsername, accountRetreived.getUsername());
        assertEquals(aPassword, accountRetreived.getPassword());
        assertEquals(aPhoneNumber, accountRetreived.getPhoneNumber());
        assertTrue(accountRetreived instanceof User);
        assertEquals(cartId, ((User) accountRetreived).getCart().getCartId());
        
    }

    @Test
    public void testSaveCustomerAccountUpdateCart() {
        // Create and save the customer
        String aEmail = "test@example.com";
        String aUsername = "AnthonySaber";
        String aPassword = "password";
        String aPhoneNumber = "+1 5142472029";
    

        // Create and save a new cart
        Cart aCart = new Cart();
        aCart = cartRepository.save(aCart);
        String cartId = aCart.getCartId();

        // Create a new account and save it
        User account = new User( aEmail,  aUsername,  aPassword,  aPhoneNumber,  aCart);
        


        Cart newCart = new Cart();
        newCart = cartRepository.save(newCart);
        String newCartId = newCart.getCartId();
        (account).setCart(newCart);
        account = accountRepository.save(account);

        Account accountRetreived = accountRepository.findByEmail(aEmail);

       

        assertNotNull(accountRetreived);
        assertEquals(aEmail, accountRetreived.getEmail());
        assertEquals(aUsername, accountRetreived.getUsername());
        assertEquals(aPassword, accountRetreived.getPassword());
        assertEquals(aPhoneNumber, accountRetreived.getPhoneNumber());
        assertNotEquals(cartId, ((User) accountRetreived).getCart().getCartId(), "Cart Id should be different");
        assertTrue(accountRetreived instanceof User);
        assertEquals(newCartId, ((User) accountRetreived).getCart().getCartId(), "Cart Id should be the new cart id");
        
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
        account = accountRepository.save(account);

        Account accountRetreived = accountRepository.findByEmail(aEmail);
        assertNotNull(accountRetreived);
        assertEquals(aEmail, accountRetreived.getEmail());
        assertEquals(aUsername, accountRetreived.getUsername());
        assertEquals(aPassword, accountRetreived.getPassword());
        assertEquals(aPhoneNumber, accountRetreived.getPhoneNumber());
        assertTrue(accountRetreived instanceof Admin);
        
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
        account = accountRepository.save(account);

        Account accountRetreived = accountRepository.findByEmail(aEmail);
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