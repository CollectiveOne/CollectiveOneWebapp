package org.collectiveone.service;

import java.util.List;
import java.util.UUID;

import org.collectiveone.AbstractTest;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.activity.ActivityService;
import org.collectiveone.modules.conversations.MessageRepositoryIf;
import org.collectiveone.modules.conversations.MessageService;
import org.collectiveone.modules.conversations.MessageThread;
import org.collectiveone.modules.conversations.MessageThreadContextType;
import org.collectiveone.modules.conversations.MessageThreadRepositoryIf;
import org.collectiveone.modules.initiatives.InitiativeService;
import org.collectiveone.modules.initiatives.dto.InitiativeTagDto;
import org.collectiveone.modules.initiatives.repositories.InitiativeRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelCardWrapperRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelSectionRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelViewRepositoryIf;
import org.collectiveone.modules.tokens.TokenHolder;
import org.collectiveone.modules.tokens.TokenService;
import org.collectiveone.modules.tokens.TokenType;
import org.collectiveone.modules.tokens.repositories.TokenHolderRepositoryIf;
import org.collectiveone.modules.tokens.repositories.TokenTypeRepositoryIf;
import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserDto;
import org.collectiveone.modules.users.AppUserRepositoryIf;
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
public class TestMessageService extends AbstractTest {
	
	
	@Autowired
	private MessageService messageService;
	
	MessageService messageServiceMock;

	
	
    @Before
    public void setUp() {
        //service.evictCache();
    		messageServiceMock=mock(MessageService.class);
    }

    @After
    public void tearDown() {
        // clean up after each test method
    }
    
    @Test
    public void isGetInitiativeIdOfMessageWorks() {
    		UUID id=UUID.randomUUID();
    		when(messageServiceMock.getInitiativeIdOfMessage(MessageThreadContextType.MODEL_CARD, id)).thenReturn(id);
    		assertTrue(messageServiceMock.getInitiativeIdOfMessage(MessageThreadContextType.MODEL_CARD, id).toString().equals(id.toString()));	
    }
    
    
    @Test
    public void isGetInitiativeIdOfMessageThreadWorks() {
    		MessageThread thread = new MessageThread();
    		
    		UUID id=UUID.randomUUID();
    		thread.setId(id);
    		thread.setContextType(MessageThreadContextType.MODEL_CARD);
    		
    		when(messageServiceMock.getInitiativeIdOfMessage(MessageThreadContextType.MODEL_CARD, id)).thenReturn(thread.getId());
    		
    		UUID returnedId=messageServiceMock.getInitiativeIdOfMessage(MessageThreadContextType.MODEL_CARD, id);
    		
    		assertTrue(returnedId.toString().equals(id.toString()));	
    }
    
    
    
    
    
   
    
}
