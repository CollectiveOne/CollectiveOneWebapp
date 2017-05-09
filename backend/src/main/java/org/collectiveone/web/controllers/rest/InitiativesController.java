package org.collectiveone.web.controllers.rest;

import org.collectiveone.services.InitiativeService;
import org.collectiveone.web.dto.NewInitiativeDto;
import org.collectiveone.web.dto.PostResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/1")
public class InitiativesController { 
	
	@Autowired
	InitiativeService initiativeService;
	
	@Secured("role::user")
	@RequestMapping(path = "initiative", method = RequestMethod.POST)
	public PostResult postInitiative(@RequestBody NewInitiativeDto initiativeDto) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	
		return initiativeService.create(auth.getName(), initiativeDto);
	}
	
}
