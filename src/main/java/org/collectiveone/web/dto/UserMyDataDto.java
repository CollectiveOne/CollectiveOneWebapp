package org.collectiveone.web.dto;

import java.util.List;

public class UserMyDataDto {
	
	private List<ProjectDto> projectsStarred;
	private List<ProjectDto> projectsWatched;
	
	public List<ProjectDto> getProjectsStarred() {
		return projectsStarred;
	}
	public void setProjectsStarred(List<ProjectDto> projectsStarred) {
		this.projectsStarred = projectsStarred;
	}
	public List<ProjectDto> getProjectsWatched() {
		return projectsWatched;
	}
	public void setProjectsWatched(List<ProjectDto> projectsWatched) {
		this.projectsWatched = projectsWatched;
	}
	
}
