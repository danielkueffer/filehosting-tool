package com.danielkueffer.filehosting.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.primefaces.model.UploadedFile;

import com.danielkueffer.filehosting.auth.AuthManager;
import com.danielkueffer.filehosting.i18n.MessageProvider;
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
	
	@Inject
	MessageProvider messageProvider;

	private User user;

	@NotNull
	private UploadedFile file;

	private String profileImagePath;
	
	private String message;

	@PostConstruct
	public void init() {
		this.user = this.authManager.getCurrentUser();
		
		if (this.user.getNotificationDiskFull() == 1) {
			this.user.setCheckboxDiskFull(true);
		}
		
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
		if (file.getSize() > 0) {
			this.userService.saveProfileImage(file);
			this.profileImagePath = this.userService.getProfileImage(this.user);
			
			return "/profile.xhtml?faces-redirect=true";
		}
		else {
			message = this.messageProvider.getValue("profile.imageempty");

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, message, ""));
			
			return "/profile.xhtml";
		}
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

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
