package org.collectiveone.modules.c1.data;

import java.util.List;

import org.collectiveone.common.BaseController;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.c1.data.dtos.DataDto;
import org.collectiveone.modules.c1.data.dtos.DraftDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/1")
public class DataController extends BaseController { 
	
	@Autowired
	private DataService dataService;
	
	
	@RequestMapping(path = "/data", method = RequestMethod.POST)
	public PostResult createContext(
			@RequestBody List<DataDto> dataDtos) throws Exception {
		
		List<String> dataLinks = dataService.createDatas(dataDtos); 
		
		return new PostResult(
				"success", 
				"contex created",
				dataLinks);
	}
	
	@RequestMapping(path = "/data/{dataId}", method = RequestMethod.GET)
	public GetResult<DataDto> getData(
			@PathVariable("dataId") String dataId) throws Exception {
		
		return new GetResult<DataDto>(
				"success", 
				"contex created", 
				dataService.getDataDto(dataId));
	}	
	
	@RequestMapping(path = "/draft", method = RequestMethod.PUT)
	public PostResult createDraft(
			@RequestBody List<DraftDto> draftDtos) throws Exception {
		
		dataService.createDrafts(draftDtos, getLoggedUserId()); 
		
		return new PostResult(
				"success", 
				"contex created",
				null);
	}
	
	@RequestMapping(path = "/draft", method = RequestMethod.GET)
	public GetResult<DataDto> getDraft(
			@RequestParam("elementId") String elementId) throws Exception {
		
		return new GetResult<DataDto>(
				"success", 
				"contex created", 
				dataService.getDraftDto(elementId, getLoggedUserId()));
	}	
}	
