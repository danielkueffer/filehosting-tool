package com.danielkueffer.filehosting.controller;

import java.io.Serializable;
import java.util.Locale;

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
import com.danielkueffer.filehosting.i18n.LocaleManager;
import com.danielkueffer.filehosting.i18n.MessageProvider;
import com.danielkueffer.filehosting.persistence.model.Group;
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

	@Inject
	MessageProvider messageProvider;

	@EJB
	UserService userService;

	@Inject
	LocaleManager localeManager;

	private User user;

	private String message;

	/**
	 * Login
	 * 
	 * @return
	 */
	public String doLogin() {

		User user = null;

		if (this.credentials.getUsername() != null
				&& this.credentials.getPassword() != null) {

			user = this.userService.login(this.credentials.getUsername(),
					this.credentials.getPassword());
		}

		boolean login = false;

		if (user != null) {
			if (user.getActive() == 1) {
				login = true;
			}
		}

		// Login success
		if (login) {
			this.user = user;

			// Set the locale
			Locale l = new Locale(user.getLanguage());
			this.localeManager.setLanguage(l.getLanguage());

			// Set if user is a administrator
			for (Group g : this.user.getGroups()) {
				if (g.getIsAdmin() == 1) {
					this.user.setAdmin(true);
				}
			}

			return "/files.xhtml?faces-redirect=true";
		} else {

			message = this.messageProvider.getValue("login.invalid");

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, message, ""));

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
	 * @return the loggedIn
	 */
	public boolean isLoggedIn() {
		return this.user != null;
	}

	/**
	 * Get the current user
	 * 
	 * @return
	 */
	@Produces
	@LoggedIn
	public User getCurrentUser() {
		return user;
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
}
