package com.danielkueffer.filehosting.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * The configuration entity
 * 
 * @author dkueffer
 * 
 */
@Entity
@Table(name = "configuration")
public class Configuration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	@Column(name = "conf_name")
	private String confName;

	@NotNull
	@Column(name = "conf_value")
	private String confValue;

	@Column(name = "updated_user")
	private String updatedUser;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the confName
	 */
	public String getConfName() {
		return confName;
	}

	/**
	 * @param confName
	 *            the confName to set
	 */
	public void setConfName(String confName) {
		this.confName = confName;
	}

	/**
	 * @return the confValue
	 */
	public String getConfValue() {
		return confValue;
	}

	/**
	 * @param confValue
	 *            the confValue to set
	 */
	public void setConfValue(String confValue) {
		this.confValue = confValue;
	}

	/**
	 * @return the updatedUser
	 */
	public String getUpdatedUser() {
		return updatedUser;
	}

	/**
	 * @param updatedUser
	 *            the updatedUser to set
	 */
	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}
}
