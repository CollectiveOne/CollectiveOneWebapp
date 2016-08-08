package coproject.cpweb.utils.db.entities.dtos;

import java.util.ArrayList;
import java.util.List;

public class UserDto {
	
	private int id;
	private String username;
	private List<String> projectsFollowing = new ArrayList<String>();
	private List<String> projectsContributed = new ArrayList<String>();
	private int nCbtionsCreated;
	private int nCbtionsAccepted;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<String> getProjectsFollowing() {
		return projectsFollowing;
	}
	public void setProjectsFollowing(List<String> projectsFollowing) {
		this.projectsFollowing = projectsFollowing;
	}
	public List<String> getProjectsContributed() {
		return projectsContributed;
	}
	public void setProjectsContributed(List<String> projectsContributed) {
		this.projectsContributed = projectsContributed;
	}
	public int getnCbtionsCreated() {
		return nCbtionsCreated;
	}
	public void setnCbtionsCreated(int nCbtionsCreated) {
		this.nCbtionsCreated = nCbtionsCreated;
	}
	public int getnCbtionsAccepted() {
		return nCbtionsAccepted;
	}
	public void setnCbtionsAccepted(int nCbtionsAccepted) {
		this.nCbtionsAccepted = nCbtionsAccepted;
	}

}
