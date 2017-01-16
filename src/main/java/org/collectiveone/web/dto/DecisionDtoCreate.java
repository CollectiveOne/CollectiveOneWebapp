package org.collectiveone.web.dto;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

public class DecisionDtoCreate {
	
	private Long id;
	@NotEmpty
	private String description;
	private long creationDate;
	private String projectName;
	private String creatorUsername;
	@NotEmpty
	private String goalTag;
	@Min(36)
	protected double verdictHours;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getCreatorUsername() {
		return creatorUsername;
	}
	public void setCreatorUsername(String creatorUsername) {
		this.creatorUsername = creatorUsername;
	}
	public String getGoalTag() {
		return goalTag;
	}
	public void setGoalTag(String goalTag) {
		this.goalTag = goalTag;
	}
	public double getVerdictHours() {
		return verdictHours;
	}
	public void setVerdictHours(double verdictHours) {
		this.verdictHours = verdictHours;
	}
	
}

