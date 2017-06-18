package org.collectiveone.modules.tokens.controllers;

import java.util.UUID;

import org.collectiveone.common.dto.GetResult;
import org.collectiveone.modules.tokens.dto.AssetsDto;
import org.collectiveone.modules.tokens.services.TokenTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/1")
public class TokenController { 
	
	@Autowired
	private TokenTransferService tokenTransferService;
	
	
	
	@RequestMapping(path = "/secured/token/{id}", method = RequestMethod.GET)
	public GetResult<AssetsDto> getToken(
			@PathVariable("id") String id, 
			@RequestParam(defaultValue = "includeSubinitiatives") String level,
			@RequestParam(defaultValue = "") String initiativeId) {
		
		AssetsDto assetDto = null;
		switch (level) {
		case "light": 
			assetDto = null; // TODO: return token data in general (no need for the moment)
			break;
		
		case "includeSubinitiatives": 
			assetDto = tokenTransferService.getTokenDistribution(UUID.fromString(id), UUID.fromString(initiativeId));
			break;
			
		}
		
		return new GetResult<AssetsDto>("success", "initiative retrieved", assetDto);
	}
	
}
