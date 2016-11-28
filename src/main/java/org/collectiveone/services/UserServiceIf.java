package org.collectiveone.services;

import org.collectiveone.model.User;
import org.collectiveone.web.dto.PasswordDto;
import org.collectiveone.web.dto.UserNewDto;
import org.collectiveone.web.error.UserAlreadyExistException;

public interface UserServiceIf {

	User findByUsername(String username);
	
	User findByEmail(String email);
	
	User registerNewUserAccount(UserNewDto accountDto) throws UserAlreadyExistException;
	
	User save(User user);
	
	void createVerificationTokenForUser(User user, String token);
	
	void createPasswordRecoveryTokenForUser(User user, String token);

	String validateVerificationToken(String token);

	String validatePasswordRecoveryToken(long id, String token);

	void changeUserPassword(User user, PasswordDto passwordDto);	
	
	
}
