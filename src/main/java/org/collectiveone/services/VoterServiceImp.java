package org.collectiveone.services;

import java.util.List;

import org.collectiveone.model.Contributor;
import org.collectiveone.model.DecisionRealm;
import org.collectiveone.model.Goal;
import org.collectiveone.model.Thesis;
import org.collectiveone.model.Voter;
import org.collectiveone.repositories.DecisionRealmDao;
import org.collectiveone.repositories.GoalDao;
import org.collectiveone.repositories.ProjectDao;
import org.collectiveone.repositories.ThesisDao;
import org.collectiveone.repositories.VoterDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class VoterServiceImp {
	
	@Autowired
	protected GoalDao goalDao;

	@Autowired
	protected ProjectDao projectDao;
	
	@Autowired
	protected ThesisDao thesisDao;

	@Autowired 
	protected VoterDao voterDao;
	
	@Autowired 
	protected DecisionRealmDao decisionRealmDao;
	
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
				
				/* update the maxWeight and actualWeight keeping the proportion
				 * in case the users has decided to manually change it */
				double scale = voter.getActualWeight()/voter.getMaxWeight();
				voter.setMaxWeight(ctrb.getPps());
				voter.setActualWeight(scale*ctrb.getPps());
				voterDao.save(voter);
				
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
			
			/* update the maxWeight and actualWeight keeping the proportion
			 * in case the users has decided to manually change it. */
			double scale = voterInGoal.getActualWeight()/voterInGoal.getMaxWeight();
			voterInGoal.setMaxWeight(voterInParent.getMaxWeight());
			voterInGoal.setActualWeight(scale*voterInParent.getMaxWeight());
			voterDao.save(voterInGoal);
			
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

}
