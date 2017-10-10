package com.elastica.beatle.logreplay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class LogReplayClient extends Thread{

	String hostName="10.0.0.63";
	String userName="usman";
	String logFolderName="";
	String testUserNameFile="";
		
	public LogReplayClient(String userName, String logFolderName, String testUserFileName){
		this.logFolderName=logFolderName;
		this.testUserNameFile=testUserFileName;
		this.userName=userName;
	}
	
	
		/*public static void main(String[] args) {
			// TODO Auto-generated method stub
			LogReplayClient ssha = new LogReplayClient("usman","box", "perf1_user.txt");
			ssha.logReplayClint();
			
		}*/

		
		public  String logReplayClint() throws InterruptedException {
			
			String consoleLog = "";
			Thread.sleep(20000);
			System.out.println("User NAme: "+userName);
			System.out.println("Log folder name: "+logFolderName);
			System.out.println("Test User NAme: "+testUserNameFile);
			
			Runtime rt = Runtime.getRuntime();
			
		
			Process proc;
			String cmdEsImport3 ="sudo netstat -nlp";
			String cmdEsImport4 ="sudo pkill python";
			String cmdEsImport ="sudo python milkyway.o/utilities/web/tools/log_replay_tool/log_replay_client.py  --magic-cookie-auth "+testUserNameFile+"  --http-proxy 10.0.0.244:3128  --https-proxy 10.0.0.244:3128 --log-folder ~/perf/test-logs/"+logFolderName+"/  --parallel 1  --iterations 1";
			//String cmdEsImport = " sudo python milkyway/utilities/web/tools/log_replay_tool/log_replay_server.py   -p 8092 -f home/usman/perf/test-logs/box/ -c home/usman/logreplay-server-ip-cert.pem  -k home/usman/logreplay-server-key.pem ";
			
			String cmdEsImport2 = " sudo python milkyway.o/utilities/web/tools/log_replay_tool/log_replay_client.py ";
			
			//String cmdEsImport1 = "sudo /home/madmin/venv/generic/bin/python /home/madmin/milkyway/utilities/ESScripts/esimport_bulk.py -i /home/" + userName + "/tests/" + fileName + " -t internal-eoe-es-1923969177.us-west-2.elb.amazonaws.com:9200";
			String cmdInjectActivities = "ssh " + this.getRsaPath(userName) + userName + "@" + hostName + " " + cmdEsImport;
			hostName="10.0.15.71";
			String cmdInjectActivities3 = "ssh " + this.getRsaPath(userName) + userName + "@" + hostName + " " + cmdEsImport3;
			String cmdInjectActivities4 = "ssh " + this.getRsaPath(userName) + userName + "@" + hostName + " " + cmdEsImport4;
			
			try {
				/*System.out.println("Inject Activities to ES CMD: " + cmdInjectActivities3);
				proc = rt.exec(cmdInjectActivities3);
				//Thread.sleep(10000);
				proc.waitFor();
				BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				String line = "";

				while ((line = br.readLine()) != null) {
				  System.out.println(line);
				  consoleLog += line;
				}

				br.close();
				
				System.out.println("Inject Activities to ES CMD: " + cmdInjectActivities4);
				proc = rt.exec(cmdInjectActivities4);
				//Thread.sleep(10000);
				proc.waitFor();
				 br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				 line = "";

				while ((line = br.readLine()) != null) {
				  System.out.println(line);
				  consoleLog += line;
				}

				br.close();*/
				
				
				System.out.println("Log reply client: " + cmdInjectActivities);
				proc = rt.exec(cmdInjectActivities);
				//Thread.sleep(10000);
				proc.waitFor();
				BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				String line = "";

				while ((line = br.readLine()) != null) {
				  System.out.println(line);
				  consoleLog += line;
				}

				br.close();
				
				Thread.sleep(30000);
				System.out.println("client to server see server running: " + cmdInjectActivities3);
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
				
				System.out.println("Shutdown Server: " + cmdInjectActivities4);
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
				
				System.out.println("Inject Activities to ES CMD: " + cmdInjectActivities3);
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
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return consoleLog;
		}
		
		
	public String getRsaPath(String userName) {
			
			if (userName.equalsIgnoreCase("madmin")) { 
				return "-i /var/jenkins/.ssh/id_rsa.tester ";
			} else {
				return "";
			}
		}
		
		
	
	 public void run() {
			
		 System.out.println("Client thread");
			try {
				logReplayClint();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		
		    System.out.println("Client thread completed");
		  }
	
		/*Runtime runt=Runtime.getRuntime();
		Process p1 = runt.exec("ssh 10.0.0.63" );
		Process p=runt.exec("ls -l");
		
		PrintStream out = new PrintStream(p.getOutputStream());
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		Thread.sleep(3000);
		while (in.ready()) {
		   String s = in.readLine();
		   System.out.println(s);
		}

		out.println("ls /home/usman/");
		Thread.sleep(3000);
		while (in.ready()) {
		    String s = in.readLine();
		    System.out.println(s);
		}
		out.println("exit");*/


}	
	
