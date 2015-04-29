package com.danielkueffer.filehosting.persistence.dao.impl;

import javax.ejb.Stateless;

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

}
