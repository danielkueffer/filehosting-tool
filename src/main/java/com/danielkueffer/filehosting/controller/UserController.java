package com.danielkueffer.filehosting.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.danielkueffer.filehosting.persistence.model.User;
import com.danielkueffer.filehosting.service.UserService;

/**
 * The user controller
 * 
 * @author dkueffer
 * 
 */
@Named
@RequestScoped
public class UserController {

	@EJB
	UserService userService;

	User user;
	
	@PostConstruct
	public void init() {
		this.user = new User();
	}

	/**
	 * Get all users
	 * 
	 * @return
	 */
	public List<User> getUsers() {
		return this.userService.getAllUsers();
	}

	/**
	 * Add a new user
	 * 
	 * @return
	 */
	public String addUser() {
		this.userService.addUser(this.user);
		
		return "/user/list.xhtml?faces-redirect=true";
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
}
