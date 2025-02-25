package com.mcgill.ecse428.textbook_exchange.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.mcgill.ecse428.textbook_exchange.exception.TextBookExchangeException;
import com.mcgill.ecse428.textbook_exchange.model.Cart;
import com.mcgill.ecse428.textbook_exchange.model.CartItem;
import com.mcgill.ecse428.textbook_exchange.model.Course;
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

    @Transactional
public Listing createListing(String bookName, String ISBN, float price, LocalDate datePosted, String username, String courseCode, BookCondition condition) {
    // Validate input parameters
    if (bookName == null || bookName.trim().isEmpty()) {
        throw new TextBookExchangeException(HttpStatus.BAD_REQUEST, "Bad book name.");
    }
    
    if (ISBN == null || ISBN.trim().isEmpty() || !isValidISBN(ISBN)) {
        throw new TextBookExchangeException(HttpStatus.BAD_REQUEST, "Bad ISBN.");
    }
    
    if (price <= 0) {
        throw new TextBookExchangeException(HttpStatus.BAD_REQUEST, "Bad price.");
    }
    
    if (datePosted == null || datePosted.isAfter(LocalDate.now())) {
        throw new TextBookExchangeException(HttpStatus.BAD_REQUEST, "Bad date.");
    }
    
    if (username == null || username.trim().isEmpty()) {
        throw new TextBookExchangeException(HttpStatus.BAD_REQUEST, "Bad username.");
    }
    
    if (courseCode == null || courseCode.trim().isEmpty()) {
        throw new TextBookExchangeException(HttpStatus.BAD_REQUEST, "Bad course code.");
    }

    // Ensure the user exists
    User user = userRepository.findByEmail(username);
    if (user == null) {
        throw new TextBookExchangeException(HttpStatus.NOT_FOUND, "User not found.");
    }

    // Ensure the course exists
    Course course = courseRepository.findByCourseId(courseCode);
    if (course == null) {
        throw new TextBookExchangeException(HttpStatus.NOT_FOUND, "Course not found.");
    }

    // Ensure the listing does not already exist
    if (listingRepository.findByISBN(ISBN) != null) {
        throw new TextBookExchangeException(HttpStatus.BAD_REQUEST, "Listing already exists.");
    }

    // Create and save the listing
    Listing listing = new Listing(bookName, ISBN, price, datePosted, user, course);
    listing.setBookcondition(condition);

    return listingRepository.save(listing);
    }

    private boolean isValidISBN(String ISBN) {
        return ISBN.matches("\\d{3}-\\d-\\d{1,5}-\\d{1,7}-\\d"); // Basic ISBN format validation
    }


}
