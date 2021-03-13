package com.comp231.easypark.feedback;

public class Feedback {
    private String name;
    private String ratingBar;
    private String review;
    private String psmId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRatingBar() {
        return ratingBar;
    }

    public void setRatingBar(String ratingBar) {
        this.ratingBar = ratingBar;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getPsmId() { return psmId; }

    public void setPsmId(String psmId) { this.psmId = psmId; }
}
