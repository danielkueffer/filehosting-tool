package com.danielkueffer.filehosting.rest.auth;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

import com.danielkueffer.filehosting.util.RestUtil;

/**
 * Add the authToken to the response headers
 * 
 * @author dkueffer
 * 
 */
@Provider
@PreMatching
public class RestResponseFilter implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext requestContext,
			ContainerResponseContext responseContext) throws IOException {

		responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
		responseContext.getHeaders().add("Access-Control-Allow-Credentials",
				"true");
		responseContext.getHeaders().add("Access-Control-Allow-Methods",
				"GET, POST, DELETE, PUT");
		responseContext.getHeaders().add("Access-Control-Allow-Headers",
				RestUtil.AUTH_TOKEN);
	}

}