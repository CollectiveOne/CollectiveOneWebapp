package org.collectiveone.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PeriodicMethods {

	/* Services  */
	@Autowired
	protected DecisionServiceImp decisionService;
	
	@Autowired
	protected BidServiceImp bidService;
	
	@Autowired
	protected GoalServiceImp goalService;
	
	@Autowired
	protected CbtionServiceImp cbtionService;
		
	@Scheduled(fixedDelay = 5000)
	public void UpdateDecisionsStatus() throws IOException {
		// System.out.print(".");
		
		/* First update all open or idle decisions */
		decisionService.updateStateAll();
		
		/* then update all entities based on the updated
		 * decisions */
		bidService.updateStateAll();
		goalService.updateStateAll();
		cbtionService.updateStateAll();
		// System.out.print("-");
	}
}
