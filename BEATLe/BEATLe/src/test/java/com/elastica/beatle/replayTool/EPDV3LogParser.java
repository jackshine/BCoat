package com.elastica.beatle.replayTool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.elastica.beatle.S3Utils.S3ActionHandler;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.replayTool.replayToolDTO.LogReplayReponseDTO;
import com.elastica.beatle.replayTool.replayToolDTO.LogReplayRequestDTO;

/**
 * 
 * @author rocky
 *
 */
public class EPDV3LogParser {

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
	
	private enum LogVersions { Version22("@@@Version: 2.2"),Version23("@@@Version: 2.3"),UNKNOWN("unknown");	 
	private final String version;	 

	/**
	 * @param version
	 */
	private LogVersions(String version) {
		this.version = version; 
	}	 

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() { 
		return version; 
	}

	/**
	 * @param value
	 * @return
	 */
	public static boolean contains(String value) 
	{
		for (LogVersions version : LogVersions.values()) {
			if(version.version.equals(value))
				return true;
		}
		return false;
	}
	};

	public Map<LogReplayRequestDTO, LogReplayReponseDTO> readRequestsFromFile(String fileName) throws FileNotFoundException, IOException, URISyntaxException {
		
		String completeFilePath = FileHandlingUtils.getFileAbsolutePath(LogReplayConstant.REPLAY_LOGFOLDER_PATH+fileName);
		File logFile = new File(completeFilePath);
		if(!logFile.isFile()) {
			Logger.info("Downloading File " + fileName + " from S3!!!");
			try {
				S3ActionHandler s3 = new S3ActionHandler();
				s3.downloadFileFromS3Bucket(LogReplayConstant.S3_BUCKET, LogReplayConstant.S3_FOLDER + fileName, completeFilePath);
				logFile = new File(completeFilePath);
			} catch (Exception ex) {
				Logger.info("Downloading File " + fileName + " from S3 is failed with exception : " + ex.getLocalizedMessage());
			}
		} else {
			Logger.info("File " + fileName + " exists in Local so Proceeding!!!");
		}
		
		Logger.info("Processing data from :" + completeFilePath);		
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(logFile)));	 
		Map<LogReplayRequestDTO, LogReplayReponseDTO> requestMap = new LinkedHashMap<>();
		
		String currentProcessingLine = bufferReader.readLine();	//Log related json object - skipping
		currentProcessingLine = bufferReader.readLine();
		if (LogVersions.contains(currentProcessingLine)) {
			
			while (currentProcessingLine != null && LogVersions.contains(currentProcessingLine)) {
				currentProcessingLine = bufferReader.readLine();	//skipping @@@Version tag.
				currentProcessingLine = bufferReader.readLine();	//skipping @@@Time tag.
				currentProcessingLine = bufferReader.readLine();	//skipping @@@RequestHeader tag.
				
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
							if (!currentProcessingLine.isEmpty()) {
								String[] header = currentProcessingLine.split(": ");
								String key = null, value = null;
								if (header.length == 2) {
									key = header[0].trim();
									value = header[1].trim();
								} else if (header.length == 1) {
									key = header[0].trim();
									value = "";
								} else {
									continue;
								}
								responseHeader.add(new BasicNameValuePair(key,value));
							}
						}
						response.setResponseHeader(responseHeader);	
						if("@@@ResponseBody".equals(currentProcessingLine) && currentProcessingLine != null){								
							if((currentProcessingLine = bufferReader.readLine()) != null && !LogVersions.contains(currentProcessingLine)){								
								StringBuilder strBldr = new StringBuilder();
								while((currentProcessingLine = bufferReader.readLine()) != null && !LogVersions.contains(currentProcessingLine))									
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
			Logger.error("EPD Version is Unknown. Please use EPDV3 trainer logs");
		
		bufferReader.close();
		return requestMap;
	}
}
