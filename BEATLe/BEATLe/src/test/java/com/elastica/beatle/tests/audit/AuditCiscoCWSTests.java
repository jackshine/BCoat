	/**
 * 
 */
package com.elastica.beatle.tests.audit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.testng.annotations.Test;

import com.elastica.beatle.logger.Logger;

/**
 * @author anuvrath
 * 
 * Before we run this script we need to whitelist our system public IP with the Cisco CWS portal. 
 * Otherwise we will get 403 status instead of 200
 *
 */
public class AuditCiscoCWSTests {

	@Test
	public static void generateCWSTraffic() throws IOException, InterruptedException{		
		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec(System.getProperty("user.dir")+"/src/test/resources/Audit/CWS_TrafficScript/traffic_generator.sh");
		process.waitFor();
		BufferedReader bufReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String processStream = "";
		
		while((processStream = bufReader.readLine()) != null){
			Logger.info(processStream);
		}
	}	
}
