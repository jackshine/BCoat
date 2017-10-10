package com.elastica.driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import com.elastica.constants.FrameworkConstants;

/**
 * Test Config
 * @author Eldo Rajan
 *
 */
public class TestConfig {



	private DriverType mode;
	private String huburl;
	private String baseUrl;		
	private String browser;
	private String chromedriver;
	private String iedriver;
	private String ghostdriver;
	private String bmpflag;
	private String proxyExtension;
	private String downloadDir;

	private Properties properties = new Properties();

	public TestConfig() {
		properties = loadPropertiesFile();
		mode = DriverType.getDriverType(getPropertyOrThrowException(FrameworkConstants.CONFIG_MODE));
		huburl = getPropertyOrThrowException(com.elastica.constants.FrameworkConstants.CONFIG_HUBURL);
		baseUrl = getPropertyOrThrowException(FrameworkConstants.CONFIG_BASEURL);
		browser = getPropertyOrThrowException(FrameworkConstants.CONFIG_BROWSER);
		chromedriver = getPropertyOrThrowException(FrameworkConstants.CONFIG_CHROMEDRIVER);
		iedriver = getPropertyOrThrowException(FrameworkConstants.CONFIG_IEDRIVER);
		ghostdriver = getPropertyOrThrowException(FrameworkConstants.CONFIG_GHOSTDRIVER);
		bmpflag = getPropertyOrThrowException(FrameworkConstants.CONFIG_BMPFLAG);
		proxyExtension = getPropertyOrThrowException(FrameworkConstants.CONFIG_PROXYEXTENSION);

		try {
			URL resourceUrl = getClass().getResource(chromedriver);
			Path resourcePath = Paths.get(resourceUrl.toURI());
			chromedriver = resourcePath.toString();

			URL resourceUrl2 = getClass().getResource(iedriver);
			Path resourcePath2 = Paths.get(resourceUrl2.toURI());
			iedriver = resourcePath2.toString();

			URL resourceUrl3 = getClass().getResource(ghostdriver);
			Path resourcePath3 = Paths.get(resourceUrl3.toURI());
			ghostdriver = resourcePath3.toString();

			URL resourceUrl4 = getClass().getResource(proxyExtension);
			Path resourcePath4 = Paths.get(resourceUrl4.toURI());
			proxyExtension = resourcePath4.toString();
		} catch (Exception e) {
		}


	}

	/**
	 * load properties file
	 * @return
	 */
	private Properties loadPropertiesFile() {
		Properties result = new Properties();
		try {
			// get specified property file
			String filename = getPropertyOrNull(FrameworkConstants.CONFIG_PROPERTIES);
			// it is not defined, use default
			if (filename == null) {
				filename = FrameworkConstants.CONFIG_PROPERTIES;
			}
			// try to load from classpath
			InputStream stream = getClass().getClassLoader().getResourceAsStream(filename);
			// no file in classpath, look on disk
			if (stream == null) {
				stream = new FileInputStream(new File(filename));
			}

			result.load(stream);
		} catch (IOException e) {
			try {
				throw new Exception("Property file is not found");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * get property or null
	 * @param name
	 * @return
	 */
	public String getPropertyOrNull(String name) {
		return getProperty(name, false);
	}

	/**
	 * get property or throw exception
	 * @param name
	 * @return
	 */
	public String getPropertyOrThrowException(String name) {
		return getProperty(name, true);
	}

	/**
	 * get property
	 * @param name
	 * @param forceExceptionIfNotDefined
	 * @return
	 */
	private String getProperty(String name, boolean forceExceptionIfNotDefined) {
		String result;
		if ((result = System.getProperty(name, null)) != null && result.length() > 0) {
			return result;
		} else if ((result = getPropertyFromPropertiesFile(name)) != null && result.length() > 0) {
			return result;
		} else if (forceExceptionIfNotDefined) {
			try {
				throw new Exception("Unknown property: [" + name + "]");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * get property from properties file
	 * @param name
	 * @return
	 */
	private String getPropertyFromPropertiesFile(String name) {
		Object result = properties.get(name);
		if (result == null) {
			return null;
		} else {
			return result.toString();
		}
	}

	/**
	 * @return the mode
	 */
	public DriverType getMode() {
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(DriverType mode) {
		this.mode = mode;
	}

	/**
	 * @return the huburl
	 */
	public String getHuburl() {
		return huburl;
	}

	/**
	 * @param huburl the huburl to set
	 */
	public void setHuburl(String huburl) {
		this.huburl = huburl;
	}

	/**
	 * @return the baseUrl
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * @param baseUrl the baseUrl to set
	 */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * @return the browser
	 */
	public String getBrowser() {
		return browser;
	}

	/**
	 * @param browser the browser to set
	 */
	public void setBrowser(String browser) {
		this.browser = browser;
	}

	/**
	 * @return the chromedriver
	 */
	public String getChromeDriver() {
		return chromedriver;
	}

	/**
	 * @param chromedriver the chromedriver to set
	 */
	public void setChromeDriver(String chromedriver) {
		this.chromedriver = chromedriver;
	}

	/**
	 * @return the iedriver
	 */
	public String getIEDriver() {
		return iedriver;
	}

	/**
	 * @param iedriver the iedriver to set
	 */
	public void setIEDriver(String iedriver) {
		this.iedriver = iedriver;
	}

	/**
	 * @return the ghostdriver
	 */
	public String getGhostDriver() {
		return ghostdriver;
	}

	/**
	 * @param ghostdriver the ghostdriver to set
	 */
	public void setGhostDriver(String ghostdriver) {
		this.ghostdriver = ghostdriver;
	}

	/**
	 * @return the bmpflag
	 */
	public String getBmpFlag() {
		return bmpflag;
	}

	/**
	 * @param bmpflag the bmpflag to set
	 */
	public void setBmpFlag(String bmpflag) {
		this.bmpflag = bmpflag;
	}

	/**
	 * @return the proxyExtension
	 */
	public String getProxyExtension() {
		return proxyExtension;
	}

	/**
	 * @param proxyExtension the proxyExtension to set
	 */
	public void setProxyExtension(String proxyExtension) {
		this.proxyExtension = proxyExtension;
	}

	/**
	 * @return the downloadDir
	 */
	public String getDownloadDir() {
		return downloadDir;
	}

	/**
	 * @param downloadDir the downloadDir to set
	 */
	public void setDownloadDir(String downloadDir) {
		this.downloadDir = downloadDir;
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	
}