package com.danielkueffer.filehosting.persistence.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.danielkueffer.filehosting.persistence.dao.AbstractDao;
import com.danielkueffer.filehosting.persistence.dao.GroupDao;
import com.danielkueffer.filehosting.persistence.model.Group;

/**
 * The group DAO implementations
 * 
 * @author dkueffer
 * 
 */
@Stateless
public class GroupDaoImpl extends AbstractDao<Group> implements GroupDao {

	/**
	 * Get the Administrator group
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Group> getAdminGroup() {
		Query q = this
				.getEm()
				.createQuery(
						"SELECT g FROM Group g WHERE g.isAdmin = 1");

		return q.getResultList();
	}

}
