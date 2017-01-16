
package org.collectiveone.test;

import java.io.IOException;
import java.sql.Timestamp;

import org.collectiveone.model.Cbtion;
import org.collectiveone.model.CbtionState;
import org.collectiveone.model.Decision;
import org.collectiveone.model.Project;
import org.collectiveone.model.User;
import org.collectiveone.services.DbServicesImp;
import org.collectiveone.services.UserServiceIm;
import org.collectiveone.test.services.TimeServiceTestIm;
import org.collectiveone.web.dto.CbtionDto;
import org.collectiveone.web.dto.DecisionDtoCreate;
import org.collectiveone.web.dto.ProjectNewDto;
import org.collectiveone.web.dto.UserNewDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
public class CollectiveOneWebappApplicationTests {

	@Autowired
	DbServicesImp dbServices;
	
	@Autowired
	UserServiceIm userService;
	
	@Test
	public void testDecisionAlgorithm() throws IOException {
		/* TODO: It would be nice to test this with http requests and not thorugh the service methods directly. 
		*  O was not able to configure spring security for the needs of the tests though*/
		
		/* Create platform user, used by the application as creator automatically created objects */
		UserNewDto platformUser = new UserNewDto();
		platformUser.setEmail("contact@collectiveone.org");
		platformUser.setPassword("1234");
		platformUser.setPasswordConfirm("1234");
		platformUser.setUsername("collectiveone");
		
		userService.emailAuthorize(platformUser.getEmail());
		userService.registerNewUserAccount(platformUser);
		
		/* Create users */
		int nusers = 10;
		for(int ix=0; ix < nusers; ix++) {
			UserNewDto userNew = new UserNewDto();
			
			userNew.setPassword("user"+ix);
			userNew.setPasswordConfirm("user"+ix);
			userNew.setUsername("user"+ix);
			userNew.setEmail("temp"+ix+"@gmail.com");
			
			userService.emailAuthorize(userNew.getEmail());
			userService.registerNewUserAccount(userNew);
		}
		
		/* Activate users s*/
		for(User user : userService.findAll()) {
			userService.enableUser(user.getId());
		}
		
		/* Create project */
		ProjectNewDto projectNew = new ProjectNewDto();
		
		projectNew.setName("testProject");
		projectNew.setCreatorUsername("user0");
		projectNew.setDescription("Dummy description");
		projectNew.setPpsInitial(100);
				
		dbServices.projectCreate(projectNew);
		dbServices.projectStart(projectNew.getName(), projectNew.getPpsInitial());
		
		Project project = dbServices.projectGet(projectNew.getName()); 
		
		/* Create a contribution, a bid and accept it for each */
		for(User user : userService.findAll()) {
			
			/* 100 for each user (exclude project creator and platform) */
			if((!user.getUsername().equals("user0")) && (!user.getUsername().equals("collectiveone"))) {
				/* create contribution */
				CbtionDto cbtionDto = new CbtionDto();
				cbtionDto.setTitle("created for user"+user.getUsername());
				cbtionDto.setDescription("dummy description");
				cbtionDto.setCreatorUsername("user1");
				cbtionDto.setProjectName(projectNew.getName());
				
				Long cbtionId = dbServices.cbtionCreate(cbtionDto);
				
				/* force cbtoin state to accepted and assign pps */
				Cbtion cbtion = dbServices.cbtionGet(cbtionId);
				
				cbtion.setState(CbtionState.ACCEPTED);
				cbtion.setAssignedPpoints(100);
				cbtion.setContributor(user);
				
				dbServices.cbtionUpdate(cbtion);
			}
		}
		
		/* update voter weight */
		for(User user : userService.findAll()) {
			dbServices.voterUpdate(user.getId(),project.getId(),0.0);
		}
		 
		/* test decision */
		DecisionDtoCreate decisionDto = new DecisionDtoCreate();
		decisionDto.setProjectName(project.getName());
		decisionDto.setCreatorUsername("user1");
		decisionDto.setDescription("test decision");
		
		Long decId = dbServices.decisionCreate(decisionDto);
		
		/* prepare time simulation */
		dbServices.setTimeService(new TimeServiceTestIm());
		double votePeriod = 36.0 / (double) nusers * (1000.0 * 60.0 * 60.0);
		
		for(int ix=0; ix < nusers; ix++) {
			
			if(ix == 0) dbServices.thesisOfDecSave(dbServices.userGet("user"+ix), 1, decId);
			dbServices.decisionUpdateState(decId);
			
			Decision dec = dbServices.decisionGet(decId);
			System.out.println("-------------IX "+ix+"------------");
			System.out.println("p_to_flip: "+dec.p_to_flip);
			System.out.println("extFactor: "+dec.extFactor);
			System.out.println("pc_ci_low: "+dec.pc_ci_low);
			System.out.println("pc_ci_low_ext: "+dec.pc_ci_low_ext);
			System.out.println("pc_ci_low_ext_time: "+dec.pc_ci_low_ext_time);
			System.out.println("pc_ci_high: "+dec.pc_ci_high);
			System.out.println("pc_ci_high_ext: "+dec.pc_ci_high_ext);
			System.out.println("pc_ci_high_ext_time: "+dec.pc_ci_high_ext_time);
			System.out.println(dec.getState().toString());
			
			/* simulate time pass */
			Timestamp now = new Timestamp((long) (dbServices.getTimeService().getNow().getTime() + votePeriod));
			dbServices.getTimeService().setNow(now);
			
		}
		
	}

}
