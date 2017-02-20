package org.collectiveone.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.collectiveone.model.Cbtion;
import org.collectiveone.model.Contributor;
import org.collectiveone.model.Project;
import org.collectiveone.model.User;
import org.collectiveone.repositories.CbtionRepository;
import org.collectiveone.repositories.ProjectDao;
import org.collectiveone.repositories.UserDao;
import org.collectiveone.web.dto.ProjectContributedDto;
import org.collectiveone.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class UserServiceImp {
	
	@Autowired
	protected UserDao userDao;
	
	@Autowired
	protected CbtionRepository cbtionDao;
	
	@Autowired
	protected ProjectDao projectDao;
	
	@Transactional
	public void userSave(User user) {
		userDao.save(user);
	}

	@Transactional
	public User userGet(Long id) {
		return userDao.get(id);
	}

	@Transactional
	public Long userGetN() {
		return userDao.getN();
	}

	@Transactional
	public User userGet(String username) {
		return userDao.get(username);
	}

	@Transactional
	public UserDto userGetDto(String username) {
		return userDao.get(username).toDto();
	}

	@Transactional
	public List<User> userGetAll(Integer max) {
		return userDao.getAll(max);
	}
	
	@Transactional
	public void userUpdateProfile(UserDto userDto) {
		User user = userDao.get(userDto.getUsername());
		user.setProfile(userDto.getProfile());
		userDao.save(user);
	}

	@Transactional
	public double userPpointsInProjectRecalc(Long userId, Long projectId) {

		double ppoints = 0;
		List<Cbtion> cbtionsAccepted = cbtionDao.getAcceptedOfUserInProject(userId, projectId);

		for(Cbtion cbtion : cbtionsAccepted) {	ppoints += cbtion.getAssignedPpoints();	}

		return ppoints;
	}

	@Transactional
	public ProjectContributedDto  userProjectPpsGet(String username, String projectName) {
		Project project = projectDao.get(projectName);
		User user = userDao.get(username);

		ProjectContributedDto projectAndPps = new ProjectContributedDto();

		projectAndPps.setProjectName(project.getName());
		projectAndPps.setUsername(user.getUsername());
		projectAndPps.setPpsTot(project.getPpsTot());
		
		Contributor ctrb = projectDao.getContributor(project.getId(), user.getId());
		
		double ppsInProject = 0.0;
		if(ctrb != null) { ppsInProject = ctrb.getPps(); }
		projectAndPps.setPpsContributed(ppsInProject);
		
		return projectAndPps;
	}

	@Transactional
	public List<ProjectContributedDto> userProjectsContributedAndPpsGet(String username) {
		/* returns a summary of the project name, total pps, and pps contributed by username 
		 * from all the projects to which username has contributed */
		
		List<ProjectContributedDto> projectsAndPps = new ArrayList<ProjectContributedDto>();
		User user = userDao.get(username);

		for(Project project : userProjectsContributedGet(user.getId())) {
			ProjectContributedDto projectAndPps = new ProjectContributedDto();

			projectAndPps.setProjectName(project.getName());
			projectAndPps.setUsername(user.getUsername());
			projectAndPps.setPpsTot(project.getPpsTot());
			
			Contributor ctrb = projectDao.getContributor(project.getId(), user.getId());
			
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
	public List<Project> userProjectsContributedGet(Long userId) {
		return userDao.getProjectsContributed(userId);
	}

	@Transactional
	public List<String> usernameGetSuggestions(String query) {
		return userDao.getSuggestions(query);
	}
	
	@Transactional
	public List<String> usernameGetSuggestionsReferrer(String query) {
		return userDao.getSuggestionsReferrer(query);
	}
	
}
