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
import org.collectiveone.modules.activity.dto.NotificationDto;
import org.collectiveone.modules.activity.dto.PushInfoDto;
import org.collectiveone.modules.activity.dto.SubscriberDto;
import org.collectiveone.modules.activity.enums.ActivityType;
import org.collectiveone.modules.activity.enums.NotificationEmailState;
import org.collectiveone.modules.activity.enums.NotificationPushState;
import org.collectiveone.modules.activity.enums.NotificationState;
import org.collectiveone.modules.activity.enums.SubscriberEmailNotificationsState;
import org.collectiveone.modules.activity.enums.SubscriberState;
import org.collectiveone.modules.activity.enums.SubscriptionElementType;
import org.collectiveone.modules.activity.repositories.ActivityRepositoryIf;
import org.collectiveone.modules.activity.repositories.NotificationRepositoryIf;
import org.collectiveone.modules.activity.repositories.SubscriberRepositoryIf;
import org.collectiveone.modules.assignations.Assignation;
import org.collectiveone.modules.conversations.Message;
import org.collectiveone.modules.conversations.MessageService;
import org.collectiveone.modules.conversations.MessageThreadContextType;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.InitiativeService;
import org.collectiveone.modules.initiatives.Member;
import org.collectiveone.modules.initiatives.repositories.InitiativeRepositoryIf;
import org.collectiveone.modules.model.ModelCardWrapper;
import org.collectiveone.modules.model.ModelSection;
import org.collectiveone.modules.model.ModelView;
import org.collectiveone.modules.model.repositories.ModelCardWrapperRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelSectionRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelViewRepositoryIf;
import org.collectiveone.modules.tokens.InitiativeTransfer;
import org.collectiveone.modules.tokens.TokenMint;
import org.collectiveone.modules.tokens.TokenType;
import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {
	
	@Autowired
	private InitiativeService initiativeService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private MessageService messageService;

	@Autowired
	private PushMessageBuilder pushMessageBuilder;
	
	@Autowired
	private AppUserRepositoryIf appUserRepository;
	
	@Autowired
	private ActivityRepositoryIf activityRepository;
	
	@Autowired
	private NotificationRepositoryIf notificationRepository;
	
	@Autowired
	private SubscriberRepositoryIf subscriberRepository;
	
	@Autowired
	private InitiativeRepositoryIf initiativeRepository;
	
	@Autowired
	private ModelViewRepositoryIf modelViewRepository;
	
	@Autowired
	private ModelSectionRepositoryIf modelSectionRepository;
	
	@Autowired
	private ModelCardWrapperRepositoryIf modelCardWrapperRepository;
	
	
	@Transactional
	public void sendWantToContributeEmails() throws IOException {
		
		
	}
	
	@Transactional
	public void sendNotificationEmailsSendNow() throws IOException {
		
		List<Notification> notifications = 
				notificationRepository.findBySubscriber_EmailNotificationsStateAndEmailState(
						SubscriberEmailNotificationsState.SEND_NOW, NotificationEmailState.PENDING);
		
		emailService.sendNotificationsSendNow(notifications);
	}
	
	@Transactional
	public void sendNotificationEmailsOnceADay() throws IOException {
		
		List<Notification> notifications = 
				notificationRepository.findBySubscriber_EmailNotificationsStateAndEmailState(
						SubscriberEmailNotificationsState.SEND_ONCEADAY, NotificationEmailState.PENDING);
		
		emailService.sendNotificationsGrouped(notifications);
	}
	
	@Transactional
	public void sendNotificationEmailsOnceAWeek() throws IOException {
		
		List<Notification> notifications = 
				notificationRepository.findBySubscriber_EmailNotificationsStateAndEmailState(
						SubscriberEmailNotificationsState.SEND_ONCEAWEEK, NotificationEmailState.PENDING);
		
		emailService.sendNotificationsGrouped(notifications);
	}
	
	@Transactional
	public GetResult<List<NotificationDto>> getUserNotifications(UUID userId, PageRequest page) {
		
		List<NotificationDto> notifications = new ArrayList<NotificationDto>();
		NotificationDto notificationResult;
		PushInfoDto pushInfo;
		for(Notification notification : notificationRepository.findOfUser(userId, page)) {

			notificationResult = notification.toDto();
			pushInfo = pushMessageBuilder.getPushMessage(notification);
			notificationResult.setPushInfo(pushInfo);			
			notifications.add(notificationResult);
		}
		
		return new GetResult<List<NotificationDto>>("success", "notifications found", notifications);
	}
	
	@Transactional
	public PostResult notificationsRead(UUID userId) {
		for(Notification notification: notificationRepository.findBySubscriber_User_C1IdAndState(userId, NotificationState.PENDING)) {
			notification.setState(NotificationState.DELIVERED);
			notification.setEmailState(NotificationEmailState.DELIVERED);
			notificationRepository.save(notification);
		}
		
		return new PostResult("success", "success", "");
	}
	
	@Transactional
	public PostResult notificationsPushed(UUID userId) {
		for(Notification notification: notificationRepository.findBySubscriber_User_C1IdAndPushState(userId, NotificationPushState.PENDING)) {
			System.out.println(notification);
			
			notification.setPushState(NotificationPushState.PUSHED);
			notification.setEmailState(NotificationEmailState.DELIVERED);
			
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
			subscriber.setEmailNotificationsState(SubscriberEmailNotificationsState.SEND_ONCEADAY);
			
			subscriberRepository.save(subscriber);
		}
	}
	
	@Transactional
	public void removeSubscriber(UUID elementId, UUID userId) {
		Subscriber subscriber = subscriberRepository.findByElementIdAndUser_C1Id(elementId, userId);
		subscriberRepository.delete(subscriber);
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
		subscriber.setEmailNotificationsState(emailNotificationsState);
		subscriberRepository.save(subscriber);
		
		return new PostResult("success", "subscriber email notifications changed", "");
	}
	
	@Transactional
	public GetResult<SubscriberDto> getSubscriber(UUID userId, UUID initiativeId) {
		Subscriber subscriber = subscriberRepository.findByElementIdAndUser_C1Id(initiativeId, userId);
		if (subscriber != null) {
			return new GetResult<SubscriberDto>("success", "success", subscriber.toDto());	
		} else {
			return new GetResult<SubscriberDto>("success", "subscriber not found", null);
		}
		
	}
	
	/** Each user have one general purposed Susbscriber element used to send general notifications
	 * not associated to any initiative or element */
	@Transactional
	private Subscriber getOrCreateCollectiveOneSubscriber(UUID userId) {
		Subscriber subscriber = subscriberRepository.findByUser_C1IdAndType(userId, SubscriptionElementType.COLLECTIVEONE);

		if (subscriber != null) {
			return subscriber;
		}
		
		subscriber = new Subscriber();
		
		subscriber.setType(SubscriptionElementType.COLLECTIVEONE);
		subscriber.setUser(appUserRepository.findByC1Id(userId));
		subscriber.setState(SubscriberState.SUBSCRIBED);
		subscriber.setEmailNotificationsState(SubscriberEmailNotificationsState.SEND_NOW);
		
		return subscriberRepository.save(subscriber);
	}
	
	/**
	 * 
	 * First Step
	 * 
	 * */
	
	
	@Transactional
	public void newInitiativeCreated(Initiative initiative, AppUser triggerUser) {
		Activity activity = getBaseActivity(triggerUser, initiative); 
		
		activity.setType(ActivityType.INITIATIVE_CREATED);
		activity = activityRepository.save(activity);
		
		addNewInitiativeNotifications(activity);
	}
	
	@Transactional
	public void newTokenCreated(Initiative initiative, AppUser triggerUser, TokenType token, TokenMint mint) {
		Activity activity = getBaseActivity(triggerUser, initiative); 
		
		activity.setType(ActivityType.TOKEN_CREATED);
		activity.setTokenType(token);
		activity.setMint(mint);
		
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
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
	
	@Transactional
	public void tokensMinted(Initiative initiative, TokenMint mint) {
		Activity activity = new Activity();
		
		activity.setType(ActivityType.TOKENS_MINTED);
		activity.setTriggerUser(mint.getOrderedBy());
		activity.setInitiative(initiative);
		activity.setTimestamp(new Timestamp(System.currentTimeMillis()));
		
		activity.setMint(mint);
		
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void peerReviewedAssignationCreated(Assignation assignation, AppUser triggerUser) {
		Activity activity = new Activity();
		
		activity.setType(ActivityType.PR_ASSIGNATION_CREATED);
		activity.setTriggerUser(triggerUser);
		activity.setInitiative(assignation.getInitiative());
		activity.setTimestamp(new Timestamp(System.currentTimeMillis()));
		
		activity.setAssignation(assignation);
		
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void peerReviewedAssignationDone(Assignation assignation) {
		Activity activity = new Activity();
		
		activity.setType(ActivityType.PR_ASSIGNATION_DONE);
		activity.setTriggerUser(assignation.getCreator());
		activity.setInitiative(assignation.getInitiative());
		activity.setTimestamp(new Timestamp(System.currentTimeMillis()));
		
		activity.setAssignation(assignation);
		
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void directAssignationCreated(Assignation assignation, AppUser triggerUser) {
		Activity activity = new Activity();
		
		activity.setType(ActivityType.D_ASSIGNATION_CREATED);
		activity.setTriggerUser(triggerUser);
		activity.setInitiative(assignation.getInitiative());
		activity.setTimestamp(new Timestamp(System.currentTimeMillis()));
		
		activity.setAssignation(assignation);
		
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void transferToSubinitiative(InitiativeTransfer transfer) {
		Activity activity = new Activity();
		
		activity.setType(ActivityType.INITIATIVE_TRANSFER);
		activity.setTriggerUser(transfer.getOrderedBy());
		activity.setInitiative(transfer.getFrom());
		activity.setTimestamp(new Timestamp(System.currentTimeMillis()));
		
		activity.setInitiativeTransfer(transfer);
		
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void assignationRevertOrdered(Assignation assignation, AppUser triggerUser) {
		Activity activity = new Activity();
		
		activity.setType(ActivityType.ASSIGNATION_REVERT_ORDERED);
		activity.setTriggerUser(triggerUser);
		activity.setInitiative(assignation.getInitiative());
		activity.setTimestamp(new Timestamp(System.currentTimeMillis()));
		
		activity.setAssignation(assignation);
		
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void assignationRevertCancelled(Assignation assignation) {
		Activity revertOrder = activityRepository.findTop1ByAssignation_IdAndTypeOrderByTimestampDesc(assignation.getId(), ActivityType.ASSIGNATION_REVERT_ORDERED);
		
		Activity activity = new Activity();
		
		activity.setType(ActivityType.ASSIGNATION_REVERT_CANCELLED);
		activity.setTriggerUser(revertOrder.getTriggerUser());
		activity.setInitiative(assignation.getInitiative());
		activity.setTimestamp(new Timestamp(System.currentTimeMillis()));
		
		activity.setAssignation(assignation);
		
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void assignationReverted(Assignation assignation) {
		Activity revertOrder = activityRepository.findTop1ByAssignation_IdAndTypeOrderByTimestampDesc(assignation.getId(), ActivityType.ASSIGNATION_REVERT_ORDERED);
		
		Activity activity = new Activity();
		
		activity.setType(ActivityType.ASSIGNATION_REVERTED);
		activity.setTriggerUser(revertOrder.getTriggerUser());
		activity.setInitiative(assignation.getInitiative());
		activity.setTimestamp(new Timestamp(System.currentTimeMillis()));
		
		activity.setAssignation(assignation);
		
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void assignationDeleted(Assignation assignation, AppUser triggerUser) {
		Activity activity = new Activity();
		
		activity.setType(ActivityType.ASSIGNATION_DELETED);
		activity.setTriggerUser(triggerUser);
		activity.setInitiative(assignation.getInitiative());
		activity.setTimestamp(new Timestamp(System.currentTimeMillis()));
		
		activity.setAssignation(assignation);
		
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void initiativeDeleted(Initiative initiative, AppUser triggerUser) {
		Activity activity = new Activity();
		
		activity.setType(ActivityType.INITIATIVE_DELETED);
		activity.setTriggerUser(triggerUser);
		activity.setInitiative(initiative);
		activity.setTimestamp(new Timestamp(System.currentTimeMillis()));
		
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void modelViewCreated(ModelView view, AppUser triggerUser) {
		Activity activity = getBaseActivity(triggerUser, view.getInitiative()); 
		
		activity.setType(ActivityType.MODEL_VIEW_CREATED);
		activity.setModelView(view);
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void modelViewEdited(ModelView view, AppUser triggerUser) {
		Activity activity = getBaseActivity(triggerUser, view.getInitiative()); 
		
		activity.setType(ActivityType.MODEL_VIEW_EDITED);
		activity.setModelView(view);
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void modelViewDeleted(ModelView view, AppUser triggerUser) {
		Activity activity = getBaseActivity(triggerUser, view.getInitiative()); 
		
		activity.setType(ActivityType.MODEL_VIEW_DELETED);
		activity.setModelView(view);
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void modelSectionCreatedOnSection(ModelSection section, ModelSection onSection, AppUser triggerUser) {
		Activity activity = getBaseActivity(triggerUser, section.getInitiative()); 
		activity.setType(ActivityType.MODEL_SECTION_CREATED);
		activity.setModelSection(section);
		activity.setOnSection(onSection);
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
		addSubscriber(section.getId(),triggerUser.getC1Id(), SubscriptionElementType.SUBSECTION);
	}
	
	@Transactional
	public void modelSectionCreatedOnView(ModelSection section, ModelView onView, AppUser triggerUser) {
		Activity activity = getBaseActivity(triggerUser, section.getInitiative()); 
		
		activity.setType(ActivityType.MODEL_SECTION_CREATED);
		activity.setModelSection(section);
		activity.setOnView(onView);
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
		addSubscriber(section.getId(),triggerUser.getC1Id(), SubscriptionElementType.SECTION);
	}
	
	@Transactional
	public void modelSectionEdited(ModelSection section, AppUser triggerUser) {
		Activity activity = getBaseActivity(triggerUser, section.getInitiative()); 
		
		activity.setType(ActivityType.MODEL_SECTION_EDITED);
		activity.setModelSection(section);
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void modelSectionRemovedFromSection(ModelSection section, ModelSection fromSection, AppUser triggerUser) {
		Activity activity = getBaseActivity(triggerUser, section.getInitiative()); 
		
		activity.setType(ActivityType.MODEL_SECTION_REMOVED);
		activity.setModelSection(section);
		activity.setFromSection(fromSection);
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void modelSectionRemovedFromView(ModelSection section, ModelView fromView, AppUser triggerUser) {
		Activity activity = getBaseActivity(triggerUser, section.getInitiative()); 
		
		activity.setType(ActivityType.MODEL_SECTION_REMOVED);
		activity.setModelSection(section);
		activity.setFromView(fromView);
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void modelViewMoved(ModelView view, AppUser triggerUser) {
		Activity activity = getBaseActivity(triggerUser, view.getInitiative()); 
		
		activity.setType(ActivityType.MODEL_VIEW_MOVED);
		activity.setModelView(view);
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void modelSectionMovedInView(ModelSection section, ModelView onView, AppUser triggerUser) {
		Activity activity = getBaseActivity(triggerUser, section.getInitiative()); 
		
		activity.setType(ActivityType.MODEL_SECTION_MOVED);
		activity.setModelSection(section);
		activity.setFromView(onView);
		activity.setOnView(onView);
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void modelSectionMovedFromViewToSection(ModelSection section, ModelView fromView, ModelSection onSection, AppUser triggerUser) {
		Activity activity = getBaseActivity(triggerUser, section.getInitiative()); 
		
		activity.setType(ActivityType.MODEL_SECTION_MOVED);
		activity.setModelSection(section);
		activity.setFromView(fromView);
		activity.setOnSection(onSection);
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void modelSectionMovedFromSectionToSection(ModelSection section, ModelSection fromSection, ModelSection onSection, AppUser triggerUser) {
		Activity activity = getBaseActivity(triggerUser, section.getInitiative()); 
		
		activity.setType(ActivityType.MODEL_SECTION_MOVED);
		activity.setModelSection(section);
		activity.setFromSection(fromSection);
		activity.setOnSection(onSection);
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void modelSectionMovedFromSectionToView(ModelSection section, ModelSection fromSection, ModelView onView, AppUser triggerUser) {
		Activity activity = getBaseActivity(triggerUser, section.getInitiative()); 
		
		activity.setType(ActivityType.MODEL_SECTION_MOVED);
		activity.setModelSection(section);
		activity.setFromSection(fromSection);
		activity.setOnView(onView);
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void modelNewSubsection(ModelSection section, ModelSection onSection, AppUser triggerUser) {
		Activity activity = getBaseActivity(triggerUser, section.getInitiative()); 
		
		activity.setType(ActivityType.MODEL_SECTION_CREATED);
		activity.setModelSection(section);
		activity.setOnSection(onSection);
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
		addSubscriber(section.getId(),triggerUser.getC1Id(), SubscriptionElementType.SUBSECTION);
	}
	
	@Transactional
	public void modelNewSection(ModelSection section, ModelView onView, AppUser triggerUser) {
		Activity activity = getBaseActivity(triggerUser, section.getInitiative()); 
		
		activity.setType(ActivityType.MODEL_SECTION_CREATED);
		activity.setModelSection(section);
		activity.setOnView(onView);
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
		addSubscriber(section.getId(),triggerUser.getC1Id(), SubscriptionElementType.SECTION);
		//add section to the subscribers
	}
	
	@Transactional
	public void modelCardWrapperAdded(ModelCardWrapper cardWrapper, ModelSection onSection, AppUser triggerUser) {
		Activity activity = getBaseActivity(triggerUser, cardWrapper.getInitiative()); 
		
		activity.setType(ActivityType.MODEL_CARDWRAPPER_ADDED);
		activity.setModelCardWrapper(cardWrapper);
		activity.setOnSection(onSection);
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void modelCardWrapperRemoved(ModelCardWrapper cardWrapper, ModelSection fromSection, AppUser triggerUser) {
		Activity activity = getBaseActivity(triggerUser, cardWrapper.getInitiative()); 
		
		activity.setType(ActivityType.MODEL_CARDWRAPPER_REMOVED);
		activity.setModelCardWrapper(cardWrapper);
		activity.setFromSection(fromSection);
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void modelSectionDeleted(ModelSection section, AppUser triggerUser) {
		Activity activity = getBaseActivity(triggerUser, section.getInitiative()); 
		
		activity.setType(ActivityType.MODEL_SECTION_DELETED);
		activity.setModelSection(section);
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void modelCardWrapperCreated(ModelCardWrapper cardWrapper, ModelSection onSection, AppUser triggerUser) {
		Activity activity = getBaseActivity(triggerUser, cardWrapper.getInitiative()); 
		
		activity.setType(ActivityType.MODEL_CARDWRAPPER_CREATED);
		activity.setModelCardWrapper(cardWrapper);
		activity.setOnSection(onSection);
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void modelCardWrapperEdited(ModelCardWrapper cardWrapper, AppUser triggerUser) {
		Activity activity = getBaseActivity(triggerUser, cardWrapper.getInitiative()); 
		
		activity.setType(ActivityType.MODEL_CARDWRAPPER_EDITED);
		activity.setModelCardWrapper(cardWrapper);
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void modelCardWrapperMoved(ModelCardWrapper cardWrapper, ModelSection fromSection, ModelSection onSection, AppUser triggerUser) {
		Activity activity = getBaseActivity(triggerUser, cardWrapper.getInitiative()); 
		
		activity.setType(ActivityType.MODEL_CARDWRAPPER_MOVED);
		activity.setModelCardWrapper(cardWrapper);
		activity.setFromSection(fromSection);
		activity.setOnSection(onSection);
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void modelCardWrapperDeleted(ModelCardWrapper cardWrapper, AppUser triggerUser) {
		Activity activity = getBaseActivity(triggerUser, cardWrapper.getInitiative()); 
		
		activity.setType(ActivityType.MODEL_CARDWRAPPER_DELETED);
		activity.setModelCardWrapper(cardWrapper);
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	@Transactional
	public void messagePosted(Message message, AppUser triggerUser, MessageThreadContextType contextType, UUID elementId) {
		
		Initiative initiative = initiativeRepository.findById(messageService.getInitiativeIdOfMessageThread(message.getThread()));
		Activity activity = getBaseActivity(triggerUser, initiative); 
		
		activity.setType(ActivityType.MESSAGE_POSTED);
		activity.setMessage(message);
		setMessageLocation(activity, contextType, elementId);
		
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
	}
	
	private void setMessageLocation(Activity activity, MessageThreadContextType contextType, UUID elementId) {
		switch (contextType) {
			
			case MODEL_CARD:
				activity.setModelCardWrapper(modelCardWrapperRepository.findById(elementId));
				break;
			
			case MODEL_SECTION:
				activity.setModelSection(modelSectionRepository.findById(elementId));
				break;
				
			case MODEL_VIEW:
				activity.setModelView(modelViewRepository.findById(elementId));
				break;
				
			case INITIATIVE:
				activity.setInitiative(initiativeRepository.findById(elementId));
				break;
				
		}
	}
	
	
	
	private Activity getBaseActivity(AppUser triggerUser, Initiative initiative) {
		Activity activity = new Activity();
		
		activity.setTriggerUser(triggerUser);
		activity.setInitiative(initiative);
		activity.setTimestamp(new Timestamp(System.currentTimeMillis()));

		return activity;
	}
	
	
	
	/**
	 * 
	 * Second Step
	 * 
	 * */
	
	
	
	private void addNewInitiativeNotifications (Activity activity) {
		SortedSet<Member> members = activity.getInitiative().getMembers();
		
		for (Member member : members) {
			if(activity.getTriggerUser().getC1Id() != member.getUser().getC1Id()) {
				/* add a notification only if the trigger user is not the subscriber */
				Subscriber subscriber = getOrCreateCollectiveOneSubscriber(member.getUser().getC1Id());
				
				if (subscriber.getState() != SubscriberState.UNSUBSCRIBED) {
					/* prepare a notification only if the subscriber is subscribed */
					
					Notification notification = new Notification();
					notification.setCreationDate(new Timestamp(System.currentTimeMillis()));
					notification.setActivity(activity);
					notification.setSubscriber(subscriber);
					
					notification.setState(NotificationState.PENDING);
					notification.setPushState(NotificationPushState.PENDING);
					/* if not unsubscribed from emails, set the email as pending */
					if (subscriber.getEmailNotificationsState() != SubscriberEmailNotificationsState.DISABLED) {
						notification.setEmailState(NotificationEmailState.PENDING);
					} else {
						notification.setEmailState(NotificationEmailState.DELIVERED);
					}
					
					notification = notificationRepository.save(notification);
					
					activity.getNotifications().add(notification);
				}
				
			}
		}
	}
	
	
	/** To be called within an activity transaction, modifies the notifications property of the
	 * input activity */
	private void addInitiativeActivityNotifications (Activity activity) {
		List<Subscriber> initiative_subscribers = getInitiativeSubscribers(activity.getInitiative().getId());
		List<Subscriber> section_subscribers = null;
		if(activity.getModelSection() != null) {
			section_subscribers = getSectionSubscribers(activity.getModelSection().getId());
		}
		for (Subscriber subscriber : initiative_subscribers) {
			if (subscriber.getState() != SubscriberState.UNSUBSCRIBED) {
				if(activity.getTriggerUser().getC1Id() != subscriber.getUser().getC1Id()) {
					/* add a notification only if the trigger user is not the subscriber */

					/*add a notification only if the user is subscribed to the section*/
					if (subscriber.getState() == SubscriberState.SUBSCRIBED) {

						if(section_subscribers != null) {
							for(Subscriber section_subscriber : section_subscribers) {
								if (section_subscriber.getState() == SubscriberState.SUBSCRIBED) {
									addNotification(activity,subscriber);
								}
							}
						}else {
							addNotification(activity,subscriber);
						}
						
					}
				}
			}
			
		}
	}
	
	private void addNotification(Activity activity, Subscriber subscriber) {
		Notification notification = new Notification();
		notification.setCreationDate(new Timestamp(System.currentTimeMillis()));
		notification.setActivity(activity);
		notification.setSubscriber(subscriber);
		notification.setState(NotificationState.PENDING);
		notification.setPushState(NotificationPushState.PENDING);
		
		/* if not unsubscribed from emails, set the email as peding */
		if (subscriber.getEmailNotificationsState() != SubscriberEmailNotificationsState.DISABLED 
				&& subscriber.getUser().getEmailNotificationsEnabled()) {
			notification.setEmailState(NotificationEmailState.PENDING);
		} else {
			notification.setEmailState(NotificationEmailState.DELIVERED);
		}
		
		notification = notificationRepository.save(notification);
		
		activity.getNotifications().add(notification);
	}
	
	@Transactional
	private List<Subscriber> getInitiativeSubscribers (UUID initiativeId) {
		
		/* example https://docs.google.com/drawings/d/1PqPhefzrGVlWVfG-SRGS56l_e2qpNEsajLbnsAWcTfA/edit,
		 * assume initiativeId = C */
		/* start with this initiative subscribers (S3 and S6 in example). Take into account that 
		 * a subscriber state may be SUBSCRIPTION_DISABLED */
		List<Subscriber> allSubscribers = subscriberRepository.findByElementId(initiativeId);
		
		/* then add the subscribers of all parent initiatives 2(B and A, in that order) */
		List<Initiative> parents = initiativeService.getParentGenealogyInitiatives(initiativeId);
		
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
	
	@Transactional
	private List<Subscriber> getSectionSubscribers (UUID sectionId) {
		
		/* example https://docs.google.com/drawings/d/1PqPhefzrGVlWVfG-SRGS56l_e2qpNEsajLbnsAWcTfA/edit,
		 * assume initiativeId = C */
		/* start with this initiative subscribers (S3 and S6 in example). Take into account that 
		 * a subscriber state may be SUBSCRIPTION_DISABLED */
		List<Subscriber> allSubscribers = subscriberRepository.findByElementId(sectionId);
		
		/* then add the subscribers of all parent initiatives (B and A, in that order) */
		List<Initiative> parents = initiativeService.getParentGenealogyInitiatives(sectionId);
		
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
