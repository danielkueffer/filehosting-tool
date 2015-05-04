package com.danielkueffer.filehosting.service;

import java.util.List;

import com.danielkueffer.filehosting.persistence.model.Group;

/**
 * The group service
 * 
 * @author dkueffer
 * 
 */
public interface GroupService {
	List<Group> getAllGroups();
	
	void addGroup(Group group);
	
	Group getGroupById(int id);
	
	boolean updateGroup(Group group);
	
	boolean deleteGroup(int id);
}
