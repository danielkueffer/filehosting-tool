package com.danielkueffer.filehosting.persistence.dao;

import java.util.List;

import com.danielkueffer.filehosting.persistence.model.UploadFile;
import com.danielkueffer.filehosting.persistence.model.User;

/**
 * The file DAO
 * 
 * @author dkueffer
 * 
 */
public interface FileDao extends Dao<UploadFile> {
	List<UploadFile> getFilesByUser(User user);
	
	List<UploadFile> getSingleFileByUser(String filePath, User user);
}
