package org.collectiveone.service;

import java.util.UUID;

import org.apache.tomcat.jni.Time;
import org.collectiveone.AbstractTest;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.activity.ActivityService;
import org.collectiveone.modules.activity.dto.NotificationDto;
import org.collectiveone.modules.activity.dto.SubscriberDto;
import org.collectiveone.modules.activity.enums.SubscriberInAppConfig;

import org.collectiveone.modules.users.AppUserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertTrue;

import java.util.List;


@Transactional
public class TestActivityService extends AbstractTest {
	@Autowired
    private ActivityService activityService;
	
	@Autowired
    private AppUserService userService;
	
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
    
    @Test
    public void ifGetUserNotificationsWorks() {
    		GetResult<List<NotificationDto>> notifications = activityService.getUserNotifications(UUID.fromString(this.userId), new PageRequest(1, 10));
    		assertTrue(notifications.getResult().equals("success"));
    }
    
    @Test
    public void ifGetSubscriberWorks() {
    		GetResult<SubscriberDto> notification = activityService.getSubscriber(UUID.fromString(this.userId), UUID.fromString(this.initiativeId));
    		assertTrue(notification.getResult().equals("success"));
    }
    
    @Test
    public void ifEditSubscriberStateWorks() {
    		PostResult result = activityService.editSubscriberState(UUID.fromString(this.userId), UUID.fromString(initiativeId), SubscriberInAppConfig.UNSUBSCRIBED);
    		assertTrue(result.getResult().equals("success"));
    }
      
    
}
