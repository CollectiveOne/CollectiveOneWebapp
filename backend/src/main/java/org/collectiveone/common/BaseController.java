package org.collectiveone.common;

import java.util.UUID;

import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseController {
	
	@Autowired
	private AppUserService appUserService;
	
	protected UUID getLoggedUserId() {
		AppUser loggedUser = getLoggedUser();
		if (loggedUser == null) {
			return null;
		} else {
			return loggedUser.getC1Id();
		}
	}
	
	protected AppUser getLoggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return appUserService.getFromAuth0Id(auth.getName());
	}
} 