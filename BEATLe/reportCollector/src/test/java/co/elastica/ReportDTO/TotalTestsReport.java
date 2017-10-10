/**
 * 
 */
package co.elastica.ReportDTO;

/**
 * @author anuvrath
 *
 */
public class TotalTestsReport {

	private int TotalSkippedTests;
	private int TotalFailedTests;
	private int TotalMethodPassed;
	
	public TotalTestsReport(){
		this.TotalFailedTests = 0;
		this.TotalMethodPassed = 0;
		this.TotalSkippedTests = 0;
	}
		
	/**
	 * @return the totalMethodPassed
	 */
	public int getTotalMethodPassed() {
		return TotalMethodPassed;
	}
	/**
	 * @param totalMethodPassed the totalMethodPassed to set
	 */
	public void setTotalMethodPassed(int totalMethodPassed) {
		TotalMethodPassed = totalMethodPassed;
	}

	/**
	 * @return the totalSkippedTests
	 */
	public int getTotalSkippedTests() {
		return TotalSkippedTests;
	}
	/**
	 * @param totalSkippedTests the totalSkippedTests to set
	 */
	public void setTotalSkippedTests(int totalSkippedTests) {
		TotalSkippedTests = totalSkippedTests;
	}
	/**
	 * @return the totalFailedTests
	 */
	public int getTotalFailedTests() {
		return TotalFailedTests;
	}
	/**
	 * @param totalFailedTests the totalFailedTests to set
	 */
	public void setTotalFailedTests(int totalFailedTests) {
		TotalFailedTests = totalFailedTests;
	}
}