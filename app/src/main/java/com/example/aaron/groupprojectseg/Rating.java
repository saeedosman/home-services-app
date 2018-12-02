package com.example.aaron.groupprojectseg;

public class Rating {
	private ServiceProvider serviceProvider;
	private String username;
    private float rating;
    private String comment;

	public Rating() {

	}

    public Rating(ServiceProvider serviceProvider, String username, float rating, String comment) {
        this.serviceProvider = serviceProvider;
        this.username = username;
        this.rating = rating;
        this.comment = comment;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
