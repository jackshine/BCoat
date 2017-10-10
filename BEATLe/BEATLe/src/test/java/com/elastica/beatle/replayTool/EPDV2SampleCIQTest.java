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

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.util.EntityUtils;

import com.elastica.beatle.RestClient.GatewayClient;
import com.elastica.beatle.RestClient.GatewayClientWrapper;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.replayTool.replayToolDTO.LogReplayReponseDTO;
import com.elastica.beatle.replayTool.replayToolDTO.LogReplayRequestDTO;

public class EPDV2SampleCIQTest {
	
	public void runSampleTest() throws UnsupportedEncodingException, Exception {
		
		HttpHost gateway = new HttpHost("10.0.50.68", 443, "http");
		GatewayClient client = new GatewayClient(gateway);
		
		LogParser parser = new LogParser();
		String replayFileName = "Box/Test,AFolder,upload_txt.log";
		Map<LogReplayRequestDTO, LogReplayReponseDTO> requestList = parser.readRequestsFromFile(replayFileName);
		
		Logger.info("Number of Requests to be Played : " + requestList.size());
		
		Logger.info("\n Started Replaying Logs\n");
		
		Iterator<Entry<LogReplayRequestDTO, LogReplayReponseDTO>> iterator = requestList.entrySet().iterator();
	    while (iterator.hasNext()) {
	        Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO> requestPair = (Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO>)iterator.next();
	        if (isContainsFileNameHeader(requestPair.getKey().getRequestHeader())) {
	        	Logger.info("Upload Request");
	        	String fileName = "Test.txt";
	        	String completeFilePath = FileHandlingUtils.getFileAbsolutePath(LogReplayConstant.REPLAY_LOGFOLDER_PATH + "uploadFiles/" + fileName);
	        	File file = new File(completeFilePath);
	        	FileBody fileBody = new FileBody(file);
	        	 
	        	MultipartEntityBuilder builder = MultipartEntityBuilder.create();
	        	builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
	        	builder.addPart("upfile", fileBody);
	        	HttpEntity entity = builder.build();
	        	
	        	HttpResponse response = GatewayClientWrapper.executeGatewayRequest(client, requestPair.getKey(), entity, "testuser1@gatewaybeatle.com");
	        } else {
	        	HttpResponse response = GatewayClientWrapper.executeGatewayRequest(client, requestPair.getKey(), "testuser1@gatewaybeatle.com");
	        }
	        
	        Logger.info("Response Received from gateway");
//	        validateResponseStatus(requestPair.getValue(), response);
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
	
	private boolean isContainsFileNameHeader(List<NameValuePair> headers) {
		
		Iterator<NameValuePair> iterator1 = headers.listIterator();
		boolean contains = false;
		while(iterator1.hasNext()) {
			NameValuePair header = iterator1.next();
			if (header.getName().equals("X-File-Name")) {
				contains = true;
				Logger.info("X-File-Name : " + header.getValue());
				break;
			}
		}
		
		return contains;
	}

	public static void main(String[] args) throws Exception{
		EPDV2SampleCIQTest t = new EPDV2SampleCIQTest();
		t.runSampleTest();
	}
	
}
