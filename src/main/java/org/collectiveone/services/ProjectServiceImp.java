package org.collectiveone.services;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.collectiveone.model.AuthorizedProject;
import org.collectiveone.model.Bid;
import org.collectiveone.model.BidState;
import org.collectiveone.model.Cbtion;
import org.collectiveone.model.CbtionState;
import org.collectiveone.model.Contributor;
import org.collectiveone.model.Decision;
import org.collectiveone.model.DecisionRealm;
import org.collectiveone.model.DecisionState;
import org.collectiveone.model.DecisionType;
import org.collectiveone.model.Goal;
import org.collectiveone.model.GoalState;
import org.collectiveone.model.Project;
import org.collectiveone.model.User;
import org.collectiveone.web.dto.Filters;
import org.collectiveone.web.dto.GoalDto;
import org.collectiveone.web.dto.ObjectListRes;
import org.collectiveone.web.dto.ProjectDto;
import org.collectiveone.web.dto.ProjectDtoListRes;
import org.collectiveone.web.dto.ProjectNewDto;
import org.collectiveone.web.dto.UsernameAndData;
import org.collectiveone.web.dto.UsernameAndPps;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectServiceImp extends BaseService {

	@Transactional
	public void save(Project project) {
		projectRepository.save(project);
	}

	@Transactional
	public void authorize(String projectName) throws IOException {
		AuthorizedProject authProject = new AuthorizedProject();
		
		authProject.setProjectName(projectName);
		authProject.setAuthorized(true);
		
		authorizedProjectDao.save(authProject);
	}
	
	@Transactional
	public boolean isAuthorized(String projectName) {
		AuthorizedProject projectAuthorized = authorizedProjectDao.get(projectName);
		if(projectAuthorized != null) {
			return true;
		} else {
			return false;
		}
    }
	
	@Transactional
	public void create(ProjectNewDto projectDto) throws IOException {
		if(get(projectDto.getName()) == null) {
			Project project = new Project();
			projectRepository.save(project);

			User creator = userRepository.get(projectDto.getCreatorUsername());

			project.setName(projectDto.getName());
			project.setCreator(creator);
			project.setCreationDate(new Timestamp(System.currentTimeMillis()));
			project.setDescription(projectDto.getDescription());
			project.setPpsTot(0.0);
		}
	}
	
	@Transactional
	public void createFirstGoal(ProjectNewDto projectDto) throws IOException {
		/* One goal must be created at project creation */
		GoalDto goalDto = new GoalDto();
		
		goalDto.setCreationDate(new Timestamp(System.currentTimeMillis()));
		goalDto.setCreatorUsername(projectDto.getCreatorUsername());
		goalDto.setDescription(projectDto.getGoalDescription());
		goalDto.setProjectName(projectDto.getName());
		goalDto.setGoalTag(projectDto.getGoalTag());
		
		goalService.create(goalDto,GoalState.ACCEPTED);
	}

	@Transactional
	public void start(String projectName, List<UsernameAndPps> usernamesAndPps) {

		User coprojects = userRepository.get("collectiveone");
		userRepository.save(coprojects);

		Project project = projectRepository.get(projectName);
		project.setEnabled(true);
		projectRepository.save(project);
		
		/* only one goal exist as the project was just created 
		 * get one goal to use as decision realm of manual decisions
		 * created here */
		List<Goal> goals = goalRepository.getAllOfProject(project.getId());
		Goal goal = goals.get(0);
		
		User creator = project.getCreator();
		userRepository.save(creator);

		DecisionRealm realm = decisionRealmRepository.getFromGoalId(goal.getId());
		decisionRealmRepository.save(realm);

		/* An accepted cbtion is added to the project for each contributor */
		for(UsernameAndPps usernameAndPps : usernamesAndPps) {
			User ctrb = userRepository.get(usernameAndPps.getUsername());
			
			Cbtion cbtion = new Cbtion();

			cbtion.setCreator(creator);
			cbtion.setCreationDate(new Timestamp(System.currentTimeMillis()));
			cbtion.setTitle("Create project " + project.getName());
			cbtion.setDescription("Start contribution");
			cbtion.setProject(project);

			cbtionRepository.save(cbtion);

			/* Bids and decisions are created for consistency */
			Bid bid = new Bid();
			bidRepository.save(bid);

			bid.setCbtion(cbtion);
			cbtion.getBids().add(bid);

			bid.setCreator(ctrb);
			bid.setCreationDate(new Timestamp(System.currentTimeMillis()));
			bid.setPpoints(usernameAndPps.getPps());
			bid.setDescription("Create project "+ project.getName());
			bid.setState(BidState.OFFERED);

			Decision assign_bid = new Decision();
			decisionRepository.save(assign_bid);

			Decision accept_bid = new Decision();
			decisionRepository.save(accept_bid);

			bid.setAssign(assign_bid);
			bid.setAccept(accept_bid);

			assign_bid.setCreator(coprojects);
			assign_bid.setCreationDate(new Timestamp(System.currentTimeMillis()));
			assign_bid.setDescription("assign bid to cbtion:"+bid.getCbtion().getTitle()+" by:"+bid.getCreator().getUsername());
			assign_bid.setVerdict(1);
			assign_bid.setState(DecisionState.CLOSED_ACCEPTED);
			assign_bid.setDecisionRealm(realm);
			assign_bid.setFromState(BidState.OFFERED.toString());
			assign_bid.setToState(BidState.ASSIGNED.toString());
			assign_bid.setProject(project);
			assign_bid.setGoal(goal);
			assign_bid.setType(DecisionType.BID);
			assign_bid.setAffectedBid(bid);

			accept_bid.setCreator(coprojects);
			accept_bid.setCreationDate(new Timestamp(System.currentTimeMillis()));
			accept_bid.setDescription("accept bid to cbtion:"+bid.getCbtion().getTitle()+" by:"+bid.getCreator().getUsername());
			accept_bid.setVerdict(1);
			accept_bid.setState(DecisionState.CLOSED_ACCEPTED);
			accept_bid.setDecisionRealm(realm);
			accept_bid.setFromState(BidState.ASSIGNED.toString());
			accept_bid.setToState(BidState.ACCEPTED.toString());
			accept_bid.setProject(project);
			accept_bid.setGoal(goal);
			accept_bid.setType(DecisionType.BID);
			accept_bid.setAffectedBid(bid);

			/* simulate the bid acceptance process */
			bid.setState(BidState.ACCEPTED);

			project.setPpsTot(project.getPpsTot() + bid.getPpoints());

			cbtion.setAssignedPpoints(bid.getPpoints());
			cbtion.setContributor(bid.getCreator());
			cbtion.setState(CbtionState.ACCEPTED);

			/* add user to project contributors */
			contributorDao.updateContributor(project.getId(), ctrb.getId(), bid.getPpoints());
		}
	}

	@Transactional
	public Project get(Long id) {
		return projectRepository.get(id);
	}

	@Transactional
	public Project get(String project_name) {
		return projectRepository.get(project_name);
	}

	@Transactional
	public ProjectDto getDto(String project_name) {
		Project project = projectRepository.get(project_name);
		ProjectDto dto = project.toDto();
		return dto;
	}

	@Transactional
	public List<String> getList() {
		return projectRepository.getListEnabled();
	}

	@Transactional
	public List<Project> getAll(Integer max) {
		return projectRepository.getFromRef(new Project(), max);
	}

	@Transactional
	public List<UsernameAndData> getContributorsAndData(Long projectId) {

		List<UsernameAndData> usernamesAndData = new ArrayList<UsernameAndData>();

		for(Contributor contributor : getContributors(projectId)) {
			UsernameAndData usernameAndData = new UsernameAndData();
			
			usernameAndData.setUsername(contributor.getContributorUser().getUsername());
			usernameAndData.setPps(contributor.getPps());
			
			Long cbtId = contributor.getContributorUser().getId();
			usernameAndData.setnCbtionsCreated(cbtionRepository.getNCreatedByUserInProject(projectId,cbtId));
			usernameAndData.setnCbtionsDone(cbtionRepository.getNAcceptedOfUserInProject(projectId,cbtId));
			
			Long monthsToMs = 2678400000L;
			usernameAndData.setnCbtionsDoneRecently(
					cbtionRepository.getNAcceptedOfUserInProjectRecently(projectId,cbtId, new Timestamp(System.currentTimeMillis() - 2*monthsToMs)));
			
			usernamesAndData.add(usernameAndData);
		}

		Collections.sort(usernamesAndData, new Comparator<UsernameAndData>(){
			public int compare(UsernameAndData o1, UsernameAndData o2){
				return Double.compare(o2.getPps(), o1.getPps());
			}
		});

		return usernamesAndData;
	}

	@Transactional
	public void updatePpsTot(Long projectId, double lastOne) {
		Project project = projectRepository.get(projectId);

		double ppsTot = 0.0;
		for(Contributor ctrb : project.getContributors()) {
			ppsTot += ctrb.getPps();
		}

		project.setPpsTot(ppsTot+lastOne);
		projectRepository.save(project);
	}

	@Transactional
	public Set<Contributor> getContributors(Long projectId) {
		return projectRepository.getContributors(projectId);
	}
	
	@Transactional
	public ProjectDtoListRes getFilteredDto(Filters filters) {
		ObjectListRes<Project> projectsRes = projectRepository.get(filters);

		ProjectDtoListRes projectsDtosRes = new ProjectDtoListRes();

		projectsDtosRes.setResSet(projectsRes.getResSet());
		projectsDtosRes.setProjectDtos(new ArrayList<ProjectDto>());

		for(Project project : projectsRes.getObjects()) {
			projectsDtosRes.getProjectDtos().add(project.toDto());
		}

		return projectsDtosRes;
	}	

}
