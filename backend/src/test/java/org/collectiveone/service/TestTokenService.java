package org.collectiveone.service;

import java.util.List;
import java.util.UUID;

import org.collectiveone.AbstractTest;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.activity.ActivityService;
import org.collectiveone.modules.initiatives.InitiativeService;
import org.collectiveone.modules.initiatives.dto.InitiativeTagDto;
import org.collectiveone.modules.initiatives.repositories.InitiativeRepositoryIf;
import org.collectiveone.modules.tokens.TokenHolder;
import org.collectiveone.modules.tokens.TokenService;
import org.collectiveone.modules.tokens.TokenType;
import org.collectiveone.modules.tokens.repositories.TokenHolderRepositoryIf;
import org.collectiveone.modules.tokens.repositories.TokenTypeRepositoryIf;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@Transactional
public class TestTokenService extends AbstractTest {
	
	@Autowired
    private TokenService tokenService;
	
	@Autowired
	InitiativeRepositoryIf initiativeRepository;
	
	@Autowired
	TokenTypeRepositoryIf tokenTypeRepository;
	
	@Autowired
	TokenHolderRepositoryIf tokenHolderRepository;
	
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
    public void isCreateTokenWorks() {
    		TokenType token=this.tokenService.create("testtokenname", "T");
    		assertTrue(token.getName().equals("testtokenname"));
    
    }
    
    
    
    @Test
    public void isGetTokenTypeWOrks() {
    		TokenTypeRepositoryIf tokenTypeRepositoryMock=mock(TokenTypeRepositoryIf.class);
    		TokenType mockToken=new TokenType();
    		UUID uuid=UUID.randomUUID();
    		mockToken.setId(uuid);
    		
    		when(tokenTypeRepositoryMock.findById(uuid)).thenReturn(mockToken);
    		TokenType token=tokenTypeRepositoryMock.findById(uuid);
    		assertTrue(token.getId().toString().equals(uuid.toString()));		
    
    }
    
    @Test
	public void isGetHolderWorks() {
		/* get TokenHolder row in token holders database */
    		TokenHolderRepositoryIf tokenHolderRepositoryMock=mock(TokenHolderRepositoryIf.class);
    		UUID tokenTypeId=UUID.randomUUID();
    		UUID holderId=UUID.randomUUID();
    		
    		TokenHolder holder=new TokenHolder();
    		holder.setHolderId(holderId);
    		holder.setId(tokenTypeId);
    		
    		when(tokenHolderRepositoryMock.findByTokenTypeIdAndHolderId(tokenTypeId, holderId));
		assertTrue(tokenHolderRepositoryMock.findByTokenTypeIdAndHolderId(tokenTypeId, holderId).getId().toString().equals(holder.getId().toString()));
	}
    
   
    
}
