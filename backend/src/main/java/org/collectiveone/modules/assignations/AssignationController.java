package org.collectiveone.modules.assignations;

import java.util.UUID;

import org.collectiveone.common.BaseController;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.governance.DecisionVerdict;
import org.collectiveone.modules.governance.GovernanceService;
import org.collectiveone.modules.initiatives.InitiativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/1")
public class AssignationController extends BaseController {
	
	@Autowired
	private InitiativeService initiativeService;
	
	@Autowired
	private AssignationService assignationService;
	
	@Autowired
	private GovernanceService governanceService;

	
	@RequestMapping(path = "/initiative/{initiativeId}/assignation", method = RequestMethod.POST)
	public PostResult newAssignation(@PathVariable("initiativeId") String initiativeId, @RequestBody AssignationDto assignation) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		DecisionVerdict canCreate = governanceService.canCreateAssignation(UUID.fromString(initiativeId), getLoggedUser().getC1Id());
		
		if (canCreate == DecisionVerdict.DENIED) {
			return new PostResult("error", "creation of assignation not authorized",  "");
		}
		
		return assignationService.createAssignation(UUID.fromString(initiativeId), assignation, getLoggedUser().getC1Id());
	} 
	
	@RequestMapping(path = "/initiative/{initiativeId}/assignations", method = RequestMethod.GET)
	public GetResult<InitiativeAssignationsDto> getAssignations(@PathVariable("initiativeId") String initiativeIdStr) {
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (!initiativeService.canAccess(initiativeId, getLoggedUserId())) {
			return new GetResult<InitiativeAssignationsDto>("error", "access denied", null);
		}
		
		return assignationService.getAssignationsResult(initiativeId, getLoggedUserId());
	}
	
	@RequestMapping(path = "/assignation/{assignationId}", method = RequestMethod.GET)
	public GetResult<AssignationDto> getAssignationOfUser(
			@PathVariable("assignationId") String assignationIdStr, 
			@RequestParam(defaultValue = "false") Boolean  addAllEvaluations) {
		
		UUID assignationId = UUID.fromString(assignationIdStr); 
		UUID initiativeId = assignationService.findInitiativeId(assignationId);
				
		if (!initiativeService.canAccess(initiativeId, getLoggedUserId())) {
			return new GetResult<AssignationDto>("error", "access denied", null);
		}
		
		return assignationService.getAssignationDto(assignationId, getLoggedUserId(), addAllEvaluations);
	}
	
	@RequestMapping(path = "/assignation/{assignationId}/evaluate", method = RequestMethod.POST)
	public PostResult evaluateAssignation(
			@PathVariable("assignationId") String assignationId,
			@RequestBody EvaluationDto evaluationDto) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		PostResult result = assignationService.evaluateAndUpdateAssignation(getLoggedUser().getC1Id(), UUID.fromString(assignationId), evaluationDto);
		
		return result;
	}
	
	@RequestMapping(path = "/assignation/{assignationId}/revert", method = RequestMethod.PUT)
	public PostResult revertAssignation(
			@PathVariable("assignationId") String assignationId) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		DecisionVerdict canRevert = governanceService.canRevertAssignation(assignationService.getInitiativeIdOf(UUID.fromString(assignationId)), getLoggedUser().getC1Id());
		
		if (canRevert == DecisionVerdict.DENIED) {
			return new PostResult("error", "revert of assignation not authorized",  "");
		}
		
		PostResult result = assignationService.revertAssignation(UUID.fromString(assignationId), getLoggedUser().getC1Id());
		
		return result;
	}
	
	@RequestMapping(path = "/assignation/{assignationId}/approveRevert", method = RequestMethod.PUT)
	public PostResult approveRevertAssignation(
			@PathVariable("assignationId") String assignationId, 
			@RequestParam Boolean approveFlag) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		PostResult result = assignationService.approveRevertAssignation(getLoggedUser().getC1Id(), UUID.fromString(assignationId), approveFlag);
		
		/* update assignation state in case all receivers have approved */
		assignationService.checkRevertStatus(UUID.fromString(assignationId));
		
		return result;
	}
	
	@RequestMapping(path = "/assignation/{assignationId}/delete", method = RequestMethod.PUT)
	public PostResult deleteAssignation(
			@PathVariable("assignationId") String assignationId) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		DecisionVerdict canDelete = governanceService.canDeleteAssignation(assignationService.getInitiativeIdOf(UUID.fromString(assignationId)), getLoggedUser().getC1Id());
		
		if (canDelete == DecisionVerdict.DENIED) {
			return new PostResult("error", "delete of assignation not authorized",  "");
		}
		
		PostResult result = assignationService.deleteAssignation(UUID.fromString(assignationId), getLoggedUser().getC1Id());
		
		return result;
	}
	
}
