package com.mk.usersend2endapp.registration;

import java.util.List;

import com.mk.usersend2endapp.model.Role;

import lombok.Data;

// This is the Registration Request of the User that is being registered.

@Data
public class RegistrationRequest {
	
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private List<Role> roles;

}
