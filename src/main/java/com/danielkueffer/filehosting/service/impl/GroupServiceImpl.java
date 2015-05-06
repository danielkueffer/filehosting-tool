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
	public boolean updateGroup(Group group) {

		// Administrator group can't be updated
		if (this.getAdminGroup().getId() != group.getId()) {
			Group updGroup = this.groupDao.get(group.getId());
			updGroup.setIsAdmin(0);
			updGroup.setTitle(group.getTitle());
			updGroup.setLastUpdated(DateUtil.getSQLTimestamp());
			updGroup.setUpdatedUser(this.authManager.getCurrentUser()
					.getUsername());

			this.groupDao.update(updGroup);

			return true;
		}

		return false;
	}

	/**
	 * Delete a group
	 */
	@Override
	public boolean deleteGroup(int id) {

		// Administrator group can't be deleted
		if (this.getAdminGroup().getId() != id) {

			// The group can't be deleted if she is used by users
			Group group = this.groupDao.get(id);
			
			if (!group.getMembers().isEmpty()) {
				return false;
			}

			this.groupDao.deleteById(id);

			return true;
		}

		return false;
	}

	/**
	 * Get the Administrator group
	 * 
	 * @return
	 */
	private Group getAdminGroup() {
		List<Group> groupList = this.groupDao.getAdminGroup();

		if (groupList.isEmpty()) {
			return null;
		} else {
			return groupList.get(0);
		}
	}
}
