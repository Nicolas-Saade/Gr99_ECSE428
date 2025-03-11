package com.mcgill.ecse428.textbook_exchange.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.time.DateTimeException;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.mcgill.ecse428.textbook_exchange.exception.TextBookExchangeException;
import com.mcgill.ecse428.textbook_exchange.model.Cart;
import com.mcgill.ecse428.textbook_exchange.model.Course;
import com.mcgill.ecse428.textbook_exchange.model.Faculty;
import com.mcgill.ecse428.textbook_exchange.model.Institution;
import com.mcgill.ecse428.textbook_exchange.model.Listing;
import com.mcgill.ecse428.textbook_exchange.model.Listing.BookCondition;
import com.mcgill.ecse428.textbook_exchange.model.User;
import com.mcgill.ecse428.textbook_exchange.repository.CartRepository;
import com.mcgill.ecse428.textbook_exchange.repository.CourseRepository;
import com.mcgill.ecse428.textbook_exchange.repository.ListingRepository;
import com.mcgill.ecse428.textbook_exchange.repository.UserRepository;

@SpringBootTest
@MockitoSettings(strictness = Strictness.LENIENT)
public class ListingServiceTests {

    @Mock
    private ListingRepository mockListingRepository;
    
    @Mock
    private UserRepository mockUserRepository;
    
    @Mock
    private CourseRepository mockCourseRepository;

    @Mock
    private CartRepository mockCartRepository; 

    @InjectMocks
    private ListingService listingService;

    private static final String VALID_USERNAME = "myusername";
    private static final String VALID_EMAIL = "test@example.com";
    private static final String VALID_BOOK_NAME = "addedBook";
    private static final String VALID_ISBN = "011-0-1-0987654-2";
    private static final float VALID_PRICE = 25.17f;
    private static final BookCondition VALID_BOOK_CONDITION = BookCondition.New;
    private static final LocalDate VALID_DATE = LocalDate.of(2025, 2, 15);
    private static final String VALID_COURSE_CODE = "ECSE 428";

    private static final String INVALID_ISBN_TOO_LONG = "011-0-1-0987654-251";
    private static final float INVALID_PRICE_NEG = -10.0f;
    private static final String INVALID_COURSE_CODE = null;

    // We'll keep this but won't use it for the invalid date tests
    private static final LocalDate INVALID_DATE_FUTURE = LocalDate.of(2030, 1, 1);

    private Cart mockCart;
    private User mockUser;
    private Course mockCourse;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockCart = new Cart();
        mockUser = new User(VALID_EMAIL, VALID_USERNAME, "password", "+1 5142472029", mockCart);
        mockCourse = new Course(VALID_COURSE_CODE, new Faculty("ECSE", new Institution("McGill University")));
        when(mockUserRepository.findByEmail(VALID_EMAIL)).thenReturn(mockUser);
        when(mockCourseRepository.findByCourseId(anyString())).thenAnswer(inv -> {
            String code = inv.getArgument(0);
            if (code == null) {
                return null;
            }
            return new Course(code, new Faculty("SomeFaculty", new Institution("SomeUniversity")));
        });
        when(mockListingRepository.save(any(Listing.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    // Feature #14: User Creates a Listing
    // Scenario Outline: User wants to create a listing (Normal Flow)
    @Test
    public void testCreateListing_Valid() {
        Listing createdListing = listingService.createListing(
            VALID_BOOK_NAME,
            VALID_ISBN,
            VALID_PRICE,
            VALID_DATE,
            VALID_EMAIL,
            VALID_COURSE_CODE,
            VALID_BOOK_CONDITION
        );
        assertNotNull(createdListing);
        assertEquals(VALID_BOOK_NAME, createdListing.getBookName());
        assertEquals(VALID_ISBN, createdListing.getISBN());
        assertEquals(VALID_PRICE, createdListing.getPrice());
        assertEquals(VALID_DATE, createdListing.getDatePosted());
        assertEquals(VALID_BOOK_CONDITION, createdListing.getBookcondition());
        assertEquals(mockUser, createdListing.getUser());
        assertEquals("ECSE 428", createdListing.getCourse().getCourseId());
        assertNotNull(mockUser.getCart());
        verify(mockListingRepository, times(1)).save(any(Listing.class));
    }

    // Feature #14: User Creates a Listing
    // Scenario Outline (example 1): bookName = "addedBook1", ISBN = "011-0-1-0987654-2", bookCondition = New, price = 25.17, datePosted = 2025-02-15
    @Test
    public void testCreateListing_Valid_Book1() {
        Listing createdListing = listingService.createListing(
            "addedBook1",                       
            "011-0-1-0987654-2",                
            25.17f,                             
            LocalDate.of(2025, 2, 15),          
            VALID_EMAIL,
            "MATH 109",                       
            BookCondition.New                  
        );
        assertNotNull(createdListing);
        assertEquals("addedBook1", createdListing.getBookName());
        assertEquals("011-0-1-0987654-2", createdListing.getISBN());
        assertEquals(25.17f, createdListing.getPrice());
        assertEquals(LocalDate.of(2025, 2, 15), createdListing.getDatePosted());
        assertEquals(BookCondition.New, createdListing.getBookcondition());
        assertEquals(mockUser, createdListing.getUser());
        assertEquals("MATH 109", createdListing.getCourse().getCourseId());
        assertNotNull(mockUser.getCart());
        verify(mockListingRepository, times(1)).save(any(Listing.class));
    }

    // Feature #14: User Creates a Listing
    // Scenario Outline (example 2): bookName = "addedBook2", ISBN = "012-6-3-9375829-8", bookCondition = Used, price = 10.99, datePosted = 2025-02-20
    @Test
    public void testCreateListing_Valid_Book2() {
        Listing createdListing = listingService.createListing(
            "addedBook2",                       
            "012-6-3-9375829-8",                
            10.99f,                              
            LocalDate.of(2025, 2, 20),          
            VALID_EMAIL,
            "ANTH 119",                      
            BookCondition.Used                 
        );
        assertNotNull(createdListing);
        assertEquals("addedBook2", createdListing.getBookName());
        assertEquals("012-6-3-9375829-8", createdListing.getISBN());
        assertEquals(10.99f, createdListing.getPrice());
        assertEquals(LocalDate.of(2025, 2, 20), createdListing.getDatePosted());
        assertEquals(BookCondition.Used, createdListing.getBookcondition());
        assertEquals(mockUser, createdListing.getUser());
        assertEquals("ANTH 119", createdListing.getCourse().getCourseId());
        assertNotNull(mockUser.getCart());
        verify(mockListingRepository, times(1)).save(any(Listing.class));
    }

    // Feature #14: User Creates a Listing
    // Just kept as an extra example (unchanged scenario data)
    @Test
    public void testCreateListing_Valid_Book3() {
        Listing createdListing = listingService.createListing(
            "addedBook",
            "921-2-8-0987235-1",
            45.17f,
            LocalDate.of(2025, 2, 15),  
            VALID_EMAIL,
            "ANAT 279",
            BookCondition.New
        );
        assertNotNull(createdListing);
        assertEquals("addedBook", createdListing.getBookName());
        assertEquals("921-2-8-0987235-1", createdListing.getISBN());
        assertEquals(45.17f, createdListing.getPrice());
        assertEquals(LocalDate.of(2025, 2, 15), createdListing.getDatePosted());
        assertEquals(BookCondition.New, createdListing.getBookcondition());
        assertEquals(mockUser, createdListing.getUser());
        assertEquals("ANAT 279", createdListing.getCourse().getCourseId());
        assertNotNull(mockUser.getCart());
        verify(mockListingRepository, times(1)).save(any(Listing.class));
    }

    // Feature #14: User Creates a Listing
    // Scenario Outline: User wants to create a listing with an invalid ISBN (Error Flow)
    @Test
    public void testCreateListing_InvalidISBNTooLong() {
        TextBookExchangeException ex = assertThrows(TextBookExchangeException.class, () -> {
            listingService.createListing(
                VALID_BOOK_NAME,
                INVALID_ISBN_TOO_LONG,
                25.17f,
                VALID_DATE,
                VALID_EMAIL,
                VALID_COURSE_CODE,
                BookCondition.New
            );
        });
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertTrue(ex.getMessage().contains("Invalid ISBN Format"));
        verify(mockListingRepository, never()).save(any(Listing.class));
    }

    // Feature #14: User Creates a Listing
    // Scenario Outline: User attempts to create a listing with an invalid price (Error Flow)
    @Test
    public void testCreateListing_InvalidPriceNegative() {
        TextBookExchangeException ex = assertThrows(TextBookExchangeException.class, () -> {
            listingService.createListing(
                "cheapBook",
                "010-0-1-1234567-9",
                INVALID_PRICE_NEG,
                VALID_DATE,
                VALID_EMAIL,
                VALID_COURSE_CODE,
                BookCondition.New
            );
        });
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertTrue(ex.getMessage().contains("Invalid Price"));
        verify(mockListingRepository, never()).save(any(Listing.class));
    }

    // Feature #14: User Creates a Listing
    // Scenario Outline: User attempts to create a listing with an invalid price (Error Flow) - second example (0.00)
    @Test
    public void testCreateListing_InvalidPriceZero() {
        TextBookExchangeException ex = assertThrows(TextBookExchangeException.class, () -> {
            listingService.createListing(
                "freeBook",
                "012-6-3-9375829-8",
                0.0f,
                VALID_DATE,
                VALID_EMAIL,
                VALID_COURSE_CODE,
                BookCondition.Used
            );
        });
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertTrue(ex.getMessage().contains("Invalid Price"));
        verify(mockListingRepository, never()).save(any(Listing.class));
    }

    // Feature #14: User Creates a Listing
    // Scenario Outline: User wants to create a listing with an invalid ISBN (Error Flow)
    @Test
    public void testCreateListing_InvalidISBN_TableInvalid1() {
        TextBookExchangeException ex = assertThrows(TextBookExchangeException.class, () -> {
            listingService.createListing(
                "invalid1",
                "011-0-1-0987654-251",
                25.17f,
                VALID_DATE,
                VALID_EMAIL,
                "MATH 109",
                BookCondition.New
            );
        });
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertTrue(ex.getMessage().contains("Invalid ISBN Format"));
        verify(mockListingRepository, never()).save(any(Listing.class));
    }

    // Feature #14: User Creates a Listing
    // Scenario Outline: User wants to create a listing with an invalid ISBN (Error Flow)
    @Test
    public void testCreateListing_InvalidISBN_TableInvalid2() {
        TextBookExchangeException ex = assertThrows(TextBookExchangeException.class, () -> {
            listingService.createListing(
                "invalid2",
                "",
                2.17f,
                VALID_DATE,
                VALID_EMAIL,
                "ANTH 119",
                BookCondition.New
            );
        });
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertTrue(ex.getMessage().contains("Invalid ISBN Format"));
        verify(mockListingRepository, never()).save(any(Listing.class));
    }

    // Feature #14: User Creates a Listing
    // Scenario Outline: User wants to create a listing with an invalid ISBN (Error Flow)
    @Test
    public void testCreateListing_InvalidISBN_TableInvalid3() {
        TextBookExchangeException ex = assertThrows(TextBookExchangeException.class, () -> {
            listingService.createListing(
                "invalid3",
                "invalidISBN",
                45.17f,
                VALID_DATE,
                VALID_EMAIL,
                "ANAT 279",
                BookCondition.New
            );
        });
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertTrue(ex.getMessage().contains("Invalid ISBN Format"));
        verify(mockListingRepository, never()).save(any(Listing.class));
    }

    // Feature #14: User Creates a Listing
    // Scenario Outline: User creates a listing with an invalid date (Error Flow) - "book1" example 
    @Test
    public void testCreateListing_InvalidDate_TableInvalid1() {
        DateTimeException ex = assertThrows(DateTimeException.class, () -> {
            listingService.createListing(
                "book1",
                "011-0-1-0987654-2",
                25.17f,
                LocalDate.of(2025, 13, 32),
                VALID_EMAIL,
                "MATH 109",
                BookCondition.New
            );
        });
        verify(mockListingRepository, never()).save(any(Listing.class));
    }

    // Feature #14: User Creates a Listing
    // Scenario Outline: User creates a listing with an invalid date (Error Flow) - "book3" example
    @Test
    public void testCreateListing_InvalidDate_TableInvalid3() {
        DateTimeException ex = assertThrows(DateTimeException.class, () -> {
            listingService.createListing(
                "book3",
                "921-2-8-0987235-1",
                45.17f,
                LocalDate.of(0, 0, 0),
                VALID_EMAIL,
                "ANAT 279",
                BookCondition.New
            );
        });
        // We don't get a TextBookExchangeException, but a DateTimeException from LocalDate
        verify(mockListingRepository, never()).save(any(Listing.class));
    }

    // Feature #14: User Creates a Listing
    // Scenario Outline: User creates a listing with an invalid course (Error Flow)
    @Test
    public void testCreateListing_InvalidCourseCode() {
        when(mockUserRepository.findByEmail(VALID_EMAIL)).thenReturn(mockUser);
        when(mockCourseRepository.findByCourseId(INVALID_COURSE_CODE)).thenReturn(null);
        TextBookExchangeException ex = assertThrows(TextBookExchangeException.class, () -> {
            listingService.createListing(
                VALID_BOOK_NAME,
                VALID_ISBN,
                VALID_PRICE,
                VALID_DATE,
                VALID_EMAIL,
                INVALID_COURSE_CODE,
                BookCondition.Used
            );
        });
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertTrue(ex.getMessage().contains("Invalid Course Code"));
        verify(mockListingRepository, never()).save(any(Listing.class));
    }

    // Feature #14: User Creates a Listing
    // Scenario: User not found (Error Flow)
    @Test
    public void testCreateListing_UserNotFound() {
        when(mockUserRepository.findByEmail(VALID_EMAIL)).thenReturn(null);
        TextBookExchangeException ex = assertThrows(TextBookExchangeException.class, () -> {
            listingService.createListing(
                VALID_BOOK_NAME,
                VALID_ISBN,
                VALID_PRICE,
                VALID_DATE,
                VALID_EMAIL,
                VALID_COURSE_CODE,
                BookCondition.New
            );
        });
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertTrue(ex.getMessage().contains("User not found."));
        verify(mockListingRepository, never()).save(any(Listing.class));
    }
}
