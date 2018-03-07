package org.collectiveone.modules.model;

import java.util.UUID;

import org.collectiveone.common.BaseController;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.activity.dto.ActivityDto;
import org.collectiveone.modules.governance.DecisionVerdict;
import org.collectiveone.modules.governance.GovernanceService;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.InitiativeService;
import org.collectiveone.modules.model.dto.ModelCardDto;
import org.collectiveone.modules.model.dto.ModelCardWrapperDto;
import org.collectiveone.modules.model.dto.ModelSectionDto;
import org.collectiveone.modules.model.dto.ModelSectionGenealogyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/1")
public class ModelController extends BaseController { 
	
	@Autowired
	private ModelService modelService;

	@Autowired
	private GovernanceService governanceService;
	
	@Autowired
	private InitiativeService initiativeService;
	
	
	@RequestMapping(path = "/initiative/{initiativeId}/model", method = RequestMethod.GET) 
	public GetResult<ModelSectionDto> getModel(
			@PathVariable("initiativeId") String initiativeIdStr,
			@RequestParam(defaultValue = "0") Integer level,
			@RequestParam(defaultValue = "false") Boolean onlySections) {
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (!initiativeService.canAccess(initiativeId, getLoggedUserId())) {
			return new GetResult<ModelSectionDto>("error", "access denied", null);
		}
		
		return modelService.getModel(initiativeId, level, getLoggedUserId(), onlySections);
	}
	
	@RequestMapping(path = "/model/section/{sectionId}/subsection", method = RequestMethod.POST)
	public PostResult createSectionSubsection(
			@PathVariable("sectionId") String sectionIdStr,
			@RequestBody ModelSectionDto sectionDto) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID sectionId = UUID.fromString(sectionIdStr);
		UUID initiativeId = modelService.getSectionInitiative(sectionId).getId();
		
		if (governanceService.canEditModel(initiativeId, getLoggedUserId()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.createSection(sectionDto, sectionId, getLoggedUserId(), true);
	}
	
	@RequestMapping(path = "/model/section/{sectionId}/subsection/{subsectionId}", method = RequestMethod.PUT)
	public PostResult addExistingSectionSubsection(
			@PathVariable("sectionId") String sectionIdStr,
			@PathVariable("subsectionId") String subsectionIdStr, 
			@RequestParam(name = "beforeSubsectionId", defaultValue="") String beforeSubsectionIdStr) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID sectionId = UUID.fromString(sectionIdStr);
		UUID initiativeId = modelService.getSectionInitiative(sectionId).getId();
		
		if (governanceService.canEditModel(initiativeId, getLoggedUserId()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		/* dropped on subsection can be empty */
		UUID beforeSubsectionId =  beforeSubsectionIdStr.equals("") ? null : UUID.fromString(beforeSubsectionIdStr);
		
		return modelService.addSection(UUID.fromString(subsectionIdStr), sectionId, beforeSubsectionId, getLoggedUserId());
	}
	
	@RequestMapping(path = "/model/section/{sectionId}", method = RequestMethod.PUT) 
	public PostResult editSection(
			@PathVariable("sectionId") String sectionIdStr,
			@RequestBody ModelSectionDto sectionDto) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID sectionId = UUID.fromString(sectionIdStr);
		UUID initiativeId = modelService.getSectionInitiative(sectionId).getId();
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.editSection(sectionId, sectionDto, getLoggedUser().getC1Id());
	}
	
	@RequestMapping(path = "/model/section/{sectionId}/removeSubsection/{subsectionId}", method = RequestMethod.PUT) 
	public PostResult removeExistingSubsection(
			@PathVariable("sectionId") String sectionIdStr,
			@PathVariable("subsectionId") String subsectionIdStr) {
	
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID sectionId = UUID.fromString(sectionIdStr);
		UUID initiativeId = modelService.getSectionInitiative(sectionId).getId();
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.removeSubsectionFromSection(UUID.fromString(sectionIdStr), UUID.fromString(subsectionIdStr), getLoggedUserId());
	}
	
	@RequestMapping(path = "/model/section/{sectionId}/moveSubsection/{subsectionId}", method = RequestMethod.PUT) 
	public PostResult moveSectionSubsection(
			@PathVariable("sectionId") String sectionIdStr,
			@PathVariable("subsectionId") String subsectionIdStr,
			@RequestParam(name = "onSectionId", defaultValue="") String onSectionIdStr,
			@RequestParam(name = "beforeSubsectionId", defaultValue="") String beforeSubsectionIdStr) {
	
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID sectionId = UUID.fromString(sectionIdStr);
		UUID initiativeId = modelService.getSectionInitiative(sectionId).getId();
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		/* dropped on section can be empty */
		UUID onSectionId =  onSectionIdStr.equals("") ? null : UUID.fromString(onSectionIdStr);
		
		/* dropped on subsection can be empty */
		UUID beforeSubsectionId =  beforeSubsectionIdStr.equals("") ? null : UUID.fromString(beforeSubsectionIdStr);
		
		return modelService.moveSubsection(
				sectionId, 
				UUID.fromString(subsectionIdStr),
				onSectionId,
				beforeSubsectionId,
				getLoggedUserId());
	}
	
	@RequestMapping(path = "/model/section/{sectionId}/cardWrapper/{cardWrapperId}", method = RequestMethod.PUT) 
	public PostResult addExistingCard(
			@PathVariable("sectionId") String sectionIdStr,
			@PathVariable("cardWrapperId") String cardWrapperIdStr,
			@RequestParam(name = "beforeCardWrapperId", defaultValue="") String beforeCardWrapperIdStr) {
	
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);
		UUID initiativeId = modelService.getCardWrapperInitiative(cardWrapperId).getId();
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		UUID beforeCardWrapperId = beforeCardWrapperIdStr.equals("") ? null : UUID.fromString(beforeCardWrapperIdStr);
		
		return modelService.addCardToSection(UUID.fromString(sectionIdStr), cardWrapperId, beforeCardWrapperId, getLoggedUserId());
	}
	
	@RequestMapping(path = "/model/section/{sectionId}/removeCard/{cardWrapperId}", method = RequestMethod.PUT) 
	public PostResult removeExistingCard(
			@PathVariable("sectionId") String sectionIdStr,
			@PathVariable("cardWrapperId") String cardWrapperIdStr) {
	
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);
		UUID initiativeId = modelService.getCardWrapperInitiative(cardWrapperId).getId();
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.removeCardFromSection(UUID.fromString(sectionIdStr), UUID.fromString(cardWrapperIdStr), getLoggedUserId());
	}
	
	@RequestMapping(path = "/model/section/{sectionId}/moveCard/{cardWrapperId}", method = RequestMethod.PUT) 
	public PostResult moveExistingCard(
			@PathVariable("sectionId") String fromSectionIdStr,
			@PathVariable("cardWrapperId") String cardWrapperIdStr,
			@RequestParam(name = "onSectionId") String onSectionIdStr,
			@RequestParam(name = "onCardWrapperId", defaultValue="") String onCardWrapperIdStr) {
	
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);
		UUID initiativeId = modelService.getCardWrapperInitiative(cardWrapperId).getId();
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		/* dropped on card can be empty */
		UUID onCardWrapperID =  null;
		if (!onCardWrapperIdStr.equals("")) {
			onCardWrapperID = UUID.fromString(onCardWrapperIdStr);
		}		
		
		return modelService.moveCardWrapper(
				UUID.fromString(fromSectionIdStr), 
				cardWrapperId,
				UUID.fromString(onSectionIdStr),
				onCardWrapperID,
				getLoggedUserId());
	}
	
	@RequestMapping(path = "/model/section/{sectionId}", method = RequestMethod.GET) 
	public GetResult<ModelSectionDto> getSection(
			@PathVariable("sectionId") String sectionIdStr,
			@RequestParam(defaultValue = "1") Integer levels) {
		
		UUID sectionId = UUID.fromString(sectionIdStr);
		UUID initiativeId = modelService.getSectionInitiative(sectionId).getId();
		
		if (!initiativeService.canAccess(initiativeId, getLoggedUserId())) {
			return new GetResult<ModelSectionDto>("error", "access denied", null);
		}
		
		return modelService.getSection(sectionId, levels, getLoggedUserId(), false);
	}
	
	@RequestMapping(path = "/model/section/{sectionId}/cardWrappers", method = RequestMethod.GET) 
	public GetResult<List<ModelCardWrapperDto>> getSectionCardWrappers(
			@PathVariable("sectionId") String sectionIdStr) {
		
		UUID sectionId = UUID.fromString(sectionIdStr);
		UUID initiativeId = modelService.getSectionInitiative(sectionId).getId();
		
		if (!initiativeService.canAccess(initiativeId, getLoggedUserId())) {
			return new GetResult<List<ModelCardWrapperDto>>("error", "access denied", null);
		}
		
		return modelService.getSectionCardWrappers(sectionId, getLoggedUserId());
	}
	
	@RequestMapping(path = "/model/section/{sectionId}/genealogy", method = RequestMethod.GET) 
	public GetResult<ModelSectionGenealogyDto> getSectionGenealogy(
			@PathVariable("sectionId") String sectionIdStr) {
		
		UUID sectionId = UUID.fromString(sectionIdStr);
		UUID initiativeId = modelService.getSectionInitiative(sectionId).getId();
		
		if (!initiativeService.canAccess(initiativeId, getLoggedUserId())) {
			return new GetResult<ModelSectionGenealogyDto>("error", "access denied", null);
		}
		
		return modelService.getSectionParentGenealogy(sectionId);
	}
	
	@RequestMapping(path = "/model/section/{sectionId}", method = RequestMethod.DELETE) 
	public PostResult deleteSection(
			@PathVariable("sectionId") String sectionIdStr) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID sectionId = UUID.fromString(sectionIdStr);
		UUID initiativeId = modelService.getSectionInitiative(sectionId).getId();
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.deleteSection(UUID.fromString(sectionIdStr), getLoggedUser().getC1Id());
	}
	
	
	@RequestMapping(path = "/model/section/{sectionId}/cardWrapper", method = RequestMethod.POST)
	public PostResult createCardWrapper(
			@PathVariable("sectionId") String sectionIdStr,
			@RequestBody ModelCardDto cardDto,
			@RequestParam(name="beforeCardWrapperId", defaultValue="") String beforeCardWrapperIdStr,
			@RequestParam(name="afterCardWrapperId", defaultValue="") String afterCardWrapperIdStr) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID sectionId = UUID.fromString(sectionIdStr);
		UUID initiativeId = modelService.getSectionInitiative(sectionId).getId();
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		UUID beforeId = beforeCardWrapperIdStr.equals("") ? null : UUID.fromString(beforeCardWrapperIdStr);
		UUID afterId = afterCardWrapperIdStr.equals("") ? null : UUID.fromString(afterCardWrapperIdStr);
		
		return modelService.createCardWrapper(cardDto, sectionId, getLoggedUser().getC1Id(), beforeId, afterId);
	}
	
	@RequestMapping(path = "/model/cardWrapper/{cardWrapperId}", method = RequestMethod.GET) 
	public GetResult<ModelCardWrapperDto> getCardWrapper(
			@PathVariable("cardWrapperId") String cardWrapperIdStr) {
		
		UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);
		UUID initiativeId = modelService.getCardWrapperInitiative(cardWrapperId).getId();
		
		if (!initiativeService.canAccess(initiativeId, getLoggedUserId())) {
			return new GetResult<ModelCardWrapperDto>("error", "access denied", null);
		}
		
		return modelService.getCardWrapper(cardWrapperId, getLoggedUserId());
	}
	

	@RequestMapping(path = "/model/cardWrapper/{cardWrapperId}", method = RequestMethod.PUT) 
	public PostResult editCardWrapper(
			@PathVariable("cardWrapperId") String cardWrapperIdStr,
			@RequestBody ModelCardDto cardDto) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);
		UUID initiativeId = modelService.getCardWrapperInitiative(cardWrapperId).getId();
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.editCardWrapper(initiativeId, cardWrapperId, cardDto, getLoggedUser().getC1Id());
	}
	
	@RequestMapping(path = "/model/section/{sectionId}/cardWrappers/search", method = RequestMethod.GET) 
	public GetResult<Page<ModelCardWrapperDto>> searchCardWrapper(
			@PathVariable("sectionId") String sectionIdStr,
			@RequestParam(name="query", defaultValue="") String query,
			@RequestParam(name="page", defaultValue="0") Integer page,
			@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
			@RequestParam(name="levels", defaultValue="1") Integer levels,
			@RequestParam(name="sortBy", defaultValue="1") String sortBy) {
		
		UUID sectionId = UUID.fromString(sectionIdStr);
		UUID initiativeId = modelService.getSectionInitiative(sectionId).getId();
		
		if (!initiativeService.canAccess(initiativeId, getLoggedUserId())) {
			return new GetResult<Page<ModelCardWrapperDto>>("error", "access denied", null);
		}
		
		return modelService.searchCardWrapper(sectionId, query, page, pageSize, sortBy, levels, getLoggedUserId());
	}
	
	@RequestMapping(path = "/model/cardWrapper/{cardWrapperId}", method = RequestMethod.DELETE) 
	public PostResult deleteCardWrapper(
			@PathVariable("cardWrapperId") String cardWrapperIdStr) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);
		UUID initiativeId = modelService.getCardWrapperInitiative(cardWrapperId).getId();
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.deleteCardWrapper(cardWrapperId, getLoggedUser().getC1Id());
	}
		
	@RequestMapping(path = "/model/section/search", method = RequestMethod.GET) 
	public GetResult<Page<ModelSectionDto>> searchSection(
			@RequestParam("sectionId") String sectionIdStr,
			@RequestParam("query") String query,
			@RequestParam("page") Integer page,
			@RequestParam("size") Integer size) {
		
		UUID sectionId = UUID.fromString(sectionIdStr);
		UUID initiativeId = modelService.getSectionInitiative(sectionId).getId();
		
		if (!initiativeService.canAccess(initiativeId, getLoggedUserId())) {
			return new GetResult<Page<ModelSectionDto>>("error", "access denied", null);
		}
		
		return modelService.searchSection(query, new PageRequest(page, size), initiativeId, getLoggedUserId());
	}
	
	@RequestMapping(path = "/activity/model/section/{sectionId}", method = RequestMethod.GET)
	public GetResult<Page<ActivityDto>> getActivityUnderSection(
			@PathVariable("sectionId") String sectionIdStr,
			@RequestParam("page") Integer page,
			@RequestParam("size") Integer size, 
			@RequestParam(name="levels", defaultValue="1") Integer levels,
			@RequestParam(name="onlyMessages", defaultValue="false") Boolean onlyMessages) {
		
		UUID sectionId = UUID.fromString(sectionIdStr);
		
		Initiative initiative = modelService.getSectionInitiative(sectionId);
		
		if (!initiativeService.canAccess(initiative.getId(), getLoggedUserId())) {
			return new GetResult<Page<ActivityDto>>("error", "access denied", null);
		}
		
		return modelService.getActivityResultUnderSection(sectionId, new PageRequest(page, size), onlyMessages, levels);
	}
	
	@RequestMapping(path = "/model/card/{cardWrapperId}/countMessages", method = RequestMethod.GET)
	public GetResult<Long> countMessagesUnderCard(
			@PathVariable("cardWrapperId") String cardWrapperIdStr, 
			@RequestParam(name="onlyMessages", defaultValue="false") Boolean onlyMessages) {
		
		UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);
		UUID initiativeId = modelService.getCardWrapperInitiative(cardWrapperId).getId();
		
		if (!initiativeService.canAccess(initiativeId, getLoggedUserId())) {
			return new GetResult<Long>("error", "access denied", null);
		}
		
		return modelService.countMessagesUnderCard(cardWrapperId, onlyMessages);
	}
	
	@RequestMapping(path = "/activity/model/card/{cardWrapperId}", method = RequestMethod.GET)
	public GetResult<Page<ActivityDto>> getActivityUnderCard(
			@PathVariable("cardWrapperId") String cardWrapperIdStr,
			@RequestParam("page") Integer page,
			@RequestParam("size") Integer size, 
			@RequestParam(name="onlyMessages", defaultValue="false") Boolean onlyMessages) {
		
		UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);
		
		Initiative initiative = modelService.getCardWrapperInitiative(cardWrapperId);
		
		if (!initiativeService.canAccess(initiative.getId(), getLoggedUserId())) {
			return new GetResult<Page<ActivityDto>>("error", "access denied", null);
		}
		
		return modelService.getActivityResultUnderCard(cardWrapperId, new PageRequest(page, size), onlyMessages);
	}
	
	@RequestMapping(path = "/model/card/{cardWrapperId}/like", method = RequestMethod.PUT)
	public PostResult setCardLike(
			@PathVariable("cardWrapperId") String cardWrapperIdStr,
			@RequestParam("likeStatus") Boolean likeStatus) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		// UUID initiativeId = UUID.fromString(initiativeIdStr);
		UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);
		Initiative initiative = modelService.getCardWrapperInitiative(cardWrapperId);
		
		if (!initiativeService.isMemberOfEcosystem(initiative.getId(), getLoggedUserId())) {
			return new PostResult("error", "not authorized", "");
		}
				
		return modelService.setLikeToCard(cardWrapperId, getLoggedUserId(), likeStatus);
	}
	
	@RequestMapping(path = "/model/card/{cardWrapperId}/countLikes", method = RequestMethod.GET)
	public GetResult<Integer> countCardLikes(
			@PathVariable("cardWrapperId") String cardWrapperIdStr, 
			@RequestParam(name="onlyMessages", defaultValue="false") Boolean onlyMessages) {
		
		UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);
		UUID initiativeId = modelService.getCardWrapperInitiative(cardWrapperId).getId();
		
		if (!initiativeService.canAccess(initiativeId, getLoggedUserId())) {
			return new GetResult<Integer>("error", "access denied", null);
		}
		
		return modelService.countCardLikes(cardWrapperId);
	}
}
