package org.collectiveone.service;

import java.util.UUID;

import org.collectiveone.AbstractTest;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.modules.activity.ActivityService;
import org.collectiveone.modules.initiatives.InitiativeService;
import org.collectiveone.modules.initiatives.dto.InitiativeTagDto;
import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserDto;
import org.collectiveone.modules.users.AppUserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;


@Transactional
public class TestAppUserService extends AbstractTest {
	
	@Autowired
    private AppUserService userService;
	
	@Value("${TEST_USER}")
	String userId;
	
	
    @Before
    public void setUp() {
        //service.evictCache();
    }

    @After
    public void tearDown() {
        // clean up after each test method
    }
    
    
    @Test
    public void getFromC1IdTestIsSuccess() {
    		AppUser user=this.userService.getFromC1Id(UUID.fromString(this.userId));
    		assertTrue(user.getC1Id().toString().equals(this.userId));
    
    }
    
    @Test
    public void getUserLightIsSuccess() {
    		GetResult<AppUserDto> result=this.userService.getUserLight(UUID.fromString(this.userId));
    		assertTrue(result.getResult().equals(this.STR_SUCCESS));
    }
    
    @Test
    public void getUserFullIsSuccess() {
    		GetResult<AppUserDto> result=this.userService.getUserFull(UUID.fromString(this.userId));
    		assertTrue(result.getResult().equals(this.STR_SUCCESS));
    }
}
