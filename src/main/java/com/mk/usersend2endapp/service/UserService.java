package com.mk.usersend2endapp.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mk.usersend2endapp.model.Role;
import com.mk.usersend2endapp.model.User;
import com.mk.usersend2endapp.registration.RegistrationRequest;
import com.mk.usersend2endapp.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class UserService implements IUserService {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private IPasswordResetTokenService passwordResetTokenService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	// Gets all Users from the database in a 5 results per page format
	@Override
	public Page<User> findPage(int pageNumber){
	    Pageable pageable = PageRequest.of(pageNumber - 1,5);
	    return userRepository.findAll(pageable);
	}

	
	/* First checks if the User exists in the database, 
	 * if the User exists in the database, a "user exists" String will be returned 
	 * which the UserController will use to display the user exists message 
	 * on the client side. 
	 * If not. the User's password will be encoded and the User will be registered 
	 * getting the USER_ROLE by default. */
	@Override
	public String registerUser(RegistrationRequest registration) {
		Optional<User> user = userRepository.findByEmail(registration.getEmail());
		if(user.isPresent()) {
			return "user exists";
		}
		var theUser = new User(registration.getFirstName(), registration.getLastName(), 
				registration.getEmail(), 
				passwordEncoder.encode(registration.getPassword()), 
				Arrays.asList(new Role("ROLE_USER")));
		theUser.setEnabled(true);
		userRepository.save(theUser);
		return "";
				
	}

	
	/* Finds a User in the database by email, by calling the 
	 * findByEmail() method of the UserRepository class. If the User is not found, 
	 * an Exception is thrown with the String "User not found". */
	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	
	/* Finds a User in the database by id, by calling the 
	 * findById() method of the UserRepository class. */
	@Override
	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}

	/* Updates a User in the database by calling the 
	 * modified update() method of the UserRepository class. */
	@Transactional
	@Override
	public void updateUser(Long id, String firstName, String lastName, String email) {
		userRepository.update(firstName, lastName, email, id);
	}

	
	/* First finds a User in the database by the User's id. 
	 * If the User that is being deleted is logged in, the User's Password Reset Token 
	 * will be deleted from the database if there is any, then the User will be deleted 
	 * from the database and a "forced logout" String will be returned. 
	 * If that is not the case, the User's Password Reset Token 
	 * will be deleted from the database if there is any, then the User will be deleted 
	 * from the database and an empty String will be returned. */
	@Transactional
	@Override
	public String deleteUser(Long id, Authentication authentication) {
		Optional<User> theUser = userRepository.findById(id);
		String loggedInUser = authentication.getName();
		if(theUser.isPresent() && theUser.get().getEmail().equalsIgnoreCase(loggedInUser)) {
			passwordResetTokenService.deleteUserToken(theUser.get().getId());
			userRepository.deleteById(theUser.get().getId());
			return "forced logout";
		}
		theUser.ifPresent((user) -> passwordResetTokenService.deleteUserToken(user.getId()));
		userRepository.deleteById(id);
		return "";
	}


}
