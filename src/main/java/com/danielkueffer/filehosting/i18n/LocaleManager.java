package com.danielkueffer.filehosting.i18n;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * The locale manager
 * 
 * @author dkueffer
 * 
 */
@Named
@SessionScoped
public class LocaleManager implements Serializable {

	private static final long serialVersionUID = -1923457167900152264L;

	private Locale locale;

	@PostConstruct
	public void init() {
		locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
	}

	/**
	 * Get the locale
	 * 
	 * @return
	 */
	@Produces
	@CurrentLocale
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Get the language
	 * 
	 * @return
	 */
	public String getLanguage() {
		return locale.getLanguage();
	}

	/**
	 * Set the language
	 * 
	 * @param language
	 */
	public void setLanguage(String language) {
		locale = new Locale(language);
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
	}

	/**
	 * Get the supported languages
	 * 
	 * @return
	 */
	public List<String> getSupportedLanguages() {
		List<String> langList = new ArrayList<String>();

		for (Languages l : Languages.values()) {
			langList.add(l.toString());
		}

		return langList;
	}

}