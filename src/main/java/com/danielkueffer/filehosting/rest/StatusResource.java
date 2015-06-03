package com.danielkueffer.filehosting.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Status rest service. Used by the desktop client to check if the URL to server
 * entered is valid
 * 
 * @author dkueffer
 * 
 */
@Path("status")
public class StatusResource {

	/**
	 * Return the status as JSON
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getStatus() {
		return "{\"installed\": true}";
	}
}
