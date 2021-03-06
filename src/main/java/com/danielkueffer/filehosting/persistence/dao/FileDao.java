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
	
	List<UploadFile> getFilesByUser(User user, int parent);
	
	List<UploadFile> getSingleFileByUser(String filePath, User user);
	
	List<UploadFile> getSingleFileByUserAndParent(String filePath, User user, int parent);
	
	List<UploadFile> getFilesByParent(int parent);
}
