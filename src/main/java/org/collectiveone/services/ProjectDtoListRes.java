package org.collectiveone.services;

import java.util.List;

import org.collectiveone.web.dto.ProjectDto;

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
