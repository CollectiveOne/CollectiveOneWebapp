package org.collectiveone.modules.assignations;

import java.util.UUID;

import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.governance.DecisionVerdict;
import org.collectiveone.modules.governance.GovernanceService;
import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserService;
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
	
	@Autowired
	private GovernanceService governanceService;

	
	@RequestMapping(path = "/secured/initiative/{initiativeId}/assignation", method = RequestMethod.POST)
	public PostResult newAssignation(@PathVariable("initiativeId") String initiativeId, @RequestBody AssignationDto assignation) {
		
		DecisionVerdict canCreate = governanceService.canCreateAssignation(UUID.fromString(initiativeId), getLoggedUser().getC1Id());
		
		if (canCreate == DecisionVerdict.DENIED) {
			return new PostResult("error", "creation of assignation not authorized",  "");
		}
		
		return assignationService.createAssignation(UUID.fromString(initiativeId), assignation);
	} 
	
	@RequestMapping(path = "/secured/initiative/{initiativeId}/assignations", method = RequestMethod.GET)
	public GetResult<InitiativeAssignationsDto> getAssignations(@PathVariable("initiativeId") String initiativeId) {
		return assignationService.getAssignationsResult(UUID.fromString(initiativeId), getLoggedUser().getC1Id());
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
