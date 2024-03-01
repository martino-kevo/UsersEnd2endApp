package com.mk.usersend2endapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mk.usersend2endapp.repository.UserRepository;

// Needed for an authenticated User

@Service
public class EndToEndUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return userRepository.findByEmail(email)
				.map(EndToEndUserDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

}
