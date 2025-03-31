package com.mcgill.ecse428.textbook_exchange.service;

import com.mcgill.ecse428.textbook_exchange.model.Review;
import com.mcgill.ecse428.textbook_exchange.model.User;
import com.mcgill.ecse428.textbook_exchange.repository.ReviewRepository;
import com.mcgill.ecse428.textbook_exchange.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Transactional(noRollbackFor = IllegalArgumentException.class)
    public Review createReview(String reviewerEmail, String sellerEmail, int rating, String message) {
        // Validate inputs
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Invalid rating. Please provide a rating between 1 and 5.");
        }
        
        // Get reviewer and seller
        User reviewer = userRepository.findByEmail(reviewerEmail);
        User seller = userRepository.findByEmail(sellerEmail);
        
        if (reviewer == null || seller == null) {
            throw new IllegalArgumentException("Seller not found");
        }
        
        // Prevent self-reviews
        if (reviewerEmail.equals(sellerEmail)) {
            throw new IllegalArgumentException("You cannot review yourself");
        }
        
        // Check for existing review
        List<Review> existingReviews = new ArrayList<>();
        for (Review review : reviewRepository.findAll()) {
            if (review.getUser().getEmail().equals(reviewerEmail)) {
                existingReviews.add(review);
            }
        }
        
        if (!existingReviews.isEmpty()) {
            throw new IllegalArgumentException("You have already reviewed this seller");
        }
        
        // Create and save the review
        Review review = new Review(rating, message, reviewer);
        return reviewRepository.save(review);
    }
    
    @Transactional(noRollbackFor = IllegalArgumentException.class)
    public Review updateReview(String reviewerEmail, String sellerEmail, int newRating, String newMessage) {
        // Validate inputs
        if (newRating < 1 || newRating > 5) {
            throw new IllegalArgumentException("Invalid rating. Please provide a rating between 1 and 5.");
        }
        
        // Get reviewer and seller
        User reviewer = userRepository.findByEmail(reviewerEmail);
        User seller = userRepository.findByEmail(sellerEmail);
        
        if (reviewer == null || seller == null) {
            throw new IllegalArgumentException("Seller not found");
        }
        
        // Find existing review
        List<Review> existingReviews = new ArrayList<>();
        for (Review review : reviewRepository.findAll()) {
            if (review.getUser().getEmail().equals(reviewerEmail)) {
                existingReviews.add(review);
            }
        }
        
        if (existingReviews.isEmpty()) {
            throw new IllegalArgumentException("You have not reviewed this seller");
        }
        
        Review review = existingReviews.get(0);
        review.setRating(newRating);
        review.setMessage(newMessage);
        
        return reviewRepository.save(review);
    }
    
    @Transactional(noRollbackFor = IllegalArgumentException.class)
    public void deleteReview(String reviewId) {
        reviewRepository.deleteById(reviewId);
    }
    
    public List<Review> getReviewsByUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        
        List<Review> userReviews = new ArrayList<>();
        for (Review review : reviewRepository.findAll()) {
            if (review.getUser().getEmail().equals(userEmail)) {
                userReviews.add(review);
            }
        }
        
        return userReviews;
    }
} 