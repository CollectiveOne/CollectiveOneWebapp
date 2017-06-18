package org.collectiveone.modules.assignations.controllers;

import java.util.List;
import java.util.UUID;

import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.assignations.dto.AssignationDto;
import org.collectiveone.modules.assignations.dto.EvaluationDto;
import org.collectiveone.modules.assignations.enums.AssignationType;
import org.collectiveone.modules.assignations.services.AssignationService;
import org.collectiveone.modules.users.model.AppUser;
import org.collectiveone.modules.users.services.AppUserService;
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
public class AssignationController {
	
	@Autowired
	private AssignationService assignationService;
	
	@Autowired
	private AppUserService appUserService;

	
	@RequestMapping(path = "/secured/initiative/{initiativeId}/assignation", method = RequestMethod.POST)
	public PostResult newAssignation(@PathVariable("initiativeId") String initiativeId, @RequestBody AssignationDto assignation) {
		
		AssignationType type = AssignationType.valueOf(assignation.getType());
		
		switch(type) {
		case DIRECT:
			return assignationService.makeDirectAssignation(UUID.fromString(initiativeId), assignation);
			
		case PEER_REVIEWED:
			return assignationService.createAssignation(UUID.fromString(initiativeId), assignation);
		}
		
		return new PostResult("error", "error", "");
		
	} 
	
	@RequestMapping(path = "/secured/initiative/{initiativeId}/assignations", method = RequestMethod.GET)
	public GetResult<List<AssignationDto>> getAssignations(@PathVariable("initiativeId") String initiativeId) {
		return assignationService.getAssignations(UUID.fromString(initiativeId), getLoggedUser().getC1Id());
	}
	
	@RequestMapping(path = "/secured/assignation/{assignationId}/evaluate", method = RequestMethod.POST)
	public PostResult evaluateAssignation(
			@PathVariable("assignationId") String assignationId,
			@RequestBody EvaluationDto evaluationDto) {
		
		PostResult result = assignationService.evaluateAndUpdateAssignation(getLoggedUser().getC1Id(), UUID.fromString(assignationId), evaluationDto);
		
		return result;
	}
	
	private AppUser getLoggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return appUserService.getFromAuth0Id(auth.getName());
	}
	
}
