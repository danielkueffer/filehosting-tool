package com.danielkueffer.filehosting.util;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.CacheControl;

/**
 * REST utilities
 * 
 * @author dkueffer
 * 
 */
public class RestUtil {
	
	public static final String AUTH_TOKEN = "auth_token";

	/**
	 * Get the JSON message with the authToken
	 * 
	 * @param authToken
	 * @return
	 */
	public static String getRestSuccessMessage(String authToken) {
		JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
		jsonObjBuilder.add("auth_token", authToken);
		JsonObject jsonObj = jsonObjBuilder.build();

		return jsonObj.toString();
	}

	/**
	 * Get the JSON login error
	 * 
	 * @return
	 */
	public static String getRestLoginError() {
		JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
		jsonObjBuilder.add("message", "Problem matching username and password");
		JsonObject jsonObj = jsonObjBuilder.build();

		return jsonObj.toString();
	}

	/**
	 * Return the cache control
	 * 
	 * @return
	 */
	public static CacheControl getCacheControl() {
		CacheControl cc = new CacheControl();
		cc.setNoCache(true);
		cc.setMaxAge(-1);
		cc.setMustRevalidate(true);

		return cc;
	}
}
