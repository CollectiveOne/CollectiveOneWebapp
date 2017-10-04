package org.collectiveone.modules.activity;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.collectiveone.modules.activity.enums.NotificationTrackingType;
import org.collectiveone.modules.activity.repositories.NotificationEmailTrackingRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class NotificationPeriodicService {
	
	@Autowired
	private NotificationEmailTrackingRepositoryIf notificationEmailTrackingRepository; 
	
	@Autowired
	private ActivityService activityService;
	
	@Scheduled(fixedDelay = 30000)
	public void sendEmailsSendNow() throws IOException {
		activityService.sendEmailsSendNow();
	}
	
	@Scheduled(fixedDelay = 30000)
	public void sendEmailsEveryDay() throws IOException {
		
		NotificationEmailTracking emailTracking = notificationEmailTrackingRepository.findByType(NotificationTrackingType.NEXT_ONCEADAY);
		
		if (emailTracking == null) {
			emailTracking = new NotificationEmailTracking();
			emailTracking.setType(NotificationTrackingType.NEXT_ONCEADAY);
			emailTracking.setTimestamp(tomorrow());
			emailTracking = notificationEmailTrackingRepository.save(emailTracking);
		}
		
		if (System.currentTimeMillis() > emailTracking.getTimestamp().getTime()) {

			emailTracking.setTimestamp(tomorrow());
			emailTracking = notificationEmailTrackingRepository.save(emailTracking);
			
			activityService.sendEmailsOnceADay();
		}
	}
	
	@Scheduled(fixedDelay = 30000)
	public void sendEmailsEveryWeek() throws IOException {
		NotificationEmailTracking emailTracking = notificationEmailTrackingRepository.findByType(NotificationTrackingType.NEXT_ONCEAWEEK);
		
		if (emailTracking == null) {
			emailTracking = new NotificationEmailTracking();
			emailTracking.setType(NotificationTrackingType.NEXT_ONCEAWEEK);
			emailTracking.setTimestamp(nextWeek());
			emailTracking = notificationEmailTrackingRepository.save(emailTracking);
		}
		
		if (System.currentTimeMillis() > emailTracking.getTimestamp().getTime()) {
			
			emailTracking.setTimestamp(nextWeek());
			emailTracking = notificationEmailTrackingRepository.save(emailTracking);
			
			activityService.sendEmailsOnceAWeek();
		}
	}
	
	private Timestamp tomorrow() {
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.add(Calendar.DAY_OF_MONTH, 1);
//		c.add(Calendar.MINUTE, 1);
		return new Timestamp(c.getTimeInMillis());
	}
	
	private Timestamp nextWeek() {
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.add(Calendar.DAY_OF_MONTH, 7);
		return new Timestamp(c.getTimeInMillis());
	}
	
}
