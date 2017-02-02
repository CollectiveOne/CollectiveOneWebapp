package org.collectiveone.web.dto;

import java.util.List;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

public class ProjectNewDto {
	@NotEmpty
	@Pattern(regexp="^[0-9a-zA-z-]+$", message="only letters, numbers or bars '-'")
	private String name;
	@NotEmpty
	private String description;
	
	@NotEmpty
	@Pattern(regexp="^[0-9a-zA-z-]+$", message="only letters, numbers or bars '-'")
	private String goalTag;
	@NotEmpty
	private String goalDescription;
	
	private String creatorUsername;
	
	private List<UsernameAndPps> usernamesAndPps;
	
	public ProjectNewDto(){
	}
	
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
	public String getGoalTag() {
		return goalTag;
	}
	public void setGoalTag(String goalTag) {
		this.goalTag = goalTag;
	}
	public String getGoalDescription() {
		return goalDescription;
	}
	public void setGoalDescription(String goalDescription) {
		this.goalDescription = goalDescription;
	}
	public List<UsernameAndPps> getUsernamesAndPps() {
		return usernamesAndPps;
	}
	public void setUsernamesAndPps(List<UsernameAndPps> usernamesAndPps) {
		this.usernamesAndPps = usernamesAndPps;
	}
	
}
