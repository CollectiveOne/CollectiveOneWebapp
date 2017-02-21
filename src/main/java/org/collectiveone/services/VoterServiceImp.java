package org.collectiveone.services;

import java.util.List;

import org.collectiveone.model.Contributor;
import org.collectiveone.model.DecisionRealm;
import org.collectiveone.model.Goal;
import org.collectiveone.model.Thesis;
import org.collectiveone.model.Voter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoterServiceImp extends BaseService {
	
	@Transactional
	public void updateVoterInProject(Long projectId, Long userId) {
		/* Goes all over the decision realms in a project and updates the max weight of a 
		 * user by setting it to the user pps in the project.
		 * It starts from the super-goals (those without parent goal) and then continues
		 * iteratively with the sub-goals */
		
		List<Goal> superGoals = goalRepository.getSuperGoalsOnly(projectId);
		Contributor ctrb = projectRepository.getContributor(projectId, userId);
		
		if(ctrb != null) {
			for(Goal superGoal:superGoals) {
				
				DecisionRealm realm = decisionRealmRepository.getFromGoalId(superGoal.getId());
				Voter voter = decisionRealmRepository.getVoter(realm.getId(), userId);
				
				/* update the maxWeight and actualWeight keeping the proportion
				 * in case the users has decided to manually change it */
				double scale = voter.getActualWeight()/voter.getMaxWeight();
				voter.setMaxWeight(ctrb.getPps());
				voter.setActualWeight(scale*ctrb.getPps());
				voterRepository.save(voter);
				
				/* and update the weight of the theses of that user (it will have immediate
				 * effect in the decision algorithm) */
				List<Thesis> theses = thesisRepository.getOfUserInRealm(realm.getId(), userId);
				
				for(Thesis thesis : theses) {
					thesis.setWeight(voter.getActualWeight());
					thesisRepository.save(thesis);
				}
			
				/* now go over all subgoals and update realms and theses */
				List<Goal> subGoals = goalRepository.getSubgoalsIteratively(superGoal.getId());
				
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
		
		Goal goal = goalRepository.get(goalId);
		
		DecisionRealm parentRealm = decisionRealmRepository.getFromGoalId(goal.getParent().getId());
		Voter voterInParent = decisionRealmRepository.getVoter(parentRealm.getId(), userId);
		
		DecisionRealm goalRealm = decisionRealmRepository.getFromGoalId(goal.getId());
		Voter voterInGoal = decisionRealmRepository.getVoter(goalRealm.getId(), userId);
		
		/* if user is in realm */
		if(voterInGoal != null) {
			/* update the max weight to the weight of the user in the parent goal */
			
			/* update the maxWeight and actualWeight keeping the proportion
			 * in case the users has decided to manually change it. */
			double scale = voterInGoal.getActualWeight()/voterInGoal.getMaxWeight();
			voterInGoal.setMaxWeight(voterInParent.getMaxWeight());
			voterInGoal.setActualWeight(scale*voterInParent.getMaxWeight());
			voterRepository.save(voterInGoal);
			
			/* and update the weight of the theses of that user (it will have immediate
			 * effect in the decision algorithm) */
			List<Thesis> theses = thesisRepository.getOfUserInRealm(goalRealm.getId(), userId);
			
			for(Thesis thesis : theses) {
				thesis.setWeight(voterInGoal.getActualWeight());
				thesisRepository.save(thesis);
			}
		}
		
		/* now go over all subgoals and update realms and theses (RECUERSIVE) */
		List<Goal> subGoals = goalRepository.getSubgoalsIteratively(goal.getId());
		
		for(Goal subGoal : subGoals) {
			updateVoterInGoal(subGoal.getId(), userId);
		}
	}

}
