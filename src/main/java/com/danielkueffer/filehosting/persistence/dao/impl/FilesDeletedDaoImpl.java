package com.danielkueffer.filehosting.persistence.dao.impl;

import javax.ejb.Stateless;

import com.danielkueffer.filehosting.persistence.dao.AbstractDao;
import com.danielkueffer.filehosting.persistence.dao.FilesDeletedDao;
import com.danielkueffer.filehosting.persistence.model.FilesDeleted;

/**
 * The files deleted DAO implementation
 * 
 * @author dkueffer
 * 
 */
@Stateless
public class FilesDeletedDaoImpl extends AbstractDao<FilesDeleted> implements
		FilesDeletedDao {

}
