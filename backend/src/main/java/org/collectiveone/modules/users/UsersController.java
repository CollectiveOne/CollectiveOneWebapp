package org.collectiveone.modules.users;

import java.util.List;

import org.collectiveone.common.dto.GetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
		return appUserService.getDto(user.getC1Id());
	}
	
	@RequestMapping(path = "/secured/users/suggestions", method = RequestMethod.GET)
	public GetResult<List<AppUserDto>> suggestions(@RequestParam("q") String query) {
		return appUserService.searchBy(query);
	}
	
}
