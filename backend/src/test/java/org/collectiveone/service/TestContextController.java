package org.collectiveone.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.lang.reflect.Type;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.collectiveone.AbstractTest;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.contexts.dto.ContextMetadataDto;
import org.collectiveone.modules.contexts.dto.NewCardDto;
import org.collectiveone.modules.contexts.dto.PerspectiveDto;
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
    public void createContext() throws Exception {
    	
    	String title = "My Title";
    	String description = "My Description";
    	
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
		Type resultType = new TypeToken<GetResult<PerspectiveDto>>(){}.getType();
        
        GetResult<PerspectiveDto> getResult = 
        		gson.fromJson(result.getResponse().getContentAsString(), resultType);
        
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
			gson.fromJson(result.getResponse().getContentAsString(), resultType);
        
        PerspectiveDto perspectiveDto2 = getResult2.getData();
        
        assertEquals("unexpected title",
        		"root context", perspectiveDto2.getMetadata().getTitle());
        
        assertEquals("unexpected number of subcontexts",
        		1, perspectiveDto2.getSubcontexts().size());
        
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
			gson.fromJson(result.getResponse().getContentAsString(), resultType);
        
        PerspectiveDto perspectiveDto3 = getResult3.getData();
        
        assertEquals("unexpected title",
        		"root context", perspectiveDto3.getMetadata().getTitle());
        
        assertEquals("unexpected number of subcontexts",
        		0, perspectiveDto3.getSubcontexts().size());
        
        /** Get actions on working commit */
        result = this.mockMvc
    	    	.perform(get("/1/persp/" + perspectiveDto3.getId() + "/actions")
    	    	.param("levels", "0")
    	    	.header("Authorization", "Bearer " + authorizationTokenUser2))
    	    	.andReturn();
        
        
        
        
    }
    
    
    @Test
    public void commitWorkingCommit() throws Exception {
    	
    }

    
}
