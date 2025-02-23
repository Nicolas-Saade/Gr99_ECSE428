package com.mcgill.ecse428.textbook_exchange.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.mcgill.ecse428.textbook_exchange.exception.TextBookExchangeException;
import com.mcgill.ecse428.textbook_exchange.model.Cart;
import com.mcgill.ecse428.textbook_exchange.model.CartItem;
import com.mcgill.ecse428.textbook_exchange.model.Listing;
import com.mcgill.ecse428.textbook_exchange.repository.CartItemRepository;
import com.mcgill.ecse428.textbook_exchange.repository.CartRepository;
import com.mcgill.ecse428.textbook_exchange.repository.CourseRepository;
import com.mcgill.ecse428.textbook_exchange.repository.ListingRepository;
import com.mcgill.ecse428.textbook_exchange.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;
import com.mcgill.ecse428.textbook_exchange.model.User;
import com.mcgill.ecse428.textbook_exchange.model.Listing.BookCondition;

@Service
public class ListingService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ListingRepository listingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;




    @Transactional
    public Listing getListingByISBN(String ISBN) {
        Listing listing = listingRepository.findByISBN(ISBN);
        if (listing == null) {
            throw new TextBookExchangeException(HttpStatus.NOT_FOUND,"Listing not found");
        }

        return listing;
    }


    @Transactional
    public List<Listing> getListingsByBookName(String bookName) {
        List<Listing> listings = listingRepository.findAllByBookName(bookName);
        if (listings == null) {
            throw new TextBookExchangeException(HttpStatus.NOT_FOUND,"Listings for book name not found");
        }

        return listings;
    }



    public void deleteListingByISBN(String ISBN) {
        Listing listing = getListingByISBN(ISBN);
        listingRepository.delete(listing);
    }

    public void deleteAllListings() {
        listingRepository.deleteAll();
    }

    public void changeListingPrice(String ISBN, int newPrice) {
        Listing listing = getListingByISBN(ISBN);
        listing.setPrice(newPrice);
        listingRepository.save(listing);
    }   

    public void changeListingBookName(String ISBN, String bookName) {
        Listing listing = getListingByISBN(ISBN);
        listing.setBookName(bookName);
        listingRepository.save(listing);
    }

    
    public void changeListingCondition(String ISBN, BookCondition condition) {
        Listing listing = getListingByISBN(ISBN);
        listing.setBookcondition(condition);
        listingRepository.save(listing);
    }

    public void changeListingDatePosted(String ISBN, LocalDate datePosted) {
        Listing listing = getListingByISBN(ISBN);
        listing.setDatePosted(datePosted);
        listingRepository.save(listing);
    }

    public void updateListing(String ISBN, String bookName, BookCondition condition, float price, LocalDate datePosted) {
        Listing listing = getListingByISBN(ISBN);
        listing.setBookName(bookName);
        listing.setBookcondition(condition);
        listing.setPrice(price);
        listing.setDatePosted(datePosted);
        listingRepository.save(listing);
    }

    public void createListing(String ISBN, String bookName, BookCondition condition, float price, LocalDate datePosted) {
        Listing listing = new Listing(ISBN, bookName, condition, price, datePosted);
        listingRepository.save(listing);
    }


    @Transactional
    public void removeCourseFromListing(String ISBN) {
        Listing listing = getListingByISBN(ISBN);
        listing.removeCourse();
    }

    @Transactional
    public void addCourseToListing(String ISBN, String courseCode) {
        Listing listing = getListingByISBN(ISBN);
        listing.setCourse(courseRepository.findByCourseId(courseCode));
    }

    @Transactional
    public void addUserToListing(String ISBN, String email) {
        Listing listing = getListingByISBN(ISBN);
        listing.setUser(userRepository.findByEmail(email));
    }

}
