package com.mk.usersend2endapp.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.mk.usersend2endapp.model.User;
import com.mk.usersend2endapp.registration.RegistrationRequest;
import org.springframework.security.core.Authentication;

public interface IUserService {
	
	String registerUser(RegistrationRequest registration);
	
	User findByEmail(String email);

	Optional<User> findById(Long id);

	void updateUser(Long id, String firstName, String lastName, String email);

	Page<User> findPage(int pageNumber);

	String deleteUser(Long id, Authentication authentication);

}
