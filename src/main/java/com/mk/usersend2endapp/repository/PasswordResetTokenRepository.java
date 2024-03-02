package com.mk.usersend2endapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mk.usersend2endapp.model.PasswordResetToken;

//This PasswordResetTokenRepository class allows the performing of Database queries on the Password Reset Token table from the application

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

	Optional<PasswordResetToken> findByToken(String theToken);

	void deleteByUserId(Long id);

}
