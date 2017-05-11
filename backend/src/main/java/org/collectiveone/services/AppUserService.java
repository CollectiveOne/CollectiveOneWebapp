package org.collectiveone.services;

import javax.transaction.Transactional;

import org.collectiveone.model.AppUser;
import org.collectiveone.repositories.AppUserRepositoryIf;
import org.collectiveone.web.dto.AppUserDto;
import org.collectiveone.web.dto.GetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;

@Service
public class AppUserService {
	
	@Autowired
	AppUserRepositoryIf appUserRepository;
	
	@Autowired
	ManagementAPI mgmt;

	@Transactional
	public AppUser get(String auth0Id) {
		
		AppUser appUser = appUserRepository.findByAuth0Id(auth0Id);
		
		if(appUser == null) {
			/* the first time a user is requested, it 
			 * is added to the local DB */
			appUser = addUserToLocalDB(auth0Id);
		}
    	
    	return appUser;
	}
	
	@Transactional
	public GetResult<AppUserDto> getDto(String auth0Id) {
		return new GetResult<AppUserDto>("success", "user profile retrieved", get(auth0Id).toDto());
	}
	
	@Transactional
	private AppUser addUserToLocalDB(String auth0Id) {
		/* retrieve from Auth0 */
		AppUser appUser = null;
		User auth0User = null;
		try {
			auth0User = mgmt.users().get(auth0Id, null).execute();
			
			appUser = new AppUser();
			
			appUser.setAuth0Id(auth0User.getId());
			appUser.setNickname(auth0User.getNickname());
			appUser.setEmail(auth0User.getEmail());
			appUser.setPictureUrl(auth0User.getPicture());
			
			appUser = appUserRepository.save(appUser);
			
		} catch (APIException exception) {
		    System.out.println(exception.getMessage());
		} catch (Auth0Exception exception) {
			System.out.println(exception.getMessage());
		}
		
		return appUser;
	} 
	
}
