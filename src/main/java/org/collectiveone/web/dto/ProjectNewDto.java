package org.collectiveone.web.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

public class ProjectNewDto {
	@NotEmpty
	@Pattern(regexp="^((?!\\s).)*$", message="may not contain white spaces")
	private String name;
	@NotEmpty
	private String description;
	private String creatorUsername;
	
	@Min(10)
	private double ppsInitial;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name.trim();
	}
	public String getCreatorUsername() {
		return creatorUsername;
	}
	public void setCreatorUsername(String creatorUsername) {
		this.creatorUsername = creatorUsername;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPpsInitial() {
		return ppsInitial;
	}
	public void setPpsInitial(double ppsInitial) {
		this.ppsInitial = ppsInitial;
	}
}
