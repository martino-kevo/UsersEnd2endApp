package com.mk.usersend2endapp.service;

import java.util.Optional;

import com.mk.usersend2endapp.model.User;

public interface IPasswordResetTokenService {

	String checkPasswordResetToken(String theToken);

	Optional<User> findUserByPasswordResetToken(String theToken);

	void resetPassword(User theUser, String newPassword);

	void createPasswordResetTokenForUser(User user, String passwordResetToken);

	void deleteUserToken(Long id);

}
