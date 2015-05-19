package com.danielkueffer.filehosting.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

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
	 * Delete a file from disk
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteFile(String path) {
		FileSystem fs = FileSystems.getDefault();

		Path p = fs.getPath(path);

		return p.toFile().delete();
	}

	/**
	 * Write a file to disk
	 * 
	 * @param content
	 * @param filename
	 * @throws IOException
	 */
	public static void writeFile(byte[] content, String filename)
			throws IOException {
		File file = new File(filename);

		file.createNewFile();

		FileOutputStream fop = new FileOutputStream(file);

		fop.write(content);
		fop.flush();
		fop.close();
	}
}
