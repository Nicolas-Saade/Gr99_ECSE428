package com.mcgill.ecse428.textbook_exchange.repository;


import org.springframework.data.repository.CrudRepository;


import com.mcgill.ecse428.textbook_exchange.model.Review;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
    public Review findById(int reviewId);
    
}
