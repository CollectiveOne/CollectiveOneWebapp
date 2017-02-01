package org.collectiveone.web.dto;

public class GoalDetachDto {
	
	private Long goalId;
	private double increaseBudget;
	
	public Long getGoalId() {
		return goalId;
	}
	public void setGoalId(Long goalId) {
		this.goalId = goalId;
	}
	public double getIncreaseBudget() {
		return increaseBudget;
	}
	public void setIncreaseBudget(double increaseBudget) {
		this.increaseBudget = increaseBudget;
	}
}