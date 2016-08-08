package coproject.cpweb.utils.db.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PeriodicMethods {

	/* Services  */
	@Autowired
	DbServicesImp dbServices;
	
	public DbServicesImp getDbServices() {
		return dbServices;
	}

	public void setDbServices(DbServicesImp dbServices) {
		this.dbServices = dbServices;
	}
	
	@Scheduled(fixedDelay = 5000)
	public void UpdateDecisionsStatus() {
		//System.out.print(".");
		dbServices.decisionsUpdateState();
		dbServices.bidsUpdateState();
		//System.out.print("-");
	}
}
