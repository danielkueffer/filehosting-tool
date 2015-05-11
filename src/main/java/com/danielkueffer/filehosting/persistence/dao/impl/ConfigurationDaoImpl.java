package com.danielkueffer.filehosting.persistence.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.danielkueffer.filehosting.persistence.dao.AbstractDao;
import com.danielkueffer.filehosting.persistence.dao.ConfigurationDao;
import com.danielkueffer.filehosting.persistence.model.Configuration;

/**
 * The configuration DAO implementation
 * 
 * @author dkueffer
 * 
 */
@Stateless
public class ConfigurationDaoImpl extends AbstractDao<Configuration> implements
		ConfigurationDao {

	/**
	 * Find a configuration by a configuration name
	 */
	@Override
	public Configuration findByConfigurationName(String confName) {

		Query q = this.getEm().createQuery(
				"SELECT c FROM Configuration c WHERE c.confName = :qConfName");
		
		@SuppressWarnings("unchecked")
		List<Configuration> confList = q.setParameter("qConfName", confName)
				.getResultList();

		Configuration conf = null;

		for (Configuration c : confList) {
			conf = c;
		}

		return conf;
	}

}
