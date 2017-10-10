/**
 * 
 */
package com.elastica.beatle.replayTool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;

import com.elastica.beatle.RestClient.GatewayClient;
import com.elastica.beatle.RestClient.GatewayClientWrapper;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.replayTool.replayToolDTO.LogReplayReponseDTO;
import com.elastica.beatle.replayTool.replayToolDTO.LogReplayRequestDTO;

public class EPDV3SampleTest {
	
	public void runSampleTest() throws UnsupportedEncodingException, Exception {
		String replayFileName = "GoogleMail/Any,Gmail,DCompose,Compose.log";
		
		runSampleTest(replayFileName);
	}
	
	public void runSampleTest(String replayFileName) throws UnsupportedEncodingException, Exception {
		
		HttpHost gateway = new HttpHost("10.0.52.30", 443, "http");
		GatewayClient client = new GatewayClient(gateway);
		
		EPDV3LogParser parser = new EPDV3LogParser();
		
		Map<LogReplayRequestDTO, LogReplayReponseDTO> requestList = parser.readRequestsFromFile(replayFileName);
		
		Logger.info("Number of Requests to be Played : " + requestList.size());
		
		Logger.info("\n Started Replaying Logs\n");
		
		Iterator<Entry<LogReplayRequestDTO, LogReplayReponseDTO>> iterator = requestList.entrySet().iterator();
	    while (iterator.hasNext()) {
	        Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO> requestPair = (Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO>)iterator.next();
	        
	        if ((requestPair.getKey().getRequestURI().toString().contains("client-channel.google.com"))||
	        		requestPair.getKey().getRequestURI().toString().contains("google-analytics.com") ||
	        		requestPair.getKey().getRequestURI().toString().contains("img.en25.com") ||
	        		requestPair.getKey().getRequestURI().toString().contains("mozilla.net") ||
	        		requestPair.getKey().getRequestURI().toString().contains("gstatic.com") ||
	        		requestPair.getKey().getRequestURI().toString().contains("eloqua.com")||
	        		requestPair.getKey().getRequestURI().toString().contains("office_online")||
	        		requestPair.getKey().getRequestURI().toString().contains("login.salesforce.com") ||
	        		requestPair.getKey().getRequestURI().toString().contains("versioncheck-bg.addons.mozilla.org") ||
	        		requestPair.getKey().getRequestURI().toString().contains("addons.seleniumhq.org)") ||
	        		requestPair.getKey().getRequestURI().toString().contains("doubleclick.net") ||
	        		requestPair.getKey().getRequestURI().toString().contains("sfdcstatic.com") ||
	        		
	        		//(requestPair.getKey().getRequestURI().toString().contains("static.sharepointonline.com"))||
	        		//(requestPair.getKey().getRequestURI().toString().contains("gwo365beatle-my.sharepoint.com"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("download.cdn.mozilla.net"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("ns=PendingRequest&ev=PendingNotificationRequest&"))||
	        		(requestPair.getKey().getRequestURI().toString().contains(".css"))||
	        		(requestPair.getKey().getRequestURI().toString().contains(".js"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("secure.aadcdn.microsoftonline-p.com"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("prod.msocdn.com"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("aus4.mozilla.org"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("download.mozilla.org"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("s1-word-edit-15"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("eoe.elastica-inc.com"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("clients2.google.com/availability"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("abc-static"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("gatewaybeatle.com"))||
	        		
	        		(requestPair.getKey().getRequestURI().toString().contains("safebrowsing-cache.google.com"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("safebrowsing.google.com"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("setgmail"))||
	        		
	        		
	        		(requestPair.getKey().getRequestURI().toString().contains("apis.google.com"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("hangouts.google.com"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("accounts.youtube.com"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("plus.google.com"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("play.google.com"))||
	        		
	        		//these things are not blocking but i think its not required..
	        		(requestPair.getKey().getRequestURI().toString().contains("clients6.google.com"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("clients5.google.com"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("clients4.google.com"))||
	        		
	        		
	        		(requestPair.getKey().getRequestURI().toString().contains("www.google.com/gen_"))){
	        	System.out.println("I skipped this request : " + requestPair.getKey().getRequestURI().toString());
	        	continue;
	        }
	        
	        HttpResponse response = GatewayClientWrapper.executeGatewayRequest(client, requestPair.getKey(), "testuser1@gatewaybeatle.com");
	        Logger.info("Response Received from gateway");
	        Assert.assertTrue(validateResponseStatus(requestPair.getValue(), response), "Response Validation Failed!!");
	    }
	}
	
	private boolean validateResponseStatus(LogReplayReponseDTO value, HttpResponse response) throws ParseException, IOException {
		Logger.info("Validating the Response Status");
		Logger.info("Actual Response Status : " + response.getStatusLine().toString());
		Logger.info("Expected Response Status : " + value.getResponseStatus().toString());
//		Logger.info("Response Body: ");
//		Logger.info(EntityUtils.toString(response.getEntity(), "utf-8"));
		
		if(value.getResponseStatus().trim().equals(response.getStatusLine().toString()) || 
				value.getResponseStatus().contains("302") ||
				value.getResponseStatus().contains("301")){
			Logger.info("Response Status matched!");
			return true;
		}
		
		Logger.info("Response Status NOT matched!");
		return false;
	}
	
	public static void main(String[] args) throws Exception{
		EPDV3SampleTest t = new EPDV3SampleTest();
		t.runSampleTest();
//		//t.replayLogs("TestBox/"+"Test,CFolder,upload_pdf.log");
//		//t.replayLogs("apidata.txt,uploadtext.log");
//		//t.replayLogs("Apidata.txt,UploadTxtFile.log");
//		//t.replayLogs("Afjal resume,downloaddoc.log");
//		//t.replayLogs("afjalresume,uploaddoc.log");
//		
//		//String logFile="nwlogs/"+"TestFile,AllFiles,uploadallfiles.log";
//		//String logFile="nwlogs/"+"TestFile,AllFiles,shareAllfiles.log";
//		
//		System.out.println("Logs a");
//		//t.replayLogs(logFile);
//		
//		String folder="GoogleMail/";
//		//String folder="f/";
//		//String folder="nwlogs/";
//		//String folder="Test1/2015.09.17/";
//		String absolutePath=LogReplayConstant.REPLAY_LOGFOLDER_PATH+folder;
//		String completeFilePath = FileHandlingUtils
//				.getFileAbsolutePath(LogReplayConstant.REPLAY_LOGFOLDER_PATH+folder);
//		
//		//String sampleCompleteFilePath="/Users/usman/Documents/epd-data/"+folder;
//						//+ folder);
//		List<String> results = new ArrayList<String>();
//
//		File[] files = new File(completeFilePath).listFiles();
//		//File[] files = new File(sampleCompleteFilePath).listFiles();
//		//If this pathname does not denote a directory, then listFiles() returns null. 
//
//		//for (File file : files) {
//		for (int i=0; i<files.length; i++){
//		    if (files[i].isFile()) {
//		        results.add(files[i].getName());
//		        if (files[i].getName().endsWith(".log")){
//		        	// Thread.sleep(5000);
//		        	 System.out.println(i+" : "+files[i].getName());
//		        	// if (files[i].getName().contains("O365,AFolder,create_new_folder.log")||files[i].getName().contains("O365,AFolder,move_Test_txt.log"))//||
//		        			//file.getName().contains("O365,outlook,newBodyMail.log"))    //|| file.getName().contains("Login.log"))
//		        		 //continue;
//		        	 t.runSampleTest(folder+files[i].getName());
//		        	
//		        }
//		       // System.out.println(file.getName().endsWith(".log"));
//		    }
//		}
	}
}