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
	public String uploadFiles(List<InputPart> inputParts) {

		// Check if a directory for the current user exists
		this.createUserDir();

		for (InputPart inputPart : inputParts) {

			try {

				MultivaluedMap<String, String> header = inputPart.getHeaders();
				String fileName = getFileName(header);

				InputStream inputStream = inputPart.getBody(InputStream.class,
						null);

				byte[] bytes = IOUtils.toByteArray(inputStream);

				// constructs upload file path
				String filePath = System.getProperty(BASE_DIR) + "/" + FILE_DIR
						+ "/" + this.authManager.getCurrentUser().getUsername()
						+ "/" + fileName;

				FileUtil.writeFile(bytes, filePath);

				// Get the MIME type
				Path path = Paths.get(filePath);
				String type = Files.probeContentType(path);

				// Get the file size
				long size = Files.size(path);

				// Check if the file already exists in the database
				List<UploadFile> fileList = this.fileDao.getSingleFileByUser(
						fileName, this.authManager.getCurrentUser());

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
					uf.setPath(fileName);
					uf.setParrent(0);
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
	public String getFilesFormCurrentUser() {
		User currentUser = this.authManager.getCurrentUser();

		ResourceBundle bundle = ResourceBundle.getBundle(
				"com.danielkueffer.filehosting.i18n.messages", new Locale(
						currentUser.getLanguage()));

		JsonGeneratorFactory factory = Json.createGeneratorFactory(null);
		StringWriter writer = new StringWriter();
		JsonGenerator gen = factory.createGenerator(writer);

		gen.writeStartArray();

		for (UploadFile uf : this.fileDao.getFilesByUser(currentUser)) {

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
			} else {
				typeLabel = bundle.getString("files.file");
				type = "file";
			}

			gen.writeStartObject().write("id", uf.getId())
					.write("path", uf.getPath())
					.write("parrent", uf.getParrent())
					.write("name", uf.getName()).write("type", type)
					.write("typeLabel", typeLabel)
					.write("thumbnail", thumbnail)
					.write("documentType", docType).write("size", uf.getSize())
					.write("lastModified", uf.getLastModified().toString())
					.writeEnd();
		}

		gen.writeEnd().flush();

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
				this.fileDao.delete(uf);

				return true;
			}
		}

		return false;
	}
}
