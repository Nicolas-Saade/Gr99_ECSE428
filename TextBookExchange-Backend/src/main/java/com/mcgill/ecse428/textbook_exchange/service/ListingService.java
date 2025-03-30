package com.mcgill.ecse428.textbook_exchange.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mcgill.ecse428.textbook_exchange.exception.TextBookExchangeException;
import com.mcgill.ecse428.textbook_exchange.model.Course;
import com.mcgill.ecse428.textbook_exchange.model.Institution;
import com.mcgill.ecse428.textbook_exchange.model.Listing;
import com.mcgill.ecse428.textbook_exchange.model.Listing.BookCondition;
import com.mcgill.ecse428.textbook_exchange.model.Listing.ListingStatus;
import com.mcgill.ecse428.textbook_exchange.model.User;
import com.mcgill.ecse428.textbook_exchange.repository.CartItemRepository;
import com.mcgill.ecse428.textbook_exchange.repository.CartRepository;
import com.mcgill.ecse428.textbook_exchange.repository.CourseRepository;
import com.mcgill.ecse428.textbook_exchange.repository.InstitutionRepository;
import com.mcgill.ecse428.textbook_exchange.repository.ListingRepository;
import com.mcgill.ecse428.textbook_exchange.repository.UserRepository;

import jakarta.transaction.Transactional;

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
    @Autowired
    private InstitutionRepository institutionRepository;

    @Transactional
    public Listing getListingByISBN(String ISBN) {

        Listing listing = listingRepository.findByISBN(ISBN);
        if (listing == null) {
            throw new TextBookExchangeException(HttpStatus.NOT_FOUND,"Listing not found" );
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

    public void updateListingStatus(String username,String ISBN, ListingStatus status) {
    
        Listing listing = getListingByISBN(ISBN);
        if (listing == null) {
            throw new TextBookExchangeException(HttpStatus.NOT_FOUND, "Listing not found");
        }
        if (!username.equals(listing.getUser().getUsername())) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST, "You are not authorized to mark this listing as sold");
        }
        if (listing.getListingStatus() == status && status == ListingStatus.Unavailable) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST, "This listing is already sold");
        }

        listing.setListingStatus(status);
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
    public void changeListingStatus(String ISBN, String status) {
        Listing listing = getListingByISBN(ISBN);
        listing.setListingStatus(status);
    }
    @Transactional
    public void changeListingBookCondition(String ISBN, String status) {
        Listing listing = getListingByISBN(ISBN);
        listing.setBookcondition(status);
    }

    @Transactional
    public Listing createListing(String bookName, String ISBN, float price, LocalDate datePosted, String username, String courseCode, BookCondition condition) {
        if (bookName == null || bookName.trim().isEmpty()) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST, "Bad book name.");
        }
        if (ISBN == null || ISBN.trim().isEmpty() || !isValidISBN(ISBN)) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST, "Invalid ISBN Format");
        }
        if (price <= 0) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST, "Invalid Price");
        }
        if (datePosted == null || datePosted.isAfter(LocalDate.now())) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST, "Invalid Date Format");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST, "Bad username.");
        }
        if (courseCode == null || courseCode.trim().isEmpty()) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST, "Invalid Course Code");
        }

        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new TextBookExchangeException(HttpStatus.NOT_FOUND, "User not found.");
        }

        Course course = courseRepository.findByCourseId(courseCode);
        if (course == null) {
            throw new TextBookExchangeException(HttpStatus.NOT_FOUND, "Course not found.");
        }

        if (listingRepository.findByISBN(ISBN) != null) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST, "Listing already exists.");
        }

        Listing listing = new Listing(bookName, ISBN, price, datePosted, user, course);
        listing.setBookcondition(condition);

        return listingRepository.save(listing);
    }

    private boolean isValidISBN(String ISBN) {
        return ISBN.matches("\\d{3}-\\d-\\d{1,5}-\\d{1,7}-\\d");
    }

    @Transactional
    public List<Listing> filterListingsByPriceRange(float minPrice, float maxPrice) {
        if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            throw new IllegalArgumentException("Invalid price range");
        }
        
        List<Listing> allListings = getAllListings();
        return allListings.stream()
                .filter(listing -> listing.getPrice() >= minPrice && listing.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public List<Listing> filterListingsByCondition(BookCondition condition) {
        List<Listing> allListings = getAllListings();
        return allListings.stream()
                .filter(listing -> listing.getBookcondition() == condition)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public List<Listing> filterListingsByStatus(ListingStatus status) {
        List<Listing> allListings = getAllListings();
        return allListings.stream()
                .filter(listing -> listing.getListingStatus() == status)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public List<Listing> filterListingsByCourse(String courseId) {
        Course course = courseRepository.findByCourseId(courseId);
        if (course == null) {
            return new ArrayList<>();
        }
        
        List<Listing> allListings = getAllListings();
        return allListings.stream()
                .filter(listing -> listing.getCourse() != null && 
                        listing.getCourse().getCourseId().equals(courseId))
                .collect(Collectors.toList());
    }
    
    @Transactional
    public List<Listing> filterListingsByInstitution(String institutionName) {
        Institution institution = institutionRepository.findByInstitutionName(institutionName);
        if (institution == null) {
            return new ArrayList<>();
        }
        
        List<Listing> allListings = getAllListings();
        for (Listing listing : allListings) {
            System.out.println("Institution: for listing " + listing.getBookName() + " is " + listing.getCourse().getFaculty().getInstitution().getInstitutionName());
        }
        return allListings.stream()
                .filter(listing -> {
                    if (listing.getCourse() == null) return false;
                    if (listing.getCourse().getFaculty() == null) return false;
                    if (listing.getCourse().getFaculty().getInstitution() == null) return false;
                    return listing.getCourse().getFaculty().getInstitution()
                            .getInstitutionName().equals(institutionName);
                })
                .collect(Collectors.toList());
    }
    
    @Transactional
    public List<Listing> filterListingsByConditionAndMaxPrice(BookCondition condition, float maxPrice) {
        if (maxPrice < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        
        List<Listing> allListings = getAllListings();
        return allListings.stream()
                .filter(listing -> listing.getBookcondition() == condition && 
                                  listing.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }
    
    @Transactional
    @SuppressWarnings("unchecked")
    public List<Listing> filterListingsWithMultipleCriteria(Map<String, Object> criteria) {
        List<Listing> allListings = getAllListings();
        
        List<Listing> filteredListings = new ArrayList<>(allListings);
        
        // Apply price range filter if present
        if (criteria.containsKey("minPrice") && criteria.containsKey("maxPrice")) {
            float minPrice = (float) criteria.get("minPrice");
            float maxPrice = (float) criteria.get("maxPrice");
            
            filteredListings = filteredListings.stream()
                    .filter(listing -> listing.getPrice() >= minPrice && 
                                      listing.getPrice() <= maxPrice)
                    .collect(Collectors.toList());
        }
        
        // Apply condition filter if present
        if (criteria.containsKey("condition")) {
            BookCondition condition = (BookCondition) criteria.get("condition");
            
            filteredListings = filteredListings.stream()
                    .filter(listing -> listing.getBookcondition() == condition)
                    .collect(Collectors.toList());
        }
        
        // Apply status filter if present
        if (criteria.containsKey("status")) {
            ListingStatus status = (ListingStatus) criteria.get("status");
            
            filteredListings = filteredListings.stream()
                    .filter(listing -> listing.getListingStatus() == status)
                    .collect(Collectors.toList());
        }
        
        return filteredListings;
    }
    
    // Helper method to get all listings
    private List<Listing> getAllListings() {
        Iterable<Listing> listingsIterable = listingRepository.findAll();
        return StreamSupport.stream(listingsIterable.spliterator(), false)
                .collect(Collectors.toList());
    }
}
