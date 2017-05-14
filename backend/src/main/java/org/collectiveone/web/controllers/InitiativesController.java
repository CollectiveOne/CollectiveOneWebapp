package org.collectiveone.web.controllers;

import java.util.List;
import java.util.UUID;

import org.collectiveone.model.AppUser;
import org.collectiveone.services.AppUserService;
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
	
	@Autowired
	AppUserService appUserService;
	
	@RequestMapping(path = "/secured/initiative", method = RequestMethod.POST)
	public PostResult postInitiative(@RequestBody NewInitiativeDto initiativeDto) {
		return initiativeService.init(getLoggedUser().getC1Id(), initiativeDto);
	}
	
	@RequestMapping(path = "/secured/initiative/{id}", method = RequestMethod.GET)
	public GetResult<InitiativeDto> getInitiative(@PathVariable("id") String id) {
		return new GetResult<InitiativeDto>("success", "initiative retrieved", initiativeService.get(UUID.fromString(id)));
	}
	
	@RequestMapping(path = "/secured/initiatives/mines", method = RequestMethod.GET)
	public GetResult<List<InitiativeDto>> myInitiatives() {
		return initiativeService.getOfUser(getLoggedUser().getC1Id());
	}
	
	private AppUser getLoggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return appUserService.getFromAuth0Id(auth.getName());
	}
	
	
}
