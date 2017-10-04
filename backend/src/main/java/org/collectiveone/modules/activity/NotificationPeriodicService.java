package org.collectiveone.modules.activity;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class NotificationPeriodicService {
	
	@Autowired
	private ActivityService activityService;
		
	@Scheduled(fixedDelay = 30000)
	public void sendEmailsSendNow() throws IOException {
		activityService.sendEmailsSendNow();
	}
	
	@Scheduled(fixedDelay = 60000)
//	@Scheduled(fixedDelay = 86400000)
	public void sendEmailsEveryDay() throws IOException {
		activityService.sendEmailsOnceADay();
	}
}
