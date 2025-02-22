package com.mcgill.ecse428.textbook_exchange.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.mcgill.ecse428.textbook_exchange.model.Cart;
import com.mcgill.ecse428.textbook_exchange.repository.CartItemRepository;
import com.mcgill.ecse428.textbook_exchange.repository.CartRepository;
import com.mcgill.ecse428.textbook_exchange.repository.ListingRepository;
import com.mcgill.ecse428.textbook_exchange.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.mcgill.ecse428.textbook_exchange.model.User;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ListingRepository listingRepository;
    @Autowired
    private UserRepository userRepository;
    
    public Cart getCartByUserEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Cart cart = user.getCart();

        return cart;
    }
    
    public Cart getCartByCartId(String cartId) {
        Cart cart = cartRepository.findByCartId(cartId);
        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }
        return cart;
    }
    
}
