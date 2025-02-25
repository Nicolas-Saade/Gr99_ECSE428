package com.mcgill.ecse428.textbook_exchange.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.refEq;

import com.mcgill.ecse428.textbook_exchange.model.Account;
import com.mcgill.ecse428.textbook_exchange.model.Admin;
import com.mcgill.ecse428.textbook_exchange.model.Cart;
import com.mcgill.ecse428.textbook_exchange.model.Course;
import com.mcgill.ecse428.textbook_exchange.model.Faculty;
import com.mcgill.ecse428.textbook_exchange.model.Institution;
import com.mcgill.ecse428.textbook_exchange.model.Listing;
import com.mcgill.ecse428.textbook_exchange.model.User;

import jakarta.persistence.criteria.CriteriaBuilder.In;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.boot.test.context.SpringBootTest;




@SpringBootTest
public class ListingRepositoryTest {

    @Autowired
    private ListingRepository listingRepository;
    @Autowired
    private UserRepository userRepository;    
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private InstitutionRepository institutionRepository;


    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        listingRepository.deleteAll(); 
        userRepository.deleteAll();   
        cartRepository.deleteAll(); 
        courseRepository.deleteAll();
        facultyRepository.deleteAll();
        institutionRepository.deleteAll();
    }

    




    @Test
    public void testSaveListing() {
        // Create a user
        // Create and save the customer
        String aEmail = "test@example.com";
        String aUsername = "AnthonySaber";
        String aPassword = "password";
        String aPhoneNumber = "+1 5142472029";
    

        // Create and save a new cart
        Cart aCart = new Cart();
        aCart = cartRepository.save(aCart);

        // Create a new user
        User user = new User( aEmail,  aUsername,  aPassword,  aPhoneNumber,  aCart);
        user = userRepository.save(user);
        
        // Create and save a new institution
        String institutioName = "McGill University";
        Institution institution = new Institution(institutioName);
        institution = institutionRepository.save(institution);


        //Create and save a new faculty
        String facultyName = "ECSE";
        Faculty faculty = new Faculty(facultyName, institution);
        faculty = facultyRepository.save(faculty);

        // Create a new course
        String courseCode = "428";
        Course course = new Course(courseCode, faculty);
        course = courseRepository.save(course);
        
        
        
        // Create and save the listing
        String bookName = "Harry Potter";
        String ISBN = "978-3-16-148410-0";
        float price = 99;
        LocalDate date = LocalDate.of(2019,8 , 2);

        

        Listing listing = new Listing(bookName, ISBN, price, date, user, course);

        
        
        listing = listingRepository.save(listing);
        Listing listingRetreived = listingRepository.findByISBN(ISBN);
        


        assertNotNull(listingRetreived); 
        

        assertEquals(ISBN, listingRetreived.getISBN());
        assertEquals(bookName, listingRetreived.getBookName()); 
        assertEquals(price, listingRetreived.getPrice());
        assertEquals(date, listingRetreived.getDatePosted());
        assertEquals(user.getEmail(), listingRetreived.getUser().getEmail());
        assertEquals(course.getCourseId(), listingRetreived.getCourse().getCourseId());


    }

    @Test
    public void testUpdateListingPrice() {
     // Create a user
        // Create and save the customer
        String aEmail = "test@example.com";
        String aUsername = "AnthonySaber";
        String aPassword = "password";
        String aPhoneNumber = "+1 5142472029";
    

        // Create and save a new cart
        Cart aCart = new Cart();
        aCart = cartRepository.save(aCart);
        String cartId = aCart.getCartId();

        // Create a new user
        User user = new User( aEmail,  aUsername,  aPassword,  aPhoneNumber,  aCart);
        user = userRepository.save(user);
        
        // Create and save a new institution
        String institutioName = "McGill University";
        Institution institution = new Institution(institutioName);
        institution = institutionRepository.save(institution);


        //Create and save a new faculty
        String facultyName = "ECSE";
        Faculty faculty = new Faculty(facultyName, institution);
        faculty = facultyRepository.save(faculty);

        // Create a new course
        String courseCode = "428";
        Course course = new Course(courseCode, faculty);
        course = courseRepository.save(course);
        
        
        
        // Create and save the listing
        String bookName = "Harry Potter";
        String ISBN = "978-3-16-148410-0";
        float price = 99;
        LocalDate date = LocalDate.of(2019,8 , 2);


        Listing listing = new Listing(bookName, ISBN, price, date, user, course);
        
        float newPrice = 100;
        listing.setPrice(newPrice);
        listingRepository.save(listing);
    

        Listing listingRetreived = listingRepository.findByISBN(ISBN);
        assertNotNull(listingRetreived);    
        assertEquals(ISBN, listingRetreived.getISBN());
        assertEquals(bookName, listingRetreived.getBookName()); 
        assertEquals(newPrice, listingRetreived.getPrice());
        assertEquals(date, listingRetreived.getDatePosted());
        assertEquals(user.getEmail(), listingRetreived.getUser().getEmail());
        assertEquals(course.getCourseId(), listingRetreived.getCourse().getCourseId());




        
    }
    @Test
    public void testUpdateListingBookName() {
             // Create a user
        // Create and save the customer
        String aEmail = "test@example.com";
        String aUsername = "AnthonySaber";
        String aPassword = "password";
        String aPhoneNumber = "+1 5142472029";
    

        // Create and save a new cart
        Cart aCart = new Cart();
        aCart = cartRepository.save(aCart);
        String cartId = aCart.getCartId();

        // Create a new user
        User user = new User( aEmail,  aUsername,  aPassword,  aPhoneNumber,  aCart);
        user = userRepository.save(user);
        
        // Create and save a new institution
        String institutioName = "McGill University";
        Institution institution = new Institution(institutioName);
        institution = institutionRepository.save(institution);


        //Create and save a new faculty
        String facultyName = "ECSE";
        Faculty faculty = new Faculty(facultyName, institution);
        faculty = facultyRepository.save(faculty);

        // Create a new course
        String courseCode = "428";
        Course course = new Course(courseCode, faculty);
        course = courseRepository.save(course);
        
        
        
        // Create and save the listing
        String bookName = "Harry Potter";
        String ISBN = "978-3-16-148410-0";
        float price = 99;
        LocalDate date = LocalDate.of(2019,8 , 2);


        Listing listing = new Listing(bookName, ISBN, price, date, user, course);
        
        String newBookName = "Harry Potter and the Chamber of Secrets";
        listing.setBookName(newBookName);
        listingRepository.save(listing);
    

        Listing listingRetreived = listingRepository.findByISBN(ISBN);
        assertNotNull(listingRetreived);    
        assertEquals(ISBN, listingRetreived.getISBN());
        assertEquals(newBookName, listingRetreived.getBookName()); 
        assertEquals(price, listingRetreived.getPrice());
        assertEquals(date, listingRetreived.getDatePosted());
        assertEquals(user.getEmail(), listingRetreived.getUser().getEmail());
        assertEquals(course.getCourseId(), listingRetreived.getCourse().getCourseId());

        
    }

}