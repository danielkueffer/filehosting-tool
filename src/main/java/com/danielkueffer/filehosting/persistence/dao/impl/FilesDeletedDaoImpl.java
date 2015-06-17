package com.danielkueffer.filehosting.persistence.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.danielkueffer.filehosting.persistence.dao.AbstractDao;
import com.danielkueffer.filehosting.persistence.dao.FilesDeletedDao;
import com.danielkueffer.filehosting.persistence.model.FilesDeleted;
import com.danielkueffer.filehosting.persistence.model.User;

/**
 * The files deleted DAO implementation
 * 
 * @author dkueffer
 * 
 */
@Stateless
public class FilesDeletedDaoImpl extends AbstractDao<FilesDeleted> implements
		FilesDeletedDao {

	/**
	 * Get files deleted by user
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FilesDeleted> getFilesDeletedByUser(User user) {
		Query q = this.getEm().createQuery(
				"SELECT f FROM FilesDeleted f WHERE f.user = :qUser");

		q.setParameter("qUser", user);

		return q.getResultList();
	}
}
