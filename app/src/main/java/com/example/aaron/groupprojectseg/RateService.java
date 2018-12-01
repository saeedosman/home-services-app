package com.example.aaron.groupprojectseg;

public class RateService {

	private String comment;
	private int rating;

	public RateService() {

	}

	public RateService(int rating, String comment){
		this.comment = comment; 

		if (rating>=1 && rating<=5){
			this.rating = rating;
		}
		else{
			this.rating = null; 
		}

	}

	public int getRating(){
		return rating;
	}

	public void setRating(int n){
		if (n>=1 && n<=5){
			this.rating = n;
		}
		else{
			this.rating = null; 
		}
	}

	public String getComment(){
		return comment;
	}


	public void setComment(String s){
		this.comment = s;
	}

} 
