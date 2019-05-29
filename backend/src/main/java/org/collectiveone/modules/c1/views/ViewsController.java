package org.collectiveone.modules.c1.views;

import java.util.Arrays;

import org.collectiveone.common.BaseController;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.ipld.IpldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/1")
public class ViewsController extends BaseController { 
	
	@Autowired
	private ViewService viewService;
	
	
	@RequestMapping(path = "/view", method = RequestMethod.PUT)
	public PostResult setView(
			@RequestBody ViewDto viewDto) throws Exception {
		
		return new PostResult(
				"success", 
				"view created",
				Arrays.asList(viewService.setView(viewDto, getLoggedUserId())));
	}
	
	@RequestMapping(path = "/view/{elementId}", method = RequestMethod.GET)
	public GetResult<ViewDto> getView(
			@PathVariable("elementId") String elementId,
			@RequestParam("inElementId") String inElementId) throws Exception {
		
		return new GetResult<ViewDto>(
				"success", 
				"view retrieved", 
				viewService.getView(
						IpldService.encode(elementId).toBytes(), 
						inElementId != null ? IpldService.encode(inElementId).toBytes() : null,
						getLoggedUserId()));
	}	
	
}	
