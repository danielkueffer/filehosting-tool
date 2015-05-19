package com.danielkueffer.filehosting.persistence.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.danielkueffer.filehosting.persistence.dao.AbstractDao;
import com.danielkueffer.filehosting.persistence.dao.FileDao;
import com.danielkueffer.filehosting.persistence.model.UploadFile;
import com.danielkueffer.filehosting.persistence.model.User;

/**
 * The file DAO implementation
 * 
 * @author dkueffer
 * 
 */
@Stateless
public class FileDaoImpl extends AbstractDao<UploadFile> implements FileDao {

	/**
	 * Get files by user
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UploadFile> getFilesByUser(User user) {
		Query q = this.getEm().createQuery(
				"SELECT u FROM UploadFile u WHERE u.user = :qUser");

		q.setParameter("qUser", user);

		return q.getResultList();
	}

	/**
	 * Get a single file by user
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UploadFile> getSingleFileByUser(String filePath, User user) {
		Query q = this
				.getEm()
				.createQuery(
						"SELECT u FROM UploadFile u WHERE u.user = :qUser AND u.path = :qPath");

		q.setParameter("qUser", user);
		q.setParameter("qPath", filePath);

		return q.getResultList();
	}

}
