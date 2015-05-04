package com.danielkueffer.filehosting.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.danielkueffer.filehosting.persistence.model.Group;
import com.danielkueffer.filehosting.service.GroupService;

/**
 * The group controller
 * 
 * @author dkueffer
 * 
 */
@Named
@RequestScoped
public class GroupController {

	@EJB
	GroupService groupService;

	Group group;

	@PostConstruct
	public void init() {
		this.group = new Group();
	}

	/**
	 * Get all groups
	 * 
	 * @return
	 */
	public List<Group> getGroups() {
		return this.groupService.getAllGroups();
	}

	/**
	 * Add a new group
	 */
	public String addGroup() {
		this.groupService.addGroup(this.group);

		return "/group/list.xhtml?faces-redirect=true";
	}

	/**
	 * Set the group to be updated
	 */
	public void initUpdate() {
		this.group = this.groupService.getGroupById(this.group.getId());
	}

	/**
	 * Update a group
	 * 
	 * @return
	 */
	public String updateGroup() {
		this.groupService.updateGroup(this.group);

		return "/group/list.xhtml?faces-redirect=true";
	}

	/**
	 * Delete a group
	 * 
	 * @param id
	 */
	public String deleteGroup() {
		this.groupService.deleteGroup(this.group.getId());

		return "/group/list.xhtml?faces-redirect=true";
	}

	/**
	 * @return the group
	 */
	public Group getGroup() {
		return group;
	}
}
