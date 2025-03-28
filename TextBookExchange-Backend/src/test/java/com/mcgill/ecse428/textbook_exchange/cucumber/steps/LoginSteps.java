package com.mcgill.ecse428.textbook_exchange.cucumber.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse428.textbook_exchange.exception.TextBookExchangeException;
import com.mcgill.ecse428.textbook_exchange.service.AccountService;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@SpringBootTest
public class LoginSteps {

    @Autowired
    private AccountService accountService;

    // Store current credentials and any error message from login attempt.
    private String currentEmail;
    private String currentPassword;
    private String loginErrorMessage;

    // Scenario: Registered user logs into account (Normal Flow)
    @Given("a registered user or admin with email {string} and password {string} exists")
    public void a_registered_user_or_admin_with_email_and_password_exists(String email, String password) {
        this.currentEmail = email;
        this.currentPassword = password;
        
        // Remove any existing account with this email.
        try {
            accountService.deleteUser(email);
        } catch (Exception e) { /* ignore if doesn't exist */ }
        try {
            accountService.deleteAdmin(email);
        } catch (Exception e) { /* ignore if doesn't exist */ }
        
        // Create an admin account with the given credentials.
        // (Using admin creation because it has fewer restrictions on password.)
        accountService.createAdmin(email, "TestAdmin", password, "1234567890");
    }

    // Scenario: User login fails due to incorrect password (Error Flow)
    @Given("a registered user with {string} exists")
    public void a_registered_user_with_exists(String email) {
        this.currentEmail = email;
        // Use a valid default password that meets user creation criteria.
        this.currentPassword = "DefaultPass1";
        
        // Remove any existing account with this email.
        try {
            accountService.deleteUser(email);
        } catch (Exception e) { }
        try {
            accountService.deleteAdmin(email);
        } catch (Exception e) { }
        
        // Try to create a user account.
        try {
            accountService.createUser(email, "TestUser", currentPassword, "1234567890");
        } catch (TextBookExchangeException e) {
            // If user creation fails (due to validation), fall back to admin.
            accountService.createAdmin(email, "TestAdmin", currentPassword, "1234567890");
        }
    }

    // Scenario: User login fails due to unregistered email (Error Flow)
    @Given("no account exists for email {string}")
    public void no_account_exists_for_email(String email) {
        this.currentEmail = email;
        // Delete any user or admin with this email.
        try {
            accountService.deleteUser(email);
        } catch (Exception e) { }
        try {
            accountService.deleteAdmin(email);
        } catch (Exception e) { }
    }

    // When valid credentials are entered.
    @When("the user enters valid credentials and submits the login request")
    public void the_user_enters_valid_credentials_and_submits_the_login_request() {
        try {
            accountService.login(currentEmail, currentPassword);
            loginErrorMessage = null;
        } catch (TextBookExchangeException e) {
            loginErrorMessage = e.getMessage();
        }
    }

    // When an incorrect password is entered.
    @When("they enter an incorrect password {string}")
    public void they_enter_an_incorrect_password(String wrongPassword) {
        this.currentPassword = wrongPassword;
        try {
            accountService.login(currentEmail, currentPassword);
            loginErrorMessage = null;
        } catch (TextBookExchangeException e) {
            loginErrorMessage = e.getMessage();
        }
    }

    // When a login is attempted (for unregistered email case).
    @When("they attempt to login with {string}")
    public void they_attempt_to_login_with(String password) {
        this.currentPassword = password;
        try {
            accountService.login(currentEmail, currentPassword);
            loginErrorMessage = null;
        } catch (TextBookExchangeException e) {
            loginErrorMessage = e.getMessage();
        }
    }

    // Then the login should succeed.
    @Then("the user is logged in successfully")
    public void the_user_is_logged_in_successfully() {
        assertNull(loginErrorMessage, "Expected successful login but got error: " + loginErrorMessage);
    }

    // Then the proper error message is displayed.
    @Then("an {string} message is displayed")
    public void an_message_is_displayed(String expectedMessage) {
        assertNotNull(loginErrorMessage, "Expected an error message but login succeeded.");
        assertEquals(expectedMessage, loginErrorMessage);
    }
}
