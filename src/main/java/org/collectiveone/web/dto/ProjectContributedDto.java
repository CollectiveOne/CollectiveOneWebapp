package org.collectiveone.web.dto;

public class ProjectContributedDto {
	private String username;
	private String projectName;
	private double ppsTot;
	private double ppsContributed;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public double getPpsTot() {
		return ppsTot;
	}
	public void setPpsTot(double ppsTot) {
		this.ppsTot = ppsTot;
	}
	public double getPpsContributed() {
		return ppsContributed;
	}
	public void setPpsContributed(double ppsContributed) {
		this.ppsContributed = ppsContributed;
	}
	
}
