package org.collectiveone.services;


import javax.annotation.PostConstruct;

import org.collectiveone.model.Role;
import org.collectiveone.model.User;
import org.collectiveone.repositories.AuthorizedEmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class StartUpMethods {

	@Autowired
	UserServiceIf userService;

	@Autowired
	RoleServiceIf roleService;
	
	@Autowired
	AuthorizedEmailRepository authorizedEmailRepository;
	
	@Autowired
    PasswordEncoder passwordEncoder;

	
	@PostConstruct
	public void UpdateDecisionsStatus() {
		
		System.out.println("Filling DB");
		/*AuthorizedEmail authEmail = new AuthorizedEmail();
		authEmail.setEmail("p@x.com");
		authEmail.setAuthorized(true);
		authorizedEmailRepository.save(authEmail);
		
		createUser("pepoospina", 			"pepo.ospina@gmail.com", 	"pepoospina1", 		true);
		createUser("estebanortizospina", 	"temp1@gmail.com", 			"Esteban1017", 		true);
		createUser("mospina", 				"temp2@gmail.com", 			"amoajose", 		true);
		createUser("jpmarindiaz", 			"temp3@gmail.com", 			"jpmarindiazx", 	true);
		createUser("quiquin", 				"temp4@gmail.com", 			"quiquines", 		true);
		createUser("collectiveone", 		"temp5@gmail.com", 			"collectiveone", 	false);*/
		System.out.println("done");
		
	}
	
	@SuppressWarnings("unused")
	private void createUser(String username, String email, String password, boolean enable) {
		User user = new User();
		user.setEnabled(enable);
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		
		userService.save(user);
		
		Role role = new Role();
		role.setRole("ROLE_USER");
		role.setUsername(user.getUsername());
		
		roleService.save(role);
		
	}
}
