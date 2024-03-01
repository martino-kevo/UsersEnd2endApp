package com.mk.usersend2endapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* This PasswordResetToken class represents a Data we are going to be working 
 * with in the database. This is the Password Reset Token which would be 
 * generated for the User when the User wants to reset their password */

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PasswordResetToken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String token;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	

	public PasswordResetToken(String token, User user) {
		this.token = token;
		this.user = user;
	}

}
