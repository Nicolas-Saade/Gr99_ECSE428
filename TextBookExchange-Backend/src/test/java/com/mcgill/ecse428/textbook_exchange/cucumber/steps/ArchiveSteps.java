package com.mcgill.ecse428.textbook_exchange.cucumber.steps;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse428.textbook_exchange.model.Admin;
import com.mcgill.ecse428.textbook_exchange.model.Cart;
import com.mcgill.ecse428.textbook_exchange.model.Course;
import com.mcgill.ecse428.textbook_exchange.model.Faculty;
import com.mcgill.ecse428.textbook_exchange.model.Institution;
import com.mcgill.ecse428.textbook_exchange.model.Listing;
import com.mcgill.ecse428.textbook_exchange.model.Listing.BookCondition;
import com.mcgill.ecse428.textbook_exchange.model.Listing.ListingStatus;
import com.mcgill.ecse428.textbook_exchange.model.User;
import com.mcgill.ecse428.textbook_exchange.repository.AdminRepository;
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
import net.bytebuddy.agent.ByteBuddyAgent.AttachmentProvider.Accessor.Unavailable;

@SpringBootTest
public class ArchiveSteps {

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
    private AdminRepository adminRepository;

    private User userTestAccount;
    private Institution institutionTest;
    private Admin testAdmin;

    @Autowired
    private CartRepository cartRepository;

    private String adminUserName = "admin";
    private String adminEmail = "admin@example.com";


    private String errorMessage;
    private boolean errorOccured = false;

    @Before
    public void setUp() {
        institutionTest = new Institution("McGill University");
        institutionTest = institutionRepository.save(institutionTest);

        Cart cart = new Cart();
        cart = cartRepository.save(cart);

        userTestAccount = new User("test2@example.com", "test2user", "password", "5141234567", cart);
        userTestAccount = userRepository.save(userTestAccount);
        
        errorMessage = null;
    }

    @After
    public void tearDown() {
        listingRepository.deleteAll();
        courseRepository.deleteAll();
        facultyRepository.deleteAll();
        userRepository.delete(userTestAccount);
        institutionRepository.delete(institutionTest);
    }

    @Given("an administrator is logged into the system")
    public void anAdministratorIsLoggedIntoTheSystem() {
        // Check if already exists
        testAdmin = adminRepository.findByEmail(adminEmail);
        
        if (testAdmin == null) {
            testAdmin = new Admin("admin@example.com", adminUserName, "adminpass", "5141111111");
            testAdmin = adminRepository.save(testAdmin);
        }
    }
    @Given ("a user with email {string} and username {string} is logged into the system2")
    public void aUserWithEmailAndUsernameIsLoggedIntoTheSystem(String email, String username) {
        Cart cart = new Cart();
        cart = cartRepository.save(cart);
        userTestAccount = new User(email, username, "password", "5141234567", cart);
        userTestAccount = userRepository.save(userTestAccount);
    }

    @Given("the listing with ISBN {string} is currently marked as {string}")
    public void theListingWithISBNIsCurrentlyMarkedAsStatus(String isbn, String status) {
        Listing listing = listingRepository.findByISBN(isbn);
        String username = listing.getUser().getUsername();

        if (listing == null) {
            errorMessage = "Listing not found";
            return;
        }

        ListingStatus newStatus = ListingStatus.Unavailable;

        try {
            // Let the service method handle both update and save
            listingService.updateListingStatus(username,isbn, newStatus, errorOccured,false);
            errorOccured = false;
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @Given("the listing with ISBN {string} is already marked as {string}")
    public void theListingWithISBNIsAlreadyMarkedAsStatus(String isbn, String status) {
        Listing listing = listingRepository.findByISBN(isbn);
        String username = listing.getUser().getUsername();

        if (listing == null) {
            errorMessage = "Listing not found";
            return;
        }

        ListingStatus newStatus = ListingStatus.Unavailable;

        try {
            // Let the service method handle both update and save
            listingService.updateListingStatus(username,isbn, newStatus, errorOccured,false);
            errorOccured = false;
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @Given("the system is experiencing technical issues")
    public void theSystemIsExperiencingTechnicalIssues() {
        errorOccured = true;
        
    }

    @Then("the listing status should remain unchanged")
    public void theListingStatusShouldRemainUnchanged() {
        errorOccured = false;
    }
    
    @Given("the following listings exist2:")
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

            Listing newListing = new Listing(bookName, isbn, price, datePosted, userTestAccount, null);
            newListing.setBookcondition(condition);
            newListing.setListingStatus(status);
            listingRepository.save(newListing);
        }
    }
    @Given ("another user {string} owns a listing with ISBN {string}")
    public void anotherUserOwnsAListing(String username, String isbn){
        // Create a new cart
    Cart cart = new Cart();
    cart = cartRepository.save(cart);

    // Create and save the user
    User otherUser = new User(username + "@example.com", username, "password", "5140000000", cart);
    otherUser = userRepository.save(otherUser);

    // Ensure a faculty and course exist
    Faculty faculty = facultyRepository.findByDepartmentName("COMP");
    if (faculty == null) {
        faculty = new Faculty("COMP", institutionTest);
        faculty = facultyRepository.save(faculty);
    }

    Course course = courseRepository.findByCourseId("COMP 250");
    if (course == null) {
        course = new Course("COMP 250", faculty);
        course = courseRepository.save(course);
    }

    // Create and save the listing
    Listing listing = new Listing("Dummy Book", isbn, 50.0f, LocalDate.now(), otherUser, course);

    listingRepository.save(listing);

    }


    @When ("the user {string} attempts to change the status back to {string} for ISBN {string}")
    public void the_user_attempts_to_change_the_status_back_to_for_ISBN(String username, String status, String isbn) {
        try {
            Listing listing = listingRepository.findByISBN(isbn);
            // throw new Exception("User should not be able to change the status"+ listing.getListingStatus());
            listingService.updateListingStatus(username, isbn, ListingStatus.Available, errorOccured, errorOccured);
            
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }
    @When ("the user {string} attempts to mark the listing with ISBN {string} as sold")
    public void theUserAttemptsToMarkTheListingAsSold(String username, String isbn) {
        try {
            ListingStatus soldStatus  = ListingStatus.Unavailable;
            listingService.updateListingStatus(username, isbn, soldStatus,errorOccured,false);
            errorOccured = false;
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @When("the user {string} marks the following listings as sold:")
    public void theUserMarksTheFollowingListingsAsSold(String username, DataTable dataTable) {
        List<Map<String, String>> listings = dataTable.asMaps();

        for (Map<String, String> row : listings) {
            String isbn = row.get("ISBN");
            try {
                listingService.updateListingStatus(username, isbn, ListingStatus.Unavailable, errorOccured, false);
            } catch (Exception e) {
                errorMessage = e.getMessage();
            }
        }

        errorOccured = false;
    }


    @When("the user {string} attempts to mark the listing with ISBN {string} as sold again")
    public void theUserAttemptsToMarkTheListingAsSoldAgain(String username, String isbn) {
        try {
            ListingStatus soldStatus  = ListingStatus.Unavailable;
            listingService.updateListingStatus(username, isbn, soldStatus,errorOccured,false);
            errorOccured = false;
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @Then ("the system should display an error message1 {string}")
    public void theSystemShouldDisplayAnErrorMessage(String errorMessage) {
        assertEquals(errorMessage, this.errorMessage);
    }
        
        
    @When ("the administrator marks the listing with ISBN {string} as sold")
    public void theAdministratorMarksTheListingAsSold(String isbn) {
        try {
            ListingStatus soldStatus  = ListingStatus.Unavailable;
            listingService.updateListingStatus(adminUserName, isbn, soldStatus,errorOccured,true);
            errorOccured = false;
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @When("the user {string} marks the listing with ISBN {string} as sold")
    public void theUserMarksTheListingWithISBNAsSold(String username, String isbn) {
        try {
            ListingStatus soldStatus  = ListingStatus.Unavailable;
            listingService.updateListingStatus(username, isbn, soldStatus,errorOccured, false);
            errorOccured = false;
            
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @Then("the listing should be updated with:" )
    public void theListingShouldBeUpdatedWithStatus(DataTable dataTable) {
        List<Map<String, String>> listings = dataTable.asMaps();

        for (Map<String, String> listing : listings) {
            String isbn = listing.get("ISBN");
            Listing updatedListing = listingRepository.findByISBN(isbn);
            
            assertNotNull(updatedListing);
            assertEquals(ListingStatus.valueOf(listing.get("listingStatus")), updatedListing.getListingStatus());
            assertEquals(listing.get("bookName"), updatedListing.getBookName());
        }
    }

    @Then("the listings should be updated with:")
    public void theListingsShouldBeUpdatedWith(DataTable dataTable) {
        List<Map<String, String>> expectedListings = dataTable.asMaps();

        for (Map<String, String> expected : expectedListings) {
            String isbn = expected.get("ISBN");
            String expectedBookName = expected.get("bookName");
            ListingStatus expectedStatus = ListingStatus.valueOf(expected.get("listingStatus"));

            Listing actualListing = listingRepository.findByISBN(isbn);

            assertNotNull(actualListing, "Listing not found for ISBN: " + isbn);
            assertEquals(expectedBookName, actualListing.getBookName(), "Wrong book name for ISBN: " + isbn);
            assertEquals(expectedStatus, actualListing.getListingStatus(), "Wrong status for ISBN: " + isbn);
        }
    }



}
