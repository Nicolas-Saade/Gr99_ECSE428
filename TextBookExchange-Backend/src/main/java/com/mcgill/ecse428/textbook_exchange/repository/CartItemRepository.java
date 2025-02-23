package com.mcgill.ecse428.textbook_exchange.repository;


import org.springframework.data.repository.CrudRepository;



import com.mcgill.ecse428.textbook_exchange.model.CartItem;

public interface CartItemRepository extends CrudRepository<CartItem, String> {
    public CartItem findByCartItemId(String cartItemId);
    
}
