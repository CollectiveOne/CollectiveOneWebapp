package org.collectiveone.service;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.collectiveone.AbstractTest;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.initiatives.dto.InitiativeDto;
import org.collectiveone.modules.initiatives.dto.MemberDto;
import org.collectiveone.modules.initiatives.dto.NewInitiativeDto;
import org.collectiveone.modules.model.ModelScope;
import org.collectiveone.modules.model.dto.ModelCardDto;
import org.collectiveone.modules.model.dto.ModelCardWrapperDto;
import org.collectiveone.modules.model.dto.ModelSectionDto;
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
public class TestElementOrder extends AbstractTest {
	
	private static final Logger logger = LogManager.getLogger(TestElementOrder.class);
	
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
	
	static final String initiativeName = "My Initiative";
	
	static private String initiativeId;
	
	private InitiativeDto initiative;
	
	
	@SuppressWarnings("serial")
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
		
		
		GetResult<AppUserDto> getResultUser = 
        		gson.fromJson(result.getResponse().getContentAsString(), 
        				new TypeToken<GetResult<AppUserDto>>(){}.getType());
        
        user1 = getResultUser.getData();
		
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
		
		
		getResultUser = 
        		gson.fromJson(result.getResponse().getContentAsString(), 
        				new TypeToken<GetResult<AppUserDto>>(){}.getType());
        
        user2 = getResultUser.getData();
		
        logger.debug("Test user created: " + result.getResponse().getContentAsString());
        
        /** create initiative */
        
        NewInitiativeDto initiativeDto = new NewInitiativeDto(initiativeName, "");
        MemberDto member = new MemberDto();
        member.setRole("ADMIN");
        member.setUser(user1);
        
        initiativeDto.getMembers().add(member);
        		
        result = this.mockMvc
    	    	.perform(post("/1/initiative/create")
    	        .header("Authorization", "Bearer " + authorizationTokenUser1)	    	
    	    	.contentType(MediaType.APPLICATION_JSON)
    	    	.content(gson.toJson(initiativeDto)))
				.andReturn();
        
        assertEquals("error in http request: " + result.getResponse().getErrorMessage(),
        		200, result.getResponse().getStatus());
        
        PostResult postResult = gson.fromJson(result.getResponse().getContentAsString(), PostResult.class); 
        
        initiativeId = postResult.getElementId();
        
        logger.debug("Initiative created: " + initiativeId);

        result = this.mockMvc
    	    	.perform(get("/1/initiative/" + initiativeId)
    	        .header("Authorization", "Bearer " + authorizationTokenUser1))    	
    	    	.andReturn();
        
        GetResult<InitiativeDto> getResultInit = 
        		gson.fromJson(result.getResponse().getContentAsString(), 
        				new TypeToken<GetResult<InitiativeDto>>(){}.getType());
        
        initiative = getResultInit.getData();
        
        logger.debug("Initiative retrieved: " + result.getResponse().getContentAsString());
    }

    @After
    public void tearDown() {
        // clean up after each test method
    }
    
    /* creates one section and two COMMON cards without specifying the order */
    @SuppressWarnings("serial")
	@Test
    public void createCardsMoveAndReorder() throws Exception {
    	ModelSectionDto sectionDto = new ModelSectionDto("s1", "", ModelScope.COMMON);
    	
    	MvcResult result = this.mockMvc
     	    	.perform(post("/1/model/section/" + initiative.getTopModelSection().getId() + "/subsection")
     	        .header("Authorization", "Bearer " + authorizationTokenUser1)
     	        .contentType(MediaType.APPLICATION_JSON)
     	    	.content(gson.toJson(sectionDto)))
 				.andReturn();
    	
    	assertEquals("error in http request: " + result.getResponse().getErrorMessage(),
        		200, result.getResponse().getStatus());
    	
    	String s1Id = gson.fromJson(result.getResponse().getContentAsString(), PostResult.class).getElementId();

    	logger.debug("Subsection created: " + result.getResponse().getContentAsString());
    	    	
    	/** add cards */
    	ModelCardDto cardDto1 = new ModelCardDto("c1", ModelScope.COMMON); 
    	
    	result = this.mockMvc
     	    	.perform(post("/1/model/section/" + s1Id + "/cardWrapper")
     	        .header("Authorization", "Bearer " + authorizationTokenUser1)
     	        .contentType(MediaType.APPLICATION_JSON)
     	    	.content(gson.toJson(cardDto1)))
 				.andReturn();
    	
    	assertEquals("error in http request: " + result.getResponse().getErrorMessage(),
        		200, result.getResponse().getStatus());
    	
    	logger.debug("Card 1 created: " + result.getResponse().getContentAsString());
    	
    	String c1Id = gson.fromJson(result.getResponse().getContentAsString(), PostResult.class).getElementId();
    	    	
    	ModelCardDto cardDto2 = new ModelCardDto("c2", ModelScope.COMMON);
    	
    	result = this.mockMvc
     	    	.perform(post("/1/model/section/" + s1Id + "/cardWrapper")
     	        .header("Authorization", "Bearer " + authorizationTokenUser1)
     	        .contentType(MediaType.APPLICATION_JSON)
     	    	.content(gson.toJson(cardDto2)))
 				.andReturn();
    	
    	assertEquals("error in http request: " + result.getResponse().getErrorMessage(),
        		200, result.getResponse().getStatus());
    	
    	logger.debug("Card 2 created: " + result.getResponse().getContentAsString());
    	
    	String c2Id = gson.fromJson(result.getResponse().getContentAsString(), PostResult.class).getElementId();
    	    	
    	/** get section content */
    	result = this.mockMvc
     	    	.perform(get("/1/model/section/" + s1Id)
     	        .header("Authorization", "Bearer " + authorizationTokenUser1))
 				.andReturn();
    	
    	GetResult<ModelSectionDto> getResultSection = 
        		gson.fromJson(result.getResponse().getContentAsString(), 
        				new TypeToken<GetResult<ModelSectionDto>>(){}.getType());
    	
    	List<ModelCardWrapperDto> commonCards = getResultSection.getData().getCardsWrappersCommon();
    	
    	assertEquals("wrong number of cards",
        		2, commonCards.size());
    	    	
    	ModelCardWrapperDto c1 = commonCards.stream()
    			.filter(c -> c.getId().equals(c1Id))
    			.findFirst().get(); 
    	
    	ModelCardWrapperDto c2 = commonCards.stream()
    			.filter(c -> c.getId().equals(c2Id))
    			.findFirst().get();
    	
    	assertEquals("wrong after element",
        		null, c1.getAfterElementId());
    	
    	assertEquals("wrong before element",
        		c2Id, c1.getBeforeElementId());
    	
    	assertEquals("wrong after element",
        		c1Id, c2.getAfterElementId());
    	
    	assertEquals("wrong before element",
        		null, c2.getBeforeElementId());
    	
    	/** add card after first card */
    	ModelCardDto cardDto3 = new ModelCardDto("c3", ModelScope.COMMON); 
    	
    	result = this.mockMvc
     	    	.perform(post("/1/model/section/" + s1Id + "/cardWrapper")
     	        .header("Authorization", "Bearer " + authorizationTokenUser1)
     	        .param("onCardWrapperId", c1Id)
     	        .param("isBefore", "false")
     	        .contentType(MediaType.APPLICATION_JSON)
     	    	.content(gson.toJson(cardDto3)))
 				.andReturn();
    	
    	assertEquals("error in http request: " + result.getResponse().getErrorMessage(),
        		200, result.getResponse().getStatus());
    	
    	logger.debug("Card 3 created: " + result.getResponse().getContentAsString());
    	
    	String c3Id = gson.fromJson(result.getResponse().getContentAsString(), PostResult.class).getElementId();
    	
    	result = this.mockMvc
     	    	.perform(get("/1/model/section/" + s1Id)
     	        .header("Authorization", "Bearer " + authorizationTokenUser1))
 				.andReturn();
    	
    	GetResult<ModelSectionDto> getResultSection2 = 
        		gson.fromJson(result.getResponse().getContentAsString(), 
        				new TypeToken<GetResult<ModelSectionDto>>(){}.getType());
    	
    	List<ModelCardWrapperDto> commonCards2 = getResultSection2.getData().getCardsWrappersCommon();

    	ModelCardWrapperDto c1b = commonCards2.stream()
    			.filter(c -> c.getId().equals(c1Id))
    			.findFirst().get(); 
    	
    	ModelCardWrapperDto c2b = commonCards2.stream()
    			.filter(c -> c.getId().equals(c2Id))
    			.findFirst().get();
    	
    	ModelCardWrapperDto c3b = commonCards2.stream()
    			.filter(c -> c.getId().equals(c3Id))
    			.findFirst().get();
    	
    	assertEquals("wrong after element",
        		null, c1b.getAfterElementId());
    	
    	assertEquals("wrong before element",
        		c3Id, c1b.getBeforeElementId());
    	
    	assertEquals("wrong after element",
        		c1Id, c3b.getAfterElementId());
    	
    	assertEquals("wrong before element",
        		c2Id, c3b.getBeforeElementId());
    	
    	assertEquals("wrong after element",
        		c3Id, c2b.getAfterElementId());
    	
    	assertEquals("wrong before element",
        		null, c2b.getBeforeElementId());
    	
    	
    	/** add card after first card */
    	ModelCardDto cardDto4 = new ModelCardDto("c3", ModelScope.COMMON); 
    	
    	result = this.mockMvc
     	    	.perform(post("/1/model/section/" + s1Id + "/cardWrapper")
     	        .header("Authorization", "Bearer " + authorizationTokenUser1)
     	        .param("onCardWrapperId", c1Id)
     	        .param("isBefore", "true")
     	        .contentType(MediaType.APPLICATION_JSON)
     	    	.content(gson.toJson(cardDto4)))
 				.andReturn();
    	
    	assertEquals("error in http request: " + result.getResponse().getErrorMessage(),
        		200, result.getResponse().getStatus());
    	
    	logger.debug("Card 4 created: " + result.getResponse().getContentAsString());
    	
    	String c4Id = gson.fromJson(result.getResponse().getContentAsString(), PostResult.class).getElementId();
    	
    	/* get section */
    	result = this.mockMvc
     	    	.perform(get("/1/model/section/" + s1Id)
     	        .header("Authorization", "Bearer " + authorizationTokenUser1))
 				.andReturn();
    	
    	GetResult<ModelSectionDto> getResultSection3 = 
        		gson.fromJson(result.getResponse().getContentAsString(), 
        				new TypeToken<GetResult<ModelSectionDto>>(){}.getType());
    	
    	List<ModelCardWrapperDto> commonCards3 = getResultSection3.getData().getCardsWrappersCommon();
    	
    	
    	ModelCardWrapperDto c1c = commonCards3.stream()
    			.filter(c -> c.getId().equals(c1Id))
    			.findFirst().get(); 
    	
    	ModelCardWrapperDto c2c = commonCards3.stream()
    			.filter(c -> c.getId().equals(c2Id))
    			.findFirst().get();
    	
    	ModelCardWrapperDto c3c = commonCards3.stream()
    			.filter(c -> c.getId().equals(c3Id))
    			.findFirst().get();
    	
    	ModelCardWrapperDto c4c = commonCards3.stream()
    			.filter(c -> c.getId().equals(c4Id))
    			.findFirst().get();
    	
    	/* order should be c4 c1 c3 c2 */
    	
    	assertEquals("wrong after element",
        		null, c4c.getAfterElementId());
    	
    	assertEquals("wrong before element",
        		c1Id, c4c.getBeforeElementId());
    	
    	assertEquals("wrong after element",
        		c4Id, c1c.getAfterElementId());
    	
    	assertEquals("wrong before element",
        		c3Id, c1c.getBeforeElementId());
    	
    	assertEquals("wrong after element",
        		c1Id, c3c.getAfterElementId());
    	
    	assertEquals("wrong before element",
        		c2Id, c3c.getBeforeElementId());
    	
    	assertEquals("wrong after element",
        		c3Id, c2c.getAfterElementId());
    	
    	assertEquals("wrong before element",
        		null, c2c.getBeforeElementId());
    	
    	
    	/* reorder cards in one section **/
    	/* move c1 before c4*/
    	result = this.mockMvc
     	    	.perform(put("/1/model/section/" + s1Id + "/moveCard/" + c1Id)
     	        .header("Authorization", "Bearer " + authorizationTokenUser1)
     	        .param("onSectionId", s1Id)
     	        .param("onCardWrapperId", c4Id)
     	        .param("isBefore", "true"))
 				.andReturn();
    	
    	assertEquals("error in http request: " + result.getResponse().getErrorMessage(),
        		200, result.getResponse().getStatus());
    	
    	/* get section */
    	result = this.mockMvc
     	    	.perform(get("/1/model/section/" + s1Id)
     	        .header("Authorization", "Bearer " + authorizationTokenUser1))
 				.andReturn();
    	
    	GetResult<ModelSectionDto> getResultSection4 = 
        		gson.fromJson(result.getResponse().getContentAsString(), 
        				new TypeToken<GetResult<ModelSectionDto>>(){}.getType());
    	
    	List<ModelCardWrapperDto> commonCards4 = getResultSection4.getData().getCardsWrappersCommon();
    	
    	
    	ModelCardWrapperDto c1d = commonCards4.stream()
    			.filter(c -> c.getId().equals(c1Id))
    			.findFirst().get(); 
    	
    	ModelCardWrapperDto c2d = commonCards4.stream()
    			.filter(c -> c.getId().equals(c2Id))
    			.findFirst().get();
    	
    	ModelCardWrapperDto c3d = commonCards4.stream()
    			.filter(c -> c.getId().equals(c3Id))
    			.findFirst().get();
    	
    	ModelCardWrapperDto c4d = commonCards4.stream()
    			.filter(c -> c.getId().equals(c4Id))
    			.findFirst().get();
    	
    	/* order should be c1 c4 c3 c2 */
    	assertEquals("wrong after element",
        		null, c1d.getAfterElementId());
    	
    	assertEquals("wrong before element",
        		c4Id, c1d.getBeforeElementId());
    	
    	assertEquals("wrong after element",
        		c1Id, c4d.getAfterElementId());
    	
    	assertEquals("wrong before element",
        		c3Id, c4d.getBeforeElementId());
    	
    	assertEquals("wrong after element",
        		c4Id, c3d.getAfterElementId());
    	
    	assertEquals("wrong before element",
        		c2Id, c3d.getBeforeElementId());
    	
    	assertEquals("wrong after element",
        		c3Id, c2d.getAfterElementId());
    	
    	assertEquals("wrong before element",
        		null, c2d.getBeforeElementId());
    	
    	/* move c2 after c1*/
    	result = this.mockMvc
     	    	.perform(put("/1/model/section/" + s1Id + "/moveCard/" + c2Id)
     	        .header("Authorization", "Bearer " + authorizationTokenUser1)
     	        .param("onSectionId", s1Id)
     	        .param("onCardWrapperId", c1Id)
     	        .param("isBefore", "false"))
 				.andReturn();
    	
    	assertEquals("error in http request: " + result.getResponse().getErrorMessage(),
        		200, result.getResponse().getStatus());
    	
    	/* get section */
    	result = this.mockMvc
     	    	.perform(get("/1/model/section/" + s1Id)
     	        .header("Authorization", "Bearer " + authorizationTokenUser1))
 				.andReturn();
    	
    	GetResult<ModelSectionDto> getResultSection5 = 
        		gson.fromJson(result.getResponse().getContentAsString(), 
        				new TypeToken<GetResult<ModelSectionDto>>(){}.getType());
    	
    	List<ModelCardWrapperDto> commonCards5 = getResultSection5.getData().getCardsWrappersCommon();
    	
    	
    	ModelCardWrapperDto c1e = commonCards5.stream()
    			.filter(c -> c.getId().equals(c1Id))
    			.findFirst().get(); 
    	
    	ModelCardWrapperDto c2e = commonCards5.stream()
    			.filter(c -> c.getId().equals(c2Id))
    			.findFirst().get();
    	
    	ModelCardWrapperDto c3e = commonCards5.stream()
    			.filter(c -> c.getId().equals(c3Id))
    			.findFirst().get();
    	
    	ModelCardWrapperDto c4e = commonCards5.stream()
    			.filter(c -> c.getId().equals(c4Id))
    			.findFirst().get();
    	
    	/* order should be c1 c2 c4 c3 */
    	assertEquals("wrong after element",
        		null, c1e.getAfterElementId());
    	
    	assertEquals("wrong before element",
        		c2Id, c1e.getBeforeElementId());
    	
    	assertEquals("wrong after element",
        		c1Id, c2e.getAfterElementId());
    	
    	assertEquals("wrong before element",
        		c4Id, c2e.getBeforeElementId());
    	
    	assertEquals("wrong after element",
        		c2Id, c4e.getAfterElementId());
    	
    	assertEquals("wrong before element",
        		c3Id, c4e.getBeforeElementId());
    	
    	assertEquals("wrong after element",
        		c4Id, c3e.getAfterElementId());
    	
    	assertEquals("wrong before element",
        		null, c3e.getBeforeElementId());
    	
    	
    	/* move c4 after c3*/
    	result = this.mockMvc
     	    	.perform(put("/1/model/section/" + s1Id + "/moveCard/" + c4Id)
     	        .header("Authorization", "Bearer " + authorizationTokenUser1)
     	        .param("onSectionId", s1Id)
     	        .param("onCardWrapperId", c3Id)
     	        .param("isBefore", "false"))
 				.andReturn();
    	
    	assertEquals("error in http request: " + result.getResponse().getErrorMessage(),
        		200, result.getResponse().getStatus());
    	
    	/* get section */
    	result = this.mockMvc
     	    	.perform(get("/1/model/section/" + s1Id)
     	        .header("Authorization", "Bearer " + authorizationTokenUser1))
 				.andReturn();
    	
    	GetResult<ModelSectionDto> getResultSection6 = 
        		gson.fromJson(result.getResponse().getContentAsString(), 
        				new TypeToken<GetResult<ModelSectionDto>>(){}.getType());
    	
    	List<ModelCardWrapperDto> commonCards6 = getResultSection6.getData().getCardsWrappersCommon();
    	
    	
    	ModelCardWrapperDto c1f = commonCards6.stream()
    			.filter(c -> c.getId().equals(c1Id))
    			.findFirst().get(); 
    	
    	ModelCardWrapperDto c2f = commonCards6.stream()
    			.filter(c -> c.getId().equals(c2Id))
    			.findFirst().get();
    	
    	ModelCardWrapperDto c3f = commonCards6.stream()
    			.filter(c -> c.getId().equals(c3Id))
    			.findFirst().get();
    	
    	ModelCardWrapperDto c4f = commonCards6.stream()
    			.filter(c -> c.getId().equals(c4Id))
    			.findFirst().get();
    	
    	/* order should be c1 c2 c3 c4 */
    	assertEquals("wrong after element",
        		null, c1f.getAfterElementId());
    	
    	assertEquals("wrong before element",
        		c2Id, c1f.getBeforeElementId());
    	
    	assertEquals("wrong after element",
        		c1Id, c2f.getAfterElementId());
    	
    	assertEquals("wrong before element",
        		c3Id, c2f.getBeforeElementId());
    	
    	assertEquals("wrong after element",
        		c2Id, c3f.getAfterElementId());
    	
    	assertEquals("wrong before element",
        		c4Id, c3f.getBeforeElementId());
    	
    	assertEquals("wrong after element",
        		c3Id, c4f.getAfterElementId());
    	
    	assertEquals("wrong before element",
        		null, c4f.getBeforeElementId());
    	

    	/** Now create another section and move c1 there */
    	ModelSectionDto section2Dto = new ModelSectionDto("s2", "", ModelScope.COMMON);
    	
    	result = this.mockMvc
     	    	.perform(post("/1/model/section/" + initiative.getTopModelSection().getId() + "/subsection")
     	        .header("Authorization", "Bearer " + authorizationTokenUser1)
     	        .contentType(MediaType.APPLICATION_JSON)
     	    	.content(gson.toJson(section2Dto)))
 				.andReturn();
    	
    	assertEquals("error in http request: " + result.getResponse().getErrorMessage(),
        		200, result.getResponse().getStatus());
    	
    	String s2Id = gson.fromJson(result.getResponse().getContentAsString(), PostResult.class).getElementId();

    	logger.debug("Subsection created: " + result.getResponse().getContentAsString());
    	
    	/* move c1 to s2 (without specifying the location)*/
    	result = this.mockMvc
     	    	.perform(put("/1/model/section/" + s1Id + "/moveCard/" + c1Id)
     	        .header("Authorization", "Bearer " + authorizationTokenUser1)
     	        .param("onSectionId", s2Id))
 				.andReturn();
    	
    	/* get section */
    	result = this.mockMvc
     	    	.perform(get("/1/model/section/" + s2Id)
     	        .header("Authorization", "Bearer " + authorizationTokenUser1))
 				.andReturn();
    	
    	GetResult<ModelSectionDto> getResultSection7 = 
        		gson.fromJson(result.getResponse().getContentAsString(), 
        				new TypeToken<GetResult<ModelSectionDto>>(){}.getType());
    	
    	List<ModelCardWrapperDto> commonCards7 = getResultSection7.getData().getCardsWrappersCommon();
    	
    	ModelCardWrapperDto c1g = commonCards7.stream()
    			.filter(c -> c.getId().equals(c1Id))
    			.findFirst().get(); 
    	
    	assertEquals("wrong after element",
        		null, c1g.getAfterElementId());
    	
    	assertEquals("wrong before element",
    			null, c1g.getBeforeElementId());   	
    	
    }
        
}
