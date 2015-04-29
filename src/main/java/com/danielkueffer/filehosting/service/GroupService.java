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
}
