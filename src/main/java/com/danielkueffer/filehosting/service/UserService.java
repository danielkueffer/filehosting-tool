package com.danielkueffer.filehosting.service;

import com.danielkueffer.filehosting.persistence.model.User;

/**
 * The user service
 * 
 * @author dkueffer
 * 
 */
public interface UserService {
	User login(String username, String password);

	User getUserFromUsername(String username);
}
