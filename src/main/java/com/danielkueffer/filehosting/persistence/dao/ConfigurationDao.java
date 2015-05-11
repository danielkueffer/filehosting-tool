package com.danielkueffer.filehosting.persistence.dao;

import com.danielkueffer.filehosting.persistence.model.Configuration;

/**
 * The configuration DAO
 * 
 * @author dkueffer
 * 
 */
public interface ConfigurationDao extends Dao<Configuration> {
	Configuration findByConfigurationName(String confName);
}
