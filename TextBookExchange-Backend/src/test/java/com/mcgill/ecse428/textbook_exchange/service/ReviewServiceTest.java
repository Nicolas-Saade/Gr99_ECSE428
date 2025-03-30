package com.mcgill.ecse428.textbook_exchange.service;

import com.mcgill.ecse428.textbook_exchange.model.Review;
import com.mcgill.ecse428.textbook_exchange.model.User;
import com.mcgill.ecse428.textbook_exchange.model.Cart;
import com.mcgill.ecse428.textbook_exchange.repository.ReviewRepository;
import com.mcgill.ecse428.textbook_exchange.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.mockito.quality.Strictness;

@SpringBootTest
@MockitoSettings(strictness = Strictness.LENIENT)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReviewService reviewService;

    private User reviewer;
    private User seller;
    private String reviewerEmail;
    private String sellerEmail;

    @BeforeEach
    void setUp() {
        reviewerEmail = "reviewer@test.com";
        sellerEmail = "seller@test.com";
        
        reviewer = new User(reviewerEmail, "reviewer", "password", "1234567890", new Cart());
        seller = new User(sellerEmail, "seller", "password", "1234567890", new Cart());
    }

    @Test
    void createReview_Success() {
        // Arrange
        when(userRepository.findByEmail(reviewerEmail)).thenReturn(reviewer);
        when(userRepository.findByEmail(sellerEmail)).thenReturn(seller);
        when(reviewRepository.findAll()).thenReturn(new ArrayList<>());
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Review review = reviewService.createReview(reviewerEmail, sellerEmail, 5, "Great service!");

        // Assert
        assertNotNull(review);
        assertEquals(5, review.getRating());
        assertEquals("Great service!", review.getMessage());
        assertEquals(reviewer, review.getUser());
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    void createReview_InvalidRating() {
        // Arrange
        when(userRepository.findByEmail(reviewerEmail)).thenReturn(reviewer);
        when(userRepository.findByEmail(sellerEmail)).thenReturn(seller);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> reviewService.createReview(reviewerEmail, sellerEmail, 6, "Great service!"));
        assertEquals("Invalid rating. Please provide a rating between 1 and 5.", exception.getMessage());
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void createReview_SellerNotFound() {
        // Arrange
        when(userRepository.findByEmail(reviewerEmail)).thenReturn(reviewer);
        when(userRepository.findByEmail(sellerEmail)).thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> reviewService.createReview(reviewerEmail, sellerEmail, 5, "Great service!"));
        assertEquals("Seller not found", exception.getMessage());
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void createReview_SelfReview() {
        // Arrange
        when(userRepository.findByEmail(reviewerEmail)).thenReturn(reviewer);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> reviewService.createReview(reviewerEmail, reviewerEmail, 5, "Great service!"));
        assertEquals("You cannot review yourself", exception.getMessage());
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void createReview_DuplicateReview() {
        // Arrange
        when(userRepository.findByEmail(reviewerEmail)).thenReturn(reviewer);
        when(userRepository.findByEmail(sellerEmail)).thenReturn(seller);
        List<Review> existingReviews = new ArrayList<>();
        existingReviews.add(new Review(5, "Existing review", reviewer));
        when(reviewRepository.findAll()).thenReturn(existingReviews);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> reviewService.createReview(reviewerEmail, sellerEmail, 5, "New review"));
        assertEquals("You have already reviewed this seller", exception.getMessage());
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void updateReview_Success() {
        // Arrange
        Review existingReview = new Review(5, "Old review", reviewer);
        List<Review> existingReviews = new ArrayList<>();
        existingReviews.add(existingReview);

        when(userRepository.findByEmail(reviewerEmail)).thenReturn(reviewer);
        when(userRepository.findByEmail(sellerEmail)).thenReturn(seller);
        when(reviewRepository.findAll()).thenReturn(existingReviews);
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Review updatedReview = reviewService.updateReview(reviewerEmail, sellerEmail, 4, "Updated review");

        // Assert
        assertNotNull(updatedReview);
        assertEquals(4, updatedReview.getRating());
        assertEquals("Updated review", updatedReview.getMessage());
        assertEquals(reviewer, updatedReview.getUser());
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    void updateReview_NoExistingReview() {
        // Arrange
        when(userRepository.findByEmail(reviewerEmail)).thenReturn(reviewer);
        when(userRepository.findByEmail(sellerEmail)).thenReturn(seller);
        when(reviewRepository.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> reviewService.updateReview(reviewerEmail, sellerEmail, 4, "Updated review"));
        assertEquals("You have not reviewed this seller", exception.getMessage());
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void deleteReview_Success() {
        // Arrange
        String reviewId = "123";

        // Act
        reviewService.deleteReview(reviewId);

        // Assert
        verify(reviewRepository).deleteById(reviewId);
    }

    @Test
    void getReviewsByUser_Success() {
        // Arrange
        List<Review> expectedReviews = new ArrayList<>();
        expectedReviews.add(new Review(5, "Review 1", reviewer));
        expectedReviews.add(new Review(4, "Review 2", reviewer));

        when(userRepository.findByEmail(reviewerEmail)).thenReturn(reviewer);
        when(reviewRepository.findAll()).thenReturn(expectedReviews);

        // Act
        List<Review> actualReviews = reviewService.getReviewsByUser(reviewerEmail);

        // Assert
        assertNotNull(actualReviews);
        assertEquals(2, actualReviews.size());
        assertTrue(actualReviews.stream().allMatch(review -> review.getUser().equals(reviewer)));
    }

    @Test
    void getReviewsByUser_UserNotFound() {
        // Arrange
        when(userRepository.findByEmail(reviewerEmail)).thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> reviewService.getReviewsByUser(reviewerEmail));
        assertEquals("User not found", exception.getMessage());
        verify(reviewRepository, never()).findAll();
    }
} 