package org.collectiveone.service;

import java.util.UUID;

import org.collectiveone.AbstractTest;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.modules.activity.ActivityService;
import org.collectiveone.modules.initiatives.InitiativeService;
import org.collectiveone.modules.initiatives.dto.InitiativeTagDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class TestInitiativeService extends AbstractTest {
	@Autowired
    private InitiativeService service;

    @Before
    public void setUp() {
        //service.evictCache();
    }

    @After
    public void tearDown() {
        // clean up after each test method
    }

    @Test
    public void testGetTag() {

    		GetResult<InitiativeTagDto> result=service.getTag(UUID.fromString("c0a80067-61b9-1590-8161-b91c6d570010"));
    		assertThat(true);

    }
}
