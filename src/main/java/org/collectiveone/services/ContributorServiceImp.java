package org.collectiveone.services;

import org.collectiveone.model.Contributor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class ContributorServiceImp extends BaseService {

	@Transactional 
	public void update(Long projectId, Long userId, double lastOne) {
		/* update or create the contributor pps in project recalculating all accepted
		 * contributions and adding a delta (for cases in which pps are being assigned
		 * during the transaction)  */
		double ppsPrev = userService.ppointsInProjectRecalc(userId, projectId);
		double ppsTot = ppsPrev + lastOne;
		
		Contributor ctrb = contributorRepository.getContributor(projectId, userId);
		if(ctrb == null) {
			/* new contributor in the project */
			decisionRealmService.decisionRealmAddVoterToAll(projectId,userId,ppsTot);
		}
		
		contributorRepository.updateContributor(projectId, userId, ppsTot);
	}
}
