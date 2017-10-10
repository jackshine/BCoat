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

public class EPDV2SampleSalesforceCIQTest {
	
	public void runSampleTest() throws UnsupportedEncodingException, Exception {
		
		HttpHost gateway = new HttpHost("10.0.52.30", 443, "http");
		GatewayClient client = new GatewayClient(gateway);
		
		LogParser parser = new LogParser();
		String replayFileName = "Salesforce/Salesforce,Files,upload_Test_txt.log";
		Map<LogReplayRequestDTO, LogReplayReponseDTO> requestList = parser.readRequestsFromFile(replayFileName);
		
		Logger.info("Number of Requests to be Played : " + requestList.size());
		
		Logger.info("\n Started Replaying Logs\n");
		
		Iterator<Entry<LogReplayRequestDTO, LogReplayReponseDTO>> iterator = requestList.entrySet().iterator();
	    while (iterator.hasNext()) {
	        Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO> requestPair = (Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO>)iterator.next();
	        NameValuePair multipartHeader = getMultipartFormDataHeader(requestPair.getKey().getRequestHeader());
	        if (multipartHeader != null) {
	        	Logger.info("Upload Request");
	        	
	        	MultipartEntityBuilder builder = MultipartEntityBuilder.create();
	        	builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
	        	
	        	Logger.info("Header Name : " + multipartHeader.getName() + " : " + multipartHeader.getValue());
	        	String boundary = getBoundaryFromMPHeader(multipartHeader);
	        	
	        	String reqBody = requestPair.getKey().getRequestBody();
	        	Logger.info("Request Body : " + reqBody);
	        	String[] bodyMultiPart = reqBody.split("--" + boundary);
	        	for (int i=1; i<bodyMultiPart.length; i++) {
	        		String mpItem = bodyMultiPart[i];
	        		String[] mpSplit = mpItem.split(";");
	        		if (mpSplit.length>2) {
	        			String s = mpSplit[1].trim();
	        			String name = s.substring(s.indexOf("\"")+1, s.length()-1);
	        			s = mpSplit[2].trim();
	        			String[] ss = s.split("\"");
	        			
	        			String fileName = ss[1];
	    	        	String completeFilePath = FileHandlingUtils.getFileAbsolutePath(LogReplayConstant.REPLAY_LOGFOLDER_PATH + "uploadFiles/" + fileName);
	    	        	File file = new File(completeFilePath);
	    	        	builder.addBinaryBody(name, file, ContentType.TEXT_PLAIN, fileName);
	        		} else if (mpSplit.length == 2) {
	        			String s = mpSplit[1].trim();
	        			String[] ss = s.split("\"");
	        			if (ss.length>2) {
	        				builder.addTextBody(ss[1], ss[2], ContentType.TEXT_PLAIN);
	        			} else if (ss.length == 2){
	        				builder.addTextBody(ss[1], "", ContentType.TEXT_PLAIN);
	        			}
	        		}
	        	}
	        	
	        	HttpEntity entity = builder.build();
	        	HttpResponse response = GatewayClientWrapper.executeGatewayRequest(client, requestPair.getKey(), entity, "testuser1@gatewaybeatle.com");
	        } else {
	        	HttpResponse response = GatewayClientWrapper.executeGatewayRequest(client, requestPair.getKey(), "testuser1@gatewaybeatle.com");
	        }
	        
	        Logger.info("Response Received from gateway");
//	        validateResponseStatus(requestPair.getValue(), response);
	    }
	}
	
	private String getBoundaryFromMPHeader(NameValuePair header) {
		String boundaryLbl = "boundary=";
		String value = header.getValue();
		String boundary = value.substring(value.indexOf(boundaryLbl)+boundaryLbl.length());
		
		return boundary;
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
	
	private NameValuePair getMultipartFormDataHeader(List<NameValuePair> headers) {
		NameValuePair multipartHeader = null;
		Iterator<NameValuePair> iterator1 = headers.listIterator();
		while(iterator1.hasNext()) {
			NameValuePair header = iterator1.next();
			if (header.getName().equals("Content-Type") && header.getValue().startsWith("multipart/form-data")) {
				multipartHeader = header;
				Logger.info("Content-Type : " + header.getValue());
				break;
			}
		}
		
		return multipartHeader;
	}
	
	public static void main(String[] args) throws Exception{
		EPDV2SampleSalesforceCIQTest t = new EPDV2SampleSalesforceCIQTest();
		t.runSampleTest();
	}
	
}
