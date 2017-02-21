package org.collectiveone.services;

import java.util.ArrayList;
import java.util.List;

import org.collectiveone.model.Contributor;
import org.collectiveone.model.DecisionRealm;
import org.collectiveone.model.Goal;
import org.collectiveone.model.Project;
import org.collectiveone.model.Voter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DecisionRealmServiceImp extends BaseService {

	@Transactional
	public void decisionRealmAddVoterToAll(Long projectId,Long userId,double maxWeight) {
		/* Add a voter to all the realms of a project, this is done the first time a contributor
		 * is added to a project */
		
		List<DecisionRealm> realms = decisionRealmDao.getAllOfProject(projectId);
		
		for(DecisionRealm realm : realms) {
			Voter existingVoter = decisionRealmDao.getVoter(realm.getId(), userId);
			if(existingVoter == null) {
				Voter newVoter = new Voter();
				newVoter.setVoterUser(userDao.get(userId));
				newVoter.setRealm(realm);
				newVoter.setMaxWeight(maxWeight);
				newVoter.setActualWeight(maxWeight);
				
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
		
		for(Contributor contributor : project.getContributors()) {
			Voter newVoter = new Voter();
			
			newVoter.setRealm(destRealm);
			newVoter.setVoterUser(contributor.getContributorUser());
			newVoter.setMaxWeight(contributor.getPps());
			newVoter.setActualWeight(contributor.getPps());
			
			voterDao.save(newVoter);
		}
		
		decisionRealmDao.save(destRealm);
	}

	
	
	@Transactional
	public void decisionRealmInitToOther(DecisionRealm destRealm,Long sourceRealmId) {
		/* the destRealm of voters is created based on the sourceRealmId. */
		
		DecisionRealm sourceRealm = decisionRealmDao.get(sourceRealmId);
		
		/* TODO: delete all voters before recreating them, for safety? */
		
		for(Voter voter : sourceRealm.getVoters()) {
			Voter newVoter = new Voter();
			
			newVoter.setRealm(destRealm);
			newVoter.setVoterUser(voter.getVoterUser());
			newVoter.setMaxWeight(voter.getMaxWeight());
			newVoter.setActualWeight(voter.getActualWeight());
			
			voterDao.save(newVoter);
		}
		
		decisionRealmDao.save(destRealm);
	}
	
	@Transactional
	public void decisionRealmUpdateToOther(DecisionRealm destRealm,Long sourceRealmId) {
		/* the destRealm of voters is updated based on the sourceRealmId. It assumes that
		 * all the voters in the dest realm are in the source realm */
		
		DecisionRealm sourceRealm = decisionRealmDao.get(sourceRealmId);
		
		for(Voter sourceVoter : sourceRealm.getVoters()) {
			for(Voter destVoter : destRealm.getVoters()) {
				if(destVoter.getVoterUser().getId() == sourceVoter.getVoterUser().getId()) {
					
					destVoter.setVoterUser(sourceVoter.getVoterUser());
					destVoter.setMaxWeight(sourceVoter.getMaxWeight());
					destVoter.setActualWeight(sourceVoter.getActualWeight());
					
					voterDao.save(destVoter);
				}
			}
		}
		
		decisionRealmDao.save(destRealm);
	}

}
