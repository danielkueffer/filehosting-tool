package com.danielkueffer.filehosting.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.danielkueffer.filehosting.i18n.LocaleManager;
import com.danielkueffer.filehosting.persistence.model.Group;
import com.danielkueffer.filehosting.persistence.model.User;
import com.danielkueffer.filehosting.service.GroupService;
import com.danielkueffer.filehosting.service.UserService;

/**
 * The user controller
 * 
 * @author dkueffer
 * 
 */
@Named
@RequestScoped
public class UserController {

	@EJB
	UserService userService;

	@EJB
	GroupService groupService;

	@Inject
	LocaleManager localeManager;

	private User user;

	private List<String> languages;

	private List<Group> groups;

	@PostConstruct
	public void init() {
		this.user = new User();

		this.languages = this.localeManager.getSupportedLanguages();
		this.groups = this.groupService.getAllGroups();
	}

	/**
	 * Get all users
	 * 
	 * @return
	 */
	public List<User> getUsers() {
		return this.userService.getAllUsers();
	}

	/**
	 * Add a new user
	 * 
	 * @return
	 */
	public String addUser() {
		this.userService.addUser(this.user);

		return "/user/list.xhtml?faces-redirect=true";
	}

	/**
	 * Delete a user
	 * 
	 * @return
	 */
	public String deleteUser() {
		this.userService.deleteUser(this.user.getId());

		return "/user/list.xhtml?faces-redirect=true";
	}

	/**
	 * Set the user to be updated
	 */
	public void initUpdate() {
		this.user = this.userService.getUserById(user.getId());
	}

	/**
	 * Update a user
	 * 
	 * @return
	 */
	public String updateUser() {
		this.userService.updateUser(this.user);
		
		return "/user/list.xhtml?faces-redirect=true";
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @return the languages
	 */
	public List<String> getLanguages() {
		return languages;
	}

	/**
	 * @return the groups
	 */
	public List<Group> getGroups() {
		return groups;
	}
}
