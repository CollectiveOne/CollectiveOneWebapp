package org.collectiveone.web.dto;

import java.util.List;

public class GoalDtoListRes {
	private List<GoalDto> goalDtos;
	private int[] resSet = {0,0,0};
	
	public List<GoalDto> getGoalDtos() {
		return goalDtos;
	}
	public void setGoalDtos(List<GoalDto> goalDtos) {
		this.goalDtos = goalDtos;
	}
	public int[] getResSet() {
		return resSet;
	}
	public void setResSet(int[] resSet) {
		this.resSet = resSet;
	}
	
	
	
}
