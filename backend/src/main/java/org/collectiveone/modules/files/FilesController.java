package org.collectiveone.modules.files;

import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/1")
public class FilesController {

	@Autowired
	private FileService fileService;
	
	@Autowired
	private AppUserService appUserService;
    
    @RequestMapping(value = "/secured/upload/profileImage", method = RequestMethod.POST)
    public @ResponseBody String uploadProfileImage(@RequestParam("file") MultipartFile file) {
    	fileService.uploadImageProfile(getLoggedUser().getC1Id(), file);
    	return "success";
    }
    
    private AppUser getLoggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		AppUser user = appUserService.getFromAuth0Id(auth.getName());
		return user;
	}
}
