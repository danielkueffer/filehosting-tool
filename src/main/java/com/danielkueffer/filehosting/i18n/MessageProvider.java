package com.danielkueffer.filehosting.i18n;

import java.io.Serializable;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

/**
 * Get a value from a resouce bundle
 * 
 * @author dkueffer
 * 
 */
public class MessageProvider implements Serializable {

	private static final long serialVersionUID = 1L;

	private ResourceBundle bundle;

	/**
	 * Get the resource bundle
	 * 
	 * @return
	 */
	public ResourceBundle getBundle() {
		if (bundle == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			bundle = context.getApplication().getResourceBundle(context, "msg");
		}

		return bundle;
	}

	/**
	 * Get a string by key from the message bundle
	 * 
	 * @param key
	 * @return
	 */
	public String getValue(String key) {

		String result = null;
		try {
			result = getBundle().getString(key);
		} catch (MissingResourceException e) {
			result = "???" + key + "??? not found";
		}
		return result;
	}
}
