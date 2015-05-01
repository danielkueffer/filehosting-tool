package com.danielkueffer.filehosting.auth;

import javax.inject.Inject;
import javax.inject.Named;

import com.danielkueffer.filehosting.persistence.model.User;

/**
 * The AuthManager
 * 
 * @author dkueffer
 * 
 */
@Named
public class AuthManager {

	@Inject
	@LoggedIn
	User currentUser;

	/**
	 * Get the current user
	 * 
	 * @return
	 */
	public User getCurrentUser() {
		return this.currentUser;
	}
}
