package com.danielkueffer.filehosting.rest.auth;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ejb.EJB;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.security.auth.login.LoginException;

import com.danielkueffer.filehosting.persistence.model.User;
import com.danielkueffer.filehosting.service.UserService;

/**
 * The Rest authenticator
 * 
 * @author dkueffer
 * 
 */
@Singleton
public class RestAuthenticator {

	@EJB
	UserService userService;

	private User user;

	private final Map<String, String> authorizationTokensStorage = new HashMap<String, String>();

	/**
	 * Login
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws LoginException
	 */
	public String login(String username, String password) throws LoginException {
		User user = this.userService.login(username, password);

		if (user != null) {

			/**
			 * Once all parameters are matched, the authToken will be generated
			 * and will be stored in the authorizationTokensStorage. The
			 * authToken will be needed for every REST API invocation unless the
			 * user is already logged in in the web application
			 */
			String authToken = UUID.randomUUID().toString();
			authorizationTokensStorage.put(authToken, username);
			
			return authToken;
		}

		throw new LoginException("Login failed!");
	}

	/**
	 * Check if the authToken is valid
	 * 
	 * @param authToken
	 * @return
	 */
	public boolean isAuthTokenValid(String authToken) {
		return authorizationTokensStorage.containsKey(authToken);
	}

	/**
	 * Get the user associated with the authToken
	 * 
	 * @param authToken
	 * @return
	 */
	public User getUserFromToken(String authToken) {
		String username = this.authorizationTokensStorage.get(authToken);

		return this.userService.getUserFromUsername(username);
	}

	/**
	 * Set the current rest user
	 * 
	 * @param user
	 */
	public void setCurrentRestUser(User user) {
		this.user = user;
	}

	/**
	 * Get the current rest user
	 * 
	 * @return
	 */
	@Produces
	@RestLoggedIn
	public User getCurrentRestUser() {
		return this.user;
	}

	/**
	 * Logout
	 * 
	 * @param serviceKey
	 * @param authToken
	 * @throws GeneralSecurityException
	 */
	public void logout(String authToken) throws GeneralSecurityException {

		if (authorizationTokensStorage.containsKey(authToken)) {

			/**
			 * When a client logs out, the authentication token will be removed
			 * and will be made invalid.
			 */
			authorizationTokensStorage.remove(authToken);
			this.setCurrentRestUser(null);

			return;
		}

		throw new GeneralSecurityException("Invalid authorization token.");
	}
}
