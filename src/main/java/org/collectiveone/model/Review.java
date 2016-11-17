package org.collectiveone.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.web.dto.ReviewDto;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "REVIEWS")
public class Review {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private Long id;
	@ManyToOne
	private User creator;
	private Timestamp creationDate;
	@ManyToOne
	private User reviewee;
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String description;
	private double rate;
	
	public ReviewDto toDto() {
		ReviewDto dto = new ReviewDto();
		
		dto.setId(id);
		if(creator != null) dto.setCreatorUsername(creator.getUsername());
		if(creationDate != null) dto.setCreationDate(creationDate.getTime());
		if(reviewee != null) dto.setRevieweeUsername(reviewee.getUsername());
		if(description != null) dto.setDescription(description);
		dto.setRate(rate);
		
		return dto;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getCreator() {
		return creator;
	}
	public void setCreator(User creator) {
		this.creator = creator;
	}
	public Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	public User getReviewee() {
		return reviewee;
	}
	public void setReviewee(User reviewee) {
		this.reviewee = reviewee;
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
