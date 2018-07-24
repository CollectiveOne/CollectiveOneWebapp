package org.collectiveone.modules.model;

import java.util.UUID;

import org.collectiveone.common.BaseController;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.activity.dto.ActivityDto;
import org.collectiveone.modules.model.dto.CardWrappersHolderDto;
import org.collectiveone.modules.model.dto.ModelCardDto;
import org.collectiveone.modules.model.dto.ModelCardWrapperDto;
import org.collectiveone.modules.model.dto.ModelSectionDto;
import org.collectiveone.modules.model.dto.ModelSectionLinkedDto;
import org.collectiveone.modules.model.repositories.ModelCardWrapperAdditionRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelCardWrapperRepositoryIf;
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
	
	
	
	// #####
	// @RequestMapping(path = "/initiative/{initiativeId}/model", method = RequestMethod.GET) 
	// public GetResult<ModelSectionDto> getModel(
	// 		@PathVariable("initiativeId") String initiativeIdStr,
	// 		@RequestParam(defaultValue = "0") Integer level,
	// 		@RequestParam(defaultValue = "false") Boolean onlySections) {
		
	// 	UUID initiativeId = UUID.fromString(initiativeIdStr);
		
	// 	if (!modelService.canAccess(initiativeId, getLoggedUserId())) {
	// 		return new GetResult<ModelSectionDto>("error", "access denied", null);
	// 	}
		
	// 	return modelService.getModel(initiativeId, level, getLoggedUserId(), onlySections);
	// }
	
	@RequestMapping(path = "/model/section/{sectionId}/subsection", method = RequestMethod.POST)
	public PostResult createSectionSubsection(
			@PathVariable("sectionId") String sectionIdStr,
			@RequestBody ModelSectionDto sectionDto) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID sectionId = UUID.fromString(sectionIdStr);
		
		if (!modelService.canEdit(sectionId, getLoggedUserId())) {
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
		
		if (!modelService.canEdit(sectionId, getLoggedUserId())) {
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
		if (!modelService.canEdit(sectionId, getLoggedUserId())) {
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
		if (!modelService.canEdit(sectionId, getLoggedUserId())) {
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
		if (!modelService.canEdit(sectionId, getLoggedUserId())) {
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
			@RequestParam(name = "onCardWrapperId", defaultValue="") String onCardWrapperIdStr,
			@RequestParam(name = "isBefore", defaultValue="false") Boolean isBefore,
			@RequestParam(name = "scope", defaultValue="") String scopeStr) {
	
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);
		
		if (!modelService.canEdit(UUID.fromString(sectionIdStr), getLoggedUserId())) {
			return new PostResult("error", "not authorized", "");
		}
		
		ModelCardWrapperScope scope = scopeStr.equals("") ? ModelCardWrapperScope.COMMON : ModelCardWrapperScope.valueOf(scopeStr);
		
		UUID onCardWrapperId = onCardWrapperIdStr.equals("") ? null : UUID.fromString(onCardWrapperIdStr);
		
		return modelService.addCardToSection(
				UUID.fromString(sectionIdStr), 
				cardWrapperId, 
				onCardWrapperId, 
				isBefore,
				getLoggedUserId(), 
				scope);
	}
	
	@RequestMapping(path = "/model/section/{sectionId}/removeCard/{cardWrapperId}", method = RequestMethod.PUT) 
	public PostResult removeExistingCard(
			@PathVariable("sectionId") String sectionIdStr,
			@PathVariable("cardWrapperId") String cardWrapperIdStr) {
	
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);
		if (!modelService.canEdit(UUID.fromString(sectionIdStr), getLoggedUserId())) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.removeCardFromSection(UUID.fromString(sectionIdStr), UUID.fromString(cardWrapperIdStr), getLoggedUserId());
	}
	
	@RequestMapping(path = "/model/section/{sectionId}/moveCard/{cardWrapperId}", method = RequestMethod.PUT) 
	public PostResult moveExistingCard(
			@PathVariable("sectionId") String fromSectionIdStr,
			@PathVariable("cardWrapperId") String cardWrapperIdStr,
			@RequestParam(name = "onSectionId") String onSectionIdStr,
			@RequestParam(name = "onCardWrapperId", defaultValue="") String onCardWrapperIdStr,
			@RequestParam(name = "isBefore", defaultValue="false") Boolean isBefore) {
	
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);

		//#### i am using onSectionIdStr, can
		if (!modelService.canEdit(UUID.fromString(onSectionIdStr), getLoggedUserId())) {
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
				isBefore,
				getLoggedUserId());
	}
	
	@RequestMapping(path = "/model/section/{sectionId}", method = RequestMethod.GET) 
	public GetResult<ModelSectionDto> getSection(
			@PathVariable("sectionId") String sectionIdStr,
			@RequestParam(defaultValue = "1") Integer levels,
			@RequestParam(name="onlySections", defaultValue = "false") Boolean onlySections) {
		
		UUID sectionId = UUID.fromString(sectionIdStr);
		
		if (!modelService.canAccess(sectionId, getLoggedUserId())) {
			return new GetResult<ModelSectionDto>("error", "access denied", null);
		}
		
		return modelService.getSection(sectionId, levels, getLoggedUserId(), onlySections);
	}
	
	@RequestMapping(path = "/model/section/{sectionId}/cardWrappers", method = RequestMethod.GET) 
	public GetResult<CardWrappersHolderDto> getSectionCardWrappers(
			@PathVariable("sectionId") String sectionIdStr) {
		
		UUID sectionId = UUID.fromString(sectionIdStr);
		
		if (!modelService.canAccess(sectionId, getLoggedUserId())) {
			return new GetResult<CardWrappersHolderDto>("error", "access denied", null);
		}
		
		return modelService.getSectionCardWrappers(sectionId, getLoggedUserId());
	}
	
	@RequestMapping(path = "/model/section/{sectionId}/genealogy", method = RequestMethod.GET) 
	public GetResult<ModelSectionLinkedDto> getSectionGenealogy(
			@PathVariable("sectionId") String sectionIdStr) {
		
		UUID sectionId = UUID.fromString(sectionIdStr);
		
		if (!modelService.canAccess(sectionId, getLoggedUserId())) {
			return new GetResult<ModelSectionLinkedDto>("error", "access denied", null);
		}
		
		return modelService.getSectionParentGenealogy(sectionId, null);
	}
	
	@RequestMapping(path = "/model/section/{sectionId}", method = RequestMethod.DELETE) 
	public PostResult deleteSection(
			@PathVariable("sectionId") String sectionIdStr) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID sectionId = UUID.fromString(sectionIdStr);
		
		if (!modelService.canEdit(sectionId, getLoggedUser().getC1Id())) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.deleteSection(UUID.fromString(sectionIdStr), getLoggedUser().getC1Id());
	}
	
	
	@RequestMapping(path = "/model/section/{sectionId}/cardWrapper", method = RequestMethod.POST)
	public PostResult createCardWrapper(
			@PathVariable("sectionId") String sectionIdStr,
			@RequestBody ModelCardDto cardDto,
			@RequestParam(name="onCardWrapperId", defaultValue="") String onCardWrapperIdStr,
			@RequestParam(name = "isBefore", defaultValue="false") Boolean isBefore) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID sectionId = UUID.fromString(sectionIdStr);
		
		if (!modelService.canEdit(sectionId, getLoggedUser().getC1Id())) {
			return new PostResult("error", "not authorized", "");
		}
		
		UUID onCardWrapperId = onCardWrapperIdStr.equals("") ? null : UUID.fromString(onCardWrapperIdStr);
		
		return modelService.createCardWrapper(
				cardDto, 
				sectionId, 
				getLoggedUser().getC1Id(), 
				onCardWrapperId, 
				isBefore);
	}
	
	@RequestMapping(path = "/model/cardWrapper/{cardWrapperId}", method = RequestMethod.GET) 
	public GetResult<ModelCardWrapperDto> getCardWrapper(
			@PathVariable("cardWrapperId") String cardWrapperIdStr,
			@RequestParam(name="inSectionId", defaultValue="") String inSectionIdStr) {
		
		UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);

		// ####
		// List<ModelSection> initiativeId = modelService.getCardWrapperInitiative(cardWrapperId).getId();
		
		// if (!modelService.canAccess(initiativeId, getLoggedUserId())) {
		// 	return new GetResult<ModelCardWrapperDto>("error", "access denied", null);
		// }
		
		UUID inSectionId = inSectionIdStr.equals("") ? null : UUID.fromString(inSectionIdStr);
		
		return modelService.getCardWrapperAddition(cardWrapperId, getLoggedUserId(), inSectionId);
	}
	

	//#####
	// @RequestMapping(path = "/model/cardWrapper/{cardWrapperId}", method = RequestMethod.PUT) 
	// public PostResult editCardWrapper(
	// 		@PathVariable("cardWrapperId") String cardWrapperIdStr,
	// 		@RequestBody ModelCardDto cardDto,
	// 		@RequestParam(name="inSectionId", defaultValue="") String inSectionIdStr) {
		
	// 	if (getLoggedUser() == null) {
	// 		return new PostResult("error", "endpoint enabled users only", null);
	// 	}
		
	// 	UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);
	// 	// ####
	// 	// UUID initiativeId = modelService.getCardWrapperInitiative(cardWrapperId).getId();
		
	// 	// if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
	// 	// 	return new PostResult("error", "not authorized", "");
	// 	// }
		
	// 	UUID inSectionId = inSectionIdStr.equals("") ? null : UUID.fromString(inSectionIdStr);
		
	// 	return modelService.editCardWrapper(initiativeId, inSectionId, cardWrapperId, cardDto, getLoggedUser().getC1Id());
	// }
	
	@RequestMapping(path = "/model/section/{sectionId}/cardWrapper/{cardWrapperId}/makeShared", method = RequestMethod.PUT) 
	public PostResult makeCardWrapperShared(
			@PathVariable("sectionId") String sectionIdStr,
			@PathVariable("cardWrapperId") String cardWrapperIdStr) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);
		UUID sectionId = UUID.fromString(sectionIdStr);

		// ####
		// UUID initiativeId = modelService.getCardWrapperInitiative(cardWrapperId).getId();
		
		// if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
		// 	return new PostResult("error", "not authorized", "");
		// }
		
		if (!modelService.getModelCardWrapperCreatorId(cardWrapperId).equals(getLoggedUserId())) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.makeCardWrapperShared(cardWrapperId, sectionId, getLoggedUserId());
	}
	
	@RequestMapping(path = "/model/section/{sectionId}/cardWrapper/{cardWrapperId}/makeCommon", method = RequestMethod.PUT) 
	public PostResult makeCardWrapperCommon(
			@PathVariable("sectionId") String sectionIdStr,
			@PathVariable("cardWrapperId") String cardWrapperIdStr) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);
		UUID sectionId = UUID.fromString(sectionIdStr);

		// ####
		// ModelCardWrapperAdditionRepositoryIf modelCardWrapper;
		
		// modelCardWrapper.findParentSections(cardWrapperId)
		// #### 
		// UUID initiativeId = modelService.getCardWrapperInitiative(cardWrapperId).getId();
		
		// if (!modelService.canEdit(initiativeId, getLoggedUser().getC1Id())) {
		// 	return new PostResult("error", "not authorized", "");
		// }
		
		if (!modelService.getModelCardWrapperCreatorId(cardWrapperId).equals(getLoggedUserId())) {
			return new PostResult("error", "not authorized, you are not the card author", "");
		}
		
		return modelService.makeCardWrapperCommon(cardWrapperId, sectionId, getLoggedUserId());
	}
	
	@RequestMapping(path = "/model/section/{sectionId}/cardWrappers/search", method = RequestMethod.GET) 
	public GetResult<Page<ModelCardWrapperDto>> searchCardWrapper(
			@PathVariable("sectionId") String sectionIdStr,
			@RequestParam(name="query", defaultValue="") String query,
			@RequestParam(name="page", defaultValue="0") Integer page,
			@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
			@RequestParam(name="levels", defaultValue="1") Integer levels,
			@RequestParam(name="sortBy", defaultValue="CREATION_DATE_DESC") String sortBy,
			@RequestParam(name="inInitiativeEcosystem", defaultValue="false") Boolean inInitiativeEcosystem) {
		
		UUID sectionId = UUID.fromString(sectionIdStr);
		
		
		if (!modelService.canAccess(sectionId, getLoggedUserId())) {
			return new GetResult<Page<ModelCardWrapperDto>>("error", "access denied", null);
		}
		
		return modelService.searchCardWrapper(sectionId, query, page, pageSize, sortBy, levels, getLoggedUserId(), inInitiativeEcosystem);
	}
	
	@RequestMapping(path = "/model/cardWrapper/{cardWrapperId}", method = RequestMethod.DELETE) 
	public PostResult deleteCardWrapper(
			@PathVariable("cardWrapperId") String cardWrapperIdStr) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);
		// ####
		// UUID initiativeId = modelService.getCardWrapperInitiative(cardWrapperId).getId();
		
		// if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
		// 	return new PostResult("error", "not authorized", "");
		// }
		
		return modelService.deleteCardWrapper(cardWrapperId, getLoggedUser().getC1Id());
	}
		
	@RequestMapping(path = "/model/section/search", method = RequestMethod.GET) 
	public GetResult<Page<ModelSectionDto>> searchSection(
			@RequestParam("sectionId") String sectionIdStr,
			@RequestParam("query") String query,
			@RequestParam("page") Integer page,
			@RequestParam("size") Integer size) {
	
		UUID sectionId = UUID.fromString(sectionIdStr);
		if (!modelService.canAccess(sectionId, getLoggedUserId())) {
			return new GetResult<Page<ModelSectionDto>>("error", "access denied", null);
		}
		
		return modelService.searchSection(query, new PageRequest(page, size), sectionId, getLoggedUserId());
	}
	
	@RequestMapping(path = "/activity/model/section/{sectionId}", method = RequestMethod.GET)
	public GetResult<Page<ActivityDto>> getActivityUnderSection(
			@PathVariable("sectionId") String sectionIdStr,
			@RequestParam("page") Integer page,
			@RequestParam("size") Integer size, 
			@RequestParam(name="levels", defaultValue="1") Integer levels,
			@RequestParam(name="onlyMessages", defaultValue="false") Boolean onlyMessages) {
		
		UUID sectionId = UUID.fromString(sectionIdStr);
		
	
		
		if (!modelService.canAccess(sectionId, getLoggedUserId())) {
			return new GetResult<Page<ActivityDto>>("error", "access denied", null);
		}
		
		return modelService.getActivityResultUnderSection(sectionId, new PageRequest(page, size), onlyMessages, levels);
	}
	
	@RequestMapping(path = "/model/card/{cardWrapperId}/countMessages", method = RequestMethod.GET)
	public GetResult<Long> countMessagesUnderCard(
			@PathVariable("cardWrapperId") String cardWrapperIdStr, 
			@RequestParam(name="onlyMessages", defaultValue="false") Boolean onlyMessages) {
		
		UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);
		
		//## workout later
		// if (!modelService.canAccess(initiativeId, getLoggedUserId())) {
		// 	return new GetResult<Long>("error", "access denied", null);
		// }
		
		return modelService.countMessagesUnderCard(cardWrapperId, onlyMessages);
	}
	
	@RequestMapping(path = "/activity/model/card/{cardWrapperId}", method = RequestMethod.GET)
	public GetResult<Page<ActivityDto>> getActivityUnderCard(
			@PathVariable("cardWrapperId") String cardWrapperIdStr,
			@RequestParam("page") Integer page,
			@RequestParam("size") Integer size, 
			@RequestParam(name="onlyMessages", defaultValue="false") Boolean onlyMessages) {
		
		UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);
		
		//####
		// Initiative initiative = modelService.getCardWrapperInitiative(cardWrapperId);
		
		// if (!initiativeService.canAccess(initiative.getId(), getLoggedUserId())) {
		// 	return new GetResult<Page<ActivityDto>>("error", "access denied", null);
		// }
		
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
		// ####
		// Initiative initiative = modelService.getCardWrapperInitiative(cardWrapperId);
		
		// if (!initiativeService.isMemberOfEcosystem(initiative.getId(), getLoggedUserId())) {
		// 	return new PostResult("error", "not authorized", "");
		// }
				
		return modelService.setLikeToCard(cardWrapperId, getLoggedUserId(), likeStatus);
	}
	
	@RequestMapping(path = "/model/card/{cardWrapperId}/countLikes", method = RequestMethod.GET)
	public GetResult<Integer> countCardLikes(
			@PathVariable("cardWrapperId") String cardWrapperIdStr, 
			@RequestParam(name="onlyMessages", defaultValue="false") Boolean onlyMessages) {
		
		UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);

		//#####
		// UUID initiativeId = modelService.getCardWrapperInitiative(cardWrapperId).getId();
		
		// if (!initiativeService.canAccess(initiativeId, getLoggedUserId())) {
		// 	return new GetResult<Integer>("error", "access denied", null);
		// }
		
		return modelService.countCardLikes(cardWrapperId);
	}
}
