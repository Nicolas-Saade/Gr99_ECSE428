package com.mcgill.ecse428.textbook_exchange.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.mcgill.ecse428.textbook_exchange.exception.TextBookExchangeException;
import com.mcgill.ecse428.textbook_exchange.model.Account;
import com.mcgill.ecse428.textbook_exchange.model.Admin;
import com.mcgill.ecse428.textbook_exchange.model.Cart;
import com.mcgill.ecse428.textbook_exchange.model.User;
import com.mcgill.ecse428.textbook_exchange.repository.AccountRepository;
import com.mcgill.ecse428.textbook_exchange.repository.AdminRepository;
import com.mcgill.ecse428.textbook_exchange.repository.CartRepository;
import com.mcgill.ecse428.textbook_exchange.repository.UserRepository;

@SpringBootTest
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class AccountServiceTests {

    @Mock
    private AccountRepository mockAccountRepository;
    
    @Mock
    private AdminRepository mockAdminRepository;
    
    @Mock
    private UserRepository mockUserRepository;
    
    @Mock
    private CartRepository mockCartRepository;
    
    @InjectMocks
    private AccountService accountService;
    
    // Test constants for Admin
    private static final String VALID_EMAIL_ADMIN = "admin@test.com";
    private static final String VALID_USERNAME_ADMIN = "adminUser";
    private static final String VALID_PASSWORD_ADMIN = "adminPass";
    private static final String VALID_PHONE_ADMIN = "1234567890";
    
    // Test constants for User
    private static final String VALID_EMAIL_USER = "user@test.com";
    private static final String VALID_USERNAME_USER = "normalUser";
    private static final String VALID_PASSWORD_USER = "userPass";
    private static final String VALID_PHONE_USER = "0987654321";
    
    // Invalid input constants
    private static final String INVALID_EMPTY = "";
    private static final String INVALID_NULL = null;
    
  
    
    @Test
    public void testCreateValidAdmin() {
        // No account exists with this email
        //arrange
        when(mockAccountRepository.findByEmail(VALID_EMAIL_ADMIN)).thenReturn(null);
        when(mockAdminRepository.save(any(Admin.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        //act
        Admin admin = accountService.createAdmin(VALID_EMAIL_ADMIN, VALID_USERNAME_ADMIN, VALID_PASSWORD_ADMIN, VALID_PHONE_ADMIN);
        //assert
        assertNotNull(admin);
        assertEquals(VALID_EMAIL_ADMIN, admin.getEmail());
        assertEquals(VALID_USERNAME_ADMIN, admin.getUsername());
        assertEquals(VALID_PASSWORD_ADMIN, admin.getPassword());
        assertEquals(VALID_PHONE_ADMIN, admin.getPhoneNumber());
        verify(mockAdminRepository, times(1)).save(admin);
    }
    
    @Test
    public void testCreateAdminWithEmptyEmail() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.createAdmin(INVALID_EMPTY, VALID_USERNAME_ADMIN, VALID_PASSWORD_ADMIN, VALID_PHONE_ADMIN));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Email cannot be empty", exception.getMessage());
        verify(mockAdminRepository, never()).save(any(Admin.class));
    }
    @Test
    public void testCreateAdminWithEmptyUsername() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.createAdmin(VALID_EMAIL_ADMIN, INVALID_EMPTY, VALID_PASSWORD_ADMIN, VALID_PHONE_ADMIN));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Username cannot be empty", exception.getMessage());
        verify(mockAdminRepository, never()).save(any(Admin.class));
    }
    @Test
    public void testCreateAdminWithEmptyPassword() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.createAdmin(VALID_EMAIL_ADMIN, VALID_USERNAME_ADMIN, INVALID_EMPTY, VALID_PHONE_ADMIN));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Password cannot be empty", exception.getMessage());
        verify(mockAdminRepository, never()).save(any(Admin.class));
    }
    @Test
    public void testCreateAdminWithEmptyPhone() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.createAdmin(VALID_EMAIL_ADMIN, VALID_USERNAME_ADMIN, VALID_PASSWORD_ADMIN, INVALID_EMPTY));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Phone number cannot be empty", exception.getMessage());
        verify(mockAdminRepository, never()).save(any(Admin.class));
    }
    @Test
    public void testCreateAdminWithNullEmail() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.createAdmin(INVALID_NULL, VALID_USERNAME_ADMIN, VALID_PASSWORD_ADMIN, VALID_PHONE_ADMIN));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Email cannot be empty", exception.getMessage());
        verify(mockAdminRepository, never()).save(any(Admin.class));
    }
    @Test
    public void testCreateAdminWithNullUsername() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.createAdmin(VALID_EMAIL_ADMIN, INVALID_NULL, VALID_PASSWORD_ADMIN, VALID_PHONE_ADMIN));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Username cannot be empty", exception.getMessage());
        verify(mockAdminRepository, never()).save(any(Admin.class));
    }
    @Test
    public void testCreateAdminWithNullPassword() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.createAdmin(VALID_EMAIL_ADMIN, VALID_USERNAME_ADMIN, INVALID_NULL, VALID_PHONE_ADMIN));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Password cannot be empty", exception.getMessage());
        verify(mockAdminRepository, never()).save(any(Admin.class));
    }
    @Test
    public void testCreateAdminWithNullPhone() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.createAdmin(VALID_EMAIL_ADMIN, VALID_USERNAME_ADMIN, VALID_PASSWORD_ADMIN, INVALID_NULL));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Phone number cannot be empty", exception.getMessage());
        verify(mockAdminRepository, never()).save(any(Admin.class));
    }
    
    @Test
    public void testCreateAdminWithDuplicateEmail() {
        // An account already exists with the email
        Account existing = new Admin(VALID_EMAIL_ADMIN, "otherAdmin", "otherPass", "1111111111");
        when(mockAccountRepository.findByEmail(VALID_EMAIL_ADMIN)).thenReturn(existing);
        
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.createAdmin(VALID_EMAIL_ADMIN, VALID_USERNAME_ADMIN, VALID_PASSWORD_ADMIN, VALID_PHONE_ADMIN));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Email already exists", exception.getMessage());
        verify(mockAdminRepository, never()).save(any(Admin.class));
    }
    
    @Test
    public void testGetAdminSuccess() {
        Admin admin = new Admin(VALID_EMAIL_ADMIN, VALID_USERNAME_ADMIN, VALID_PASSWORD_ADMIN, VALID_PHONE_ADMIN);
        when(mockAdminRepository.findByEmail(VALID_EMAIL_ADMIN)).thenReturn(admin);
        
        Admin result = accountService.getAdmin(VALID_EMAIL_ADMIN);
        assertNotNull(result);
        assertEquals(VALID_EMAIL_ADMIN, result.getEmail());
        verify(mockAdminRepository, times(1)).findByEmail(VALID_EMAIL_ADMIN);
    }
    
    @Test
    public void testGetAdminNotFound() {
        when(mockAdminRepository.findByEmail(VALID_EMAIL_ADMIN)).thenReturn(null);
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.getAdmin(VALID_EMAIL_ADMIN));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Admin not found", exception.getMessage());
        verify(mockAdminRepository, times(1)).findByEmail(VALID_EMAIL_ADMIN);
    }
    @Test
    public void testGetAllAdmins() {
        Admin admin = new Admin(VALID_EMAIL_ADMIN, VALID_USERNAME_ADMIN, VALID_PASSWORD_ADMIN, VALID_PHONE_ADMIN);
        Admin secondAdmin = new Admin(VALID_EMAIL_ADMIN+"j", VALID_USERNAME_ADMIN+"j", VALID_PASSWORD_ADMIN+"j", VALID_PHONE_ADMIN+"4");
        when(mockAdminRepository.findAll()).thenReturn(java.util.Arrays.asList(admin, secondAdmin));
        Iterable<Account> result = accountService.getAllAdmins();
        assertNotNull(result);
        assertEquals(2, ((java.util.List<Account>) result).size());
        verify(mockAdminRepository, times(1)).findAll();
        assertEquals(admin.getEmail(), ((java.util.List<Account>) result).get(0).getEmail());
        assertEquals(secondAdmin.getEmail(), ((java.util.List<Account>) result).get(1).getEmail());
        assertEquals(admin.getUsername(), ((java.util.List<Account>) result).get(0).getUsername());
        assertEquals(secondAdmin.getUsername(), ((java.util.List<Account>) result).get(1).getUsername());
        assertEquals(admin.getPassword(), ((java.util.List<Account>) result).get(0).getPassword());
        assertEquals(secondAdmin.getPassword(), ((java.util.List<Account>) result).get(1).getPassword());
        assertEquals(admin.getPhoneNumber(), ((java.util.List<Account>) result).get(0).getPhoneNumber());
        assertEquals(secondAdmin.getPhoneNumber(), ((java.util.List<Account>) result).get(1).getPhoneNumber());
    }
    
    @Test
    public void testUpdateAdminSuccess() {
        Admin admin = new Admin(VALID_EMAIL_ADMIN, VALID_USERNAME_ADMIN, VALID_PASSWORD_ADMIN, VALID_PHONE_ADMIN);
        when(mockAdminRepository.findByEmail(VALID_EMAIL_ADMIN)).thenReturn(admin);
        when(mockAdminRepository.save(any(Admin.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        String newUsername = "newAdmin";
        String newPassword = "newPass";
        String newPhone = "2222222222";
        Admin updated = accountService.updateAdmin(VALID_EMAIL_ADMIN, newUsername, newPassword, newPhone);
        
        assertNotNull(updated);
        assertEquals(newUsername, updated.getUsername());
        assertEquals(newPassword, updated.getPassword());
        assertEquals(newPhone, updated.getPhoneNumber());
    }
    @Test
    public void testUpdateAdminNotFound() {
        when(mockAdminRepository.findByEmail(VALID_EMAIL_ADMIN+"s")).thenReturn(null);
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.updateAdmin(VALID_EMAIL_ADMIN+"s", VALID_USERNAME_ADMIN, VALID_PASSWORD_ADMIN, VALID_PHONE_ADMIN));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Admin not found", exception.getMessage());
        verify(mockAdminRepository, never()).save(any(Admin.class));
    }
    
    @Test
    public void testDeleteAdminSuccess() {
        Admin admin = new Admin(VALID_EMAIL_ADMIN, VALID_USERNAME_ADMIN, VALID_PASSWORD_ADMIN, VALID_PHONE_ADMIN);
        when(mockAdminRepository.findByEmail(VALID_EMAIL_ADMIN)).thenReturn(admin);
        
        accountService.deleteAdmin(VALID_EMAIL_ADMIN);
        verify(mockAdminRepository, times(1)).delete(admin);
    }
    
    @Test
    public void testDeleteAdminNotFound() {
        when(mockAdminRepository.findByEmail(VALID_EMAIL_ADMIN)).thenReturn(null);
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.deleteAdmin(VALID_EMAIL_ADMIN));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Admin not found", exception.getMessage());
        verify(mockAdminRepository, never()).delete(any(Admin.class));
    }

    
    //==================== User Tests ====================
    
    // Feature #1: Create new user
    // Scenario Outline: User creates an account with valid information (Normal Flow)
    @Test
    public void testCreateValidUser() {
        // No account exists with this email
        // arrange
        when(mockAccountRepository.findByEmail(VALID_EMAIL_USER)).thenReturn(null);
        when(mockCartRepository.save(any(Cart.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(mockUserRepository.save(any(User.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        // act
        User user = accountService.createUser(VALID_EMAIL_USER, VALID_USERNAME_USER, VALID_PASSWORD_USER, VALID_PHONE_USER);
        // assert
        assertNotNull(user);
        assertEquals(VALID_EMAIL_USER, user.getEmail());
        assertEquals(VALID_USERNAME_USER, user.getUsername());
        assertEquals(VALID_PASSWORD_USER, user.getPassword());
        assertEquals(VALID_PHONE_USER, user.getPhoneNumber());
        // Verify that a Cart was created and associated
        assertNotNull(user.getCart());
        verify(mockUserRepository, times(1)).save(user);
    }

    // Feature #1: Create new user
    // Scenario Outline: User attempts to create an account with empty email (Error Flow)
    @Test
    public void testCreateUserWithEmptyEmail() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.createUser(INVALID_EMPTY, VALID_USERNAME_USER, VALID_PASSWORD_USER, VALID_PHONE_USER));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Email cannot be empty", exception.getMessage());
        verify(mockUserRepository, never()).save(any(User.class));
    }

    // Feature #1: Create new user
    // Scenario Outline: User attempts to create an account with empty username (Error Flow)
    @Test
    public void testCreateUserWithEmptyUsername() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.createUser(VALID_EMAIL_USER, INVALID_EMPTY, VALID_PASSWORD_USER, VALID_PHONE_USER));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Username cannot be empty", exception.getMessage());
        verify(mockUserRepository, never()).save(any(User.class));
    }

    // Feature #1: Create new user
    // Scenario Outline: User attempts to create an account with empty password (Error Flow)
    @Test
    public void testCreateUserWithEmptyPassword() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.createUser(VALID_EMAIL_USER, VALID_USERNAME_USER, INVALID_EMPTY, VALID_PHONE_USER));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Password cannot be empty", exception.getMessage());
        verify(mockUserRepository, never()).save(any(User.class));
    }

    // Feature #1: Create new user
    // Scenario Outline: User attempts to create an account with password that's too short (Error Flow)
    @Test
    public void testCreateUserWithShortPassword() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.createUser(VALID_EMAIL_USER, VALID_USERNAME_USER, "short", VALID_PHONE_USER));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Password must be at least 8 characters long", exception.getMessage());
        verify(mockUserRepository, never()).save(any(User.class));
    }

    // Feature #1: Create new user
    // Scenario Outline: User attempts to create an account with empty phone number (Error Flow)
    @Test
    public void testCreateUserWithEmptyPhone() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.createUser(VALID_EMAIL_USER, VALID_USERNAME_USER, VALID_PASSWORD_USER, INVALID_EMPTY));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Phone number cannot be empty", exception.getMessage());
        verify(mockUserRepository, never()).save(any(User.class));
    }

    // Feature #1: Create new user
    // Scenario Outline: User attempts to create an account with null email (Error Flow)
    @Test
    public void testCreateUserWithNullEmail() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.createUser(INVALID_NULL, VALID_USERNAME_USER, VALID_PASSWORD_USER, VALID_PHONE_USER));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Email cannot be empty", exception.getMessage());
        verify(mockUserRepository, never()).save(any(User.class));
    }

    // Feature #1: Create new user
    // Scenario Outline: User attempts to create an account with null username (Error Flow)
    @Test
    public void testCreateUserWithNullUsername() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.createUser(VALID_EMAIL_USER, INVALID_NULL, VALID_PASSWORD_USER, VALID_PHONE_USER));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Username cannot be empty", exception.getMessage());
        verify(mockUserRepository, never()).save(any(User.class));
    }

    // Feature #1: Create new user
    // Scenario Outline: User attempts to create an account with null password (Error Flow)
    @Test
    public void testCreateUserWithNullPassword() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.createUser(VALID_EMAIL_USER, VALID_USERNAME_USER, INVALID_NULL, VALID_PHONE_USER));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Password cannot be empty", exception.getMessage());
        verify(mockUserRepository, never()).save(any(User.class));
    }

    // Feature #1: Create new user
    // Scenario Outline: User attempts to create an account with null phone number (Error Flow)
    @Test
    public void testCreateUserWithNullPhone() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.createUser(VALID_EMAIL_USER, VALID_USERNAME_USER, VALID_PASSWORD_USER, INVALID_NULL));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Phone number cannot be empty", exception.getMessage());
        verify(mockUserRepository, never()).save(any(User.class));
    }

    // Feature #1: Create new user
    // Scenario Outline: User attempts to create an account with email that already exists (Error Flow)
    @Test
    public void testCreateUserWithDuplicateEmail() {
        // An account already exists with the email
        Account existing = new User(VALID_EMAIL_USER, "otherUser", "otherPass", "1111111111", new Cart());
        when(mockAccountRepository.findByEmail(VALID_EMAIL_USER)).thenReturn(existing);
        
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.createUser(VALID_EMAIL_USER, VALID_USERNAME_USER, VALID_PASSWORD_USER, VALID_PHONE_USER));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Email already exists", exception.getMessage());
        verify(mockUserRepository, never()).save(any(User.class));
    }

    @Test
    public void testGetUserSuccess() {
        User user = new User(VALID_EMAIL_USER, VALID_USERNAME_USER, VALID_PASSWORD_USER, VALID_PHONE_USER, new Cart());
        when(mockUserRepository.findByEmail(VALID_EMAIL_USER)).thenReturn(user);
        
        User result = accountService.getUser(VALID_EMAIL_USER);
        assertNotNull(result);
        assertEquals(VALID_EMAIL_USER, result.getEmail());
        verify(mockUserRepository, times(1)).findByEmail(VALID_EMAIL_USER);
    }

    @Test
    public void testGetUserNotFound() {
        when(mockUserRepository.findByEmail(VALID_EMAIL_USER)).thenReturn(null);
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.getUser(VALID_EMAIL_USER));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("User not found", exception.getMessage());
        verify(mockUserRepository, times(1)).findByEmail(VALID_EMAIL_USER);
    }

    // Feature #4: Admin views all accounts
    // Scenario Outline: Admin requests to view all user accounts (Normal Flow)
    @Test
    public void testGetAllUsers() {
        User user = new User(VALID_EMAIL_USER, VALID_USERNAME_USER, VALID_PASSWORD_USER, VALID_PHONE_USER, new Cart());
        User secondUser = new User(VALID_EMAIL_USER + "2", VALID_USERNAME_USER + "2", VALID_PASSWORD_USER + "2", VALID_PHONE_USER + "2", new Cart());
        when(mockUserRepository.findAll()).thenReturn(java.util.Arrays.asList(user, secondUser));
        
        Iterable<Account> result = accountService.getAllUsers();
        assertNotNull(result);
        assertEquals(2, ((java.util.List<Account>) result).size());
        verify(mockUserRepository, times(1)).findAll();
        assertEquals(user.getEmail(), ((java.util.List<Account>) result).get(0).getEmail());
        assertEquals(secondUser.getEmail(), ((java.util.List<Account>) result).get(1).getEmail());
        assertEquals(user.getUsername(), ((java.util.List<Account>) result).get(0).getUsername());
        assertEquals(secondUser.getUsername(), ((java.util.List<Account>) result).get(1).getUsername());
        assertEquals(user.getPassword(), ((java.util.List<Account>) result).get(0).getPassword());
        assertEquals(secondUser.getPassword(), ((java.util.List<Account>) result).get(1).getPassword());
        assertEquals(user.getPhoneNumber(), ((java.util.List<Account>) result).get(0).getPhoneNumber());
        assertEquals(secondUser.getPhoneNumber(), ((java.util.List<Account>) result).get(1).getPhoneNumber());
    }

    @Test
    public void testUpdateUserSuccess() {
        User user = new User(VALID_EMAIL_USER, VALID_USERNAME_USER, VALID_PASSWORD_USER, VALID_PHONE_USER, new Cart());
        when(mockUserRepository.findByEmail(VALID_EMAIL_USER)).thenReturn(user);
        when(mockUserRepository.save(any(User.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        String newUsername = "updatedUser";
        String newPassword = "updatedPass";
        String newPhone = "3333333333";
        User updated = accountService.updateUser(VALID_EMAIL_USER, newUsername, newPassword, newPhone);
        
        assertNotNull(updated);
        assertEquals(newUsername, updated.getUsername());
        assertEquals(newPassword, updated.getPassword());
        assertEquals(newPhone, updated.getPhoneNumber());
    }

    @Test
    public void testUpdateUserNotFound() {
        when(mockUserRepository.findByEmail(VALID_EMAIL_USER + "x")).thenReturn(null);
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.updateUser(VALID_EMAIL_USER + "x", VALID_USERNAME_USER, VALID_PASSWORD_USER, VALID_PHONE_USER));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("User not found", exception.getMessage());
        verify(mockUserRepository, never()).save(any(User.class));
    }

    @Test
    public void testUpdateUserWithInvalidPhoneNumber() {
        User user = new User(VALID_EMAIL_USER, VALID_USERNAME_USER, VALID_PASSWORD_USER, VALID_PHONE_USER, new Cart());
        when(mockUserRepository.findByEmail(VALID_EMAIL_USER)).thenReturn(user);
        when(mockUserRepository.save(any(User.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        String newUsername = "updatedUser";
        String newPassword = "updatedPass";
        String newPhone = "123aa";
        
        User updated = accountService.updateUser(VALID_EMAIL_USER, newUsername, newPassword, newPhone);

        assertNotNull(updated);
        assertEquals(newUsername, updated.getUsername());
        assertEquals(newPassword, updated.getPassword());
        assertEquals(VALID_PHONE_USER, updated.getPhoneNumber());
    }

    @Test
    public void testUpdateUserWithInvalidSpecialCharactersPhoneNumber() {
        User user = new User(VALID_EMAIL_USER, VALID_USERNAME_USER, VALID_PASSWORD_USER, VALID_PHONE_USER, new Cart());
        when(mockUserRepository.findByEmail(VALID_EMAIL_USER)).thenReturn(user);
        when(mockUserRepository.save(any(User.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        String newUsername = "updatedUser";
        String newPassword = "updatedPass";
        String newPhone = "1231231234@";

        User updated = accountService.updateUser(VALID_EMAIL_USER, newUsername, newPassword, newPhone);

        assertNotNull(updated);
        assertEquals(newUsername, updated.getUsername());
        assertEquals(newPassword, updated.getPassword());
        assertEquals(VALID_PHONE_USER, updated.getPhoneNumber());
    }
    @Test
    public void testUpdateUserWithEmptyPhoneNumber() {
        User user = new User(VALID_EMAIL_USER, VALID_USERNAME_USER, VALID_PASSWORD_USER, VALID_PHONE_USER, new Cart());
        when(mockUserRepository.findByEmail(VALID_EMAIL_USER)).thenReturn(user);
        when(mockUserRepository.save(any(User.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        String newUsername = "updatedUser";
        String newPassword = "updatedPass";
        String newPhone = "";
        
        User updated = accountService.updateUser(VALID_EMAIL_USER, newUsername, newPassword, newPhone);

        assertNotNull(updated);
        assertEquals(newUsername, updated.getUsername());
        assertEquals(newPassword, updated.getPassword());
        assertEquals(VALID_PHONE_USER, updated.getPhoneNumber());
    }

    @Test
    public void testUpdateUserWithEmptyPassword() {
        User user = new User(VALID_EMAIL_USER, VALID_USERNAME_USER, VALID_PASSWORD_USER, VALID_PHONE_USER, new Cart());
        when(mockUserRepository.findByEmail(VALID_EMAIL_USER)).thenReturn(user);
        when(mockUserRepository.save(any(User.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        String newUsername = "updatedUser";
        String newPassword = "";
        String newPhone = "3333333333";
        
        User updated = accountService.updateUser(VALID_EMAIL_USER, newUsername, newPassword, newPhone);

        assertNotNull(updated);
        assertEquals(newUsername, updated.getUsername());
        assertEquals(VALID_PASSWORD_USER, updated.getPassword());
        assertEquals(newPhone, updated.getPhoneNumber());
    }

    @Test
    public void testUpdateUserWithWeakPassword() {
        User user = new User(VALID_EMAIL_USER, VALID_USERNAME_USER, VALID_PASSWORD_USER, VALID_PHONE_USER, new Cart());
        when(mockUserRepository.findByEmail(VALID_EMAIL_USER)).thenReturn(user);
        
        String newUsername = "updatedUser";
        String newPassword = "weak";
        String newPhone = "3333333333";
        
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.updateUser(VALID_EMAIL_USER, newUsername, newPassword, newPhone));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Password must be at least 8 characters long", exception.getMessage());
    }

    @Test
    public void testUpdateUserWithSamePassword() {
        User user = new User(VALID_EMAIL_USER, VALID_USERNAME_USER, VALID_PASSWORD_USER, VALID_PHONE_USER, new Cart());
        when(mockUserRepository.findByEmail(VALID_EMAIL_USER)).thenReturn(user);
        
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.updateUser(VALID_EMAIL_USER, VALID_USERNAME_USER, VALID_PASSWORD_USER, VALID_PHONE_USER));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("New password cannot be the same as the old password", exception.getMessage());
    }

    @Test
    public void testUpdateUserWithNoUppercaseLetter() {
        User user = new User(VALID_EMAIL_USER, VALID_USERNAME_USER, VALID_PASSWORD_USER, VALID_PHONE_USER, new Cart());
        when(mockUserRepository.findByEmail(VALID_EMAIL_USER)).thenReturn(user);

        String lowercasePassword = "lowercasepassword";
        
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.updateUser(VALID_EMAIL_USER, VALID_USERNAME_USER, lowercasePassword, VALID_PHONE_USER));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Password must contain at least one uppercase letter", exception.getMessage());
    }

    @Test
    public void testUpdateUserWithNoLetter() {
        User user = new User(VALID_EMAIL_USER, VALID_USERNAME_USER, VALID_PASSWORD_USER, VALID_PHONE_USER, new Cart());
        when(mockUserRepository.findByEmail(VALID_EMAIL_USER)).thenReturn(user);

        String onlyNumbersPassword = "1234567890";
        
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.updateUser(VALID_EMAIL_USER, VALID_USERNAME_USER, onlyNumbersPassword, VALID_PHONE_USER));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Password must contain at least one letter", exception.getMessage());
    }

    @Test
    public void testDeleteUserSuccess() {
        User user = new User(VALID_EMAIL_USER, VALID_USERNAME_USER, VALID_PASSWORD_USER, VALID_PHONE_USER, new Cart());
        when(mockUserRepository.findByEmail(VALID_EMAIL_USER)).thenReturn(user);
        
        accountService.deleteUser(VALID_EMAIL_USER);
        verify(mockUserRepository, times(1)).delete(user);
    }

    @Test
    public void testDeleteUserNotFound() {
        when(mockUserRepository.findByEmail(VALID_EMAIL_USER)).thenReturn(null);
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> accountService.deleteUser(VALID_EMAIL_USER));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("User not found", exception.getMessage());
        verify(mockUserRepository, never()).delete(any(User.class));
    }

}

