/**
 * 
 */
package co.elastica.HtmlParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

import co.elastica.Constants.Constants;
import co.elastica.DataHelper.DataReader;
import co.elastica.ReportDTO.Report;
import co.elastica.ReportDTO.ReportHolder;
import co.elastica.Utils.ReporterUtils;
import co.elastica.logger.Logger;

/**
 * @author anuvrath
 *
 */
public class HtmlAndXMLParser {
	
	private DataReader dataReader;
	private boolean isAllLinksWorked = true;
	
	public HtmlAndXMLParser() {
		super();
		dataReader = new DataReader();
	}
	/**
	 * Get the build version against the env 
	 * @param envName
	 * @return
	 */
	public String getBuildVersion(String envName) {
		return readJenkinsReportFromLink(dataReader.getEnvironmentBuildVersionURL(envName)).body().text();
	}
	
	/**
	 * @param slaveURL
	 * @return
	 */
	private Document readJenkinsReportFromLink(String slaveURL) {
		try {
			return Jsoup.connect(slaveURL).header("Authorization", Constants.JENKINS_AUTHORIZATION_TOKEN).timeout(10000).get();
		} catch (IOException e) {
			Logger.error("Looks like this Job is still running OR the job results are not available result collection: "+slaveURL);
		}
		return null;
	}
	
	/**
	 * @param slaveURL
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws MessagingException 
	 * @throws AddressException 
	 */
	public List<Report> readReportAndGetData(List<String> slaveURLList) throws AddressException, MessagingException, SAXException, IOException, ParserConfigurationException {

		List<String> URLNotAvailable = new ArrayList<String>();
		List<Report> report = new ArrayList<Report>();

		for(String slaveURL: slaveURLList){			
			Logger.info("Pulling the report for: "+slaveURL);
			Document doc = readJenkinsReportFromLink(slaveURL);
			if(doc!= null){
				Report urlReport = new Report();
				urlReport.setModuleName(ReporterUtils.getModulName(slaveURL));
				List<ReportHolder> moduleReport = parseHTMLData(doc, slaveURL, true);
				urlReport.setModuleTotals(ReporterUtils.getModuleTotal(moduleReport));
				urlReport.setReport(moduleReport);
				report.add(urlReport);
			}
			else{
				isAllLinksWorked = false;
				URLNotAvailable.add(slaveURL);
			}			
		}	
		if(!isAllLinksWorked)
			report.addAll(sendMissingReportsToAutoTeamAndGetTestName(URLNotAvailable));
		return report;
	}
	
	/**
	 * @param uRLNotAvailable
	 */
	private List<Report> sendMissingReportsToAutoTeamAndGetTestName(List<String> uRLNotAvailable) {
		List<Report> report = new ArrayList<Report>();
		boolean sendEmail =true;
		if(uRLNotAvailable.size()>0){
			String messageBody = "Looks like following Job(s) is/are still running and results are not available for result collector job:\n ";
			messageBody = messageBody +"<html><head></head><body><table><thead><tr><th>SI <br> No</th><th>Job</th><th> Owner</th></tr></thead>"
					+ "<style type=\"text/css\"> table { width: 100%; border-collapse: collapse; } .BreakWord { word-break: break-all; } .instruction {border-width:2px; border-style:solid; padding: 10px} tr.pass{background: #FFFFFF} td {word-wrap:break-word; padding: .3em; border: 1px #ccc solid;overflow: hidden;} td.fail{background: #FF0000} th { height: 50px; border: 2px #ccc solid; } thead { background: #fc9; } </style>";
			int counter = 1;
			for(String slaveURL : uRLNotAvailable){
				Logger.info("Report not found for "+slaveURL);
				String slaveURLMod = "";
				if(slaveURL.contains("BEATLe"))
					slaveURLMod = slaveURL.split("/ws")[0]+Constants.BEATLE_LASTSUCCESSFUL_URI;
				else 
					slaveURLMod = slaveURL.split("/ws")[0]+Constants.FEATLE_LASTSUCCESSFUL_URI;
				Report urlReport = new Report();
				urlReport.setModuleName(ReporterUtils.getModulName(slaveURLMod));
				List<ReportHolder> moduleReport = parseHTMLData(readJenkinsReportFromLink(slaveURLMod), slaveURLMod, false);
				if(moduleReport.size()>0){
					urlReport.setModuleTotals(ReporterUtils.getModuleTotal(moduleReport));
					urlReport.setReport(moduleReport);				
					messageBody = messageBody +"<tr><td>"+counter++ +"</td><td>"+slaveURLMod+" </td><td> "+ReporterUtils.getOwner(slaveURLMod)+"</td><tr>";
					report.add(urlReport);
				}		
				else {
					sendEmail = false;
				}
			}
			messageBody = messageBody +"</table></body></html>";
			
			if(uRLNotAvailable.size()>0 && sendEmail ){
//				MailHandler.sendEmail(messageBody,"owner");
			}
		}
		return report;
	}
	
	private List<ReportHolder> parseHTMLData(Document doc, String slaveURL, boolean isAvailable) {
		List<ReportHolder> htmlFilesData = new ArrayList<ReportHolder>();
		if(doc != null){
			int reportTableSize = doc.select(Constants.HTML_TABLE_CSS).size();
			
			if(reportTableSize >= 2){
				for(int count = 3; count <= reportTableSize;count++){
					Elements rowData = doc.select(Constants.HTML_TABLE_ROWDATA_CSS.replace("{iterator}", String.valueOf(count)));
					if(rowData.size()>0){
						ReportHolder holder = new ReportHolder();
						
						holder.setTestName(rowData.get(0).text());																
						holder.setModuleName(ReporterUtils.getModulName(holder.getTestName()));
						holder.setComment(ReporterUtils.setCommentURL(slaveURL));
						
						if (rowData.get(0).text().contains("FE Sanity Test")||rowData.get(0).text().contains("UI Sanity Test")) 
							holder.setOwner(ReporterUtils.getFEOwnerByTestName(rowData.get(0).text()));
						else if (rowData.get(0).text().contains("Sanity Test")) 
							holder.setOwner(ReporterUtils.getOwnerByTestName(rowData.get(0).text()));
						else if (rowData.get(0).text().contains("FE")||rowData.get(0).text().contains("UI")) 
							holder.setOwner(ReporterUtils.getFEOwnerByTestName(rowData.get(0).text()));
						else
							holder.setOwner(ReporterUtils.getOwner(slaveURL));					

						if(isAvailable){						
							holder.setMethodsPassedCount(rowData.get(1).text());					
							holder.setSkippedTestCaseNumber(rowData.get(2).text());
							holder.setFailedTestCaseNumber(rowData.get(3).text());
							holder.setTimeConsumedForBuild(rowData.get(4).text());
							
							String buildNumber = ReporterUtils.getCurrentBuildNumber(slaveURL);	
							if(buildNumber.isEmpty()||buildNumber.equals(" ")){
								holder.setReportLink(slaveURL.replace("ws", "ws"));
							}else{
								holder.setReportLink(slaveURL.replace("ws", buildNumber+"/artifact"));
							}
						}
						else{
							holder.setMethodsPassedCount("NA");					
							holder.setSkippedTestCaseNumber("NA");
							holder.setFailedTestCaseNumber("NA");
							holder.setTimeConsumedForBuild("NA");
							holder.setReportLink(slaveURL.split("ws/")[0]);
						}										
						htmlFilesData.add(holder);
					}					
				}				
			}
		}		
		return htmlFilesData;
	}
}