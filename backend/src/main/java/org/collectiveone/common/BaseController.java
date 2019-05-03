package org.collectiveone.common;

import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseController {
	
	@Autowired
	protected AppUserService appUserService;
	
	protected String getLoggedUserId() throws Exception {
		AppUser loggedUser = getLoggedUser();
		if (loggedUser == null) {
			return null;
		} else {
			return loggedUser.getDid();
		}
	}
	
	protected AppUser getLoggedUser() throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return appUserService.getOrCreateFromMyAuth0Id(auth.getName());
	}
} 