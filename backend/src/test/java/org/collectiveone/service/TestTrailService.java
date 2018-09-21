package org.collectiveone.service;

import static org.junit.Assert.assertTrue;

import org.collectiveone.AbstractTest;
import org.collectiveone.modules.contexts.PerspectiveOuterService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public class TestTrailService extends AbstractTest {
	
	@Autowired
    private PerspectiveOuterService trailOuterService;
	
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
    public void ifCreateInitiativeIsSuccess() {
    	// UUID c1Id=this.userService.ge
    }

    @Test
    public void createContext() {
		assertTrue(false);
    }
    
}
