package com.danielkueffer.filehosting.service;

import java.util.List;

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
	
	List<User> getAllUsers();
	
	void addUser(User user);
	
	boolean deleteUser(int id);
	
	User getUserById(int id);
	
	boolean updateUser(User user);
}
