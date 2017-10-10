/**
 * 
 */
package co.elastica.ReportDTO;

import java.util.List;

/**
 * @author anuvrath
 *
 */
public class Report {
	private String ModuleName;
	private List<ReportHolder> report;
	private TotalTestsReport moduleTotals;
	
	/**
	 * @return the moduleName
	 */
	public String getModuleName() {
		return ModuleName;
	}
	/**
	 * @param moduleName the moduleName to set
	 */
	public void setModuleName(String moduleName) {
		ModuleName = moduleName;
	}
	/**
	 * @return the report
	 */
	public List<ReportHolder> getReport() {
		return report;
	}
	/**
	 * @param report the report to set
	 */
	public void setReport(List<ReportHolder> report) {
		this.report = report;
	}
	/**
	 * @return the moduleTotals
	 */
	public TotalTestsReport getModuleTotals() {
		return moduleTotals;
	}
	/**
	 * @param moduleTotals the moduleTotals to set
	 */
	public void setModuleTotals(TotalTestsReport moduleTotals) {
		this.moduleTotals = moduleTotals;
	}
}