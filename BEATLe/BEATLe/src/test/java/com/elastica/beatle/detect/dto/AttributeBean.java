package com.elastica.beatle.detect.dto;

public class AttributeBean {
	
	private Integer threshold, window, importance, confidence;
	private boolean enabled;
	private String fileName;
	private String ioi_code;
	/**
	 * @return the confidence
	 */
	public Integer getConfidence() {
		return confidence;
	}
	/**
	 * @param confidence the confidence to set
	 */
	public void setConfidence(Integer confidence) {
		this.confidence = confidence;
	}
	
	/**
	 * @return the ioi_code
	 */
	public String getIoi_code() {
		return ioi_code;
	}
	/**
	 * @param ioi_code the ioi_code to set
	 */
	public void setIoi_code(String ioi_code) {
		this.ioi_code = ioi_code;
	}
	/**
	 * @return the threshold
	 */
	public Integer getThreshold() {
		return threshold;
	}
	/**
	 * @param threshold the threshold to set
	 */
	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
	}
	/**
	 * @return the window
	 */
	public Integer getWindow() {
		return window;
	}
	/**
	 * @param window the window to set
	 */
	public void setWindow(Integer window) {
		this.window = window;
	}
	/**
	 * @return the importance
	 */
	public Integer getImportance() {
		return importance;
	}
	/**
	 * @param importance the importance to set
	 */
	public void setImportance(Integer importance) {
		this.importance = importance;
	}
	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}
	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "threshold=" + threshold +", confidence="+ confidence + ", window=" + window + ", importance=" + importance
				+ ", enabled=" + enabled ;
	}
	
	public AttributeBean(String ioi_code, boolean enabled, Integer threshold, Integer window, Integer importance) {
		this.ioi_code = ioi_code;
		this.enabled = enabled;
		this.threshold = threshold;
		this.window = window;
		this.importance = importance;
	}
	
	public AttributeBean(String ioi_code, boolean enabled, Integer confidence, Integer importance) {
		this.ioi_code = ioi_code;
		this.enabled = enabled;
		this.confidence = confidence;
		this.importance = importance;
	}
	
	public AttributeBean(Integer threshold, Integer window, Integer importance, String fileName) {
		super();
		this.threshold = threshold;
		this.window = window;
		this.importance = importance;
		this.fileName = fileName;
	}
	public AttributeBean(Integer threshold, Integer window, Integer importance, boolean enabled) {
		super();
		this.threshold = threshold;
		this.window = window;
		this.importance = importance;
		this.enabled = enabled;
	}
	
	public AttributeBean(Integer confidence, Integer importance, boolean enabled) {
		super();
		this.confidence = confidence;
		this.importance = importance;
		this.enabled = enabled;
	}
	public AttributeBean() {
		// TODO Auto-generated constructor stub
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	

}
