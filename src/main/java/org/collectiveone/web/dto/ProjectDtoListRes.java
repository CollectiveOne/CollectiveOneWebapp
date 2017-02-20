package org.collectiveone.web.dto;

import java.util.List;

public class ProjectDtoListRes {
	private List<ProjectDto> projectDtos;
	private int[] resSet = {0,0,0};
	
	public List<ProjectDto> getProjectDtos() {
		return projectDtos;
	}
	public void setProjectDtos(List<ProjectDto> projectDtos) {
		this.projectDtos = projectDtos;
	}
	public int[] getResSet() {
		return resSet;
	}
	public void setResSet(int[] resSet) {
		this.resSet = resSet;
	}
}
