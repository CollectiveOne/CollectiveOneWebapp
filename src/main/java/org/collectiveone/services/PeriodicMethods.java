package org.collectiveone.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PeriodicMethods {

	/* Services  */
	@Autowired
	DbServicesImp dbServices;
		
	@Scheduled(fixedDelay = 5000)
	public void UpdateDecisionsStatus() {
		// System.out.print(".");
		dbServices.decisionsUpdateState();
		dbServices.bidsUpdateState();
		dbServices.goalsUpdateState();
		dbServices.cbtionsUpdateState();
		// System.out.print("-");
	}
}
