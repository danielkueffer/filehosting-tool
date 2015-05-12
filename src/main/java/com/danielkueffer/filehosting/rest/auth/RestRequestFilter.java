package com.danielkueffer.filehosting.rest.auth;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.danielkueffer.filehosting.controller.AuthController;
import com.danielkueffer.filehosting.persistence.model.User;
import com.danielkueffer.filehosting.util.RestUtil;

/**
 * Check if the user is logged in or has a authKey
 * 
 * @author dkueffer
 * 
 */
@Provider
@PreMatching
public class RestRequestFilter implements ContainerRequestFilter {

	@Inject
	AuthController authController;

	@Inject
	RestAuthenticator restAuthenticator;

	@Override
	public void filter(ContainerRequestContext requestCtx) throws IOException {

		// IMPORTANT!!! First, Acknowledge any pre-flight test from browsers for
		// this case before validating the headers (CORS stuff)
		if (requestCtx.getRequest().getMethod().equals("OPTIONS")) {
			requestCtx.abortWith(Response.status(Response.Status.OK).build());

			return;
		}

		String path = requestCtx.getUriInfo().getPath();

		// For any methods besides login, the authToken must be verified
		if (!path.startsWith("user/login/")) {

			// Check if the user is already logged in
			if (!authController.isLoggedIn()) {
				String authToken = requestCtx
						.getHeaderString(RestUtil.AUTH_TOKEN);

				// Not valid
				if (!restAuthenticator.isAuthTokenValid(authToken)) {
					requestCtx.abortWith(Response.status(
							Response.Status.UNAUTHORIZED).build());
				}
				// Set the current user in the authController
				else {
					User user = this.restAuthenticator
							.getUserFromToken(authToken);
					this.restAuthenticator.setCurrentRestUser(user);
				}
			}
		}
	}

}
