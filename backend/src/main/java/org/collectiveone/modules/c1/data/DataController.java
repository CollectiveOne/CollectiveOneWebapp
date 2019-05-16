package org.collectiveone.modules.c1.data;

import org.collectiveone.common.BaseController;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.c1.data.dtos.DataDto;
import org.collectiveone.modules.uprcl.UprtclService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/1")
public class DataController extends BaseController { 
	
	@Autowired
	private DataService dataService;
	
	@Autowired
	private UprtclService uprtclService;
	
	
	@RequestMapping(path = "/data", method = RequestMethod.POST)
	public PostResult createContext(
			@RequestBody DataDto dataDto) throws Exception {
		
		String dataId = dataService.createData(dataDto).getId(); 
		
		return new PostResult(
				"success", 
				"contex created",
				uprtclService.getLocalLink(dataId).toString());
	}
	
	@RequestMapping(path = "/data/{dataId}", method = RequestMethod.GET)
	public GetResult<DataDto> getData(
			@PathVariable("contextId") String contextId) throws Exception {
		
		return new GetResult<DataDto>(
				"success", 
				"contex created", 
				dataService.getDataDto(contextId));
	}	
}	
