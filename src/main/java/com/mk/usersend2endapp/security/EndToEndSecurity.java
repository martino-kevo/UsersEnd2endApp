package com.mk.usersend2endapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// Spring Security web configuration file

@Configuration
@EnableWebSecurity
public class EndToEndSecurity {

	
	// This Bean method encodes a password (turns a password into a series of characters in the database).
    @Bean
   public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    /* This Bean method specifies the end points that are free for 
     * non authenticated Users. 
     * It also specifies the login and logout features */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
						.requestMatchers("/", "/login", "/error", "/registration/**").permitAll()
						.anyRequest().authenticated())
				.formLogin(formLogin -> formLogin.loginPage("/login")
						.usernameParameter("email").defaultSuccessUrl("/")
						.permitAll())
				.logout(logout -> logout.invalidateHttpSession(true)
						.clearAuthentication(true)
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/")).build();
	}

}
