package co.elastica.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
/**
 * 
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import co.elastica.Constants.Constants;
import co.elastica.ReportDTO.ReportHolder;
import co.elastica.ReportDTO.TotalTestsReport;

/**
 * @author anuvrath
 *
 */
public class ReporterUtils {
	
	/**
	 * @param env
	 * @return
	 */
	public static String getEnvironmentName(String env){
		if(env.toLowerCase().contains("eoe")){
			return "EoE";
		}
		else if(env.toLowerCase().contains("qa")){
			return "QA-VPC";
		}
		else if(env.toLowerCase().contains("cep") || env.toLowerCase().contains("eu")){
			return "EU";
		}
		else if(env.toLowerCase().contains("prod")){
			return "Prod";
		}
		else
			return "";
	}

	/**
	 * @param slaveURL  
	 * @return
	 */
	public static String getOwner(String slaveURL) {		
		if(slaveURL.contains("update-Splunk Tests"))
			return "Anuvrath/Mayur";
		if(slaveURL.contains("Audit%20Web")|| slaveURL.contains("Audit%20S3")|| slaveURL.contains("Splunk")){
			return "Anuvrath";
		}	
		if(slaveURL.contains("Dashboard%20Reporting")){
			return "Rahul";
		}	
		if(slaveURL.contains("Audit%20SCP") || slaveURL.contains("Audit%20SpanVA") || slaveURL.contains("Audit%20DeviceLogs") || slaveURL.contains("Audit%20Datasources%20Equality") ||
				slaveURL.contains("Audit%20Negative") || slaveURL.contains("Audit%20TIA") || slaveURL.contains("Audit%20Tags%20and%20Comments%")||slaveURL.contains("SpanVA_Upgrades"))
			return "Mallesh";
		 
		if(slaveURL.contains("Feedback")||slaveURL.contains("Detect-rbac")){
			return "Rocky";	
		}
		if(slaveURL.contains("Audit_SpanVA_Latest_Image")){
			return "Mallesh/Anuvrath";	
		}
		
		else if(slaveURL.contains("Detect%20Preferences%20BE%20Regression")||slaveURL.contains("Detect%20BBI%20BE%20Regression")
				||slaveURL.contains("TBI")
				||slaveURL.contains("BBI")
				||slaveURL.contains("Detect%20Box")||slaveURL.contains("Detect%20DropBox")||slaveURL.contains("Detect%20Gdrive")
				||slaveURL.contains("Detect%20End%20to%20End")
				||slaveURL.contains("Detect%20Sequence%20Detector%20End%20to%20End%20BE%20Regression%20Tests")
				||slaveURL.contains("Detect%20One%20Step%20Sequence%20Detector%20Regression%20Tests%20Box%20%20GW%20")
				){
			return "Sagar";	
		}else if (slaveURL.contains("Detect-Sequence_detector-preference%20BE%20Regression%20")||slaveURL.contains("Detect%20GDrive%20OneStep%20Sequence%20End%20To%20End%20Regression%20Test")
				||slaveURL.contains("Detect-Dashboard%20Regression%20Tests%20")
				||slaveURL.contains("Detect%20One%20Step%20Sequence%20Detector%20Regression%20Tests%20Salesforce%20%20GW")
				||slaveURL.contains("Detect%20%20Sequence%20Detector%20Regression%20Tests%20Salesforce%20%20GW")) {
			return "Sagar/Ibrahim";
		} else if(slaveURL.contains("Detect"))
			return "Sagar";				
		
		if(slaveURL.contains("Protect"))
			return "Shriram/Mayur";				
		if(slaveURL.contains("DCI"))
			return "Eldo";
		
		if (slaveURL.contains("BE%20Replay")) {
			return "Rocky /Afjal";
		}
		
		if((slaveURL.contains("Gateway") || slaveURL.contains("Gatelet") || slaveURL.contains("GW"))
				&&(slaveURL.contains("FE") || slaveURL.contains("UI")))
			if (slaveURL.contains("GDrive")||slaveURL.contains("GDRIVE")||slaveURL.contains("GOOGLE")||slaveURL.contains("GMAIL")) {
				if(slaveURL.contains("CIQ")){
					return "Vijay G";	
				}
				return "Suresh";
			}else if (slaveURL.contains("BOX")||slaveURL.contains("Box")||slaveURL.contains("AZURE")) {
				if(slaveURL.contains("CIQ")){
					return "Vijay G";	
				}
				if(slaveURL.contains("DROPBOX")){
					return "Vijay G";	
				}
				return "Nissar";
			}else if (slaveURL.contains("Salesforce")||slaveURL.contains("SALESFORCE")||slaveURL.contains("AWS")) {
				if(slaveURL.contains("CIQ")){
					return "Vijay G";	
				}
				return "Mayur";
			}else if (slaveURL.contains("Office365")||slaveURL.contains("O365")) {
				if(slaveURL.contains("CIQ")){
					return "Vijay G";	
				}
				return "Usman/Suresh";
			}	
		if(slaveURL.contains("Securlet")) {
			if (slaveURL.contains("DropBox") || slaveURL.contains("GDrive") || slaveURL.contains("Yammer")) {
				return "Rahul";
			}
			if (slaveURL.contains("Onedrive")) {
				return "Maqsood";
			}
			if (slaveURL.contains("Gmail%20Mail%20ActivityLogs")) {
				return "Vijay Gangwar/Pushparaj";
			}
			if (slaveURL.contains("Gmail%20RBAC%20ActivityLogs")) {
				return "Vijay Gangwar";
			}
			if (slaveURL.contains("Office%20365%20Mail") || slaveURL.contains("Office365%20Mail") || slaveURL.contains("Gmail%20Exposure%20and%20Remediation") || slaveURL.contains("Gmail%20Dashboard")) {
				return "Nissar";
			}
			if(slaveURL.contains("AWS") || slaveURL.contains("SYSLOG"))
				return "Anuvrath";
			else if(slaveURL.contains("Securlet%20FE"))
				return "Laxman/Eldo";
			else {
				return "Pushparaj";
			}
		}
		if(slaveURL.contains("BOP")||slaveURL.contains("Infra"))
			return "Vijay Gangwar";
		if(slaveURL.contains("Dashboard"))
			return "Anuvrath";
		if(slaveURL.contains("Store%20FE")||slaveURL.contains("Detect%20FE")
				||slaveURL.contains("Audit%20FE")||slaveURL.contains("Login%20FE")
				||slaveURL.contains("CloudSoc%20FE")||slaveURL.contains("Sources%20FE")
				||slaveURL.contains("Investigate%20FE"))
			return "Laxman/Eldo";
		
		if(slaveURL.contains("SSO%20FE"))
			return "Suresh";
		
		return "Team-QA";
	}
	
	/* Returns the owner based on the test name
	 * @param testName  
	 * @return
	 */
	public static String getOwnerByTestName(String testName) {		
		
		if (testName.contains("Securlet - Dropbox"))
			return "Rahul";
		else if(testName.contains("update-Splunk Tests"))
			return "Anuvrath/Mayur";
		else if (testName.contains("DCI"))
			return "Eldo";
		else if (testName.contains("Detect UI"))
			return "Eldo";
		else if (testName.contains("Securlet - GDrive"))
			return "Rahul";
		else if (testName.contains("Securlet - Yammer"))
			return "Rahul";
		else if (testName.contains("Audit"))
			return "Anuvrath/ Mallesh";		
		else if (testName.contains("Securlets - Gmail ActivityLogs")) 
			return "Vijay Gangwar/Pushparaj";		
		else if (testName.contains("Securlet - Box"))
			return "Pushparaj";
		else if (testName.contains("Securlet - OneDrive"))
			return "Pushparaj";
		else if (testName.contains("Securlets Salesforce"))
			return "Pushparaj";
		else if (testName.contains("Securlets ServiceNow"))
			return "Pushparaj";
		else if (testName.contains("Protect"))
			return "Shriram/ Mayur";
		else if (testName.contains("Securlet - Office 365") || testName.contains("Securlet - Gmail Exposure and Remediation"))
			return "Nissar";	
		else if (testName.contains("Detect Sequence Detector Sanity Tests"))
			return "Sagar";
		else if (testName.contains("Detect TBI Sanity Tests"))
			return "Sagar";
		else if (testName.contains("Securlet AWS") || testName.contains("Dashboard Sanity")|| testName.contains("Splunk"))
			return "Anuvrath";
		else if (testName.contains("SSO"))
			return "Suresh";
		else if (testName.contains("Gateway"))
			return "Rocky /Afjal";
		else if (testName.contains("GW") || testName.contains("Gatelet"))
			if (testName.contains("GDrive")) {
				return "Suresh";
			}else if (testName.contains("Gatelet - Box")) {
				return "Nissar";
			}
			
			else if (testName.contains("Salesforce")) {
				return "Mayur";
			}else if (testName.contains("Office365")||testName.contains("O365")) {
				return "Usman/Suresh";
			}
		return "Team-QA";
	}
	
	public static String getFEOwnerByTestName(String testName) {		
		
		if (testName.contains("SSO"))
			return "Suresh";
		else if (testName.contains("Audit")||testName.contains("Securlet")
				||testName.contains("Sources")||testName.contains("Detect")
				||testName.contains("Investigate"))
			return "Laxman/Eldo";
		else if (testName.contains("Store")||testName.contains("Login")||testName.contains("CloudSoc"))
			return "Eldo";
		else if((testName.contains("Gateway") || testName.contains("Gatelet") || testName.contains("GW"))){
			if (testName.contains("GDrive")||testName.contains("GDRIVE")||testName.contains("GOOGLE")||testName.contains("GMAIL")) {
				if(testName.contains("CIQ")){
					return "Vijay G";	
				}
				return "Suresh";
			}else if (testName.contains("BOX")||testName.contains("Box")||testName.contains("AZURE")) {
				if(testName.contains("CIQ")){
					return "Vijay G";	
				}
				if(testName.contains("DROPBOX")){
					return "Vijay G";	
				}
				return "Nissar";
			}else if (testName.contains("Salesforce")||testName.contains("SALESFORCE")||testName.contains("AWS")) {
				if(testName.contains("CIQ")){
					return "Vijay G";	
				}
				return "Mayur";
			}else if (testName.contains("Office365")||testName.contains("O365")) {
				if(testName.contains("CIQ")){
					return "Vijay G";	
				}
				return "Usman/Suresh";
			}
		}
		return "Team-QA";
	}

	/**
	 * @return
	 */
	public static String setCommentURL(String slaveURL) {
		return slaveURL.split("/ws")[0].concat("/editDescription");
	}
	
	/**
	 * @return
	 */
	public static String getModulName(String slaveURL) {
		if(slaveURL.contains("Audit"))
			return "Audit";		
		if(slaveURL.contains("BOP") || slaveURL.contains("Infra")|| slaveURL.contains("Splunk"))
			return "Infra";
		if(slaveURL.contains("DCI"))
			return "DCI";
		if(slaveURL.contains("Detect") || slaveURL.contains("TIA"))
			return "Detect";
		if((slaveURL.contains("Gateway") || slaveURL.contains("Gatelet") || slaveURL.contains("GW"))){
			if(slaveURL.contains("Reach") || slaveURL.contains("REACH") || slaveURL.contains("REACH_AGENT")){
				return "GATELETS REACH";
			}
			else {
				return "GATELETS PAC";
			}
		}
		if(slaveURL.contains("Protect"))
			return "Protect(Securlets)";
		if(slaveURL.contains("Securlet"))
			return "Securlet";
		if(slaveURL.contains("Dashboard"))
			return "Dashboard";
		if(slaveURL.contains("SSO"))
			return "SSO";
		if(slaveURL.contains("Login"))
			return "Login";
		if(slaveURL.contains("Store"))
			return "Store";
		if(slaveURL.contains("CloudSoc"))
			return "CloudSoc";
		if(slaveURL.contains("Sources"))
			return "Sources";
		if(slaveURL.contains("Investigate"))
			return "Investigate";
		return null;
	}

	/**
	 * CALCULATE TEH TIME IN HOURS, MINUTES AND SECONDS FROM MILLISECONDS
	 * @param timeConsumedForBuild
	 * @return
	 */
	public static String convertTime(String timeConsumedForBuild) {
		long time = TimeUnit.MILLISECONDS.toSeconds(Long.valueOf(timeConsumedForBuild.replace(",", "").trim()));		
		if(time>60 && time<3600)
			return String.valueOf(time/(60) +" Min");
		else if(time>3600)
			return String.valueOf(time/(60*60) +" Hr");
		else
			return String.valueOf(time +" Sec");
	}
	
	/**
	 * @param report
	 * @param fileName
	 * @throws IOException
	 */
	public static void writeReportToFile(String report, String fileName) throws IOException{
		FileWriter writer = new FileWriter(new File(fileName));
		writer.write(report);
		writer.close();
	}
	
	/**
	 * @param moduleReport
	 * @return
	 */
	public static TotalTestsReport getModuleTotal(List<ReportHolder> moduleReport) {
		TotalTestsReport totalReport = new TotalTestsReport();
		for(ReportHolder report: moduleReport){
			if(!"NA".equals(report.getFailedTestCaseNumber()))
				totalReport.setTotalFailedTests(totalReport.getTotalFailedTests()+Integer.parseInt(report.getFailedTestCaseNumber().replaceAll(",", "")));
			if(!"NA".equals(report.getMethodsPassedCount()))
				totalReport.setTotalMethodPassed(totalReport.getTotalMethodPassed()+Integer.parseInt(report.getMethodsPassedCount().replaceAll(",", "")));
			if(!"NA".equals(report.getSkippedTestCaseNumber()))
				totalReport.setTotalSkippedTests(totalReport.getTotalSkippedTests()+Integer.parseInt(report.getSkippedTestCaseNumber().replaceAll(",", "")));			
		}
		return totalReport;
	}
	
	public static String getRegressionReportHTMLHeader(){
		return  "<html><head>{GRAPH_CODE}<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css\">"
				+"<meta http-equiv=\"Content-Type\"  content=\"text/html charset=UTF-8\" />"
				+ "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css\">"
				+ "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js\"></script><style type=\"text/css\">  body{margin-left:5%; margin-right:5%} 	pre { font-size: 100%;font-weight: bold; }table { width: 100%; border-collapse: collapse; } .BreakWord { word-break: break-all; } .instruction {border-width:2px; border-style:solid; padding: 10px} tr.pass{background: #FFFFFF} td {word-wrap:break-word; padding: .3em; border: 1px #ccc solid;overflow: hidden;} td.fail { background-color: #FF0000 } th { height: 50px; border: 2px #ccc solid; } thead { background: #CCCCFF; } </style></head><body>"
				+ "<div id=\"chart_div\"></div>";		
	}
	
	public static String getRegressionReportTableHeader(){
		return "<table><thead><tr><th>S.No</th><th>Tests</th><th>Tests </br> Passed</th><th>Tests </br> Skipped</th><th>Tests </br> Failed</th><th>Total </br> Time</th><th>Report</th><th>Owner</th><th>Comment</th></tr></thead>";
	}
	
	/**
	 * DRAW THE PIE CHART BASED ON THE PASS, FAIL AND SKIP COUNTS
	 * @param passCount
	 * @param skipCount
	 * @param failCount
	 * @return
	 */
	public static String drawPieChart(int passCount, int skipCount, int failCount) {
		int total = passCount + skipCount + failCount;		
		String img = "<img src=\"http://chart.apis.google.com/chart?chtt=Test+Results+Distribution&amp;chts=000000,12&amp;chs=300x150&amp;chf=bg,s,ffffff&amp;cht=p3&amp;chd=t:passtest,failtest,skiptest&amp;chl=Passed|Failed|Skipped&amp;chco=006600,ff0000,ffff00"
				+ "\"alt=\"Google Chart\"/>";
		img = img.replaceAll("passtest", Integer.toString(passCount * 100 / total));
		img = img.replaceAll("failtest", Integer.toString(failCount * 100 / total));
		img = img.replaceAll("skiptest", Integer.toString(skipCount * 100 / total));
		return img;
	}
	
	/**
	 * This method gets the latest build number for the Job for the Jenkins home page
	 * @param slaveURL
	 * @return
	 */
	public static String getCurrentBuildNumber(String slaveURL) {
		try{						
			if(slaveURL.contains("lastSuccessfulBuild/artifact"))
				slaveURL = slaveURL.replace("lastSuccessfulBuild/artifact", "ws");
			
			String base64login = "Basic cWEtamVua2luczopWWg8XXZ3XTJwfSNZakxm";
			Document doc = Jsoup.connect(slaveURL.split("/ws")[0]).header("Authorization", base64login).timeout(10000).get();
			return doc.select(Constants.BUILD_ID_CSS).text().split("#")[1];
		}catch(Exception e){
			e.printStackTrace();
		}		
		return "";
	}
	
	/**
	 * KEEPING TRACK OF THE TOATAL TESTS
	 * @param rowData
	 */
	public static TotalTestsReport setTotalCounts(ReportHolder rowData,TotalTestsReport totalReport) {
		if(!rowData.getMethodsPassedCount().equals("NA") && !rowData.getFailedTestCaseNumber().equals("NA") && !rowData.getSkippedTestCaseNumber().equals("NA")){
			if(totalReport.getTotalMethodPassed() == 0)
				totalReport.setTotalMethodPassed(Integer.parseInt(rowData.getMethodsPassedCount()));
			else
				totalReport.setTotalMethodPassed(totalReport.getTotalMethodPassed()+Integer.parseInt(rowData.getMethodsPassedCount().replaceAll(",", "")));				
			
			if(totalReport.getTotalSkippedTests() == 0)
				totalReport.setTotalSkippedTests(Integer.parseInt(rowData.getSkippedTestCaseNumber()));
			else
				totalReport.setTotalSkippedTests(totalReport.getTotalSkippedTests()+Integer.parseInt(rowData.getSkippedTestCaseNumber().replaceAll(",", "")));
			
			if(totalReport.getTotalFailedTests() == 0)
				totalReport.setTotalFailedTests(Integer.parseInt(rowData.getFailedTestCaseNumber()));
			else
				totalReport.setTotalFailedTests(totalReport.getTotalFailedTests()+Integer.parseInt(rowData.getFailedTestCaseNumber().replaceAll(",", "")));
		}		
		return totalReport;
	}
	
	public static String getReportName(String envName){
		if(envName.toLowerCase().contains("_fe_sanity"))
			return "FESanityReport.html";
		else if(envName.toLowerCase().contains("sanity"))
			return "SanityReport.html";
		else if(envName.toLowerCase().contains("_fe"))
			return "FERegressionReport.html";
		else
			return "regressionReport.html";
	}

	public static String getReportFor(String envName) {
		if(envName.toLowerCase().contains("sanity"))
			return "Sanity";
		else
			return "Regression";
	}
	
}
