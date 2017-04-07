package org.collectiveone.web.controllers.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.collectiveone.services.UserServiceImp;
import org.collectiveone.web.dto.AutocompleteDto;
import org.collectiveone.web.dto.CredentialsDto;
import org.collectiveone.web.dto.ProjectContributedDto;
import org.collectiveone.web.dto.UserDto;
import org.collectiveone.web.dto.UserMyDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/1")
public class UsersController {
	
	@Autowired
	UserServiceImp userService;
	
	@RequestMapping(value="/user/login", method = RequestMethod.POST)
	public @ResponseBody UserDto login(@RequestBody CredentialsDto credentials) {
		return userService.getDto(credentials.getUsername());
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
	public List<AutocompleteDto> getList(@RequestParam("q") String query) {
		List<AutocompleteDto> res = new ArrayList<AutocompleteDto>();
		
		for(String suggestion : userService.getSuggestions(query)) {
			res.add(new AutocompleteDto(suggestion,suggestion));
		}
		return res;
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
	
	// @Secured("ROLE_USER")
	@RequestMapping(value="/user", method = RequestMethod.PUT)
	public @ResponseBody Boolean udpateProfile(@RequestBody UserDto userDto) throws IOException {
		/* creator is the logged user */
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// if(auth.isAuthenticated()) {
			if(userDto.getUsername().equals("")) {
				userService.updateProfile(userDto);
				return true;
			}
		// }
		return false;
	}
	
	// @Secured("ROLE_USER")
	@RequestMapping(value="/user/{username}/myData", method = RequestMethod.GET)
	public @ResponseBody UserMyDataDto getMyData(@PathVariable String username) {
		
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// if(auth.isAuthenticated()) {
			if(username.equals("")) {
				return userService.getMyData(username);
			}
		//}
		
		return null;
	}
		
}
