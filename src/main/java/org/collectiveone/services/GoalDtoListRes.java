package org.collectiveone.services;

import java.util.List;

import org.collectiveone.web.dto.GoalDto;

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
