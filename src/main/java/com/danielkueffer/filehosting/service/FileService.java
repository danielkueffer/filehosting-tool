package com.danielkueffer.filehosting.service;

import java.io.File;
import java.util.List;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;

/**
 * The file service
 * 
 * @author dkueffer
 * 
 */
public interface FileService {
	boolean uploadFiles(List<InputPart> inputParts, int parent,
			String fileName, long lastModified);

	String getFilesFromCurrentUser();

	String getFilesFromCurrentUser(int parent);

	File getDownloadFile(String filePath);

	boolean deleteFile(String filePath, boolean fromClient);

	boolean createFolder(String folder, int parent);

	boolean updateFileName(String fileName, int id);

	String getUsedDiskSpaceByCurrentUserAsString();

	long getUsedDiskSpaceByCurrentUser();

	String getDeletedFilesFromCurrentUser();

	boolean updateDeletedFiles();
}
