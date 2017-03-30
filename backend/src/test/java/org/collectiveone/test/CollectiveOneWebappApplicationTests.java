
package org.collectiveone.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.collectiveone.model.Decision;
import org.collectiveone.services.DecisionServiceImp;
import org.collectiveone.services.PeriodicMethods;
import org.collectiveone.services.ProjectServiceImp;
import org.collectiveone.services.UserAuthServiceImp;
import org.collectiveone.test.config.TestConfig;
import org.collectiveone.test.services.TimeServiceTestIm;
import org.collectiveone.web.controllers.BaseController;
import org.collectiveone.web.controllers.rest.DecisionsController;
import org.collectiveone.web.controllers.views.ViewsController;
import org.collectiveone.web.dto.DecisionDtoCreate;
import org.collectiveone.web.dto.ProjectNewDto;
import org.collectiveone.web.dto.ThesisDto;
import org.collectiveone.web.dto.UserNewDto;
import org.collectiveone.web.dto.UsernameAndPps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
@ContextConfiguration(classes={TestConfig.class})
public class CollectiveOneWebappApplicationTests {
	
	@Autowired
	BaseController baseController;
	
	@Autowired
	ViewsController viewsController;
	
	@Autowired
	DecisionsController decisionController;
	
	
	@Autowired
	UserAuthServiceImp userAuthService;
	
	@Autowired
	ProjectServiceImp projectService; 
	
	@Autowired
	DecisionServiceImp decisionService;
	
	@Autowired
	PeriodicMethods periodicMethods;
	
	
	private String projectName = "test-project";
	private String goalTag = "test-goal";
	private int nusers = 10;
	
	private double verdictHours = 100;
	private Long timeStepMs = 10L*60L*60L*1000L;
	private double hoursToMs = 60.0*60*1000;
	
	@Before
	public void setup() throws IOException {
		/* Mockups */
		BindingResult result = mock(BindingResult.class);
	    when(result.hasErrors()).thenReturn(false);
	    
	    Model model = mock(Model.class);
	    HttpServletRequest request = mock(HttpServletRequest.class);
		
		/* Create platform user, used by the application as creator automatically created objects */
		UserNewDto platformUser = new UserNewDto();
		platformUser.setEmail("contact@collectiveone.org");
		platformUser.setPassword("12345678");
		platformUser.setPasswordConfirm("12345678");
		platformUser.setUsername("collectiveone");

		userAuthService.emailAuthorize(platformUser.getEmail());
		userAuthService.registerNewUserAccount(platformUser);
		
		/* create dummy objects */
		Locale locale = new Locale("EN-US");
		
		/* Create users */
		for(int ix=0; ix < nusers; ix++) {
			UserNewDto userNewDto = new UserNewDto();
	
			userNewDto.setPassword("userpassword"+ix);
			userNewDto.setPasswordConfirm("userpassword"+ix);
			userNewDto.setUsername("user"+ix);
			userNewDto.setEmail("temp"+ix+"@gmail.com");
	
			userAuthService.emailAuthorize(userNewDto.getEmail());
			baseController.signupSubmit(locale, userNewDto, result, model, request);
		}
		
		
		/* Create project */
		/* Login one user*/
		UserDetails userDetails = userAuthService.loadUserByUsername("user0");
		Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername (),userDetails.getPassword (),userDetails.getAuthorities ());
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		ProjectNewDto projectDto = new ProjectNewDto();
		
		projectDto.setName(projectName);
		projectDto.setCreatorUsername("user0");
		projectDto.setDescription("Dummy project created for test");
		projectDto.setGoalTag(goalTag);
		projectDto.setGoalDescription("Dummy goal created at project creation");
		
		List<UsernameAndPps> contributorNamesAndPps = new ArrayList<UsernameAndPps>();
		
		for(int ix=0; ix < nusers; ix++) {
			contributorNamesAndPps.add(new UsernameAndPps("user"+ix,100));
		}
		
		projectDto.setUsernamesAndPps(contributorNamesAndPps);
		
		projectService.authorize(projectDto.getName());
	    viewsController.projectNewSubmit(projectDto, result);
	}
	
	@Test
	public void testDecisionAlgorithm01() throws IOException {
		
		System.out.println("---------------TEST 01-----------------");
		
		/* Mockups */
		BindingResult result = mock(BindingResult.class);
	    when(result.hasErrors()).thenReturn(false);
	    Model model = mock(Model.class);
	    
		/* Create a new decision */
	    DecisionDtoCreate decisionDto = new DecisionDtoCreate();
	    decisionDto.setDescription("Test decision");
	    decisionDto.setGoalTag(goalTag);
	    decisionDto.setProjectName(projectName);
	    decisionDto.setVerdictHours(verdictHours);
	    
	    String returned = viewsController.decisionNewSubmit(decisionDto, result, model);
	    String[] parts = returned.split("/");
	    Long decisionId = Long.parseLong(parts[3]);
	    
	    /* Login one user*/
	    for(int ix=0; ix < nusers; ix++) {
	    	UserDetails userDetails = userAuthService.loadUserByUsername("user"+ix);
			Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername (),userDetails.getPassword (),userDetails.getAuthorities ());
			SecurityContextHolder.getContext().setAuthentication(auth);
			
			ThesisDto thesisDto = new ThesisDto();
			thesisDto.setDecisionId(decisionId);
			thesisDto.setValue(1);
			decisionController.vote(thesisDto);
			
			UpdatedDecisionAndDisplay(decisionId);
		}
	}
	
	private void UpdatedDecisionAndDisplay(Long decisionId) throws IOException {
		Decision decision = null;
		decision = decisionService.get(decisionId);
		
		System.out.println("----------------------------------------------");
		System.out.println("state before: "+decision.getState().toString());
		
		periodicMethods.updateState();
		
		decision = decisionService.get(decisionId);
		
		System.out.println("verdict: "+decision.getVerdict());
		System.out.println("state after: "+decision.getState().toString());
		System.out.println("n: "+decision.n);
		System.out.println("pest: "+decision.pest);
		System.out.println("weightTot: "+decision.weightTot);
		System.out.println("weightCum: "+decision.weightCum);
		System.out.println("elapsedFactor: "+decision.elapsedFactor);
		System.out.println("shrinkFactor: "+decision.shrinkFactor);
		System.out.println("pc_low: "+decision.pc_low);
		System.out.println("pc_high: "+decision.pc_high);
	} 
	
	@Test
	public void testDecisionAlgorithm02() throws IOException {
		
		System.out.println("---------------TEST 02-----------------");
		
		/* Mockups */
		BindingResult result = mock(BindingResult.class);
	    when(result.hasErrors()).thenReturn(false);
	    Model model = mock(Model.class);
	    
		/* Create a new decision */
	    DecisionDtoCreate decisionDto = new DecisionDtoCreate();
	    decisionDto.setDescription("Test decision");
	    decisionDto.setGoalTag(goalTag);
	    decisionDto.setProjectName(projectName);
	    decisionDto.setVerdictHours(verdictHours);
	    
	    String returned = viewsController.decisionNewSubmit(decisionDto, result, model);
	    String[] parts = returned.split("/");
	    Long decisionId = Long.parseLong(parts[3]);
	    Decision decision = decisionService.get(decisionId);
	    Timestamp creationDate = decision.getCreationDate();
	    
	    /* one user vote and time passes by*/
    	UserDetails userDetails = userAuthService.loadUserByUsername("user0");
		Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername (),userDetails.getPassword (),userDetails.getAuthorities ());
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		ThesisDto thesisDto = new ThesisDto();
		thesisDto.setDecisionId(decisionId);
		thesisDto.setValue(1);
		decisionController.vote(thesisDto);
		
		/* zero time has passed */
		decisionService.setTimeService(new TimeServiceTestIm());
		decisionService.getTimeService().setNow(creationDate);
		
		UpdatedDecisionAndDisplay(decisionId);
		
		Long timePassedMs = 0L;
		
		while(timePassedMs <= ((verdictHours*1.1)*hoursToMs)) {
			timePassedMs += timeStepMs;
			decisionService.getTimeService().setNow(new Timestamp(creationDate.getTime() + timePassedMs));
			
			UpdatedDecisionAndDisplay(decisionId);
		}
		
	
	}
}


