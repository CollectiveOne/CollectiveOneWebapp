package org.collectiveone.modules.users;

import java.util.List;
import java.util.UUID;

import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
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
public class UsersController {
	
	@Autowired
	AppUserService appUserService;
	
	@RequestMapping(path = "secured/user/myProfile",  method = RequestMethod.GET)
    public GetResult<AppUserDto> myProfile() {
		AppUser user = appUserService.getOrCreateFromAuth0Id(SecurityContextHolder.getContext().getAuthentication().getName());
		return appUserService.getUserLight(user.getC1Id());
	}
	
	@RequestMapping(path = "secured/user/profile/{userId}",  method = RequestMethod.GET)
    public GetResult<AppUserDto> getProfile(@PathVariable("userId") UUID userId) {
		return appUserService.getUserFull(userId);
	}
	
	@RequestMapping(path = "secured/user/username/exist",  method = RequestMethod.GET)
    public GetResult<Boolean> getProfile(@RequestParam String username) {
		return appUserService.usernameExist(username);
	}
	
	@RequestMapping(path = "secured/user/profile/{userId}",  method = RequestMethod.PUT)
    public PostResult editProfile(@PathVariable("userId") UUID userId, @RequestBody AppUserDto userDto) {
		
		if (getLoggedUser().getC1Id().compareTo(userId) == 0) {
			return appUserService.editUserProfile(userId, userDto);
		}
		
		return new PostResult("error", "only the profile owner can edit a profile", "");
	}
	
	@RequestMapping(path = "/secured/users/suggestions", method = RequestMethod.GET)
	public GetResult<List<AppUserDto>> suggestions(@RequestParam("q") String query) {
		return appUserService.searchBy(query);
	}
	
	private AppUser getLoggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return appUserService.getFromAuth0Id(auth.getName());
	}
	
}
