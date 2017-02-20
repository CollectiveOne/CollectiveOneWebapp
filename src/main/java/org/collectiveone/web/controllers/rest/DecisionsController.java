package org.collectiveone.web.controllers.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.collectiveone.model.ArgumentTendency;
import org.collectiveone.model.User;
import org.collectiveone.services.DbServicesImp;
import org.collectiveone.web.dto.ArgumentBackDto;
import org.collectiveone.web.dto.ArgumentDto;
import org.collectiveone.web.dto.DecisionDtoFull;
import org.collectiveone.web.dto.DecisionDtoListRes;
import org.collectiveone.web.dto.Filters;
import org.collectiveone.web.dto.ThesisDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/rest/decisions")
public class DecisionsController {
	
	@Autowired
	DbServicesImp dbServices;
	
	@RequestMapping(value="/get/{id}", method = RequestMethod.POST)
	public @ResponseBody DecisionDtoFull get(@PathVariable Long id) {
		return dbServices.decisionGetDto(id);
	}
	
	@RequestMapping(value="/getList", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getList(@RequestBody Filters filters) {
		if(filters.getPage() == 0) filters.setPage(1);
		if(filters.getNperpage() == 0) filters.setNperpage(15);
		
		DecisionDtoListRes decisionsDtosRes = dbServices.decisionDtoGetFiltered(filters);
		
		List<DecisionDtoFull> decisionDtos = decisionsDtosRes.getDecisionDtos();
		int[] resSet = decisionsDtosRes.getResSet();
		
		Map<String,Object> map = new HashMap<>();
		
		map.put("decisionDtos", decisionDtos);
		map.put("resSet", resSet);
		
		return map;
	}
	
	@RequestMapping(value="/vote", method = RequestMethod.POST)
	public @ResponseBody boolean vote(@RequestBody ThesisDto thesisDto) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User logged = dbServices.userGet(auth.getName());
		if(logged != null) {
			dbServices.thesisOfDecSave(logged, thesisDto.getValue(), thesisDto.getDecisionId());
			return true;
		}
		return false;
		
	}
	
	@RequestMapping(value="/getVote/{id}", method = RequestMethod.POST)
	public @ResponseBody ThesisDto getVote(@PathVariable Long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User logged = dbServices.userGet(auth.getName());
		if(logged != null) {
			ThesisDto thesisDto = dbServices.thesisOfUser(id, logged.getId());
			if(thesisDto != null){
				return thesisDto;
			} 
		}
		return new ThesisDto();
	}
	
	@RequestMapping(value="/getArguments/{id}", method = RequestMethod.POST)
	public @ResponseBody Map<String,List<ArgumentDto>> getArguments(@PathVariable Long id) {
		
		List<ArgumentDto> argumentYesDtos = dbServices.argumentGetOfDecisionDto(id, ArgumentTendency.FORYES);
		List<ArgumentDto> argumentNoDtos = dbServices.argumentGetOfDecisionDto(id, ArgumentTendency.FORNO);
		
		Map<String,List<ArgumentDto>> map = new HashMap<>();
		
		map.put("argumentYesDtos", argumentYesDtos);
		map.put("argumentNoDtos", argumentNoDtos);
			
		return map;
	}

	@RequestMapping(value="/newArgument", method = RequestMethod.POST)
	public @ResponseBody boolean newArgument(@RequestBody ArgumentDto argumentDto) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User logged = dbServices.userGet(auth.getName());
		if(logged != null) {
			argumentDto.setCreatorUsername(logged.getUsername());
			dbServices.argumentCreate(argumentDto);
			return true;
		}
		return false;
		
	}
	
	@RequestMapping(value="/getArgument/{id}", method = RequestMethod.POST)
	public @ResponseBody ArgumentDto argumentGet(@PathVariable Long id) {
		return dbServices.argumentGetDto(id);
	}
	
	@RequestMapping(value="/argumentIsBacked/{id}", method = RequestMethod.POST)
	public @ResponseBody boolean argumentIsBacked(@PathVariable Long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User logged = dbServices.userGet(auth.getName());
		if(logged != null) {
			return dbServices.argumentIsBacked(id,logged.getId());	 
		}
		return false;
	}
	
	@RequestMapping(value="/argumentBack", method = RequestMethod.POST)
	public @ResponseBody boolean argumentBacked(@RequestBody ArgumentBackDto argumentBackDto) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User logged = dbServices.userGet(auth.getName());
		if(logged != null) {
			if(argumentBackDto.getBack()) {
				dbServices.argumentBack(argumentBackDto.getArgId(),logged.getId());
			} else {
				dbServices.argumentUnBack(argumentBackDto.getArgId(),logged.getId());
			}
			return true;
		}
		return false;
		
	}
	
}
