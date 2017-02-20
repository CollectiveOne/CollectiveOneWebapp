package org.collectiveone.services;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.collectiveone.model.Activity;
import org.collectiveone.model.ActivityType;
import org.collectiveone.model.Decision;
import org.collectiveone.model.DecisionRealm;
import org.collectiveone.model.DecisionState;
import org.collectiveone.model.DecisionType;
import org.collectiveone.model.Goal;
import org.collectiveone.model.GoalAttachState;
import org.collectiveone.model.Project;
import org.collectiveone.model.Thesis;
import org.collectiveone.model.Voter;
import org.collectiveone.repositories.DecisionDao;
import org.collectiveone.repositories.DecisionRealmDao;
import org.collectiveone.repositories.GoalDao;
import org.collectiveone.repositories.ProjectDao;
import org.collectiveone.repositories.UserDao;
import org.collectiveone.web.dto.DecisionDtoCreate;
import org.collectiveone.web.dto.DecisionDtoFull;
import org.collectiveone.web.dto.DecisionDtoListRes;
import org.collectiveone.web.dto.Filters;
import org.collectiveone.web.dto.ObjectListRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class DecisionServiceImp {
	
	@Autowired
	protected DecisionDao decisionDao;

	@Autowired
	protected ProjectDao projectDao;
	
	@Autowired 
	protected DecisionRealmDao decisionRealmDao;
	
	@Autowired
	protected GoalDao goalDao;
	
	@Autowired
	protected UserDao userDao;
	
	@Autowired
	protected TimeServiceImp timeService;
	
	@Autowired
	protected ActivityServiceImp activityService;

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
		dec.updateState(timeService.getNow(),decisionRealmDao.getWeightTot(dec.getDecisionRealm().getId()));
		decisionDao.save(dec);

		/* store activity only for custom decisions (automatic decisions activity 
		 * is recorded based on the element it changes).
		 * Activity is recorded if the decision switch state */
		boolean isCustom = false;
		if(!dec.getCreator().getUsername().equals("collectiveone")) {
			isCustom = true;
		}
		
		boolean autoDecayWeights = false;
		
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
				activityService.activitySaveAndNotify(act);
				break;

			case CLOSED_ACCEPTED:
				act.setEvent("accepted");
				if(isCustom) activityService.activitySaveAndNotify(act);
				autoDecayWeights = true;
				break;

			case CLOSED_DENIED:
				act.setEvent("rejected");
				if(isCustom) activityService.activitySaveAndNotify(act);
				autoDecayWeights = true;
				break;

			case CLOSED_EXTERNALLY:
				act.setEvent("closed externally");
				if(isCustom) activityService.activitySaveAndNotify(act);
				break;
			}		

			break;

		case OPEN:
			switch(dec.getState()) {
			case IDLE:
				act.setEvent("back to idle");
				activityService.activitySaveAndNotify(act);
				break;

			case OPEN:
				break;

			case CLOSED_ACCEPTED:
				act.setEvent("accepted");
				if(isCustom) activityService.activitySaveAndNotify(act);
				autoDecayWeights = true;
				break;

			case CLOSED_DENIED:
				act.setEvent("rejected");
				if(isCustom) activityService.activitySaveAndNotify(act);
				autoDecayWeights = true;
				break;

			case CLOSED_EXTERNALLY:
				act.setEvent("closed externally");
				if(isCustom) activityService.activitySaveAndNotify(act);
				break;
			}	
			break;

		case CLOSED_ACCEPTED:
		case CLOSED_DENIED:
		case CLOSED_EXTERNALLY:
			break;

		}
		
		if(autoDecayWeights) {
			Goal goal = dec.getGoal();
			if(goal.getAttachedState() == GoalAttachState.DETACHED) {
				/* decay is done only if goal is detached */
				DecisionRealm realm = dec.getDecisionRealm();
				List<Voter> votersNotVoted = new ArrayList<Voter>();
				
				for(Voter voter : realm.getVoters()) {
					/* see if the voter voted */
					/* TODO: optimize search? */
					boolean voterVoted = false;
					for(Thesis thesis : dec.getThesesCast()) {
						if(thesis.getAuthor().getId() == voter.getVoterUser().getId()) {
							voterVoted = true;
						}
					}
					
					if(!voterVoted) {
						/* if not voted, then add it to the list*/
						votersNotVoted.add(voter);
					}
				}
				
				for(Voter voterNotVoted : votersNotVoted) {
					double maxWeight = voterNotVoted.getMaxWeight();
					/* voter weight is halved for every decision in which the voter
					 * does not participate */
					double actualWeight = voterNotVoted.getActualWeight()*0.5;
					decisionRealmDao.updateVoter(realm.getId(), voterNotVoted.getVoterUser().getId(), maxWeight, actualWeight);
				}
			}
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
		Goal goal = goalDao.get(decisionDto.getGoalTag(),project.getName());
		
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
			activityService.activitySaveAndNotify(act);
		}

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

}
