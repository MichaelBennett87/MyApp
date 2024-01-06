package com.kenzie.appserver.service;

import com.kenzie.capstone.service.client.ReviewLambdaServiceClient;
import com.kenzie.capstone.service.model.Review;
import com.kenzie.capstone.service.model.ReviewRequest;
import com.kenzie.capstone.service.model.ReviewResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing pet reviews.
 */
@Service
public class ReviewService {

    private final ReviewLambdaServiceClient reviewLambdaServiceClient;

    /**
     * Constructs a new ReviewService with the specified ReviewLambdaServiceClient.
     *
     * @param reviewLambdaServiceClient the ReviewLambdaServiceClient to use for review operations
     */
    @Autowired
    public ReviewService(ReviewLambdaServiceClient reviewLambdaServiceClient) {
        this.reviewLambdaServiceClient = reviewLambdaServiceClient;
    }

    /**
     * Submits a review for a pet.
     *
     * @param reviewRequest the ReviewRequest containing the review details
     * @return the ReviewResponse representing the submitted review
     */
    public ReviewResponse reviewPet(ReviewRequest reviewRequest) {
        return reviewLambdaServiceClient.addReview(reviewRequest);
    }

    /**
     * Retrieves all reviews for a specific pet.
     *
     * @param petId the ID of the pet to retrieve reviews for
     * @return a List of ReviewResponse representing the reviews for the specified pet
     * @throws IllegalArgumentException if the petId is null
     */
    public List<ReviewResponse> getPetReviews(String petId) {
        if (petId == null) {
            throw new IllegalArgumentException("Pet ID cannot be null");
        }

        return reviewLambdaServiceClient.getReviews(petId)
                .stream()
                .map(this::convertReviewToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Converts a Review object to a ReviewResponse object.
     *
     * @param review the Review to convert
     * @return the converted ReviewResponse object
     */
    private ReviewResponse convertReviewToResponse(Review review) {
        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setReviewId(review.getReviewId());
        reviewResponse.setPetId(review.getPetId());
        reviewResponse.setReviewDate(reviewResponse.getReviewDate());

        return reviewResponse;
    }
}
