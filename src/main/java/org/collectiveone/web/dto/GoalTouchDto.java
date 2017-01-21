package org.collectiveone.web.dto;

public class GoalTouchDto {
	
	private String username;
	private boolean touchFlag;
	private String projectName;
	private String goalTag;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public boolean getTouchFlag() {
		return touchFlag;
	}
	public void setTouchFlag(boolean touchFlag) {
		this.touchFlag = touchFlag;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getGoalTag() {
		return goalTag;
	}
	public void setGoalTag(String goalTag) {
		this.goalTag = goalTag;
	}
}