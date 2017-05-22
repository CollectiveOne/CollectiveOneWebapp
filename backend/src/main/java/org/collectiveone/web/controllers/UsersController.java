package org.collectiveone.web.controllers;

import org.collectiveone.model.basic.AppUser;
import org.collectiveone.services.AppUserService;
import org.collectiveone.web.dto.AppUserDto;
import org.collectiveone.web.dto.GetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	
}
