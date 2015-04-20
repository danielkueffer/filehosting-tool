package com.danielkueffer.filehosting.persistence.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Generic DAO interface
 * 
 * This interface is extended by all DAO interfaces
 * and provides methods for common database operations
 * @author dkueffer
 *
 * @param <T>
 */
public interface Dao<T extends Object> {
	
	void create(T t);
	
	T get(Serializable id);
	
	List<T> getAll();
	
	void update(T t);
	
	void delete(T t);
	
	void deleteById(Serializable id);
	
	long count();
	
	boolean exists(Serializable id);
}
