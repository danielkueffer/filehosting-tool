package com.danielkueffer.filehosting.service;

import com.danielkueffer.filehosting.configuration.FormConfiguration;

/**
 * The configuration service
 * 
 * @author dkueffer
 * 
 */
public interface ConfigurationService {
	FormConfiguration getConfiguration();
	
	void updateConfiguration(FormConfiguration formConfiguration);
}
