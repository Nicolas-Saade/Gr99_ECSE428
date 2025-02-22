package com.mcgill.ecse428.textbook_exchange.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.mcgill.ecse428.textbook_exchange.exception.TextBookExchangeException;
import com.mcgill.ecse428.textbook_exchange.model.Cart;
import com.mcgill.ecse428.textbook_exchange.model.CartItem;
import com.mcgill.ecse428.textbook_exchange.model.Listing;
import com.mcgill.ecse428.textbook_exchange.repository.CartItemRepository;
import com.mcgill.ecse428.textbook_exchange.repository.CartRepository;
import com.mcgill.ecse428.textbook_exchange.repository.ListingRepository;
import com.mcgill.ecse428.textbook_exchange.repository.UserRepository;

import jakarta.transaction.Transactional;

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

    @Transactional
    public Cart getCartByUserEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
     throw new TextBookExchangeException(HttpStatus.NOT_FOUND,"USER not found");
        }
        Cart cart = user.getCart();

        return cart;
    }

    @Transactional
    public Cart getCartByCartId(String cartId) {
        Cart cart = cartRepository.findByCartId(cartId);
        if (cart == null) {
            throw new TextBookExchangeException(HttpStatus.NOT_FOUND,"Cart not found");
        }
        return cart;
    }

    @Transactional
    public Cart addListingToCart(String cartId, String ISBN) {
        Cart cart = getCartByCartId(cartId);

        Listing listing = listingRepository.findByISBN(ISBN);
        if (listing == null) {
            throw new TextBookExchangeException(HttpStatus.NOT_FOUND,"Listing not found");
        }
        for (CartItem item : cart.getCartItems()) {
            if (item.getListing().getISBN().equals(ISBN)) {
                throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Listing already exists in cart");
            }
        }
        CartItem cartItem = new CartItem(listing,cart);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
        return cart;
    }

    @Transactional
    public Cart removeListingFromCart(String cartId, String ISBN) {
        Cart cart = getCartByCartId(cartId);
        Listing listing = listingRepository.findByISBN(ISBN);
        if (listing == null) {
            throw new TextBookExchangeException(HttpStatus.NOT_FOUND,"Listing not found");
        }
        CartItem cartItem = null;
        for (CartItem item : cart.getCartItems()) {
            if (item.getListing().getISBN().equals(ISBN)) {
                cartItem = item;
                break;
            }
        }
        if (cartItem == null) {
            throw new TextBookExchangeException(HttpStatus.NOT_FOUND,"Listing not found in cart");
        }
        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
        cartRepository.save(cart);
        return cart;
    }

    @Transactional
    public void clearCart(String cartId) {
        Cart cart = getCartByCartId(cartId);
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    @Transactional
    public List<CartItem> getAllCartItems(String cartId) {
        Cart cart = getCartByCartId(cartId);
        return cart.getCartItems();
    }


    
}
