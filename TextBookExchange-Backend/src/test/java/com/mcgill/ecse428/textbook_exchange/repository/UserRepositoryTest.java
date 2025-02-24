package com.mcgill.ecse428.textbook_exchange.repository;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse428.textbook_exchange.model.Cart;
import com.mcgill.ecse428.textbook_exchange.model.User;




@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        userRepository.deleteAll();
        cartRepository.deleteAll();
     
    }

    @Test
    public void testSaveCustomeruser() {
        // Create and save the customer
        String aEmail = "test@example.com";
        String aUsername = "AnthonySaber";
        String aPassword = "password";
        String aPhoneNumber = "+1 5142472029";
    

        // Create and save a new cart
        Cart aCart = new Cart();
        aCart = cartRepository.save(aCart);
        String cartId = aCart.getCartId();

        // Create a new user and save it
        User user = new User( aEmail,  aUsername,  aPassword,  aPhoneNumber,  aCart);
        user = userRepository.save(user);

        User userRetreived = userRepository.findByEmail(aEmail);

        //create a second user 
        String aEmail2 = "test2@ex.com";
        String aUsername2 = "AnthonySaber2";
        String aPassword2 = "password2";
        String aPhoneNumber2 = "+1 5142472029";
        Cart aCart2 = new Cart();
        aCart2 = cartRepository.save(aCart2);
        String cartId2 = aCart2.getCartId();
        User user2 = new User( aEmail2,  aUsername2,  aPassword2,  aPhoneNumber2,  aCart2);
        user2 = userRepository.save(user2);
        User userRetreived2 = userRepository.findByEmail(aEmail2);

        assertNotNull(userRetreived);
        assertEquals(aEmail, userRetreived.getEmail());
        assertEquals(aUsername, userRetreived.getUsername());
        assertEquals(aPassword, userRetreived.getPassword());
        assertEquals(aPhoneNumber, userRetreived.getPhoneNumber());
        assertTrue(userRetreived instanceof User);
        assertEquals(cartId, ((User) userRetreived).getCart().getCartId());
        assertNotNull(userRetreived2);
        assertEquals(aEmail2, userRetreived2.getEmail());
        assertEquals(aUsername2, userRetreived2.getUsername());
        assertEquals(aPassword2, userRetreived2.getPassword());
        assertEquals(aPhoneNumber2, userRetreived2.getPhoneNumber());
        assertTrue(userRetreived2 instanceof User);
        assertEquals(cartId2, ((User) userRetreived2).getCart().getCartId());
        
    }

    @Test
    public void testSaveCustomeruserUpdateCart() {
        // Create and save the customer
        String aEmail = "test@example.com";
        String aUsername = "AnthonySaber";
        String aPassword = "password";
        String aPhoneNumber = "+1 5142472029";
    

        // Create and save a new cart
        Cart aCart = new Cart();
        aCart = cartRepository.save(aCart);
        String cartId = aCart.getCartId();

        // Create a new user and save it
        User user = new User( aEmail,  aUsername,  aPassword,  aPhoneNumber,  aCart);
        


        Cart newCart = new Cart();
        newCart = cartRepository.save(newCart);
        String newCartId = newCart.getCartId();
        (user).setCart(newCart);
        user = userRepository.save(user);

        User userRetreived = userRepository.findByEmail(aEmail);

       

        assertNotNull(userRetreived);
        assertEquals(aEmail, userRetreived.getEmail());
        assertEquals(aUsername, userRetreived.getUsername());
        assertEquals(aPassword, userRetreived.getPassword());
        assertEquals(aPhoneNumber, userRetreived.getPhoneNumber());
        assertNotEquals(cartId, ((User) userRetreived).getCart().getCartId(), "Cart Id should be different");
        assertTrue(userRetreived instanceof User);
        assertEquals(newCartId, ((User) userRetreived).getCart().getCartId(), "Cart Id should be the new cart id");
        
    }
}