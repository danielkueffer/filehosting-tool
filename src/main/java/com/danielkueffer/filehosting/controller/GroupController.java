package com.danielkueffer.filehosting.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.danielkueffer.filehosting.i18n.MessageProvider;
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

	@Inject
	MessageProvider messageProvider;

	Group group;

	private String message;

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
		boolean success = this.groupService.deleteGroup(this.group.getId());

		if (success) {
			return "/group/list.xhtml?faces-redirect=true";

		} else {
			message = this.messageProvider.getValue("groups.deletefail");

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, message, ""));

			return "/group/list.xhtml";
		}
	}

	/**
	 * @return the group
	 */
	public Group getGroup() {
		return group;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
}
