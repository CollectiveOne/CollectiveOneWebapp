package org.collectiveone.services;

import java.sql.Timestamp;

import org.collectiveone.model.AuthorizedEmail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorizedEmailServiceImp extends BaseService {
	
	@Transactional
	public void add(String email, Long referralId, String token) {
		AuthorizedEmail authorizedEmail = new AuthorizedEmail();
		
		authorizedEmail.setEmail(email);
		authorizedEmail.setReferral(referralId);
		authorizedEmail.setToken(token);
		authorizedEmail.setDateRequested(new Timestamp(System.currentTimeMillis()));
		authorizedEmail.setAuthorized(false);
		
		authorizedEmailRepository.save(authorizedEmail);
	}

	@Transactional
	public boolean validate(String email, String token) {
		AuthorizedEmail authorizedEmail = authorizedEmailRepository.findByEmailAndToken(email, token);
		if(authorizedEmail != null) {
			authorizedEmail.setAuthorized(true);
			authorizedEmailRepository.save(authorizedEmail);
			return true;
		} else {
			return false;
		}
	}
	

}
