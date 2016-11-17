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
