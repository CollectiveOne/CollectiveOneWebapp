package org.collectiveone.web.controllers.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.collectiveone.model.GoalState;
import org.collectiveone.model.User;
import org.collectiveone.services.GoalServiceImp;
import org.collectiveone.services.UserServiceImp;
import org.collectiveone.web.dto.Filters;
import org.collectiveone.web.dto.GoalDto;
import org.collectiveone.web.dto.GoalDtoListRes;
import org.collectiveone.web.dto.GoalWeightsDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/1")
public class GoalsController { // NO_UCD (unused code)
	
	@Autowired
	GoalServiceImp goalService;
	
	@Autowired
	UserServiceImp userService;
	
	@RequestMapping(value="/goal/{goalId}", method = RequestMethod.GET)
	public @ResponseBody GoalDto get(	@PathVariable("goalId") Long goalId ) {
		return goalService.getDto(goalId);
	}
	
	@RequestMapping(value="/goal", method = RequestMethod.GET)
	public @ResponseBody GoalDto get(	@RequestParam("projectName") String projectName,
										@RequestParam("goalTag") String goalTag) {
		
		return goalService.getDto(goalTag ,projectName);
	}
	
	@RequestMapping(value="/goalExist", method = RequestMethod.GET)
	public @ResponseBody boolean exist(	@RequestParam("projectName") String projectName,
										@RequestParam("goalTag") String goalTag) {
		
		return goalService.exist(goalTag, projectName, GoalState.ACCEPTED);
	}
	
	@RequestMapping(value="/goal", method = RequestMethod.POST)
	public @ResponseBody boolean newGoal(@Valid @RequestBody GoalDto goalDto, BindingResult result) throws IOException {
		if(result.hasErrors()) {
			return false;
		} else {
			/* check goal-tag is new in that project */
			if(!goalService.exist(goalDto.getGoalTag(),goalDto.getProjectName())) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				User logged = userService.get(auth.getName());
				goalDto.setCreatorUsername(logged.getUsername());
				goalService.create(goalDto);
				return true;
			} else {
				return false;
			}
		}
	}
	
	@RequestMapping(value="/goals/suggestions", method = RequestMethod.GET)
	public Map<String,List<String>> getList(@RequestParam("projectName") String projectName, 
											@RequestParam("query") String query) {
		
		List<String> projectNames = new ArrayList<String>();
		if(projectName != null) {
			if(!projectName.equals("")) {
				projectNames.add(projectName);
			}
		}
		Map<String,List<String>> map = new HashMap<>();
		map.put("suggestions", goalService.getSuggestions(query, projectNames));
		return map;
	}
	
	@RequestMapping(value="/project/{projectName}/goals", method = RequestMethod.GET)
	public @ResponseBody List<GoalDto> get(@PathVariable("projectName") String projectName) {
		Filters filters = new Filters();
		
		List<String> projectNames = new ArrayList<String>();
		projectNames.add(projectName);
		filters.setProjectNames(projectNames);
		filters.setOnlyParents(true);
		
		List<String> states = new ArrayList<String>();
		states.add(GoalState.PROPOSED.toString());
		states.add(GoalState.ACCEPTED.toString());
		filters.setStateNames(states);
		
		GoalDtoListRes goalDtos = goalService.getFilteredDto(filters);
		return goalDtos.getGoalDtos();
	}
	
	@RequestMapping(value="/goal/{goalId}/weights", method = RequestMethod.GET)
	public @ResponseBody GoalWeightsDataDto getWeightData(@PathVariable("goalId") Long goalId) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return goalService.getWeightsData(goalId, auth.getName());
	}
	
	@Secured("ROLE_USER")
	@RequestMapping(value="/goal/{goalId}/touch", method = RequestMethod.PUT)
	public boolean touch(@PathVariable("goalId") Long goalId, @RequestParam("touch") boolean touch) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User logged = userService.get(auth.getName());
		goalService.touch(goalId,logged.getId(),touch);
		return true;
	}
	
	@Secured("ROLE_USER")
	@RequestMapping(value="/goal/{goalId}/setWeight", method = RequestMethod.PUT)
	public boolean setWeight(@PathVariable("goalId") Long goalId, @RequestParam("weight") double weight) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User logged = userService.get(auth.getName());
		goalService.setWeightIteratively(goalId,logged.getId(),weight);
		return true;
	}
	
	@RequestMapping(value="/goals", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getList(@RequestBody Filters filters) {
		if(filters.getPage() == 0) filters.setPage(1);
		if(filters.getNperpage() == 0) filters.setNperpage(15);
		
		GoalDtoListRes goalsDtosRes = goalService.getFilteredDto(filters);
		
		List<GoalDto> goalDtos = goalsDtosRes.getGoalDtos();
		int[] resSet = goalsDtosRes.getResSet();
		
		Map<String,Object> map = new HashMap<>();
		
		map.put("goalDtos", goalDtos);
		map.put("resSet", resSet);
		
		return map;
	}
	
	@Secured("ROLE_USER")
	@RequestMapping(value="/goal/{goalId}/proposeEdit", method = RequestMethod.PUT)
	public @ResponseBody boolean proposeEdit(@PathVariable("goalId") Long goalId, @RequestParam("newDescription") String newDescription) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User logged = userService.get(auth.getName());
		if(logged != null) {
			goalService.proposeEdition(goalId, logged.getId(),newDescription) ;
		}
		return true;
	}
	
	
	@Secured("ROLE_USER")
	@RequestMapping(value="/goal/{goalId}/proposeParent", method = RequestMethod.PUT)
	public @ResponseBody boolean proposeParent(@PathVariable("goalId") Long goalId, @RequestParam("parentTag") String parentTag) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User logged = userService.get(auth.getName());
		if(logged != null) {
			goalService.proposeParent(goalId, parentTag) ;
		}
		return true;
	}
	
	@Secured("ROLE_USER")
	@RequestMapping(value="/goal/{goalId}/proposeDetach", method = RequestMethod.PUT)
	public @ResponseBody boolean proposeDetach(@PathVariable("goalId") Long goalId, @RequestParam("increaseBudget") double increaseBudget) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User logged = userService.get(auth.getName());
		if(logged != null) {
			goalService.detach(goalId, increaseBudget);
		}
		return true;
	}
	
	@Secured("ROLE_USER")
	@RequestMapping(value="/goal/{goalId}/proposeReattach", method = RequestMethod.PUT)
	public @ResponseBody boolean proposeReattach(@PathVariable("goalId") Long goalId) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User logged = userService.get(auth.getName());
		if(logged != null) {
			goalService.reattach(goalId);
		}
		return true;
	}
	
	@Secured("ROLE_USER")
	@RequestMapping(value="/goal/{goalId}/proposeIncreaseBudget", method = RequestMethod.PUT)
	public @ResponseBody boolean proposeIncreaseBudget(@PathVariable("goalId") Long goalId, @RequestParam("increaseBudget") double increaseBudget) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User logged = userService.get(auth.getName());
		if(logged != null) {
			goalService.increaseBudget(goalId, increaseBudget);
		}
		return true;
	}
}
