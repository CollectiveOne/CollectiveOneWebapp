package org.collectiveone.service;

import org.collectiveone.AbstractTest;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public class TestActivityService extends AbstractTest {
	
	@Value("${TEST_USER}")
	String userId;
	
	@Value("${TEST_INITIATIVE_ID}")
	String initiativeId;
	
    @Before
    public void setUp() {
        //service.evictCache();
    }

    @After
    public void tearDown() {
        // clean up after each test method
    }
    
//    @Test
//    public void ifGetUserNotificationsWorks() {
//    		GetResult<List<NotificationDto>> notifications = activityService.getUserNotifications(UUID.fromString(this.userId), new PageRequest(1, 10));
//    		assertTrue(notifications.getResult().equals("success"));
//    }
//    
//    @Test
//    public void ifGetSubscriberWorks() {
//    		GetResult<SubscriberDto> notification = activityService.getSubscriber(UUID.fromString(this.userId), UUID.fromString(this.initiativeId));
//    		assertTrue(notification.getResult().equals("success"));
//    }
    
//    @Test
//    public void ifEditSubscriberStateWorks() {
//    		PostResult result = activityService.editSubscriberState(UUID.fromString(this.userId), UUID.fromString(initiativeId), SubscriberInAppConfig.UNSUBSCRIBED);
//    		assertTrue(result.getResult().equals("success"));
//    }
      
    
}
