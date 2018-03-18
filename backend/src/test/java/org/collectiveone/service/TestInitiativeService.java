package org.collectiveone.service;

import java.util.UUID;

import org.apache.tomcat.jni.Time;
import org.collectiveone.AbstractTest;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.activity.ActivityService;
import org.collectiveone.modules.initiatives.InitiativeService;
import org.collectiveone.modules.initiatives.InitiativeTag;
import org.collectiveone.modules.initiatives.Member;
import org.collectiveone.modules.initiatives.dto.InitiativeTagDto;
import org.collectiveone.modules.initiatives.dto.MemberDto;
import org.collectiveone.modules.initiatives.dto.NewInitiativeDto;
import org.collectiveone.modules.users.AppUserDto;
import org.collectiveone.modules.users.AppUserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.applicationdiscovery.model.Tag;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;


@Transactional
public class TestInitiativeService extends AbstractTest {
	@Autowired
    private InitiativeService initiativeService;
	
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
    public void ifCreateInitiativeIsSuccess() {
    		//UUID c1Id=this.userService.ge
    }

    @Test
    public void ifGetTagIsSuccess() {
		InitiativeService initiativeServiceMock=mock(InitiativeService.class);
		InitiativeTagDto tag=new InitiativeTagDto();
		tag.setId(UUID.randomUUID().toString());
		when(initiativeServiceMock.getTag(UUID.fromString("c0a80067-61b9-1590-8161-b91c6d570010"))).thenReturn(new GetResult<InitiativeTagDto>("success", "initiative tag returned", tag));		
		GetResult<InitiativeTagDto> result=initiativeServiceMock.getTag(UUID.fromString("c0a80067-61b9-1590-8161-b91c6d570010"));
		assertTrue(result.getResult().equals("success"));

    }
    
    @Test
    public void createInitiativeIfNoMembersWorks() {
		NewInitiativeDto initiative = new NewInitiativeDto();
		initiative.setName("test");
		
		//create data in db
		PostResult result = this.initiativeService.init(UUID.fromString(this.userId), initiative);
		//get id and try to convert id to UUID if valid or not
		try {
			UUID initiativeId=UUID.fromString(result.getElementId());	
    		assertTrue(initiativeId.toString().length()>0);
		}
		catch(Exception ex)
		{
			assertTrue(false);
		}
    		
    		
    }
    
    
    @Test
    public void isSearchTagsByWorks() {
		GetResult<List<InitiativeTagDto>> result=this.initiativeService.searchTagsBy("s");
		assertTrue(result.getResult().equals("success"));
    }
    
    @Test
    public void isGetOrAddMemberWorks() {
		Member member = this.initiativeService.getOrAddMember(UUID.fromString(this.initiativeId),UUID.fromString(this.userId));
		assertTrue(member != null);
    }
    
   
    
    
}
