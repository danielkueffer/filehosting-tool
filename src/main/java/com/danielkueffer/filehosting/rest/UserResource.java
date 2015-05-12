package com.danielkueffer.filehosting.rest;

import java.io.Serializable;
import java.security.GeneralSecurityException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.auth.login.LoginException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.danielkueffer.filehosting.rest.auth.RestAuthenticator;
import com.danielkueffer.filehosting.service.UserService;
import com.danielkueffer.filehosting.util.RestUtil;

/**
 * The user rest service
 * 
 * @author dkueffer
 * 
 */
@Path("user")
public class UserResource implements Serializable {

	private static final long serialVersionUID = 5149764323943523237L;

	@EJB
	UserService userService;

	@Inject
	RestAuthenticator restAuthenticator;

	/**
	 * Login to the resource
	 * 
	 * @param httpHeaders
	 * @param username
	 * @param password
	 * @return
	 */
	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@Context HttpHeaders httpHeaders,
			@FormParam("username") String username,
			@FormParam("password") String password) {

		try {
			String authToken = this.restAuthenticator.login(username, password);

			return Response.status(Response.Status.OK)
					.cacheControl(RestUtil.getCacheControl())
					.entity(RestUtil.getRestSuccessMessage(authToken)).build();

		} catch (final LoginException ex) {

			return Response.status(Response.Status.UNAUTHORIZED)
					.cacheControl(RestUtil.getCacheControl())
					.entity(RestUtil.getRestLoginError()).build();
		}
	}

	/**
	 * Logout of the resource
	 * 
	 * @param httpHeaders
	 * @return
	 */
	@POST
	@Path("logout")
	public Response logout(@Context HttpHeaders httpHeaders) {
		try {
			String authToken = httpHeaders.getHeaderString(RestUtil.AUTH_TOKEN);

			this.restAuthenticator.logout(authToken);

			return Response.status(Response.Status.NO_CONTENT).build();

		} catch (final GeneralSecurityException ex) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
	}

	/**
	 * Get the user info as JSON
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public String getUserInfo() {
		return this.userService.getUserInfoAsJson();
	}
}
