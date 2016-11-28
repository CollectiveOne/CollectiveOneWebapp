
package org.collectiveone;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.collectiveone.model.User;
import org.collectiveone.services.UserServiceIm;
import org.collectiveone.web.controllers.BaseController;
import org.collectiveone.web.controllers.rest.UsersController;
import org.collectiveone.web.controllers.views.ViewsController;
import org.collectiveone.web.dto.ProjectNewDto;
import org.collectiveone.web.dto.UserNewDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
public class CollectiveOneWebappApplicationTests {

	
	@Autowired
	BaseController baseController;
	
	@Autowired
	UsersController usersController;
	
	@Autowired
	ViewsController viewsController;
	
	@Autowired
	UserServiceIm userService;
	
	@Test
	public void testDecisionAlgorithm() {
		/* request environment  */
		Model model = new BindingAwareModelMap();
		HttpServletRequest request = new MockHttpServletRequest();
		Locale locale = new Locale("US");
		
		/* security mockup */
		
		
		/* Create users */
		int nusers = 10;
		for(int ix=0; ix < nusers; ix++) {
			UserNewDto userNew = new UserNewDto();
			
			userNew.setPassword("user"+ix);
			userNew.setPasswordConfirm("user"+ix);
			userNew.setUsername("user"+ix);
			userNew.setEmail("temp"+ix+"@gmail.com");
			
			userService.emailAuthorize(userNew.getEmail());
			baseController.signupSubmit(locale, userNew, model, request);
		}
		
		/* Activate users s*/
		for(User user : userService.findAll()) {
			userService.enableUser(user.getId());
		}
		
		/* Create project */
		ProjectNewDto projectNew = new ProjectNewDto();
		
		projectNew.setName("testProject");
		projectNew.setCreatorUsername("user1");
		projectNew.setDescription("Dummy description");
		projectNew.setPpsInitial(100);
				
		viewsController.projectNewSubmit(projectNew, model);
		
	}

}
