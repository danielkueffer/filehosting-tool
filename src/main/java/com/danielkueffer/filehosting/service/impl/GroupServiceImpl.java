package com.danielkueffer.filehosting.service.impl;

import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.danielkueffer.filehosting.auth.AuthManager;
import com.danielkueffer.filehosting.i18n.CurrentLocale;
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
	
	@Inject
	AuthManager authManager;
	
	@Inject
	@CurrentLocale
	Locale locale;

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

	/**
	 * Get a group by id
	 */
	@Override
	public Group getGroupById(int id) {
		return this.groupDao.get(id);
	}

	/**
	 * Update a group
	 */
	@Override
	public void updateGroup(Group group) {
		Group updGroup = this.groupDao.get(group.getId());
		updGroup.setIsAdmin(0);
		updGroup.setTitle(group.getTitle());
		updGroup.setLastUpdated(DateUtil.getSQLTimestamp());
		updGroup.setUpdatedUser(this.authManager.getCurrentUser().getUsername());

		this.groupDao.update(updGroup);
	}
}
