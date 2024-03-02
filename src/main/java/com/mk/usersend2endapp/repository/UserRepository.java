package com.mk.usersend2endapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mk.usersend2endapp.model.User;

// This UserRepository class allows the performing of Database queries on the user table from the application.

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByEmail(String email);
	
	@Modifying      // Annotation is needed because only a part of the entity is being modified
	@Query(value = "UPDATE User u SET u.firstName = :firstName,"
			+ "u.lastName = :lastName, u.email = :email WHERE u.id = :id")
	void update(String firstName, String lastName, String email, Long id);

}
