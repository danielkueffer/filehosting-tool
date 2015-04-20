package com.danielkueffer.filehosting.persistence.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * The abstract DAO
 * 
 * This DAO is extended by all other DAO implementations and provides all common
 * database operations
 * 
 * @author dkueffer
 * 
 * @param <T>
 */
public abstract class AbstractDao<T extends Object> {

	@PersistenceContext
	private EntityManager entityManager;

	private Class<T> domainClass;

	/**
	 * Get the entity manager
	 * 
	 * @return
	 */
	protected EntityManager getEm() {
		return this.entityManager;
	}

	/**
	 * Get the class
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Class<T> getDomainClass() {
		if (domainClass == null) {
			ParameterizedType thisType = (ParameterizedType) getClass()
					.getGenericSuperclass();
			this.domainClass = (Class<T>) thisType.getActualTypeArguments()[0];
		}

		return domainClass;
	}

	/**
	 * Get the class name
	 * 
	 * @return
	 */
	private String getDomainClassName() {
		return getDomainClass().getName();
	}

	/**
	 * Create a entry
	 * 
	 * @param t
	 */
	public void create(T t) {
		this.entityManager.persist(t);
	}

	/**
	 * Get entry
	 * 
	 * @param id
	 * @return
	 */
	public T get(Serializable id) {
		return (T) this.entityManager.find(getDomainClass(), id);
	}

	/**
	 * Get all addresses
	 * 
	 * @return
	 */
	public List<T> getAll() {
		return this.entityManager.createQuery(
				"select t from " + this.getDomainClassName() + " t",
				this.getDomainClass()).getResultList();
	}

	/**
	 * Update entry
	 * 
	 * @param t
	 * @return
	 */
	public void update(T t) {
		entityManager.merge(t);
	}

	/**
	 * Delete entry
	 * 
	 * @param t
	 */
	public void delete(T t) {
		entityManager.remove(t);
	}

	/**
	 * Delete a entry by id
	 * 
	 * @param id
	 */
	public void deleteById(Serializable id) {
		delete(get(id));
	}

	/**
	 * Count rows
	 * 
	 * @return
	 */
	public long count() {
		return (Long) this.entityManager.createQuery(
				"select count(*) from " + getDomainClassName() + "t",
				this.getDomainClass()).getSingleResult();
	}

	/**
	 * Check if entry exists
	 * 
	 * @param id
	 * @return
	 */
	public boolean exists(Serializable id) {
		return (get(id) != null);
	}
}
