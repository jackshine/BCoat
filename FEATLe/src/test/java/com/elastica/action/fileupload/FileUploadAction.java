package com.elastica.action.fileupload;

import java.io.File;

import com.elastica.action.Action;
import com.elastica.common.SuiteData;
import com.elastica.logger.Logger;

/**
 * File Upload Action Class
 * @author Eldo Rajan
 *
 */
public class FileUploadAction extends Action{

	/**
	 * File upload function for chrome with autoit
	 * @param fileName
	 */
	public void autoitFileUploadChrome(String fileName){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		File fileLocation   =  new File(fileName);
		String filePath =   fileLocation.getAbsolutePath();
		File scriptLocation	= new File("src/test/resources/utils/FileUploadChrome.exe");
		String scriptPath  =  scriptLocation.getAbsolutePath().replace("\\", "\\\\");                    
		try {
			new ProcessBuilder(scriptPath,
					filePath, "Open").start();
			hardWait(10);
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * File upload function for firefox with autoit
	 * @param fileName
	 */
	public void autoitFileUploadFirefox(String fileName){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		File fileLocation   =  new File(fileName);
		String filePath =   fileLocation.getAbsolutePath();
		File scriptLocation	= new File("src/test/resources/utils/FileUploadFirefox.exe");
		String scriptPath  =  scriptLocation.getAbsolutePath().replace("\\", "\\\\");                    
		try {
			new ProcessBuilder(scriptPath,
					filePath, "Open").start();
			hardWait(10);
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * File upload function for safari with applescript
	 * @param fileName
	 */
	public void applescriptFileUploadSafari(String fileName){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String scriptPath  =   System.getProperty("user.dir")+File.separator+"src"+File.separator +
				"test"+File.separator +"resources"+File.separator +"utils" + File.separator+ "FileUploadSafari.scpt";               
		try {
			String command="osascript " + scriptPath+" "+fileName;
			Logger.info(command);
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor();
			hardWait(10);
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	/**
	 * File upload function for chrome with applescript
	 * @param fileName
	 */
	public void applescriptFileUploadChrome(String fileName){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String scriptPath  =   System.getProperty("user.dir")+File.separator+"src"+File.separator +
				"test"+File.separator +"resources"+File.separator +"utils" + File.separator+ "FileUploadChrome.scpt";               
		try {
			String command="osascript " + scriptPath+" "+fileName;
			Logger.info(command);
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor();
			hardWait(10);
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	/**
	 * File upload function for firefox with applescript
	 * @param fileName
	 */
	public void applescriptFileUploadFirefox(String fileName){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String scriptPath  =   System.getProperty("user.dir")+File.separator+"src"+File.separator +
				"test"+File.separator +"resources"+File.separator +"utils" + File.separator+ "FileUploadFirefox.scpt";               
		             
		try {
			String command="osascript " + scriptPath+" "+fileName;
			Logger.info(command);
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor();
			hardWait(10);
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void fileUpload(SuiteData suiteData, String filePath){
		String os = System.getProperty("os.name").toLowerCase();
		
		if(suiteData.getBrowser().equalsIgnoreCase("firefox")){
			if(os.indexOf("win")>=0){
				autoitFileUploadFirefox(filePath);
			}else if(os.indexOf("mac")>=0){
				applescriptFileUploadFirefox(filePath);
			}
		}else if(suiteData.getBrowser().equalsIgnoreCase("chrome")){
			if(os.indexOf("win")>=0){
				autoitFileUploadChrome(filePath);
			}else if(os.indexOf("mac")>=0){
				applescriptFileUploadChrome(filePath);
			}
		}else if(suiteData.getBrowser().equalsIgnoreCase("safari")){
			if(os.indexOf("mac")>=0){
				applescriptFileUploadSafari(filePath);
			}
		}
		
		
	}
	

}
