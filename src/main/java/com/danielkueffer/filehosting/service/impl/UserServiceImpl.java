package com.danielkueffer.filehosting.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.model.UploadedFile;

import com.danielkueffer.filehosting.auth.AuthManager;
import com.danielkueffer.filehosting.i18n.LocaleManager;
import com.danielkueffer.filehosting.persistence.dao.UserDao;
import com.danielkueffer.filehosting.persistence.model.Group;
import com.danielkueffer.filehosting.persistence.model.User;
import com.danielkueffer.filehosting.service.FileService;
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

	private static final String BASE_DIR = "jboss.server.data.dir";
	private static final String PROFILE_IMAGE_DIR = "profile-images";

	@EJB
	UserDao userDao;

	@EJB
	GroupService groupService;

	@EJB
	FileService fileService;

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
		// Set active
		if (user.isCheckboxActive()) {
			user.setActive(1);
		}
		
		long diskQuota = user.getDiskQuota();

		// Set the diskQuota from GB to bytes
		user.setDiskQuota(this.getBytesFormGB(diskQuota));

		// Set the groups selected
		List<Group> groupList = new ArrayList<Group>();

		for (String id : user.getGroupIds()) {
			groupList.add(this.groupService.getGroupById(Integer.valueOf(id)));
		}

		user.setGroups(groupList);

		// Convert the password to md5
		user.setPassword(DigestUtils.md5Hex(user.getPassword()));

		user.setNotificationDiskFull(1);
		user.setDateCreated(DateUtil.getSQLTimestamp());

		this.userDao.create(user);
	}

	/**
	 * Delete a user
	 */
	@Override
	public boolean deleteUser(int id) {
		this.userDao.deleteById(id);

		return true;
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
		User updUser = this.userDao.get(user.getId());

		// Set the password only if it has changed
		if (!user.getPassword().equals(updUser.getPassword())) {
			updUser.setPassword(DigestUtils.md5Hex(user.getPassword()));
		}

		updUser.setLanguage(user.getLanguage());

		// Set the diskQuota from GB to bytes
		long diskQuota = user.getDiskQuota();
		updUser.setDiskQuota(this.getBytesFormGB(diskQuota));

		// Set active
		if (user.isCheckboxActive()) {
			updUser.setActive(1);
		} else {
			updUser.setActive(0);
		}

		// Set the groups selected
		List<Group> groupList = new ArrayList<Group>();

		for (String id : user.getGroupIds()) {
			groupList.add(this.groupService.getGroupById(Integer.valueOf(id)));
		}

		updUser.setGroups(groupList);

		this.userDao.update(updUser);
		return true;
	}

	/**
	 * Update user profile
	 */
	@Override
	public boolean updateUserProfile(User user) {
		User updUser = this.userDao.get(user.getId());

		updUser.setDisplayName(user.getDisplayName());

		// Set the password only if it has changed
		if (!user.getPassword().equals(updUser.getPassword())) {
			updUser.setPassword(DigestUtils.md5Hex(user.getPassword()));
		}

		updUser.setEmail(user.getEmail());

		// Set the language in the locale manager
		if (!user.getLanguage().equals(updUser.getLanguage())) {
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

	/**
	 * Save the profile image
	 */
	@Override
	public boolean saveProfileImage(UploadedFile file) {
		File dataDir = new File(System.getProperty(BASE_DIR) + "/"
				+ PROFILE_IMAGE_DIR);

		String filename = FilenameUtils.getBaseName(file.getFileName());
		String extension = FilenameUtils.getExtension(file.getFileName());

		try {
			File tmpFile = File.createTempFile(filename + "-", "." + extension,
					dataDir);

			User currentUser = this.authManager.getCurrentUser();
			User updUser = this.userDao.get(currentUser.getId());
			updUser.setProfileImage(tmpFile.getName());

			this.userDao.update(updUser);

			// create an InputStream from the uploaded file
			InputStream inputStr = null;
			inputStr = file.getInputstream();
			FileUtils.copyInputStreamToFile(inputStr, tmpFile);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Get the profile image from user
	 */
	@Override
	public String getProfileImage(User user) {
		return this.userDao.get(user.getId()).getProfileImage();
	}

	/**
	 * Get the user info as JSON
	 */
	@Override
	public String getUserInfoAsJson() {

		JsonGeneratorFactory factory = Json.createGeneratorFactory(null);
		StringWriter writer = new StringWriter();
		JsonGenerator gen = factory.createGenerator(writer);

		gen.writeStartArray();

		User user = this.authManager.getCurrentUser();

		String displayName = "";
		String email = "";
		String profileImage = "";

		if (user.getDisplayName() != null) {
			displayName = user.getDisplayName();
		}

		if (user.getEmail() != null) {
			email = user.getEmail();
		}

		if (user.getProfileImage() != null) {
			profileImage = user.getProfileImage();
		}

		// Get the used disk space in bytes
		long usedDiskSpace = this.fileService.getUsedDiskSpaceByCurrentUser();

		gen.writeStartObject().write("id", user.getId())
				.write("username", user.getUsername())
				.write("displayName", displayName).write("email", email)
				.write("language", user.getLanguage())
				.write("diskQuota", user.getDiskQuota())
				.write("usedDiskSpace", usedDiskSpace)
				.write("profileImage", profileImage)
				.write("dateCreated", user.getDateCreated() + "").writeEnd();

		gen.writeEnd().flush();

		return writer.toString();
	}

	/**
	 * Convert GB to bytes
	 * 
	 * @param gb
	 * @return
	 */
	private long getBytesFormGB(long gb) {
		return gb * 1024 * 1024 * 1024;
	}

	/**
	 * Set the last login time in the current user
	 */
	@Override
	public void setLastLoginTime() {
		User user = this.authManager.getCurrentUser();

		user.setLastLogin(DateUtil.getSQLTimestamp());
		this.userDao.update(user);
	}
}
