package com.example.aaron.groupprojectseg;

public class RateService {

	private String comment;
	private int rating;

	public RateService() {

	}

	public RateService(int rating, String comment){
		this.comment = comment;
		this.rating = rating;

	}

	public int getRating(){
		return rating;
	}

	public void setRating(int n){
		this.rating = n;
	}

	public String getComment(){
		return comment;
	}


	public void setComment(String s){
		this.comment = s;
	}

} 
