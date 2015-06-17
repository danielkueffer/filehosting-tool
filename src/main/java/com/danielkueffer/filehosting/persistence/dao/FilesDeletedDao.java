package com.danielkueffer.filehosting.persistence.dao;

import java.util.List;

import com.danielkueffer.filehosting.persistence.model.FilesDeleted;
import com.danielkueffer.filehosting.persistence.model.User;

/**
 * The files deleted DAO
 * 
 * @author dkueffer
 * 
 */
public interface FilesDeletedDao extends Dao<FilesDeleted> {
	List<FilesDeleted> getFilesDeletedByUser(User user);
}
