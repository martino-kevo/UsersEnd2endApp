package com.mk.usersend2endapp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mk.usersend2endapp.model.PasswordResetToken;
import com.mk.usersend2endapp.model.User;
import com.mk.usersend2endapp.repository.PasswordResetTokenRepository;
import com.mk.usersend2endapp.repository.UserRepository;

@Service
public class PasswordResetTokenService implements IPasswordResetTokenService {
	
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	
	/* Checks if a User has a Password Reset Token in the database. 
	 * If there is no token, an "invalid" String is returned. 
	 * If there is a token, a "valid" String is returned. */
	@Override
	public String checkPasswordResetToken(String theToken) {
		Optional<PasswordResetToken>  passwordResetToken = passwordResetTokenRepository.findByToken(theToken);
		if(passwordResetToken.isEmpty()) {
			return "invalid";
		}
		return "valid";
	}

	
	/* Finds a User in the database by the User's Password Reset Token and getting that 
	 * User. This is done by calling the 
	 * findByToken() method of the PasswordResetTokenRepository class. */
	@Override
	public Optional<User> findUserByPasswordResetToken(String theToken) {
		return Optional.ofNullable(passwordResetTokenRepository.findByToken(theToken).get().getUser());
	}
	
	
	// Resets the User's password by encoding the new password and saving the User again in the Database
	@Override
	public void resetPassword(User theUser, String newPassword) {
		theUser.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(theUser);
	}

	
	// Creates a Password Reset Token for the User and saving it in Database
	@Override
	public void createPasswordResetTokenForUser(User user, String passwordResetToken) {
		PasswordResetToken resetToken = new PasswordResetToken(passwordResetToken, user);
		passwordResetTokenRepository.save(resetToken);
	}

	
	// Deletes a User's Password Reset Token by a the User's id from the Database
	@Override
	public void deleteUserToken(Long id) {
		passwordResetTokenRepository.deleteByUserId(id);
	}

}
