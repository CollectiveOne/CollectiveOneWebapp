package org.collectiveone.modules.activity;

import java.util.List;
import java.util.UUID;

import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/1")
public class ActivityController {
	
	@Autowired
	private AppUserService appUserService;
	
	@Autowired
	private ActivityService activityService;
	
	
	@RequestMapping(path = "/secured/user/notifications", method = RequestMethod.GET)
	public GetResult<List<NotificationDto>> getNotifications() {
		return activityService.getUserNotifications(getLoggedUser().getC1Id());
	}
	
	@RequestMapping(path = "/secured/user/notifications/read", method = RequestMethod.PUT)
	public PostResult notificationsRead(@PathVariable("userId") String userId) {
		return activityService.notificationsRead(getLoggedUser().getC1Id());
	}
	
	@RequestMapping(path = "/secured/notifications/unsuscribeFromInitiative/{initiativeId}", method = RequestMethod.PUT)
	public PostResult unsuscribeFromInitiative(@PathVariable("initiativeId") String initiativeId) {
		return activityService.disableSubscriber(UUID.fromString(initiativeId), getLoggedUser().getC1Id());
	}
	
	@RequestMapping(path = "/secured/notifications/unsuscribeFromAll", method = RequestMethod.PUT)
	public PostResult unsuscribeFromInitiative() {
		return appUserService.disableEmailNotifications(getLoggedUser().getC1Id());
	}
	
	private AppUser getLoggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return appUserService.getFromAuth0Id(auth.getName());
	}
	
}