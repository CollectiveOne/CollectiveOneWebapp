package org.collectiveone.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.collectiveone.AbstractTest;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.contexts.dto.CommitDto;
import org.collectiveone.modules.contexts.dto.ContextMetadataDto;
import org.collectiveone.modules.contexts.dto.PerspectiveDto;
import org.collectiveone.modules.contexts.dto.StagedElementDto;
import org.collectiveone.modules.contexts.entities.enums.CommitStatus;
import org.collectiveone.modules.contexts.entities.enums.StageAction;
import org.collectiveone.modules.contexts.entities.enums.StageStatus;
import org.collectiveone.modules.contexts.entities.enums.StageType;
import org.collectiveone.modules.users.AppUserDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.net.AuthRequest;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestContextController extends AbstractTest {
	
	private static final Logger logger = LogManager.getLogger(TestContextController.class);
	
	@Autowired
    private MockMvc mockMvc;
	
	private Gson gson = new Gson();
	
	@Value("${AUTH0_ISSUER}")
	private String auth0Domain;
	
	@Value("${AUTH0_AUDIENCE}")
	private String clientId;
	
	@Value("${AUTH0_SECRET}")
	private String clientSecret;
	
	@Value("${TEST_USER_EMAIL_1}")
	private String testEmail1;
	
	@Value("${TEST_USER_PWD_1}")
	private String testPwd1;
	
	private String authorizationTokenUser1;
	
	@Value("${TEST_USER_EMAIL_2}")
	private String testEmail2;
	
	@Value("${TEST_USER_PWD_2}")
	private String testPwd2;
	
	private String authorizationTokenUser2;
	
	private AppUserDto user1;
	
	private AppUserDto user2;
	
	final String title = "My Title";
	final String description = "My Description";
	static private String subperspectiveId;
    
	@Before
    public void setUp() throws Exception {
		
		AuthAPI auth = new AuthAPI(auth0Domain, clientId, clientSecret);
		
		AuthRequest request = auth.login(testEmail1, testPwd1)
		    .setScope("openid contacts");
		try {
		    TokenHolder holder = request.execute();
		    authorizationTokenUser1 = holder.getIdToken();
		} catch (APIException exception) {
			System.out.println(exception);
		} catch (Auth0Exception exception) {
			System.out.println(exception);
		}
		
		authorizationTokenUser1 = authorizationTokenUser1.substring(0, authorizationTokenUser1.length() - 5) + "AAAAA";
		
		MvcResult result = this.mockMvc
	    	.perform(get("/1/user/myProfile")
	        .header("Authorization", "Bearer " + authorizationTokenUser1))	    	
	    	.andReturn();
		
		assertEquals("error in http request: " + result.getResponse().getErrorMessage(),
        		200, result.getResponse().getStatus());
		
		
		@SuppressWarnings("serial")
		Type resultType = new TypeToken<GetResult<AppUserDto>>(){}.getType();
        
        GetResult<AppUserDto> getResult = 
        		gson.fromJson(result.getResponse().getContentAsString(), resultType);
        
        user1 = getResult.getData();
		
        logger.debug("Test user created:" + result.getResponse().getContentAsString());
        
        request = auth.login(testEmail2, testPwd2)
    		    .setScope("openid contacts");
		try {
		    TokenHolder holder = request.execute();
		    authorizationTokenUser2 = holder.getIdToken();
		} catch (APIException exception) {
			System.out.println(exception);
		} catch (Auth0Exception exception) {
			System.out.println(exception);
		}
		
		result = this.mockMvc
	    	.perform(get("/1/user/myProfile")
	        .header("Authorization", "Bearer " + authorizationTokenUser2))	    	
	    	.andReturn();
		
		assertEquals("error in http request: " + result.getResponse().getErrorMessage(),
        		200, result.getResponse().getStatus());
		
		
		getResult = 
        		gson.fromJson(result.getResponse().getContentAsString(), resultType);
        
        user2 = getResult.getData();
		
        logger.debug("Test user created:" + result.getResponse().getContentAsString());

    }

    @After
    public void tearDown() {
        // clean up after each test method
    }
    
    @Test
    @Rollback(false)
    public void createContext() throws Exception {
    	
    	ContextMetadataDto contextDto = new ContextMetadataDto(title, description);
    	
    	String json = gson.toJson(contextDto);
        MvcResult result = null;
        
        /* add new context to user 1 working commit */
        result = this.mockMvc
	    	.perform(post("/1/ctx")
	    	.header("Authorization", "Bearer " + authorizationTokenUser1)
	    	.contentType(MediaType.APPLICATION_JSON)
	    	.content(json))
	    	.andReturn();
        
        assertEquals("error in http request: " + result.getResponse().getErrorMessage(),
        		200, result.getResponse().getStatus());
        
        PostResult postResult = gson.fromJson(result.getResponse().getContentAsString(), PostResult.class); 
        
        assertEquals("error creating context: " + postResult.getMessage(),
        		"success", postResult.getResult());
        
        /* store id context of new context */
        String contextId = postResult.getElementId();
        
        assertNotNull("unexpected id",  UUID.fromString(contextId));
        
        /* get the new context and check metadata is correct*/
        result = this.mockMvc
    	    	.perform(get("/1/ctx/" + contextId)
    	    	.header("Authorization", "Bearer " + authorizationTokenUser1))
    	    	.andReturn();
        
        assertEquals("error in http request: " + result.getResponse().getErrorMessage(),
        		200, result.getResponse().getStatus());
        
        @SuppressWarnings("serial")
		Type perspectiveDtoresultType = new TypeToken<GetResult<PerspectiveDto>>(){}.getType();
        
        GetResult<PerspectiveDto> getResult = 
        		gson.fromJson(result.getResponse().getContentAsString(), perspectiveDtoresultType);
        
        assertEquals("error getting context: " + getResult.getMessage(),
        		"success", getResult.getResult());
        
        PerspectiveDto perspectiveDto = getResult.getData();
        
        assertEquals("unexpected title",
        		title, perspectiveDto.getMetadata().getTitle());
        
        assertEquals("unexpected description",
       		 	description, perspectiveDto.getMetadata().getDescription());
        
        /* get parent context with children and check metadata is correct */
        result = this.mockMvc
    	    	.perform(get("/1/ctx/" + user1.getRootContextId())
    	    	.param("levels", "1")
    	    	.header("Authorization", "Bearer " + authorizationTokenUser1))
    	    	.andReturn();
        
        assertEquals("error in http request: " + result.getResponse().getErrorMessage(),
        		200, result.getResponse().getStatus());
        
        @SuppressWarnings("serial")
		GetResult<PerspectiveDto> getResult2 = 
			gson.fromJson(result.getResponse().getContentAsString(), perspectiveDtoresultType);
        
        PerspectiveDto perspectiveDto2 = getResult2.getData();
        
        assertEquals("unexpected title",
        		"root context", perspectiveDto2.getMetadata().getTitle());
        
        assertEquals("unexpected number of subcontexts",
        		1, perspectiveDto2.getSubcontexts().size());
        
        assertEquals("unexpected commit status",
        		CommitStatus.PENDING, perspectiveDto2.getSubcontexts().get(0).getCommitStatus());
        
        assertEquals("unexpected title",
        		title, perspectiveDto2.getSubcontexts().get(0).getMetadata().getTitle());
        
        assertEquals("unexpected description",
        		description, perspectiveDto2.getSubcontexts().get(0).getMetadata().getDescription());
        
        /** Get parent context with children and check metadata is correct as user 2 
         * (should not see the subcontext as it is in the working commit of user 1)*/
        result = this.mockMvc
    	    	.perform(get("/1/ctx/" + user1.getRootContextId())
    	    	.param("levels", "1")
    	    	.header("Authorization", "Bearer " + authorizationTokenUser2))
    	    	.andReturn();
        
        assertEquals("error in http request: " + result.getResponse().getErrorMessage(),
        		200, result.getResponse().getStatus());
        
        @SuppressWarnings("serial")
		GetResult<PerspectiveDto> getResult3 = 
			gson.fromJson(result.getResponse().getContentAsString(), perspectiveDtoresultType);
        
        PerspectiveDto perspectiveDto3 = getResult3.getData();
        subperspectiveId = perspectiveDto3.getId();        
        
        assertEquals("unexpected title",
        		"root context", perspectiveDto3.getMetadata().getTitle());
        
        assertEquals("unexpected number of subcontexts",
        		0, perspectiveDto3.getSubcontexts().size());
        
    }
    
    @Test
    @Rollback(false)
    public void stageAndCommit() throws Exception {
    	
    	MvcResult result = null;
    	PostResult postResult = null;
    	String json = null;
    	
    	/** Get list of staged elements on working commit for user 1 */
        result = this.mockMvc
    	    	.perform(get("/1/persp/" + subperspectiveId + "/stagedElements")
    	    	.param("levels", "0")
    	    	.header("Authorization", "Bearer " + authorizationTokenUser1))
    	    	.andReturn();
        
        assertEquals("error in http request: " + result.getResponse().getErrorMessage(),
        		200, result.getResponse().getStatus());
        
        @SuppressWarnings("serial")
		GetResult<List<StagedElementDto>> getResult4 = 
			gson.fromJson(result.getResponse().getContentAsString(), 
					new TypeToken<GetResult<List<StagedElementDto>>>(){}.getType());
        
        List<StagedElementDto> stagedElements = getResult4.getData();
        
        assertEquals("unexpected size",
        		1, stagedElements.size());
        
        assertEquals("unexpected action",
        		StageAction.ADD, stagedElements.get(0).getAction());
        
        assertEquals("unexpected action",
        		StageStatus.PENDING, stagedElements.get(0).getStatus());
        
        assertEquals("unexpected type",
        		StageType.SUBCONTEXT, stagedElements.get(0).getType());
        
        
        /** Try to set the status as user 2 */
        result = this.mockMvc
    	    	.perform(put("/1/stagedElement/" + stagedElements.get(0).getId() + "/status")
    	    	.param("newStatus", StageStatus.ADDED.toString())
    	    	.header("Authorization", "Bearer " + authorizationTokenUser2))
    	    	.andReturn();
        
        assertEquals("error in http request: " + result.getResponse().getErrorMessage(),
        		200, result.getResponse().getStatus());
        
        postResult = gson.fromJson(result.getResponse().getContentAsString(), PostResult.class); 
        
        assertEquals("error preventing staged element status change",
        		"error", postResult.getResult());
        
        /** Set the status of a staged element as user 1 */
        result = this.mockMvc
    	    	.perform(put("/1/stagedElement/" + stagedElements.get(0).getId() + "/status")
    	    	.param("newStatus", StageStatus.ADDED.toString())
    	    	.header("Authorization", "Bearer " + authorizationTokenUser1))
    	    	.andReturn();
        
        assertEquals("error in http request: " + result.getResponse().getErrorMessage(),
        		200, result.getResponse().getStatus());
        
        postResult = gson.fromJson(result.getResponse().getContentAsString(), PostResult.class); 
        
        assertEquals("error setting staged element status",
        		"success", postResult.getResult());
        
        /** Get list of staged elements on working commit for user 1 */
        result = this.mockMvc
    	    	.perform(get("/1/persp/" + subperspectiveId + "/stagedElements")
    	    	.param("levels", "0")
    	    	.header("Authorization", "Bearer " + authorizationTokenUser1))
    	    	.andReturn();
        
        assertEquals("error in http request: " + result.getResponse().getErrorMessage(),
        		200, result.getResponse().getStatus());
        
        @SuppressWarnings("serial")
		GetResult<List<StagedElementDto>> getResult5 = 
			gson.fromJson(result.getResponse().getContentAsString(), 
					new TypeToken<GetResult<List<StagedElementDto>>>(){}.getType());
        
        stagedElements = getResult5.getData();
        
        assertEquals("unexpected size",
        		1, stagedElements.size());
        
        assertEquals("unexpected action",
        		StageAction.ADD, stagedElements.get(0).getAction());
        
        assertEquals("unexpected action",
        		StageStatus.ADDED, stagedElements.get(0).getStatus());
        
        assertEquals("unexpected type",
        		StageType.SUBCONTEXT, stagedElements.get(0).getType());
        
        
        /** Now try to commit added stagedChanges as the author 2 */
        String message = "I wanted to add this subcontext";
    	
    	CommitDto commitDto = new CommitDto(message);
    	json = gson.toJson(commitDto);
        
        result = this.mockMvc
    	    	.perform(put("/1/persp/" + subperspectiveId + "/commit")
    	    	.header("Authorization", "Bearer " + authorizationTokenUser1)
    	    	.param("levels", "0")
    	    	.contentType(MediaType.APPLICATION_JSON)
    	    	.content(json))
    	    	.andReturn();
        
        assertEquals("error in http request: " + result.getResponse().getErrorMessage(),
        		200, result.getResponse().getStatus());
        
        postResult = gson.fromJson(result.getResponse().getContentAsString(), PostResult.class); 
        
        String commitId = postResult.getElementId();
        
        /* get user root context and verify that subcontext is still there */
        result = this.mockMvc
    	    	.perform(get("/1/ctx/" + user1.getRootContextId())
    	    	.param("levels", "1")
    	    	.header("Authorization", "Bearer " + authorizationTokenUser1))
    	    	.andReturn();
        
        assertEquals("error in http request: " + result.getResponse().getErrorMessage(),
        		200, result.getResponse().getStatus());
        
        @SuppressWarnings("serial")
		Type perspectiveDtoresultType = new TypeToken<GetResult<PerspectiveDto>>(){}.getType();
        
        @SuppressWarnings("serial")
		GetResult<PerspectiveDto> getResult6 = 
			gson.fromJson(result.getResponse().getContentAsString(), perspectiveDtoresultType);
        
        PerspectiveDto perspectiveDto4 = getResult6.getData();
        
        assertEquals("unexpected title",
        		"root context", perspectiveDto4.getMetadata().getTitle());
        
        assertEquals("unexpected number of subcontexts",
        		1, perspectiveDto4.getSubcontexts().size());
        
        assertEquals("unexpected commit status",
        		CommitStatus.COMMITTED, perspectiveDto4.getSubcontexts().get(0).getCommitStatus());
        
        assertEquals("unexpected title",
        		title, perspectiveDto4.getSubcontexts().get(0).getMetadata().getTitle());
        
        assertEquals("unexpected description",
        		description, perspectiveDto4.getSubcontexts().get(0).getMetadata().getDescription());
        
        /** get staged elements, they should be empty now*/
        result = this.mockMvc
    	    	.perform(get("/1/persp/" + subperspectiveId + "/stagedElements")
    	    	.param("levels", "0")
    	    	.header("Authorization", "Bearer " + authorizationTokenUser1))
    	    	.andReturn();
        
        assertEquals("error in http request: " + result.getResponse().getErrorMessage(),
        		200, result.getResponse().getStatus());
        
        @SuppressWarnings("serial")
		GetResult<List<StagedElementDto>> getResult7 = 
			gson.fromJson(result.getResponse().getContentAsString(), 
					new TypeToken<GetResult<List<StagedElementDto>>>(){}.getType());
        
        stagedElements = getResult7.getData();
        
        assertEquals("unexpected size",
        		0, stagedElements.size());
    }
    
    
    @Test
    public void commitWorkingCommit() throws Exception {
    	
    }

    
}
