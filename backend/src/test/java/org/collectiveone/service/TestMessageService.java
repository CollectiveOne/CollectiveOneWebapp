package org.collectiveone.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.collectiveone.AbstractTest;
import org.collectiveone.modules.conversations.MessageService;
import org.collectiveone.modules.conversations.MessageThread;
import org.collectiveone.modules.conversations.MessageThreadContextType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public class TestMessageService extends AbstractTest {
	
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
    		when(messageServiceMock.getModelSectionIdOfMessage(MessageThreadContextType.MODEL_CARD, id)).thenReturn(id);
    		assertTrue(messageServiceMock.getModelSectionIdOfMessage(MessageThreadContextType.MODEL_CARD, id).toString().equals(id.toString()));	
    }
    
    
    @Test
    public void isGetInitiativeIdOfMessageThreadWorks() {
    		MessageThread thread = new MessageThread();
    		
    		UUID id=UUID.randomUUID();
    		thread.setId(id);
    		thread.setContextType(MessageThreadContextType.MODEL_CARD);
    		
    		when(messageServiceMock.getModelSectionIdOfMessage(MessageThreadContextType.MODEL_CARD, id)).thenReturn(thread.getId());
    		
    		UUID returnedId=messageServiceMock.getModelSectionIdOfMessage(MessageThreadContextType.MODEL_CARD, id);
    		
    		assertTrue(returnedId.toString().equals(id.toString()));	
    }
    
    
    
    
    
   
    
}
