package org.collectiveone.web.dto;

public class ReviewDto {
	private Long id;
	private Long bidId;
	private String creatorUsername;
	private long creationDate;
	private String revieweeUsername;
	private String description;
	private double rate;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getBidId() {
		return bidId;
	}
	public void setBidId(Long bidId) {
		this.bidId = bidId;
	}
	public String getCreatorUsername() {
		return creatorUsername;
	}
	public void setCreatorUsername(String creatorUsername) {
		this.creatorUsername = creatorUsername;
	}
	public long getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}
	public String getRevieweeUsername() {
		return revieweeUsername;
	}
	public void setRevieweeUsername(String revieweeUsername) {
		this.revieweeUsername = revieweeUsername;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	
	
}
