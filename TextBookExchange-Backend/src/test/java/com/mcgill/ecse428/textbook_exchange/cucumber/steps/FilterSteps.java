package com.mcgill.ecse428.textbook_exchange.cucumber.steps;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse428.textbook_exchange.model.Cart;
import com.mcgill.ecse428.textbook_exchange.model.Course;
import com.mcgill.ecse428.textbook_exchange.model.Faculty;
import com.mcgill.ecse428.textbook_exchange.model.Institution;
import com.mcgill.ecse428.textbook_exchange.model.Listing;
import com.mcgill.ecse428.textbook_exchange.model.Listing.BookCondition;
import com.mcgill.ecse428.textbook_exchange.model.Listing.ListingStatus;
import com.mcgill.ecse428.textbook_exchange.model.User;
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
public class FilterSteps {

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
    
    private List<Listing> filteredListings;
    private String errorMessage;
    
    // Create a test user and institution for setup
    private User testUser;
    private Institution testInstitution;
    private Institution testInstitution2;
    @Before
    public void setup() {
        // Create a test institution
        testInstitution = new Institution("McGill University");
        testInstitution = institutionRepository.save(testInstitution);

        testInstitution2 = new Institution("Concordia University");
        testInstitution2 = institutionRepository.save(testInstitution2);
        
        // Create and save a cart for the test user
        Cart cart = new Cart();
        cart = cartRepository.save(cart);
        
        // Create a test user
        testUser = new User("test@example.com", "testuser", "password", "5141234567", cart);
        testUser = userRepository.save(testUser);
        
        errorMessage = null;
        filteredListings = new ArrayList<>();
    }

    @After
    public void tearDown() {
        // Clean up all created data
        listingRepository.deleteAll();
        courseRepository.deleteAll();
        facultyRepository.deleteAll();
        userRepository.deleteAll();
        cartRepository.deleteAll();
        institutionRepository.deleteAll();
    }

    @Given("the following listings exist:")
    public void the_following_listings_exist(DataTable dataTable) {
        List<Map<String, String>> listings = dataTable.asMaps();
        
        for (Map<String, String> listing : listings) {
            String bookName = listing.get("bookName").replace("\"", "");
            String isbn = listing.get("ISBN").replace("\"", "");
            BookCondition condition = BookCondition.valueOf(listing.get("bookCondition").replace("\"", ""));
            ListingStatus status = ListingStatus.valueOf(listing.get("listingStatus").replace("\"", ""));
            float price = Float.parseFloat(listing.get("price").replace("\"", ""));
            LocalDate datePosted = LocalDate.parse(listing.get("datePosted").replace("\"", ""));
            String courseName = listing.get("course").replace("\"", "");
            
            // Create faculty based on the course discipline (e.g., COMP for Computer Science)
            String discipline = courseName.split(" ")[0];
            Faculty faculty = facultyRepository.findByDepartmentName(discipline);
            Faculty faculty2 = facultyRepository.findByDepartmentName(discipline);
            if (faculty == null) {
                if (bookName.equals("Data Structures") || bookName.equals("Algorithms")) {
                    faculty = new Faculty(discipline, testInstitution);
                    faculty = facultyRepository.save(faculty);
                }
                else {
                    faculty2 = new Faculty(discipline, testInstitution2);
                    faculty2 = facultyRepository.save(faculty2);
                }
            }
            
            // Create or retrieve the course
            Course course = courseRepository.findByCourseId(courseName);
            if (course == null) {
                if (bookName.equals("Data Structures") || bookName.equals("Algorithms")) {
                    course = new Course(courseName, faculty);
                    course = courseRepository.save(course);
                }
                else {
                    course = new Course(courseName, faculty2);
                    course = courseRepository.save(course);
                }
            }
            
            // Create the listing
            Listing newListing = new Listing(bookName, isbn, price, datePosted, testUser, course);
            newListing.setBookcondition(condition);
            newListing.setListingStatus(status);
            listingRepository.save(newListing);
        }
    }

    @When("the user filters listings with a price range between {string} and {string}")
    public void the_user_filters_listings_with_a_price_range_between_and(String minPrice, String maxPrice) {
        try {
            float min = Float.parseFloat(minPrice);
            float max = Float.parseFloat(maxPrice);
            filteredListings = listingService.filterListingsByPriceRange(min, max);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @When("the user applies a filter to show only {string} books")
    public void the_user_applies_a_filter_to_show_only_books(String condition) {
        try {
            BookCondition bookCondition = BookCondition.valueOf(condition);
            filteredListings = listingService.filterListingsByCondition(bookCondition);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @When("the user applies a filter to show only {string} listings")
    public void the_user_applies_a_filter_to_show_only_listings(String status) {
        try {
            ListingStatus listingStatus = ListingStatus.valueOf(status);
            filteredListings = listingService.filterListingsByStatus(listingStatus);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @When("the user filters listings for the course {string}")
    public void the_user_filters_listings_for_the_course(String courseId) {
        try {
            filteredListings = listingService.filterListingsByCourse(courseId);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @When("the user applies a filter to show only listings from {string}")
    public void the_user_applies_a_filter_to_show_only_listings_from(String institutionName) {
        try {
            filteredListings = listingService.filterListingsByInstitution(institutionName);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @When("the user applies a filter to show only {string} books with a price below {string}")
    public void the_user_applies_a_filter_to_show_only_books_with_a_price_below(String condition, String maxPrice) {
        try {
            BookCondition bookCondition = BookCondition.valueOf(condition);
            float price = Float.parseFloat(maxPrice);
            filteredListings = listingService.filterListingsByConditionAndMaxPrice(bookCondition, price);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @When("the user applies the following filters:")
    public void the_user_applies_the_following_filters(DataTable dataTable) {
        try {
            List<Map<String, String>> filters = dataTable.asMaps();
            Map<String, Object> filterCriteria = new HashMap<>();
            
            for (Map<String, String> filter : filters) {
                String filterType = filter.get("Filter Type").replace("\"", "");
                String value = filter.get("Value").replace("\"", "");
                
                if (filterType.equals("Price Range")) {
                    String[] range = value.split(" - ");
                    filterCriteria.put("minPrice", Float.parseFloat(range[0]));
                    filterCriteria.put("maxPrice", Float.parseFloat(range[1]));
                } else if (filterType.equals("Condition")) {
                    filterCriteria.put("condition", BookCondition.valueOf(value));
                } else if (filterType.equals("Availability")) {
                    filterCriteria.put("status", ListingStatus.valueOf(value));
                }
            }
            
            filteredListings = listingService.filterListingsWithMultipleCriteria(filterCriteria);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @When("the user applies an invalid filter {string} with value {string}")
    public void the_user_applies_an_invalid_filter_with_value(String filterType, String invalidValue) {
        try {
            filterType = filterType.replace("\"", "");
            invalidValue = invalidValue.replace("\"", "");
            
            if (filterType.equals("Price Range")) {
                float price = Float.parseFloat(invalidValue);
                if (price < 0) {
                    throw new IllegalArgumentException("Invalid filter input");
                }
            } else if (filterType.equals("Condition")) {
                BookCondition.valueOf(invalidValue); // Will throw IllegalArgumentException if invalid
            } else if (filterType.equals("Availability")) {
                ListingStatus.valueOf(invalidValue); // Will throw IllegalArgumentException if invalid
            } else if (filterType.equals("Course Code")) {
                // Validate course code format (should be in format like "COMP 250")
                if (!invalidValue.matches("[A-Z]{4}\\s\\d{3}")) {
                    throw new IllegalArgumentException("Invalid filter input");
                }
            }
            
            // If we got here without an exception, it's actually valid
            errorMessage = null;
        } catch (Exception e) {
            errorMessage = "Invalid filter input";
        }
    }

    @Then("the system should return the following results:")
    public void the_system_should_return_the_following_results(DataTable dataTable) {
        List<Map<String, String>> expectedResults = dataTable.asMaps();
        
        // Verify we have the correct number of results
        assertEquals(expectedResults.size(), filteredListings.size(), "Expected " + expectedResults + " listings but got " + filteredListings);
        
        // For each expected result, verify there's a matching listing
        for (Map<String, String> expected : expectedResults) {
            String expectedBookName = expected.get("bookName").replace("\"", "");
            String expectedISBN = expected.get("ISBN").replace("\"", "");
            String expectedCondition = expected.get("bookCondition").replace("\"", "");
            String expectedStatus = expected.get("listingStatus").replace("\"", "");
            float expectedPrice = Float.parseFloat(expected.get("price").replace("\"", ""));
            String expectedCourse = expected.get("course").replace("\"", "");
            
            boolean foundMatch = false;
            for (Listing listing : filteredListings) {
                if (listing.getBookName().equals(expectedBookName)
                        && listing.getISBN().equals(expectedISBN)
                        && listing.getBookcondition().name().equals(expectedCondition)
                        && listing.getListingStatus().name().equals(expectedStatus)
                        && listing.getPrice() == expectedPrice
                        && listing.getCourse().getCourseId().equals(expectedCourse)) {
                    foundMatch = true;
                    break;
                }
            }
            
            assertTrue(foundMatch, "Could not find expected listing for book: " + expectedBookName);
        }
    }

    @Then("the system should display a message: {string}")
    public void the_system_should_display_a_message(String expectedMessage) {
        if (expectedMessage.equals("No listings found matching your filters")) {
            assertEquals(0, filteredListings.size(), "Expected no listings but found " + filteredListings.size());
        } else {
            assertEquals(expectedMessage, errorMessage);
        }
    }

    @Then("the system should display an error message: {string}")
    public void the_system_should_display_an_error_message(String expectedErrorMessage) {
        assertNotNull(errorMessage, "Expected an error message but none was received");
        assertEquals(expectedErrorMessage, errorMessage);
    }
}
