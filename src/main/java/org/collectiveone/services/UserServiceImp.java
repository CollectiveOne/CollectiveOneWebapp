package org.collectiveone.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.collectiveone.model.Cbtion;
import org.collectiveone.model.Contributor;
import org.collectiveone.model.Project;
import org.collectiveone.model.User;
import org.collectiveone.web.dto.ProjectContributedDto;
import org.collectiveone.web.dto.ProjectDto;
import org.collectiveone.web.dto.UserDto;
import org.collectiveone.web.dto.UserMyDataDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImp extends BaseService {
	
	@Transactional
	public void save(User user) {
		userRepository.save(user);
	}

	@Transactional
	public User get(Long id) {
		return userRepository.get(id);
	}

	@Transactional
	public Long getN() {
		return userRepository.getN();
	}

	@Transactional
	public User get(String username) {
		return userRepository.get(username);
	}

	@Transactional
	public UserDto getDto(String username) {
		return userRepository.get(username).toDto();
	}

	@Transactional
	public List<User> getAll(Integer max) {
		return userRepository.getAll(max);
	}
	
	@Transactional
	public void updateProfile(UserDto userDto) {
		User user = userRepository.get(userDto.getUsername());
		user.setProfile(userDto.getProfile());
		userRepository.save(user);
	}

	@Transactional
	public double ppointsInProjectRecalc(Long userId, Long projectId) {

		double ppoints = 0;
		List<Cbtion> cbtionsAccepted = cbtionRepository.getAcceptedOfUserInProject(userId, projectId);

		for(Cbtion cbtion : cbtionsAccepted) {	ppoints += cbtion.getAssignedPpoints();	}

		return ppoints;
	}

	@Transactional
	public ProjectContributedDto  projectPpsGet(String username, String projectName) {
		Project project = projectRepository.get(projectName);
		User user = userRepository.get(username);

		ProjectContributedDto projectAndPps = new ProjectContributedDto();

		projectAndPps.setProjectName(project.getName());
		projectAndPps.setUsername(user.getUsername());
		projectAndPps.setPpsTot(project.getPpsTot());
		
		Contributor ctrb = projectRepository.getContributor(project.getId(), user.getId());
		
		double ppsInProject = 0.0;
		if(ctrb != null) { ppsInProject = ctrb.getPps(); }
		projectAndPps.setPpsContributed(ppsInProject);
		
		return projectAndPps;
	}

	@Transactional
	public List<ProjectContributedDto> projectsContributedAndPpsGet(String username) {
		/* returns a summary of the project name, total pps, and pps contributed by username 
		 * from all the projects to which username has contributed */
		
		List<ProjectContributedDto> projectsAndPps = new ArrayList<ProjectContributedDto>();
		User user = userRepository.get(username);

		for(Project project : projectsContributedGet(user.getId())) {
			ProjectContributedDto projectAndPps = new ProjectContributedDto();

			projectAndPps.setProjectName(project.getName());
			projectAndPps.setUsername(user.getUsername());
			projectAndPps.setPpsTot(project.getPpsTot());
			
			Contributor ctrb = projectRepository.getContributor(project.getId(), user.getId());
			
			double ppsInPrj = 0.0;
			if(ctrb != null) { ppsInPrj = ctrb.getPps(); }
			projectAndPps.setPpsContributed(ppsInPrj);

			projectsAndPps.add(projectAndPps);
		}

		/* sort based on the projects in which the user has relative larger contributions */
		Collections.sort(projectsAndPps, new Comparator<ProjectContributedDto>(){
			public int compare(ProjectContributedDto o1, ProjectContributedDto o2){
				return Double.compare(o2.getPpsContributed()/o2.getPpsTot(), o1.getPpsContributed()/o1.getPpsTot());
			}
		});

		return projectsAndPps;
	}

	@Transactional
	public List<Project> projectsContributedGet(Long userId) {
		return userRepository.getProjectsContributed(userId);
	}

	@Transactional
	public List<String> getSuggestions(String query) {
		return userRepository.getSuggestions(query);
	}
	
	@Transactional
	public List<String> getSuggestionsReferrer(String query) {
		return userRepository.getSuggestionsReferrer(query);
	}
	
	@Transactional
	public boolean isProjectStarred(Long projectId, Long userId) {
		return userRepository.isProjectStarred(projectId,userId);
	}
	
	@Transactional
	public boolean isProjectWatched(Long projectId, Long userId) {
		return userRepository.isProjectWatched(projectId,userId);
	}
	
	@Transactional
	public UserMyDataDto getMyData(String username) {
		User user = userRepository.get(username);
		
		UserMyDataDto myDataDto = new UserMyDataDto();
		
		myDataDto.setProjectsStarred(new ArrayList<ProjectDto>());
		myDataDto.setProjectsWatched(new ArrayList<ProjectDto>());
		
		for(Project project : user.getProjectsStarred()) {
			ProjectDto projectDto = project.toDto();
			projectDto.setIsStarred(isProjectStarred(projectDto.getId(),user.getId()));
			projectDto.setIsWatched(isProjectWatched(projectDto.getId(),user.getId()));
			
			myDataDto.getProjectsStarred().add(projectDto);
		}
		
		for(Project project : user.getProjectsWatched()) {
			ProjectDto projectDto = project.toDto();
			projectDto.setIsStarred(isProjectStarred(projectDto.getId(),user.getId()));
			projectDto.setIsWatched(isProjectWatched(projectDto.getId(),user.getId()));
			
			myDataDto.getProjectsWatched().add(projectDto);
		}
		
		return myDataDto;
	}
	
	@Transactional
	public List<String> getProjectsStarredNames(Long userId) {
		
		User user = userRepository.get(userId);
				
		List<String> projectsStarredNames = new ArrayList<String>();
		
		for(Project project : user.getProjectsStarred()) {
			projectsStarredNames.add(project.getName());
		}
		
		return projectsStarredNames;
	}
	
	
}
