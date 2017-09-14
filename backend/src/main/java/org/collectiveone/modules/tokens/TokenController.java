package org.collectiveone.modules.tokens;

import java.util.UUID;

import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.governance.DecisionVerdict;
import org.collectiveone.modules.governance.GovernanceService;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.InitiativeService;
import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/1")
public class TokenController { 
	

	@Autowired
	private TokenTransferService tokenTransferService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private GovernanceService governanceService;
	
	@Autowired
	private AppUserService appUserService;
	
	@Autowired
	private InitiativeService initiativeService;
	
	
	@RequestMapping(path = "/token/{id}", method = RequestMethod.GET)
	public GetResult<AssetsDto> getToken(
			@PathVariable("id") String id, 
			@RequestParam(defaultValue = "false") Boolean includeSubinitiatives,
			@RequestParam(defaultValue = "") String initiativeId) {
		
		AssetsDto assetDto = tokenService.getTokenDto(UUID.fromString(id));
		
		if (includeSubinitiatives) {
			assetDto = tokenTransferService.getTokenDistribution(UUID.fromString(id), UUID.fromString(initiativeId));
		}
		
		return new GetResult<AssetsDto>("success", "initiative retrieved", assetDto);
	}
	
	@RequestMapping(path = "/token/{tokenId}/mint", method = RequestMethod.PUT) 
	public PostResult mintTokens(
			@PathVariable("tokenId") String tokenIdStr, 
			@RequestBody TokenMintDto mintDto) {
		
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
	
	@RequestMapping(path = "/initiative/{initiativeId}/transfersToInitiatives", method = RequestMethod.GET)
	public GetResult<InitiativeTransfersDto> getTransferToInitiatives(
			@PathVariable("initiativeId") String initiativeId) {
		 
		InitiativeTransfersDto transfers = tokenTransferService.getTransfersToSubInitiatives(UUID.fromString(initiativeId));
		
		return new GetResult<InitiativeTransfersDto>("success", "transfers retrieved", transfers);
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/transferToInitiative", method = RequestMethod.POST)
	public PostResult makeTransferToInitiative(
			@PathVariable("initiativeId") String initiativeId,
			@RequestBody TransferDto transferDto) {
		 
		return tokenTransferService.transferFromInitiativeToInitiative(UUID.fromString(initiativeId), transferDto, getLoggedUser().getC1Id());
	}
	
	private AppUser getLoggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return appUserService.getFromAuth0Id(auth.getName());
	}
}
