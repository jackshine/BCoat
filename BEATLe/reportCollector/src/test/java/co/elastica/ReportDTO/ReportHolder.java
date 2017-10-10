/**
 * 
 */
package co.elastica.ReportDTO;

/**
 * @author anuvrath
 *
 */
public class ReportHolder {
	private String testName;
	private String methodsPassedCount;
	private String skippedTestCaseNumber;
	private String failedTestCaseNumber;
	private String timeConsumedForBuild;
	private String reportLink; 
	private String owner; 
	private String comment;
	private String moduleName;
	
	/**
	 * @return the moduleName
	 */
	public String getModuleName() {
		return moduleName;
	}
	/**
	 * @param moduleName the moduleName to set
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	/**
	 * @return the testName
	 */
	public String getTestName() {
		return testName;
	}
	/**
	 * @param testName the testName to set
	 */
	public void setTestName(String testName) {
		this.testName = testName;
	}
	/**
	 * @return the methodsPassedCount
	 */
	public String getMethodsPassedCount() {
		return methodsPassedCount;
	}
	/**
	 * @param methodsPassedCount the methodsPassedCount to set
	 */
	public void setMethodsPassedCount(String methodsPassedCount) {
		this.methodsPassedCount = methodsPassedCount;
	}
	/**
	 * @return the skippedTestCaseNumber
	 */
	public String getSkippedTestCaseNumber() {
		return skippedTestCaseNumber;
	}
	/**
	 * @param skippedTestCaseNumber the skippedTestCaseNumber to set
	 */
	public void setSkippedTestCaseNumber(String skippedTestCaseNumber) {
		this.skippedTestCaseNumber = skippedTestCaseNumber;
	}
	/**
	 * @return the failedTestCaseNumber
	 */
	public String getFailedTestCaseNumber() {
		return failedTestCaseNumber;
	}
	/**
	 * @param failedTestCaseNumber the failedTestCaseNumber to set
	 */
	public void setFailedTestCaseNumber(String failedTestCaseNumber) {
		this.failedTestCaseNumber = failedTestCaseNumber;
	}
	/**
	 * @return the timeConsumedForBuild
	 */
	public String getTimeConsumedForBuild() {
		return timeConsumedForBuild;
	}
	/**
	 * @param timeConsumedForBuild the timeConsumedForBuild to set
	 */
	public void setTimeConsumedForBuild(String timeConsumedForBuild) {
		this.timeConsumedForBuild = timeConsumedForBuild;
	}
	/**
	 * @return the reportLink
	 */
	public String getReportLink() {
		return reportLink;
	}
	/**
	 * @param reportLink the reportLink to set
	 */
	public void setReportLink(String reportLink) {
		this.reportLink = reportLink;
	}
	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}
	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
}
