package org.collectiveone.services;

import org.collectiveone.model.Role;
import org.collectiveone.model.User;
import org.collectiveone.repositories.RoleRepository;
import org.collectiveone.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DbServicesTest {
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	public void createUser(String username, String email, String password, boolean enable) {
		User user = new User();
		user.setEnabled(enable);
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(password);
		
		userRepository.save(user);
		
		Role role = new Role();
		role.setRole("ROLE_USER");
		role.setUsername(user.getUsername());
		
		roleRepository.save(role);
		
	}
}
