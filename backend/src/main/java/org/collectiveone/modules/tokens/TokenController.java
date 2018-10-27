package org.collectiveone.modules.tokens;

import java.util.List;
import java.util.UUID;

import org.collectiveone.common.BaseController;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.governance.DecisionVerdict;
import org.collectiveone.modules.governance.GovernanceService;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.InitiativeService;
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
	private GovernanceService governanceService;
	
	@Autowired
	private InitiativeService initiativeService;
	
	
	@RequestMapping(path = "/token/{id}", method = RequestMethod.GET)
	public GetResult<AssetsDto> getToken(
			@PathVariable("id") String id, 
			@RequestParam(defaultValue = "false") Boolean includeSubinitiatives,
			@RequestParam(defaultValue = "") String initiativeIdStr) {
		
		UUID tokenTypeId = UUID.fromString(id);
		Initiative initiative = initiativeService.findByTokenType_Id(tokenTypeId);
		
		if (!initiativeService.canAccess(initiative.getId(), getLoggedUserId())) {
			return new GetResult<AssetsDto>("error", "access denied", null);
		}
		
		AssetsDto assetDto = tokenService.getTokenDto(UUID.fromString(id));
		
		if (includeSubinitiatives) {
			UUID initiativeContextId = UUID.fromString(initiativeIdStr);
			
			if (!initiativeService.canAccess(initiativeContextId, getLoggedUserId())) {
				return new GetResult<AssetsDto>("error", "access denied", null);
			}
			
			assetDto = tokenTransferService.getTokenDistribution(tokenTypeId, initiativeContextId);
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
		
		if (governanceService.canMintTokens(tokenId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		Initiative initiative = initiativeService.findByTokenType_Id(tokenId);
		String result = tokenTransferService.mintToInitiative(tokenId, initiative.getId(), getLoggedUser().getC1Id(), mintDto);
		
		if(result.equals("success")) {
			return new PostResult("success", "tokens minted", tokenId.toString());
		} else {
			return new PostResult("error", "error while minting tokens", "");
		}
	}
	
	@RequestMapping(path = "/token/{tokenId}/burn", method = RequestMethod.PUT) 
	public PostResult burnTokens(
			@PathVariable("tokenId") String tokenIdStr, 
			@RequestBody TokenMintDto mintDto) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID tokenId = UUID.fromString(tokenIdStr);
		
		if (governanceService.canMintTokens(tokenId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		Initiative initiative = initiativeService.findByTokenType_Id(tokenId);
		String result = tokenTransferService.burnOfInitiative(tokenId, initiative.getId(), getLoggedUserId(), mintDto);
		
		if(result.equals("success")) {
			return new PostResult("success", "tokens minted", tokenId.toString());
		} else {
			return new PostResult("error", "error while minting tokens", "");
		}
	}
	
	@RequestMapping(path = "/token/{tokenId}", method = RequestMethod.DELETE) 
	public PostResult deleteToken(
			@PathVariable("tokenId") UUID tokenId) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		if (governanceService.canMintTokens(tokenId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		String result = tokenService.delete(tokenId);
		
		if(result.equals("success")) {
			return new PostResult("success", "token deleted", tokenId.toString());
		} else {
			return new PostResult("error", "error while deleting token", "");
		}
	}
	
	@RequestMapping(path = "/token/{tokenId}/restore", method = RequestMethod.PUT) 
	public PostResult restoreToken(
			@PathVariable("tokenId") UUID tokenId) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		if (governanceService.canMintTokens(tokenId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		String result = tokenService.restore(tokenId);
		
		if(result.equals("success")) {
			return new PostResult("success", "token deleted", tokenId.toString());
		} else {
			return new PostResult("error", "error while deleting token", "");
		}
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/transfersToInitiatives", method = RequestMethod.GET)
	public GetResult<List<TransferDto>> getTransferFromInitiative(
			@PathVariable("initiativeId") String initiativeIdStr,
			@RequestParam("page") Integer page,
			@RequestParam("size") Integer size,
			@RequestParam("sortDirection") String sortDirection,
			@RequestParam("sortProperty") String sortProperty ) {
		 
		UUID initiativeId = UUID.fromString(initiativeIdStr);	
		
		if (!initiativeService.canAccess(initiativeId, getLoggedUserId())) {
			return new GetResult<List<TransferDto>>("error", "access denied", null);
		}
		
		Sort sort = new Sort(Sort.Direction.valueOf(sortDirection), sortProperty);
		
		return tokenTransferService.getTransfersFromInitiative(initiativeId, new PageRequest(page, size, sort));
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/transfersFromSubinitiatives", method = RequestMethod.GET)
	public GetResult<List<TransferDto>> getTransferFromSubinitiative(
			@PathVariable("initiativeId") String initiativeIdStr,
			@RequestParam("page") Integer page,
			@RequestParam("size") Integer size,
			@RequestParam("sortDirection") String sortDirection,
			@RequestParam("sortProperty") String sortProperty ) {
		 
		UUID initiativeId = UUID.fromString(initiativeIdStr);	
		
		if (!initiativeService.canAccess(initiativeId, getLoggedUserId())) {
			return new GetResult<List<TransferDto>>("error", "access denied", null);
		}
		
		Sort sort = new Sort(Sort.Direction.valueOf(sortDirection), sortProperty);
		
		return tokenTransferService.getTransfersFromSubinitiatives(initiativeId, new PageRequest(page, size, sort));
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/transferToInitiative", method = RequestMethod.POST)
	public PostResult makeTransferToInitiative(
			@PathVariable("initiativeId") String initiativeIdStr,
			@RequestBody TransferDto transferDto) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (governanceService.canTransferTokens(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return tokenTransferService.transferFromInitiativeToInitiative(initiativeId, transferDto, getLoggedUser().getC1Id());
	}
	
}
