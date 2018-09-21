package org.collectiveone.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.collectiveone.AbstractTest;
import org.collectiveone.modules.contexts.dto.NewContextDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.google.gson.Gson;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestContextController extends AbstractTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	private String authorizationToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRvbUB4LmNvbSIsIm5hbWUiOiJ0b21AeC5jb20iLCJwaWN0dXJlIjoiaHR0cHM6Ly9zLmdyYXZhdGFyLmNvbS9hdmF0YXIvMDgzYTMxYTgwZGNmZGVjNzJkMTBhMDgyYzJiOTUyMDM_cz00ODAmcj1wZyZkPWh0dHBzJTNBJTJGJTJGY2RuLmF1dGgwLmNvbSUyRmF2YXRhcnMlMkZ0by5wbmciLCJuaWNrbmFtZSI6InRvbSIsImFwcF9tZXRhZGF0YSI6eyJzY29wZSI6InJvbGVfdXNlciJ9LCJzY29wZSI6InJvbGVfdXNlciIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiY2xpZW50SUQiOiJrdURYMVpWb3JBbHk1UFlkeVY3MjF6Um9UZjBLMG9ybSIsInVwZGF0ZWRfYXQiOiIyMDE4LTA5LTIxVDEzOjM2OjM5LjYxMFoiLCJ1c2VyX2lkIjoiYXV0aDB8NTllZGFjYWI3NDE2OGE3MDM0MTA5OTE4IiwiaWRlbnRpdGllcyI6W3sidXNlcl9pZCI6IjU5ZWRhY2FiNzQxNjhhNzAzNDEwOTkxOCIsInByb3ZpZGVyIjoiYXV0aDAiLCJjb25uZWN0aW9uIjoidGVzdCIsImlzU29jaWFsIjpmYWxzZX1dLCJjcmVhdGVkX2F0IjoiMjAxNy0xMC0yM1QwODo0NzozOS4wMTdaIiwiaXNzIjoiaHR0cHM6Ly9jb2xsZWN0aXZlb25lLmF1dGgwLmNvbS8iLCJzdWIiOiJhdXRoMHw1OWVkYWNhYjc0MTY4YTcwMzQxMDk5MTgiLCJhdWQiOiJrdURYMVpWb3JBbHk1UFlkeVY3MjF6Um9UZjBLMG9ybSIsImlhdCI6MTUzNzUzNzAwMCwiZXhwIjoxNTQwMTI5MDAwfQ.qFU32s_botYT21m3wx4XtXr_GqZ_Y1wBR93s9VIPfV4";
    
	@Before
    public void setUp() throws Exception {
		this.mockMvc
	    	.perform(get("/1/user/myProfile"))
	    	.andDo(MockMvcResultHandlers.print());
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
