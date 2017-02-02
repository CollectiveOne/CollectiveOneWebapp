package org.collectiveone.services;

import java.sql.Timestamp;
import java.util.Calendar;

import org.collectiveone.model.AuthorizedEmail;
import org.collectiveone.model.PasswordRecoveryToken;
import org.collectiveone.model.User;
import org.collectiveone.model.VerificationToken;
import org.collectiveone.repositories.AuthorizedEmailRepository;
import org.collectiveone.repositories.PasswordRecoveryTokenRepository;
import org.collectiveone.repositories.UserRepository;
import org.collectiveone.repositories.VerificationTokenRepository;
import org.collectiveone.web.dto.PasswordDto;
import org.collectiveone.web.dto.UserNewDto;
import org.collectiveone.web.error.PasswordNotAccepted;
import org.collectiveone.web.error.PasswordsNotEqualException;
import org.collectiveone.web.error.UserAlreadyExistException;
import org.collectiveone.web.error.UserNotAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
@Transactional
public class UserServiceIm implements UserServiceIf, UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AuthorizedEmailRepository authorizedEmailRepository;
	
	@Autowired
	RoleServiceIf roleService;
	
	@Autowired
	VerificationTokenRepository verificationTokenRepository;
	
	@Autowired
	PasswordRecoveryTokenRepository recoveryTokenRepository;
	
	@Autowired
    PasswordEncoder passwordEncoder;
	
	@Autowired
    private UserDetailsService userDetailsService;
	
	@Autowired
    private AppMailServiceHeroku mailService;
	
	@Autowired
	private Environment env;

	
    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";
	
	@Autowired
	public UserServiceIm(UserRepository userRepository){
		this.userRepository = userRepository;
	}

	@Override
	public Iterable<User> findAll() {
		return userRepository.findAll();
	}
	
	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = findByUsername(username);
		if( user == null ){
			throw new UsernameNotFoundException(username);
		}
		
		UserDetailsIm userDetails = new UserDetailsIm(user);
		userDetails.setRoleService(roleService);
		return userDetails;
	}
	
	@Override
    public User registerNewUserAccount(final UserNewDto accountDto) {
		if(accountDto.getPassword().length() < 8) {
			throw new PasswordNotAccepted("Password should have at leat 8 characters");
		}
		if(accountDto.getPassword().compareTo(accountDto.getPasswordConfirm()) != 0) {
			throw new PasswordsNotEqualException("Passwords are not equal");
		}
		if (usernameExist(accountDto.getUsername())) {
			throw new UserAlreadyExistException("There is an account with that username: " + accountDto.getUsername());
        }
		if (emailExist(accountDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email adress: " + accountDto.getEmail());
        }
		if( !emailAuthorized(accountDto.getEmail())) {
			throw new UserNotAuthorizedException("The email address "+accountDto.getEmail()+" is not authorized. Please contact us");
		}
		
		
        final User user = new User();

        user.setUsername(accountDto.getUsername());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setEmail(accountDto.getEmail());
        /* email confirmation is not necessary as it works though signup request*/
        user.setEnabled(true);
        user.setJoindate(new Timestamp(System.currentTimeMillis()));
        roleService.addRoleTo(user.getUsername(),"ROLE_USER");
        
        User userSaved = userRepository.save(user);
        
        try {
        	String subject = user.getUsername()+" user signed-up";
            String body = user.getUsername()+" user signed-up";
            
            mailService.sendMail(
            		env.getProperty("collectiveone.webapp.admin-email"),
            		subject, 
            		body);
        } catch(Exception e) {}
        
        
        return userSaved;
    }
	
	public User save(User user) {
		return userRepository.save(user);
		
	}
	
	@Override
	public boolean usernameExist(final String username) {
        return userRepository.findByUsername(username) != null;
    }
	
	@Override
	public boolean emailExist(final String email) {
        return userRepository.findByEmail(email) != null;
    }
	
	@Override
	public boolean emailAuthorized(final String email) {
        return authorizedEmailRepository.findByEmailAndAuthorized(email, true) != null;
    }
	
	public void emailAuthorize(final String email) {
		if(!emailAuthorized(email)) {
			AuthorizedEmail authEmail = new AuthorizedEmail();
			authEmail.setEmail(email);
			authEmail.setAuthorized(true);
		    authorizedEmailRepository.save(authEmail);	
		}
    }
	
	
	@Override
    public void createVerificationTokenForUser(final User user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);
    }
	
	@Override
    public void createPasswordRecoveryTokenForUser(final User user, final String token) {
        final PasswordRecoveryToken myToken = new PasswordRecoveryToken(token, user);
        recoveryTokenRepository.save(myToken);
    }
	
	@Override
    public String validateVerificationToken(String token) {
        final VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        final User user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            verificationTokenRepository.delete(verificationToken);
            return TOKEN_EXPIRED;
        }

        user.setEnabled(true);
        userRepository.save(user);
        
        return TOKEN_VALID;
    }
	
	@Override
    public void enableUser(Long id) {
		User user = userRepository.findOne(id); 
		user.setEnabled(true);
        userRepository.save(user);
	}
	
	@Override
    public String validatePasswordRecoveryToken(long id, String token) {
        final PasswordRecoveryToken passToken = recoveryTokenRepository.findByToken(token);
        if ((passToken == null) || (passToken.getUser().getId() != id)) {
            return "invalidToken";
        }

        final Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return "expired";
        }

        final User user = passToken.getUser();
        
        final Authentication auth = new UsernamePasswordAuthenticationToken(user, null, userDetailsService.loadUserByUsername(user.getUsername()).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        
        return null;
    }
	
	@Override
    public void changeUserPassword(final User user, final PasswordDto passwordDto) {
		if(passwordDto.getNewPassword().length() < 8) {
			throw new PasswordNotAccepted("Password should have at leat 8 characters");
		}
		if(!passwordDto.getNewPassword().equals(passwordDto.getNewPasswordConfirm())) {
			 throw new PasswordsNotEqualException("Passwords are not equal"); 
        }
        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        userRepository.save(user);
    }

}
