package org.collectiveone.web.dto;

public class ProjectNewDto {
	private String name;
	private String description;
	private String creatorUsername;
	private double ppsInitial;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
