package org.collectiveone.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.model.basic.AppUser;
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
	public AppUser getOrCreateFromAuth0Id(String auth0Id) {
		
		AppUser appUser = appUserRepository.findByAuth0Id(auth0Id);
		
		if(appUser == null) {
			/* the first time a user is requested, it 
			 * is added to the local DB */
			appUser = addUserToLocalDB(auth0Id);
		}
    	
    	return appUser;
	}
	
	@Transactional
	public AppUser getFromAuth0Id(String auth0Id) {
    	return appUserRepository.findByAuth0Id(auth0Id);
	}
	
	@Transactional
	public AppUser getFromC1Id(UUID c1Id) {
		return appUserRepository.findByC1Id(c1Id);
	}
	
	@Transactional
	public GetResult<AppUserDto> getDto(UUID c1Id) {
		return new GetResult<AppUserDto>("success", "user profile retrieved", getFromC1Id(c1Id).toDto());
	}

	@Transactional
	public GetResult<List<AppUserDto>> searchBy(String q) {
		List<AppUser> appUsers = appUserRepository.searchByNickname(q);
		
		List<AppUserDto> appUserDtos = new ArrayList<AppUserDto>();
		
		for(AppUser appUser : appUsers) {
			appUserDtos.add(appUser.toDto());
		}
		
		return new GetResult<List<AppUserDto>>("succes", "initiatives returned", appUserDtos);
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
