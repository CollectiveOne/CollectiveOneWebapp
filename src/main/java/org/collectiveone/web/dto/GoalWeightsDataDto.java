package org.collectiveone.web.dto;

import java.util.List;

public class GoalWeightsDataDto {
	
	private Long goalId;
	private String projectName;
	private String goalTag;
	private double totalWeight;
	private GoalUserWeightsDto userWeightsDto;
	private List<VoterDto> votersDtos;
	
	public Long getGoalId() {
		return goalId;
	}
	public void setGoalId(Long goalId) {
		this.goalId = goalId;
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
	public double getTotalWeight() {
		return totalWeight;
	}
	public void setTotalWeight(double totalWeight) {
		this.totalWeight = totalWeight;
	}
	public GoalUserWeightsDto getUserWeightsDto() {
		return userWeightsDto;
	}
	public void setUserWeightsDto(GoalUserWeightsDto userWeightsDto) {
		this.userWeightsDto = userWeightsDto;
	}
	public List<VoterDto> getVotersDtos() {
		return votersDtos;
	}
	public void setVotersDtos(List<VoterDto> votersDtos) {
		this.votersDtos = votersDtos;
	}
	
}