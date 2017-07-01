package org.collectiveone.modules.activity;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.InitiativeRepositoryIf;
import org.collectiveone.modules.initiatives.InitiativeService;
import org.collectiveone.modules.initiatives.Member;
import org.collectiveone.modules.tokens.InitiativeTransfer;
import org.collectiveone.modules.tokens.TokenType;
import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {
	
	@Autowired
	private InitiativeService initiativeService;
	
	@Autowired
	private EmailService emailService;
	
	
	@Autowired
	private AppUserRepositoryIf appUserRepository;
	
	@Autowired
	private InitiativeRepositoryIf initiativeRepository;
	
	@Autowired
	private ActivityRepositoryIf activityRepository;
	
	@Autowired
	private NotificationRepositoryIf notificationRepository;
	
	@Autowired
	private SubscriberRepositoryIf subscriberRepository;
	
	
	@Transactional
	public void sendPendingEmails() throws IOException {
		
		List<Notification> notifications = 
				notificationRepository.findBySubscriber_EmailNotificationsStateAndEmailState(
						SubscriberEmailNotificationsState.SEND_NOW, NotificationEmailState.PENDING);
		
		emailService.sendNotifications(notifications);

		for (Notification notification : notifications) {
			notification.setEmailState(NotificationEmailState.DELIVERED);
			notificationRepository.save(notification);
		}
		
	}
	
	@Transactional
	public GetResult<List<NotificationDto>> getUserNotifications(UUID userId) {
		
		List<NotificationDto> notifications = new ArrayList<NotificationDto>();
		
		for(Notification notification : notificationRepository.findBySubscriber_User_C1Id(userId)) {
			notifications.add(notification.toDto());
		}
		
		return new GetResult<List<NotificationDto>>("success", "notifications found", notifications);
		
	}
	
	@Transactional
	public PostResult notificationsRead(UUID userId) {
		for(Notification notification: notificationRepository.findBySubscriber_User_C1IdAndState(userId, NotificationState.PENDING)) {
			notification.setState(NotificationState.DELIVERED);
			notificationRepository.save(notification);
		}
		
		return new PostResult("success", "success", "");
	}
	
	@Transactional
	public void addSubscriber(UUID elementId, UUID userId, SubscriptionElementType type) {
		if (subscriberRepository.findByElementIdAndUser_C1Id(elementId, userId) == null) {
			Subscriber subscriber = new Subscriber();
			
			subscriber.setElementId(elementId);
			subscriber.setType(type);
			subscriber.setUser(appUserRepository.findByC1Id(userId));
			subscriber.setState(SubscriberState.SUBSCRIBED);
			subscriber.setEmailNotificationState(SubscriberEmailNotificationsState.SEND_NOW);
			
			subscriberRepository.save(subscriber);
		}
	}
	
	@Transactional
	public PostResult editSubscriberState(UUID userId, UUID elementId, SubscriberState state) {
		Subscriber subscriber = subscriberRepository.findByElementIdAndUser_C1Id(elementId, userId);
		subscriber.setState(state);
		subscriberRepository.save(subscriber);
		
		return new PostResult("success", "member notifications changed", "");
	}
	
	@Transactional
	public PostResult editSubscriberEmailNotificationsState(UUID userId, UUID elementId, SubscriberEmailNotificationsState emailNotificationsState) {
		Subscriber subscriber = subscriberRepository.findByElementIdAndUser_C1Id(elementId, userId);
		subscriber.setEmailNotificationState(emailNotificationsState);
		subscriberRepository.save(subscriber);
		
		return new PostResult("success", "subscriber email notifications changed", "");
	}
	
	@Transactional
	public GetResult<SubscriberDto> getSubscriber(UUID userId, UUID initiativeId) {
		Subscriber subscriber = subscriberRepository.findByElementIdAndUser_C1Id(initiativeId, userId);
		return new GetResult<SubscriberDto>("success", "success", subscriber.toDto());
	}
	
	/** Each user have one general purposed Susbscriber element used to send general notifications
	 * not associated to any initiative or element */
	@Transactional
	private Subscriber getOrCreateCollectiveOneSubscriber(UUID userId) {
		Subscriber subscriber = subscriberRepository.findByUser_C1IdAndType(userId, SubscriptionElementType.COLLECTIVEONE);
	}
	
	
	/* REGISTER NEW ACTIVITY */
	
	@Transactional
	public void newInitiativeCreated(Initiative initiative, AppUser triggerUser, TokenType token) {
		Activity activity = new Activity();
		
		activity.setType(ActivityType.INITIATIVE_CREATED);
		activity.setTriggerUser(triggerUser);
		activity.setInitiative(initiative);
		activity.setTimestamp(new Timestamp(System.currentTimeMillis()));
		
		activity = activityRepository.save(activity);
		
		addNewInitiativeNotifications(activity);
	}
		
	@Transactional
	public void newSubinitiativeCreated(Initiative initiative, AppUser triggerUser, Initiative subinitiative, List<InitiativeTransfer> transfers) {
		Activity activity = new Activity();
		
		activity.setType(ActivityType.SUBINITIATIVE_CREATED);
		activity.setTriggerUser(triggerUser);
		activity.setInitiative(initiative);
		activity.setTimestamp(new Timestamp(System.currentTimeMillis()));
		
		activity.setSubInitiative(subinitiative);
		for (InitiativeTransfer transfer : transfers) {
			activity.getInitiativeTransfers().add(transfer);
		}
		
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void initiativeEdited(Initiative initiative, AppUser triggerUser, String oldName, String oldDriver) {
		Activity activity = new Activity();
		
		activity.setType(ActivityType.INITIATIVE_EDITED);
		activity.setTriggerUser(triggerUser);
		activity.setInitiative(initiative);
		activity.setTimestamp(new Timestamp(System.currentTimeMillis()));
		
		activity.setOldName(oldName);
		activity.setOldDriver(oldDriver);
		
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	private void addNewInitiativeNotifications (Activity activity) {
		SortedSet<Member> members = activity.getInitiative().getMembers();
		
		for (Member member : members) {
			if(activity.getTriggerUser().getC1Id() != member.getUser().getC1Id()) {
				/* add a notification only if the trigger user is not the subscriber */
				Subscriber subscriber = getOrCreateCollectiveOneSubscriber(member.getUser().getC1Id());
				
				Notification notification = new Notification();
				notification.setActivity(activity);
				notification.setSubscriber(subscriber);
				notification.setState(NotificationState.PENDING);
				notification.setEmailState(NotificationEmailState.PENDING);
				
				notification = notificationRepository.save(notification);
				
				activity.getNotifications().add(notification);
			}
		}
	}
	
	
	/** To be called within an activity transaction, modifies the notifications property of the
	 * input activity */
	private void addInitiativeActivityNotifications (Activity activity) {
		List<Subscriber> subscribers = getInitiativeSubscribers(activity.getInitiative().getId());
		
		for (Subscriber subscriber : subscribers) {
			if(activity.getTriggerUser().getC1Id() != subscriber.getUser().getC1Id()) {
				/* add a notification only if the trigger user is not the subscriber */
				Notification notification = new Notification();
				notification.setActivity(activity);
				notification.setSubscriber(subscriber);
				notification.setState(NotificationState.PENDING);
				notification.setEmailState(NotificationEmailState.PENDING);
				
				notification = notificationRepository.save(notification);
				
				activity.getNotifications().add(notification);
			}
		}
	}
	
	@Transactional
	private List<Subscriber> getInitiativeSubscribers (UUID initiativeId) {
		
		/* example https://docs.google.com/drawings/d/1PqPhefzrGVlWVfG-SRGS56l_e2qpNEsajLbnsAWcTfA/edit,
		 * assume initiativeId = C */
		/* start with this initiative subscribers (S3 and S6 in example). Take into account that 
		 * a subscriber state may be SUBSCRIPTION_DISABLED */
		List<Subscriber> allSubscribers = subscriberRepository.findByElementId(initiativeId);
		
		/* then add the subscribers of all parent initiatives (B and A, in that order) */
		List<Initiative> parents = initiativeService.getParentInitiatives(initiativeId);
		
		for (Initiative parent : parents) {
			List<Subscriber> parentSubscribers = subscriberRepository.findByElementId(parent.getId());
			
			for (Subscriber parentSubscriber : parentSubscribers) {
				int ixOfSubscriber = indexOfSubscriber(allSubscribers, parentSubscriber);
				if (ixOfSubscriber == -1) {
					/* if this user is not already in the list of subscriptions, then
					 * add it (this means that the applicable subscription is that at
					 * the lowest level) */
					allSubscribers.add(parentSubscriber);
				}
			}
		}
		
		return allSubscribers;
	}
	
	private int indexOfSubscriber(List<Subscriber> subscribers, Subscriber parentSubscriber) {
		for (int ix = 0; ix < subscribers.size(); ix++) {
			Subscriber subscriber = subscribers.get(ix);
			if (subscriber.getUser().getC1Id() == parentSubscriber.getUser().getC1Id()) {
				return ix;
			}
		}
		return -1;
	}
	
}
