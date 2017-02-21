package org.collectiveone.services;

import org.collectiveone.model.Contributor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContributorServiceImp extends BaseService {

	@Transactional
	public void contributorUpdate(Long projectId, Long userId, double lastOne) {
		/* update or create the contributor pps in project recalculating all accepted
		 * contributions and adding a delta (for cases in which pps are being assigned
		 * during the transaction)  */
		double ppsPrev = userService.userPpointsInProjectRecalc(userId, projectId);
		double ppsTot = ppsPrev + lastOne;
		
		Contributor ctrb = contributorDao.getContributor(projectId, userId);
		if(ctrb == null) {
			/* new contributor in the project */
			decisionRealmService.decisionRealmAddVoterToAll(projectId,userId,ppsTot);
		}
		
		contributorDao.updateContributor(projectId, userId, ppsTot);
	}
}
