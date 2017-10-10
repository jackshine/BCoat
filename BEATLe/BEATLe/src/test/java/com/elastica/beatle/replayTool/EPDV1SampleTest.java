/**
 * 
 */
package com.elastica.beatle.replayTool;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import com.elastica.beatle.RestClient.GatewayClient;
import com.elastica.beatle.RestClient.GatewayClientWrapper;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.gateway.CommonConfiguration;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.replayTool.replayToolDTO.LogReplayReponseDTO;
import com.elastica.beatle.replayTool.replayToolDTO.LogReplayRequestDTO;

public class EPDV1SampleTest extends CommonConfiguration{
	
	public void runSampleTest() throws UnsupportedEncodingException, Exception {
		
		HttpHost gateway = new HttpHost("10.0.54.16", 443, "http");
		GatewayClient client = new GatewayClient(gateway);
		
		EPDV1Parser parser = new EPDV1Parser();
		String replayFileName = "Documents,ItemShareInvite.log";
		Map<LogReplayRequestDTO, LogReplayReponseDTO> requestList = parser.readRequestsFromFile(replayFileName);
		
		Logger.info("Number of Requests to be Played : " + requestList.size());
		
		Iterator<Entry<LogReplayRequestDTO, LogReplayReponseDTO>> iterator = requestList.entrySet().iterator();
	    while (iterator.hasNext()) {
	        Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO> requestPair = (Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO>)iterator.next();
	        HttpResponse response = GatewayClientWrapper.executeGatewayRequest(client, requestPair.getKey(), "mohammad.usman@elastica.co");
	        Logger.info("Response Received from gateway");
	        validateResponseStatus(requestPair.getValue(), response);
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
		EPDV1SampleTest t = new EPDV1SampleTest();
		t.runSampleTest();
		
		
		//EPDV1SampleTest t = new EPDV1SampleTest();
		/*String folder="boxnew/";
		String absolutePath=LogReplayConstant.REPLAY_LOGFOLDER_PATH+folder;
		String completeFilePath = FileHandlingUtils
				.getFileAbsolutePath(LogReplayConstant.REPLAY_LOGFOLDER_PATH);
						//+ folder);
		List<String> results = new ArrayList<String>();


		File[] files = new File(completeFilePath).listFiles();
		//If this pathname does not denote a directory, then listFiles() returns null. 

		for (File file : files) {
		    if (file.isFile()) {
		        results.add(file.getName());
		        if (file.getName().endsWith(".log")){
		        	 //Thread.sleep(5000);
		        	 System.out.println(file.getName());
		        	 if (file.getName().contains("Box1.txt,All Files,FileUpload.log")||file.getName().contains("IMG.png,All Files,FileUpload.log")||
		        			 file.getName().contains("Logout.log"))//|| file.getName().contains("Login.log"))
		        		 continue;
		        	 t.replayLogs(file.getName());
		        	
		        }
		       // System.out.println(file.getName().endsWith(".log"));
		    }
		}*/
		
	}
	
	
public void replayLogs(String fileName) throws UnsupportedEncodingException, Exception {
	HttpHost gateway = new HttpHost("10.0.54.16", 443, "http");
	GatewayClient client = new GatewayClient(gateway);
	
	EPDV1Parser parser = new EPDV1Parser();
	String replayFileName = fileName;//+".log";//"bm0,FileCreate.log";
	Map<LogReplayRequestDTO, LogReplayReponseDTO> requestList = parser.readRequestsFromFile(replayFileName);
	
	Logger.info("Number of Requests to be Played : " + requestList.size());
	
	Iterator<Entry<LogReplayRequestDTO, LogReplayReponseDTO>> iterator = requestList.entrySet().iterator();
    while (iterator.hasNext()) {
        Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO> requestPair = (Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO>)iterator.next();
        HttpResponse response = GatewayClientWrapper.executeGatewayRequest(client, requestPair.getKey(), "testuser1@gatewaybeatle.com");
        Logger.info("Response Received from gateway");
        validateResponseStatus(requestPair.getValue(), response);
    }
    Thread.sleep(5000);
	}

}
