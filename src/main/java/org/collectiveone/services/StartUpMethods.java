package org.collectiveone.services;


import javax.annotation.PostConstruct;

import org.collectiveone.repositories.AuthorizedEmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartUpMethods {

	@Autowired
	AuthorizedEmailRepository authorizedEmailRepository;
	
	

	
	@PostConstruct
	public void UpdateDecisionsStatus() {
		System.out.println("Filling DB");
		System.out.println("done");
	}
	
	
}
