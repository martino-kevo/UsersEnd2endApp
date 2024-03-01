package com.mk.usersend2endapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mk.usersend2endapp.model.User;
import com.mk.usersend2endapp.registration.RegistrationRequest;
import com.mk.usersend2endapp.service.IUserService;

@Controller
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private IUserService userService;
	
	
	// Handles the end point for the first page of Users result
	@GetMapping
	public String getFirstPage(Model model){
	    return getOnePage(model, 1);
	}
	
	// Handles the end point for the other pages of Users result
	@GetMapping("/page/{pageNumber}")
	public String getOnePage(Model model, @PathVariable("pageNumber") int currentPage){
	    Page<User> page = userService.findPage(currentPage);
	    int totalPages = page.getTotalPages();
	    long totalItems = page.getTotalElements();
	    List<User> users = page.getContent();

	    model.addAttribute("currentPage", currentPage);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("totalItems", totalItems);
	    model.addAttribute("users", users);

	    return "users";
	}
	
	// Handles the end point for the Add a new User form page
	@GetMapping("/add-user-form")
	public String addUserForm(Model model) {
		model.addAttribute("user", new RegistrationRequest());
		return "add-a-new-user";
	}
	
	
	/* Adds a new User by calling the registerUser() method of the 
	 * UserService class. 
	 * If the User already exists in the database, the User will be 
	 * redirected to the Users page with a User Exists message. 
	 * If not, the User will be 
	 * redirected to the Users page with a success message. */
	@PostMapping("/add-user")
	public String addUser(@ModelAttribute("user") RegistrationRequest registration) {
		String user = userService.registerUser(registration);
		if(user.equalsIgnoreCase("user exists")) {
		return "redirect:/users/add-user-form?userExists";
		}
		return "redirect:/users?add_success";
	}
	
	/* Handles the end point for the User Update form page. 
	* Here is what it does: it first finds the User by id, 
	* 						then that User"s data is passed to 
	* 						the User update form
	* 						where it will be updated. */
	@GetMapping("/edit/{id}")
	public String showUpdateForm(@PathVariable Long id, Model model) {
		Optional<User> user = userService.findById(id);
		model.addAttribute("user", user.get());
		return "update-user";
	}
	
	
	/* Updates the User's data (excepts the User's password) 
	 * by calling the updateUser() method of the UserService class, and then 
	 * redirects the User to the users page with a success message. */
	@PostMapping("/update/{id}")
	public String updateUser(@PathVariable Long id, User user) {
		userService.updateUser(id, user.getFirstName(), user.getLastName(), user.getEmail());
		return "redirect:/users?update_success";
	}
	
	
	/* Deletes the User's data by calling the deleteUser() method of the UserService class. 
	 * If the User that is being deleted is logged in, the User will be deleted 
	 * and be redirected to the logout end point.
	 * If that is not the case, the User will be deleted and be redirected 
	 * to the users page with a delete success message. */
	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable Long id, Authentication authentication) {
		String user = userService.deleteUser(id, authentication);
		if(user.equalsIgnoreCase("forced logout")) {
			return "redirect:/logout";
			}
		return "redirect:/users?delete_success";
	}

}
