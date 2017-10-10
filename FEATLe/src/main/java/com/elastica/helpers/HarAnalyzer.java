package com.elastica.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.elastica.logger.Logger;

import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;
import net.lightbody.bmp.core.har.HarNameValuePair;
import net.lightbody.bmp.core.har.HarPage;
import net.lightbody.bmp.core.har.HarPageTimings;
//import net.lightbody.bmp.core.har.HarPostData;
import net.lightbody.bmp.core.har.HarRequest;
import net.lightbody.bmp.core.har.HarResponse;
//import net.lightbody.bmp.core.har.HarTimings;

/**
 * har analyzer helper class
 * @author Eldo Rajan
 *
 */

public class HarAnalyzer {

	/**
	 * save har content onto a file
	 * @param filename
	 * @param har
	 */
	public void saveHarFile(String filename, Har har){
		File dir = new File(System.getProperty("user.dir")+File.separator+"har");
		File file = new File(System.getProperty("user.dir")+File.separator+"har"+File.separator+filename); 		
		Logger.info("Path of har file is="+file.getPath());
	
		if(!dir.exists()){
			dir.mkdir();
		}
		
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			FileOutputStream fos=new FileOutputStream(file);
			har.writeTo(fos);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Parses the HAR file and prints out all the information
	 * @param harfile
	 */
	public void investigateHar(Har harfile) {
		/*Root of Exported Data*/
		HarLog log=harfile.getLog();
		
		/*Comment information*/
		//String comment=log.getComment();
		//Logger.info("1. Comment ="+comment);
		
		/*Creator information*/
		/*HarNameVersion creatorData=log.getCreator();
		String creator_comment=creatorData.getComment();
		String creator_name=creatorData.getName();
		String creator_version=creatorData.getVersion();*/
		
		/*Logger.info("2. Creator Data:-");
		Logger.info("Creator: name="+creator_name);
		Logger.info("Creator: comment="+creator_comment);
		Logger.info("Creator: version="+creator_version);*/
		
		/*Browser information*/	
		/*HarNameVersion browserData=log.getBrowser();
		String browser_name=browserData.getName();
		String browser_comment=browserData.getComment();
		String browser_version=browserData.getVersion();*/
		
		/*Logger.info("3. Browser Data:");
		Logger.info("Browser: name="+browser_name);
		Logger.info("Browser: comment="+browser_comment);
		Logger.info("Browser: version="+browser_version);*/
		
		/*Entries information*/
		List<HarEntry> listEntries=log.getEntries();
		
		for(HarEntry entry:listEntries){
			long time=entry.getTime();
			String pageRef=entry.getPageref();
			
			/*HarTimings timings=entry.getTimings();
			long blocked=timings.getBlocked();
			long connect=timings.getConnect();
			long dns=timings.getDns();
			long recieve=timings.getReceive();
			long send= timings.getSend();
			long ssl=timings.getSsl();
			long wait=timings.getWait();*/
			//long sum=connect+dns+recieve+send+ssl+wait;
			
			//String connection=			entry.getConnection();
			//String pageref=			entry.getPageref();
			//String serverIP=			entry.getServerIPAddress();
			
			HarRequest request=			entry.getRequest();
			String requestURL=			request.getUrl();
			
			Logger.info("\nRequest URL: "+requestURL);
			
			List<HarNameValuePair> queryString=			request.getQueryString();
			/*HarPostData postData=			request.getPostData();

			String postDataText=null;
			if(postData!=null){
				postDataText=postData.getText();
			}*/
			//Logger.info("Connection="+connection+"  PageRef="+pageref+"   serverIP="+serverIP+"   post data="+postDataText);
			Logger.info("PageRef= "+pageRef);

			if(queryString.size()>0){
				Logger.info("Request Query String:");
				for(HarNameValuePair pair: queryString){
					String name= pair.getName();
					String value= pair.getValue();
				
					Logger.info("Name= "+name+"  Value= "+value);
				}
			}
			/*You can dig in to Response Object also*/
			HarResponse response=			entry.getResponse();
			int status=response.getStatus();
			//Logger.info("Time="+time+"  Sum of timings tag="+sum+  " (Except Blocked time="+blocked+")");
			Logger.info("Time="+time);
			Logger.info("Response code="+status);
		}
		/*Pages Information*/
		List<HarPage> harPage=log.getPages();
		for(HarPage page: harPage){
			
			String id=page.getId();
			String title=page.getTitle();
			Date date=page.getStartedDateTime();
			
			Logger.info("**********************************************************************");
			
			HarPageTimings timing=page.getPageTimings();
			if(timing!=null){
				
				//long onContentload=timing.getOnContentLoad();
				long onload=timing.getOnLoad();
				Logger.info("PageTiming Data: Title="+title+"   Id="+id+"   onLoad="+onload+"   Date="+date);
			}
		}
	}
}
