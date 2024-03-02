package com.mk.usersend2endapp.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mk.usersend2endapp.model.User;
import com.mk.usersend2endapp.registration.RegistrationRequest;
import com.mk.usersend2endapp.service.IPasswordResetTokenService;
import com.mk.usersend2endapp.service.IUserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IPasswordResetTokenService passwordResetTokenService;
	
	
	// Handles the end point for the Registration form page
	@GetMapping("/registration-form")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new RegistrationRequest());
		return "registration";
	}
	
	
	/* Registers a User by calling the registerUser() method of the 
	 * UserService class. 
	 * If the User already exists in the database, the User will be 
	 * redirected to the Login page with a User Exist message. 
	 * If not, a registration success message will just pop up */
	@PostMapping("/register")
	public String registerUser(@ModelAttribute("user") RegistrationRequest registration) {
		String user = userService.registerUser(registration);
		if(user.equalsIgnoreCase("user exists")) {
		return "redirect:/login?userExists";
		}
		return "redirect:/registration/registration-form?success";
	}
	
	
	// Handles the end point for the Forgot password form page
	@GetMapping("/forgot-password-form")
	public String forgotPasswordForm() {
		return "forgot-password-form";
	}
	
	
	/* Gets the User's email from the form input field.
	 * Finds the User by email, by calling the findByEmail() method of the 
	 * UserService class. 
	 * If the User does not exist in the database, the User will be 
	 * redirected to the Login page. 
	 * If not, a Password Reset Token will be created for the User and will be redirected 
	 * to the Password reset form page with the token. */
	@PostMapping("/forgot-password")
	public String resetPasswordRequest(HttpServletRequest request) {
		String email = request.getParameter("email");
		User user = userService.findByEmail(email);
		if(user == null) {
			return "redirect:/login";
		}
		String passwordResetToken = UUID.randomUUID().toString();
		passwordResetTokenService.createPasswordResetTokenForUser(user, passwordResetToken);
		return "redirect:/registration/reset-password-form?token=" + passwordResetToken;
	}
	
	
	// Handles the end point for the Password reset form page.
	@GetMapping("/reset-password-form")
	public String passwordResetForm(@RequestParam("token") String token, Model model) {
		model.addAttribute("token", token);
		return "password-reset-form";
	}
	
	
	/* Gets the User's token from the form input field.
	 * Gets the User's new password from the form input field.
	 * Checks if the User has a Password reset Token. If the User does not have, 
	 * the User will be redirected to the Error page with an error message.
	 * If the User has, the User will be gotten from the token, then the User's password
	 * will be changed.
	 * Then the User will be redirected to the Login page with a success message. */
	@PostMapping("/reset-password")
	public String resetPassword(HttpServletRequest request) {
		String theToken = request.getParameter("token");
		String newPassword = request.getParameter("password");
		String checkToken = passwordResetTokenService.checkPasswordResetToken(theToken);
		if(!checkToken.equalsIgnoreCase("valid")) {
			return "redirect:/error?invalid_token";
		}
		Optional<User> theUser = passwordResetTokenService.findUserByPasswordResetToken(theToken);
		if(theUser.isPresent()) {
			passwordResetTokenService.resetPassword(theUser.get(), newPassword);
			return "redirect:/login?reset_success";
		}
		return "redirect:/error?not_found";
	}

}
