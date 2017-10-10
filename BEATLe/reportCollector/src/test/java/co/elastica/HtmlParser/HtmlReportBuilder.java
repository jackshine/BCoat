package co.elastica.HtmlParser;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import co.elastica.ReportDTO.ModuleReport;
import co.elastica.ReportDTO.ReportHolder;
import co.elastica.ReportDTO.TotalTestsReport;
import co.elastica.Utils.ReporterUtils;
import co.elastica.logger.Logger;

/**
 * @author anuvrath
 */
public class HtmlReportBuilder {
	HtmlAndXMLParser htmlParse;
	
	public HtmlReportBuilder(){
		htmlParse = new HtmlAndXMLParser();
	}
	/**
	 * @param rowData
	 * @param counter
	 * @return
	 * @throws IOException 
	 */
	public String buildReportRow(ReportHolder rowData, int counter){
		String htmlReport = "<tr><td>"+ counter +"</td><td>"+rowData.getTestName().trim()+"</td><td>"+rowData.getMethodsPassedCount()+"</td>";
		String failCount = rowData.getFailedTestCaseNumber();
		String skipCount = rowData.getSkippedTestCaseNumber();

		if(skipCount.equals("0")||skipCount.equals("NA"))
			htmlReport = htmlReport +"<td>"+rowData.getSkippedTestCaseNumber()+"</td>";
		else
			htmlReport = htmlReport +"<td style=\"background-color: #FF0000\">"+rowData.getSkippedTestCaseNumber()+"</td>";
		if(failCount.equals("0")||failCount.equals("NA"))				
			htmlReport = htmlReport +"<td>"+rowData.getFailedTestCaseNumber()+"</td>";							
		else
			htmlReport = htmlReport +"<td style=\"background-color: #FF0000\">"+rowData.getFailedTestCaseNumber()+"</td>";

		if(!rowData.getTimeConsumedForBuild().equals("NA"))
			htmlReport = htmlReport +"<td>"+ReporterUtils.convertTime(rowData.getTimeConsumedForBuild())+"</td>";
		else
			htmlReport = htmlReport +"<td>"+rowData.getTimeConsumedForBuild()+"</td>";

		htmlReport = htmlReport +"<td><a href="+rowData.getReportLink()+">Report</a></td><td>"+rowData.getOwner()+"</td>";
		htmlReport = htmlReport +"<td><a href="+rowData.getComment()+">Comments</a></td></tr>";
		return htmlReport;
	}
	
	/**
	 * @return
	 */
	private String getReportInstructionsSectionAndFooterHTML(){
		return "<div class=\"instruction\">"
				+ "Instruction to run these jobs:"
				+ "<ul>"
					+ "<li>Please connect to EoE VPN</li>"
					+ "<li>Navigate to this <a href=\"http://10.0.0.38:8080/\">link</a></li>"
					+ "<li>Select the job that you want to run and click build now</li>"
				+ "</ul> "
				+ "<b>NA: </b>Results are not available at this time. The report link in this row point to results of old build <br>"
				+ "<b>Skipped: </b>Tests are skipped because the dependent tests failed</div></body></html>";
	}
	
	/**
	 * @param htmlReport
	 * @param envName
	 * @param reportFor
	 * @param totalReport
	 * @return
	 */
	private String getTotalCountTable(String htmlReport,String envName, String reportFor,TotalTestsReport totalReport){
		htmlReport = htmlReport + "<table><thead><tr><th>"+ReporterUtils.getEnvironmentName(envName) +" "+ reportFor + " Report For Build Version: </th><th></th></tr></thead>"
				+ "<tr>"				
				+ "<td align=\"center\"><strong>"+htmlParse.getBuildVersion(envName)+"</strong></td>"
				+ "</tr></table>";
		htmlReport = htmlReport + "<table><thead><tr><th>Grand Total Tests</th><th>Tests </br> Passed</th><th>Tests </br> Skipped</th><th>Tests </br> Failed</th></th><tr></thead>";
		htmlReport = htmlReport + "<tr><td>Total</td><td>"+totalReport.getTotalMethodPassed()+"</td><td>"+totalReport.getTotalSkippedTests()+"</td><td>"+totalReport.getTotalFailedTests()+"</table><br>";
		htmlReport = htmlReport.replace("{GRAPH_CODE}", ReporterUtils.drawPieChart(totalReport.getTotalMethodPassed(),totalReport.getTotalSkippedTests(),totalReport.getTotalFailedTests()));
		return htmlReport;
	}
	
	/**
	 * @param completeReport
	 * @param envName
	 * @param reportFor
	 * @param totalReport
	 * @return
	 */
	public String buildFinalReport(Map<String,ModuleReport> completeReport,String envName, String reportFor,TotalTestsReport totalReport){
		Iterator<Entry<String, ModuleReport>> it = completeReport.entrySet().iterator();
		String htmlReport = getTotalCountTable(ReporterUtils.getRegressionReportHTMLHeader(),envName, reportFor, totalReport);
	    
		while (it.hasNext()) {
	        Map.Entry<String, ModuleReport> pair = (Map.Entry<String, ModuleReport>)it.next();
	        ModuleReport report1 = pair.getValue();	        	
	        htmlReport+=report1.getModuleReportTable();
	        htmlReport +="<tr><td></td><td>Total</td><td>"+report1.getModuleTotalReport().getTotalMethodPassed()
	        		+"</td><td>"+report1.getModuleTotalReport().getTotalSkippedTests()
	        		+"</td><td>"+report1.getModuleTotalReport().getTotalFailedTests()
	        		+"</td><td></td><td></td><td></td><td></td></table><br>";
	    }
	    htmlReport = htmlReport.concat(getReportInstructionsSectionAndFooterHTML());
		Logger.info("Report Built");			
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		try {
			Logger.info("Writing the Html to file");
			ReporterUtils.writeReportToFile(htmlReport, ReporterUtils.getReportName(envName));
			Logger.info("Final report written to file");
			Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return htmlReport;
	}
}