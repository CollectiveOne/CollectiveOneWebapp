package org.collectiveone.modules.users;

import org.collectiveone.common.BaseController;
import org.collectiveone.common.dto.GetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/1")
public class AppUserController extends BaseController {
	
	@Autowired
	AppUserService appUserService;
	
	@RequestMapping(path = "/u/me",  method = RequestMethod.GET)
    public GetResult<AppUserDto> myProfile() throws Exception {
		
		String auth0Id = SecurityContextHolder.getContext().getAuthentication().getName();
		if (auth0Id.equals("anonimousUser")) {
			return new GetResult<AppUserDto>("error", "anonymous user", null);
		}
		
		AppUserDto userDto = appUserService.toDtoWithRootPerspective(
				appUserService.getOrCreateFromMyAuth0Id(auth0Id));
		
		return new GetResult<AppUserDto> ("success", "user retrieved", userDto);
	}
	
}
