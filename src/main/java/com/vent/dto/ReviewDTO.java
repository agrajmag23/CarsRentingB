package com.vent.dto;

public class ReviewDTO {
	private int rid;
	private int rating;
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + rating;
		result = prime * result + rid;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReviewDTO other = (ReviewDTO) obj;
		if (rating != other.rating)
			return false;
		if (rid != other.rid)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ReviewDTO [rid=" + rid + ", rating=" + rating + "]";
	}
	
	
}
