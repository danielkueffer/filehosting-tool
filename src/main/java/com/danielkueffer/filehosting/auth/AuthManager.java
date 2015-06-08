package com.danielkueffer.filehosting.auth;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.danielkueffer.filehosting.persistence.model.User;
import com.danielkueffer.filehosting.rest.auth.RestLoggedIn;

/**
 * The AuthManager
 * 
 * @author dkueffer
 * 
 */
@Named
@SessionScoped
public class AuthManager implements Serializable {

	private static final long serialVersionUID = -1997507066926190203L;

	public AuthManager() {
	}

	@Inject
	@LoggedIn
	User currentUser;

	@Inject
	@RestLoggedIn
	User restUser;

	/**
	 * Get the current user
	 * 
	 * @return
	 */
	public User getCurrentUser() {
		if (currentUser != null) {
			return currentUser;
		} else {
			return restUser;
		}
	}
}
