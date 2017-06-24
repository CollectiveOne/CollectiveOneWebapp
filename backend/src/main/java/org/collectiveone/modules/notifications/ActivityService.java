package org.collectiveone.modules.notifications;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.InitiativeRepositoryIf;
import org.collectiveone.modules.initiatives.InitiativeService;
import org.collectiveone.modules.users.AppUserRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {
	
	@Autowired
	private InitiativeService initiativeService;
	
	
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
	public void addSubscriber(UUID elementId, UUID userId, SubscriptionElementType type) {
		Subscriber subscriber = new Subscriber();
		
		subscriber.setElementId(elementId);
		subscriber.setType(type);
		subscriber.setUser(appUserRepository.findByC1Id(userId));
		subscriber.setState(SubscriberState.SUBSCRIBED);
		
		subscriberRepository.save(subscriber);
	}
	
	@Transactional
	public void addNewSubinitiative(UUID initiativeId, UUID subinitiativeId) {
		Activity activity = new Activity();
		
		activity.setType(ActivityType.SUBINITIATIVE_CREATED);
		activity.setInitiative(initiativeRepository.findById(initiativeId));
		activity.setTimestamp(new Timestamp(System.currentTimeMillis()));
		
		activity.setSubInitiative(initiativeRepository.findById(subinitiativeId));
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	} 
	
	/** To be called within an activity transaction, modifies the notifications property of the
	 * input activity */
	private void addInitiativeActivityNotifications (Activity activity) {
		List<Subscriber> subscribers = getInitiativeSubscribers(activity.getInitiative().getId());
		
		for (Subscriber subscriber : subscribers) {
			Notification notification = new Notification();
			notification.setActivity(activity);
			notification.setSubscriber(subscriber);
			notification.setState(NotificationState.PENDING);
			
			notification = notificationRepository.save(notification);
			
			activity.getNotifications().add(notification);
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
