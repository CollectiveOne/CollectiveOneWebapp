package org.collectiveone.modules.tokens;

import java.util.List;
import java.util.UUID;

import org.collectiveone.common.BaseController;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.model.ModelSection;
import org.collectiveone.modules.model.ModelService;
import org.collectiveone.modules.tokens.dto.AssetsDto;
import org.collectiveone.modules.tokens.dto.TokenMintDto;
import org.collectiveone.modules.tokens.dto.TransferDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/1")
public class TokenController extends BaseController { 
	

	@Autowired
	private TokenTransferService tokenTransferService;
	
	@Autowired
	private TokenService tokenService;

	
	@Autowired
	private ModelService modelService;
	
	
	@RequestMapping(path = "/token/{id}", method = RequestMethod.GET)
	public GetResult<AssetsDto> getToken(
			@PathVariable("id") String id, 
			@RequestParam(defaultValue = "false") Boolean includeSubModelSections,
			@RequestParam(defaultValue = "") String modelSectionIdStr) {
		
		UUID tokenTypeId = UUID.fromString(id);
		ModelSection modelSection = modelService.findByTokenType_Id(tokenTypeId);
		
		if (!modelService.canAccess(modelSection.getId(), getLoggedUserId())) {
			return new GetResult<AssetsDto>("error", "access denied", null);
		}
		
		AssetsDto assetDto = tokenService.getTokenDto(UUID.fromString(id));
		
		if (includeSubModelSections) {
			UUID modelSectionContextId = UUID.fromString(modelSectionIdStr);
			
			if (!modelService.canAccess(modelSectionContextId, getLoggedUserId())) {
				return new GetResult<AssetsDto>("error", "access denied", null);
			}
			
			assetDto = tokenTransferService.getTokenDistribution(tokenTypeId, modelSectionContextId);
		}
		
		return new GetResult<AssetsDto>("success", "initiative retrieved", assetDto);
	}
	
	@RequestMapping(path = "/token/{tokenId}/mint", method = RequestMethod.PUT) 
	public PostResult mintTokens(
			@PathVariable("tokenId") String tokenIdStr, 
			@RequestBody TokenMintDto mintDto) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID tokenId = UUID.fromString(tokenIdStr);
		
		if (!modelService.canMintTokens(tokenId, getLoggedUser().getC1Id())) {
			return new PostResult("error", "not authorized", "");
		}
		
		ModelSection modelSection = modelService.findByTokenType_Id(tokenId);
		String result = tokenTransferService.mintToInitiative(tokenId, modelSection.getId(), getLoggedUser().getC1Id(), mintDto);
		
		if(result.equals("success")) {
			return new PostResult("success", "tokens minted", tokenId.toString());
		} else {
			return new PostResult("error", "error while minting tokens", "");
		}
	}
	
	@RequestMapping(path = "/ctx/{modelSectionId}/transfersToModelSections", method = RequestMethod.GET)
	public GetResult<List<TransferDto>> getTransferFromModelSections(
			@PathVariable("modelSectionId") String modelSectionIdStr,
			@RequestParam("page") Integer page,
			@RequestParam("size") Integer size,
			@RequestParam(name="levels", defaultValue="1") Integer levels,
			@RequestParam("sortDirection") String sortDirection,
			@RequestParam("sortProperty") String sortProperty ) {
		 
		UUID modelSectionsId = UUID.fromString(modelSectionIdStr);	
		
		if (!modelService.canAccess(modelSectionsId, getLoggedUserId())) {
			return new GetResult<List<TransferDto>>("error", "access denied", null);
		}
		
		Sort sort = new Sort(Sort.Direction.valueOf(sortDirection), sortProperty);
		
		return tokenTransferService.getTransfersFromModelSections(modelSectionsId, levels, new PageRequest(page, size, sort));
	}
	

	// //#### transfersFromSubModelSections ??
	// @RequestMapping(path = "/ctx/{modelSectionId}/transfersFromSubModelSections", method = RequestMethod.GET)
	// public GetResult<List<TransferDto>> getTransferFromSubinitiatives(
	// 		@PathVariable("modelSectionId") String modelSectionIdStr,
	// 		@RequestParam("page") Integer page,
	// 		@RequestParam("size") Integer size,
	// 		@RequestParam("sortDirection") String sortDirection,
	// 		@RequestParam("sortProperty") String sortProperty ) {
		 
	// 	UUID modelSectionId = UUID.fromString(modelSectionIdStr);	
		
	// 	if (!modelService.canAccess(modelSectionId, getLoggedUserId())) {
	// 		return new GetResult<List<TransferDto>>("error", "access denied", null);
	// 	}
		
	// 	Sort sort = new Sort(Sort.Direction.valueOf(sortDirection), sortProperty);
	// 	//inside function get all subsection using getSectionNode
	// 	return tokenTransferService.getTransfersFromSubModelSections(modelSectionId, new PageRequest(page, size, sort));
	// }
	
	@RequestMapping(path = "/ctx/{modelSectionId}/transferToModelSection", method = RequestMethod.POST)
	public PostResult makeTransferToModelSection(
			@PathVariable("modelSectionId") String modelSectionIdStr,
			@RequestBody TransferDto transferDto) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID modelSectionId = UUID.fromString(modelSectionIdStr);
		
		if (modelService.canTransferToken(modelSectionId, getLoggedUser().getC1Id())) {
			return new PostResult("error", "not authorized", "");
		}
		
		return tokenTransferService.transferFromModelSectionToModelSection(modelSectionId, transferDto, getLoggedUser().getC1Id());
	}
	
}
