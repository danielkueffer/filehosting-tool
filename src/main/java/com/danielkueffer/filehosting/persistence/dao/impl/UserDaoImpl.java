package com.danielkueffer.filehosting.persistence.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.danielkueffer.filehosting.persistence.dao.AbstractDao;
import com.danielkueffer.filehosting.persistence.dao.UserDao;
import com.danielkueffer.filehosting.persistence.model.User;

/**
 * The user DAO implementation
 * @author dkueffer
 *
 */
@Stateless
public class UserDaoImpl extends AbstractDao<User> implements UserDao {

	/**
	 * Check if the login data is valid
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<User> login(String username, String password) {
		
		Query q = this
				.getEm()
				.createQuery(
						"SELECT u FROM User u WHERE u.username = :qUsername AND u.password = :uPassword");

		q.setParameter("qUsername", username);
		q.setParameter("uPassword", password);

		return q.getResultList();
	}

	/**
	 * Get a user by user name
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUserFromUsername(String username) {
		Query q = this
				.getEm()
				.createQuery(
						"SELECT u FROM User u WHERE u.username = :qUsername");

		q.setParameter("qUsername", username);

		return q.getResultList();
	}

}