package com.danielkueffer.filehosting.service.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.danielkueffer.filehosting.persistence.dao.GroupDao;
import com.danielkueffer.filehosting.persistence.model.Group;
import com.danielkueffer.filehosting.service.GroupService;

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
}
