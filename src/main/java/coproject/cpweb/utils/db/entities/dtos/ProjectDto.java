package coproject.cpweb.utils.db.entities.dtos;

import java.sql.Timestamp;

import coproject.cpweb.utils.db.entities.Project;
import coproject.cpweb.utils.db.entities.User;

public class ProjectDto {
	
	private int id;
	private String name;
	private String description;
	private Timestamp creationDate;
	private String creatorUsername;
	private int nCbtionsAccepted;
	private double ppsTot;
	private int nContributors;
	
	public Project toProject(User creator) {
		Project project = new Project();
		
		project.setName(name);
		project.setDescription(description);
		project.setCreator(creator);
		
		return project;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	public int getnCbtionsAccepted() {
		return nCbtionsAccepted;
	}
	public void setnCbtionsAccepted(int nCbtionsAccepted) {
		this.nCbtionsAccepted = nCbtionsAccepted;
	}
	public double getPpsTot() {
		return ppsTot;
	}
	public void setPpsTot(double ppsTot) {
		this.ppsTot = ppsTot;
	}
	public int getnContributors() {
		return nContributors;
	}
	public void setnContributors(int nContributors) {
		this.nContributors = nContributors;
	}
	
	
		
}
