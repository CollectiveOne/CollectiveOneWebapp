package org.collectiveone.services;

import java.sql.Timestamp;

import org.collectiveone.model.AuthorizedEmail;
import org.collectiveone.repositories.AuthorizedEmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class AuthorizedEmailServiceImp {
	
	@Autowired
	protected AuthorizedEmailRepository authorizedEmailRepository;
	
	@Transactional
	public void authorizedEmailAdd(String email, Long referralId, String token) {
		AuthorizedEmail authorizedEmail = new AuthorizedEmail();
		
		authorizedEmail.setEmail(email);
		authorizedEmail.setReferral(referralId);
		authorizedEmail.setToken(token);
		authorizedEmail.setDateRequested(new Timestamp(System.currentTimeMillis()));
		authorizedEmail.setAuthorized(false);
		
		authorizedEmailRepository.save(authorizedEmail);
	}

	@Transactional
	public boolean authorizedEmailValidate(String email, String token) {
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
