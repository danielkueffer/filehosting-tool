package com.danielkueffer.filehosting.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;

public class FileUtil {

	/**
	 * The document MIME types
	 */
	public static String[] documentTypes = new String[] {
			MimeType.doc.getContentType(), MimeType.docx.getContentType(),
			MimeType.odp.getContentType(), MimeType.ods.getContentType(),
			MimeType.odt.getContentType(), MimeType.pdf.getContentType(),
			MimeType.ppt.getContentType(), MimeType.pptx.getContentType(),
			MimeType.xls.getContentType(), MimeType.xlsx.getContentType() };

	/**
	 * Word document types
	 */
	public static String[] wordDocumentTypes = new String[] {
			MimeType.doc.getContentType(), MimeType.docx.getContentType(),
			MimeType.odt.getContentType() };

	/**
	 * Spreadsheet document types
	 */
	public static String[] spreadsheetDocumentTypes = new String[] {
			MimeType.xls.getContentType(), MimeType.xlsx.getContentType(),
			MimeType.ods.getContentType() };

	/**
	 * Presentations document types
	 */
	public static String[] presentationDocumentTypes = new String[] {
			MimeType.ppt.getContentType(), MimeType.pptx.getContentType(),
			MimeType.odt.getContentType() };

	/**
	 * Get a file from disk
	 * 
	 * @param path
	 * @return
	 */
	public static File getFile(String path) {
		FileSystem fs = FileSystems.getDefault();

		Path p = fs.getPath(path);

		return p.toFile();
	}

	/**
	 * Delete a file of directory from disk
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static boolean deleteFile(String path) {
		FileSystem fs = FileSystems.getDefault();

		Path p = fs.getPath(path);

		File f = p.toFile();

		if (f.isDirectory()) {
			try {
				FileUtils.deleteDirectory(f);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			return f.delete();
		}

		return false;
	}

	/**
	 * Write a file to disk
	 * 
	 * @param content
	 * @param filename
	 * @throws IOException
	 */
	public static File writeFile(InputStream inputStream, String filename,
			long lastModified) throws IOException {

		File file = new File(filename);
		file.createNewFile();

		OutputStream out = new FileOutputStream(file);

		byte[] bytes = new byte[4096];

		for (int len; (len = inputStream.read(bytes)) > 0;) {
			out.write(bytes, 0, len);
		}

		out.flush();
		out.close();

		if (lastModified > 0) {
			file.setLastModified(lastModified);
		}

		return file;
	}

	/**
	 * Add a directory to a ZIP archive
	 * 
	 * @param zos
	 * @param srcDir
	 */
	public static void addDirToArchive(ZipOutputStream zos, String path,
			File initialDir) {

		File srcDir = new File(path);
		File[] files = srcDir.listFiles();

		for (int i = 0; i < files.length; i++) {
			try {

				// Get the relative path
				String relativePath = initialDir.toURI()
						.relativize(files[i].toURI()).getPath();

				// Create file or folder
				zos.putNextEntry(new ZipEntry(relativePath));

				// Check if empty directory
				boolean emptyDir = files[i].isDirectory()
						&& files[i].length() == 0;

				// Write file or folder with content
				if (!emptyDir) {

					FileInputStream fis = new FileInputStream(files[i]);

					// create byte buffer
					byte[] buffer = new byte[1024];

					int length;

					// Write the content
					while ((length = fis.read(buffer)) > 0) {
						zos.write(buffer, 0, length);
					}

					fis.close();
				} else {
					// Write empty directory
					zos.write(new byte[0], 0, 0);
				}

				zos.closeEntry();

			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

			// if the file is directory, use recursion
			if (files[i].isDirectory()) {
				addDirToArchive(zos, files[i].getPath(), initialDir);
			}
		}
	}
}
