package org.collectiveone.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.collectiveone.AbstractTest;
import org.collectiveone.modules.contexts.dto.NewContextDto;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.net.AuthRequest;
import com.google.gson.Gson;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestContextController extends AbstractTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@Value("${AUTH0_ISSUER}")
	private String auth0Domain;
	
	@Value("${AUTH0_AUDIENCE}")
	private String clientId;
	
	@Value("${AUTH0_SECRET}")
	private String clientSecret;
	
	@Value("${TEST_USER_EMAIL}")
	private String testEmail;
	
	@Value("${TEST_USER_PWD}")
	private String testPwd;
	
	private String authorizationToken;
    
	@Before
    public void setUp() throws Exception {
		
		AuthAPI auth = new AuthAPI(auth0Domain, clientId, clientSecret);
		
		AuthRequest request = auth.login(testEmail, testPwd)
		    .setScope("openid contacts");
		try {
		    TokenHolder holder = request.execute();
		    authorizationToken = holder.getIdToken();
		} catch (APIException exception) {
			System.out.println(exception);
		} catch (Auth0Exception exception) {
			System.out.println(exception);
		}
		
		MvcResult result = this.mockMvc
	    	.perform(get("/1/user/myProfile")
	        .header("Authorization", "Bearer " + authorizationToken))	    	
	    	.andReturn();
		
		String content = result.getResponse().getContentAsString();
		
		System.out.println(content);
    }

    @After
    public void tearDown() {
        // clean up after each test method
    }
    
    @Test
    public void ifCreateInitiativeIsSuccess() {
    	// UUID c1Id=this.userService.ge
    }

    @Test
    public void createContext() throws Exception {
    	
    	NewContextDto contextDto = new NewContextDto("myTitle", "myDescription");
    	
    	Gson gson = new Gson();
        String json = gson.toJson(contextDto);
    	
    	this.mockMvc
	    	.perform(post("/1/ctx")
	    	.header("Authorization", "Bearer " + authorizationToken)
	    	.contentType(MediaType.APPLICATION_JSON)
	    	.content(json))
	    	.andDo(MockMvcResultHandlers.print());
    	
    }
    
}
