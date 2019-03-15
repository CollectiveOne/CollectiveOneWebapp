package org.collectiveone.modules.activity;

import java.io.IOException;
import java.util.List;

import org.collectiveone.modules.activity.enums.NotificationState;
import org.collectiveone.modules.activity.enums.NotificationTrackingType;
import org.collectiveone.modules.activity.repositories.NotificationEmailTrackingRepositoryIf;
import org.collectiveone.modules.activity.repositories.WantToContributeRepositoryIf;
import org.collectiveone.modules.users.AppUserRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class NotificationPeriodicService {
	
	@Autowired
	private NotificationEmailTrackingRepositoryIf notificationEmailTrackingRepository; 
	
	@Autowired
	private WantToContributeRepositoryIf wantToContributeRepository;
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private TimeService timeService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private AppUserRepositoryIf appUserRepository;
	
	/* check every 1 min */
	@Scheduled(fixedDelay = 60000)
	public void checkWantToContributeNow() throws IOException {
		List<WantToContributeNotification> notifications = 
				wantToContributeRepository.findByEmailState(NotificationState.PENDING);
		
		emailService.sendWantToContributeNotifications(notifications);
	}
	
	/* check every 1 min */
	@Scheduled(fixedDelay = 60000)
	public void checkSendEmailsNow() throws IOException {
		
		List<NotificationEmailTracking> emailTrackings = 
				notificationEmailTrackingRepository.findByType(NotificationTrackingType.NEXT_NOW);
		
		NotificationEmailTracking emailTracking = null;
		
		/* delete repeated entries */
		if (emailTrackings.size() > 1) {
			notificationEmailTrackingRepository.deleteAll(emailTrackings.subList(1, emailTrackings.size() - 1));
			emailTracking = emailTrackings.get(0);
		}
		
		/* create one entry */
		if (emailTrackings.size() == 0) {
			emailTracking = new NotificationEmailTracking();
			emailTracking.setType(NotificationTrackingType.NEXT_NOW);
			emailTracking.setTimestamp(timeService.inTwoMinutes());
			emailTracking = notificationEmailTrackingRepository.save(emailTracking);
		} else {
			emailTracking = emailTrackings.get(0);
		}
		
		if (System.currentTimeMillis() > emailTracking.getTimestamp().getTime()) {

			emailTracking.setTimestamp(timeService.inTwoMinutes());
			emailTracking = notificationEmailTrackingRepository.save(emailTracking);
			
			activityService.sendNotificationEmailsSendNow();
		}
	}
	
	/* check every 5 min */
	@Scheduled(fixedDelay = 300000)
	public void checkSendEmailsEveryDay() throws IOException {
		
		List<NotificationEmailTracking> emailTrackings = 
				notificationEmailTrackingRepository.findByType(NotificationTrackingType.NEXT_ONCEADAY);
		
		NotificationEmailTracking emailTracking = null;
		
		/* delete repeated entries */
		if (emailTrackings.size() > 1) {
			notificationEmailTrackingRepository.deleteAll(emailTrackings.subList(1, emailTrackings.size() - 1));
			emailTracking = emailTrackings.get(0);
		}
		
		/* create one entry */
		if (emailTrackings.size() == 0) {
			emailTracking = new NotificationEmailTracking();
			emailTracking.setType(NotificationTrackingType.NEXT_ONCEADAY);
			emailTracking.setTimestamp(timeService.tomorrow());
			emailTracking = notificationEmailTrackingRepository.save(emailTracking);
		} else {
			emailTracking = emailTrackings.get(0);
		}
		
		if (System.currentTimeMillis() > emailTracking.getTimestamp().getTime()) {

			emailTracking.setTimestamp(timeService.tomorrow());
			emailTracking = notificationEmailTrackingRepository.save(emailTracking);
			
			activityService.sendNotificationEmailsOnceADay();
		}
	}
	
	/* check every 30 min */
	@Scheduled(fixedDelay = 1800000)
	public void checkSendEmailsEveryWeek() throws IOException {
		List<NotificationEmailTracking> emailTrackings = 
				notificationEmailTrackingRepository.findByType(NotificationTrackingType.NEXT_ONCEAWEEK);
		
		NotificationEmailTracking emailTracking = null;
		
		/* delete repeated entries */
		if (emailTrackings.size() > 1) {
			notificationEmailTrackingRepository.deleteAll(emailTrackings.subList(1, emailTrackings.size() - 1));
			emailTracking = emailTrackings.get(0);
		}
		
		if (emailTracking == null) {
			emailTracking = new NotificationEmailTracking();
			emailTracking.setType(NotificationTrackingType.NEXT_ONCEAWEEK);
			emailTracking.setTimestamp(timeService.nextWeek());
			emailTracking = notificationEmailTrackingRepository.save(emailTracking);
		} else {
			emailTracking = emailTrackings.get(0);
		}
		
		if (System.currentTimeMillis() > emailTracking.getTimestamp().getTime()) {
			
			emailTracking.setTimestamp(timeService.nextWeek());
			emailTracking = notificationEmailTrackingRepository.save(emailTracking);
			
			activityService.sendNotificationEmailsOnceAWeek();
		}
	}
	
	/* check every 24H */
	@Scheduled(fixedDelay = 86400000)
	public void deleteOldNotifications() throws IOException {
		activityService.deleteOldNotifications();
	}
	
	
	
}
