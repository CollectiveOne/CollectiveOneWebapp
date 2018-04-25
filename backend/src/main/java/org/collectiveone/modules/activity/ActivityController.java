package org.collectiveone.modules.activity;

import java.util.List;
import java.util.UUID;

import org.collectiveone.common.BaseController;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.activity.dto.NotificationDto;
import org.collectiveone.modules.activity.dto.SubscriberDto;
import org.collectiveone.modules.activity.enums.NotificationContextType;
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
	
	
	@RequestMapping(path = "/notifications/{contextElementType}/{elementId}", method = RequestMethod.GET)
	public GetResult<List<NotificationDto>> getNotifications(
			@RequestParam("page") Integer page,
			@RequestParam(name="size", defaultValue="10") Integer size,
			@RequestParam(name="isHtml", defaultValue="false") Boolean isHtml,
			@PathVariable(name="contextElementType") String contextElementType,
			@PathVariable("elementId") String elementId) {
		
		if (getLoggedUser() == null) {
			return new GetResult<List<NotificationDto>>("error", "endpoint enabled users only", null);
		}
		
		return activityService.getUserNotifications(
				getLoggedUserId(),
				NotificationContextType.valueOf(contextElementType),
				UUID.fromString(elementId),
				new PageRequest(page, size),
				isHtml);
	}
	
	@RequestMapping(path = "/notifications/{contextElementType}/{elementId}/read", method = RequestMethod.PUT)
	public PostResult notificationsRead(
			@PathVariable(name="contextElementType") String contextElementType,
			@PathVariable("elementId") String elementId) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		return activityService.notificationsRead(
				getLoggedUserId(), 
				NotificationContextType.valueOf(contextElementType),
				UUID.fromString(elementId));
	}
	
	@RequestMapping(path = "/notifications/pushed/{notificationId}", method = RequestMethod.PUT)
	public PostResult notificationPushed(
			@PathVariable(name="notificationId") String notificationId) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		return activityService.notificationPushed(
				UUID.fromString(notificationId));
	}
	
	@RequestMapping(path = "/notifications/subscriber/{elementType}/{elementId}", method = RequestMethod.GET)
	public GetResult<SubscriberDto> getSubscriber(
				@PathVariable("elementId") String elementId,
				@PathVariable(name="elementType") String elementType ) {
		if (getLoggedUser() == null) {
			return new GetResult<SubscriberDto>("error", "endpoint enabled users only", null);
		}
		
		return activityService.getSubscriber(getLoggedUserId(), UUID.fromString(elementId), SubscriptionElementType.valueOf(elementType));
	}
	
	@RequestMapping(path = "/notifications/subscriber/{elementType}/{elementId}/inheritFrom", method = RequestMethod.GET)
	public GetResult<SubscriberDto> getSubscriberInheritFrom(
				@PathVariable("elementId") String elementId,
				@PathVariable(name="elementType") String elementType ) {
		if (getLoggedUser() == null) {
			return new GetResult<SubscriberDto>("error", "endpoint enabled users only", null);
		}
		
		return activityService.getSubscriberInheritFrom(getLoggedUserId(), UUID.fromString(elementId), SubscriptionElementType.valueOf(elementType));
	}
	
	@RequestMapping(path = "/notifications/subscriber/{elementType}/{elementId}", method = RequestMethod.PUT)
	public PostResult editSubscriber(
			@PathVariable("elementId") String elementId,
			@PathVariable(name="elementType") String elementType,
			@RequestBody SubscriberDto subscriber) {
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		activityService.editSubscriber(
				getLoggedUserId(), 
				UUID.fromString(elementId), 
				SubscriptionElementType.valueOf(elementType),
				subscriber);
		
		return new PostResult("success", "success", "");
	}
	
	@RequestMapping(path = "/notifications/disableEmails", method = RequestMethod.PUT)
	public PostResult unsuscribeFromInitiative() {
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		return appUserService.disableEmailNotifications(getLoggedUser().getC1Id());
	}
	
}