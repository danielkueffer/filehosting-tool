package com.danielkueffer.filehosting.rest;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
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

	@Inject
	HttpServletRequest request;

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
	 * Get all files from current user under the specified parent directory
	 * 
	 * @param parrent
	 * @return
	 */
	@GET
	@Path("{parent}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllFromParent(@PathParam("parent") int parent) {
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
	@Consumes(MediaType.MULTIPART_FORM_DATA + "; charset=UTF-8")
	public Response uploadFile(MultipartFormDataInput input) {
		int parent = 0;
		String filename = "";
		boolean ieForm = false;

		try {
			parent = Integer.valueOf(input.getFormDataMap().get("parent")
					.get(0).getBodyAsString());

			filename = input.getFormDataMap().get("my-filename").get(0)
					.getBodyAsString();

			if (input.getFormDataMap().get("ie-form") != null) {
				ieForm = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.fileService.uploadFiles(input.getFormDataMap().get("file"),
				parent, filename);

		// Upload send by Internet Explorer below v.10. Redirect to the file
		// list
		if (ieForm) {
			try {
				URI uri = new URL(this.request.getScheme() + "://"
						+ this.request.getServerName() + ":"
						+ this.request.getServerPort()
						+ this.request.getContextPath()).toURI();

				return Response.temporaryRedirect(uri).build();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}

		return Response.ok().build();
	}

	/**
	 * Update the filename
	 * 
	 * @param fileName
	 * @return
	 */
	@POST
	@Path("update")
	public Response updateFileName(@FormParam("fileName") String fileName,
			@FormParam("id") int id) {

		this.fileService.updateFileName(fileName, id);

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
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
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
	@Produces(MediaType.APPLICATION_OCTET_STREAM + "; charset=UTF-8")
	public Response downloadFile(@PathParam("filePath") String filePath) {
		File file = this.fileService.getDownloadFile(filePath);

		if (file == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		ResponseBuilder rb = Response.ok(file).header("Content-Disposition",
				"attachment; filename=\"" + file.getName() + "\"");

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
			@FormParam("parent") int parrent) {

		this.fileService.createFolder(folderName, parrent);

		return Response.ok().build();
	}
}
