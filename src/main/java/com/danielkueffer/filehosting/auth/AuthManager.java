package com.danielkueffer.filehosting.auth;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
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
@SessionScoped
public class AuthManager implements Serializable {

	private static final long serialVersionUID = -1997507066926190203L;

	public AuthManager() {
	}

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
