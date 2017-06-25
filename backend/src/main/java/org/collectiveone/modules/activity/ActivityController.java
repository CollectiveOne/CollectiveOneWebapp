package org.collectiveone.modules.activity;

import java.util.List;
import java.util.UUID;

import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/1")
public class ActivityController {
	
	@Autowired
	private ActivityService activityService;
	
	@RequestMapping(path = "/secured/user/{userId}/notifications", method = RequestMethod.GET)
	public GetResult<List<NotificationDto>> getNotifications(@PathVariable("userId") String userId) {
		return activityService.getUserNotifications(UUID.fromString(userId));
	}
	
	@RequestMapping(path = "/secured/user/{userId}/notifications/read", method = RequestMethod.PUT)
	public PostResult notificationsRead(@PathVariable("userId") String userId) {
		return activityService.notificationsRead(UUID.fromString(userId));
	}
	
}