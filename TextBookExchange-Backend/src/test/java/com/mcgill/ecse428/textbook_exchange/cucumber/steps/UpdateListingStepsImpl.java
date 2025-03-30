package com.mcgill.ecse428.textbook_exchange.cucumber.steps;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse428.textbook_exchange.model.Cart;
import com.mcgill.ecse428.textbook_exchange.model.Course;
import com.mcgill.ecse428.textbook_exchange.model.Faculty;
import com.mcgill.ecse428.textbook_exchange.model.Institution;
import com.mcgill.ecse428.textbook_exchange.model.Listing;
import com.mcgill.ecse428.textbook_exchange.model.User;
import com.mcgill.ecse428.textbook_exchange.model.Listing.BookCondition;
import com.mcgill.ecse428.textbook_exchange.model.Listing.ListingStatus;
import com.mcgill.ecse428.textbook_exchange.repository.CartRepository;
import com.mcgill.ecse428.textbook_exchange.repository.CourseRepository;
import com.mcgill.ecse428.textbook_exchange.repository.FacultyRepository;
import com.mcgill.ecse428.textbook_exchange.repository.InstitutionRepository;
import com.mcgill.ecse428.textbook_exchange.repository.ListingRepository;
import com.mcgill.ecse428.textbook_exchange.repository.UserRepository;
import com.mcgill.ecse428.textbook_exchange.service.ListingService;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@SpringBootTest
public class UpdateListingStepsImpl {

    @Autowired
    private ListingService listingService;
    
    @Autowired
    private ListingRepository listingRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private FacultyRepository facultyRepository;
    
    @Autowired
    private InstitutionRepository institutionRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    private String errorMessage;
    private Listing originalListing;
    private Listing updatedListing;
    private User currentUser;
    
    @Before
    public void setup() {
        errorMessage = null;
        updatedListing = null;
        originalListing = null;
        currentUser = null;
    }
    
    @After
    public void tearDown() {
        listingRepository.deleteAll();
        courseRepository.deleteAll();
        facultyRepository.deleteAll();
        institutionRepository.deleteAll();
        userRepository.deleteAll();
        cartRepository.deleteAll();
    }

    // Using specific patterns to avoid conflicts
    @Given("For updating listings: a user with email {string} and username {string} is logged into the system")
    public void user_is_logged_into_system_for_update(String email, String username) {
        Cart cart = new Cart();
        cart = cartRepository.save(cart);
        
        currentUser = new User(email, username, "Password1", "5141234567", cart);
        userRepository.save(currentUser);
    }
    
    @Given("For updating listings: the following listings exist:")
    public void listings_exist_for_update_test(DataTable dataTable) {
        // Create institution
        Institution institution = new Institution("McGill University");
        institutionRepository.save(institution);
        
        // Create faculties
        Faculty compFaculty = new Faculty("COMP", institution);
        Faculty ecseFaculty = new Faculty("ECSE", institution);
        Faculty mathFaculty = new Faculty("MATH", institution);
        facultyRepository.save(compFaculty);
        facultyRepository.save(ecseFaculty);
        facultyRepository.save(mathFaculty);
        
        // Create courses
        Course ecse428 = new Course("ECSE 428", ecseFaculty);
        Course ecse429 = new Course("ECSE 429", ecseFaculty);
        Course math202 = new Course("MATH 202", mathFaculty);
        courseRepository.save(ecse428);
        courseRepository.save(ecse429);
        courseRepository.save(math202);
        
        List<Map<String, String>> rows = dataTable.asMaps();
        
        for (Map<String, String> row : rows) {
            String bookName = row.get("bookName");
            String isbn = row.get("ISBN");
            BookCondition condition = BookCondition.valueOf(row.get("bookCondition"));
            ListingStatus status = ListingStatus.valueOf(row.get("listingStatus"));
            float price = Float.parseFloat(row.get("price"));
            LocalDate datePosted = LocalDate.parse(row.get("datePosted"));
            String courseId = row.get("course");
            
            Course course = courseRepository.findByCourseId(courseId);
            
            Listing listing = new Listing(bookName, isbn, price, datePosted, currentUser, course);
            listing.setBookcondition(condition);
            listing.setListingStatus(status);
            listingRepository.save(listing);
        }
    }
    
    @Given("For updating listings: another user {string} owns a listing with ISBN {string}")
    public void another_user_owns_a_listing(String email, String isbn) {
        Cart cart = new Cart();
        cart = cartRepository.save(cart);
        
        User otherUser = new User(email, "otheruser", "Password1", "5149876543", cart);
        userRepository.save(otherUser);
        
        Course course = courseRepository.findByCourseId("ECSE 428");
        Listing listing = new Listing("Other User Book", isbn, 20.0f, LocalDate.now(), otherUser, course);
        listing.setBookcondition(BookCondition.New);
        listing.setListingStatus(ListingStatus.Available);
        listingRepository.save(listing);
    }
    
    @When("For updating listings: the user {string} updates the price of the listing with ISBN {string} to {string}")
    public void user_updates_price(String username, String isbn, String newPrice) {
        try {
            User user = userRepository.findByUsername(username);
            Listing listing = listingRepository.findByISBN(isbn);
            
            if (listing == null) {
                errorMessage = "Listing not found";
                return;
            }
            
            originalListing = copyListing(listing);
            
            if (!listing.getUser().getUsername().equals(username)) {
                errorMessage = "You are not authorized to update this listing";
                return;
            }
            
            float price = Float.parseFloat(newPrice);
            if (price <= 0) {
                errorMessage = "Invalid Price";
                return;
            }
            
            listing.setPrice(price);
            updatedListing = listingRepository.save(listing);
            
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }
    
    @When("For updating listings: the user {string} updates the book condition of the listing with ISBN {string} to {string}")
    public void user_updates_book_condition(String username, String isbn, String newCondition) {
        try {
            User user = userRepository.findByUsername(username);
            Listing listing = listingRepository.findByISBN(isbn);
            
            if (listing == null) {
                errorMessage = "Listing not found";
                return;
            }
            
            originalListing = copyListing(listing);
            
            if (!listing.getUser().getUsername().equals(username)) {
                errorMessage = "You are not authorized to update this listing";
                return;
            }
            
            try {
                BookCondition condition = BookCondition.valueOf(newCondition);
                listing.setBookcondition(condition);
            } catch (IllegalArgumentException e) {
                errorMessage = "Invalid Book Condition";
                return;
            }
            
            updatedListing = listingRepository.save(listing);
            
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }
    
    @When("For updating listings: the user {string} updates the listing status of the listing with ISBN {string} to {string}")
    public void user_updates_listing_status(String username, String isbn, String newStatus) {
        try {
            User user = userRepository.findByUsername(username);
            Listing listing = listingRepository.findByISBN(isbn);
            
            if (listing == null) {
                errorMessage = "Listing not found";
                return;
            }
            
            originalListing = copyListing(listing);
            
            if (!listing.getUser().getUsername().equals(username)) {
                errorMessage = "You are not authorized to update this listing";
                return;
            }
            
            try {
                ListingStatus status = ListingStatus.valueOf(newStatus);
                listing.setListingStatus(status);
            } catch (IllegalArgumentException e) {
                errorMessage = "Invalid Listing Status";
                return;
            }
            
            updatedListing = listingRepository.save(listing);
            
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }
    
    @When("For updating listings: the user {string} updates the course for the listing with ISBN {string} to {string}")
    public void user_updates_course(String username, String isbn, String newCourseId) {
        try {
            User user = userRepository.findByUsername(username);
            Listing listing = listingRepository.findByISBN(isbn);
            
            if (listing == null) {
                errorMessage = "Listing not found";
                return;
            }
            
            originalListing = copyListing(listing);
            
            if (!listing.getUser().getUsername().equals(username)) {
                errorMessage = "You are not authorized to update this listing";
                return;
            }
            
            Course course = courseRepository.findByCourseId(newCourseId);
            if (course == null) {
                errorMessage = "Invalid Course Code";
                return;
            }
            
            listing.setCourse(course);
            updatedListing = listingRepository.save(listing);
            
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }
    
    @When("For updating listings: the user {string} updates the date posted for the listing with ISBN {string} to {string}")
    public void user_updates_date_posted(String username, String isbn, String newDate) {
        try {
            User user = userRepository.findByUsername(username);
            Listing listing = listingRepository.findByISBN(isbn);
            
            if (listing == null) {
                errorMessage = "Listing not found";
                return;
            }
            
            originalListing = copyListing(listing);
            
            if (!listing.getUser().getUsername().equals(username)) {
                errorMessage = "You are not authorized to update this listing";
                return;
            }
            
            try {
                LocalDate datePosted = LocalDate.parse(newDate);
                listing.setDatePosted(datePosted);
            } catch (DateTimeParseException e) {
                errorMessage = "Invalid Date Format";
                return;
            }
            
            updatedListing = listingRepository.save(listing);
            
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }
    
    @When("For updating listings: the user {string} attempts to update the listing with ISBN {string}")
    public void user_attempts_to_update_nonexistent_listing(String username, String isbn) {
        try {
            User user = userRepository.findByUsername(username);
            Listing listing = listingRepository.findByISBN(isbn);
            
            if (listing == null) {
                errorMessage = "Listing not found";
                return;
            }
            
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }
    
    @When("For updating listings: the user {string} attempts to update the price of the listing with ISBN {string} to {string}")
    public void user_attempts_to_update_other_listing(String username, String isbn, String newPrice) {
        try {
            User user = userRepository.findByUsername(username);
            Listing listing = listingRepository.findByISBN(isbn);
            
            if (listing == null) {
                errorMessage = "Listing not found";
                return;
            }
            
            originalListing = copyListing(listing);
            
            if (!listing.getUser().getUsername().equals(username)) {
                errorMessage = "You are not authorized to update this listing";
                return;
            }
            
            float price = Float.parseFloat(newPrice);
            if (price <= 0) {
                errorMessage = "Invalid Price";
                return;
            }
            
            listing.setPrice(price);
            updatedListing = listingRepository.save(listing);
            
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }
    
    @Then("For updating listings: the listing should be updated with:")
    public void the_listing_should_be_updated_with(DataTable dataTable) {
        assertNull(errorMessage, "Unexpected error: " + errorMessage);
        assertNotNull(updatedListing, "Listing was not updated");
        
        Map<String, String> expected = dataTable.asMaps().get(0);
        
        if (expected.containsKey("price")) {
            float expectedPrice = Float.parseFloat(expected.get("price"));
            assertEquals(expectedPrice, updatedListing.getPrice(), 0.001, "Price was not updated correctly");
        }
        
        if (expected.containsKey("bookCondition")) {
            BookCondition expectedCondition = BookCondition.valueOf(expected.get("bookCondition"));
            assertEquals(expectedCondition, updatedListing.getBookcondition(), "Book condition was not updated correctly");
        }
        
        if (expected.containsKey("listingStatus")) {
            ListingStatus expectedStatus = ListingStatus.valueOf(expected.get("listingStatus"));
            assertEquals(expectedStatus, updatedListing.getListingStatus(), "Listing status was not updated correctly");
        }
        
        if (expected.containsKey("course")) {
            String expectedCourseId = expected.get("course");
            assertEquals(expectedCourseId, updatedListing.getCourse().getCourseId(), "Course was not updated correctly");
        }
    }
    
    @Then("For updating listings: the system should display an error message: {string}")
    public void system_displays_error_message_for_update(String expectedMessage) {
        assertNotNull(errorMessage, "Expected an error but none was thrown");
        assertEquals(expectedMessage, errorMessage);
    }
    
    @Then("For updating listings: the listing should remain unchanged")
    public void listing_should_remain_unchanged() {
        if (originalListing != null) {
            Listing currentListing = listingRepository.findByISBN(originalListing.getISBN());
            assertNotNull(currentListing, "Listing no longer exists");
            
            assertEquals(originalListing.getPrice(), currentListing.getPrice(), 0.001, "Price was changed");
            assertEquals(originalListing.getBookcondition(), currentListing.getBookcondition(), "Book condition was changed");
            assertEquals(originalListing.getListingStatus(), currentListing.getListingStatus(), "Listing status was changed");
            assertEquals(originalListing.getCourse().getCourseId(), currentListing.getCourse().getCourseId(), "Course was changed");
        }
    }
    
    // Helper method to create a copy of a listing for comparison
    private Listing copyListing(Listing original) {
        Listing copy = new Listing(
            original.getBookName(),
            original.getISBN(),
            original.getPrice(),
            original.getDatePosted(),
            original.getUser(),
            original.getCourse()
        );
        copy.setBookcondition(original.getBookcondition());
        copy.setListingStatus(original.getListingStatus());
        return copy;
    }
} 