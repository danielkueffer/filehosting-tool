package com.danielkueffer.filehosting.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.danielkueffer.filehosting.auth.AuthManager;
import com.danielkueffer.filehosting.configuration.FormConfiguration;
import com.danielkueffer.filehosting.service.ConfigurationService;

/**
 * The configuration controller
 * 
 * @author dkueffer
 * 
 */
@Named
@RequestScoped
public class ConfigurationController {

	@EJB
	ConfigurationService configurationService;
	
	@Inject
	AuthManager authManager;

	private FormConfiguration formConfiguration;

	@PostConstruct
	public void init() {
		this.formConfiguration = this.configurationService.getConfiguration();
		
		if (this.formConfiguration.getMaxUploadSize() == 0) {
			this.formConfiguration.setMaxUploadSize(1024);
		}
		
		if (this.formConfiguration.getDeletedRestoreTime() == 0) {
			this.formConfiguration.setDeletedRestoreTime(24);
		}
	}

	/**
	 * Update the configuration
	 * 
	 * @return
	 */
	public String updateConfiguration() {
		if (this.authManager.getCurrentUser().isAdmin()) {
			this.configurationService.updateConfiguration(this.formConfiguration);
		}
		
		return "/settings.xhtml?faces-redirect=true";
	}

	/**
	 * @return the formConfiguration
	 */
	public FormConfiguration getFormConfiguration() {
		return formConfiguration;
	}
}
