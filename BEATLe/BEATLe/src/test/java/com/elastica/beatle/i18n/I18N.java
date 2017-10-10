package com.elastica.beatle.i18n;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

public class I18N {
	public static final String I18N_TEST = "testi18n";
	public static final String BUNDLE_NAME = "i18n.testi18n";
	
	
	public static String getString(String strLocale) {

		Locale locale;
		ResourceBundle bundle;
		
		if (strLocale !=null ) {
			String parts[] = strLocale.split("_");
			locale  = new Locale(parts[0], parts[1]);
			bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale, new UTF8Control());
			return bundle.getString("string");
		} else {
			bundle = ResourceBundle.getBundle(BUNDLE_NAME, new UTF8Control());
			return bundle.getString("string");
		}
	}
	
	public static String getString(String name, String strLocale) {

		Locale locale;
		ResourceBundle bundle;
		
		if (strLocale !=null ) {
			String parts[] = strLocale.split("_");
			locale  = new Locale(parts[0], parts[1]);
			bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale, new UTF8Control());
			return bundle.getString(name);
		} else {
			bundle = ResourceBundle.getBundle(BUNDLE_NAME, new UTF8Control());
			return bundle.getString(name);
		}
	}
	

	
	public static String getNumbers(String strLocale) {

		Locale locale;
		ResourceBundle bundle;
		
		if (strLocale !=null ) {
			String parts[] = strLocale.split("_");
			locale  = new Locale(parts[0], parts[1]);
			bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale, new UTF8Control());
			return bundle.getString("numbers");
		} else {
			bundle = ResourceBundle.getBundle(BUNDLE_NAME, new UTF8Control());
			return bundle.getString("numbers");
		}
	}
	
	
	
	public static String getDate(String strLocale) {

		Locale locale;
		ResourceBundle bundle;
		
		if (strLocale !=null ) {
			String parts[] = strLocale.split("_");
			locale  = new Locale(parts[0], parts[1]);
			bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale, new UTF8Control());
			return bundle.getString("date");
		} else {
			bundle = ResourceBundle.getBundle(BUNDLE_NAME, new UTF8Control());
			return bundle.getString("date");
		}
	}
	
	public String fetchString(String strLocale) {

		Locale locale;
		ResourceBundle bundle;

		if (strLocale !=null ) {
			String parts[] = strLocale.split("_");
			locale  = new Locale(parts[0], parts[1]);
			bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale, new UTF8Control());
			return bundle.getString("string");
		} else {
			bundle = ResourceBundle.getBundle(BUNDLE_NAME, new UTF8Control());
			return bundle.getString("string");
		}
	}

	public String fetchString() {
		ResourceBundle bundle;
		bundle = ResourceBundle.getBundle(BUNDLE_NAME, new UTF8Control());
		return bundle.getString("string");
	}

}
