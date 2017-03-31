package org.collectiveone.web.controllers.rest;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.collectiveone.services.UserServiceImp;
import org.collectiveone.web.dto.ProjectContributedDto;
import org.collectiveone.web.dto.UserDto;
import org.collectiveone.web.dto.UserMyDataDto;
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
@RequestMapping("/1")
public class UsersController { // NO_UCD (unused code)
	
	@Autowired
	UserServiceImp userService;
	
	@RequestMapping("/user")
	public Principal user(Principal user) {
		System.out.println(user);
		return user;
	}
	
	@RequestMapping(value="/user/{username}", method = RequestMethod.GET)
	public @ResponseBody UserDto get(@PathVariable String username) {
		return userService.getDto(username);
	}
	
	@RequestMapping(value="/user/{username}/ppsInProject", method = RequestMethod.GET)
	public @ResponseBody ProjectContributedDto offer( 	@PathVariable String username, 
														@RequestParam("projectName") String projectName) {
		
		return userService.projectPpsGet(username,projectName);
	}
	
	@RequestMapping(value="/users/suggestions", method = RequestMethod.GET)
	public Map<String,List<String>> getList(@RequestParam("query") String query) {
		Map<String,List<String>> map = new HashMap<>();
		map.put("suggestions", userService.getSuggestions(query));
		return map;
	}
	
	@RequestMapping(value="/users/suggestionsReferrer", method = RequestMethod.GET)
	public Map<String,List<String>> getListReferrers(@RequestParam("query") String query) {
		Map<String,List<String>> map = new HashMap<>();
		map.put("suggestions", userService.getSuggestionsReferrer(query));
		return map;
	}
	
	@RequestMapping(value="/user/{username}/projectsContributed", method = RequestMethod.GET)
	public @ResponseBody List<ProjectContributedDto> getProjectsContributed(@PathVariable String username) {
		return userService.projectsContributedAndPpsGet(username);
	}
	
	@Secured("ROLE_USER")
	@RequestMapping(value="/user", method = RequestMethod.PUT)
	public @ResponseBody Boolean udpateProfile(@RequestBody UserDto userDto) throws IOException {
		/* creator is the logged user */
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth.isAuthenticated()) {
			if(userDto.getUsername().equals(auth.getName())) {
				userService.updateProfile(userDto);
				return true;
			}
		}
		return false;
	}
	
	@Secured("ROLE_USER")
	@RequestMapping(value="/user/{username}/myData", method = RequestMethod.GET)
	public @ResponseBody UserMyDataDto getMyData(@PathVariable String username) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth.isAuthenticated()) {
			if(username.equals(auth.getName())) {
				return userService.getMyData(username);
			}
		}
		
		return null;
	}
		
}
