package coproject.cpweb.utils.db.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import coproject.cpweb.actions.json.ResStatus;
import coproject.cpweb.utils.db.daos.*;
import coproject.cpweb.utils.db.entities.*;
import coproject.cpweb.utils.db.entities.dtos.ActivityDto;
import coproject.cpweb.utils.db.entities.dtos.ArgumentDto;
import coproject.cpweb.utils.db.entities.dtos.BidDto;
import coproject.cpweb.utils.db.entities.dtos.CbtionDto;
import coproject.cpweb.utils.db.entities.dtos.CommentDto;
import coproject.cpweb.utils.db.entities.dtos.DecisionDto;
import coproject.cpweb.utils.db.entities.dtos.GoalDto;
import coproject.cpweb.utils.db.entities.dtos.ProjectDto;
import coproject.cpweb.utils.db.entities.dtos.ReviewDto;
import coproject.cpweb.utils.db.entities.dtos.ThesisDto;
import coproject.cpweb.utils.db.entities.dtos.UserDto;

@Service
@Component(value="dbServices")
public class DbServicesImp {

	@Autowired
	protected SessionDao sessionDao;

	@Autowired
	protected CbtionDao cbtionDao;
	
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
	protected ArgumentDao argumentDao;

	@Autowired 
	protected ReviewDao reviewDao;
	
	@Autowired 
	protected PromoterDao promoterDao;
	
	@Autowired
	protected ActivityDao activityDao;
	
	@Autowired
	protected CommentDao commentDao;
	


	@Transactional
	public void userSave(User user) {
		userDao.save(user);
	}

	@Transactional
	public String userCreate(String username, String password) {
		User userInDB = userDao.get(username);
		if(userInDB == null) {
			User user = new User();
			user.setUsername(username);
			user.setJoindate(new Timestamp(System.currentTimeMillis()));
			user.setPassword(password);
			userDao.save(user);
			return "user created";
		} else {
			return "username already exists";
		}

	}

	@Transactional
	public User userGet(Integer id) {
		return userDao.get(id);
	}

	@Transactional
	public int userGetN() {
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
	public UserDto userLoginDto(String username, String password) {
		User user = userDao.get(username);
		if(user.getPassword().equals(password)) {
			return user.toDto();
		} else {
			return null;
		}
	}

	@Transactional
	public double userPpointsInProjectGet(int userId, int projectId) {

		double ppoints = 0;

		List<Cbtion> cbtionsAccepted = cbtionDao.getAcceptedOfUserInProject(userId, projectId);

		for(Cbtion cbtion : cbtionsAccepted) {
			ppoints += cbtion.getAssignedPpoints();
		}

		return ppoints;
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
	public void projectCreate(ProjectDto projectDto) {

		Project project = new Project();
		projectDao.save(project);

		User creator = userDao.get(projectDto.getCreatorUsername());

		project.setName(projectDto.getName());
		project.setCreator(creator);
		project.setCreationDate(new Timestamp(System.currentTimeMillis()));
		project.setDescription(projectDto.getDescription());

		/* Each project has its own decision realm */
		DecisionRealm realm = new DecisionRealm();
		decisionRealmDao.save(realm);
		realm.setProject(project);
	}
	
	@Transactional
	public void projectStart(String projectName) {
		
		User coprojects = userDao.get("coprojects");
		userDao.save(coprojects);
		
		Project project = projectDao.get(projectName);
		projectDao.save(project);
		
		User creator = project.getCreator();
		userDao.save(creator);
		
		DecisionRealm realm = decisionRealmDao.getFromProjectId(project.getId());
		decisionRealmDao.save(realm);
		
		/* An accepted cbtion is added to the project to the contributor */
		Cbtion cbtion = new Cbtion();

		cbtion.setCreator(creator);
		creator.getCbtionsCreated().add(cbtion);
		
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
		bid.setPpoints(100.0);
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
		assign_bid.setType(DecisionType.BID);
		assign_bid.setBid(bid);
		
		accept_bid.setCreator(coprojects);
		accept_bid.setCreationDate(new Timestamp(System.currentTimeMillis()));
		accept_bid.setDescription("accept bid to cbtion:"+bid.getCbtion().getTitle()+" by:"+bid.getCreator().getUsername());
		accept_bid.setVerdict(1);
		accept_bid.setState(DecisionState.CLOSED_ACCEPTED);
		accept_bid.setDecisionRealm(realm);
		accept_bid.setFromState(BidState.ASSIGNED.toString());
		accept_bid.setToState(BidState.ACCEPTED.toString());
		accept_bid.setProject(project);
		accept_bid.setType(DecisionType.BID);
		accept_bid.setBid(bid);

		/* simulate the bid acceptance process */
		bid.setState(BidState.ACCEPTED);

		cbtion.setAssignedPpoints(bid.getPpoints());
		cbtion.setContributor(bid.getCreator());
		cbtion.setState(CbtionState.ACCEPTED);

		creator.getCbtionsAccepted().add(cbtion);
		userDao.addProjectContributed(creator.getId(),project.getId());

		project.getCbtions().add(cbtion);
		project.getCbtionsAccepted().add(cbtion);
		projectDao.addContributor(creator.getId(), project.getId());

		/* add user to decision realm */
		voterDao.updateOrAdd(realm.getId(), creator.getId(), bid.getPpoints());

	}

	@Transactional
	public Project projectGet(Integer id) {
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
		dto.setPpsTot(projectGetPpsTot(project.getId()));
		return dto;
	}

	@Transactional
	public List<String> projectGetList() {
		return projectDao.getList();
	}

	@Transactional
	public List<Project> projectGetAll(Integer max) {
		return projectDao.getFromRef(new Project(), max);
	}

	@Transactional
	public double projectGetPpsTot(int projectId) {
		return projectDao.projectGetPpsTot(projectId);
	}
	
	@Transactional
	public int goalCreate(GoalDto goalDto) {
		Goal goal = new Goal();
		Project project = projectDao.get(goalDto.getProjectName());
		projectDao.save(project);
		
		goal.setCreationDate(new Timestamp(System.currentTimeMillis()));
		goal.setCreator(userDao.get(goalDto.getCreatorUsername()));
		goal.setDescription(goalDto.getDescription());
		goal.setProject(project);
		goal.setState(GoalState.PROPOSED);
		goal.setGoalTag(goalDto.getGoalTag());
		
		int id = goalDao.save(goal);
		
		if(goalDto.getParentGoalsTags().size() > 0) {
			for(String parentGoalTag : goalDto.getParentGoalsTags()) {
				Goal parentGoal = goalDao.get(parentGoalTag);
				goalDao.addSubGoal(parentGoal.getId(),goal.getId());
				goalDao.save(parentGoal);
			}
		}
		
		DecisionRealm realm = decisionRealmDao.getFromProjectId(project.getId());
		decisionRealmDao.save(realm);
		
		Decision create = new Decision();
		Decision delete = new Decision();
		
		create.setCreator(userDao.get("coprojects"));
		create.setCreationDate(new Timestamp(System.currentTimeMillis()));
		create.setDescription("create goal '"+goal.getGoalTag()+"'");
		create.setState(DecisionState.IDLE);
		create.setVerdictHours(36);
		create.setDecisionRealm(realm);
		create.setFromState(GoalState.PROPOSED.toString());
		create.setToState(GoalState.ACCEPTED.toString());
		create.setProject(project);
		create.setType(DecisionType.GOAL);
		create.setGoal(goal);
		
		delete.setCreator(userDao.get("coprojects"));
		delete.setCreationDate(new Timestamp(System.currentTimeMillis()));
		delete.setDescription("delete goal '"+goal.getGoalTag()+"'");
		delete.setState(DecisionState.IDLE);
		delete.setVerdictHours(36);
		delete.setDecisionRealm(realm);
		delete.setFromState(GoalState.ACCEPTED.toString());
		delete.setToState(GoalState.DELETED.toString());
		delete.setProject(project);
		delete.setType(DecisionType.GOAL);
		delete.setGoal(goal);
		
		goal.setCreateDec(create);
		goal.setDeleteDec(delete);
		
		decisionDao.save(create);
		decisionDao.save(delete);
		
		Activity act = new Activity("proposed", 
				new Timestamp(System.currentTimeMillis()),
				project);
		
		act.setGoal(goal);
		act.setType(ActivityType.GOAL);
		activityDao.save(act);
		
		return id;
	}
	
	@Transactional
	public GoalDto goalGetDto(int goalId) {
		return goalDao.get(goalId).toDto();
	}
	
	@Transactional
	public GoalDto goalGetDto(String goalTag) {
		Goal goal = goalDao.get(goalTag);
		return goal.toDto();
	}
	
	@Transactional
	public GoalDtoListRes goalDtoGetFiltered(Filters filters) {
		ObjectListRes<Goal> goalsRes = goalDao.get(filters);

		GoalDtoListRes goalsDtosRes = new GoalDtoListRes();

		goalsDtosRes.setResSet(goalsRes.getResSet());
		goalsDtosRes.setGoalDtos(new ArrayList<GoalDto>());

		for(Goal goal : goalsRes.getObjects()) {
			goalsDtosRes.getGoalDtos().add(goal.toDto());
		}
		
		return goalsDtosRes;
	}
	
	@Transactional
	public List<String> goalGetSuggestions(String query, String projectName) {
		return goalDao.getSuggestions(query, projectDao.get(projectName).getId());
	}
	
	@Transactional
	public void goalsUpdateState() {
		/* Update state of all not closed bids */
		List<Goal> goalsNotDeleted = goalDao.getNotDeleted();
		for(Goal goal : goalsNotDeleted) {
			goalUpdateState(goal.getId());
		}	
	}
	
	@Transactional
	public void goalUpdateState(int goalId) {
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
		
		switch(create.getState()){
		case OPEN: 
			break;

		case CLOSED_DENIED : 
			switch(goal.getState()) {
			case PROPOSED:
				goal.setState(GoalState.NOT_ACCEPTED);
				act.setEvent("not accepted");
				activityDao.save(act);
				break;

			default:
				break;
			}

			break;

		case CLOSED_ACCEPTED: 
			switch(goal.getState()) {
			case PROPOSED:
				goal.setState(GoalState.ACCEPTED);
				act.setEvent("accepted");
				activityDao.save(act);
				break;
			default:
				break;
			}

		default:
			break;
		} 

		/* Update accept decision */ 
		Decision delete = goal.getDeleteDec();

		/* update goal state based on delete decision */

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
				activityDao.save(act);
				break;
				
			case ACCEPTED:
				goal.setState(GoalState.DELETED);
				act.setEvent("deleted");
				activityDao.save(act);
				break;				

			default:
				break;
			}

			break;

		default:
			break;
		} 
	}
	
	@Transactional
	public void cbtionSave(Cbtion cbtion) {
		cbtionDao.save(cbtion);
	}

	@Transactional
	public int cbtionCreate(CbtionDto cbtionDto) {
		Cbtion cbtion = new Cbtion();
		Project project = projectDao.get(cbtionDto.getProjectName());
		projectDao.save(project);
		Goal goal = goalDao.get(cbtionDto.getGoalTag());
		
		cbtion.setCreationDate(new Timestamp(System.currentTimeMillis()));
		cbtion.setCreator(userDao.get(cbtionDto.getCreatorUsername()));
		cbtion.setDescription(cbtionDto.getDescription());
		cbtion.setProject(project);
		cbtion.setProduct(cbtionDto.getProduct());
		cbtion.setState(CbtionState.PROPOSED);
		cbtion.setTitle(cbtionDto.getTitle());
		cbtion.setGoal(goal);
		
		int id = cbtionDao.save(cbtion);
		
		DecisionRealm realm = decisionRealmDao.getFromProjectId(project.getId());
		decisionRealmDao.save(realm);
		
		Decision open = new Decision();
		
		open.setCreator(userDao.get("coprojects"));
		open.setCreationDate(new Timestamp(System.currentTimeMillis()));
		open.setDecisionRealm(realm);
		open.setDescription("open contribution '"+cbtion.getTitle()+"'");
		open.setFromState(CbtionState.PROPOSED.toString());		
		open.setToState(CbtionState.OPEN.toString());
		open.setProject(project);
		open.setState(DecisionState.IDLE);
		open.setVerdictHours(36);
		open.setType(DecisionType.CBTION);
		open.setCbtion(cbtion);
		
		cbtion.setOpenDec(open);
		decisionDao.save(open);
		
		Activity act = new Activity("created", 
				new Timestamp(System.currentTimeMillis()),
				cbtion.getProject());
		
		act.setCbtion(cbtion);
		act.setType(ActivityType.CBTION);
		activityDao.save(act);
		
		return id;
	}

	@Transactional
	public Cbtion cbtionGet(Integer id) {
		return cbtionDao.get(id);
	}

	@Transactional
	public List<Cbtion> cbtionGet(Cbtion refCbtion) {
		return cbtionDao.get(refCbtion);
	}

	@Transactional
	public CbtionDto cbtionGetDto(int cbtionId) {
		return cbtionDao.get(cbtionId).toDto();
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
			cbtionsDtosRes.getCbtionsDtos().add(cbtion.toDto());
		}

		return cbtionsDtosRes;
	}
	
	@Transactional
	public void cbtionsUpdateState() {
		/* Update state of all not closed bids */
		List<Cbtion> cbtionsProposed = cbtionDao.getWithState(CbtionState.PROPOSED);
		for(Cbtion cbtion : cbtionsProposed) {
			cbtionUpdateState(cbtion.getId());
		}	
	}
	
	@Transactional
	public void cbtionUpdateState(int cbtionId) {
		Cbtion cbtion = cbtionDao.get(cbtionId);
		
		cbtionDao.save(cbtion);
		Decision open = cbtion.getOpenDec();
		
		Activity act = new Activity();
		act.setProject(cbtion.getProject());
		act.setCreationDate(new Timestamp(System.currentTimeMillis()));
		act.setType(ActivityType.CBTION);
		act.setCbtion(cbtion);

		switch(open.getState()){
		case OPEN: 
			break;

		case CLOSED_DENIED : 
			switch(cbtion.getState()) {
			case PROPOSED:
				cbtion.setState(CbtionState.NOTOPENED);
				act.setEvent("proposal refused");
				activityDao.save(act);
				break;

			default:
				break;
			}

			break;

		case CLOSED_ACCEPTED: 
			switch(cbtion.getState()) {
			case PROPOSED:
				cbtion.setState(CbtionState.OPEN);
				act.setEvent("opened for bidding");
				activityDao.save(act);
				break;
			default:
				break;
			}

		default:
			break;
		} 
	}
	
	@Transactional
	public ResStatus cbtionPromote(int cbtionId, int userId, boolean promoteUp) {
		ResStatus resStatus = new ResStatus();
		
		Cbtion cbtion = cbtionDao.get(cbtionId);
		cbtionDao.save(cbtion);
		
		Promoter promoter = promoterDao.getOfCbtion(cbtionId, userId);
		
		if(promoter == null) {
			promoter = new Promoter();
			promoter.setUser(userDao.get(userId));
			cbtion.getPromoters().add(promoter);
		}
		
		promoter.setCreationDate(new Timestamp(System.currentTimeMillis()));
		promoter.setPromoteUp(promoteUp);
		
		if(promoteUp) resStatus.setMsg("contribution promoted up");
		else resStatus.setMsg("contribution promoted down");
		
		cbtion.setRelevance(cbtionDao.countPromotersDiff(cbtionId));
		
		resStatus.setSuccess(true);
		
		return resStatus;		
	}
	
	@Transactional
	public List<CommentDto> cbtionGetCommentsDtos(int cbtionId) {
		List<Comment> comments = cbtionDao.getCommentsSorted(cbtionId);
		List<CommentDto> commentsDtos = new ArrayList<CommentDto>();
		for (Comment comment : comments) {
			commentsDtos.add(comment.toDto());
		}
		return commentsDtos;
	}
	
	@Transactional
	public List<ReviewDto> cbtionGetReviewsDtos(int cbtionId) {
		Bid bid = cbtionDao.getAcceptedBid(cbtionId);
		return bidGetReviewsDtos(bid.getId());
	}

	@Transactional
	public void bidSave(Bid bid, int cbtionId) {
		Cbtion cbtion = cbtionDao.get(cbtionId);
		bid.setCbtion(cbtion);
		cbtion.getBids().add(bid);

		bidDao.save(bid);
		cbtionDao.save(cbtion);

	}
	
	@Transactional
	public Bid bidGet(int id) {
		return bidDao.get(id);
	}
	
	@Transactional
	public BidDto bidGetDto(int id) {
		return bidDao.get(id).toDto();
	}
	
	@Transactional
	public User bidGetCreator(int id) {
		return bidDao.get(id).getCreator();
	}

	@Transactional
	public void bidSave(Bid bid) {
		bidDao.save(bid);
	}
	
	@Transactional
	public void bidSaveState(int bidId, BidState state) {
		Bid bid = bidDao.get(bidId);
		bid.setState(state);
		bidDao.save(bid);
	}

	@Transactional
	public String bidCreate(BidDto bidDtoIn) {

		int cbtionId = bidDtoIn.getCbtionId();
		int userId = bidDtoIn.getCreatorDto().getId();

		Bid bid = bidDao.getOfCbtionAndUser(cbtionId, userId);
		
		if(bid == null) {
			Cbtion cbtion = cbtionDao.get(cbtionId);
			
			if(cbtion.getState() == CbtionState.OPEN) {
			
				Project project = cbtion.getProject();
				projectDao.save(project);
				
				/* create bid only if this user has not created one yet */
				bid = new Bid();
	
				bid.setCreator(userDao.get(userId));
				bid.setCreationDate(new Timestamp(System.currentTimeMillis()));
				bid.setDescription(bidDtoIn.getDescription());
				bid.setDeliveryDate(new Timestamp(bidDtoIn.getDeliveryDate()));
				bid.setCbtion(cbtion);
				bid.setPpoints(bidDtoIn.getPpoints());
				bid.setState(BidState.OFFERED);
	
				bidDao.save(bid);
				
				cbtion.getBids().add(bid);
				cbtionDao.save(cbtion);
	
				DecisionRealm realm = decisionRealmDao.getFromProjectId(bid.getCbtion().getProject().getId());
	
				Decision assign = new Decision();
				assign.setCreator(userDao.get("coprojects"));
				assign.setCreationDate(new Timestamp(System.currentTimeMillis()));
				assign.setDescription("assign contribution '"+bid.getCbtion().getTitle()+"' to "+bid.getCreator().getUsername());
				assign.setFromState(BidState.OFFERED.toString());
				assign.setToState(BidState.ASSIGNED.toString());
				assign.setState(DecisionState.IDLE);
				/* TODO: Include bid duration logic */
				assign.setVerdictHours(36);
				assign.setDecisionRealm(realm);
				assign.setProject(project);
				assign.setType(DecisionType.BID);
				assign.setBid(bid);
	
				Decision accept = new Decision();
				accept.setCreator(userDao.get("coprojects"));
				accept.setCreationDate(new Timestamp(System.currentTimeMillis()));
				accept.setDescription("accept contribution '"+bid.getCbtion().getTitle()+"' as delivered by "+bid.getCreator().getUsername());
				accept.setFromState(BidState.ASSIGNED.toString());
				accept.setToState(BidState.ACCEPTED.toString());
				accept.setState(DecisionState.IDLE);
				/* TODO: Include bid duration logic */
				accept.setVerdictHours(36);
				accept.setDecisionRealm(realm);
				accept.setProject(project);
				accept.setType(DecisionType.BID);
				accept.setBid(bid);
	
				bid.setAssign(assign);
				bid.setAccept(accept);
	
				decisionRealmDao.save(realm);
				decisionDao.save(assign);
				decisionDao.save(accept);
				
				Activity act = new Activity();
				act.setCreationDate(new Timestamp(System.currentTimeMillis()));
				act.setProject(project);
				act.setBid(bid);
				act.setType(ActivityType.BID);
				act.setEvent("created");
				activityDao.save(act);
				
				return " bid created";
				
			} else {
				return " contribution is not open";
			}
		} else {
			return " user already bid to this contribution";
		}
	}

	@Transactional
	public List<BidDto> bidGetOfUserDto(int userId) {
		List<Bid> bids = bidDao.getOfUser(userId);
		List<BidDto> bidDtos = new ArrayList<BidDto>();
		for (Bid bid : bids) {
			bidDtos.add(bid.toDto());
		}
		return bidDtos;
	}

	@Transactional
	public List<BidDto> bidGetOfCbtionDto(int cbtionId) {
		List<Bid> bids = bidDao.getOfCbtion(cbtionId);
		List<BidDto> bidDtos = new ArrayList<BidDto>();
		for (Bid bid : bids) {
			bidDtos.add(bid.toDto());
		}
		return bidDtos;
	}
	
	@Transactional
	public void bidsUpdateState() {
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
	public List<ReviewDto> bidGetReviewsDtos(int bidId) {
		List<Review> reviews = bidDao.get(bidId).getReviews();
		List<ReviewDto> reviewsDtos = new ArrayList<ReviewDto>();
		for (Review review : reviews) {
			reviewsDtos.add(review.toDto());
		}
		return reviewsDtos;
	}
	
	@Transactional
	public void bidUpdateState(int bidId) {
		Bid bid = bidDao.get(bidId);
		Cbtion cbtion = bid.getCbtion();

		bidDao.save(bid);
		cbtionDao.save(cbtion);

		/* Update assign decision */ 
		Decision assign = bid.getAssign();


		Activity act = new Activity();
		act.setCreationDate(new Timestamp(System.currentTimeMillis()));
		act.setBid(bid);
		act.setType(ActivityType.BID);
		act.setProject(cbtion.getProject());
		
		/* update bid state based on assign decision state 
		 * and propagate all consequences of the decision 
		 * outcome */
		
		
		switch(assign.getState()){
		case OPEN: 
			break;

		case CLOSED_DENIED : 
			switch(bid.getState()) {
			case OFFERED:
				bid.setState(BidState.NOT_ASSIGNED);
				act.setEvent("not assigned");
				activityDao.save(act);
				break;

			default:
				break;
			}

			break;

		case CLOSED_ACCEPTED: 
			switch(bid.getState()) {
			case OFFERED:
				bid.setState(BidState.ASSIGNED);
				act.setEvent("assigned");
				activityDao.save(act);
				break;
			default:
				break;
			}

			switch(cbtion.getState()) {
			case OPEN:
				cbtion.setState(CbtionState.ASSIGNED);
				
				break;
			default:
				break;
			}
			break;

		default:
			break;
		} 

		/* Update accept decision */ 
		Decision accept = bid.getAccept();

		/* update bid state based on accept decision state 
		 * and propagate all consequences of the decision 
		 * outcome */

		switch(accept.getState()){
		case OPEN: 
			break;

		case CLOSED_DENIED : 
			switch(bid.getState()) {
			case ASSIGNED:
				bid.setState(BidState.NOT_ACCEPTED);
				act.setEvent("not accepted");
				activityDao.save(act);
				break;

			default:
				break;
			}
			break;

		case CLOSED_ACCEPTED: 
			switch(bid.getState()) {
			case ASSIGNED:
				bid.setState(BidState.ACCEPTED); 

				/* once a bid is accepted, the cbtion and all other bids on it
				 * are closed */

				/* update cbtion state */
				cbtion.setAssignedPpoints(bid.getPpoints());
				cbtion.setContributor(bid.getCreator());
				cbtion.setState(CbtionState.ACCEPTED);

				/* Update project */
				Project project = cbtion.getProject(); 
				project.getCbtionsAccepted().add(cbtion);
				projectDao.addContributor(bid.getCreator().getId(), project.getId());
				projectDao.save(project);
				
				/* Update user creator */
				bid.getCreator().getCbtionsAccepted().add(cbtion);
				userDao.addProjectContributed(bid.getCreator().getId(), cbtion.getProject().getId());
				
				/* close all other bids and decisions */
				for(Bid otherBid : cbtion.getBids()) {
					if(otherBid.getId() != bid.getId()) {
						otherBid.getAssign().setState(DecisionState.CLOSED_EXTERNALLY);
						otherBid.getAccept().setState(DecisionState.CLOSED_EXTERNALLY);
						otherBid.setState(BidState.SUPERSEEDED);
						bidDao.save(otherBid);
					}
				}

				/* add author to decision realm (internally checks if it is already in it) 
				 * the pps are added to the weight of that user in that decision realm, therefore
				 * bookkeeping of pps of user per realm is only taken here */
				voterDao.updateOrAdd(decisionRealmDao.getIdFromProjectId(cbtion.getProject().getId()), 
						bid.getCreator().getId(),
						bid.getPpoints());
				
				
				act.setEvent("accepted");
				activityDao.save(act);
				
				break;
				
			case ACCEPTED:
				break;
				
			default:
				break;
			}
			
			break;

		default:
			break;
		} 
	}
	
	@Transactional
	public ThesisDto thesisOfUser(int decId, int userId) {
		Thesis thesis = thesisDao.getOfUserInDec(decId, userId);
		ThesisDto thesisDto = null;
		if(thesis != null) thesisDto = thesis.toDto();
		return thesisDto;
	}

	@Transactional
	public String thesisOfDecSave(User author, int value, int decId) {

		Decision dec = decisionDao.get(decId);

		if(dec.getState() == DecisionState.IDLE || dec.getState() == DecisionState.OPEN) {
			/* if decision is still open */
			Voter voter = voterDao.getFromUserAndRealm(dec.getDecisionRealm().getId(),author.getId());

			if(voter != null) {
				/* if voter is in the realm of the decision */
				Thesis thesis = decisionDao.getThesisCasted(decId, author.getId());				

				boolean newThesis = false;

				if(thesis == null) {
					thesis = new Thesis();
					thesis.setAuthor(author);
					thesis.setDecision(dec);
					newThesis = true;
				}
				thesisDao.save(thesis);

				thesis.setValue(value);
				thesis.setCastDate(new Timestamp(System.currentTimeMillis()));

				/* weight of the thesis are set at the cast time */
				thesis.setWeight(voter.getWeight());

				/* make a copy of the thesis in the cast theses list */
				if(newThesis) dec.getThesesCast().add(thesis);
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
	public void thesisAssignOfBidSave(User author, int value, int bidId) {
		Bid bid = bidDao.get(bidId);
		Cbtion cbtion = bid.getCbtion();
		if((cbtion.getState() == CbtionState.OPEN) || 
				(cbtion.getState() == CbtionState.ASSIGNED)) {

			thesisOfDecSave(author, value, bid.getAssign().getId());	
		}
	}

	@Transactional
	public void thesisAcceptOfBidSave(User author, int value, int bidId) {
		Bid bid = bidDao.get(bidId);
		Cbtion cbtion = bid.getCbtion();
		if(cbtion.getState() == CbtionState.ASSIGNED) {
			thesisOfDecSave(author, value, bid.getAccept().getId());	
		}
	}

	@Transactional
	public void decisionsUpdateState() {
		
		List<DecisionState> states = new ArrayList<DecisionState>();
		states.add(DecisionState.IDLE);
		states.add(DecisionState.OPEN);
		
		List<Decision> decsIdle = decisionDao.getWithStates(states);
		for(Decision dec : decsIdle) {
			
			/* Update the decision */
			DecisionState before = dec.getState();
			dec.updateState();
			decisionDao.save(dec);
			
			/* store activity if switched state and user created decision */
			if(!dec.getCreator().getUsername().equals("coprojects")) {
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
								activityDao.save(act);	
								break;
								
							case CLOSED_ACCEPTED:
								act.setEvent("accepted");
								activityDao.save(act);	
								break;
								
							case CLOSED_DENIED:
								act.setEvent("rejected");
								activityDao.save(act);	
								break;
								
							case CLOSED_EXTERNALLY:
								act.setEvent("closed externally");
								activityDao.save(act);	
								break;
						}		
								
						break;
						
					case OPEN:
						switch(dec.getState()) {
							case IDLE:
								act.setEvent("back to idle");
								activityDao.save(act);
								break;
								
							case OPEN:
								break;
								
							case CLOSED_ACCEPTED:
								act.setEvent("accepted");
								activityDao.save(act);	
								break;
								
							case CLOSED_DENIED:
								act.setEvent("rejected");
								activityDao.save(act);	
								break;
								
							case CLOSED_EXTERNALLY:
								act.setEvent("closed externally");
								activityDao.save(act);	
								break;
						}	
						break;
						
					case CLOSED_ACCEPTED:
					case CLOSED_DENIED:
					case CLOSED_EXTERNALLY:
						break;
						
				}
			}
		}
	}
	
	@Transactional
	public DecisionDtoListRes decisionDtoGetFiltered(Filters filters) {
		ObjectListRes<Decision> decisionsRes = decisionDao.get(filters);

		DecisionDtoListRes decisionsDtosRes = new DecisionDtoListRes();

		decisionsDtosRes.setResSet(decisionsRes.getResSet());
		decisionsDtosRes.setDecisionDtos(new ArrayList<DecisionDto>());

		for(Decision decision : decisionsRes.getObjects()) {
			decisionsDtosRes.getDecisionDtos().add(decision.toDto());
		}

		return decisionsDtosRes;
	}	
	
	@Transactional
	public String decisionCreate(DecisionDto decisionDto) {
		Decision decision = new Decision();
		Project project = projectDao.get(decisionDto.getProjectName());
		projectDao.save(project);
		
		DecisionRealm realm = decisionRealmDao.getFromProjectId(project.getId());
		decisionRealmDao.save(realm);
		
		decision.setCreationDate(new Timestamp(System.currentTimeMillis()));
		decision.setCreator(userDao.get(decisionDto.getCreatorUsername()));
		decision.setDescription(decisionDto.getDescription());
		decision.setProject(project);
		decision.setState(DecisionState.IDLE);
		decision.setDecisionRealm(realm);
		decision.setType(DecisionType.GENERAL);
		
		decisionDao.save(decision);
		
		if(!decision.getCreator().getUsername().equals("coprojects"))  {
			Activity act = new Activity();
			act.setCreationDate(new Timestamp(System.currentTimeMillis()));
			act.setDecision(decision);
			act.setType(ActivityType.DECISION);
			act.setProject(project);
			act.setEvent("created");
			activityDao.save(act);	
		}
		
		return "decision created";
	}
	
	@Transactional
	public DecisionDto decisionGetDto(int id) {
		return decisionDao.get(id).toDto();
	}
	
	@Transactional
	public ArgumentDto argumentGetDto(int id) {
		return argumentDao.get(id).toDto();
	}
	
	@Transactional
	public List<ArgumentDto> argumentGetOfDecisionDto(int decisionId, ArgumentTendency tendency) {
		List<Argument> arguments = argumentDao.getOfDecision(decisionId,tendency);
		List<ArgumentDto> argumentDtos = new ArrayList<ArgumentDto>();
		for (Argument argument : arguments) {
			argumentDtos.add(argument.toDto());
		}
		return argumentDtos;
	}
	
	
	@Transactional
	public String argumentCreate(ArgumentDto argumentDto) {

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
		activityDao.save(act);
		
		return "argument created";
	}
	
	@Transactional
	public String argumentBack(int argId, int userId) {
		User user = userDao.get(userId);
		return argumentDao.back(argId,user);
	}
	
	@Transactional
	public String argumentUnBack(int argId, int userId) {
		User user = userDao.get(userId);
		return argumentDao.unBack(argId,user);
	}
	
	@Transactional
	public boolean argumentIsBacked(int argId, int userId) {
		if(argumentDao.getBacker(argId,userId) == null) {
			return false;
		} else {
			return true;
		}
	}
	
	@Transactional
	public String reviewBidCreate(ReviewDto reviewDto, int bidId) {

		User creator = userDao.get(reviewDto.getCreatorUsername());
		Bid bid = bidDao.get(bidId);
		
		if(bidDao.getReviewer(bidId, creator.getId()) == null) {
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
	public ResStatus commentCbtionCreate(CommentDto commentDto) {

		User creator = userDao.get(commentDto.getCreatorUsername());
		Cbtion cbtion = cbtionDao.get(commentDto.getCbtionId());
		
		Comment parent = null;
		
		if(commentDto.getParentId() != 0) {
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
		
		ResStatus resStatus = new ResStatus();
		
		resStatus.setSuccess(true);
		resStatus.setMsg("comment saved");
		
		return resStatus;
	}
	
	@Transactional
	public List<CommentDto> commentGetRepliesDtos(int commentId) {
		List<Comment> replies = commentDao.getRepliesSorted(commentId);
		List<CommentDto> repliesDtos = new ArrayList<CommentDto>();
		for (Comment comment : replies) {
			repliesDtos.add(comment.toDto());
		}
		return repliesDtos;
	}
	
	
	@Transactional
	public ResStatus commentPromote(int commentId, int userId, boolean promoteUp) {
		ResStatus resStatus = new ResStatus();
		
		Comment comment = commentDao.get(commentId);
		commentDao.save(comment);
		
		Promoter promoter = promoterDao.getOfComment(commentId, userId);
		
		if(promoter == null) {
			promoter = new Promoter();
			promoter.setUser(userDao.get(userId));
			comment.getPromoters().add(promoter);
		}
		
		promoter.setCreationDate(new Timestamp(System.currentTimeMillis()));
		promoter.setPromoteUp(promoteUp);
		
		if(promoteUp) resStatus.setMsg("comment promoted up");
		else resStatus.setMsg("comment promoted down");
		
		/* update relevance to order results */
		comment.setRelevance(commentDao.countPromotersDiff(commentId));
		
		resStatus.setSuccess(true);
		
		return resStatus;		
	}
	
}


 