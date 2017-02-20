package org.collectiveone.web.dto;

import java.util.List;

public class ActivityDtoListRes {
	private List<ActivityDto> activityDtos;
	private int[] resSet = {0,0,0};
	
	public List<ActivityDto> getActivityDtos() {
		return activityDtos;
	}
	public void setActivityDtos(List<ActivityDto> activityDtos) {
		this.activityDtos = activityDtos;
	}
	public int[] getResSet() {
		return resSet;
	}
	public void setResSet(int[] resSet) {
		this.resSet = resSet;
	}
	
	
	
}
