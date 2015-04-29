package com.danielkueffer.filehosting.controller;

import java.util.List;

import javax.ejb.EJB;
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
public class GroupController {

	@EJB
	GroupService groupService;
	
	/**
	 * Get all groups
	 * @return
	 */
	public List<Group> getGroups() {
		return this.groupService.getAllGroups();
	}
}
