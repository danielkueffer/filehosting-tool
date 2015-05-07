package com.danielkueffer.filehosting.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.primefaces.model.UploadedFile;

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

	@NotNull
	private UploadedFile file;

	private String profileImagePath;

	@PostConstruct
	public void init() {
		this.user = this.authManager.getCurrentUser();
		this.profileImagePath = this.userService.getProfileImage(this.user);
	}

	/**
	 * Update the user profile
	 */
	public String updateProfile() {
		this.userService.updateUserProfile(this.user);

		return "/profile.xhtml?faces-redirect=true";
	}

	/**
	 * Upload the profile image
	 */
	public String upload() {
		if (file != null) {
			this.userService.saveProfileImage(file);
			this.profileImagePath = this.userService.getProfileImage(this.user);
		}
		
		return "/profile.xhtml?faces-redirect=true";
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Get the uploaded file
	 * 
	 * @return
	 */
	public UploadedFile getFile() {
		return file;
	}

	/**
	 * Set the uploaded file
	 * 
	 * @param file
	 */
	public void setFile(UploadedFile file) {
		this.file = file;
	}

	/**
	 * Get the profile image path
	 * 
	 * @return
	 */
	public String getProfileImagePath() {
		return profileImagePath;
	}
}
