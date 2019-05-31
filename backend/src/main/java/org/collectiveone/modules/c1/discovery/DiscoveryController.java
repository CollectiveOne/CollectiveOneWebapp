package org.collectiveone.modules.c1.discovery;

import org.collectiveone.common.BaseController;
import org.collectiveone.common.dto.GetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/1")
public class DiscoveryController extends BaseController { 
	
	@Autowired
	private DiscoveryService discoveryService;
	
	@RequestMapping(path = "/discovery/you", method = RequestMethod.GET)
	public GetResult<String> getMyUSL() throws Exception {
		
		return new GetResult<String>(
				"success", 
				"view retrieved", 
				discoveryService.getOrigin());
	}	
	
}	
