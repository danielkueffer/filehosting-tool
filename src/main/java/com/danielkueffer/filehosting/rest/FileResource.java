package com.danielkueffer.filehosting.rest;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.danielkueffer.filehosting.service.FileService;

/**
 * The file rest service
 * 
 * @author dkueffer
 * 
 */
@Path("file")
public class FileResource implements Serializable {

	private static final long serialVersionUID = -7978847135919069960L;

	@EJB
	FileService fileService;

	@POST
	@Path("upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String uploadFile(MultipartFormDataInput input) {
		return this.fileService.uploadFiles(input.getFormDataMap().get("file"));
	}
}
