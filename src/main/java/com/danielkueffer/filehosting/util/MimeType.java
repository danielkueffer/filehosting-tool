package com.danielkueffer.filehosting.util;

/**
 * MIME Types
 * 
 * @author dkueffer
 * 
 */
public enum MimeType {
	docx("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
	doc("application/msword"),
	odt("application/vnd.oasis.opendocument.text "),
	xls("application/vnd.ms-excel"),
	xlsx("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
	ods("application/vnd.oasis.opendocument.spreadsheet"),
	ppt("application/vnd.ms-powerpoint"),
	pptx("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
	odp("application/vnd.oasis.opendocument.presentation"),
	pdf("application/pdf");

	private String contentType;

	private MimeType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}
}
