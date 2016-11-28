package org.collectiveone;

import javax.transaction.Transactional;

import org.collectiveone.services.DbServicesTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
@Transactional
public class CollectiveOneWebappApplicationTests {

	@Autowired
	DbServicesTest dbService;
	
	@Test
	public void testDecisionAlgoruthm() {
		
		dbService.createUser("pepoospina", 			"pepo.ospina@gmail.com", 	"pepoospina1", 		true);
		dbService.createUser("estebanortizospina", 	"temp1@gmail.com", 			"Esteban1017", 		true);
		dbService.createUser("mospina", 			"temp2@gmail.com", 			"amoajose", 		true);
		dbService.createUser("jpmarindiaz", 		"temp3@gmail.com", 			"jpmarindiazx", 	true);
		dbService.createUser("quiquin", 			"temp4@gmail.com", 			"quiquines", 		true);
		dbService.createUser("collectiveone", 		"temp5@gmail.com", 			"collectiveone", 	false);
		
	}

}
