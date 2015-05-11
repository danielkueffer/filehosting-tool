package com.danielkueffer.filehosting.configuration;

import javax.validation.constraints.NotNull;

/**
 * The form configuration bean
 * 
 * @author dkueffer
 * 
 */
public class FormConfiguration {
	
	@NotNull
	private int maxUploadSize;
	
	@NotNull
	private int deletedRestoreTime;
	
	/**
	 * @return the maxUploadSize
	 */
	public int getMaxUploadSize() {
		return maxUploadSize;
	}

	/**
	 * @param maxUploadSize
	 *            the maxUploadSize to set
	 */
	public void setMaxUploadSize(int maxUploadSize) {
		this.maxUploadSize = maxUploadSize;
	}

	/**
	 * @return the deletedRestoreTime
	 */
	public int getDeletedRestoreTime() {
		return deletedRestoreTime;
	}

	/**
	 * @param deletedRestoreTime the deletedRestoreTime to set
	 */
	public void setDeletedRestoreTime(int deletedRestoreTime) {
		this.deletedRestoreTime = deletedRestoreTime;
	}
}
