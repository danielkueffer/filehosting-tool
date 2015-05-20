package com.danielkueffer.filehosting.rest;

import java.io.File;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

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

	/**
	 * Get all files from current user
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAll() {
		return this.fileService.getFilesFromCurrentUser();
	}

	/**
	 * Get all files from current user under the specified parrent directory
	 * 
	 * @param parrent
	 * @return
	 */
	@GET
	@Path("{parent}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllFromParrent(@PathParam("parent") int parent) {
		return this.fileService.getFilesFromCurrentUser(parent);
	}

	/**
	 * Upload and create a file
	 * 
	 * @param input
	 * @return
	 */
	@POST
	@Path("upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(MultipartFormDataInput input) {
		this.fileService.uploadFiles(input.getFormDataMap().get("file"));

		return Response.ok().build();
	}

	/**
	 * Delete a file
	 * 
	 * @param filePath
	 * @return
	 */
	@DELETE
	@Path("{filePath:.*}")
	public Response deleteFile(@PathParam("filePath") String filePath) {
		boolean deleted = this.fileService.deleteFile(filePath);

		if (!deleted) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		return Response.ok().build();
	}

	/**
	 * Download a file
	 * 
	 * @param filePath
	 * @return
	 */
	@GET
	@Path("download/{filePath:.*}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadFile(@PathParam("filePath") String filePath) {
		File file = this.fileService.getDownloadFile(filePath);

		if (file == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		ResponseBuilder rb = Response.ok(file);
		rb.header("Content-Disposition",
				"attachment; filename=" + file.getName());

		return rb.build();
	}

	/**
	 * Create a folder
	 * 
	 * @return
	 */
	@POST
	@Path("folder/add")
	public Response createFolder(@FormParam("folder") String folderName,
			@FormParam("parrent") int parrent) {

		this.fileService.createFolder(folderName, parrent);

		return Response.ok().build();
	}
}
