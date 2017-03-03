package org.collectiveone.web.dto;

import java.sql.Timestamp;

import org.collectiveone.model.Project;
import org.collectiveone.model.User;

public class ProjectDto {
	
	private Long id;
	private String name;
	private String description;
	private Timestamp creationDate;
	private String creatorUsername;
	private double ppsTot;
	
	/* for user logged*/
	private boolean isStarred;
	private boolean isWatched;
	
	public Project toProject(User creator) {
		Project project = new Project();
		
		project.setName(name);
		project.setDescription(description);
		project.setCreator(creator);
		
		return project;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	public String getCreatorUsername() {
		return creatorUsername;
	}
	public void setCreatorUsername(String creatorUsername) {
		this.creatorUsername = creatorUsername;
	}
	public double getPpsTot() {
		return ppsTot;
	}
	public void setPpsTot(double ppsTot) {
		this.ppsTot = ppsTot;
	}
	public boolean getIsStarred() {
		return isStarred;
	}
	public void setIsStarred(boolean isStarred) {
		this.isStarred = isStarred;
	}
	public boolean getIsWatched() {
		return isWatched;
	}
	public void setIsWatched(boolean isWatched) {
		this.isWatched = isWatched;
	}
		
}
