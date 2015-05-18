package com.danielkueffer.filehosting.service.impl;

import java.io.File;
import java.io.FileOutputStream;
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

				writeFile(bytes, filePath);

				// Get the MIME type
				Path path = Paths.get(filePath);
				String type = Files.probeContentType(path);

				// Get the file size
				long size = Files.size(path);

				UploadFile uf = new UploadFile();
				uf.setUser(this.authManager.getCurrentUser());
				uf.setPath(fileName);
				uf.setParrent(0);
				uf.setName(fileName);
				uf.setMimeType(type);
				uf.setSize(size);
				uf.setLastModified(DateUtil.getSQLTimestamp());

				this.fileDao.create(uf);

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
	 * Write a file to disk
	 * 
	 * @param content
	 * @param filename
	 * @throws IOException
	 */
	private void writeFile(byte[] content, String filename) throws IOException {
		File file = new File(filename);

		file.createNewFile();

		FileOutputStream fop = new FileOutputStream(file);

		fop.write(content);
		fop.flush();
		fop.close();
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

		for (UploadFile uf : this.fileDao.getAll()) {

			String type = "";
			String mime = uf.getMimeType();

			if (mime.startsWith("image")) {
				type = bundle.getString("files.image");
			} else if (mime.startsWith("audio")) {
				type = bundle.getString("files.audio");
			} else if (mime.startsWith("video")) {
				type = bundle.getString("files.video");
			} else if (Arrays.asList(FileUtil.documentTypes).contains(mime)) {
				type = bundle.getString("files.document");
			} else {
				type = bundle.getString("files.file");
			}

			gen.writeStartObject().write("id", uf.getId())
					.write("path", uf.getPath())
					.write("parrent", uf.getParrent())
					.write("name", uf.getName()).write("type", type)
					.write("size", uf.getSize())
					.write("lastModified", uf.getLastModified().toString())
					.writeEnd();
		}

		gen.writeEnd().flush();

		return writer.toString();
	}
}
