package com.danielkueffer.filehosting.controller;

import java.io.Serializable;
import java.util.Locale;

import javax.ejb.EJB;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import com.danielkueffer.filehosting.auth.Credentials;
import com.danielkueffer.filehosting.auth.LoggedIn;
import com.danielkueffer.filehosting.i18n.MessageProvider;
import com.danielkueffer.filehosting.persistence.model.User;
import com.danielkueffer.filehosting.service.UserService;

/**
 * The authorization controller
 * 
 * @author dkueffer
 * 
 */
@ManagedBean
@SessionScoped
public class AuthController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	Credentials credentials;

	@Inject
	MessageProvider messageProvider;

	@EJB
	UserService userService;
	
	@ManagedProperty(value = "#{localeController}")
	LocaleController localeManager;

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

		// Login success
		if (user != null) {
			this.user = user;

			Locale l = new Locale(user.getLanguage());
			this.localeManager.setLanguage(l.getLanguage());

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
	public User getCurrentUser() {
		return this.user;
	}

	/**
	 * @param localeManager
	 *            the localeManager to set
	 */
	public void setLocaleManager(LocaleController localeManager) {
		this.localeManager = localeManager;
	}
}
