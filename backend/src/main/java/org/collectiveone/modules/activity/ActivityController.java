package org.collectiveone.modules.activity;

import java.util.List;
import java.util.UUID;

import org.collectiveone.common.BaseController;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.activity.dto.NotificationDto;
import org.collectiveone.modules.activity.dto.SubscriberDto;
import org.collectiveone.modules.activity.enums.SubscriberEmailsConfig;
import org.collectiveone.modules.activity.enums.SubscriberInAppConfig;
import org.collectiveone.modules.activity.enums.SubscriptionElementType;
import org.collectiveone.modules.users.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/1")
public class ActivityController extends BaseController {
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private AppUserService appUserService;
	
	
	@RequestMapping(path = "/user/notifications", method = RequestMethod.GET)
	public GetResult<List<NotificationDto>> getNotifications(
			@RequestParam("page") Integer page,
			@RequestParam("size") Integer size) {
		
		if (getLoggedUser() == null) {
			return new GetResult<List<NotificationDto>>("error", "endpoint enabled users only", null);
		}
		
		return activityService.getUserNotifications(getLoggedUserId(), new PageRequest(page, size));
	}
	
	@RequestMapping(path = "/user/notifications/read", method = RequestMethod.PUT)
	public PostResult notificationsRead() {
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		return activityService.notificationsRead(getLoggedUser().getC1Id());
	}
	
	@RequestMapping(path = "/user/notifications/subscriber/{initiativeId}", method = RequestMethod.GET)
	public GetResult<SubscriberDto> getSubscriber(
				@PathVariable("elementId") String elementId,
				@RequestParam(name="elementType", defaultValue="INITIATIVE") String elementType ) {
		if (getLoggedUser() == null) {
			return new GetResult<SubscriberDto>("error", "endpoint enabled users only", null);
		}
		
		return activityService.getSubscriber(getLoggedUserId(), UUID.fromString(elementId), SubscriptionElementType.valueOf(elementType));
	}
	
	@RequestMapping(path = "/user/notifications/subscriber/{elementId}", method = RequestMethod.PUT)
	public PostResult editSubscriber(
			@PathVariable("elementId") String elementId,
			@RequestParam(name="elementType", defaultValue="initiative") String elementType,
			@RequestBody SubscriberDto subscriber) {
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		activityService.editSubscriberState(getLoggedUser().getC1Id(), UUID.fromString(initiativeId), SubscriberInAppConfig.valueOf(subscriber.getState()));
		activityService.editSubscriberEmailNotificationsState(getLoggedUser().getC1Id(), UUID.fromString(initiativeId), SubscriberEmailsConfig.valueOf(subscriber.getEmailNotificationsState()));
		
		return new PostResult("success", "success", "");
	}
	
	@RequestMapping(path = "/notifications/unsubscribeFromInitiative/{initiativeId}", method = RequestMethod.PUT)
	public PostResult unsuscribeFromInitiative(@PathVariable("initiativeId") String initiativeId) {
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		return activityService.editSubscriberState(getLoggedUser().getC1Id(), UUID.fromString(initiativeId), SubscriberInAppConfig.UNSUBSCRIBED);
	}
	
	@RequestMapping(path = "/notifications/unsubscribeFromAll", method = RequestMethod.PUT)
	public PostResult unsuscribeFromInitiative() {
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		return appUserService.disableEmailNotifications(getLoggedUser().getC1Id());
	}
	
}