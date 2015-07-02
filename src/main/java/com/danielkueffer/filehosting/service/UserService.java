package com.danielkueffer.filehosting.service;

import java.util.List;

import org.primefaces.model.UploadedFile;

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
	
	boolean updateUserProfile(User user);
	
	boolean saveProfileImage(UploadedFile file);
	
	String getProfileImage(User user);
	
	String getUserInfoAsJson();
	
	void setLastLoginTime();
}
