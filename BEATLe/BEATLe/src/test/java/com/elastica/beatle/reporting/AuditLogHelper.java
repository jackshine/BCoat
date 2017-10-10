package com.elastica.beatle.reporting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.testng.Reporter;

public class AuditLogHelper {
	
	public static void main(String[] args) throws Exception {
		
		String auditFilePath = "/Users/rahulkumar/NetBeansProjects/BackendAutomation/BeatleElastica/BEATLe/BEATLe/src/test/resources/reporting/suites/metrics/";
		String fileName = "zscaler_test";
		String tmplFile = auditFilePath + fileName + ".log";
		String targetFile = auditFilePath + fileName + "_latest.log";
		
		AuditLogHelper helper = new AuditLogHelper();
		helper.produceAuditLogs(tmplFile, targetFile, LogFormat.ZSCALER);
	}
	
	public enum LogFormat {
		BLUECOAT, ZSCALER
	}
	
	public void produceAuditLogs(String tmplFile, String resLogFile, LogFormat logDateFormat) {
		
		SimpleDateFormat logDateFormatter = getDateFormatter(logDateFormat);
    	
		try {
			Calendar cal = Calendar.getInstance();
			Date startDate = cal.getTime();
			long timeDiff = -1;
			long timeDiffDelta = -1;
			Reporter.log("New Start Date: " + logDateFormatter.format(startDate));
			
			BufferedReader br = new BufferedReader(new FileReader(tmplFile));
			File logFile = new File(resLogFile);
			logFile.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(logFile));
			int idx = 1;
		    for(String line; (line = br.readLine()) != null; idx++) {
		    	Reporter.log("Line Conent : " + line, true);
		    	
		    	if (line.length()==0) {
		    		bw.write(line + "\n");
		    		continue;
		    	}
		    	
		    	String outLine = "";
		    	if (line.startsWith("#")) {
		    		outLine = line;
		    	} else {
		    		Date logDate = getLogDate(line, logDateFormat);
		    		
		    		if (timeDiffDelta < 0) {
		    			timeDiffDelta = startDate.getTime() - logDate.getTime();
		    		}
		    		timeDiff = startDate.getTime() - logDate.getTime() - timeDiffDelta;
		    		
		    		Reporter.log("Time Difference : " + timeDiff, true);
		    		int timeDiffSec = (int) (timeDiff / 1000);
		    		Reporter.log("Time Difference in Seconds : " + timeDiffSec, true);
		    		cal.setTime(startDate);
		    		cal.add(Calendar.SECOND, (int) (timeDiffSec * -1));
		    		Date updatedDate = cal.getTime();
		    		Reporter.log("Updated Date: " + logDateFormatter.format(updatedDate), true);
		    		
		    		outLine = updateLogDate(line, updatedDate, logDateFormat);
		    	}
		    	
		        bw.write(outLine + "\n");
		    }
		    Reporter.log("Total Number of Logs : " + idx);
		    
		    bw.flush();
		    bw.close();
		    br.close();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Date getLogDate(String logLine, LogFormat logFormat) throws ParseException {
		
		String strLogDate = "";
		
		switch(logFormat) {
		case BLUECOAT:
			strLogDate = logLine.substring(0, 19);
			break;
		case ZSCALER:
			String[] logAttrs = logLine.split(",");
			strLogDate = logAttrs[0].substring(logAttrs[0].indexOf("=")+1);
			break;
		}
		
		Reporter.log("Date in Line : " + strLogDate, true);
		SimpleDateFormat logDateFormatter = getDateFormatter(logFormat);
		
		return logDateFormatter.parse(strLogDate);
	}
	
	public String updateLogDate(String logLine, Date updatedDate, LogFormat logFormat) {
		
		String strLog = "";
		SimpleDateFormat logDateFormatter = getDateFormatter(logFormat);
		
		switch(logFormat) {
		case BLUECOAT:
			strLog = logDateFormatter.format(updatedDate) + logLine.substring(19);
			break;
		case ZSCALER:
			strLog = "datetime=" + logDateFormatter.format(updatedDate) + logLine.substring(logLine.indexOf(","));
			break;
		}
		
		return strLog;
	}
	
	public SimpleDateFormat getDateFormatter(LogFormat logFormat) {
		SimpleDateFormat logDateFormatter = null;
		
		switch(logFormat) {
		case BLUECOAT:
			logDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			break;
		case ZSCALER:
			logDateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
			break;
		}
//		logDateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		return logDateFormatter;
	}
}
