package com.danielkueffer.filehosting.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.danielkueffer.filehosting.auth.AuthManager;
import com.danielkueffer.filehosting.configuration.ConfNames;
import com.danielkueffer.filehosting.configuration.ConfValues;
import com.danielkueffer.filehosting.configuration.FormConfiguration;
import com.danielkueffer.filehosting.persistence.dao.ConfigurationDao;
import com.danielkueffer.filehosting.persistence.model.Configuration;
import com.danielkueffer.filehosting.service.ConfigurationService;
import com.danielkueffer.filehosting.util.ConfUtil;

/**
 * The configuration service implementation
 * 
 * @author dkueffer
 * 
 */
@Stateless
public class ConfigurationServiceImpl implements ConfigurationService {

	@EJB
	ConfigurationDao configurationDao;

	@Inject
	AuthManager authManager;

	/**
	 * Get the configuration
	 */
	@Override
	public FormConfiguration getConfiguration() {
		List<Configuration> confList = this.configurationDao.getAll();

		FormConfiguration formConfiguration = new FormConfiguration();

		if (confList.size() > 0) {
			for (Configuration conf : confList) {

				// File configuration
				if (conf.getConfName().equals(ConfNames.fileconf.toString())) {
					Map<String, String> fileConfMap = ConfUtil
							.getMapFromString(conf.getConfValue());
					
					formConfiguration.setMaxUploadSize(Integer
							.valueOf(fileConfMap.get(ConfValues.maxUploadSize
									.toString())));

					formConfiguration.setDeletedRestoreTime(Integer
							.valueOf(fileConfMap
									.get(ConfValues.deletedRestoreTime
											.toString())));
				}
			}
		}

		return formConfiguration;
	}

	/**
	 * Update the configuration
	 */
	@Override
	public void updateConfiguration(FormConfiguration formConfiguration) {

		// File configuration
		Configuration fileConfiguration = this.configurationDao
				.findByConfigurationName(ConfNames.fileconf.toString());

		String fileConfString = this
				.createFileConfigurationString(formConfiguration);

		if (fileConfiguration == null) {
			Configuration conf = new Configuration();
			conf.setConfName(ConfNames.fileconf.toString());
			conf.setConfValue(fileConfString);
			conf.setUpdatedUser(this.authManager.getCurrentUser().getUsername());

			this.configurationDao.create(conf);
		} else {
			fileConfiguration.setConfValue(fileConfString);
			fileConfiguration.setUpdatedUser(this.authManager.getCurrentUser()
					.getUsername());

			this.configurationDao.update(fileConfiguration);
		}

	}

	/**
	 * Create the file configuration string
	 * 
	 * @param formConf
	 * @return
	 */
	private String createFileConfigurationString(FormConfiguration formConf) {

		int maxUploadSize = formConf.getMaxUploadSize();

		// Convert max upload size to bytes
		maxUploadSize = maxUploadSize * 1024 * 1024;

		Map<String, String> confMap = new LinkedHashMap<String, String>();
		confMap.put(ConfValues.maxUploadSize.toString(), maxUploadSize + "");
		confMap.put(ConfValues.deletedRestoreTime.toString(),
				formConf.getDeletedRestoreTime() + "");

		return confMap.entrySet().toString();
	}

}
