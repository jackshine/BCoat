/**
 * 
 */
package com.elastica.beatle.replayTool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
//import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.replayTool.replayToolDTO.LogReplayReponseDTO;
import com.elastica.beatle.replayTool.replayToolDTO.LogReplayRequestDTO;
import com.elastica.beatle.S3Utils.S3ActionHandler;
import com.elastica.beatle.fileHandler.FileHandlingUtils;

/**
 * @author anuvrath
 *
 */
public class LogParser {
	
	private enum RequestType { GET, POST, DELETE, PUT,OPTIONS;
	
		/**
		 * @param value
		 * @return
		 */
		public static boolean contains(String value) 
		{
		    for (RequestType type : RequestType.values()) 		    
		        if (type.name().equals(value)) 
		            return true;		        
		    
		    return false;
		}
	};	
		
	
	/**
	 * @author anuvrath
	 *
	 */
	private enum ResponseType { HTTP11("HTTP/1.1"),HTTP10("HTTP/1.0"),UNKNOWN("unknown");	 
	    private final String Type;	 
	    
	    /**
	     * @param type
	     */
	    private ResponseType(String type) {
	    	this.Type = type; 
	    }	 
	    
	    /* (non-Javadoc)
	     * @see java.lang.Enum#toString()
	     */
	    @Override
	    public String toString() { 
	    	return Type; 
	    }
	    
	    /**
	     * @param value
	     * @return
	     */
	    public static boolean contains(String value) 
		{
		    for (ResponseType type : ResponseType.values()) {
		    	if(type.Type.equals(value))
		    		return true;
		    }
		    return false;
		}
	};
	
	/**
	 * @param logFilePath
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public Map<LogReplayRequestDTO, LogReplayReponseDTO> readRequestsFromFile(String logFilePath) throws FileNotFoundException, IOException, URISyntaxException{
		String completeFilePath = FileHandlingUtils.getFileAbsolutePath(LogReplayConstant.REPLAY_LOGFOLDER_PATH+logFilePath);
		File logFile = new File(completeFilePath);
		if(!logFile.isFile()) {
			Logger.info("Downloading File " + logFilePath + " from S3!!!");
			try {
				S3ActionHandler s3 = new S3ActionHandler();
				s3.downloadFileFromS3Bucket(LogReplayConstant.S3_BUCKET, LogReplayConstant.S3_FOLDER + logFilePath, completeFilePath);
				logFile = new File(completeFilePath);
			} catch (Exception ex) {
				Logger.info("Downloading File " + logFilePath + " from S3 is failed with exception : " + ex.getLocalizedMessage());
			}
		} else {
			Logger.info("File " + logFilePath + " exists in Local so Proceeding!!!");
		}
		
		Logger.info("Processing data from :" + completeFilePath);
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(logFile)));	 
		Map<LogReplayRequestDTO, LogReplayReponseDTO> requestMap = new LinkedHashMap<>();		
		String currentProcessingLine = bufferReader.readLine();

		if(currentProcessingLine.equals("@@@version 2")){
			currentProcessingLine = bufferReader.readLine();
			while (currentProcessingLine != null && currentProcessingLine.equals("@@@RequestHeader")) {															
				Logger.info("Parsing the "+currentProcessingLine);
				currentProcessingLine = bufferReader.readLine();
					
				String url = currentProcessingLine;
				LogReplayRequestDTO request = new LogReplayRequestDTO();
				LogReplayReponseDTO response = new LogReplayReponseDTO();
				Logger.info("Collecting the request details for: "+currentProcessingLine);
					
				String[] requestLine = currentProcessingLine.split(" ");
				if(RequestType.contains(requestLine[0])){					
					request.setMethodType(requestLine[0]);
					String strURI = StringUtils.replaceEach(requestLine[1], new String[]{"{", "}", "|"}, new String[]{"%7B", "%7D", "%7C"});
					request.setRequestURI(new URI(strURI));
					request.setHttpRequestVersion(requestLine[2]);
					//currentProcessingLine = bufferReader.readLine();
					List<NameValuePair> requestHeader = new ArrayList<>();
					while(!(currentProcessingLine = bufferReader.readLine()).equals("@@@RequestBody") && !currentProcessingLine.equals("@@@ResponseHeader")){
						//Maqsood: added a condition to skip this header as the StringEntity is adding this header
						if(request.getMethodType().equals("PUT")|| request.getMethodType().equals("POST")){
							if (!currentProcessingLine.contains("Content-Length") && !currentProcessingLine.contains("x-elastica_gw")){  
								requestHeader.add(new BasicNameValuePair(currentProcessingLine.split(": ")[0].trim(), currentProcessingLine.split(": ")[1].trim()));
							}
						}													
						else
							requestHeader.add(new BasicNameValuePair(currentProcessingLine.split(": ")[0].trim(), currentProcessingLine.split(": ")[1].trim()));
					}
					request.setRequestHeader(requestHeader);
					if("@@@RequestBody".equals(currentProcessingLine)){
						StringBuilder strBldr = new StringBuilder();
						while(!(currentProcessingLine = bufferReader.readLine()).equals("@@@ResponseHeader"))
							strBldr.append(currentProcessingLine);								
						request.setRequestBody(strBldr.toString());
					}																
				}
				
				// Processing @@@ResponseHeader				
				if(currentProcessingLine.equals("@@@ResponseHeader")){
					currentProcessingLine = bufferReader.readLine();
					String[] responseLine = currentProcessingLine.split(" ");
					if(ResponseType.contains(responseLine[0])){
						Logger.info("Collecting the response details for: "+url);
							
						response.setResponseStatus(currentProcessingLine);
						List<NameValuePair> responseHeader =  new ArrayList<>();
							
						while(!(currentProcessingLine = bufferReader.readLine()).equals("@@@ResponseBody") && !currentProcessingLine.equals("@@@RequestHeader")){
							responseHeader.add(new BasicNameValuePair(currentProcessingLine.split(": ")[0].trim(),currentProcessingLine.split(": ")[1].trim()));
						}
						response.setResponseHeader(responseHeader);	
						if("@@@ResponseBody".equals(currentProcessingLine) && currentProcessingLine != null){								
							if((currentProcessingLine = bufferReader.readLine()) != null && !currentProcessingLine.equals("@@@RequestHeader")){								
								StringBuilder strBldr = new StringBuilder();
								while((currentProcessingLine = bufferReader.readLine()) != null && !currentProcessingLine.equals("@@@RequestHeader"))									
									strBldr.append(currentProcessingLine);								
								response.setResponseBody(strBldr.toString());
							}																	
						}	
					}
				}
				else
					Logger.error("Response details not found for this request");
				requestMap.put(request,response);
			}												
		}
		else
			Logger.error("EPD Version is Unknown. Please use Version 2 EPD logs");
		
		bufferReader.close();
		return requestMap;
	}
}

/**
 * @Depricated as there is new version of EPD tool
 * @param logFilePath
 * @throws FileNotFoundException
 * @throws IOException
 * @throws URISyntaxException
 *//*
public Map<LogReplayRequestDTO, LogReplayReponseDTO> readRequestsFromFile(String logFilePath) throws FileNotFoundException, IOException, URISyntaxException{
	String completeFilePath = FileHandlingUtils.getFileAbsolutePath(LogReplayConstant.REPLAY_LOGFOLDER_PATH+logFilePath);
	Logger.info("Processind data from :"+completeFilePath);
	BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(completeFilePath))));	 
	String currentProcessingLine = null;
	Map<LogReplayRequestDTO, LogReplayReponseDTO> requestMap = new HashMap<>();
	
	while ((currentProcessingLine = bufferReader.readLine()) != null) {			
		if(!currentProcessingLine.matches("")){
			Logger.info("Started prcessing and collecting data for :"+currentProcessingLine);
			String[] requestLine = currentProcessingLine.split(" ");
			LogReplayRequestDTO request = new LogReplayRequestDTO();
			LogReplayReponseDTO response = new LogReplayReponseDTO();
			if(RequestType.contains(requestLine[0])){
				request.setMethodType(requestLine[0]);
				request.setRequestURI(new URI(requestLine[1]));
				request.setHttpRequestVersion(requestLine[2]);
				List<NameValuePair> requestHeader =  new ArrayList<>();
				while(!(currentProcessingLine = bufferReader.readLine()).matches("")){
					String[] headerLine = currentProcessingLine.split(":");
					requestHeader.add(new BasicNameValuePair(headerLine[0].trim(),headerLine[1].trim()));
				}
				request.setRequestHeader(requestHeader);
				if(request.getMethodType().equals("GET")){
					if(currentProcessingLine.matches(""))
						currentProcessingLine = bufferReader.readLine();
				}				
				else if(request.getMethodType().equals("POST")|| request.getMethodType().equals("PUT")){
					request.setRequestBody(bufferReader.readLine());
					currentProcessingLine = bufferReader.readLine();
				}					
				
				currentProcessingLine = bufferReader.readLine();
				if(ResponseType.contains(currentProcessingLine.split(" ")[0]))
				{
					
					response.setResponseStatus(currentProcessingLine);
					List<NameValuePair> responseHeader =  new ArrayList<>();
					while(!(currentProcessingLine = bufferReader.readLine()).matches("")){
						String[] respheaderLine = currentProcessingLine.split(":");
						responseHeader.add(new BasicNameValuePair(respheaderLine[0].trim(),respheaderLine[1].trim()));
					}
					response.setResponseHeader(responseHeader);
					currentProcessingLine = bufferReader.readLine();
					if(!currentProcessingLine.matches(""))
						response.setResponseBody(currentProcessingLine);													
				}	
				requestMap.put(request, response);
			}
			else
				Logger.error("Unknow request found");
		}
	}
	bufferReader.close();
	return requestMap;
}*/