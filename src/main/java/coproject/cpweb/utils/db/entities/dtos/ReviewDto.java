package coproject.cpweb.utils.db.entities.dtos;

public class ReviewDto {
	private int id;
	private String creatorUsername;
	private long creationDate;
	private String revieweeUsername;
	private String description;
	private double rate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
