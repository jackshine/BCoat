package com.elastica.beatle.es;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ESScripts {
	
	public String getRsaPath(String userName) {
		
		if (userName.equalsIgnoreCase("madmin")) { 
			return "-i /var/jenkins/.ssh/id_rsa.tester ";
		} else {
			return "";
		}
	}
	
	public String scpActivityLogs(String fileName, String filePath, String hostName, String userName) {
		String consoleLog = "";
		
		System.out.println(filePath);
		
		Runtime rt = Runtime.getRuntime();
		Process proc;
		
		String cmdSCP = "scp " + this.getRsaPath(userName) + filePath + fileName + " " + userName + "@" + hostName + ":/home/" + userName + "/tests/";
		try {
			System.out.println("scp CMD: " + cmdSCP);
			proc = rt.exec(cmdSCP);
			proc.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = "";

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

	public String injectActivityLogs(String fileName, String hostName, String userName) {
		String consoleLog = "";
		
		Runtime rt = Runtime.getRuntime();
		Process proc;
		String cmdEsImport = "sudo /home/madmin/venv/generic/bin/python /home/madmin/milkyway/utilities/ESScripts/esimport_bulk.py -i /home/" + userName + "/tests/" + fileName + " -t internal-eoe-es-1923969177.us-west-2.elb.amazonaws.com:9200";
		String cmdInjectActivities = "ssh " + this.getRsaPath(userName) + userName + "@" + hostName + " " + cmdEsImport;
		
		try {
			System.out.println("Inject Activities to ES CMD: " + cmdInjectActivities);
			proc = rt.exec(cmdInjectActivities);
			proc.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = "";

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

}
