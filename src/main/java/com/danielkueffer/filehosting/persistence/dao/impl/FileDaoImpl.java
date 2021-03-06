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
	 * Get files by user under the parent directory
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UploadFile> getFilesByUser(User user, int parent) {
		Query q = this
				.getEm()
				.createQuery(
						"SELECT u FROM UploadFile u WHERE u.user = :qUser AND u.parent = :qParent");

		q.setParameter("qUser", user);
		q.setParameter("qParent", parent);

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

	/**
	 * Get a single file by user and parent
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UploadFile> getSingleFileByUserAndParent(String filePath,
			User user, int parent) {

		Query q = this
				.getEm()
				.createQuery(
						"SELECT u FROM UploadFile u WHERE u.user = :qUser AND u.path = :qPath AND u.parent = :qParent");

		q.setParameter("qUser", user);
		q.setParameter("qPath", filePath);
		q.setParameter("qParent", parent);

		return q.getResultList();
	}

	/**
	 * Get files by parent folder
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UploadFile> getFilesByParent(int parent) {
		Query q = this.getEm().createQuery(
				"SELECT u FROM UploadFile u WHERE u.parent = :qParent");

		q.setParameter("qParent", parent);

		return q.getResultList();
	}
}
