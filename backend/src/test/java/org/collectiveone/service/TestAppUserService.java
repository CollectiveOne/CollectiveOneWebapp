package org.collectiveone.service;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.collectiveone.AbstractTest;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserDto;
import org.collectiveone.modules.users.AppUserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;


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
    
    @Test
    public void updateUserDataInLocalDBisSuccess() {
    		PostResult result=this.userService.disableEmailNotifications(UUID.fromString(this.userId)); 
    		assertTrue(result.getResult().equals("success"));	
    }
    
    
    @Test
    public void searchBySReturnsData() {
    		GetResult<List<AppUserDto>> result=this.userService.searchBy("s");
    		assertTrue(result.getData().size()>0);	
    }
    

    @Test
    public void searchBySisSuccess() {
    		GetResult<List<AppUserDto>> result=this.userService.searchBy("s");
    		assertTrue(result.getResult().equals("success"));	
    }
    
    @Test
    public void usernameExistReturnsFalseInUniqueUserName() {
    		GetResult<Boolean> result=this.userService.usernameExist("sadadad");
    		assertTrue(result.getData()==false);	
    }
}
