package org.collectiveone.web.controllers.rest;

import org.collectiveone.services.InitiativeService;
import org.collectiveone.web.dto.GetResult;
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
		GetResult<String> result = new GetResult<String>();
		
		result.setData("data string");
		result.setMessage("success");
		result.setResult("success");
		
		return result;
	}
	
}
