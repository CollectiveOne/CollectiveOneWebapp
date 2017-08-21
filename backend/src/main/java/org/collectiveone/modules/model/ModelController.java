package org.collectiveone.modules.model;

import java.util.UUID;

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
public class ModelController { 
	
	@Autowired
	private AppUserService appUserService;
	
	@Autowired
	private ModelService modelService;

	@Autowired
	private GovernanceService governanceService;
	
	@RequestMapping(path = "/secured/model/view", method = RequestMethod.POST) 
	public PostResult createView(
			@PathVariable("tokenId") String tokenIdStr, 
			@RequestBody ViewDto viewDto) {
		
		UUID initiativeId = UUID.fromString(viewDto.getInitiativeId());
		
		if (governanceService.canCreateView(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.createView(initiativeId, viewDto, getLoggedUser().getC1Id());
	}
	
	private AppUser getLoggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return appUserService.getFromAuth0Id(auth.getName());
	}
}
