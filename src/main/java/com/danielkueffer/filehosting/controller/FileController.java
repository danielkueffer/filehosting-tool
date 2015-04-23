package com.danielkueffer.filehosting.controller;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import com.danielkueffer.filehosting.i18n.CurrentLocale;

/**
 * The file controller
 * 
 * @author dkueffer
 * 
 */
@Named
public class FileController {
	
	@Inject
	@CurrentLocale
	Locale currentLocale;

	public String getLanguage() {
		return this.currentLocale.getLanguage();
	}
}
