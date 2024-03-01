package com.mk.usersend2endapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/* This Controller handles the end points for the Home page
 * Login page and Error page */

@Controller
@RequestMapping("/")
public class HomeController {
	
	@GetMapping
	public String homePage() {
		return "home";
	}
	
	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}
	
	@GetMapping("/error")
	public String errorPage() {
		return "error";
	}

}
