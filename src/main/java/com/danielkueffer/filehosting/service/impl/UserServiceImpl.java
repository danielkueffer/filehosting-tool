package com.danielkueffer.filehosting.service.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.codec.digest.DigestUtils;

import com.danielkueffer.filehosting.persistence.dao.UserDao;
import com.danielkueffer.filehosting.persistence.model.User;
import com.danielkueffer.filehosting.service.UserService;

/**
 * The user service implementation
 * 
 * @author dkueffer
 * 
 */
@Stateless
public class UserServiceImpl implements UserService {

	@EJB
	UserDao userDao;

	/**
	 * Check the login data
	 */
	@Override
	public User login(String username, String password) {
		String md5Pass = DigestUtils.md5Hex(password);
		
		List<User> result = this.userDao.login(username, md5Pass);
		
		User user = null;

		if (!result.isEmpty()) {
			user = result.get(0);
		}

		return user;
	}

	/**
	 * Get a user from user name
	 */
	@Override
	public User getUserFromUsername(String username) {
		List<User> result = this.userDao.getUserFromUsername(username);

		User user = null;

		if (!result.isEmpty()) {
			user = result.get(0);
		}

		return user;
	}
}
