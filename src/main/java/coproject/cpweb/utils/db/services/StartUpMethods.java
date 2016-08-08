package coproject.cpweb.utils.db.services;

import java.sql.Timestamp;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import coproject.cpweb.utils.db.entities.dtos.ProjectDto;

@Component
public class StartUpMethods {

	/* Services  */
	@Autowired
	DbServicesImp dbServices;
	
	public DbServicesImp getDbServices() {
		return dbServices;
	}

	public void setDbServices(DbServicesImp dbServices) {
		this.dbServices = dbServices;
	}
	
	@PostConstruct
	public void UpdateDecisionsStatus() {
		/*
		System.out.println("Filling DB");
		
		dbServices.userCreate("pepoospina","12345678");
				
		ProjectDto projectDto1 = new ProjectDto();
		projectDto1.setName("CoProjects");
		projectDto1.setDescription("Automatically built");
		projectDto1.setCreationDate(new Timestamp(System.currentTimeMillis()));
		projectDto1.setCreatorUsername("pepoospina");
		
		dbServices.projectCreate(projectDto1);
		*/
	}
}
