package org.collectiveone.modules.users;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;

@Service
public class AppUserService {
	
	@Autowired
	private AppUserRepositoryIf appUserRepository;
	
	@Autowired
	private AppUserProfileRepositoryIf appUserProfileRepository;
	
	@Autowired
	private ManagementAPI mgmt;

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
	public GetResult<AppUserDto> getUserLight(UUID c1Id) {
		return new GetResult<AppUserDto>("success", "user profile retrieved", getFromC1Id(c1Id).toDtoLight());
	}
	
	@Transactional
	public GetResult<AppUserDto> getUserFull(UUID c1Id) {
		return new GetResult<AppUserDto>("success", "user profile retrieved", getFromC1Id(c1Id).toDtoFull());
	}
	
	@Transactional
	public PostResult editUserProfile(UUID c1Id, AppUserDto userDto) {
		AppUserProfile profile = appUserProfileRepository.findByUser_C1Id(c1Id);
		
		if (profile.getUsername() != null) {
			if (!profile.getUsername().equals(userDto.getUsername())) {
				/* check if username exist */
				AppUserProfile profileOfUsername = appUserProfileRepository.findByUsername(userDto.getUsername());
				
				if (profileOfUsername != null) {
					return new PostResult("error", "username already exist", "");
				}
			}
		}
		
		profile.setNickname(userDto.getNickname());
		profile.setUsername(userDto.getUsername());
		profile.setPictureUrl(userDto.getPictureUrl());
		profile.setShortBio(userDto.getShortBio());
		profile.setTwitterHandle(userDto.getTwitterHandle());
		profile.setFacebookHandle(userDto.getFacebookHandle());
		profile.setLinkedinHandle(userDto.getLinkedinHandle());
		
		appUserProfileRepository.save(profile);
		
		return new PostResult("success", "profile edited", profile.getId().toString());
		
	}
	
	
	@Transactional
	public GetResult<Boolean> usernameExist(String username) {
		AppUserProfile profile = appUserProfileRepository.findByUsername(username);
		return new GetResult<Boolean>("success", "existance cheked", profile != null);
	}
	
	

	@Transactional
	public GetResult<List<AppUserDto>> searchBy(String q) {
		List<AppUser> appUsers = appUserRepository.findTop10ByProfile_NicknameLikeIgnoreCase("%"+q+"%");
		
		List<AppUserDto> appUserDtos = new ArrayList<AppUserDto>();
		
		for(AppUser appUser : appUsers) {
			appUserDtos.add(appUser.toDtoLight());
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
			
			/* check if this email is already registered. */
			appUser = appUserRepository.findByEmail(auth0User.getEmail());
			
			if (appUser == null) {
				// if (auth0User.isEmailVerified()) {
				 if (true) {
					/* create a new user if not */
					appUser = new AppUser();
					
					appUser.getAuth0Ids().add((auth0User.getId()));
					appUser.setEmail(auth0User.getEmail());
					appUser.setEmailNotificationsEnabled(true);
					
					AppUserProfile profile = new AppUserProfile();
					
					if (auth0User.getIdentities().get(0).getProvider().equals("auth0")) {
						profile.setNickname(auth0User.getNickname());
					} else {
						profile.setNickname(auth0User.getName());
					}
					
					profile.setUser(appUser);
					profile.setPictureUrl(auth0User.getPicture());
					profile = appUserProfileRepository.save(profile);
					
					appUser.setProfile(profile);
					
				} 
			} else {
				/* just add the auth0id to the existing user */
				appUser.getAuth0Ids().add(auth0Id); 
			}
			
			appUser = appUserRepository.save(appUser);
			
		} catch (APIException exception) {
		    System.out.println(exception.getMessage());
		} catch (Auth0Exception exception) {
			System.out.println(exception.getMessage());
		}
		
		return appUser;
	} 
	
	@Transactional
	public PostResult disableEmailNotifications(UUID userId) {
		AppUser user = appUserRepository.findByC1Id(userId);
		user.setEmailNotificationsEnabled(false);
		return new PostResult("success", "email notifications disabled", "");
	}

}
