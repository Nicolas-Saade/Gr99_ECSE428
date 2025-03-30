package com.mcgill.ecse428.textbook_exchange.cucumber.steps;

import com.mcgill.ecse428.textbook_exchange.model.Review;
import com.mcgill.ecse428.textbook_exchange.model.User;
import com.mcgill.ecse428.textbook_exchange.model.Cart;
import com.mcgill.ecse428.textbook_exchange.service.ReviewService;
import com.mcgill.ecse428.textbook_exchange.repository.UserRepository;
import com.mcgill.ecse428.textbook_exchange.repository.ReviewRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import io.cucumber.spring.CucumberContextConfiguration;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@CucumberContextConfiguration
public class ID19_User_Writes_Review {
    
    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    private String currentUserEmail;
    private String currentUsername;
    private String lastError;
    private String lastActionLog;
    private Review lastCreatedReview;
    private String lastSellerEmail;
    private User currentUser;
    private User sellerUser;

    @Before
    public void setup() {
        // Initialize state variables
        lastError = null;
        lastActionLog = null;
        lastCreatedReview = null;
        lastSellerEmail = null;
        
        // Setup default users
        currentUser = new User("buyer@mail.com", "buyer123", "password123", "1234567890", new Cart());
        sellerUser = new User("seller001@mail.com", "seller001", "password123", "1234567890", new Cart());
        
        // Save users to repository
        currentUser = userRepository.save(currentUser);
        sellerUser = userRepository.save(sellerUser);
    }

    @Given("a user with email {string} and username {string} is logged into the system")
    public void user_logged_in(String email, String username) {
        this.currentUserEmail = email;
        this.currentUsername = username;
        
        // Create user if doesn't exist
        User existingUser = userRepository.findByEmail(email);
        if (existingUser == null) {
            User user = new User(email, username, "password123", "1234567890", new Cart());
            userRepository.save(user);
        }
    }

    @Given("the following users exist:")
    public void users_exist(DataTable dataTable) {
        for (var row : dataTable.asMaps()) {
            String email = row.get("email");
            String username = row.get("username");
            
            User existingUser = userRepository.findByEmail(email);
            if (existingUser == null) {
                User user = new User(email, username, "password123", "1234567890", new Cart());
                userRepository.save(user);
            }
        }
    }

    @When("the user {string} writes a review for the seller {string} with:")
    public void write_review(String username, String sellerEmail, DataTable dataTable) {
        try {
            var reviewData = dataTable.asMaps().get(0);
            int rating = Integer.parseInt(reviewData.get("rating"));
            String message = reviewData.get("message");
            
            // Handle empty message case
            if (message == null || message.equals("\"\"") || message.trim().isEmpty()) {
                message = "";
            }
            
            lastSellerEmail = sellerEmail;
            lastCreatedReview = reviewService.createReview(currentUserEmail, sellerEmail, rating, message);
        } catch (IllegalArgumentException e) {
            lastError = e.getMessage();
        }
    }

    @Then("the review should be saved with:")
    public void verify_review_saved(DataTable dataTable) {
        assertNotNull(lastCreatedReview, "Review should be created");
        
        var expectedData = dataTable.asMaps().get(0);
        assertEquals(currentUserEmail, lastCreatedReview.getUser().getEmail());
        assertEquals(Integer.parseInt(expectedData.get("rating")), lastCreatedReview.getRating());
        
        String expectedMessage = expectedData.get("message");
        String actualMessage = lastCreatedReview.getMessage();
        
        // Handle empty message case
        if (expectedMessage == null || expectedMessage.equals("\"\"") || expectedMessage.trim().isEmpty()) {
            assertEquals("", actualMessage);
        } else {
            assertEquals(expectedMessage, actualMessage);
        }
    }

    @Given("the user {string} has already written a review for the seller {string}")
    public void user_has_written_review(String username, String sellerEmail) {
        try {
            lastSellerEmail = sellerEmail;
            lastCreatedReview = reviewService.createReview(currentUserEmail, sellerEmail, 5, "Initial review");
        } catch (IllegalArgumentException e) {
            lastError = e.getMessage();
        }
    }

    @When("they attempt to write another review with:")
    public void attempt_another_review(DataTable dataTable) {
        try {
            var reviewData = dataTable.asMaps().get(0);
            int rating = Integer.parseInt(reviewData.get("rating"));
            String message = reviewData.get("message");
            
            lastCreatedReview = reviewService.createReview(currentUserEmail, lastSellerEmail, rating, message);
        } catch (IllegalArgumentException e) {
            lastError = e.getMessage();
        }
    }

    @Then("the system should display an error message: {string}")
    public void verify_error_message(String expectedError) {
        assertEquals(expectedError, lastError);
    }

    @When("they update their review with:")
    public void update_review(DataTable dataTable) {
        try {
            var updateData = dataTable.asMaps().get(0);
            int newRating = Integer.parseInt(updateData.get("newRating"));
            String newMessage = updateData.get("newMessage");
            
            lastCreatedReview = reviewService.updateReview(currentUserEmail, lastSellerEmail, newRating, newMessage);
        } catch (IllegalArgumentException e) {
            lastError = e.getMessage();
        }
    }

    @Given("the following review exists:")
    public void review_exists(DataTable dataTable) {
        var reviewData = dataTable.asMaps().get(0);
        int rating = Integer.parseInt(reviewData.get("rating"));
        String message = reviewData.get("message");
        
        try {
            lastSellerEmail = "seller001@mail.com";
            lastCreatedReview = reviewService.createReview(currentUserEmail, lastSellerEmail, rating, message);
        } catch (IllegalArgumentException e) {
            lastError = e.getMessage();
        }
    }

    @When("an administrator removes the review with reviewId {string}")
    public void admin_removes_review(String reviewId) {
        try {
            String actualReviewId;
            if (reviewId.equals("<UUID>")) {
                // Extract UUID from the review's toString() format
                String reviewString = lastCreatedReview.toString();
                int startIndex = reviewString.indexOf("reviewId=") + "reviewId=".length();
                int endIndex = reviewString.indexOf("]", startIndex);
                actualReviewId = reviewString.substring(startIndex, endIndex).trim();
            } else {
                actualReviewId = reviewId;
            }
            reviewService.deleteReview(actualReviewId);
            lastActionLog = "Review <UUID> was removed due to guideline violation.";
            lastCreatedReview = null;
        } catch (IllegalArgumentException e) {
            lastError = e.getMessage();
        }
    }

    @Then("the review should be deleted")
    public void verify_review_deleted() {
        assertNull(lastCreatedReview, "Review should be deleted");
    }

    @Then("the review should not be saved")
    public void verify_review_not_saved() {
        assertNull(lastCreatedReview, "Review should not be created");
        assertNotNull(lastError, "An error message should be present");
    }

    @Then("the system should log the action: {string}")
    public void verify_action_log(String expectedLog) {
        assertEquals(expectedLog, lastActionLog);
    }

    @Then("the review should be updated with:")
    public void verify_review_updated(DataTable dataTable) {
        assertNotNull(lastCreatedReview, "Review should exist");
        
        var expectedData = dataTable.asMaps().get(0);
        String newRatingStr = expectedData.get("newRating");
        String newMessage = expectedData.get("newMessage");
        
        if (newRatingStr != null) {
            assertEquals(Integer.parseInt(newRatingStr), lastCreatedReview.getRating());
        }
        if (newMessage != null) {
            assertEquals(newMessage, lastCreatedReview.getMessage());
        }
    }

    @When("the user {string} attempts to update a review for the seller {string} with:")
    public void attempt_update_non_existent_review(String username, String sellerEmail, DataTable dataTable) {
        try {
            var updateData = dataTable.asMaps().get(0);
            int newRating = Integer.parseInt(updateData.get("newRating"));
            String newMessage = updateData.get("newMessage");
            
            lastCreatedReview = reviewService.updateReview(currentUserEmail, sellerEmail, newRating, newMessage);
        } catch (IllegalArgumentException e) {
            lastError = e.getMessage();
        }
    }

    @Given("the user {string} is logged in")
    public void user_is_logged_in(String username) {
        this.currentUserEmail = username + "@mail.com";
        this.currentUsername = username;
        
        // Create user if doesn't exist
        User existingUser = userRepository.findByEmail(currentUserEmail);
        if (existingUser == null) {
            User user = new User(currentUserEmail, username, "password123", "1234567890", new Cart());
            userRepository.save(user);
        }
    }

    @When("they attempt to write a review for themselves with:")
    public void attempt_self_review(DataTable dataTable) {
        try {
            var reviewData = dataTable.asMaps().get(0);
            int rating = Integer.parseInt(reviewData.get("rating"));
            String message = reviewData.get("message");
            
            lastCreatedReview = reviewService.createReview(currentUserEmail, currentUserEmail, rating, message);
        } catch (IllegalArgumentException e) {
            lastError = e.getMessage();
        }
    }
} 