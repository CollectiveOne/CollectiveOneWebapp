package org.collectiveone.modules.initiatives.controllers;

import java.util.List;
import java.util.UUID;

import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.governance.enums.DecisionMakerRole;
import org.collectiveone.modules.initiatives.dto.InitiativeDto;
import org.collectiveone.modules.initiatives.dto.MemberDto;
import org.collectiveone.modules.initiatives.dto.NewInitiativeDto;
import org.collectiveone.modules.initiatives.services.InitiativeService;
import org.collectiveone.modules.users.model.AppUser;
import org.collectiveone.modules.users.services.AppUserService;
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
public class InitiativesController { 
	
	@Autowired
	private InitiativeService initiativeService;
	
	@Autowired
	private AppUserService appUserService;
	
	@RequestMapping(path = "/secured/initiative", method = RequestMethod.POST)
	public PostResult postInitiative(@RequestBody NewInitiativeDto initiativeDto) {
		return initiativeService.init(getLoggedUser().getC1Id(), initiativeDto);
	}
	
	@RequestMapping(path = "/secured/initiative/{id}", method = RequestMethod.GET)
	public GetResult<InitiativeDto> getInitiative(
			@PathVariable("id") String id, 
			@RequestParam(defaultValue = "false") boolean addAssets,
			@RequestParam(defaultValue = "false") boolean addSubinitiatives,
			@RequestParam(defaultValue = "false") boolean addMembers) {
		
		InitiativeDto initiativeDto = null;
		
		if(!addAssets) {
			initiativeDto = initiativeService.getLight(UUID.fromString(id));
		} else {
			initiativeDto = initiativeService.getWithOwnAssets(UUID.fromString(id));
		}
		
		if(addSubinitiatives) {
			initiativeDto.setSubInitiatives(initiativeService.getSubinitiativesTree(UUID.fromString(id)));
		}
		
		if(addMembers) {
			initiativeDto.setMembers(initiativeService.getMembers(UUID.fromString(id)));
		}
		
		return new GetResult<InitiativeDto>("success", "initiative retrieved", initiativeDto);
	}
	
	@RequestMapping(path = "/secured/initiatives/mines", method = RequestMethod.GET)
	public GetResult<List<InitiativeDto>> myInitiatives() {
		return initiativeService.getOfUser(getLoggedUser().getC1Id());
	}
	
	@RequestMapping(path = "/secured/initiatives/suggestions", method = RequestMethod.GET)
	public GetResult<List<InitiativeDto>> suggestions(@RequestParam("q") String query) {
		return initiativeService.searchBy(query);
	}
	
	@RequestMapping(path = "/secured/initiative/{id}/member", method = RequestMethod.POST) 
	public PostResult addContributor(@PathVariable("id") String id, @RequestBody MemberDto memberDto) {
		return initiativeService.postMember(
				UUID.fromString(memberDto.getInitiativeId()), 
				UUID.fromString(memberDto.getUser().getC1Id()),
				DecisionMakerRole.valueOf(memberDto.getRole()),
				getLoggedUser().getC1Id());
	}
	
	@RequestMapping(path = "/secured/initiative/{initiativeId}/member/{userId}", method = RequestMethod.DELETE) 
	public PostResult deleteContributor(@PathVariable("initiativeId") String initiativeId, @PathVariable("userId") String userId) {
		return initiativeService.deleteMember(getLoggedUser().getC1Id(), UUID.fromString(initiativeId), UUID.fromString(userId));
	}
	
	private AppUser getLoggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return appUserService.getFromAuth0Id(auth.getName());
	}
	
	
}
