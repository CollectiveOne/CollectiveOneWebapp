package org.collectiveone.modules.files;

import java.io.IOException;
import java.util.UUID;

import org.collectiveone.common.BaseController;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.model.ModelSection;
import org.collectiveone.modules.model.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/1")
public class FilesController extends BaseController {

	@Autowired
	private FileService fileService;
	
	@Autowired
	private ModelService modelService;
	

	
    @RequestMapping(value = "/upload/profileImage", method = RequestMethod.POST)
    public @ResponseBody String uploadProfileImage(@RequestParam("file") MultipartFile file) {
    	
    	if (getLoggedUser() == null) {
			return "error: endpoint enabled users only";
		}
    	
    	fileService.uploadImageProfile(getLoggedUser().getC1Id(), file);
    	return "success";
    }
    
    @RequestMapping(value = "/upload/cardImage", method = RequestMethod.POST)
    public @ResponseBody PostResult uploadCardImageBeforeCreation(
    		@RequestParam("file") MultipartFile file) throws IOException {
    	
    	/* just upload the file under a user key*/
    	
    	if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", "");
		}
    	
    	return fileService.uploadCardImageBeforeCreation(getLoggedUserId(), file);
    }
    
    @RequestMapping(value = "/upload/cardImage/{cardWrapperId}", method = RequestMethod.POST)
    public @ResponseBody PostResult uploadCardImage(
    		@PathVariable("cardWrapperId") String cardWrapperIdStr,
    		@RequestParam("file") MultipartFile file) throws IOException {
    	
    	if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", "");
		}
    	
		UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);
		// ####
    	//Initiative initiative = modelService.getCardWrapperInitiative(cardWrapperId);
		
		// ####
    	// if (governanceService.canEditModel(initiative.getId(), getLoggedUserId()) == DecisionVerdict.DENIED) {
    	// 	return new PostResult("error", "not authorized", "");
		// }
    	
    	
    	return fileService.uploadCardImage(getLoggedUserId(), cardWrapperId, file);
    }
    
    @RequestMapping(value = "/upload/modelSectionImage/{modelSectionId}", method = RequestMethod.POST)
    public @ResponseBody PostResult uploadInitiativeImage(
    		@PathVariable("modelSectionId") String modelSectionIdStr,
    		@RequestParam("file") MultipartFile file) throws IOException {
    	
    	if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", "");
		}
		
		// ####
    	UUID initiativeId = UUID.fromString(modelSectionIdStr);
    	// if (governanceService.canEdit(initiativeId, getLoggedUserId()) == DecisionVerdict.DENIED) {
    	// 	return new PostResult("error", "not authorized", "");
		// }
    	
    	
    	return fileService.uploadMModelSectionImage(getLoggedUserId(), initiativeId, file);
    }
    
    @RequestMapping(value = "/files/{fileId}", method = RequestMethod.GET)
    public @ResponseBody GetResult<FileStoredDto> getFileData (
    		@PathVariable("fileId") String fileIdStr) {
    	
    	UUID fileId = UUID.fromString(fileIdStr);
    	ModelSection modelSection = fileService.getFileModelSection(fileId);
    	
    	if (modelSection != null) {
    		if (!modelService.canAccess(modelSection.getId(), getLoggedUserId())) {
    			return new GetResult<FileStoredDto>("error", "access denied", null);
    		}
    	}
    	
    	return new GetResult<FileStoredDto>("success", "file retrieved", fileService.getFileData(fileId));
    }
    
}
