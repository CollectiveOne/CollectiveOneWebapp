package org.collectiveone.services;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.collectiveone.model.Activity;
import org.collectiveone.model.ActivityType;
import org.collectiveone.model.Argument;
import org.collectiveone.model.ArgumentTendency;
import org.collectiveone.model.AuthorizedProject;
import org.collectiveone.model.Bid;
import org.collectiveone.model.BidDoneState;
import org.collectiveone.model.BidState;
import org.collectiveone.model.Cbtion;
import org.collectiveone.model.CbtionState;
import org.collectiveone.model.Comment;
import org.collectiveone.model.Contributor;
import org.collectiveone.model.Decision;
import org.collectiveone.model.DecisionRealm;
import org.collectiveone.model.DecisionState;
import org.collectiveone.model.DecisionType;
import org.collectiveone.model.Goal;
import org.collectiveone.model.GoalParentState;
import org.collectiveone.model.GoalState;
import org.collectiveone.model.Project;
import org.collectiveone.model.Promoter;
import org.collectiveone.model.Review;
import org.collectiveone.model.Thesis;
import org.collectiveone.model.User;
import org.collectiveone.model.Voter;
import org.collectiveone.repositories.ActivityDao;
import org.collectiveone.repositories.ArgumentDao;
import org.collectiveone.repositories.AuthorizedProjectDao;
import org.collectiveone.repositories.BidDao;
import org.collectiveone.repositories.CbtionRepository;
import org.collectiveone.repositories.CommentDao;
import org.collectiveone.repositories.ContributorDao;
import org.collectiveone.repositories.DecisionDao;
import org.collectiveone.repositories.DecisionRealmDao;
import org.collectiveone.repositories.GoalDao;
import org.collectiveone.repositories.MailSubscriptionRepository;
import org.collectiveone.repositories.ProjectDao;
import org.collectiveone.repositories.PromoterDao;
import org.collectiveone.repositories.ReviewDao;
import org.collectiveone.repositories.ThesisDao;
import org.collectiveone.repositories.UserDao;
import org.collectiveone.repositories.VoterDao;
import org.collectiveone.web.controllers.rest.ResStatus;
import org.collectiveone.web.dto.ActivityDto;
import org.collectiveone.web.dto.ArgumentDto;
import org.collectiveone.web.dto.BidDto;
import org.collectiveone.web.dto.BidNewDto;
import org.collectiveone.web.dto.CbtionDto;
import org.collectiveone.web.dto.CommentDto;
import org.collectiveone.web.dto.DecisionDtoCreate;
import org.collectiveone.web.dto.DecisionDtoFull;
import org.collectiveone.web.dto.DoneDto;
import org.collectiveone.web.dto.GoalDto;
import org.collectiveone.web.dto.GoalTouchDto;
import org.collectiveone.web.dto.GoalUserWeightsDto;
import org.collectiveone.web.dto.GoalWeightsDataDto;
import org.collectiveone.web.dto.ProjectContributedDto;
import org.collectiveone.web.dto.ProjectDto;
import org.collectiveone.web.dto.ProjectNewDto;
import org.collectiveone.web.dto.ReviewDto;
import org.collectiveone.web.dto.ThesisDto;
import org.collectiveone.web.dto.UserDto;
import org.collectiveone.web.dto.UsernameAndPps;
import org.collectiveone.web.dto.VoterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Component(value="dbServices")
public class DbServicesImp {

	/* global variable to store the status */
	private ResStatus status = new ResStatus();
	
	@Autowired
	protected CbtionRepository cbtionDao;

	@Autowired
	protected GoalDao goalDao;

	@Autowired
	protected ProjectDao projectDao;

	@Autowired
	protected UserDao userDao;

	@Autowired
	protected BidDao bidDao;

	@Autowired
	protected DecisionDao decisionDao;

	@Autowired
	protected ThesisDao thesisDao;

	@Autowired 
	protected VoterDao voterDao;

	@Autowired 
	protected DecisionRealmDao decisionRealmDao;
	
	@Autowired 
	protected ContributorDao contributorDao;

	@Autowired 
	protected ArgumentDao argumentDao;

	@Autowired 
	protected ReviewDao reviewDao;

	@Autowired 
	protected PromoterDao promoterDao;

	@Autowired
	protected ActivityDao activityDao;

	@Autowired
	protected CommentDao commentDao;
	
	@Autowired
	protected AuthorizedProjectDao authorizedProjectDao;
	
	@Autowired
	protected MailSubscriptionRepository mailSubscriptionRepository;
	
	@Autowired
    private AppMailServiceHeroku mailService;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private TimeServiceIf timeService;
	
	public TimeServiceIf getTimeService() {
		return timeService;
	}
	public void setTimeService(TimeServiceIf timeService) {
		this.timeService = timeService;
	}
	
	public ResStatus getStatus() {
		return status;
	}
	public void setStatus(ResStatus status) {
		this.status = status;
	}
	
	
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
	public void contributorUpdate(Long projectId, Long userId, double lastOne) {
		/* update the contributor pps in project recalculating all accepted
		 * contributions and adding a delta (for cases in which pps are bing assigned
		 * during the transaction)  */
		double ppsPrev = userPpointsInProjectRecalc(userId, projectId);
		double ppsTot = ppsPrev + lastOne;
		
		Contributor ctrb = contributorDao.getContributor(projectId, userId);
		if(ctrb == null) {
			/* new contributor in the project */
			decisionRealmAddVoterToAll(projectId,userId,ppsTot);
		}
		
		contributorDao.updateContributor(projectId, userId, ppsTot);
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
		if(ctrb != null) { ctrb.getPps(); }
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
	public void projectSave(Project project) {
		projectDao.save(project);
	}

	@Transactional
	public void projectAuthorize(String projectName) throws IOException {
		AuthorizedProject authProject = new AuthorizedProject();
		
		authProject.setProjectName(projectName);
		authProject.setAuthorized(true);
		
		authorizedProjectDao.save(authProject);
	}
	
	@Transactional
	public boolean isProjectAuthorized(String projectName) {
		AuthorizedProject projectAuthorized = authorizedProjectDao.get(projectName);
		if(projectAuthorized != null) {
			return true;
		} else {
			return false;
		}
    }
	
	@Transactional
	public void projectCreate(ProjectNewDto projectDto) throws IOException {
		if(projectGet(projectDto.getName()) == null) {
			Project project = new Project();
			projectDao.save(project);

			User creator = userDao.get(projectDto.getCreatorUsername());

			project.setName(projectDto.getName());
			project.setCreator(creator);
			project.setCreationDate(new Timestamp(System.currentTimeMillis()));
			project.setDescription(projectDto.getDescription());
		}
	}
	
	@Transactional
	public void projectCreateFirstGoal(ProjectNewDto projectDto) throws IOException {
		/* One goal must be created at project creation */
		GoalDto goalDto = new GoalDto();
		
		goalDto.setCreationDate(new Timestamp(System.currentTimeMillis()));
		goalDto.setCreatorUsername(projectDto.getCreatorUsername());
		goalDto.setDescription(projectDto.getGoalDescription());
		goalDto.setProjectName(projectDto.getName());
		goalDto.setGoalTag(projectDto.getGoalTag());
		
		goalCreate(goalDto,GoalState.ACCEPTED);
	}

	@Transactional
	public void projectStart(String projectName, double ppsInit) {

		User coprojects = userDao.get("collectiveone");
		userDao.save(coprojects);

		Project project = projectDao.get(projectName);
		project.setEnabled(true);
		projectDao.save(project);
		
		/* only one goal exist as the project was just created 
		 * get one goal to use as decision realm of manual decisions
		 * created here */
		List<Goal> goals = goalDao.getAllOfProject(project.getId());
		Goal goal = goals.get(0);
		
		User creator = project.getCreator();
		userDao.save(creator);

		DecisionRealm realm = decisionRealmDao.getFromGoalId(goal.getId());
		decisionRealmDao.save(realm);

		/* An accepted cbtion is added to the project to the contributor */
		Cbtion cbtion = new Cbtion();

		cbtion.setCreator(creator);

		cbtion.setCreationDate(new Timestamp(System.currentTimeMillis()));
		cbtion.setTitle("Create project " + project.getName());
		cbtion.setDescription("Start contribution");
		cbtion.setProject(project);

		cbtionDao.save(cbtion);

		/* Bids and decisions are created for consistency */
		Bid bid = new Bid();
		bidDao.save(bid);

		bid.setCbtion(cbtion);
		cbtion.getBids().add(bid);

		bid.setCreator(creator);
		bid.setCreationDate(new Timestamp(System.currentTimeMillis()));
		bid.setPpoints(ppsInit);
		bid.setDescription("Create project "+ project.getName());
		bid.setState(BidState.OFFERED);

		Decision assign_bid = new Decision();
		decisionDao.save(assign_bid);

		Decision accept_bid = new Decision();
		decisionDao.save(accept_bid);

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

		project.setPpsTot(bid.getPpoints());

		cbtion.setAssignedPpoints(bid.getPpoints());
		cbtion.setContributor(bid.getCreator());
		cbtion.setState(CbtionState.ACCEPTED);

		/* add user to project contributors */
		contributorDao.updateContributor(project.getId(), creator.getId(), bid.getPpoints());
	}

	@Transactional
	public Project projectGet(Long id) {
		return projectDao.get(id);
	}

	@Transactional
	public Project projectGet(String project_name) {
		return projectDao.get(project_name);
	}

	@Transactional
	public ProjectDto projectGetDto(String project_name) {
		Project project = projectDao.get(project_name);
		ProjectDto dto = project.toDto();
		return dto;
	}

	@Transactional
	public List<String> projectGetList() {
		return projectDao.getListEnabled();
	}

	@Transactional
	public List<Project> projectGetAll(Integer max) {
		return projectDao.getFromRef(new Project(), max);
	}

	@Transactional
	public List<UsernameAndPps> projectContributorsAndPpsGet(Long projectId) {

		List<UsernameAndPps> usernamesAndPps = new ArrayList<UsernameAndPps>();

		for(Contributor contributor : getProjectContributors(projectId)) {
			UsernameAndPps usernameAndPps = new UsernameAndPps();
			usernameAndPps.setUsername(contributor.getContributorUser().getUsername());
			usernameAndPps.setPps(contributor.getPps());
			
			usernamesAndPps.add(usernameAndPps);
		}

		Collections.sort(usernamesAndPps, new Comparator<UsernameAndPps>(){
			public int compare(UsernameAndPps o1, UsernameAndPps o2){
				return Double.compare(o2.getPps(), o1.getPps());
			}
		});

		return usernamesAndPps;
	}

	@Transactional
	public void projectUpdatePpsTot(Long projectId, double lastOne) {
		Project project = projectDao.get(projectId);

		double ppsTot = 0.0;
		for(Contributor ctrb : project.getContributors()) {
			ppsTot += ctrb.getPps();
		}

		project.setPpsTot(ppsTot+lastOne);
		projectDao.save(project);
	}

	@Transactional
	public Set<Contributor> getProjectContributors(Long projectId) {
		return projectDao.getContributors(projectId);
	}

	@Transactional
	public Long goalCreate(GoalDto goalDto, GoalState state) throws IOException {
		if(!goalExist(goalDto.getGoalTag(), goalDto.getProjectName())) {
			Goal goal = new Goal();
			Project project = projectDao.get(goalDto.getProjectName());
			projectDao.save(project);

			goal.setCreationDate(new Timestamp(System.currentTimeMillis()));
			goal.setCreator(userDao.get(goalDto.getCreatorUsername()));
			goal.setDescription(goalDto.getDescription());
			goal.setProject(project);
			goal.setState(state);
			goal.setGoalTag(goalDto.getGoalTag());
			
			/* goal start attached, all the logic needed for when 
			 * detached is set later */
			goal.setAttached(true);
			
			Long id = goalDao.save(goal);

			/* each goal has its own decision realm */
			DecisionRealm realm = new DecisionRealm();
			realm.setGoal(goal);
			
			/* the voters and weights of the decision realm are initialized to the parent goal realm */
			boolean hasParent = false;
			if(goalDto.getParentGoalTag() != null) {
				if(goalDto.getParentGoalTag() != "") {
					Goal parent = goalDao.get(goalDto.getParentGoalTag(), project.getName());
					if(parent != null) {
						hasParent = true;
						goal.setParent(parent);
						/* decision realm should be initialized to that of the parent goal */
						decisionRealmInitToOther(realm, decisionRealmDao.getFromGoalId(parent.getId()).getId());
					}
				}
			}
			
			/* or the are initialized to 1 (weights) and all project contributors are voters */
			if(!hasParent){
				decisionRealmInitToProject(realm, project.getId());
			}
				
			decisionRealmDao.save(realm);

			Activity act = null;
			
			/* if the created goal has parents, the control is taken
			 * in the realm of the parent, otherwise it is controlled
			 * by its own realm */
			DecisionRealm goalControlRealm = null;
			
			if(hasParent) {
				goalControlRealm = decisionRealmDao.getFromGoalId(goal.getParent().getId());
			} else {
				goalControlRealm = realm;
			}
			
			switch(state) {
				case PROPOSED:
					Decision create = new Decision();
					
					create.setCreator(userDao.get("collectiveone"));
					create.setCreationDate(new Timestamp(System.currentTimeMillis()));
					create.setDescription("create goal '"+goal.getGoalTag()+"'");
					create.setState(DecisionState.IDLE);
					create.setVerdictHours(36);
					create.setDecisionRealm(goalControlRealm);
					create.setFromState(GoalState.PROPOSED.toString());
					create.setToState(GoalState.ACCEPTED.toString());
					create.setProject(project);
					if(hasParent) create.setGoal(goal.getParent());
					else create.setGoal(goal);
					create.setType(DecisionType.GOAL);
					create.setAffectedGoal(goal);

					goal.setCreateDec(create);
					
					decisionDao.save(create);
					
					act = new Activity("proposed", 
							new Timestamp(System.currentTimeMillis()),
							project);
					
					break;
					
					
				case ACCEPTED:
					/* one goal is created in accepted state at project creation */
					
		            Decision delete = new Decision();
		            
		            delete.setCreator(userDao.get("collectiveone"));
		            delete.setCreationDate(new Timestamp(System.currentTimeMillis()));
		            delete.setDescription("delete goal '"+goal.getGoalTag()+"'");
		            delete.setState(DecisionState.IDLE);
		            delete.setVerdictHours(36);
		            delete.setDecisionRealm(goalControlRealm);
		            delete.setFromState(GoalState.ACCEPTED.toString());
		            delete.setToState(GoalState.DELETED.toString());
		            delete.setProject(project);
		            if(hasParent) delete.setGoal(goal.getParent());
					else delete.setGoal(goal);
		            delete.setType(DecisionType.GOAL);
		            delete.setAffectedGoal(goal);

		            goal.setDeleteDec(delete);
 				    decisionDao.save(delete);
					
					act = new Activity("proposed", 
							new Timestamp(System.currentTimeMillis()),
							project);

					break;
				
				default:
					break;
			}
			
			act.setGoal(goal);
			act.setType(ActivityType.GOAL);
			activitySaveAndNotify(act);
			
			return id;
			
		} else {
			return (long) -1;
		}
	}
	
	@Transactional
	public Long goalCreate(GoalDto goalDto) throws IOException {
		return goalCreate(goalDto,GoalState.PROPOSED);
	}

	@Transactional
	public GoalDto goalGetDto(Long goalId) {
		Goal goal = goalDao.get(goalId);
		GoalDto dto = goal.toDto();
		goalAddParentsAndSubgoals(dto);

		return dto;
	}

	@Transactional
	public boolean goalExist(String goalTag, String projectName) {
		Goal goal = goalDao.get(goalTag, projectName);
		if(goal != null) {
			return true;
		} else {
			return false;
		}
	}
	
	@Transactional
	public boolean goalExist(String goalTag, String projectName, GoalState state) {
		Goal goal = goalDao.get(goalTag, projectName, state);
		if(goal != null) {
			return true;
		} else {
			return false;
		}
	}
	
	@Transactional
	public GoalDto goalGetDto(String goalTag, String projectName) {
		Goal goal = goalDao.get(goalTag, projectName);
		GoalDto dto = null;
		if(goal != null) {
			dto = goal.toDto();
			goalAddParentsAndSubgoals(dto);
		}

		return dto;
	}

	@Transactional
	public GoalDtoListRes goalDtoGetFiltered(Filters filters) {
		ObjectListRes<Goal> goalsRes = goalDao.get(filters);

		GoalDtoListRes goalsDtosRes = new GoalDtoListRes();

		goalsDtosRes.setResSet(goalsRes.getResSet());
		goalsDtosRes.setGoalDtos(new ArrayList<GoalDto>());

		for(Goal goal : goalsRes.getObjects()) {
			GoalDto dto = goal.toDto();
			goalAddParentsAndSubgoals(dto);

			goalsDtosRes.getGoalDtos().add(dto);
		}

		return goalsDtosRes;
	}

	@Transactional
	public void goalAddParentsAndSubgoals(GoalDto goalDto) {
		List<String> parentGoalsTags = new ArrayList<String>();
		for(Goal parent : goalDao.getAllParents(goalDto.getId())) { 
			parentGoalsTags.add(parent.getGoalTag()); 
		}
		goalDto.setParentGoalsTags(parentGoalsTags);

		List<String> subGoalsTags = new ArrayList<String>();
		
		List<GoalState> states = new ArrayList<GoalState>();
		states.add(GoalState.PROPOSED);
		states.add(GoalState.ACCEPTED);
		
		for(Goal subgoal : goalDao.getSubgoals(goalDto.getId(),states)) { 
			subGoalsTags.add(subgoal.getGoalTag()); 
		}
		goalDto.setSubGoalsTags(subGoalsTags);
	}

	@Transactional
	public List<String> goalGetSuggestions(String query, List<String> projectNames) {
		if(projectNames.size() == 0) {
			projectNames = projectDao.getListEnabled();
		}
		return goalDao.getSuggestions(query, projectNames);
	}

	@Transactional
	public void goalsUpdateState() throws IOException {
		/* Update state of all not closed bids */
		List<Goal> goalsNotDeleted = goalDao.getNotDeleted();
		for(Goal goal : goalsNotDeleted) {
			goalUpdateState(goal.getId());
		}	
	}

	@Transactional
	public void goalUpdateState(Long goalId) throws IOException {
		goalFromProposedToAccepted(goalId);
		goalFromAcceptedToDeleted(goalId);
		goalUpdateNewParent(goalId);
		goalUpdateAttachment(goalId);
	}

	@Transactional
	public void goalFromProposedToAccepted(Long goalId) throws IOException {
		Goal goal = goalDao.get(goalId);
		goalDao.save(goal);

		/* Create decision */ 
		Decision create = goal.getCreateDec();

		/* update goal state based on create decision */

		Activity act = new Activity();
		act.setCreationDate(new Timestamp(System.currentTimeMillis()));
		act.setProject(goal.getProject());
		act.setGoal(goal);
		act.setType(ActivityType.GOAL);

		if(create != null) {
			switch(create.getState()){
			case OPEN: 
				break;
	
			case CLOSED_DENIED : 
				switch(goal.getState()) {
				case PROPOSED:
					goal.setState(GoalState.NOT_ACCEPTED);
					act.setEvent("not accepted");
					activitySaveAndNotify(act);
					break;
	
				default:
					break;
				}
	
				break;
	
			case CLOSED_ACCEPTED: 
				switch(goal.getState()) {
				case PROPOSED:
					goal.setState(GoalState.ACCEPTED);
					
					/* create delete decision */
		            Decision delete = new Decision();
		            
		            DecisionRealm realm = decisionRealmDao.getFromGoalId(goal.getId());
		            
		            delete.setCreator(userDao.get("collectiveone"));
		            delete.setCreationDate(new Timestamp(System.currentTimeMillis()));
		            delete.setDescription("delete goal '"+goal.getGoalTag()+"'");
		            delete.setState(DecisionState.IDLE);
		            delete.setVerdictHours(36);
		            delete.setDecisionRealm(realm);
		            delete.setFromState(GoalState.ACCEPTED.toString());
		            delete.setToState(GoalState.DELETED.toString());
		            delete.setProject(goal.getProject());
		            delete.setGoal(goal);
		            delete.setType(DecisionType.GOAL);
		            delete.setAffectedGoal(goal);
		            
		            goal.setDeleteDec(delete);
		            decisionDao.save(delete);
					
		            /* register event */
		            act.setEvent("accepted");
					activitySaveAndNotify(act);
					
					
					break;
					
				default:
					break;
				}
	
			default:
				break;
			}
		}
	}

	@Transactional
	public void goalFromAcceptedToDeleted(Long goalId) throws IOException {
		Goal goal = goalDao.get(goalId);
		goalDao.save(goal);

		/* Update accept decision */ 
		Decision delete = goal.getDeleteDec();

		Activity act = new Activity();
		act.setCreationDate(new Timestamp(System.currentTimeMillis()));
		act.setProject(goal.getProject());
		act.setGoal(goal);
		act.setType(ActivityType.GOAL);

		/* update goal state based on delete decision */

		if(delete != null) {
			switch(delete.getState()){
			case OPEN: 
				break;
	
			case CLOSED_DENIED : 
				switch(goal.getState()) {
				case PROPOSED:
					break;
	
				case ACCEPTED:
					break;				
	
				default:
					break;
				}
	
				break;
	
			case CLOSED_ACCEPTED: 
				switch(goal.getState()) {
				case PROPOSED:
					goal.setState(GoalState.DELETED);
					act.setEvent("deleted");
					activitySaveAndNotify(act);
					break;
	
				case ACCEPTED:
					goal.setState(GoalState.DELETED);
					act.setEvent("deleted");
					activitySaveAndNotify(act);
					break;				
	
				default:
					break;
				}
	
				break;
	
			default:
				break;
			} 
		}
	}

	@Transactional
	public void goalUpdateNewParent(Long goalId) throws IOException {
		Goal goal = goalDao.get(goalId);
		goalDao.save(goal);

		if(goal.getParentState() == GoalParentState.PROPOSED) {
			/* Create decision */ 
			Decision proposeParent = goal.getProposeParent();

			if(proposeParent != null) {
				/* update goal parent based on create proposeParent */
				Activity act = new Activity();
				act.setCreationDate(new Timestamp(System.currentTimeMillis()));
				act.setProject(goal.getProject());
				act.setGoal(goal);
				act.setType(ActivityType.GOAL);

				if(proposeParent != null) {
					switch(proposeParent.getState()){
					case OPEN: 
						break;
	
					case CLOSED_DENIED : 
						switch(goal.getParentState()) {
						case PROPOSED:
							goal.setParentState(GoalParentState.ACCEPTED);
							act.setEvent(goal.getProposedParent().getGoalTag()+" not accepted as parent");
							activitySaveAndNotify(act);
							break;
	
						default:
							break;
						}
	
						break;
	
					case CLOSED_ACCEPTED: 
						switch(goal.getParentState()) {
						case PROPOSED:
							goal.setParentState(GoalParentState.ACCEPTED);
							goal.setParent(goal.getProposedParent());
							act.setEvent(goal.getProposedParent().getGoalTag()+" accepted as parent");
							activitySaveAndNotify(act);
							break;
						default:
							break;
						}
	
					default:
						break;
					} 
				}
			}
		}
	}
	
	@Transactional
	public void goalProposeParent(Long goalId, String parentTag) throws IOException {
		Goal goal = goalDao.get(goalId);
		Goal proposedParent = goalDao.get(parentTag,goal.getProject().getName());

		if(proposedParent != null) {
			if(goal.getParentState() != GoalParentState.PROPOSED) {
				Project project = goal.getProject();
				Decision proposeParent = new Decision();

				proposeParent.setCreationDate(new Timestamp(System.currentTimeMillis()));
				proposeParent.setCreator(userDao.get("collectiveone"));
				proposeParent.setDecisionRealm(decisionRealmDao.getFromGoalId(goal.getId()));
				proposeParent.setDescription("set "+proposedParent.getGoalTag()+" as parent goal");
				proposeParent.setProject(project);
				proposeParent.setGoal(goal);
				proposeParent.setState(DecisionState.IDLE);
				proposeParent.setType(DecisionType.GOAL);
				proposeParent.setAffectedGoal(goal);
				proposeParent.setVerdictHours(36);

				decisionDao.save(proposeParent);

				goal.setProposeParent(proposeParent);
				goal.setProposedParent(proposedParent);
				goal.setParentState(GoalParentState.PROPOSED);

				goalDao.save(goal);

				Activity act = new Activity();
				act.setCreationDate(new Timestamp(System.currentTimeMillis()));
				act.setProject(project);
				act.setGoal(goal);
				act.setType(ActivityType.GOAL);
				act.setEvent(proposedParent.getGoalTag()+" proposed as parent");
				activitySaveAndNotify(act);

				status.setMsg(proposedParent.getGoalTag()+" proposed as parent");
				status.setSuccess(true);

			} else {
				status.setMsg(goal.getProposedParent().getGoalTag()+
						" already proposed as parent. Cannot propose more than one parent at the same time");
				status.setSuccess(false);
			}
		} else {
			status.setMsg("parent tag "+parentTag+" not found");
			status.setSuccess(false);
		}
	}
	
	@Transactional
	public List<String> goalGetParentGoalsTags(Goal goal) {
		/* Add parent goals too */
		List<String> parentGoalsTags = new ArrayList<String>();
		if(goal!= null) {
			List<Goal> parentGoals = goalDao.getAllParents(goal.getId());
			for(Goal parent : parentGoals) { parentGoalsTags.add(parent.getGoalTag()); }
		}
		return parentGoalsTags;
	}
	
	@Transactional
	public GoalWeightsDataDto goalGetWeightsData(String projectName, String goalTag, String username) {
		
		Goal goal = goalDao.get(goalTag, projectName);
		DecisionRealm realm = decisionRealmDao.getFromGoalId(goal.getId());
		
		GoalWeightsDataDto goalWeightsDataDto = new GoalWeightsDataDto();  
		
		if(!username.equals("anonymousUser")) {
			User user = userDao.get(username);
			
			Voter voter = decisionRealmDao.getVoter(realm.getId(), user.getId());
			
			GoalUserWeightsDto userWeightsDto = new GoalUserWeightsDto();
			userWeightsDto.setUsername(username);
			userWeightsDto.setMaxWeight(voter.getMaxWeight());
			userWeightsDto.setActualWeight(voter.getActualWeight());
			userWeightsDto.setScale(voter.getScale());
			
			goalWeightsDataDto.setUserWeightsDto(userWeightsDto);
		}
		
		List<VoterDto> votersDtos = new ArrayList<VoterDto>();
		for(Voter otherVoter : realm.getVoters()) {
			votersDtos.add(otherVoter.toDto());
		}
		
		goalWeightsDataDto.setGoalTag(goalTag);
		goalWeightsDataDto.setProjectName(projectName);
		goalWeightsDataDto.setTotalWeight(realm.getWeightTot());
		
		goalWeightsDataDto.setVotersDtos(votersDtos);
		
		return goalWeightsDataDto;
	}
	
	@Transactional
	public void goalTouch(GoalTouchDto touchDto) {
		Goal goal = goalDao.get(touchDto.getGoalTag(), touchDto.getProjectName());
		DecisionRealm realm = decisionRealmDao.getFromGoalId(goal.getId());
		User user = userDao.get(touchDto.getUsername());
		Voter voter = decisionRealmDao.getVoter(realm.getId(), user.getId());
		
		double weigthBefore = voter.getActualWeight();
		
		if(touchDto.getTouchFlag()) {
			voter.setScale(1.0);
		} else {
			voter.setScale(0.0);
		}
		
		double weightAfter = voter.getActualWeight();
		realm.setWeightTot(realm.getWeightTot() + (weightAfter - weigthBefore));
		decisionRealmDao.save(realm);
	}
	
	@Transactional
	public void goalDetach(Long goalId, double initialBudget) {
		Goal goal = goalDao.get(goalId);
		
		if(goal.getAttached() && (goal.getParent() != null)) {
			
			/* check that the detaching process has not already been requested */
			boolean goWithDetach = false;
			if(goal.getIncreaseBudget() == null) {
				goWithDetach = true;
			} else {
				switch(goal.getIncreaseBudget().getState()) {
					case IDLE:
					case OPEN:
						goWithDetach = false;
						break;
						
					case CLOSED_ACCEPTED:
					case CLOSED_DENIED:
					case CLOSED_EXTERNALLY:
						goWithDetach = true;
						break;
				}
			}
		
			if(goWithDetach) {
				goal.setCurrentBudget(0.0);
				goal.setPpsToIncrease(initialBudget);
					
				Decision increaseBudget = new Decision();
				
				increaseBudget.setCreationDate(new Timestamp(System.currentTimeMillis()));
				increaseBudget.setCreator(userDao.get("collectiveone"));
				/* detach and increase budget decisions are taken in the supergoal realm */
				increaseBudget.setDecisionRealm(decisionRealmDao.getFromGoalId(goal.getParent().getId()));
				increaseBudget.setDescription("detach goal +"+goal.getGoalTag()+
						" from +"+goal.getParent().getGoalTag()+" with an initial budget of "+initialBudget+" pps");
				increaseBudget.setProject(goal.getProject());
				increaseBudget.setGoal(goal.getParent());
				increaseBudget.setState(DecisionState.IDLE);
				increaseBudget.setType(DecisionType.GOAL);
				increaseBudget.setAffectedGoal(goal);
				increaseBudget.setVerdictHours(36);
			}
		}
	}
	
	@Transactional
	public void goalUpdateAttachment(Long goalId) throws IOException {
		Goal goal = goalDao.get(goalId);
		
		Activity act = new Activity();
		act.setCreationDate(new Timestamp(System.currentTimeMillis()));
		act.setProject(goal.getProject());
		act.setGoal(goal);
		act.setType(ActivityType.GOAL);
		
		if(goal.getAttached()) {
			/* the increaseBudget decision is used both as a detach decision
			 * and as an increase budget decision */
			
			Decision increaseBudget = goal.getIncreaseBudget();
			if(increaseBudget != null) {
				switch(increaseBudget.getState()) {
				case IDLE:
				case OPEN:
					/* wait for the increase budget decision to close */
					break;
					
				case CLOSED_ACCEPTED:
					/* initialized the budget from the parent goal budget 
					 * and check there is enough pps */
					boolean goWithDetachment = false;
					Goal parent = goal.getParent();
					if(goal.getParent() != null) {
						if(parent.getCurrentBudget() >= goal.getPpsToIncrease()) {
							/* take the pps from the parent budget */
							parent.setCurrentBudget(parent.getCurrentBudget() - goal.getPpsToIncrease());
							goal.setCurrentBudget(goal.getPpsToIncrease());
							goalDao.save(parent);
							
							goWithDetachment = true;
						} else {
							goWithDetachment = false;
						}
					} else {
						goal.setCurrentBudget(goal.getPpsToIncrease());
						goWithDetachment = true;
					}
						
					if(goWithDetachment) {
						/* the goal should be detached*/
						goal.setAttached(false);
						
						/* create reattach decision */
						Decision reattach = new Decision();
						
						reattach.setCreator(userDao.get("collectiveone"));
						reattach.setCreationDate(new Timestamp(System.currentTimeMillis()));
						reattach.setDescription("reattach goal +"+goal.getGoalTag()+" to "+goal.getParent());
						reattach.setState(DecisionState.IDLE);
						reattach.setVerdictHours(36);
						reattach.setDecisionRealm(decisionRealmDao.getFromGoalId(goal.getId()));
						reattach.setFromState(GoalState.PROPOSED.toString());
						reattach.setToState(GoalState.ACCEPTED.toString());
						reattach.setProject(goal.getProject());
						reattach.setGoal(goal);
						reattach.setType(DecisionType.GOAL);
						reattach.setAffectedGoal(goal);

						goal.setReattach(reattach);
						
						decisionDao.save(reattach);
						goalDao.save(goal);
						
						act.setEvent("detached");
						activitySaveAndNotify(act);
					}
					
					break;
					
				case CLOSED_DENIED:
				case CLOSED_EXTERNALLY:
					act.setEvent("detachment refused");
					activitySaveAndNotify(act);
					
				}
			}
		} else {
			
			Decision reattach = goal.getReattach();
			if(reattach != null) {
				switch(reattach.getState()) {
				case IDLE:
				case OPEN:
					break;
					
				case CLOSED_ACCEPTED:
					goal.setAttached(true);
					
					/* remaining budget is returned to parent or grand parent */
					Goal parent = goal.getParent();
					if(!parent.getAttached()) {
						/* if parent is detached, return the pps to the parent budget */
						parent.setCurrentBudget(parent.getCurrentBudget() + goal.getCurrentBudget());
					} else {
						/* else look for the parent's parent iteratively until one detached is found,
						 * if found */
						boolean lookForDetachedGrandPa = true;
						boolean grandPaFound = false;
						
						Goal grandPa = parent.getParent();
						while(lookForDetachedGrandPa) {
							if(grandPa == null) {
								lookForDetachedGrandPa = false;
								grandPaFound = false;
							} else {
								if(!grandPa.getAttached()) {
									lookForDetachedGrandPa = false;
									grandPaFound = false;
								} else {
									grandPa = grandPa.getParent();
								}
							}
						}
						
						/* and give the remaining pps to it */
						if(grandPaFound) {
							grandPa.setCurrentBudget(grandPa.getCurrentBudget() + goal.getCurrentBudget());
						}
					}
					
					/* budget is set to zero*/
					goal.setCurrentBudget(0.0);
					
					/* increaseBudget decision is deleted to leave room for 
					 * detachment to reoccur */
					goal.setIncreaseBudget(null);
					goal.setPpsToIncrease(0.0);
					
					/* decision realm is reset to that of the parent */
					Long parentRealmId = decisionRealmDao.getIdFromGoalId(goal.getParent().getId());
					DecisionRealm realm = decisionRealmDao.getFromGoalId(goal.getParent().getId());
					
					decisionRealmUpdateToOther(realm, parentRealmId);
					
					break;
					
				case CLOSED_DENIED:
				case CLOSED_EXTERNALLY:
					delete 
					goal.setReattach(null);
					
					decisionDao.save(reattach);
					goalDao.save(goal);
					break;
					
				}
			}
		}
	}


	@Transactional
	public void cbtionSave(Cbtion cbtion) {
		cbtionDao.save(cbtion);
	}
	
	@Transactional
	public void cbtionUpdate(Cbtion cbtion) {
		cbtionDao.update(cbtion);
	}

	@Transactional
	public Long cbtionCreate(CbtionDto cbtionDto) throws IOException {
		Cbtion cbtion = new Cbtion();
		Project project = projectDao.get(cbtionDto.getProjectName());
		projectDao.save(project);
		Goal goal = goalDao.get(cbtionDto.getGoalTag(), project.getName());

		cbtion.setCreationDate(new Timestamp(System.currentTimeMillis()));
		cbtion.setCreator(userDao.get(cbtionDto.getCreatorUsername()));
		cbtion.setDescription(cbtionDto.getDescription());
		cbtion.setProject(project);
		cbtion.setProduct(cbtionDto.getProduct());
		cbtion.setSuggestedBid(cbtionDto.getSuggestedBid());
		cbtion.setState(CbtionState.PROPOSED);
		cbtion.setTitle(cbtionDto.getTitle());
		cbtion.setGoal(goal);

		Long id = cbtionDao.save(cbtion);

		DecisionRealm realm = decisionRealmDao.getFromGoalId(goal.getId());
		decisionRealmDao.save(realm);

		Decision open = new Decision();

		open.setCreator(userDao.get("collectiveone"));
		open.setCreationDate(new Timestamp(System.currentTimeMillis()));
		open.setDecisionRealm(realm);
		open.setDescription("open contribution '"+cbtion.getTitle()+"'");
		open.setFromState(CbtionState.PROPOSED.toString());		
		open.setToState(CbtionState.OPEN.toString());
		open.setProject(project);
		open.setGoal(goal);
		open.setState(DecisionState.IDLE);
		open.setVerdictHours(36);
		open.setType(DecisionType.CBTION);
		open.setAffectedCbtion(cbtion);

		cbtion.setOpenDec(open);
		decisionDao.save(open);

		Activity act = new Activity("created", 
				new Timestamp(System.currentTimeMillis()),
				cbtion.getProject());

		act.setCbtion(cbtion);
		act.setType(ActivityType.CBTION);
		activitySaveAndNotify(act);

		return id;
	}
	
	@Transactional
	public Long cbtionEdit(CbtionDto cbtionDto) {
		Cbtion cbtion = cbtionDao.get(cbtionDto.getId());
		
		if(cbtion != null) {
			switch(cbtion.getState()) {
			case PROPOSED:
				cbtion.setTitle(cbtionDto.getTitle());
				cbtion.setDescription(cbtionDto.getDescription());
				cbtion.setProduct(cbtionDto.getProduct());
				cbtion.setSuggestedBid(cbtionDto.getSuggestedBid());
				
				cbtionDao.save(cbtion);	
				
				status.setMsg("cbtion updated");
				status.setSuccess(true);
				break;
			
			default:
				status.setMsg("cbtion is already open");
				status.setSuccess(true);
				break;
			}
			

			return cbtion.getId();
			
		} else {
			status.setMsg("cbtion not found");
			status.setSuccess(false);
			
			return (long) 0;
		}
		
	}

	@Transactional
	public Cbtion cbtionGet(Long id) {
		return cbtionDao.get(id);
	}

	@Transactional
	public List<Cbtion> cbtionGet(Cbtion refCbtion) {
		return cbtionDao.get(refCbtion);
	}
	
	@Transactional
	public User cbtionGetCreator(Long id) {
		return cbtionDao.get(id).getCreator();
	}

	@Transactional
	public CbtionDto cbtionGetDto(Long cbtionId) {
		Cbtion cbtion = cbtionDao.get(cbtionId);
		CbtionDto cbtionDto = cbtion.toDto(goalGetParentGoalsTags(cbtion.getGoal()), cbtionGetNSubComments(cbtionId));
		return cbtionDto;
	}

	@Transactional
	public List<CbtionDto> cbtionGetDto(Cbtion refCbtion) {
		List<Cbtion> cbtions = cbtionDao.get(refCbtion);
		List<CbtionDto> cbtionDtos = new ArrayList<CbtionDto>();
		for(Cbtion cbtion : cbtions) {
			cbtionDtos.add(cbtion.toDto());
		}
		return cbtionDtos;
	}

	@Transactional
	public CbtionDtoListRes cbtionDtoGetFiltered(Filters filters) {
		ObjectListRes<Cbtion> cbtionsRes = cbtionDao.get(filters);

		CbtionDtoListRes cbtionsDtosRes = new CbtionDtoListRes();

		cbtionsDtosRes.setResSet(cbtionsRes.getResSet());
		cbtionsDtosRes.setCbtionsDtos(new ArrayList<CbtionDto>());

		for(Cbtion cbtion : cbtionsRes.getObjects()) {
			cbtionsDtosRes.getCbtionsDtos().add(cbtion.toDto(goalGetParentGoalsTags(cbtion.getGoal())));
		}

		return cbtionsDtosRes;
	}

	@Transactional
	public void cbtionsUpdateState() throws IOException {
		/* Update state of all not closed bids */
		List<Cbtion> cbtionsProposed = cbtionDao.getWithStates(Arrays.asList(CbtionState.PROPOSED, CbtionState.OPEN));
		for(Cbtion cbtion : cbtionsProposed) {
			cbtionUpdateState(cbtion.getId());
		}	
	}

	@Transactional
	public void cbtionUpdateState(Long cbtionId) throws IOException {
		Cbtion cbtion = cbtionDao.get(cbtionId);
		
		Activity act = new Activity();
		act.setProject(cbtion.getProject());
		act.setCreationDate(new Timestamp(System.currentTimeMillis()));
		act.setType(ActivityType.CBTION);
		act.setCbtion(cbtion);


		switch(cbtion.getState()) {
		
		case PROPOSED:
			Decision open = cbtion.getOpenDec();
			
			if(open != null) {
				switch(open.getState()){
				
					case OPEN: 
						break;
		
					case CLOSED_DENIED :
						cbtion.setState(CbtionState.NOTOPENED);
						cbtionDao.save(cbtion);
	
						act.setEvent("proposal refused");
						activitySaveAndNotify(act);
						break;
		
					case CLOSED_ACCEPTED: 
						cbtion.setState(CbtionState.OPEN);
						
						DecisionRealm realm = decisionRealmDao.getFromGoalId(cbtion.getGoal().getId());
						decisionRealmDao.save(realm);
		
						Decision delete = new Decision();
		
						delete.setCreator(userDao.get("collectiveone"));
						delete.setCreationDate(new Timestamp(System.currentTimeMillis()));
						delete.setDecisionRealm(realm);
						delete.setDescription("delete contribution '"+cbtion.getTitle()+"'");
						delete.setFromState(CbtionState.OPEN.toString());		
						delete.setToState(CbtionState.DELETED.toString());
						delete.setProject(cbtion.getProject());
						delete.setGoal(cbtion.getGoal());
						delete.setState(DecisionState.IDLE);
						delete.setVerdictHours(36);
						delete.setType(DecisionType.CBTION);
						delete.setAffectedCbtion(cbtion);
						
						decisionDao.save(delete);
						
						cbtion.setDeleteDec(delete);
						cbtionDao.save(cbtion);
	
						act.setEvent("opened for bidding");
						activitySaveAndNotify(act);
						
						break;
		
					default:
						break;
				}	
			}
			
			break;
			
		case OPEN:
			Decision delete = cbtion.getDeleteDec();
			
			if(delete != null) {
				switch(delete.getState()) {
					case OPEN: 
						break;
						
					case CLOSED_DENIED:
						break;
						
					case CLOSED_ACCEPTED:
						cbtionDao.remove(cbtion.getId());
						
						act.setEvent("deleted");
						activitySaveAndNotify(act);
						
						break;
					
					default:
						break;
				}
			}			
			
			break;

		default:
			break;
		}
	}


	@Transactional
	public ResStatus cbtionPromote(Long cbtionId, Long userId, boolean promoteUp) {
		ResStatus resStatus = new ResStatus();

		Cbtion cbtion = cbtionDao.get(cbtionId);
		cbtionDao.save(cbtion);

		Promoter promoter = promoterDao.getOfCbtion(cbtionId, userId);

		/* countPromotersDiff will doesn't include the current transaction so this logic 
		 * is used to solve the problem*/
		int currentRelevance = cbtionDao.countPromotersDiff(cbtionId);
		int newRelevance = currentRelevance;
		
		if(promoter == null) {
			promoter = new Promoter();
			promoter.setUser(userDao.get(userId));
			cbtion.getPromoters().add(promoter);
			if(promoteUp) {
				newRelevance = newRelevance + 1;
			} else {
				newRelevance = newRelevance - 1;
			}
		} else {
			if(promoter.getPromoteUp() !=  promoteUp) {
				if(promoteUp) {
					newRelevance = newRelevance + 2;
				} else {
					newRelevance = newRelevance - 2;
				}
			}
		}

		promoter.setCreationDate(new Timestamp(System.currentTimeMillis()));
		promoter.setPromoteUp(promoteUp);
		promoterDao.save(promoter);
		
		cbtion.setRelevance(newRelevance);

		return resStatus;		
	}
	
	@Transactional
	public CommentDto cbtionGetCommentDto(Long commentId) {
		return commentDao.get(commentId).toDto();
	}
	
	@Transactional
	public int cbtionGetNSubComments(Long cbtionId) {
		return cbtionDao.countSubComments(cbtionId);
	}

	@Transactional
	public List<CommentDto> cbtionGetCommentsDtos(Long cbtionId) {
		List<Comment> comments = cbtionDao.getCommentsSorted(cbtionId);
		List<CommentDto> commentsDtos = new ArrayList<CommentDto>();
		for (Comment comment : comments) {
			commentsDtos.add(comment.toDto());
		}
		return commentsDtos;
	}

	@Transactional
	public List<ReviewDto> cbtionGetReviewsDtos(Long cbtionId) {
		Bid bid = cbtionDao.getAcceptedBid(cbtionId);
		return bidGetReviewsDtos(bid.getId());
	}

	@Transactional
	public void bidSave(Bid bid, Long cbtionId) {
		Cbtion cbtion = cbtionDao.get(cbtionId);
		bid.setCbtion(cbtion);
		cbtion.getBids().add(bid);

		bidDao.save(bid);
		cbtionDao.save(cbtion);

	}

	@Transactional
	public Bid bidGet(Long id) {
		return bidDao.get(id);
	}
	
	@Transactional
	public BidDto bidGetDto(Long id) {
		return bidDao.get(id).toDto();
	}

	@Transactional
	public User bidGetCreator(Long id) {
		return bidDao.get(id).getCreator();
	}

	@Transactional
	public void bidSave(Bid bid) {
		bidDao.save(bid);
	}

	@Transactional
	public void bidSaveState(Long bidId, BidState state) {
		Bid bid = bidDao.get(bidId);
		bid.setState(state);
		bidDao.save(bid);
	}

	@Transactional
	public Long bidCreate(BidNewDto bidDtoIn) throws IOException {

		Long cbtionId = bidDtoIn.getCbtionId();
		User user = userDao.get(bidDtoIn.getCreatorUsername());

		Cbtion cbtion = cbtionDao.get(cbtionId);
		Bid bid = bidDao.getOfCbtionAndUser(cbtionId, user.getId());

		if(bid == null) {
			if(cbtion.getState() == CbtionState.OPEN) {
				Project project = cbtion.getProject();
				projectDao.save(project);
	
				bid = new Bid();
				
				bid.setCreator(userDao.get(user.getId()));
				bid.setCreationDate(new Timestamp(System.currentTimeMillis()));
				bid.setDescription(bidDtoIn.getDescription());
				bid.setCbtion(cbtion);
	
				if(bidDtoIn.getOffer()) {
					bid.setState(BidState.OFFERED);
					bid.setPpoints(bidDtoIn.getPpoints());
				} else {
					bid.setState(BidState.CONSIDERING);
				}
				
				bidDao.save(bid);
	
				cbtion.getBids().add(bid);
				cbtionDao.save(cbtion);
	
				Activity act = new Activity();
				act.setCreationDate(new Timestamp(System.currentTimeMillis()));
				act.setProject(project);
				act.setBid(bid);
				act.setType(ActivityType.BID);
				act.setEvent("created");
				activitySaveAndNotify(act);
				
				return bid.getId();
			} 
		} else {
			return bid.getId();
		}
		return null;
	}
	
	@Transactional
	public void bidFromConsideringToOffered(BidNewDto bidDto) {
		Bid bid = bidDao.get(bidDto.getId());

		Project project = bid.getCbtion().getProject();

		bid.setDescription(bidDto.getDescription());
		bid.setPpoints(bidDto.getPpoints());
		/* Sum one day as delivery is at the end of the date marked as delivery day*/
		bid.setDeliveryDate(new Timestamp(bidDto.getDeliveryDate() + 24*60*60*1000));
		bid.setState(BidState.OFFERED);

		/* prepare assign decision */
		DecisionRealm realm = decisionRealmDao.getFromGoalId(bid.getCbtion().getGoal().getId());

		Decision assign = new Decision();
		assign.setCreator(userDao.get("collectiveone"));
		assign.setCreationDate(new Timestamp(System.currentTimeMillis()));
		assign.setDescription("assign contribution '"+bid.getCbtion().getTitle()+"' to "+bid.getCreator().getUsername());
		assign.setFromState(BidState.OFFERED.toString());
		assign.setToState(BidState.ASSIGNED.toString());
		assign.setState(DecisionState.IDLE);
		/* TODO: Include bid decisions duration logic */
		assign.setVerdictHours(36);
		assign.setDecisionRealm(realm);
		assign.setProject(project);
		assign.setGoal(bid.getCbtion().getGoal());
		assign.setType(DecisionType.BID);
		assign.setAffectedBid(bid);

		bid.setAssign(assign);

		decisionDao.save(assign);
		bidDao.save(bid);

	}

	@Transactional
	public void bidMarkDone(DoneDto doneDto) throws IOException {
		
		Bid bid = bidDao.get(doneDto.getBidId());
		
		if(bid.getCreator().getUsername().equals(doneDto.getUsername())) {
			if(doneDto.getNewPps() <= bid.getPpoints()) {
				bid.setPpoints(doneDto.getNewPps());
				bid.setDoneState(BidDoneState.DONE);
				bid.setDoneDescription(doneDto.getDescription());
				bid.setDoneDate(new Timestamp(System.currentTimeMillis()));
				bidDao.save(bid);	

				Activity act = new Activity();
				act.setCreationDate(new Timestamp(System.currentTimeMillis()));
				act.setProject(bid.getCbtion().getProject());
				act.setBid(bid);
				act.setType(ActivityType.BID);
				act.setEvent("marked done");
				activitySaveAndNotify(act);
			}
		}
	}

	@Transactional
	public List<BidDto> bidGetOfUserDto(Long userId) {
		List<Bid> bids = bidDao.getOfUser(userId);
		List<BidDto> bidDtos = new ArrayList<BidDto>();
		for (Bid bid : bids) {
			bidDtos.add(bid.toDto());
		}
		return bidDtos;
	}

	@Transactional
	public List<BidDto> bidGetOfCbtionDto(Long cbtionId) {
		List<Bid> bids = bidDao.getOfCbtion(cbtionId);
		List<BidDto> bidDtos = new ArrayList<BidDto>();
		for (Bid bid : bids) {
			bidDtos.add(bid.toDto());
		}
		return bidDtos;
	}

	@Transactional
	public void bidsUpdateState() throws IOException {
		/* Update state of all not closed bids */
		List<Bid> bidsNotClosed = bidDao.getNotClosed();
		for(Bid bid : bidsNotClosed) {
			bidUpdateState(bid.getId());
		}	
	}

	@Transactional
	public List<Bid> bidGetAll() {
		List<Bid> bids = bidDao.getAll(100000);
		return bids;
	}

	@Transactional
	public List<ReviewDto> bidGetReviewsDtos(Long bidId) {
		List<Review> reviews = bidDao.get(bidId).getReviews();
		List<ReviewDto> reviewsDtos = new ArrayList<ReviewDto>();
		for (Review review : reviews) {
			reviewsDtos.add(review.toDto());
		}
		return reviewsDtos;
	}

	@Transactional
	public void bidUpdateState(Long bidId) throws IOException {
		Bid bid = bidDao.get(bidId);
		Cbtion cbtion = bid.getCbtion();

		bidDao.save(bid);
		cbtionDao.save(cbtion);

		Activity act = new Activity();
		act.setCreationDate(new Timestamp(System.currentTimeMillis()));
		act.setBid(bid);
		act.setType(ActivityType.BID);
		act.setProject(cbtion.getProject());

		switch(bid.getState()) {
		case OFFERED:
			Decision assign = bid.getAssign();
			switch(assign.getState()) {
			case OPEN: 
				break;

			case CLOSED_DENIED : 
				bid.setState(BidState.NOT_ASSIGNED);
				act.setEvent("not assigned");
				activitySaveAndNotify(act);
				break;

			case CLOSED_ACCEPTED: 
				bid.setState(BidState.ASSIGNED);
				act.setEvent("assigned");
				activitySaveAndNotify(act);
				cbtion.setState(CbtionState.ASSIGNED);

				/* prepare accept decision */
				Decision accept = new Decision();
				accept.setCreator(userDao.get("collectiveone"));
				accept.setCreationDate(new Timestamp(System.currentTimeMillis()));
				accept.setDescription("accept contribution '"+bid.getCbtion().getTitle()+"' as delivered by "+bid.getCreator().getUsername());
				accept.setFromState(BidState.ASSIGNED.toString());
				accept.setToState(BidState.ACCEPTED.toString());
				accept.setState(DecisionState.IDLE);
				/* TODO: Include bid duration logic */
				accept.setVerdictHours(36);
				accept.setDecisionRealm(decisionRealmDao.getFromGoalId(bid.getCbtion().getGoal().getId()));
				accept.setProject(cbtion.getProject());
				accept.setGoal(cbtion.getGoal());
				accept.setType(DecisionType.BID);
				accept.setAffectedBid(bid);

				bid.setAccept(accept);
				decisionDao.save(accept);

				break;

			default:
				break;
			}
			break;

		case ASSIGNED:
			Decision accept = bid.getAccept();
			if(accept != null) {
				switch(accept.getState()) {
				case OPEN: 
					break;

				case CLOSED_DENIED: 
					bid.setState(BidState.NOT_ACCEPTED);
					act.setEvent("not accepted");
					activitySaveAndNotify(act);
					break;

				case CLOSED_ACCEPTED: 
					bid.setState(BidState.ACCEPTED); 

					/* once a bid is accepted, the cbtion and all other bids on it
					 * are closed */

					/* update cbtion state */
					cbtion.setAssignedPpoints(bid.getPpoints());
					cbtion.setContributor(bid.getCreator());
					cbtion.setState(CbtionState.ACCEPTED);

					/* close all other bids and decisions */
					for(Bid otherBid : cbtion.getBids()) {
						if(otherBid.getId() != bid.getId()) {
							otherBid.getAssign().setState(DecisionState.CLOSED_EXTERNALLY);
							otherBid.getAccept().setState(DecisionState.CLOSED_EXTERNALLY);
							otherBid.setState(BidState.SUPERSEEDED);
							bidDao.save(otherBid);
						}
					}

					/* -----------------------------------------------------------------------*/ 
					/* updated pps of user in project - This is the only place where PPS are created/updated
					 * and here all the decisions weights update logic is called */
					/* -----------------------------------------------------------------------*/
					contributorUpdate(bid.getCbtion().getProject().getId(), bid.getCreator().getId(), cbtion.getAssignedPpoints());
					projectUpdatePpsTot(bid.getCbtion().getProject().getId(), 0.0);
					updateVoterInProject(bid.getCbtion().getProject().getId(), bid.getCreator().getId());
					/* -----------------------------------------------------------------------*/


					act.setEvent("accepted");
					activitySaveAndNotify(act);

					break;

				default:
					break;
				}
			}
			break;

		default:
			break;

		}
	}


	@Transactional
	public ThesisDto thesisOfUser(Long decId, Long userId) {
		Thesis thesis = thesisDao.getOfUserInDec(decId, userId);
		ThesisDto thesisDto = null;
		if(thesis != null) thesisDto = thesis.toDto();
		return thesisDto;
	}

	@Transactional
	public String thesisOfDecSave(User author, int value, Long decId) {

		Decision dec = decisionDao.get(decId);

		if(dec.getState() == DecisionState.IDLE || dec.getState() == DecisionState.OPEN) {
			/* if decision is still open */
			Voter voter = decisionRealmDao.getVoter(dec.getDecisionRealm().getId(),author.getId());

			if(voter != null) {
				/* if voter is in the realm of the decision */
				Thesis thesis = decisionDao.getThesisCasted(decId, author.getId());				

				if(thesis == null) {
					thesis = new Thesis();
					thesis.setAuthor(author);
					thesis.setDecision(dec);
				}
				
				thesisDao.save(thesis);

				thesis.setValue(value);
				thesis.setCastDate(new Timestamp(System.currentTimeMillis()));

				/* weight of the thesis is set at cast time, as a copy of the realm,
				 * so theses weights need to be updated every time voter weight changes */
				thesis.setWeight(voter.getActualWeight());

				decisionDao.save(dec);

				return "thesis saved";
			} else {
				return "voter not in decision realm";
			}
		} else {
			return "decision is not open";
		}
	}

	@Transactional
	public void thesisAssignOfBidSave(User author, int value, Long bidId) {
		Bid bid = bidDao.get(bidId);
		Cbtion cbtion = bid.getCbtion();
		if((cbtion.getState() == CbtionState.OPEN) || 
				(cbtion.getState() == CbtionState.ASSIGNED)) {

			thesisOfDecSave(author, value, bid.getAssign().getId());	
		}
	}

	@Transactional
	public void thesisAcceptOfBidSave(User author, int value, Long bidId) {
		Bid bid = bidDao.get(bidId);
		Cbtion cbtion = bid.getCbtion();
		if(cbtion.getState() == CbtionState.ASSIGNED) {
			thesisOfDecSave(author, value, bid.getAccept().getId());	
		}
	}

	@Transactional
	public void decisionsUpdateState() throws IOException {

		List<DecisionState> states = new ArrayList<DecisionState>();
		states.add(DecisionState.IDLE);
		states.add(DecisionState.OPEN);

		List<Decision> decsIdle = decisionDao.getWithStates(states);
		for(Decision dec : decsIdle) {
			decisionUpdateState(dec.getId());
		}
	}
	
	@Transactional
	public void decisionUpdateState(Long id) throws IOException {
		Decision dec = decisionDao.get(id);
		
		/* Update the decision */
		DecisionState before = dec.getState();
		dec.updateState(timeService.getNow());
		decisionDao.save(dec);

		/* store activity only for custom decisions (automatic decisions activity 
		 * is recorded based on the element it changes).
		 * Activity is recorded if the decision switch state */
		boolean isCustom = false;
		if(!dec.getCreator().getUsername().equals("collectiveone")) {
			isCustom = true;
		}
		
	
		Activity act = new Activity();
		act.setCreationDate(new Timestamp(System.currentTimeMillis()));
		act.setDecision(dec);
		act.setType(ActivityType.DECISION);
		act.setProject(dec.getProject());

		switch(before) {
		case IDLE:
			switch(dec.getState()) {
			case IDLE:
				break;

			case OPEN:
				act.setEvent("opened");
				/* decision activity from idle to open is registered for automatic decisions too */
				activitySaveAndNotify(act);
				break;

			case CLOSED_ACCEPTED:
				act.setEvent("accepted");
				if(isCustom) activitySaveAndNotify(act);
				break;

			case CLOSED_DENIED:
				act.setEvent("rejected");
				if(isCustom) activitySaveAndNotify(act);
				break;

			case CLOSED_EXTERNALLY:
				act.setEvent("closed externally");
				if(isCustom) activitySaveAndNotify(act);
				break;
			}		

			break;

		case OPEN:
			switch(dec.getState()) {
			case IDLE:
				act.setEvent("back to idle");
				activitySaveAndNotify(act);
				break;

			case OPEN:
				break;

			case CLOSED_ACCEPTED:
				act.setEvent("accepted");
				if(isCustom) activitySaveAndNotify(act);
				break;

			case CLOSED_DENIED:
				act.setEvent("rejected");
				if(isCustom) activitySaveAndNotify(act);
				break;

			case CLOSED_EXTERNALLY:
				act.setEvent("closed externally");
				if(isCustom) activitySaveAndNotify(act);
				break;
			}	
			break;

		case CLOSED_ACCEPTED:
		case CLOSED_DENIED:
		case CLOSED_EXTERNALLY:
			break;

		}
	}
	

	@Transactional
	public DecisionDtoListRes decisionDtoGetFiltered(Filters filters) {
		ObjectListRes<Decision> decisionsRes = decisionDao.get(filters);

		DecisionDtoListRes decisionsDtosRes = new DecisionDtoListRes();

		decisionsDtosRes.setResSet(decisionsRes.getResSet());
		decisionsDtosRes.setDecisionDtos(new ArrayList<DecisionDtoFull>());

		for(Decision decision : decisionsRes.getObjects()) {
			decisionsDtosRes.getDecisionDtos().add(decision.toDto());
		}

		return decisionsDtosRes;
	}	

	@Transactional
	public Long decisionCreate(DecisionDtoCreate decisionDto) throws IOException {
		Decision decision = new Decision();
		Project project = projectDao.get(decisionDto.getProjectName());
		Goal goal = goalDao.get(project.getName(), decisionDto.getGoalTag());
		
		projectDao.save(project);

		DecisionRealm realm = decisionRealmDao.getFromGoalId(goal.getId());
		decisionRealmDao.save(realm);

		decision.setCreationDate(new Timestamp(System.currentTimeMillis()));
		decision.setCreator(userDao.get(decisionDto.getCreatorUsername()));
		decision.setDescription(decisionDto.getDescription());
		decision.setProject(project);
		decision.setGoal(goal);
		decision.setState(DecisionState.IDLE);
		decision.setDecisionRealm(realm);
		decision.setType(DecisionType.GENERAL);
		decision.setVerdictHours(decisionDto.getVerdictHours());

		decisionDao.save(decision);

		if(!decision.getCreator().getUsername().equals("collectiveone"))  {
			Activity act = new Activity();
			act.setCreationDate(new Timestamp(System.currentTimeMillis()));
			act.setDecision(decision);
			act.setType(ActivityType.DECISION);
			act.setProject(project);
			act.setEvent("created");
			activitySaveAndNotify(act);
		}

		status.setMsg("decision created");
		status.setSuccess(true);

		return decision.getId();
	}


	@Transactional
	public Decision decisionGet(Long id) {
		return decisionDao.get(id);
	}
	
	@Transactional
	public DecisionDtoFull decisionGetDto(Long id) {
		return decisionDao.get(id).toDto();
	}
	@Transactional
	public void decisionRealmAddVoterToAll(Long projectId,Long userId,double maxWeight) {
		/* Add a voter to all the realms of a project */
		
		List<DecisionRealm> realms = decisionRealmDao.getAllOfProject(projectId);
		
		for(DecisionRealm realm : realms) {
			Voter existingVoter = decisionRealmDao.getVoter(realm.getId(), userId);
			if(existingVoter == null) {
				Voter newVoter = new Voter();
				newVoter.setVoterUser(userDao.get(userId));
				newVoter.setMaxWeight(maxWeight);
				newVoter.setRealm(realm);
				newVoter.setScale(1.0);
				
				realm.setWeightTot(realm.getWeightTot() + newVoter.getActualWeight());
				
				voterDao.save(newVoter);
				decisionRealmDao.save(realm);
			}
		}
	}
	
	@Transactional
	public void decisionRealmInitAllSupergoalsToProject(Long projectId) {
		List<Goal> superGoals = goalDao.getSuperGoalsOnly(projectId);
		for(Goal goal : superGoals) {
			Long realmId = decisionRealmDao.getIdFromGoalId(goal.getId());
			decisionRealmInitToProject(realmId, projectId);
		}
	}
	
	@Transactional
	public void decisionRealmInitToProject(Long realmId, Long projectId) {
		DecisionRealm realm = decisionRealmDao.get(realmId);
		decisionRealmInitToProject(realm,projectId);
		decisionRealmDao.save(realm);
	}
	
	@Transactional
	public void decisionRealmInitToProject(DecisionRealm destRealm, Long projectId) {
		/* the realm voters are deleted and refilled to be the project contributors 
		 * with scale 1.0 */
		Project project = projectDao.get(projectId);
		
		/* make sure the realm is empty */
		if(destRealm.getVoters() == null) { destRealm.setVoters(new ArrayList<Voter>()); }
		if(destRealm.getVoters().size() > 0) { destRealm.getVoters().clear(); }
		
		/* TODO: delete all voters before recrating them, for safety? */
		
		/* keep track of the weight to store the sum */
		double weightSum = 0.0;
		
		for(Contributor contributor : project.getContributors()) {
			Voter newVoter = new Voter();
			
			newVoter.setRealm(destRealm);
			newVoter.setVoterUser(contributor.getContributorUser());
			newVoter.setMaxWeight(contributor.getPps());
			newVoter.setScale(1.0);
			
			weightSum += newVoter.getActualWeight();
			
			voterDao.save(newVoter);
		}
		
		destRealm.setWeightTot(weightSum);
		decisionRealmDao.save(destRealm);
	}

	
	
	@Transactional
	public void decisionRealmInitToOther(DecisionRealm destRealm,Long sourceRealmId) {
		/* the destRealm of voters is created based on the sourceRealmId. */
		
		DecisionRealm sourceRealm = decisionRealmDao.get(sourceRealmId);
		
		/* TODO: delete all voters before recreating them, for safety? */
		
		/* keep track of the weight to store the sum */
		double weightSum = 0.0;
		
		for(Voter voter : sourceRealm.getVoters()) {
			Voter newVoter = new Voter();
			
			newVoter.setRealm(destRealm);
			newVoter.setVoterUser(voter.getVoterUser());
			newVoter.setMaxWeight(voter.getMaxWeight());
			newVoter.setScale(voter.getScale());
			
			weightSum += newVoter.getActualWeight();
			
			voterDao.save(newVoter);
		}
		
		destRealm.setWeightTot(weightSum);
		decisionRealmDao.save(destRealm);
	}
	
	@Transactional
	public void decisionRealmUpdateToOther(DecisionRealm destRealm,Long sourceRealmId) {
		/* the destRealm of voters is updated based on the sourceRealmId. It assumes that
		 * all the voters in the dest realm are in the source realm */
		
		DecisionRealm sourceRealm = decisionRealmDao.get(sourceRealmId);
		
		/* keep track of the weight to store the sum */
		double weightSum = 0.0;
		
		for(Voter sourceVoter : sourceRealm.getVoters()) {
			for(Voter destVoter : destRealm.getVoters()) {
				if(destVoter.getVoterUser().getId() == sourceVoter.getVoterUser().getId()) {
					
					destVoter.setVoterUser(sourceVoter.getVoterUser());
					destVoter.setMaxWeight(sourceVoter.getMaxWeight());
					destVoter.setScale(sourceVoter.getScale());
					
					weightSum += destVoter.getActualWeight();
					
					voterDao.save(destVoter);
				}
			}
		}
		
		destRealm.setWeightTot(weightSum);
		decisionRealmDao.save(destRealm);
	}
	
	@Transactional
	public void updateVoterInProject(Long projectId, Long userId) {
		/* Goes all over the decision realms in a project and updates the max weight of a 
		 * user by setting it to the user pps in the project.
		 * It starts from the super-goals (those without parent goal) and then continues
		 * iteratively with the sub-goals */
		
		List<Goal> superGoals = goalDao.getSuperGoalsOnly(projectId);
		Contributor ctrb = projectDao.getContributor(projectId, userId);
		
		if(ctrb != null) {
			for(Goal superGoal:superGoals) {
				
				DecisionRealm realm = decisionRealmDao.getFromGoalId(superGoal.getId());
				Voter voter = decisionRealmDao.getVoter(realm.getId(), userId);
				
				/* update the max weight to the user pps */
				double originalWeight = voter.getMaxWeight();
				double newWeight = ctrb.getPps();
				
				voter.setMaxWeight(newWeight);
				voterDao.save(voter);
				
				/* update the total pps aggregate */
				realm.setWeightTot(realm.getWeightTot() + (newWeight - originalWeight));
				
				/* and update the weight of the theses of that user (it will have immediate
				 * effect in the decision algorithm) */
				List<Thesis> theses = thesisDao.getOfUserInRealm(realm.getId(), userId);
				
				for(Thesis thesis : theses) {
					thesis.setWeight(voter.getActualWeight());
					thesisDao.save(thesis);
				}
			
				/* now go over all subgoals and update realms and theses */
				List<Goal> subGoals = goalDao.getSubgoalsIteratively(superGoal.getId());
				
				for(Goal subGoal : subGoals) {
					updateVoterInGoal(subGoal.getId(), userId);
				}
			}
		}
	}
	
	@Transactional
	public void updateVoterInGoal(Long goalId, Long userId) {
		/* Goes all over the decision realms in a goal and subgoals (recursively) and 
		 * update the user. It starts from the top goal and then continues recursively 
		 * with the sub-goals */
		
		Goal goal = goalDao.get(goalId);
		
		DecisionRealm parentRealm = decisionRealmDao.getFromGoalId(goal.getParent().getId());
		Voter voterInParent = decisionRealmDao.getVoter(parentRealm.getId(), userId);
		
		DecisionRealm goalRealm = decisionRealmDao.getFromGoalId(goal.getId());
		Voter voterInGoal = decisionRealmDao.getVoter(goalRealm.getId(), userId);
		
		/* if user is in realm */
		if(voterInGoal != null) {
			/* update the max weight to the weight of the user in the parent goal */
			double originalWeight = voterInGoal.getMaxWeight();
			double newWeight = voterInParent.getActualWeight();
			
			voterInGoal.setMaxWeight(newWeight);
			voterDao.save(voterInGoal);
			
			/* update the total weight aggregate. Adds the delta instead of summing again, 
			 * TODO: safe enough? */
			goalRealm.setWeightTot(goalRealm.getWeightTot() + (newWeight - originalWeight));
			
			/* and update the weight of the theses of that user (it will have immediate
			 * effect in the decision algorithm) */
			List<Thesis> theses = thesisDao.getOfUserInRealm(goalRealm.getId(), userId);
			
			for(Thesis thesis : theses) {
				thesis.setWeight(voterInGoal.getActualWeight());
				thesisDao.save(thesis);
			}
		}
		
		/* now go over all subgoals and update realms and theses (RECUERSIVE) */
		List<Goal> subGoals = goalDao.getSubgoalsIteratively(goal.getId());
		
		for(Goal subGoal : subGoals) {
			updateVoterInGoal(subGoal.getId(), userId);
		}
	}
	
	@Transactional
	public ArgumentDto argumentGetDto(Long id) {
		return argumentDao.get(id).toDto();
	}

	@Transactional
	public List<ArgumentDto> argumentGetOfDecisionDto(Long decisionId, ArgumentTendency tendency) {
		List<Argument> arguments = argumentDao.getOfDecision(decisionId,tendency);
		List<ArgumentDto> argumentDtos = new ArrayList<ArgumentDto>();
		for (Argument argument : arguments) {
			argumentDtos.add(argument.toDto());
		}
		return argumentDtos;
	}


	@Transactional
	public String argumentCreate(ArgumentDto argumentDto) throws IOException {

		User creator = userDao.get(argumentDto.getCreatorUsername());
		Decision decision = decisionDao.get(argumentDto.getDecisionId());
		Argument argument = new Argument();

		argument.setCreationDate(new Timestamp(System.currentTimeMillis()));
		argument.setCreator(creator);
		argument.setDecision(decision);
		argument.setDescription(argumentDto.getDescription());
		argument.setTendency(ArgumentTendency.valueOf(argumentDto.getTendency()));

		decision.getArguments().add(argument);

		argumentDao.save(argument);
		decisionDao.save(decision);

		Activity act = new Activity();
		act.setCreationDate(new Timestamp(System.currentTimeMillis()));
		act.setArgument(argument);
		act.setType(ActivityType.ARGUMENT);
		act.setProject(argument.getDecision().getProject());
		act.setEvent("created");
		activitySaveAndNotify(act);

		return "argument created";
	}

	@Transactional
	public String argumentBack(Long argId, Long userId) {
		User user = userDao.get(userId);
		return argumentDao.back(argId,user);
	}

	@Transactional
	public String argumentUnBack(Long argId, Long userId) {
		User user = userDao.get(userId);
		return argumentDao.unBack(argId,user);
	}

	@Transactional
	public boolean argumentIsBacked(Long argId, Long userId) {
		if(argumentDao.getBacker(argId,userId) == null) {
			return false;
		} else {
			return true;
		}
	}

	@Transactional
	public String reviewBidCreate(ReviewDto reviewDto) {

		User creator = userDao.get(reviewDto.getCreatorUsername());
		Bid bid = bidDao.get(reviewDto.getBidId());

		if(bidDao.getReviewer(reviewDto.getBidId(), creator.getId()) == null) {
			User reviewee = bid.getCreator();

			Review review = new Review();

			review.setCreationDate(new Timestamp(System.currentTimeMillis()));
			review.setCreator(creator);
			review.setReviewee(reviewee);
			review.setDescription(reviewDto.getDescription());
			review.setRate(reviewDto.getRate());

			bid.getReviews().add(review);

			bidDao.save(bid);
			reviewDao.save(review);

			return "review created";
		} else {
			return "user has already reviewed this bid";
		}


	}

	@Transactional
	public ActivityDtoListRes activityDtoGetFiltered(Filters filters) {
		ObjectListRes<Activity> activityRes = activityDao.get(filters);

		ActivityDtoListRes activityDtosRes = new ActivityDtoListRes();

		activityDtosRes.setResSet(activityRes.getResSet());
		activityDtosRes.setActivityDtos(new ArrayList<ActivityDto>());

		for(Activity activity : activityRes.getObjects()) {
			activityDtosRes.getActivityDtos().add(activity.toDto());
		}

		return activityDtosRes;
	}
	
	@Transactional
	public void activitySaveAndNotify(Activity act) throws IOException {
		activityDao.save(act);
		
		String subject = "CollectiveOne - "+act.getProject().getName()+" activity";
	    String body = act.getPrettyMessage(env.getProperty("collectiveone.webapp.baseurl"));
	    
	    List<String> subscribedUsers = mailSubscriptionRepository.getSubscribedAddresses(act.getProject().getId());
	    
	    mailService.sendMail(
	    		subscribedUsers, 
	    		subject, 
	    		body);
	}


	@Transactional
	public ResStatus commentCbtionCreate(CommentDto commentDto) throws IOException {

		User creator = userDao.get(commentDto.getCreatorUsername());
		Cbtion cbtion = cbtionDao.get(commentDto.getCbtionId());

		Comment parent = null;

		if(commentDto.getParentId() != null) {
			parent = commentDao.get(commentDto.getParentId());
		}
		
		Comment comment = new Comment();

		comment.setCreationDate(new Timestamp(System.currentTimeMillis()));
		comment.setCreator(creator);
		comment.setContent(commentDto.getContent());


		if(parent != null) {
			parent.getReplies().add(comment);
			comment.setParent(parent);
			commentDao.save(parent);
		} else {
			cbtion.getComments().add(comment);
		}

		comment.setCbtion(cbtion);

		cbtionDao.save(cbtion);
		commentDao.save(comment);

		Activity act = new Activity();
		act.setCreationDate(new Timestamp(System.currentTimeMillis()));
		act.setComment(comment);
		act.setType(ActivityType.COMMENT);
		act.setProject(cbtion.getProject());
		act.setEvent("new");
		activitySaveAndNotify(act);
		
		ResStatus resStatus = new ResStatus();

		resStatus.setSuccess(true);
		resStatus.setMsg("comment saved");

		return resStatus;
	}

	@Transactional
	public List<CommentDto> commentGetRepliesDtos(Long commentId) {
		List<Comment> replies = commentDao.getRepliesSorted(commentId);
		List<CommentDto> repliesDtos = new ArrayList<CommentDto>();
		for (Comment comment : replies) {
			repliesDtos.add(comment.toDto());
		}
		return repliesDtos;
	}


	@Transactional
	public ResStatus commentPromote(Long commentId, Long userId, boolean promoteUp) {
		ResStatus resStatus = new ResStatus();

		Comment comment = commentDao.get(commentId);
		commentDao.save(comment);

		Promoter promoter = promoterDao.getOfComment(commentId, userId);

		/* countPromotersDiff will not include the current transaction so this logic 
		 * is used to solve the problem*/
		
		int currentRelevance = commentDao.countPromotersDiff(commentId);
		int newRelevance = currentRelevance;
		
		if(promoter == null) {
			promoter = new Promoter();
			promoter.setUser(userDao.get(userId));
			comment.getPromoters().add(promoter);
			if(promoteUp) {
				newRelevance = newRelevance + 1;
			} else {
				newRelevance = newRelevance - 1;
			}
		} else {
			if(promoter.getPromoteUp() !=  promoteUp) {
				if(promoteUp) {
					newRelevance = newRelevance + 2;
				} else {
					newRelevance = newRelevance - 2;
				}
			}
		}

		promoter.setCreationDate(new Timestamp(System.currentTimeMillis()));
		promoter.setPromoteUp(promoteUp);
		
		promoterDao.save(promoter);

		if(promoteUp) resStatus.setMsg("comment promoted up");
		else resStatus.setMsg("comment promoted down");

		/* update relevance to order results */
		comment.setRelevance(newRelevance);

		resStatus.setSuccess(true);

		return resStatus;		
	}

}


