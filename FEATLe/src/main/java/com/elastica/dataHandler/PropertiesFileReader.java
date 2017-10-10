package com.elastica.dataHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class PropertiesFileReader {

	/**
	 * This function is used to read and return the properties value
	 * @return String
	 * @throws InvalidPropertiesFormatException
	 * @throws IOException
	 */
	public String getPropertiesValue(String path) {
		String propertyValue = null;
		StackTraceElement[] element = new Throwable().getStackTrace();
		Properties properties = null;
		try {
			properties = getProperties(System.getProperty("user.dir")
					+ path);
		} catch (InvalidPropertiesFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		propertyValue = (String) properties.get(element[1].getClassName() + "."
				+ element[1].getMethodName());
		return propertyValue;
	}

	/**
	 * This function is used to read all the properties file in folder and subfolder available on the file location
	 * @param fileLocation
	 * @return Properties
	 * @throws InvalidPropertiesFormatException
	 * @throws IOException
	 */
	public Properties getProperties(String fileLocation)
			throws InvalidPropertiesFormatException, IOException {
		Properties properties = new Properties();
		File[] dataFileList = null;
		File dataDir = null;
		File dir = new File(fileLocation);
		if (dir.exists() && dir.isDirectory()) {
			String[] dirList = dir.list();
			for (String dirName : dirList) {
				dataDir = new File(fileLocation + File.separator + dirName);
				if (dataDir.isDirectory()) {
					dataFileList = dataDir.listFiles();
					if (dataFileList != null) {
						for (File aFile : dataFileList) {
							if (aFile.isDirectory()) {
								dataDir = new File(fileLocation
										+ File.separator + aFile);
								dataFileList = dataDir.listFiles();
								if (dataFileList != null) {
									for (File otherFile : dataFileList) {
										if (!otherFile.isDirectory()
												&& (otherFile.getAbsolutePath()
														.endsWith(".xml") || otherFile
														.getAbsolutePath()
														.endsWith(".properties"))) {
											properties.putAll(readFile(aFile));
										}
									}
								}
							} else {
								properties.putAll(readFile(aFile));
							}
						}
					} else {
						dataFileList = dir.listFiles();
						for (File aFile : dataFileList) {
							if (aFile.isDirectory()) {
								dataDir = new File(fileLocation + aFile);
							} else {
								properties.putAll(readFile(aFile));
							}
						}
					}
				} else {
					properties.putAll(readFile(dataDir));
				}
			}
		}
		return properties;
	}

	/**
	 * This function is used to read the properties file
	 * @param dataFile
	 * @return Properties
	 * @throws InvalidPropertiesFormatException
	 * @throws IOException
	 */
	protected Properties readFile(File dataFile)
			throws InvalidPropertiesFormatException, IOException {
		Properties properties = new Properties();
		String fileName = dataFile.getAbsolutePath();
		if (fileName.endsWith(".properties")) {
			InputStream thePropFile = new FileInputStream(fileName);
			if (thePropFile != null) {
				Properties newProp = new Properties();
				if (fileName.endsWith(".properties"))
					newProp.load(thePropFile);
				Set<Entry<Object, Object>> propEntrySet = newProp.entrySet();
				if (propEntrySet != null && !propEntrySet.isEmpty()) {
					Iterator<Entry<Object, Object>> propIter = propEntrySet
							.iterator();
					while (propIter.hasNext()) {
						Entry<Object, Object> theEntry = propIter.next();
						properties.put(theEntry.getKey(), theEntry.getValue());
					}
				}
			}
		}
		return properties;
	}
}
