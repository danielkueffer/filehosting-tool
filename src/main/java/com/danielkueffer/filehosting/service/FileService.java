package com.danielkueffer.filehosting.service;

import java.util.List;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;

/**
 * The file service
 * 
 * @author dkueffer
 * 
 */
public interface FileService {
	String uploadFiles(List<InputPart> inputParts);
}
