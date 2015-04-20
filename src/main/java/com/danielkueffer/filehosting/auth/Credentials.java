package com.danielkueffer.filehosting.auth;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * The login credentials
 * 
 * @author dkueffer
 *
 */
@Named
@RequestScoped
public class Credentials {
	
	private String password;
	private String username;
	
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
}
