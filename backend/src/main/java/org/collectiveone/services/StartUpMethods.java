package org.collectiveone.services;


import java.io.IOException;

import javax.annotation.PostConstruct;

import org.collectiveone.model.User;
import org.collectiveone.repositories.UserRepositoryAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Component
public class StartUpMethods { // NO_UCD (unused code)
	
	@Autowired
	UserRepositoryAuth userRepositoryAuth;
	
	@Autowired
	ProjectServiceImp projectService;
	
//	@PostConstruct
//	public void startup() throws IOException {
//		createUser("collectiveone", 	"", 			"", 				false);
//		createUser("user1", 			"u1@x.com", 	"12345678", 		true);
//		createUser("user2", 			"u2@x.com", 	"12345678", 		true);
//		createUser("user3", 			"u3@x.com", 	"12345678", 		true);
//		createUser("user4", 			"u4@x.com", 	"12345678", 		true);
//		
//		projectService.authorize("Demo");
//		
//		System.out.println("done");
//	}
//	
//	private void createUser(String username, String email, String password, boolean enable) throws IOException {
//		User user = new User();
//		user.setEnabled(enable);
//		user.setUsername(username);
//		user.setProfile("User created automatically at startup");
//		user.setEmail(email);
//		user.setPassword(password);
//		
//		userRepositoryAuth.save(user);
//	}
}
