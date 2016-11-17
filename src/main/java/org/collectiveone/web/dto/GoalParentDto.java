package org.collectiveone.web.dto;

public class GoalParentDto {
	private Long goalId;
	private String parentTag;
	
	public Long getGoalId() {
		return goalId;
	}
	public void setGoalId(Long goalId) {
		this.goalId = goalId;
	}
	public String getParentTag() {
		return parentTag;
	}
	public void setParentTag(String parentTag) {
		this.parentTag = parentTag;
	}
	
}
