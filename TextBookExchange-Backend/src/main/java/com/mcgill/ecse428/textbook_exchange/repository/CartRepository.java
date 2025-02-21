package com.mcgill.ecse428.textbook_exchange.repository;


import org.springframework.data.repository.CrudRepository;


import com.mcgill.ecse428.textbook_exchange.model.Cart;

public interface CartRepository extends CrudRepository<Cart, String> {
    public Cart findByCartId(String cartId);
    
}
