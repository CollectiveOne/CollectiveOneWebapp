package org.collectiveone.web.controllers;

import java.util.List;

import org.collectiveone.services.InitiativeService;
import org.collectiveone.web.dto.GetResult;
import org.collectiveone.web.dto.InitiativeDto;
import org.collectiveone.web.dto.NewInitiativeDto;
import org.collectiveone.web.dto.PostResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/1")
public class InitiativesController { 
	
	@Autowired
	InitiativeService initiativeService;
	
	@RequestMapping(path = "/secured/initiative", method = RequestMethod.POST)
	public PostResult postInitiative(@RequestBody NewInitiativeDto initiativeDto) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return initiativeService.create(auth.getName(), initiativeDto);
	}
	
	@RequestMapping(path = "/public/initiative/{id}", method = RequestMethod.GET)
	public GetResult<String> getInitiative(@PathVariable("id") String id) {
		return new GetResult<String>("success", "initiative retrieved", "data");
	}
	
	@RequestMapping(path = "/secured/initiatives/mines", method = RequestMethod.GET)
	public GetResult<List<InitiativeDto>> myInitiatives() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return initiativeService.getOfUser(auth.getName());
	}
	
}
