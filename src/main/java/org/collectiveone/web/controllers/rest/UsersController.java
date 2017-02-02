package org.collectiveone.web.controllers.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.collectiveone.services.DbServicesImp;
import org.collectiveone.web.dto.ProjectAndUsername;
import org.collectiveone.web.dto.ProjectContributedDto;
import org.collectiveone.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/rest/users")
public class UsersController {
	
	@Autowired
	DbServicesImp dbServices;
	
	@RequestMapping(value="/get/{username}", method = RequestMethod.POST)
	public @ResponseBody UserDto get(@PathVariable String username) {
		return dbServices.userGetDto(username);
	}
	
	@RequestMapping(value="/ppsInProjectGet", method = RequestMethod.POST)
	public @ResponseBody ProjectContributedDto offer(@RequestBody ProjectAndUsername projectAndUsername) {
		return dbServices.userProjectPpsGet(projectAndUsername.getUsername(),projectAndUsername.getProjectName());
	}
	
	@RequestMapping(value="/getSuggestions", method = RequestMethod.GET)
	public Map<String,List<String>> getList(@RequestParam("query") String query) {
		Map<String,List<String>> map = new HashMap<>();
		map.put("suggestions", dbServices.usernameGetSuggestions(query));
		return map;
	}
	
	@RequestMapping(value="/getSuggestionsReferrer", method = RequestMethod.GET)
	public Map<String,List<String>> getListReferrers(@RequestParam("query") String query) {
		Map<String,List<String>> map = new HashMap<>();
		map.put("suggestions", dbServices.usernameGetSuggestionsReferrer(query));
		return map;
	}
	
	@RequestMapping(value="/getProjectsContributed/{username}", method = RequestMethod.POST)
	public @ResponseBody List<ProjectContributedDto> getProjectsContributed(@PathVariable String username) {
		return dbServices.userProjectsContributedAndPpsGet(username);
	}
		
}
