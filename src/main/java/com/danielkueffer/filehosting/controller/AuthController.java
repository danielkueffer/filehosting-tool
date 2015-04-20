package com.danielkueffer.filehosting.controller;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import com.danielkueffer.filehosting.auth.Credentials;
import com.danielkueffer.filehosting.auth.LoggedIn;
import com.danielkueffer.filehosting.persistence.model.User;
import com.danielkueffer.filehosting.service.UserService;


/**
 * The authorization controller
 * 
 * @author dkueffer
 * 
 */
@Named
@SessionScoped
public class AuthController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	Credentials credentials;

	@EJB
	UserService userService;

	private User user;
	private String message;

	/**
	 * Login
	 * 
	 * @return
	 */
	public String doLogin() {

		User user = this.userService.login(this.credentials.getUsername(),
				this.credentials.getPassword());

		if (user != null) {
			this.user = user;

			return "/files.xhtml?faces-redirect=true";
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Invalid Login!", "Please Try Again!"));

			message = "Invalid Login. Please Try Again!";

			return "/login.xhtml";
		}
	}

	/**
	 * Logout
	 * 
	 * @return
	 */
	public String logout() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		session.invalidate();

		this.user = null;

		return "/login.xhtml?faces-redirect=true";
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the loggedIn
	 */
	public boolean isLoggedIn() {
		return this.user != null;
	}

	/**
	 * Set the logged in user qualifier
	 * 
	 * @return
	 */
	@Produces
	@LoggedIn
	User getCurrentUser() {
		return this.user;
	}

}
