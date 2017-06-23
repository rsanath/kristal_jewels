package com.talenttakeaways.kristaljewels.beans;

/**
 * Created by sanath on 14/06/17.
 */

public class Review {

    private String reviewId, reviewAuthorName, reviewMessage, reviewAuthorId, reviewRating;

    public Review(String reviewId, String reviewAuthorName, String reviewRating,
                  String reviewMessage, String reviewAuthorId) {
        this.reviewId = reviewId;
        this.reviewAuthorName = reviewAuthorName;
        this.reviewRating = reviewRating;
        this.reviewMessage = reviewMessage;
        this.reviewAuthorId = reviewAuthorId;
    }

    public Review() {
    }

    public String getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(String reviewRating) {
        this.reviewRating = reviewRating;
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getReviewAuthorId() {
        return reviewAuthorId;
    }

    public void setReviewAuthorId(String reviewAuthorId) {
        this.reviewAuthorId = reviewAuthorId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewAuthorName() {
        return reviewAuthorName;
    }

    public void setReviewAuthorName(String reviewAuthorName) {
        this.reviewAuthorName = reviewAuthorName;
    }

    public String getReviewMessage() {
        return reviewMessage;
    }

    public void setReviewMessage(String reviewMessage) {
        this.reviewMessage = reviewMessage;
    }
}
