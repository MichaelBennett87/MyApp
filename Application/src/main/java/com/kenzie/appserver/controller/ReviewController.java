package com.kenzie.appserver.controller;


import com.kenzie.appserver.service.PetService;
import com.kenzie.appserver.service.ReviewService;
import com.kenzie.capstone.service.model.ReviewRequest;
import com.kenzie.capstone.service.model.ReviewResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/pets")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }
    @PostMapping("/review/add")
    public ResponseEntity<ReviewResponse> addReview(@RequestBody ReviewRequest reviewRequest) {
        if (reviewRequest.getReviewId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Review Id cannot be Null.....");
        }
        ReviewResponse reviewResponse = reviewService.reviewPet(reviewRequest);
        return ResponseEntity.ok(reviewResponse);
    }



    @GetMapping("/review/get/{id}")
    public ResponseEntity<List<ReviewResponse>>getReview(@PathVariable("id") String id){
        if (id == null){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "The id cannot be null");
        }
        List<ReviewResponse> reviewResponseList = reviewService.getPetReviews(id);
        return ResponseEntity.ok(reviewResponseList);
    }

}

