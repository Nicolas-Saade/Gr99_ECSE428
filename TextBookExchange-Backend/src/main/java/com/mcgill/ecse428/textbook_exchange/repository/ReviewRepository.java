package com.mcgill.ecse428.textbook_exchange.repository;

import com.mcgill.ecse428.textbook_exchange.model.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends CrudRepository<Review, String> {
}