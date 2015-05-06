package com.danielkueffer.filehosting.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.codec.digest.DigestUtils;

import com.danielkueffer.filehosting.auth.AuthManager;
import com.danielkueffer.filehosting.i18n.LocaleManager;
import com.danielkueffer.filehosting.persistence.dao.UserDao;
import com.danielkueffer.filehosting.persistence.model.Group;
import com.danielkueffer.filehosting.persistence.model.User;
import com.danielkueffer.filehosting.service.GroupService;
import com.danielkueffer.filehosting.service.UserService;
import com.danielkueffer.filehosting.util.DateUtil;

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

	@EJB
	GroupService groupService;

	@Inject
	AuthManager authManager;
	
	@Inject
	LocaleManager localeManager;

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

	/**
	 * Get all users
	 */
	@Override
	public List<User> getAllUsers() {
		return this.userDao.getAll();
	}

	/**
	 * Create a user
	 */
	@Override
	public void addUser(User user) {

		// Only Administrator can add users
		if (this.authManager.getCurrentUser().isAdmin()) {
			// Set active
			if (user.isCheckboxActive()) {
				user.setActive(1);
			}

			// Set the groups selected
			List<Group> groupList = new ArrayList<Group>();

			for (String id : user.getGroupIds()) {
				groupList.add(this.groupService.getGroupById(Integer
						.valueOf(id)));
			}

			user.setGroups(groupList);

			// Convert the password to md5
			user.setPassword(DigestUtils.md5Hex(user.getPassword()));

			user.setNotificationDiskFull(1);
			user.setDateCreated(DateUtil.getSQLTimestamp());

			this.userDao.create(user);
		}
	}

	/**
	 * Delete a user
	 */
	@Override
	public boolean deleteUser(int id) {

		// Only Administrators can delete users
		if (this.authManager.getCurrentUser().isAdmin()) {
			this.userDao.deleteById(id);

			return true;
		}

		return false;
	}

	/**
	 * Get a user by id and populate the required form fields
	 */
	@Override
	public User getUserById(int id) {
		User user = this.userDao.get(id);

		if (user.getActive() == 1) {
			user.setCheckboxActive(true);
		}

		List<String> groupIds = new ArrayList<String>();

		for (Group group : user.getGroups()) {
			groupIds.add(group.getId() + "");
		}

		user.setGroupIds(groupIds);

		return user;
	}

	/**
	 * Update a user
	 */
	@Override
	public boolean updateUser(User user) {

		// Only Administrator can update a user
		if (this.authManager.getCurrentUser().isAdmin()) {
			User updUser = this.userDao.get(user.getId());

			// Set the password only if it has changed
			if (!user.getPassword().equals(updUser.getPassword())) {
				updUser.setPassword(DigestUtils.md5Hex(user.getPassword()));
			}

			updUser.setLanguage(user.getLanguage());
			updUser.setDiskQuota(user.getDiskQuota());

			// Set active
			if (user.isCheckboxActive()) {
				updUser.setActive(1);
			} else {
				updUser.setActive(0);
			}

			// Set the groups selected
			List<Group> groupList = new ArrayList<Group>();

			for (String id : user.getGroupIds()) {
				groupList.add(this.groupService.getGroupById(Integer
						.valueOf(id)));
			}

			updUser.setGroups(groupList);

			this.userDao.update(updUser);
			return true;
		}

		return false;
	}

	/**
	 * Update user profile
	 */
	@Override
	public boolean updateUserProfile(User user) {
		User updUser = this.userDao.get(user.getId());

		// Set the password only if it has changed
		if (!user.getPassword().equals(updUser.getPassword())) {
			updUser.setPassword(DigestUtils.md5Hex(user.getPassword()));
		}

		updUser.setEmail(user.getEmail());
		
		// Set the language in the locale manager
		if (! user.getLanguage().equals(updUser.getLanguage())) {
			Locale l = new Locale(user.getLanguage());
			this.localeManager.setLanguage(l.getLanguage());
		}
		
		updUser.setLanguage(user.getLanguage());

		if (user.isCheckboxDiskFull()) {
			updUser.setNotificationDiskFull(1);
		}

		this.userDao.update(updUser);

		return true;
	}
}
