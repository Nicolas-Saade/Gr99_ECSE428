package com.mcgill.ecse428.textbook_exchange.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;


import com.mcgill.ecse428.textbook_exchange.model.Listing;

public interface ListingRepository extends CrudRepository<Listing, String> {
    public Listing findByISBN(String ISBN);
    public List<Listing> findAllByBookName(String bookName);
    
}
