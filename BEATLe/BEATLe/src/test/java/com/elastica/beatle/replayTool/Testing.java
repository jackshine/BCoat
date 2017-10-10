/**
 * 
 */
package com.elastica.beatle.replayTool;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.elastica.beatle.RestClient.GatewayClient;
import com.elastica.beatle.replayTool.replayToolDTO.LogReplayReponseDTO;
import com.elastica.beatle.replayTool.replayToolDTO.LogReplayRequestDTO;

/**
 * @author anuvrath
 *
 */
public class Testing {
	
	/*@Test
	public void testingGW() throws FileNotFoundException, IOException, URISyntaxException{
		LogParser parser = new LogParser();
		List<InputFileRequestsDTO> requestList = parser.readRequestsFromFile(LogReplayConstant.TESTDATA);
		System.out.println(requestList.size());
	}*/
	
	public static void main(String[] args) throws Exception{
		
		
		LogParser parser = new LogParser();
		Map<LogReplayRequestDTO, LogReplayReponseDTO> requestList = parser.readRequestsFromFile(LogReplayConstant.TESTDATA);
		
		System.out.println(requestList.size());
		GatewayClient client = new GatewayClient();
		
		Iterator<Entry<LogReplayRequestDTO, LogReplayReponseDTO>> iterator = requestList.entrySet().iterator();
	    while (iterator.hasNext()) {
	        Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO> requestPair = (Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO>)iterator.next();
	        LogReplayRequestDTO request = requestPair.getKey();
	        /*HttpResponse response = GatewayClientWrapper.executeGatewayRequest(client,request);
	        System.out.println(response.getStatusLine().getStatusCode());
	        */
	        
	        if(request.getMethodType().equals("GET")){
	        	System.out.println("RequestHeaders:");
				
				request.getRequestHeader().add(new BasicNameValuePair("ELASTICA_MAGIC_COOKIE", new MagicCookie().getMagicCookie() + ":stresstest.user1@elastica.co"));
				Iterator<NameValuePair> iterator1 = request.getRequestHeader().listIterator();
				int refererIdx = -1;
				while(iterator1.hasNext()) {
					refererIdx++;
					NameValuePair header = iterator1.next();
					if (header.getName().equals("Referer")) {
						request.getRequestHeader().set(refererIdx, new BasicNameValuePair("Referer", "https://app.box.com/files/0/f/0"));
					}
					System.out.println(header.getName() + "###" + header.getValue());
				}
				
				client.doGet(request.getRequestURI(), request.getRequestHeader());
			}
	    }
	    		
	}
}
