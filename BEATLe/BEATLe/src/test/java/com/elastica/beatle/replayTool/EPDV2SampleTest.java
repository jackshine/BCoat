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

import com.elastica.beatle.RestClient.GatewayClient;
import com.elastica.beatle.RestClient.GatewayClientWrapper;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.replayTool.replayToolDTO.LogReplayReponseDTO;
import com.elastica.beatle.replayTool.replayToolDTO.LogReplayRequestDTO;

public class EPDV2SampleTest {
	
	public void runSampleTest() throws UnsupportedEncodingException, Exception {
		
		HttpHost gateway = new HttpHost("10.0.54.16", 443, "http");
		GatewayClient client = new GatewayClient(gateway);
		
		LogParser parser = new LogParser();
		String replayFileName = "apidata,downloadtxt.log";
		Map<LogReplayRequestDTO, LogReplayReponseDTO> requestList = parser.readRequestsFromFile(replayFileName);
		
		Logger.info("Number of Requests to be Played : " + requestList.size());
		
		Logger.info("\n Started Replaying Logs\n");
		
		Iterator<Entry<LogReplayRequestDTO, LogReplayReponseDTO>> iterator = requestList.entrySet().iterator();
	    while (iterator.hasNext()) {
	        Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO> requestPair = (Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO>)iterator.next();
	        HttpResponse response = GatewayClientWrapper.executeGatewayRequest(client, requestPair.getKey(), "testuser1@gatewaybeatle.com");
	        Logger.info("Response Received from gateway");
	       // validateResponseStatus(requestPair.getValue(), response);
	    }
	}
	
	private boolean validateResponseStatus(LogReplayReponseDTO value, HttpResponse response) throws ParseException, IOException {
		Logger.info("Validating the Response Status");
		Logger.info("Response Status : " + response.getStatusLine().toString());
		Logger.info("Response Body: ");
		Logger.info(EntityUtils.toString(response.getEntity(), "utf-8"));
		
		if(value.getResponseStatus().trim().equals(response.getStatusLine().toString())){
			Logger.info("Response Status matched!");
			return true;
		}
		
		Logger.info("Response Status NOT matched!");
		return false;
	}
	
	public static void main(String[] args) throws Exception{
		EPDV2SampleTest t = new EPDV2SampleTest();
		//t.runSampleTest();
		//t.replayLogs("TestBox/"+"Test,CFolder,upload_pdf.log");
		//t.replayLogs("apidata.txt,uploadtext.log");
		//t.replayLogs("Apidata.txt,UploadTxtFile.log");
		//t.replayLogs("Afjal resume,downloaddoc.log");
		//t.replayLogs("afjalresume,uploaddoc.log");
		
		//String logFile="nwlogs/"+"TestFile,AllFiles,uploadallfiles.log";
		//String logFile="nwlogs/"+"TestFile,AllFiles,shareAllfiles.log";
		
		System.out.println("Logs a");
		//t.replayLogs(logFile);
		
		String folder="c/";
		//String folder="f/";
		//String folder="nwlogs/";
		//String folder="Test1/2015.09.17/";
		String absolutePath=LogReplayConstant.REPLAY_LOGFOLDER_PATH+folder;
		String completeFilePath = FileHandlingUtils
				.getFileAbsolutePath(LogReplayConstant.REPLAY_LOGFOLDER_PATH+folder);
		
		//String sampleCompleteFilePath="/Users/usman/Documents/epd-data/"+folder;
						//+ folder);
		List<String> results = new ArrayList<String>();

		File[] files = new File(completeFilePath).listFiles();
		//File[] files = new File(sampleCompleteFilePath).listFiles();
		//If this pathname does not denote a directory, then listFiles() returns null. 

		//for (File file : files) {
		for (int i=0; i<files.length; i++){
		    if (files[i].isFile()) {
		        results.add(files[i].getName());
		        if (files[i].getName().endsWith(".log")){
		        	// Thread.sleep(5000);
		        	 System.out.println(i+" : "+files[i].getName());
		        	// if (files[i].getName().contains("O365,AFolder,create_new_folder.log")||files[i].getName().contains("O365,AFolder,move_Test_txt.log"))//||
		        			//file.getName().contains("O365,outlook,newBodyMail.log"))    //|| file.getName().contains("Login.log"))
		        		 //continue;
		        	 t.replayLogs(folder+files[i].getName());
		        	
		        }
		       // System.out.println(file.getName().endsWith(".log"));
		    }
		}
	}
	
	
public void replayLogs(String fileName) throws UnsupportedEncodingException, Exception {
		
		//HttpHost gateway = new HttpHost("10.0.54.16", 443, "http");
		//HttpHost gateway = new HttpHost("10.0.54.115", 443, "http");
		HttpHost gateway = new HttpHost("10.0.52.30", 443, "http");
		GatewayClient client = new GatewayClient(gateway);
		LogParser parser = new LogParser();
		String replayFileName = fileName;//"AFolder,CreateFolder.log";
		Map<LogReplayRequestDTO, LogReplayReponseDTO> requestList = parser.readRequestsFromFile(replayFileName);
		
		Logger.info("Number of Requests to be Played : " + requestList.size());
		
		Logger.info("\n Started Replaying Logs\n");
		
		Iterator<Entry<LogReplayRequestDTO, LogReplayReponseDTO>> iterator = requestList.entrySet().iterator();
	    while (iterator.hasNext()) {
	        Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO> requestPair = (Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO>)iterator.next();
	        if ((requestPair.getKey().getRequestURI().toString().contains("client-channel.google.com"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("login.salesforce.com"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("versioncheck-bg.addons.mozilla.org"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("doubleclick.net"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("sfdcstatic.com"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("google.com"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("google-analytics.com"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("static.sharepointonline.com"))||
	        		//(requestPair.getKey().getRequestURI().toString().contains("gwo365beatle-my.sharepoint.com"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("download.cdn.mozilla.net"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("ns=PendingRequest&ev=PendingNotificationRequest&"))||
	        		(requestPair.getKey().getRequestURI().toString().contains(".css"))||
	        		(requestPair.getKey().getRequestURI().toString().contains(".js"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("secure.aadcdn.microsoftonline-p.com"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("prod.msocdn.com"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("aus4.mozilla.org"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("download.mozilla.org"))||
	        		(requestPair.getKey().getRequestURI().toString().contains("|"))||
	        		
	        		
	        		
	        		
	        		
	        		
	        		(requestPair.getKey().getRequestURI().toString().contains("addons.seleniumhq.org)"))){
	        	System.out.println("I skipped this request : " + requestPair.getKey().getRequestURI().toString());
	        	continue;
	        }
	      //  HttpResponse response = GatewayClientWrapper.executeGatewayRequest(client, requestPair.getKey(), "testuser1@gatewaybeatle.com");
	        HttpResponse response = GatewayClientWrapper.executeGatewayRequest(client, requestPair.getKey(), "testuser1@gatewayo365beatle.com");
	        Logger.info("Response Received from gateway");
	       // validateResponseStatus(requestPair.getValue(), response);
	    }
	}





public static void main1(String [] args) {

	System.out.println("Starting");
	EPDV2SampleTest ev= new EPDV2SampleTest();

	
	System.out.println("Logs a");
	//t.replayLogs(logFile);
	
	String folder="onedrive/";
	//String folder="nwlogs/";
	//String folder="Test1/2015.09.17/";
	String absolutePath=LogReplayConstant.REPLAY_LOGFOLDER_PATH+folder;
	String completeFilePath = FileHandlingUtils
			.getFileAbsolutePath(LogReplayConstant.REPLAY_LOGFOLDER_PATH+folder);
	
	//String sampleCompleteFilePath="/Users/usman/Documents/epd-data/"+folder;
					//+ folder);
	List<String> results = new ArrayList<String>();

	File[] files = new File(completeFilePath).listFiles();
	//File[] files = new File(sampleCompleteFilePath).listFiles();
	//If this pathname does not denote a directory, then listFiles() returns null. 

	//for (File file : files) {
	for (int i=0; i<files.length; i++){
	    if (files[i].isFile()) {
	        results.add(files[i].getName());
	        if (files[i].getName().endsWith(".log")){
	        	// Thread.sleep(5000);
	        	 System.out.println(i+" : "+files[i].getName());
	        	// if (files[i].getName().contains("O365,AFolder,create_new_folder.log")||files[i].getName().contains("O365,AFolder,move_Test_txt.log"))//||
	        			//file.getName().contains("O365,outlook,newBodyMail.log"))    //|| file.getName().contains("Login.log"))
	        		 //continue;
	        	 try {
	
	        		 ev.readRequestsFromFile(folder+files[i].getName());
	        	 } catch (IOException | URISyntaxException e) {
	        		 // TODO Auto-generated catch block
	        		 e.printStackTrace();
	        	 }
	        }
	    }
	}
	
}





public void readRequestsFromFile(String logFilePath) throws FileNotFoundException, IOException, URISyntaxException{
	String completeFilePath = FileHandlingUtils.getFileAbsolutePath(LogReplayConstant.REPLAY_LOGFOLDER_PATH+logFilePath);
	//String completeFilePath = "/Users/usman/Documents/epd-data/"+logFilePath;
	
	String [] fileName=logFilePath.split("/");
	System.out.println(fileName[0]);
	System.out.println(fileName[1]);
	
	
	// try {
	        // Assume default encoding.
	        FileWriter fileWriter =
	            new FileWriter(new File(System.getProperty("user.home"),fileName[1] ));

	        // Always wrap FileWriter in BufferedWriter.
	        BufferedWriter bufferedWriter =
	            new BufferedWriter(fileWriter);
	        
	
	Logger.info("Processind data from :"+completeFilePath);
	BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(completeFilePath))));	 
	Map<LogReplayRequestDTO, LogReplayReponseDTO> requestMap = new LinkedHashMap<>();		
	String currentProcessingLine = bufferReader.readLine();
	
		bufferedWriter.write(currentProcessingLine);
		bufferedWriter.newLine();
	
		currentProcessingLine = bufferReader.readLine();
		while (currentProcessingLine != null ){
			if (currentProcessingLine.equals("@@@RequestHeader")){
				bufferedWriter.write(currentProcessingLine);
				bufferedWriter.newLine();
				currentProcessingLine = bufferReader.readLine();
				System.out.println(currentProcessingLine);
				String url = currentProcessingLine;
				 url = url.replaceAll("\\{", "=%7B");
				 System.out.println(url);
				 url=url.replaceAll("\\}", "%7D");
				 url=url.replaceAll("\\|", "%7C");
				 System.out.println(url);
				bufferedWriter.write(url);
				
				//String[] requestLine = currentProcessingLine.split(" ");
				//URL rURL = new URL(requestLine[1]);
				//URI uri= new URI(rURL.getProtocol(), rURL.getUserInfo(), rURL.getHost(), rURL.getPort(), rURL.getPath(), rURL.getQuery(), rURL.getRef());
				//bufferedWriter.write(requestLine[0]+" "+uri.toASCIIString()+" "+requestLine[2]);
				
				//bufferedWriter.write(currentProcessingLine.replaceAll(requestLine[1], uri.toString()));	
				bufferedWriter.newLine();
			}
			else {
				bufferedWriter.write(currentProcessingLine);	
				bufferedWriter.newLine();
			}
			currentProcessingLine = bufferReader.readLine();
			System.out.println(currentProcessingLine);
		}
		bufferedWriter.close();

	}







}
