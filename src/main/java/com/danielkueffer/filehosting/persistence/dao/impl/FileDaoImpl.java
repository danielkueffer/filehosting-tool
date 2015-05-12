package com.danielkueffer.filehosting.persistence.dao.impl;

import javax.ejb.Stateless;

import com.danielkueffer.filehosting.persistence.dao.AbstractDao;
import com.danielkueffer.filehosting.persistence.dao.FileDao;
import com.danielkueffer.filehosting.persistence.model.File;

/**
 * The file DAO implementation
 * 
 * @author dkueffer
 * 
 */
@Stateless
public class FileDaoImpl extends AbstractDao<File> implements FileDao {

}
