/**
 * 
 */
package co.elastica.ReportDTO;

/**
 * @author anuvrath
 *
 */
public class ModuleReport {
	private int moduleCounter;
	private TotalTestsReport moduleTotalReport;
	private String moduleReportTable;
	/**
	 * @return the moduleCounter
	 */
	public int getModuleCounter() {
		return moduleCounter;
	}
	/**
	 * @param moduleCounter the moduleCounter to set
	 */
	public void setModuleCounter(int moduleCounter) {
		this.moduleCounter = moduleCounter;
	}
	/**
	 * @return the moduleTotalReport
	 */
	public TotalTestsReport getModuleTotalReport() {
		return moduleTotalReport;
	}
	/**
	 * @param moduleTotalReport the moduleTotalReport to set
	 */
	public void setModuleTotalReport(TotalTestsReport moduleTotalReport) {
		this.moduleTotalReport = moduleTotalReport;
	}
	/**
	 * @return the moduleReportTable
	 */
	public String getModuleReportTable() {
		return moduleReportTable;
	}
	/**
	 * @param moduleReportTable the moduleReportTable to set
	 */
	public void setModuleReportTable(String moduleReportTable) {
		this.moduleReportTable = moduleReportTable;
	}	
}
