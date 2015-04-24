package com.danielkueffer.filehosting.controller;

import java.io.Serializable;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.danielkueffer.filehosting.i18n.CurrentLocale;

/**
 * The locale controller Used to change the language in the FacesContext
 * 
 * @author dkueffer
 * 
 */
@ManagedBean
@SessionScoped
public class LocaleController implements Serializable {

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

}