package com.danielkueffer.filehosting.persistence.dao;

import java.util.List;

import com.danielkueffer.filehosting.persistence.model.Group;

/**
 * The group DAO
 * 
 * @author dkueffer
 * 
 */
public interface GroupDao extends Dao<Group> {
	List<Group> getAdminGroup();
}
