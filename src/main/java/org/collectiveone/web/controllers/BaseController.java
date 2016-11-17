package org.collectiveone.web.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.collectiveone.model.User;
import org.collectiveone.registration.OnPasswordRecoveryAsked;
import org.collectiveone.registration.OnRegistrationCompleteEvent;
import org.collectiveone.services.UserServiceIf;
import org.collectiveone.web.dto.PasswordDto;
import org.collectiveone.web.dto.UserNewDto;
import org.collectiveone.web.error.PasswordsNotEqualException;
import org.collectiveone.web.error.UserAlreadyExistException;
import org.collectiveone.web.error.UserNotAuthorizedException;
import org.collectiveone.web.error.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class BaseController {
	
    @Autowired
    private UserServiceIf userService;
    
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
	
	@RequestMapping("/user/signup")
	public String signup(Model model) {
		model.addAttribute("user",new UserNewDto());
		return "auth/signup";
	}
	
	@RequestMapping("/user/signupSubmit")
	public String signupSubmit(Locale locale, @Valid UserNewDto userNewDto,  Model model, final HttpServletRequest request) {
		final User registered = userService.registerNewUserAccount(userNewDto);
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
		model.addAttribute("message", messages.getMessage("message.signupSuccess", null, locale));
		return "auth/login";
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
    public String forgotPasswordSubmit(final HttpServletRequest request, @RequestParam("email") final String userEmail, Model model) {
        final User user = userService.findByEmail(userEmail);
        if (user == null) {
            throw new UserNotFoundException();
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
    public String savePassword(final Locale locale, @Valid PasswordDto passwordDto, Model model) {
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.changeUserPassword(user, passwordDto);
        model.addAttribute("message", messages.getMessage("auth.pswdUpdateSuccess", null, locale));
        return "auth/login";
    }
	
	
	/* support */
	
	private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
	
	/* errors */
	
	@ExceptionHandler(PasswordsNotEqualException.class)
	public String handleErrorPswd(PasswordsNotEqualException exception, Model model , Locale locale) throws IOException {
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
