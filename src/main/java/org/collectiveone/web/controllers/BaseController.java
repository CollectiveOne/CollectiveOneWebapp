package org.collectiveone.web.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.collectiveone.model.User;
import org.collectiveone.registration.OnPasswordRecoveryAsked;
import org.collectiveone.services.UserAuthServiceIf;
import org.collectiveone.web.dto.PasswordDto;
import org.collectiveone.web.dto.UserNewDto;
import org.collectiveone.web.error.PasswordNotAccepted;
import org.collectiveone.web.error.PasswordsNotEqualException;
import org.collectiveone.web.error.UserAlreadyExistException;
import org.collectiveone.web.error.UserNotAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class BaseController {
	
    @Autowired
    private UserAuthServiceIf userService;
    
    @Autowired
    private MessageSource messages;
    
    @Autowired
    ApplicationEventPublisher eventPublisher;
    
    /* actions */
    
    @RequestMapping("/")
	public String home() {
		return "home";
	}
	
	@RequestMapping("/login")
	public String login(Model model) {
		return "auth/login";
	}
	
	@RequestMapping("/myhome")
	public String myhome() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return "redirect:/views/userPageR/"+auth.getName();
	}
	
	@RequestMapping("/user/signup")
	public String signup(Model model) {
		model.addAttribute("user",new UserNewDto());
		return "auth/signup";
	}
	
	@RequestMapping("/user/signupSubmit")
	public String signupSubmit(Locale locale, @Valid @ModelAttribute("user") UserNewDto userNewDto, BindingResult result, Model model, final HttpServletRequest request) {
		if(result.hasErrors()) {
			return "auth/signup";
		} else {
			boolean error = false;
			if(userService.usernameExist(userNewDto.getUsername())) {
				result.rejectValue("username", "user.username", "username '"+userNewDto.getUsername()+"' already exist.");
				error = true;
		    } 
			if(userService.emailExist(userNewDto.getEmail())) {
				result.rejectValue("email", "user.email", "email '"+userNewDto.getEmail()+"' already registered.");
				error = true;
		    }
			if(!userService.emailAuthorized(userNewDto.getEmail())) {
				result.rejectValue("email", "user.email", "email '"+userNewDto.getEmail()+"' not authorized. Please contact us.");
				error = true;
		    }
			if(!userNewDto.getPassword().equals(userNewDto.getPasswordConfirm())) {
				result.rejectValue("passwordConfirm", "user.passwordConfirm", "passwords do not match");
				error = true;
			}
			
			if(!error) {
				userService.registerNewUserAccount(userNewDto);
 				/* Email confirmation not necessary as now signup works on request.
				final User registered = userService.registerNewUserAccount(userNewDto);
				eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
				*/
				model.addAttribute("message", messages.getMessage("message.signupSuccess", null, locale));
				return "auth/login";
			} else {
				return "auth/signup";
			}
		}
	}
	
	@RequestMapping("/user/signupConfirm")
    public String signupConfirm(Locale locale, Model model, @RequestParam("token") final String token) throws UnsupportedEncodingException {
        final String result = userService.validateVerificationToken(token);
        if (result.equals("valid")) {
            model.addAttribute("message", messages.getMessage("message.accountVerified", null, locale));
        } else {
        	model.addAttribute("message", messages.getMessage("message.accountNotVerified", null, locale));
        }
        return "auth/login";
    }
	
	@RequestMapping("/user/forgotPassword")
	public String forgotPassword(Model model) {
		return "auth/forgotPassword";
	}
	
	@RequestMapping("/user/forgotPasswordSubmit")
    public String forgotPasswordSubmit(@RequestParam("email") final String userEmail, Model model, final HttpServletRequest request) {
        if(userEmail.trim().length() == 0) {
			model.addAttribute("message", "email cannot be empty");
			return "auth/forgotPassword";
	    }
        User user = userService.findByEmail(userEmail);
        if(user == null) {
			model.addAttribute("message", "we were not able to find a user with the email "+userEmail);
			return "auth/forgotPassword";
	    }
		eventPublisher.publishEvent(new OnPasswordRecoveryAsked(user, request.getLocale(), getAppUrl(request)));
        model.addAttribute("message", messages.getMessage("message.forgotPasswordLinkSent", null, request.getLocale()));
        return "auth/login";
    }
	
	@RequestMapping("/user/passwordRecovery")
    public String passwordRecovery(final Locale locale, final Model model, @RequestParam("id") final long id, @RequestParam("token") final String token) {
        final String result = userService.validatePasswordRecoveryToken(id, token);
        if (result != null) {
            model.addAttribute("message", messages.getMessage("auth.pswdRecoveryError", null, locale));
            return "redirect:/login?lang=" + locale.getLanguage();
        }
        model.addAttribute("passwordDto",new PasswordDto());
        return "auth/updatePassword";
    }
	
    @RequestMapping("/user/savePassword")
    @PreAuthorize("hasRole('READ_PRIVILEGE')")
    public String savePassword(final Locale locale, @Valid @ModelAttribute("passwordDto") PasswordDto passwordDto, BindingResult result, Model model) {
    	if(result.hasErrors()) {
    		return "auth/updatePassword";
		} else {
			boolean error = false;
			if(!passwordDto.getNewPassword().equals(passwordDto.getNewPasswordConfirm())) {
				result.rejectValue("newPasswordConfirm", "passwordDto.newPasswordConfirm", "passwords do not match");
				error = true;
			}
			
			if(!error) {
				final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		        userService.changeUserPassword(user, passwordDto);
		        model.addAttribute("message", messages.getMessage("auth.pswdUpdateSuccess", null, locale));
		        return "auth/login";	
			} else {
				return "auth/updatePassword";
			}
		}
    }

    
	/* support */
	private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
	
	/* errors */
	@ExceptionHandler(PasswordNotAccepted.class)
	public String handleErrorPswd(PasswordNotAccepted exception, Model model , Locale locale) throws IOException {
		model.addAttribute("message", exception.getMessage());
		return "error";
	}
	
	@ExceptionHandler(PasswordsNotEqualException.class)
	public String handleErrorPswdNeq(PasswordsNotEqualException exception, Model model , Locale locale) throws IOException {
		model.addAttribute("message", exception.getMessage());
		return "error";
	}
	
	@ExceptionHandler(UserAlreadyExistException.class)
	public String handleErrorUser(UserAlreadyExistException exception, Model model , Locale locale) throws IOException {
		model.addAttribute("message", exception.getMessage());
		return "error";
	}
	
	@ExceptionHandler(UserNotAuthorizedException.class)
	public String handleErrorUser(UserNotAuthorizedException exception, Model model , Locale locale) throws IOException {
		model.addAttribute("message", exception.getMessage());
		return "error";
	}
}
