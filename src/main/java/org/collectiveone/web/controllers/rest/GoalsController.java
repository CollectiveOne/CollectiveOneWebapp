package org.collectiveone.web.controllers.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.collectiveone.model.User;
import org.collectiveone.services.DbServicesImp;
import org.collectiveone.services.Filters;
import org.collectiveone.services.GoalDtoListRes;
import org.collectiveone.web.dto.GoalDto;
import org.collectiveone.web.dto.GoalGetDto;
import org.collectiveone.web.dto.GoalParentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/rest/goals")
public class GoalsController {
	
	@Autowired
	DbServicesImp dbServices;
	
	@RequestMapping(value="/get", method = RequestMethod.POST)
	public @ResponseBody GoalDto get(@RequestBody GoalGetDto goalGetDto) {
		return dbServices.goalGetDto(goalGetDto.getGoalTag(),goalGetDto.getProjectName());
	}
	
	@RequestMapping("/new")
	public @ResponseBody boolean newGoal(@RequestBody GoalDto goalDto) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User logged = dbServices.userGet(auth.getName());
		goalDto.setCreatorUsername(logged.getUsername());
		dbServices.goalCreate(goalDto);
		return true;
	}
	
	@RequestMapping(value="/getSuggestions", method = RequestMethod.GET)
	public Map<String,List<String>> getList(@RequestParam("projectName") String projectName, @RequestParam("query") String query) {
		
		List<String> projectNames = new ArrayList<String>();
		if(projectName != null) {
			if(!projectName.equals("")) {
				projectNames.add(projectName);
			}
		}
		Map<String,List<String>> map = new HashMap<>();
		map.put("suggestions", dbServices.goalGetSuggestions(query, projectNames));
		return map;
	}
	
	@RequestMapping(value="/getOfProject/{projectName}", method = RequestMethod.POST)
	public @ResponseBody List<GoalDto> get(@PathVariable("projectName") String projectName) {
		Filters filters = new Filters();
		List<String> projectNames = new ArrayList<String>();
		projectNames.add(projectName);
		filters.setProjectNames(projectNames);
		filters.setOnlyParents(true);
		GoalDtoListRes goalDtos = dbServices.goalDtoGetFiltered(filters);
		return goalDtos.getGoalDtos();
	}
	
	@RequestMapping(value="/getList", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getList(@RequestBody Filters filters) {
		if(filters.getPage() == 0) filters.setPage(1);
		if(filters.getNperpage() == 0) filters.setNperpage(15);
		
		GoalDtoListRes goalsDtosRes = dbServices.goalDtoGetFiltered(filters);
		
		List<GoalDto> goalDtos = goalsDtosRes.getGoalDtos();
		int[] resSet = goalsDtosRes.getResSet();
		
		Map<String,Object> map = new HashMap<>();
		
		map.put("goalDtos", goalDtos);
		map.put("resSet", resSet);
		
		return map;
	}
	
	@RequestMapping(value="/proposeParent", method = RequestMethod.POST)
	public @ResponseBody boolean proposeParent(@RequestBody GoalParentDto goalParentDto) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User logged = dbServices.userGet(auth.getName());
		if(logged != null) {
			dbServices.goalProposeParent(goalParentDto.getGoalId(), goalParentDto.getParentTag());
		}
		return true;
	}
}
