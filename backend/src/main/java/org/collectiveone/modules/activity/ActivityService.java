package org.collectiveone.modules.activity;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.activity.dto.NotificationDto;
import org.collectiveone.modules.activity.dto.SubscriberDto;
import org.collectiveone.modules.activity.enums.ActivityType;
import org.collectiveone.modules.activity.enums.NotificationContextType;
import org.collectiveone.modules.activity.enums.NotificationState;
import org.collectiveone.modules.activity.enums.SubscriberEmailNowConfig;
import org.collectiveone.modules.activity.enums.SubscriberEmailSummaryConfig;
import org.collectiveone.modules.activity.enums.SubscriberEmailSummaryPeriodConfig;
import org.collectiveone.modules.activity.enums.SubscriberInAppConfig;
import org.collectiveone.modules.activity.enums.SubscriberInheritConfig;
import org.collectiveone.modules.activity.enums.SubscriberPushConfig;
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
import org.collectiveone.modules.model.GraphNode;
import org.collectiveone.modules.model.ModelCardWrapper;
import org.collectiveone.modules.model.ModelSection;
import org.collectiveone.modules.model.ModelService;
import org.collectiveone.modules.model.repositories.ModelCardWrapperRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelSectionRepositoryIf;
import org.collectiveone.modules.tokens.InitiativeTransfer;
import org.collectiveone.modules.tokens.TokenMint;
import org.collectiveone.modules.tokens.TokenType;
import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserRepositoryIf;
import org.collectiveone.modules.users.UserOnlineStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {
	
	@Autowired
	private InitiativeService initiativeService;
	
	@Autowired
	private ModelService modelService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private NotificationDtoBuilder notificationDtoBuilder;
	
	
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
	private ModelSectionRepositoryIf modelSectionRepository;
	
	@Autowired
	private ModelCardWrapperRepositoryIf modelCardWrapperRepository;
	
	
	@Autowired
	SimpMessagingTemplate template;
	
	@Transactional
	public void sendWantToContributeEmails() throws IOException {
		
		
	}
	
	@Transactional
	public void sendNotificationEmailsSendNow() throws IOException {
		
		List<Notification> notifications = 
				notificationRepository.findByEmailNowState(NotificationState.PENDING);
		
		emailService.sendNotificationsSendNow(notifications);
	}
	
	@Transactional
	public void sendNotificationEmailsOnceADay() throws IOException {
		
		List<Notification> notifications = 
				notificationRepository.findBySubscriber_EmailSummaryPeriodConfigAndEmailSummaryState(
						SubscriberEmailSummaryPeriodConfig.DAILY, NotificationState.PENDING);
		
		emailService.sendNotificationsGrouped(notifications);
	}
	
	@Transactional
	public void sendNotificationEmailsOnceAWeek() throws IOException {
		
		List<Notification> notifications = 
				notificationRepository.findBySubscriber_EmailSummaryPeriodConfigAndEmailSummaryState(
						SubscriberEmailSummaryPeriodConfig.WEEKLY, NotificationState.PENDING);
		
		emailService.sendNotificationsGrouped(notifications);
	}
	
	private List<Notification> userUnreadNotifications(
			UUID userId, 
			NotificationContextType contextType, 
			UUID elementId,
			PageRequest page) {
		
		List<UUID> allSectionIds = null;
		List<UUID> cardsIds = null;
		
		switch (contextType) {
			case MODEL_SECTION:
				allSectionIds = modelService.getAllSubsectionsIds(elementId, null);
				cardsIds = allSectionIds.size() > 0 ? modelCardWrapperRepository.findAllCardsIdsOfSections(allSectionIds) : new ArrayList<UUID>();
				break;
				
			case MODEL_CARD:
				allSectionIds = new ArrayList<UUID>();
				cardsIds = new ArrayList<UUID>();
				cardsIds.add(elementId);
				break;
				
			default:
				break;
		
		}
		
		if (allSectionIds.size() == 0) {
			allSectionIds.add(UUID.randomUUID());
		}
		
		if (cardsIds.size() == 0) {
			cardsIds.add(UUID.randomUUID());
		}
		
		return notificationRepository.findOfUserInSections(userId, NotificationState.PENDING, allSectionIds, cardsIds, page);
	}
	
	@Transactional
	public GetResult<List<NotificationDto>> getUserNotifications(
			UUID userId, 
			NotificationContextType contextType, 
			UUID elementId,
			PageRequest page,
			Boolean isHtml) {
		
		List<NotificationDto> notificationsDtos = new ArrayList<NotificationDto>();
		
		List<Notification> notifications = userUnreadNotifications(userId, contextType, elementId, page);
		
		for(Notification notification : notifications) {
			notificationsDtos.add(notificationDtoBuilder.getNotificationDto(notification, isHtml));
		}
		
		return new GetResult<List<NotificationDto>>("success", "notifications found", notificationsDtos);
	}
	
	@Transactional
	public PostResult notificationsRead(
			UUID userId, 
			NotificationContextType contextType, 
			UUID elementId ) {
		
		List<Notification> notifications = userUnreadNotifications(userId, contextType, elementId, null);
		
		for(Notification notification: notifications) {
			
			notification.setInAppState(NotificationState.DELIVERED);
			notification.setPushState(NotificationState.DELIVERED);
			notification.setEmailNowState(NotificationState.DELIVERED);
			notification.setEmailSummaryState(NotificationState.DELIVERED);
			
			notificationRepository.save(notification);
		}
		
		return new PostResult("success", "success", "");
	}
	
	@Transactional
	public PostResult notificationPushed(
			UUID notificationId ) {
		
		Notification notification = notificationRepository.findById(notificationId);
		
		if (notification == null) {
			return new PostResult("error", "notification not found", "");
		}
		
		notification.setPushState(NotificationState.DELIVERED);
		notificationRepository.save(notification);
		
		return new PostResult("success", "notification updated", "");
	}
	
	@Transactional
	public void addSubscriber(UUID elementId, UUID userId, SubscriptionElementType type) {
		if (subscriberRepository.findByElementIdAndTypeAndUser_C1Id(elementId, type, userId) == null) {
			Subscriber subscriber = new Subscriber();
			
			subscriber.setElementId(elementId);
			subscriber.setType(type);
			subscriber.setUser(appUserRepository.findByC1Id(userId));
			
			subscriber.setInheritConfig(SubscriberInheritConfig.INHERIT);
			
			initDefaultSubscriber(subscriber);
			
			subscriberRepository.save(subscriber);
		}
	}
	
	@Transactional
	public void removeSubscriber(UUID elementId, SubscriptionElementType type, UUID userId) {
		Subscriber subscriber = type == 
				SubscriptionElementType.COLLECTIVEONE ? 
						subscriberRepository.findByUser_C1IdAndType(userId, type) :
						subscriberRepository.findByElementIdAndTypeAndUser_C1Id(elementId, type, userId);
						
		subscriberRepository.delete(subscriber);
	}
	
	
	@Transactional
	public PostResult editSubscriber(UUID userId, UUID elementId, SubscriptionElementType type, SubscriberDto subscriberDto) {
		Subscriber subscriber = getOrCreateSubscriber(elementId, type, userId);
				
		subscriber.setInheritConfig(SubscriberInheritConfig.valueOf(subscriberDto.getInheritConfig()));
		
		subscriber.setInAppConfig(SubscriberInAppConfig.valueOf(subscriberDto.getInAppConfig()));
		subscriber.setPushConfig(SubscriberPushConfig.valueOf(subscriberDto.getPushConfig()));
		subscriber.setEmailNowConfig(SubscriberEmailNowConfig.valueOf(subscriberDto.getEmailNowConfig()));
		subscriber.setEmailSummaryConfig(SubscriberEmailSummaryConfig.valueOf(subscriberDto.getEmailSummaryConfig()));
		subscriber.setEmailSummaryPeriodConfig(SubscriberEmailSummaryPeriodConfig.valueOf(subscriberDto.getEmailSummaryPeriodConfig()));
				
		subscriberRepository.save(subscriber);
		
		return new PostResult("success", "member notifications changed", "");
	}
	
	/* central DTO conversion to add context element logic */
	private SubscriberDto getSubscriberDto(Subscriber subscriber) {
		
		SubscriberDto subscriberDto = subscriber.toDto();
		
		/* add dto of the context element */
		switch (subscriber.getType()) {
			case SECTION:
				subscriberDto.setSection(modelSectionRepository.findById(subscriber.getElementId()).toDtoLight());
				break;
			
			case INITIATIVE:
				subscriberDto.setInitiative(initiativeRepository.findById(subscriber.getElementId()).toDto());
				break;
				
			default: 
				break;
		}
		
		if (subscriber.getInheritConfig() == SubscriberInheritConfig.INHERIT) {
			Subscriber applicableSubscriber = getApplicableSubscriber(
					subscriber.getUser().getC1Id(), 
					subscriber.getType(), 
					subscriber.getElementId());
			
			SubscriberDto applicableSubscriberDto = applicableSubscriber.toDto();
			
			switch (applicableSubscriber.getType()) {
				case SECTION:
					applicableSubscriberDto.setSection(modelSectionRepository.findById(applicableSubscriber.getElementId()).toDtoLight());
					break;
				
				case INITIATIVE:
					applicableSubscriberDto.setInitiative(initiativeRepository.findById(applicableSubscriber.getElementId()).toDto());
					break;
					
				default: 
					break;
			}
			
			subscriberDto.setApplicableSubscriber(applicableSubscriberDto);
		}
		
		return subscriberDto; 
	}
	
	@Transactional
	public GetResult<SubscriberDto> getSubscriberInheritFrom(UUID userId, UUID elementId, SubscriptionElementType type) {
		Subscriber applicableSubscriber = getApplicableSubscriber(userId, type, elementId);
		return new GetResult<SubscriberDto>("success", "success", getSubscriberDto(applicableSubscriber));
	}
	
	@Transactional
	public GetResult<SubscriberDto> getSubscriber(UUID userId, UUID elementId, SubscriptionElementType type) {
		Subscriber subscriber = type == 
			SubscriptionElementType.COLLECTIVEONE ? 
					subscriberRepository.findByUser_C1IdAndType(userId, type) :
					subscriberRepository.findByElementIdAndTypeAndUser_C1Id(elementId, type, userId);
					
		SubscriberDto subscriberDto = null;
		
		if (subscriber == null) {
			subscriber = new Subscriber();
			
			subscriber.setType(type);
			subscriber.setElementId(elementId);
			subscriber.setInheritConfig(SubscriberInheritConfig.INHERIT);
			
			initDefaultSubscriber(subscriber);
			
			subscriber.setUser(appUserRepository.findByC1Id(userId));
			
			subscriberDto = getSubscriberDto(subscriber);
			
		} else {
			subscriberDto = getSubscriberDto(subscriber);
		}
		
		return new GetResult<SubscriberDto>("success", "success", subscriberDto);
		
	}
	
	/** Each user have one general purposed Susbscriber element used to send general notifications
	 * not associated to any initiative or element */
	@Transactional
	private Subscriber getOrCreateSubscriber(UUID elementId, SubscriptionElementType type, UUID userId) {
		Subscriber subscriber = type == 
				SubscriptionElementType.COLLECTIVEONE ? 
						subscriberRepository.findByUser_C1IdAndType(userId, type) :
						subscriberRepository.findByElementIdAndTypeAndUser_C1Id(elementId, type, userId);

		if (subscriber != null) {
			return subscriber;
		}
		
		subscriber = new Subscriber();
		
		subscriber.setUser(appUserRepository.findByC1Id(userId));
		subscriber.setElementId(elementId);
		subscriber.setType(type);
		
		initDefaultSubscriber(subscriber);
		
		return subscriberRepository.save(subscriber);
	}
	
	public void initDefaultSubscriber(Subscriber subscriber) {
		subscriber.setInAppConfig(SubscriberInAppConfig.ALL_EVENTS);
		subscriber.setPushConfig(SubscriberPushConfig.ONLY_MESSAGES);
		subscriber.setEmailNowConfig(SubscriberEmailNowConfig.DISABLED);
		subscriber.setEmailSummaryConfig(SubscriberEmailSummaryConfig.ALL_EVENTS);
		subscriber.setEmailSummaryPeriodConfig(SubscriberEmailSummaryPeriodConfig.DAILY);
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
	public void modelSectionCreated(ModelSection section, AppUser triggerUser) {
		Activity activity = getBaseActivity(triggerUser, section.getInitiative()); 
		
		activity.setType(ActivityType.MODEL_SECTION_CREATED);
		activity.setModelSection(section);
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
	public void modelNewSubsection(ModelSection section, ModelSection onSection, AppUser triggerUser) {
		Activity activity = getBaseActivity(triggerUser, section.getInitiative()); 
		
		activity.setType(ActivityType.MODEL_SECTION_CREATED);
		activity.setModelSection(section);
		activity.setOnSection(onSection);
		activity = activityRepository.save(activity);
		
		addInitiativeActivityNotifications(activity);
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
	
	/** To be called within an activity transaction, modifies the notifications property of the
	 * input activity */
	
	/* Special case of new initiative created. ALl members are notified, not the subscribers */
	private void addNewInitiativeNotifications (Activity activity) {
		SortedSet<Member> members = activity.getInitiative().getMembers();
		
		for (Member member : members) {
			if(activity.getTriggerUser().getC1Id() != member.getUser().getC1Id()) {
				/* add a notification only if the trigger user is not the subscriber */
				Subscriber subscriber = subscriberRepository.findByUser_C1IdAndType(member.getUser().getC1Id(), SubscriptionElementType.COLLECTIVEONE);
				createSubscriberNotification(subscriber, activity);
			}
		}
	}
	
	private Boolean isInModel(Activity activity) {
		Boolean isInModel = false;
		/* subscribers are derived from the sections if activity is on a card or section*/
		switch (activity.getType()) {
			case MODEL_CARDWRAPPER_ADDED:
			case MODEL_CARDWRAPPER_DELETED:
			case MODEL_CARDWRAPPER_CREATED:
			case MODEL_CARDWRAPPER_EDITED:
			case MODEL_CARDWRAPPER_MOVED:
			case MODEL_CARDWRAPPER_REMOVED:
			case MODEL_SECTION_ADDED:
			case MODEL_SECTION_CREATED:
			case MODEL_SECTION_DELETED:
			case MODEL_SECTION_EDITED:
			case MODEL_SECTION_MOVED:
			case MODEL_SECTION_REMOVED:
				
				isInModel = true;
				break;
				
			case MESSAGE_POSTED:
				isInModel = (activity.getModelCardWrapper() != null) 
					|| (activity.getModelSection() != null);
				break;
			
			default:
				break;			
		}
		return isInModel;
	}
	
	private void addInitiativeActivityNotifications (Activity activity) {
		
		createNotifications(activity);
		
		/* broadcast the event to connected websockets */
		broadcastMessage(activity);
	}
	
	private List<UUID> directlyAffectedSectionsIds(Activity activity) {
		List<ModelSection> sections = null;
		switch (activity.getType()) {
			case MODEL_CARDWRAPPER_ADDED:
			case MODEL_CARDWRAPPER_DELETED:
			case MODEL_CARDWRAPPER_CREATED:
			case MODEL_CARDWRAPPER_EDITED:
			case MODEL_CARDWRAPPER_MOVED:
			case MODEL_CARDWRAPPER_REMOVED:
				
				/* activity in cards is considered as occurring on the sections these card is placed*/
				sections = modelCardWrapperRepository.findParentSections(activity.getModelCardWrapper().getId());
				break;
				
			case MESSAGE_POSTED:
				sections = new ArrayList<ModelSection>();
				
				/* message context is derived from the existence or not of the associated section */
				if (activity.getModelSection() != null) {
					sections.add(activity.getModelSection());	
				} 
				
				if (activity.getModelCardWrapper() != null) {
					sections.addAll(modelCardWrapperRepository.findParentSections(activity.getModelCardWrapper().getId()));
				}
				
				break;
				
			default: 
				/* activity in section applies to that section only */
				sections = new ArrayList<ModelSection>();
				sections.add(activity.getModelSection());
				break;
		}
		
		List<UUID> sectionsIds = new ArrayList<UUID>();
		
		for (ModelSection section : sections) {
			sectionsIds.add(section.getId());
		}
		
		return sectionsIds;
	}
	
	/* this method must be consistent with the create notification method below. Otherwise the user wont know
	 * what the hell is going on... */
	private Subscriber getApplicableSubscriber(UUID userId, SubscriptionElementType elementType, UUID elementId) {
		Subscriber applicableSubscriber = null; 
		
		/* check if start on section */
		if (elementType == SubscriptionElementType.SECTION) {
			applicableSubscriber = findSubscriberOnSectionsRec(userId, elementId, new HashSet<UUID>());
			
			/* if found in sections */
			if (applicableSubscriber != null) {
				return applicableSubscriber;
			}
		}
		
		/* if not found in section or not relative to section, look on initiatives */
		applicableSubscriber = findSubscriberOnInitiatives(userId, elementId);
		
		/* if not found a CUSTOM subscriber in any section or initiative, use the user global subscriber */
		if (applicableSubscriber == null) {
			applicableSubscriber = subscriberRepository.findByUser_C1IdAndType(userId, SubscriptionElementType.COLLECTIVEONE);
		}
		
		return applicableSubscriber;		
	}
	
	private Subscriber findSubscriberOnSectionsRec(UUID userId, UUID sectionId, Set<UUID> readIds) {
		Subscriber subscriber = subscriberRepository.findByElementIdAndTypeAndUser_C1Id(sectionId, SubscriptionElementType.SECTION, userId);
		readIds.add(sectionId);
		
		/* if found, return it, otherwise look for it recursively in the parent sections*/
		if (subscriber != null) {
			if (subscriber.getInheritConfig() == SubscriberInheritConfig.CUSTOM) {
				return subscriber;	
			}
		}
		
		GraphNode sectionNode = modelService.getSectionNode(sectionId, true, false, 2);
		
		for (GraphNode parent : sectionNode.getParents()) {
			/* recursively add parent subscribers*/
			return findSubscriberOnSectionsRec(userId, parent.getElementId(), readIds);
		}
		
		/* if no parents, return null*/
		return null;
		
	}
	
	private Subscriber findSubscriberOnInitiatives(UUID userId, UUID initiativeId) {
		
		Subscriber subscriber = subscriberRepository.findByElementIdAndTypeAndUser_C1Id(initiativeId, SubscriptionElementType.INITIATIVE, userId);
		
		if (subscriber != null) {
			if (subscriber.getInheritConfig() == SubscriberInheritConfig.CUSTOM) {
				return subscriber;	
			}
		} 
		
		
		List<Initiative> parents = initiativeService.getParentGenealogyInitiatives(initiativeId);
		
		for (Initiative parent : parents) {
			subscriber = subscriberRepository.findByElementIdAndTypeAndUser_C1Id(parent.getId(), SubscriptionElementType.INITIATIVE, userId);
			if (subscriber != null) {
				if (subscriber.getInheritConfig() == SubscriberInheritConfig.CUSTOM) {
					return subscriber;
				}
			}
		}
		
		/* this should not occur, at least one subscriber CUSTOM subscriber should exist */
		return null;
		
	}
	
	private void createNotifications (Activity activity) {
		
		/* this method build the full list of subscribers and add a notification for each of them */
		/* a separate set of user ids is used to make sure only one subscriber entity per user is added 
		 * This logic means that the first subscriber that is found and that is not whose type is not 'inherited'
		 * will be the applicable subscriber */
		
		Map<UUID, Subscriber> subscribersMap = new HashMap<UUID, Subscriber>();
		
		Boolean isInModel = isInModel(activity);
		
		/* if the activity occurs in the model, search for subscribers based on sections */
		if (isInModel) {
			
			for (UUID sectionId : directlyAffectedSectionsIds(activity)) {
				/* append the subscribers of this section and all its parents */
				appendSectionSubscribers(sectionId, subscribersMap);
			}
		}
		
		/* then search for subscribers based on initiatives */
		appendInitiativeSubscribers(activity.getInitiative().getId(), subscribersMap);
		
		/* now check if there are subscribers with INHERIT config, if so, use the personal
		 * config of each user at CollectiveOne global level */
		for (Map.Entry<UUID, Subscriber> entry : subscribersMap.entrySet()) {
			Subscriber thisSubscriber = entry.getValue();
			if (thisSubscriber.getInheritConfig() == SubscriberInheritConfig.INHERIT) {
				Subscriber globalSubscriber = subscriberRepository.findByUser_C1IdAndType(thisSubscriber.getUser().getC1Id(), SubscriptionElementType.COLLECTIVEONE); 
				subscribersMap.put(entry.getKey(), globalSubscriber);
			}
		}
		
		/* now prepare a notification for each subscriber */
		for (Map.Entry<UUID, Subscriber> entry : subscribersMap.entrySet()) {
			createSubscriberNotification(entry.getValue(), activity);
		}
		
	}
	
	private void createSubscriberNotification(Subscriber subscriber, Activity activity) {
		/* only if subscriber has enabled notifications and its different from the trigger user */
		if ((subscriber.getInAppConfig() != SubscriberInAppConfig.DISABLED) && (
				!activity.getTriggerUser().getC1Id().equals(subscriber.getUser().getC1Id()))) {
			
			AppUser user = subscriber.getUser();
			
			Notification notification = new Notification();
			notification.setCreationDate(new Timestamp(System.currentTimeMillis()));
			notification.setActivity(activity);
			notification.setSubscriber(subscriber);
			
			Boolean isOnline = subscriber.getUser().getOnlineStatus() == UserOnlineStatus.ONLINE;
			if (isOnline) {
				/* if the user is online, notification is marker as delivered */
				notification.setInAppState(NotificationState.DELIVERED);
				notification.setPushState(NotificationState.DELIVERED);
				notification.setEmailNowState(NotificationState.DELIVERED);
				notification.setEmailSummaryState(NotificationState.DELIVERED);
				
			} else {
				notification.setInAppState(NotificationState.PENDING);
				notification.setPushState(NotificationState.PENDING);
				notification.setEmailNowState(NotificationState.PENDING);
				notification.setEmailSummaryState(NotificationState.PENDING);
				
				/* mark as delivered in some scenarios */
				Boolean isMentioned = activity.getMentions().contains(user);
				Boolean isMessage = activity.getType() == ActivityType.MESSAGE_POSTED;
				
				switch (subscriber.getInAppConfig()) {
				
					case DISABLED:
						/* this should never be the case  due to conditional some above */
						notification.setInAppState(NotificationState.DELIVERED);
						break;
					
					case ONLY_MENTIONS:
						if (!isMentioned) {
							notification.setInAppState(NotificationState.DELIVERED);
						}
						break;
						
					case ONLY_MESSAGES:
						if (!isMessage) {
							notification.setInAppState(NotificationState.DELIVERED);
						}
						break;	
						
					case ALL_EVENTS:
						break;
				}
				
				switch (subscriber.getPushConfig()) {
					case DISABLED:
						/* this should never be the case  due to conditional some above */
						notification.setPushState(NotificationState.DELIVERED);
						break;
					
					case ONLY_MENTIONS:
						if (!isMentioned) {
							notification.setPushState(NotificationState.DELIVERED);
						}
						break;
						
					case ONLY_MESSAGES:
						if (!isMessage) {
							notification.setPushState(NotificationState.DELIVERED);
						}
						break;	
						
					case ALL_EVENTS:
						break;
				}
				
				switch (subscriber.getEmailNowConfig()) {
					case DISABLED:
						/* this should never be the case  due to conditional some above */
						notification.setEmailNowState(NotificationState.DELIVERED);
						break;
					
					case ONLY_MENTIONS:
						if (!isMentioned) {
							notification.setEmailNowState(NotificationState.DELIVERED);
						}
						break;
						
					case ONLY_MESSAGES:
						if (!isMessage) {
							notification.setEmailNowState(NotificationState.DELIVERED);
						}
						break;	
						
					case ALL_EVENTS:
						break;
				}
				
				switch (subscriber.getEmailSummaryConfig()) {
					case DISABLED:
						/* this should never be the case  due to conditional some above */
						notification.setEmailSummaryState(NotificationState.DELIVERED);
						break;
					
					case ONLY_MENTIONS:
						if (!isMentioned) {
							notification.setEmailSummaryState(NotificationState.DELIVERED);
						}
						break;
						
					case ONLY_MESSAGES:
						if (!isMessage) {
							notification.setEmailSummaryState(NotificationState.DELIVERED);
						}
						break;	
						
					case ALL_EVENTS:
						break;
				}
			}
			
			notification = notificationRepository.save(notification);
			
			activity.getNotifications().add(notification);
			
		}
	}
	
	/* update the input subscribers list */
	private void appendSectionSubscribers(UUID sectionId, Map<UUID, Subscriber> subscribersMap) {
		List<Subscriber> thisSubscribers = subscriberRepository.findByElementId(sectionId);
		/* being a set, no duplicates are created */
		
		for (Subscriber subscriber : thisSubscribers) {
			
			if (!subscribersMap.containsKey(subscriber.getUser().getC1Id())) {
				/* if the user has not been added, then just add him */
				subscribersMap.put(subscriber.getUser().getC1Id(), subscriber);
				
			} else {
				/* else, if this subscriber is CUSTOM, get the current subscriber and place him if INHERIT */
				if (subscriber.getInheritConfig() == SubscriberInheritConfig.CUSTOM) {
					Subscriber existingSubscriber = subscribersMap.get(subscriber.getUser().getC1Id());
					if (existingSubscriber.getInheritConfig() == SubscriberInheritConfig.INHERIT) {
						subscribersMap.put(subscriber.getUser().getC1Id(), subscriber);
					}
				}
			}
		}
		
		/* get section immediate parents */
		GraphNode sectionNode = modelService.getSectionNode(sectionId, true, false, 2);
		
		for (GraphNode parent : sectionNode.getParents()) {
			/* recursively add parent subscribers*/
			appendSectionSubscribers(parent.getElementId(), subscribersMap);
		}
	}
	
	@Transactional
	private void appendInitiativeSubscribers (UUID initiativeId, Map<UUID, Subscriber> subscribersMap) {
		
		/* example https://docs.google.com/drawings/d/1PqPhefzrGVlWVfG-SRGS56l_e2qpNEsajLbnsAWcTfA/edit,
		 * assume initiativeId = C */
		/* start with this initiative subscribers (S3 and S6 in example). Take into account that 
		 * a subscriber state may be SUBSCRIPTION_DISABLED */
		List<Subscriber> thisSubscribers = subscriberRepository.findByElementId(initiativeId);
		
		for (Subscriber subscriber : thisSubscribers) {
			if (!subscribersMap.containsKey(subscriber.getUser().getC1Id())) {
				subscribersMap.put(subscriber.getUser().getC1Id(), subscriber);
			} 
		}
		
		/* then add the subscribers of all parent initiatives 2(B and A, in that order) */
		List<Initiative> parents = initiativeService.getParentGenealogyInitiatives(initiativeId);
		for (Initiative parent : parents) {
			List<Subscriber> parentSubscribers = subscriberRepository.findByElementId(parent.getId());
			
			for (Subscriber parentSubscriber : parentSubscribers) {
				subscribersMap.put(parentSubscriber.getUser().getC1Id(), parentSubscriber);
			}
		}
	}
	
	public void broadcastMessage (Activity activity) {
		
		/* message is broadcasted to all parent sections (if is in Model) and all parent initiatives */
		
		Boolean isInModel = isInModel(activity);
		
		if (isInModel) {
			
			/* sections in which the activity took place. Activity on cards can be in many sections at the same time */
			List<UUID> sectionIds = directlyAffectedSectionsIds(activity);
			
			/* parent sections of these affected sections */
			/* using a Set forces each UUID to be unique */
			Set<UUID> allIncumbentSectionsIds = new HashSet<UUID>();
			
			/* add directly affected sections */
			allIncumbentSectionsIds.addAll(sectionIds);
			
			/* add all parents of all sections */
			for (UUID sectionId : sectionIds) {
				allIncumbentSectionsIds.addAll(modelService.getSectionNode(sectionId, true, false, null).toList());
			}
			
			/* send messages to all of them */
			for (UUID sectionId : allIncumbentSectionsIds) {
	    		template.convertAndSend("/channel/activity/model/section/" + sectionId, "UPDATE");
			}
			
			/* if activity on a card wrapper, also broadcast its own channel */
			if (activity.getModelCardWrapper() != null) {
				template.convertAndSend("/channel/activity/model/card/" + activity.getModelCardWrapper().getId(), "UPDATE");
			}
		}
		
	}
}
