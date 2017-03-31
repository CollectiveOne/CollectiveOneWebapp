package org.collectiveone.web.controllers.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.collectiveone.model.ArgumentTendency;
import org.collectiveone.model.User;
import org.collectiveone.services.ArgumentServiceImp;
import org.collectiveone.services.DecisionServiceImp;
import org.collectiveone.services.ThesisServiceImp;
import org.collectiveone.services.UserServiceImp;
import org.collectiveone.web.dto.ArgumentDto;
import org.collectiveone.web.dto.DecisionDtoFull;
import org.collectiveone.web.dto.DecisionDtoListRes;
import org.collectiveone.web.dto.Filters;
import org.collectiveone.web.dto.ThesisDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/1")
public class DecisionsController { // NO_UCD (unused code)
	
	@Autowired
	DecisionServiceImp decisionService;
	
	@Autowired
	ArgumentServiceImp argumentService;
	
	@Autowired
	ThesisServiceImp thesisService;
	
	@Autowired
	UserServiceImp userService;
	
	@RequestMapping(value="/decision/{id}", method = RequestMethod.GET)
	public @ResponseBody DecisionDtoFull get(@PathVariable Long id) {
		return decisionService.getDto(id);
	}
	
	@RequestMapping(value="/decisions", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getList(@RequestBody Filters filters) {
		if(filters.getPage() == 0) filters.setPage(1);
		if(filters.getNperpage() == 0) filters.setNperpage(15);
		
		DecisionDtoListRes decisionsDtosRes = decisionService.getFilteredDto(filters);
		
		List<DecisionDtoFull> decisionDtos = decisionsDtosRes.getDecisionDtos();
		int[] resSet = decisionsDtosRes.getResSet();
		
		Map<String,Object> map = new HashMap<>();
		
		map.put("decisionDtos", decisionDtos);
		map.put("resSet", resSet);
		
		return map;
	}
	
	// @Secured("ROLE_USER")
	@RequestMapping(value="/decision/vote", method = RequestMethod.POST)
	public @ResponseBody boolean vote(@RequestBody ThesisDto thesisDto) {
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User logged = userService.get("");
		if(logged != null) {
			thesisService.save(logged, thesisDto.getValue(), thesisDto.getDecisionId());
			return true;
		}
		return false;
		
	}
	
	@RequestMapping(value="/decision/{decisionId}/vote", method = RequestMethod.GET)
	public @ResponseBody ThesisDto getVote(@PathVariable Long decisionId) {
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User logged = userService.get("");
		if(logged != null) {
			ThesisDto thesisDto = thesisService.getOfUserDto(decisionId, logged.getId());
			if(thesisDto != null){
				return thesisDto;
			} 
		}
		return new ThesisDto();
	}
	
	@RequestMapping(value="/decision/{decisionId}/arguments", method = RequestMethod.GET)
	public @ResponseBody Map<String,List<ArgumentDto>> getArguments(@PathVariable Long decisionId) {
		
		List<ArgumentDto> argumentYesDtos = argumentService.getOfDecisionDto(decisionId, ArgumentTendency.FORYES);
		List<ArgumentDto> argumentNoDtos = argumentService.getOfDecisionDto(decisionId, ArgumentTendency.FORNO);
		
		Map<String,List<ArgumentDto>> map = new HashMap<>();
		
		map.put("argumentYesDtos", argumentYesDtos);
		map.put("argumentNoDtos", argumentNoDtos);
			
		return map;
	}

	// @Secured("ROLE_USER")
	@RequestMapping(value="/argument", method = RequestMethod.POST)
	public @ResponseBody boolean newArgument(@RequestBody ArgumentDto argumentDto) throws IOException {
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User logged = userService.get("");
		if(logged != null) {
			argumentDto.setCreatorUsername(logged.getUsername());
			argumentService.create(argumentDto);
			return true;
		}
		return false;
		
	}
	
	@RequestMapping(value="/argument/{id}", method = RequestMethod.GET)
	public @ResponseBody ArgumentDto argumentGet(@PathVariable Long id) {
		return argumentService.getDto(id);
	}
	
	@RequestMapping(value="/argument/{argId}/isBacked", method = RequestMethod.GET)
	public @ResponseBody boolean argumentIsBacked(@PathVariable Long argId) {
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User logged = userService.get("");
		if(logged != null) {
			return argumentService.isBacked(argId,logged.getId());	 
		}
		return false;
	}
	
	// @Secured("ROLE_USER")
	@RequestMapping(value="/argument/{argId}/back", method = RequestMethod.PUT)
	public @ResponseBody boolean argumentBacked(@PathVariable Long argId, @RequestParam("back") boolean back) {
		
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User logged = userService.get("");
		if(logged != null) {
			if(back) {
				argumentService.back(argId,logged.getId());
			} else {
				argumentService.unBack(argId,logged.getId());
			}
			return true;
		}
		return false;
		
	}
	
}
