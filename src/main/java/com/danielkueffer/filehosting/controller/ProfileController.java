package com.danielkueffer.filehosting.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.danielkueffer.filehosting.auth.AuthManager;
import com.danielkueffer.filehosting.persistence.model.User;
import com.danielkueffer.filehosting.service.UserService;

/**
 * The user profile controller
 * 
 * @author dkueffer
 * 
 */
@Named
@RequestScoped
public class ProfileController {

	@EJB
	UserService userService;

	@Inject
	AuthManager authManager;

	private User user;

	@PostConstruct
	public void init() {
		this.user = this.authManager.getCurrentUser();
	}

	/**
	 * Update the user profile
	 */
	public String updateProfile() {
		this.userService.updateUserProfile(this.user);
		
		return "/profile.xhtml?faces-redirect=true";
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
}
