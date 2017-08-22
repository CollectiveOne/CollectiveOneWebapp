package org.collectiveone.modules.model;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/1")
public class ModelController { 
	
	@Autowired
	private AppUserService appUserService;
	
	@Autowired
	private ModelService modelService;

	@Autowired
	private GovernanceService governanceService;
	
	@RequestMapping(path = "/secured/initiative/{initiativeId}/model/view", method = RequestMethod.POST) 
	public PostResult createView(
			@PathVariable("initiativeId") String initiativeIdStr,
			@RequestBody ModelViewDto viewDto) {
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (governanceService.canCreateView(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.createView(initiativeId, viewDto, getLoggedUser().getC1Id());
	}
	
	@RequestMapping(path = "/secured/initiative/{initiativeId}/model/section", method = RequestMethod.POST)
	public PostResult createSection(
			@PathVariable("initiativeId") String initiativeIdStr,
			@RequestBody ModelSectionDto sectionDto) {
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (governanceService.canCreateView(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.createSection(sectionDto, getLoggedUser().getC1Id());
	}
	
	@RequestMapping(path = "/secured/initiative/{initiativeId}/model/card", method = RequestMethod.POST)
	public PostResult createCard(
			@PathVariable("initiativeId") String initiativeIdStr,
			@RequestBody CardDto cardDto) {
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (governanceService.canCreateCard(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.createCard(cardDto, getLoggedUser().getC1Id());
	}
	
	
	@RequestMapping(path = "/secured/initiative/{initiativeId}/model", method = RequestMethod.GET) 
	public GetResult<ModelDto> getView(
			@PathVariable("initiativeId") String initiativeIdStr,
			@RequestParam(defaultValue = "1") Integer level) {
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (governanceService.canCreateView(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new GetResult<ModelDto>("error", "not authorized", null);
		}
		
		return modelService.getModel(initiativeId, level, getLoggedUser().getC1Id());
	}
	
	private AppUser getLoggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return appUserService.getFromAuth0Id(auth.getName());
	}
}
