/**
 * 
 */
package com.elastica.beatle.tests.gatelet;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;

import com.elastica.beatle.RestClient.GatewayClient;
import com.elastica.beatle.RestClient.GatewayClientWrapper;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.replayTool.LogParser;
import com.elastica.beatle.replayTool.LogReplayConstant;
import com.elastica.beatle.replayTool.replayToolDTO.LogReplayReponseDTO;
import com.elastica.beatle.replayTool.replayToolDTO.LogReplayRequestDTO;

/**
 * @author anuvrath
 *
 */
public class dropBoxLogReplay {
	
	public boolean createFolderDropBoxlogReplay() throws Exception{
		Map<LogReplayRequestDTO, LogReplayReponseDTO> requestList = new LogParser().readRequestsFromFile(LogReplayConstant.DROP_BOX_CREATEFOLDER_LOG);
		Logger.info("********* Starting replaying logs *********\n");
		Iterator<Entry<LogReplayRequestDTO, LogReplayReponseDTO>> iterator = requestList.entrySet().iterator();
		boolean status = true;
	    while (iterator.hasNext() && status) {
	        Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO> requestPair = (Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO>)iterator.next();	        	        
	        HttpResponse response = GatewayClientWrapper.executeGatewayRequest(new GatewayClient(), requestPair.getKey());
	        Logger.info("Response Received from gateway");
	        status = validateResponse(requestPair.getValue(), response);	        
	    }
		return false;
	}
	
	public boolean loginDropBoxDropBoxlogReplay() throws Exception{
		Map<LogReplayRequestDTO, LogReplayReponseDTO> requestList = new LogParser().readRequestsFromFile(LogReplayConstant.DROP_BOX_LOGIN_LOG);
		Logger.info("********* Starting replaying logs *********\n");
		Iterator<Entry<LogReplayRequestDTO, LogReplayReponseDTO>> iterator = requestList.entrySet().iterator();
		boolean status = true;
	    while (iterator.hasNext() && status) {
	        Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO> requestPair = (Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO>)iterator.next();	        	        
	        HttpResponse response = GatewayClientWrapper.executeGatewayRequest(new GatewayClient(), requestPair.getKey());
	        Logger.info("Response Received from gateway");
	        status = validateResponse(requestPair.getValue(), response);	        
	    }
		return false;
	}
	
	public boolean logOutDropBoxDropBoxlogReplay() throws Exception{
		Map<LogReplayRequestDTO, LogReplayReponseDTO> requestList = new LogParser().readRequestsFromFile(LogReplayConstant.DROP_BOX_LOGOUT_LOG);
		Logger.info("********* Starting replaying logs *********\n");
		Iterator<Entry<LogReplayRequestDTO, LogReplayReponseDTO>> iterator = requestList.entrySet().iterator();
		boolean status = true;
	    while (iterator.hasNext() && status) {
	        Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO> requestPair = (Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO>)iterator.next();	        	        
	        HttpResponse response = GatewayClientWrapper.executeGatewayRequest(new GatewayClient(), requestPair.getKey());
	        Logger.info("Response Received from gateway");
	        status = validateResponse(requestPair.getValue(), response);	        
	    }
		return false;
	}
	
	public boolean OpenUploadedFileDropBoxlogReplay() throws Exception{
		Map<LogReplayRequestDTO, LogReplayReponseDTO> requestList = new LogParser().readRequestsFromFile(LogReplayConstant.DROP_BOX_OPENUPLOAD_LOG);
		Logger.info("********* Starting replaying logs *********\n");
		Iterator<Entry<LogReplayRequestDTO, LogReplayReponseDTO>> iterator = requestList.entrySet().iterator();
		boolean status = true;
	    while (iterator.hasNext() && status) {
	        Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO> requestPair = (Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO>)iterator.next();	        	        
	        HttpResponse response = GatewayClientWrapper.executeGatewayRequest(new GatewayClient(), requestPair.getKey());
	        Logger.info("Response Received from gateway");
	        status = validateResponse(requestPair.getValue(), response);	        
	    }
		return false;
	}
	
	public boolean renameFolderDropBoxlogReplay() throws Exception{
		Map<LogReplayRequestDTO, LogReplayReponseDTO> requestList = new LogParser().readRequestsFromFile(LogReplayConstant.DROP_BOX_RENAMEFOLDER_LOG);
		Logger.info("********* Starting replaying logs *********\n");
		Iterator<Entry<LogReplayRequestDTO, LogReplayReponseDTO>> iterator = requestList.entrySet().iterator();
		boolean status = true;
	    while (iterator.hasNext() && status) {
	        Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO> requestPair = (Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO>)iterator.next();	        	        
	        HttpResponse response = GatewayClientWrapper.executeGatewayRequest(new GatewayClient(), requestPair.getKey());
	        Logger.info("Response Received from gateway");
	        status = validateResponse(requestPair.getValue(), response);	        
	    }
		return false;
	}
	
	public boolean uploadFileDropBoxlogReplay() throws Exception{
		Map<LogReplayRequestDTO, LogReplayReponseDTO> requestList = new LogParser().readRequestsFromFile(LogReplayConstant.DROP_BOX_UPLOADFILE_LOG);
		Logger.info("********* Starting replaying logs *********\n");
		Iterator<Entry<LogReplayRequestDTO, LogReplayReponseDTO>> iterator = requestList.entrySet().iterator();
		boolean status = true;
	    while (iterator.hasNext() && status) {
	        Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO> requestPair = (Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO>)iterator.next();	        	        
	        HttpResponse response = GatewayClientWrapper.executeGatewayRequest(new GatewayClient(), requestPair.getKey());
	        Logger.info("Response Received from gateway");
	        status = validateResponse(requestPair.getValue(), response);	        
	    }
		return false;
	}
	
	/**
	 * @param value
	 * @param response
	 * @return
	 */
	private boolean validateResponse(LogReplayReponseDTO value, HttpResponse response) {
		Logger.info("Validating the response");
		if(value.getResponseStatus().trim().equals(response.getStatusLine().toString())){
			Logger.info("Response status matched");
			return true;
		}
		return false;
	}

	public static void main(String[] args) throws Exception{
		dropBoxLogReplay obj =  new dropBoxLogReplay();
		obj.createFolderDropBoxlogReplay();
	}
}