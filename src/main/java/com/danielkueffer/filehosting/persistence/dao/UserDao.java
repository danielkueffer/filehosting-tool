package com.danielkueffer.filehosting.persistence.dao;

import java.util.List;

import com.danielkueffer.filehosting.persistence.model.User;


/**
 * The user dao
 * 
 * @author dkueffer
 *
 */
public interface UserDao extends Dao<User> {
	List<User> login(String username, String password);
	
	List<User> getUserFromUsername(String username);
}
