package org.collectiveone.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.collectiveone.AbstractTest;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.governance.DecisionMakerRole;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.InitiativeService;
import org.collectiveone.modules.initiatives.dto.MemberDto;
import org.collectiveone.modules.initiatives.dto.NewInitiativeDto;
import org.collectiveone.modules.initiatives.repositories.InitiativeRepositoryIf;
import org.collectiveone.modules.model.ModelScope;
import org.collectiveone.modules.model.ModelService;
import org.collectiveone.modules.model.dto.ModelCardDto;
import org.collectiveone.modules.model.dto.ModelCardWrapperDto;
import org.collectiveone.modules.model.dto.ModelSectionDto;
import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public class TestModelService extends AbstractTest {
	
	@Autowired
    private ModelService modelService;
	
	@Autowired
    private AppUserService appUserService;
	
	@Autowired
    private InitiativeService initiativeService;
	
	@Autowired
	private InitiativeRepositoryIf initiativeRepository;
	
	@Value("${AUTH0ID}")
	String userAuth0Id;
	
	UUID userId;
	
	UUID initiativeId;
	
	UUID topLevelSectionId;
	
    @Before
    public void setUp() {
    	
    	
//    	userAuth0Id
    	
    	/* create user */
    	AppUser user = appUserService.getOrCreateFromAuth0Id(userAuth0Id);
    	userId = user.getC1Id();
    	
    	/* create initiative */
    	NewInitiativeDto initiativeDto = new NewInitiativeDto();
    	initiativeDto.setName("Test Initiative");
    	initiativeDto.setDriver("test initiative");
    	
    	List<MemberDto> members = new ArrayList<MemberDto>();
    	
    	MemberDto member = new MemberDto();
    	
    	member.setUser(user.toDtoLight());
    	member.setRole(DecisionMakerRole.ADMIN.toString());
    	members.add(member);
    	
    	initiativeDto.setMembers(members);
    	
    	PostResult result = initiativeService.init(userId, initiativeDto);
    	
    	System.out.print("creating dummy section... ");
    	
    	initiativeId = UUID.fromString(result.getElementId());
    	
    	Initiative initiative = initiativeRepository.findById(initiativeId);
    	topLevelSectionId = initiative.getTopModelSubsection().getSection().getId(); 
    	
    	ModelSectionDto sectionDto = new ModelSectionDto();
    	
    	sectionDto.setDescription("test section");
    	sectionDto.setTitle("Test Section");
    	
    	modelService.createSection(sectionDto, topLevelSectionId, userId, null, false);
    	
    	System.out.println("done!");
    }

    @After
    public void tearDown() {
        // clean up after each test method
    }
    
    @SuppressWarnings("unused")
	private UUID Str2UUID(String str) {
    	try {
    		return UUID.fromString(str);
    	} catch (Exception e) {
    		return null;
    	}    	
    }
    
    @Test
    public void isCreateCommonCards() {
    	
    	PostResult result;
    	
    	System.out.print("test common cards creation... ");
    	
    	ModelCardDto cardDto = new ModelCardDto();
    	
    	cardDto.setTitle("card common 1");
    	cardDto.setText("card common 1 - text");
    	cardDto.setNewScope(ModelScope.COMMON);
    	
    	result = modelService.createCardWrapper(
				cardDto, 
				topLevelSectionId, 
				userId, 
				null, 
				false);
    	
    	UUID cardWrp1Id = Str2UUID(result.getElementId());
    	
    	cardDto.setTitle("card common 2");
    	cardDto.setText("card common 2 - text");
    	cardDto.setNewScope(ModelScope.COMMON);
    	
    	result = modelService.createCardWrapper(
				cardDto, 
				topLevelSectionId, 
				userId, 
				null, 
				false);
    	
    	UUID cardWrp2Id = Str2UUID(result.getElementId());

    	cardDto.setTitle("card common 3");
    	cardDto.setText("card common 3 - text");
    	cardDto.setNewScope(ModelScope.COMMON);
    	
    	result = modelService.createCardWrapper(
				cardDto, 
				topLevelSectionId, 
				userId, 
				null, 
				false);
    	
    	UUID cardWrp3Id = Str2UUID(result.getElementId());
    	
    	
    	/* get cards */
    	GetResult<ModelSectionDto> cardsResult = modelService.getSection(topLevelSectionId, null, 1, userId, false);
    	
    	ModelSectionDto sectionDto = (ModelSectionDto) cardsResult.getData();
    	
    	List<ModelCardWrapperDto> commonCards = sectionDto.getCardsWrappersCommon();
    	
    	assertTrue("wrong number of cards", commonCards.size() == 3);
    	
    	assertTrue("wrong card link", Str2UUID(commonCards.get(0).getAfterElementId()) == null);
    	assertTrue("wrong card link", Str2UUID(commonCards.get(0).getBeforeElementId()).equals(cardWrp2Id));
    	
    	assertTrue("wrong card link", Str2UUID(commonCards.get(1).getAfterElementId()).equals(cardWrp1Id));
    	assertTrue("wrong card link", Str2UUID(commonCards.get(1).getBeforeElementId()).equals(cardWrp3Id));
    	
    	assertTrue("wrong card link", Str2UUID(commonCards.get(2).getAfterElementId()).equals(cardWrp2Id));
    	assertTrue("wrong card link", Str2UUID(commonCards.get(2).getBeforeElementId()) == null);
    	
    	System.out.println(result.getResult());
    	
    }
   
    
}
