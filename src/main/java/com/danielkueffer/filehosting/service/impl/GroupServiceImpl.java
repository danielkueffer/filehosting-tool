package com.danielkueffer.filehosting.service.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.danielkueffer.filehosting.persistence.dao.GroupDao;
import com.danielkueffer.filehosting.persistence.model.Group;
import com.danielkueffer.filehosting.service.GroupService;
import com.danielkueffer.filehosting.util.DateUtil;

/**
 * The group service implementation
 * 
 * @author dkueffer
 * 
 */
@Stateless
public class GroupServiceImpl implements GroupService {

	@EJB
	GroupDao groupDao;

	/**
	 * Get all groups
	 */
	@Override
	public List<Group> getAllGroups() {
		return this.groupDao.getAll();
	}

	/**
	 * Add a group
	 */
	@Override
	public void addGroup(Group group) {
		group.setDateCreated(DateUtil.getSQLTimestamp());
		
		this.groupDao.create(group);
	}
}
