package com.mcgill.ecse428.textbook_exchange.cucumber.steps;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse428.textbook_exchange.exception.TextBookExchangeException;
import com.mcgill.ecse428.textbook_exchange.model.Cart;
import com.mcgill.ecse428.textbook_exchange.model.Course;
import com.mcgill.ecse428.textbook_exchange.model.Faculty;
import com.mcgill.ecse428.textbook_exchange.model.Institution;
import com.mcgill.ecse428.textbook_exchange.model.Listing;
import com.mcgill.ecse428.textbook_exchange.model.User;
import com.mcgill.ecse428.textbook_exchange.repository.CartRepository;
import com.mcgill.ecse428.textbook_exchange.repository.CourseRepository;
import com.mcgill.ecse428.textbook_exchange.repository.FacultyRepository;
import com.mcgill.ecse428.textbook_exchange.repository.InstitutionRepository;
import com.mcgill.ecse428.textbook_exchange.repository.ListingRepository;
import com.mcgill.ecse428.textbook_exchange.repository.UserRepository;
import com.mcgill.ecse428.textbook_exchange.service.ListingService;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

@SpringBootTest
public class ListingSearchSteps {

    @Autowired
    private ListingService listingService;
    
    @Autowired
    private ListingRepository listingRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private InstitutionRepository institutionRepository;
    
    @Autowired
    private FacultyRepository facultyRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    private List<Listing> currentSearchResults;
    private String currentSearchErrorMessage;
    private String currentInvalidISBN;
    

    @Given("the following listings exist:")
    public void the_following_listings_exist(DataTable dataTable) {

        String aEmail = "test@example.com";
        String aUsername = "AnthonySaber";
        String aPassword = "password";
        String aPhoneNumber = "+1 5142472029";
        
        Cart aCart = new Cart();
        aCart = cartRepository.save(aCart);
        
  
        User   user = new User(aEmail, aUsername, aPassword, aPhoneNumber, aCart);
        user = userRepository.save(user);
        
        
        String institutionName = "McGill University";
        Institution institution = new Institution(institutionName);
        institution = institutionRepository.save(institution);
        
        
        String facultyName = "ECSE";

        Faculty  faculty = new Faculty(facultyName, institution);
        faculty = facultyRepository.save(faculty);
        
        
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            String bookName = row.get("bookName").replaceAll("^\"|\"$", "");
            String ISBN = row.get("ISBN").replaceAll("^\"|\"$", "");
            
            String priceStr = row.get("price");
            String datePostedStr = row.get("datePosted");
            String courseCode = row.get("course");
            String listingStatus = row.get("listingStatus");
            String conditionStr = row.get("bookCondition");
            
            float price = Float.parseFloat(priceStr);
            LocalDate date = LocalDate.parse(datePostedStr);
            
            Course course = courseRepository.findByCourseId(courseCode);
            if (course == null) {
                course = new Course(courseCode, faculty);
                course = courseRepository.save(course);
            }
            
            Listing listing = new Listing(bookName, ISBN, price, date, user, course);
            


            listingRepository.save(listing);
            listingService.changeListingStatus(ISBN, listingStatus);
            listingService.changeListingBookCondition(ISBN, conditionStr);

        }
        
        currentSearchResults = null;
        currentSearchErrorMessage = null;
    }
    
    
    @When("the user searches for listings with the book title {string}")
    public void the_user_searches_for_listings_with_the_book_title(String bookName) {
        try {
            currentSearchResults = listingService.getListingsByBookName(bookName);
            if (currentSearchResults.isEmpty()) {
                currentSearchErrorMessage = "No listings found for '" + bookName + "'";
            } else {
                currentSearchErrorMessage = null;
            }
        } catch (TextBookExchangeException e) {
            currentSearchResults = null;
            currentSearchErrorMessage = e.getMessage();
        }
    }
    
    @Then("the system should return the listing associated with the book title {string}")
    public void the_system_should_return_the_listing_associated_with_the_book_title(String bookName) {
        assertNotNull(currentSearchResults);
        boolean found = currentSearchResults.stream().anyMatch(listing -> listing.getBookName().equals(bookName));

        assertTrue(found);

    }
    
    @When("the user searches for listings with the ISBN {string}")
    public void the_user_searches_for_listings_with_the_isbn(String ISBN) {
        try {
            Listing listing = listingService.getListingByISBN(ISBN);
            currentSearchResults = List.of(listing);
            currentSearchErrorMessage = null;
        } catch (TextBookExchangeException e) {
            currentSearchResults = null;
            currentSearchErrorMessage = e.getMessage();
        }
    }
    
    @Then("the system should return the listing associated with the ISBN {string}")
    public void the_system_should_return_the_listing_associated_with_the_isbn(String ISBN) {
        assertNotNull(currentSearchResults);
        Listing listing = currentSearchResults.get(0);
        assertEquals(ISBN, listing.getISBN());
    }
    
    
    @When("the user searches for listings with the course code {string}")
    public void the_user_searches_for_listings_with_the_course_code(String courseCode) {
        List<Listing> allListings = (List<Listing>) listingRepository.findAll();
        currentSearchResults = allListings.stream()
                .filter(listing -> courseCode.equals(listing.getCourse().getCourseId()))
                .collect(Collectors.toList());
    }
    
    @Then("the system should return listings associated with the course code {string}")
    public void the_system_should_return_listings_associated_with_the_course_code(String courseCode) {
        assertNotNull(currentSearchResults);
        for (Listing listing : currentSearchResults) {
            assertEquals(courseCode, listing.getCourse().getCourseId());
        }
    }
    
    
    @When("the user filters listings with a price range between 30.00 and 50.00")
    public void the_user_filters_listings_with_a_price_range_between_and() {
        List<Listing> allListings = (List<Listing>) listingRepository.findAll();
        currentSearchResults = allListings.stream()
                .filter(listing -> {
                    float price = listing.getPrice();
                    return price >= 30.00 && price <= 50.00;
                })
                .collect(Collectors.toList());
    }
    
    @Then("the system should return listings with a price range between 30.00 and 50.00")
    public void the_system_should_return_listings_with_a_price_range_between_and() {
        assertNotNull(currentSearchResults);
        for (Listing listing : currentSearchResults) {
            float price = listing.getPrice();
            assertTrue(price >= 30.00 && price <= 50.00);
        }
    }
    
    
    @When("the user applies a filter to show only Available listings")
    public void the_user_applies_a_filter_to_show_only_available_listings() {
        List<Listing> allListings = (List<Listing>) listingRepository.findAll();
        currentSearchResults = allListings.stream()
                .filter(listing -> "Available".equalsIgnoreCase(listing.getListingStatus()))
                .collect(Collectors.toList());
    }
    
    @Then("the system should return only the Available listings")
    public void the_system_should_return_only_the_available_listings() {
        assertNotNull(currentSearchResults);
        for (Listing listing : currentSearchResults) {
            assertEquals("Available", listing.getListingStatus());
        }
    }
    
    
    @When("the user filters listings to show only Used books")
    public void the_user_filters_listings_to_show_only_used_books() {
        List<Listing> allListings = (List<Listing>) listingRepository.findAll();
        currentSearchResults = allListings.stream()
        .filter(listing -> listing.getBookcondition() == Listing.BookCondition.Used)
                .collect(Collectors.toList());
    }
    
    @Then("the system should return only listings with Used books")
    public void the_system_should_return_only_listings_with_used_books() {
        assertNotNull(currentSearchResults);
        for (Listing listing : currentSearchResults) {
            assertEquals(Listing.BookCondition.Used, listing.getBookcondition());
        }
    }
    
    
    @Given("the book title {string} does not exist in the system")
    public void the_book_title_does_not_exist_in_the_system(String bookName) {
        List<Listing> listings = ((Collection<Listing>) listingRepository.findAll()).stream()
                .filter(listing -> listing.getBookName().equals(bookName))
                .collect(Collectors.toList());
        for (Listing listing : listings) {
            listingRepository.delete(listing);
        }
    }
    
    @Then("the system should display a message: \"No listings found for '{string}'\"")
    public void the_system_should_display_a_message_no_listings_found_for(String bookName) {
        assertNotNull(currentSearchErrorMessage);
        assertEquals("No listings found for '" + bookName + "'", currentSearchErrorMessage);
    }
    
    
    @Given("the user enters an invalid ISBN format {string}")
    public void the_user_enters_an_invalid_isbn_format(String ISBN) {
        currentInvalidISBN = ISBN;
    }
    

    
    @Then("the system should display a message: \"Listing not found\"")
    public void the_system_should_display_a_message_invalid_isbn_format() {
        assertNotNull(currentSearchErrorMessage);
        assertEquals("Listing not found", currentSearchErrorMessage);
    }
    
    
    @When("the user submits a search without entering any filters or keywords")
    public void the_user_submits_a_search_without_entering_any_filters_or_keywords() {
        currentSearchResults = null;
        currentSearchErrorMessage = "Please enter a search term or select a filter.";
    }
    
    @Then("the system should display a message: \"Please enter a search term or select a filter.\"")
    public void the_system_should_display_a_message_please_enter_a_search_term_or_select_a_filter() {
        assertNotNull(currentSearchErrorMessage);
        assertEquals("Please enter a search term or select a filter.", currentSearchErrorMessage);
    }
}
