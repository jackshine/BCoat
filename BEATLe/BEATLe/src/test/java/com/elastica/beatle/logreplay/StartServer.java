package com.elastica.beatle.logreplay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.testng.annotations.Test;

import com.elastica.beatle.gateway.CommonConfiguration;


public class StartServer extends CommonConfiguration{

	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		StartServer  ss= new StartServer();
		ss.injectActivityLogs();

	}*/
	
	@Test(groups ={"TEST", "SERVER"})
	public void  injectActivityLogs() {
		String consoleLog = "";
		System.out.println("userName: ");
		System.out.println("log folder: "+"");
		String userName ="madmin";
		String hostName="10.0.15.71";
		Runtime rt = Runtime.getRuntime();
		Process proc;
		String cmdEsImport3 ="sudo netstat -nlp";
		//String cmdEsImport4 ="sudo pkill python";
		//String cmdEsImport ="sudo python  milkyway/utilities/web/tools/log_replay_tool/log_replay_server.py  -p 443 -f ~/perf/test-logs/"+logFolderName+"/ -c ~/logreplay-server-ip-cert.pem -k ~/logreplay-server-key.pem";
		//String cmdEsImport = " sudo python milkyway/utilities/web/tools/log_replay_tool/log_replay_server.py   -p 8092 -f home/usman/perf/test-logs/box/ -c home/usman/logreplay-server-ip-cert.pem  -k home/usman/logreplay-server-key.pem ";
		
		//String cmdEsImport2 = " sudo python milkyway.o/utilities/web/tools/log_replay_tool/log_replay_client.py ";
		
		//String cmdEsImport1 = "sudo /home/madmin/venv/generic/bin/python /home/madmin/milkyway/utilities/ESScripts/esimport_bulk.py -i /home/" + userName + "/tests/" + fileName + " -t internal-eoe-es-1923969177.us-west-2.elb.amazonaws.com:9200";
		//String cmdInjectActivities = "ssh " + this.getRsaPath(userName) + userName + "@" + hostName + " " + cmdEsImport;
		String cmdInjectActivities3 = "ssh " + this.getRsaPath(userName) + userName + "@" + hostName + " " + cmdEsImport3;
		//String cmdInjectActivities4 = "ssh " + this.getRsaPath(userName) + userName + "@" + hostName + " " + cmdEsImport4;
		
		try {
			System.out.println("On Server see if already server running: " + cmdInjectActivities3);
			proc = rt.exec(cmdInjectActivities3);
			System.out.println("Runing command");
			Thread.sleep(10000);
			proc.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = "";

			while ((line = br.readLine()) != null) {
			  System.out.println("Output: "+line);
			  consoleLog += line;
			}

			br.close();
			
			/*System.out.println("Shutdown server if already running: " + cmdInjectActivities4);
			proc = rt.exec(cmdInjectActivities4);
			//Thread.sleep(10000);
			proc.waitFor();
			 br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			 line = "";

			while ((line = br.readLine()) != null) {
			  System.out.println(line);
			  consoleLog += line;
			}

			br.close();
			
			
			System.out.println("Start server with specified log folder: " + cmdInjectActivities);
			proc = rt.exec(cmdInjectActivities);
			//Thread.sleep(10000);
			proc.waitFor();
			 br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			 line = "";

			while ((line = br.readLine()) != null) {
			  System.out.println(line);
			  consoleLog += line;
			}

			br.close();
			
			
			System.out.println("On Server see if server up and running: " + cmdInjectActivities3);
			proc = rt.exec(cmdInjectActivities3);
			//Thread.sleep(10000);
			proc.waitFor();
			 br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			 line = "";

			while ((line = br.readLine()) != null) {
			  System.out.println(line);
			  consoleLog += line;
			}

			br.close();
			
			System.out.println("Close the server and release the resource: " + cmdInjectActivities4);
			proc = rt.exec(cmdInjectActivities4);
			//Thread.sleep(10000);
			proc.waitFor();
			 br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			 line = "";

			while ((line = br.readLine()) != null) {
			  System.out.println(line);
			  consoleLog += line;
			}

			br.close();
			*/
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public String getRsaPath(String userName) {
		
		if (userName.equalsIgnoreCase("madmin")) { 
			return "-i /var/jenkins/.ssh/id_rsa.tester ";
		} else {
			return "";
		}
	}

}
