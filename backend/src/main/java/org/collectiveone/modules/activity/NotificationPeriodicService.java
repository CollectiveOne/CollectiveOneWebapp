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
	public void updateState() throws IOException {
		activityService.sendEmailsSendNow();
	}
}
