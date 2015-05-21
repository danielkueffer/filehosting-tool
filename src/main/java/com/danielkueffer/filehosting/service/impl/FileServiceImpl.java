package com.danielkueffer.filehosting.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import com.danielkueffer.filehosting.auth.AuthManager;
import com.danielkueffer.filehosting.i18n.MessageProvider;
import com.danielkueffer.filehosting.persistence.dao.FileDao;
import com.danielkueffer.filehosting.persistence.model.UploadFile;
import com.danielkueffer.filehosting.persistence.model.User;
import com.danielkueffer.filehosting.service.FileService;
import com.danielkueffer.filehosting.util.DateUtil;
import com.danielkueffer.filehosting.util.FileUtil;
import com.danielkueffer.filehosting.util.MimeType;

/**
 * The file service implementation
 * 
 * @author dkueffer
 * 
 */
@Stateless
public class FileServiceImpl implements FileService {

	private static final String BASE_DIR = "jboss.server.data.dir";
	private static final String FILE_DIR = "files";

	private String altFolder;
	private int altNum;

	@EJB
	FileDao fileDao;

	@Inject
	AuthManager authManager;

	@Inject
	MessageProvider messageProvider;

	/**
	 * Upload files
	 */
	@Override
	public String uploadFiles(List<InputPart> inputParts, int parent) {

		// Check if a directory for the current user exists
		this.createUserDir();

		for (InputPart inputPart : inputParts) {

			try {

				MultivaluedMap<String, String> header = inputPart.getHeaders();
				String fileName = getFileName(header);

				InputStream inputStream = inputPart.getBody(InputStream.class,
						null);

				byte[] bytes = IOUtils.toByteArray(inputStream);

				String filePath = fileName;

				if (parent != 0) {
					UploadFile uf = this.fileDao.get(parent);
					filePath = uf.getPath() + "/" + fileName;
				}

				// constructs upload file path
				String systemFilePath = System.getProperty(BASE_DIR) + "/"
						+ FILE_DIR + "/"
						+ this.authManager.getCurrentUser().getUsername() + "/"
						+ filePath;

				FileUtil.writeFile(bytes, systemFilePath);

				// Get the MIME type
				Path path = Paths.get(systemFilePath);
				String type = Files.probeContentType(path);

				// Get the file size
				long size = Files.size(path);

				// Check if the file already exists in the database
				List<UploadFile> fileList = this.fileDao.getSingleFileByUser(
						filePath, this.authManager.getCurrentUser());

				UploadFile uf = null;

				if (!fileList.isEmpty()) {

					// Update existing
					uf = fileList.get(0);

					uf.setSize(size);
					uf.setLastModified(DateUtil.getSQLTimestamp());

					this.fileDao.update(uf);
				} else {

					// Create new
					uf = new UploadFile();
					uf.setUser(this.authManager.getCurrentUser());
					uf.setPath(filePath);
					uf.setParent(parent);
					uf.setName(fileName);
					uf.setMimeType(type);
					uf.setSize(size);
					uf.setLastModified(DateUtil.getSQLTimestamp());

					this.fileDao.create(uf);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return "{\"success\": true, \"message\": \"created\"}";
	}

	/**
	 * Get the filename from the uploaded file
	 * 
	 * @param header
	 * @return
	 */
	private String getFileName(MultivaluedMap<String, String> header) {

		String[] contentDisposition = header.getFirst("Content-Disposition")
				.split(";");

		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {

				String[] name = filename.split("=");

				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}
		return "unknown";
	}

	/**
	 * Check if a directory for the current user exists or create one
	 * 
	 * @return
	 */
	private boolean createUserDir() {

		File f = new File(System.getProperty(BASE_DIR) + "/" + FILE_DIR + "/"
				+ this.authManager.getCurrentUser().getUsername());

		if (!f.isDirectory()) {
			return f.mkdirs();
		}

		return false;
	}

	/**
	 * Get the files from the current user
	 */
	@Override
	public String getFilesFromCurrentUser() {
		User currentUser = this.authManager.getCurrentUser();

		List<UploadFile> fileList = this.fileDao.getFilesByUser(currentUser);

		return this.getJsonFileList(fileList, currentUser, 0);
	}

	/**
	 * Get the files from the current user under the parent directory
	 * 
	 * @param parent
	 * @return
	 */
	@Override
	public String getFilesFromCurrentUser(int parent) {
		User currentUser = this.authManager.getCurrentUser();

		List<UploadFile> fileList = this.fileDao.getFilesByUser(currentUser,
				parent);

		return this.getJsonFileList(fileList, currentUser, parent);
	}

	/**
	 * Get a JSON string from the file list
	 * 
	 * @param fileList
	 * @return
	 */
	private String getJsonFileList(List<UploadFile> fileList, User currentUser,
			int parent) {

		ResourceBundle bundle = ResourceBundle.getBundle(
				"com.danielkueffer.filehosting.i18n.messages", new Locale(
						currentUser.getLanguage()));

		JsonGeneratorFactory factory = Json.createGeneratorFactory(null);
		StringWriter writer = new StringWriter();
		JsonGenerator gen = factory.createGenerator(writer);

		UploadFile parentFolder = this.fileDao.get(parent);

		String folderPath = "";

		if (parentFolder != null) {
			folderPath = parentFolder.getPath();
		}

		// Write the bread crumb array
		gen.writeStartObject().writeStartArray("breadcrumb").writeStartObject()
				.write("folderPath", folderPath).writeEnd().writeEnd()
				.writeStartArray("files");

		for (UploadFile uf : fileList) {

			String type = "";
			String typeLabel = "";
			String mime = uf.getMimeType();
			String docType = "";
			String thumbnail = "";

			// Check the MIME type and assign values
			if (mime.startsWith("image")) {
				typeLabel = bundle.getString("files.image");
				type = "image";

				// Create thumbnail

			} else if (mime.startsWith("audio")) {
				typeLabel = bundle.getString("files.audio");
				type = "audio";
			} else if (mime.startsWith("video")) {
				typeLabel = bundle.getString("files.video");
				type = "video";
			} else if (Arrays.asList(FileUtil.documentTypes).contains(mime)) {
				typeLabel = bundle.getString("files.document");
				type = "document";

				if (Arrays.asList(FileUtil.wordDocumentTypes).contains(mime)) {
					docType = "doc";
				} else if (Arrays.asList(FileUtil.presentationDocumentTypes)
						.contains(mime)) {
					docType = "presentation";
				} else if (Arrays.asList(FileUtil.spreadsheetDocumentTypes)
						.contains(mime)) {
					docType = "spreadsheet";
				} else if (mime.equals(MimeType.pdf.getContentType())) {
					docType = "pdf";
				}
			} else if (mime.equals("folder")) {
				typeLabel = bundle.getString("files.folder");
				type = "folder";
			} else {
				typeLabel = bundle.getString("files.file");
				type = "file";
			}

			// Write the uploadFile object
			gen.writeStartObject().write("id", uf.getId())
					.write("path", uf.getPath())
					.write("parent", uf.getParent())
					.write("name", uf.getName()).write("type", type)
					.write("typeLabel", typeLabel)
					.write("thumbnail", thumbnail)
					.write("documentType", docType).write("size", uf.getSize())
					.write("lastModified", uf.getLastModified().toString())
					.writeEnd();
		}

		gen.writeEnd().writeEnd().flush();

		return writer.toString();
	}

	/**
	 * Get a file to download
	 */
	@Override
	public File getDownloadFile(String filePath) {
		List<UploadFile> fileList = this.fileDao.getSingleFileByUser(filePath,
				this.authManager.getCurrentUser());

		UploadFile uf = null;

		if (!fileList.isEmpty()) {
			uf = fileList.get(0);

			String path = System.getProperty(BASE_DIR) + "/" + FILE_DIR + "/"
					+ this.authManager.getCurrentUser().getUsername() + "/"
					+ uf.getPath();

			return FileUtil.getFile(path);
		}

		return null;
	}

	/**
	 * Delete a file
	 */
	@Override
	public boolean deleteFile(String filePath) {
		List<UploadFile> fileList = this.fileDao.getSingleFileByUser(filePath,
				this.authManager.getCurrentUser());

		UploadFile uf = null;

		if (!fileList.isEmpty()) {
			uf = fileList.get(0);

			String path = System.getProperty(BASE_DIR) + "/" + FILE_DIR + "/"
					+ this.authManager.getCurrentUser().getUsername() + "/"
					+ uf.getPath();

			// Delete the file form the file system and from the database
			if (FileUtil.deleteFile(path)) {

				if (uf.getMimeType().equals("folder")) {
					this.deleteFolderContents(uf.getId());
				}

				// Delete the file or folder on disk
				this.fileDao.delete(uf);

				return true;
			}
		}

		return false;
	}

	/**
	 * Create a folder
	 */
	@Override
	public boolean createFolder(String folder, int parent) {

		User currentUser = this.authManager.getCurrentUser();

		UploadFile parentFile = null;
		int qParent = 0;

		String folderPath = folder;

		// Create the path
		if (parent != 0) {
			parentFile = this.fileDao.get(parent);
			qParent = parentFile.getId();
			folderPath = parentFile.getPath() + "/" + folderPath;
		}

		List<UploadFile> fileList = this.fileDao.getSingleFileByUserAndParent(
				folderPath, currentUser, qParent);

		// Check if the folder name already exists and append a number to the
		// folder name
		if (!fileList.isEmpty()) {
			this.checkFolderDuplicateName(folderPath, currentUser, 1);
			folderPath = this.altFolder;
			folder = folder + " (" + this.altNum + ")";
		}

		// Create a file from the system path
		File f = new File(System.getProperty(BASE_DIR) + "/" + FILE_DIR + "/"
				+ this.authManager.getCurrentUser().getUsername() + "/"
				+ folderPath);

		// Create the folder
		f.mkdirs();

		// Get the MIME type
		Path path = Paths.get(f.toURI());

		// Get the file size
		long size = 0;
		try {
			size = Files.size(path);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create the uploadFile object
		UploadFile uf = new UploadFile();
		uf.setUser(this.authManager.getCurrentUser());
		uf.setPath(folderPath);
		uf.setParent(parent);
		uf.setName(folder);
		uf.setMimeType("folder");
		uf.setSize(size);
		uf.setLastModified(DateUtil.getSQLTimestamp());

		// Save the uploadFile
		this.fileDao.create(uf);

		return true;
	}

	/**
	 * Delete the folder content in the database
	 * 
	 * @param id
	 */
	private void deleteFolderContents(int id) {
		if (this.fileDao.get(id) != null) {
			List<UploadFile> fileList = this.fileDao.getFilesByParent(id);

			for (UploadFile uf : fileList) {

				if (uf.getMimeType().equals("folder")) {
					this.deleteFolderContents(uf.getId());
				}

				this.fileDao.delete(uf);
			}
		}
	}

	/**
	 * Check for duplicate folder and assign alternative name
	 * 
	 * @param folderName
	 * @param currentUser
	 */
	private void checkFolderDuplicateName(String folderName, User currentUser,
			int startNum) {

		this.altFolder = folderName + " (" + startNum + ")";
		this.altNum = startNum;

		if (!this.fileDao.getSingleFileByUser(this.altFolder, currentUser)
				.isEmpty()) {
			this.checkFolderDuplicateName(folderName, currentUser, startNum + 1);
		}
	}
}
