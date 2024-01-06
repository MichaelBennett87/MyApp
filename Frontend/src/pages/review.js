import ReviewClient from "../api/reviewClient";
import { v4 as uuidv4 } from "uuid";

/**
 * Class for managing the rendering and submission of reviews.
 */
class ReviewManager {
    /**
     * Creates an instance of ReviewManager.
     */
    constructor() {
        this.reviewClient = new ReviewClient();
        this.reviewForm = document.getElementById("review-form");
        this.authorInput = document.getElementById("review-author");
        this.ratingInput = document.getElementById("review-rating");
        this.contentInput = document.getElementById("review-content");

        // Bind event handlers
        this.handleFormSubmit = this.handleFormSubmit.bind(this);
    }

    /**
     * Initializes the ReviewManager by attaching event listeners and rendering initial reviews.
     */
    init() {
        this.reviewForm.addEventListener("submit", this.handleFormSubmit);
        this.renderReviews();
    }

    /**
     * Renders the reviews by fetching them from the ReviewClient and creating review cards.
     */
    async renderReviews() {
        try {
            const petId = localStorage.getItem("petId");
            const reviews = await this.reviewClient.getReview(petId);
            const reviewList = document.getElementById("review-list");

            // Clear existing reviews
            reviewList.innerHTML = "";

            // Loop through each review and create review cards
            reviews.forEach((review) => {
                const reviewCard = document.createElement("div");
                reviewCard.classList.add("review-card");

                const reviewHeader = document.createElement("div");
                reviewHeader.classList.add("review-header");

                const reviewerInfo = document.createElement("div");
                reviewerInfo.classList.add("reviewer-info");

                const author = document.createElement("h4");
                author.classList.add("review-author");
                author.textContent = review.author;

                const date = document.createElement("span");
                date.classList.add("review-date");
                date.textContent = review.date;

                const rating = document.createElement("div");
                rating.classList.add("review-rating");

                const ratingStars = Math.floor(review.rating);
                const ratingHalfStar = review.rating % 1 !== 0;

                for (let i = 0; i < ratingStars; i++) {
                    const starIcon = document.createElement("i");
                    starIcon.classList.add("fas", "fa-star");
                    rating.appendChild(starIcon);
                }

                if (ratingHalfStar) {
                    const halfStarIcon = document.createElement("i");
                    halfStarIcon.classList.add("fas", "fa-star-half-alt");
                    rating.appendChild(halfStarIcon);
                }

                reviewHeader.appendChild(reviewerInfo);
                reviewerInfo.appendChild(author);
                reviewerInfo.appendChild(date);
                reviewHeader.appendChild(rating);

                const reviewContent = document.createElement("div");
                reviewContent.classList.add("review-content");
                reviewContent.textContent = review.content;

                reviewCard.appendChild(reviewHeader);
                reviewCard.appendChild(reviewContent);

                reviewList.appendChild(reviewCard);
            });
        } catch (error) {
            console.error("Error fetching reviews:", error);
        }
    }

    /**
     * Handles the form submission by validating inputs, creating a new review object, adding the review using the ReviewClient, and refreshing the reviews.
     * @param {Event} event - The form submit event.
     */
    async handleFormSubmit(event) {
        event.preventDefault();

        const author = this.authorInput.value;
        const rating = parseFloat(this.ratingInput.value);
        const content = this.contentInput.value;

        // Validate inputs
        if (!author || !rating || !content) {
            alert("Please fill in all fields.");
            return;
        }

        const newReview = {
            reviewId: uuidv4(), // Generate a UUID for the review
            author,
            date: this.getCurrentDate(),
            rating,
            content,
        };

        const petId = localStorage.getItem("petId");

        // Get existing reviews from local storage or initialize an empty array
        const existingReviews = JSON.parse(localStorage.getItem(`reviews_${petId}`)) || [];

        // Add the new review to the existing reviews array
        existingReviews.push(newReview);

        // Store the updated reviews array in local storage
        localStorage.setItem(`reviews_${petId}`, JSON.stringify(existingReviews));
        console.log("Reviews updated in local storage.");

        // Store the reviewId in local storage
        localStorage.setItem("reviewId", newReview.reviewId);
        console.log("New reviewId:", newReview.reviewId);

        try {
            // Add the new review using the ReviewClient (if needed)
            const response = await this.reviewClient.addReview(newReview);
            console.log("Review added:", response);

            // Clear form inputs
            this.authorInput.value = "";
            this.ratingInput.value = "";
            this.contentInput.value = "";

            // Refresh the reviews
            await this.renderReviews();
        } catch (error) {
            console.error("Error adding review:", error);
        }
    }



    /**
     * Retrieves the current date in the format: Month DD, YYYY.
     * @returns {string} The current date string.
     */
    getCurrentDate() {
        const currentDate = new Date();
        const options = { year: "numeric", month: "long", day: "numeric" };
        return currentDate.toLocaleDateString(undefined, options);
    }
}

// Create an instance of ReviewManager
const reviewManager = new ReviewManager();

// Initialize the ReviewManager
reviewManager.init();
