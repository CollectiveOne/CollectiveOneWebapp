package org.collectiveone.services;


import java.io.IOException;

import javax.annotation.PostConstruct;

import org.collectiveone.model.Role;
import org.collectiveone.model.User;
import org.collectiveone.repositories.AuthorizedEmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class StartUpMethods { // NO_UCD (unused code)

	@Autowired
	UserAuthServiceIf userService;

	@Autowired
	RoleServiceIf roleService;
	
	@Autowired
	AuthorizedEmailRepository authorizedEmailRepository;
	
	@Autowired
    PasswordEncoder passwordEncoder;
	
	@PostConstruct
	public void UpdateDecisionsStatus() throws IOException {
		
		System.out.println("Filling DB");
		
		/*
		AuthorizedEmail authEmail = new AuthorizedEmail();
		authEmail.setEmail("p@x.com");
		authEmail.setAuthorized(true);
		authorizedEmailRepository.save(authEmail);
		
		createUser("collectiveone", 	"", 			"", 				false);
		createUser("user1", 			"u1@x.com", 	"12345678", 		true);
		createUser("user2", 			"u2@x.com", 	"12345678", 		true);
		createUser("user3", 			"u3@x.com", 	"12345678", 		true);
		createUser("user4", 			"u4@x.com", 	"12345678", 		true);
		
		dbServices.projectAuthorize("Demo");
		*/
		
		System.out.println("done");
	}
	
	@SuppressWarnings("unused")
	private void createUser(String username, String email, String password, boolean enable) throws IOException {
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
