package org.collectiveone.web.controllers.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.collectiveone.services.UserServiceImp;
import org.collectiveone.web.dto.ProjectAndUsername;
import org.collectiveone.web.dto.ProjectContributedDto;
import org.collectiveone.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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
@RequestMapping("/rest/users")
public class UsersController {
	
	@Autowired
	UserServiceImp userService;
	
	@RequestMapping(value="/get/{username}", method = RequestMethod.POST)
	public @ResponseBody UserDto get(@PathVariable String username) {
		return userService.userGetDto(username);
	}
	
	@RequestMapping(value="/ppsInProjectGet", method = RequestMethod.POST)
	public @ResponseBody ProjectContributedDto offer(@RequestBody ProjectAndUsername projectAndUsername) {
		return userService.userProjectPpsGet(projectAndUsername.getUsername(),projectAndUsername.getProjectName());
	}
	
	@RequestMapping(value="/getSuggestions", method = RequestMethod.GET)
	public Map<String,List<String>> getList(@RequestParam("query") String query) {
		Map<String,List<String>> map = new HashMap<>();
		map.put("suggestions", userService.usernameGetSuggestions(query));
		return map;
	}
	
	@RequestMapping(value="/getSuggestionsReferrer", method = RequestMethod.GET)
	public Map<String,List<String>> getListReferrers(@RequestParam("query") String query) {
		Map<String,List<String>> map = new HashMap<>();
		map.put("suggestions", userService.usernameGetSuggestionsReferrer(query));
		return map;
	}
	
	@RequestMapping(value="/getProjectsContributed/{username}", method = RequestMethod.POST)
	public @ResponseBody List<ProjectContributedDto> getProjectsContributed(@PathVariable String username) {
		return userService.userProjectsContributedAndPpsGet(username);
	}
	
	@Secured("ROLE_USER")
	@RequestMapping(value="/updateProfile", method = RequestMethod.POST)
	public @ResponseBody Boolean udpateProfile(@RequestBody UserDto userDto) throws IOException {
		/* creator is the logged user */
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth.isAuthenticated()) {
			if(userDto.getUsername().equals(auth.getName())) {
				userService.userUpdateProfile(userDto);
				return true;
			}
		}
		return false;
	}
		
}
