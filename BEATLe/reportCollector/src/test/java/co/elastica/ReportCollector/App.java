package co.elastica.ReportCollector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.mail.EmailException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;
import co.elastica.DataHelper.DataReader;
import co.elastica.EMailHandler.MailHandler;
import co.elastica.HtmlParser.HtmlAndXMLParser;
import co.elastica.HtmlParser.HtmlReportBuilder;
import co.elastica.ReportDTO.ModuleReport;
import co.elastica.ReportDTO.Report;
import co.elastica.ReportDTO.ReportHolder;
import co.elastica.ReportDTO.TotalTestsReport;
import co.elastica.Utils.ReporterUtils;
import javax.mail.MessagingException;
import javax.xml.parsers.ParserConfigurationException;

/**
 * @author anuvrath
 *
 */
public class App {
	
	Map<String,ModuleReport> completeReport;
	private TotalTestsReport totalReport;
	private HtmlAndXMLParser htmlParse;
	private DataReader dataReader;
	private MailHandler mailHandler;
	private HtmlReportBuilder htmlReportBuilder;
	

	@BeforeClass(alwaysRun=true)
	public void initReporter(){
		dataReader = new DataReader();
		mailHandler = new MailHandler();
		htmlParse = new HtmlAndXMLParser();		
		totalReport = new TotalTestsReport();
		completeReport = new HashMap<String, ModuleReport>();
		htmlReportBuilder = new HtmlReportBuilder();
	}

	/**
	 * @param envName
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws EmailException
	 * @throws MessagingException
	 */
	@Test()
	@Parameters("envName")
	public void getReports(String envName) throws SAXException, IOException, ParserConfigurationException, EmailException, MessagingException{
		List<Report> buildReports = new ArrayList<Report>();
		buildReports.addAll(htmlParse.readReportAndGetData(dataReader.getSlaveURL(envName)));
		String reportFor = ReporterUtils.getReportFor(envName);
		buildDifferentModuleTables(buildReports,reportFor,ReporterUtils.getReportName(envName));			
		if (buildReports.size() > 0 && (totalReport.getTotalFailedTests() > 0 || totalReport.getTotalSkippedTests() > 0 )){
			mailHandler.sendEmail(htmlReportBuilder.buildFinalReport(completeReport,envName,reportFor,totalReport), envName);
			/*String s3ReportName = reportName.split(".html")[0]+"_"+ReporterUtils.getCurrentTimeInHourMinuteSecond()+".html";
			new S3Archive().pushReportToS3(envName+"_"+buildVersion, new ByteArrayInputStream(htmlReport.getBytes(Charset.forName("UTF-8"))),s3ReportName);*/
		}
	}
	
	/**
	 * @param report
	 * @param reportFor
	 * @param reportName
	 * @throws IOException
	 */
	private void buildDifferentModuleTables(List<Report> report,String reportFor,String reportName) throws IOException{
		for(Report reportData: report){
			List<ReportHolder> rowData  = reportData.getReport();
			if(reportFor.toLowerCase().equals("sanity")){
				for(ReportHolder linkData: rowData){
					if(linkData.getModuleName()!= null)
						populateSanityModuleTableData(linkData.getModuleName(),linkData, reportFor);
				}
			}else if(reportFor.toLowerCase().equals("regression")){
				populateRegressionModuleTableData(reportData,rowData);
			}
		}
	}
	
	/**
	 * @param reportData
	 * @param rowData
	 */
	private void populateRegressionModuleTableData(Report reportData, List<ReportHolder> rowData) {
		if(completeReport.containsKey(reportData.getModuleName())){
			ModuleReport report = completeReport.get(reportData.getModuleName());
			TotalTestsReport totalReport = report.getModuleTotalReport();
			totalReport.setTotalFailedTests(totalReport.getTotalFailedTests()+reportData.getModuleTotals().getTotalFailedTests());
			totalReport.setTotalMethodPassed(totalReport.getTotalMethodPassed()+reportData.getModuleTotals().getTotalMethodPassed());
			totalReport.setTotalSkippedTests(totalReport.getTotalSkippedTests()+reportData.getModuleTotals().getTotalSkippedTests());
				
			report.setModuleCounter(report.getModuleCounter()+1);
			report.setModuleTotalReport(totalReport);
			report.setModuleReportTable(report.getModuleReportTable().concat(getReportRow(reportData.getReport(),report.getModuleCounter())));				
		}else{
			ModuleReport report = new ModuleReport();
			TotalTestsReport totalReport = new TotalTestsReport();				
			totalReport.setTotalFailedTests(reportData.getModuleTotals().getTotalFailedTests());
			totalReport.setTotalMethodPassed(reportData.getModuleTotals().getTotalMethodPassed());
			totalReport.setTotalSkippedTests(reportData.getModuleTotals().getTotalSkippedTests());
			report.setModuleTotalReport(totalReport);
				
			report.setModuleReportTable(ReporterUtils.getRegressionReportTableHeader());
			report.setModuleReportTable(report.getModuleReportTable().concat("<pre>"+reportData.getModuleName()+" Regression Tests </pre>"));
			report.setModuleReportTable(report.getModuleReportTable()+ getReportRow(reportData.getReport(),1));
			report.setModuleCounter(1);
			completeReport.put(reportData.getModuleName(), report);
		}			
	}

	/**
	 * @param moduleName
	 * @param linkData
	 * @param reportFor
	 * @throws IOException
	 */
	private void populateSanityModuleTableData(String moduleName, ReportHolder linkData,String reportFor) throws IOException {
		
		if(!linkData.getSkippedTestCaseNumber().equals("NA") && !linkData.getMethodsPassedCount().equals("NA") && !linkData.getFailedTestCaseNumber().equals("NA")){
			if(completeReport.containsKey(moduleName)){
				ModuleReport report = completeReport.get(moduleName);
				TotalTestsReport totalReport = report.getModuleTotalReport();
				totalReport.setTotalFailedTests(totalReport.getTotalFailedTests()+Integer.parseInt(linkData.getFailedTestCaseNumber()));
				totalReport.setTotalMethodPassed(totalReport.getTotalMethodPassed()+Integer.parseInt(linkData.getMethodsPassedCount()));
				totalReport.setTotalSkippedTests(totalReport.getTotalSkippedTests()+Integer.parseInt(linkData.getSkippedTestCaseNumber()));
				report.setModuleCounter(report.getModuleCounter()+1);
				report.setModuleTotalReport(totalReport);
				report.setModuleReportTable(report.getModuleReportTable().concat(htmlReportBuilder.buildReportRow(linkData,report.getModuleCounter())));				
			}else{
				ModuleReport report = new ModuleReport();
				TotalTestsReport totalReport = new TotalTestsReport();				
				totalReport.setTotalFailedTests(Integer.parseInt(linkData.getFailedTestCaseNumber()));
				totalReport.setTotalMethodPassed(Integer.parseInt(linkData.getMethodsPassedCount()));
				totalReport.setTotalSkippedTests(Integer.parseInt(linkData.getSkippedTestCaseNumber()));
				report.setModuleTotalReport(totalReport);
				report.setModuleReportTable(ReporterUtils.getRegressionReportTableHeader());
				report.setModuleReportTable(report.getModuleReportTable().concat("<pre>"+moduleName+" Sanity Tests </pre>"));
				report.setModuleReportTable(report.getModuleReportTable()+ htmlReportBuilder.buildReportRow(linkData,1));
				report.setModuleCounter(1);
				completeReport.put(moduleName, report);
			}			
			totalReport = ReporterUtils.setTotalCounts(linkData,totalReport);
		}
	}

	/**
	 * BUILD A ROW IN THE HTML REPORT FOR EACH RECORD IN THE URL LINK
	 * @param report
	 * @return
	 */
	private String getReportRow(List<ReportHolder> report, int counter) {
		String htmlReport =""; 
		for(ReportHolder rowData : report){
			htmlReport = htmlReport + htmlReportBuilder.buildReportRow(rowData, counter++);
			totalReport = ReporterUtils.setTotalCounts(rowData,totalReport);
		}

		return htmlReport;
	}
	
}