package com.elastica.beatle.detect.dto;
public class Objects
{
    private boolean enabled;

    private int window;

    private int importance;

    private String resource_uri;

    private int confidence;

    private int threshold;


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


	/**
	 * @return the window
	 */
	public int getWindow() {
		return window;
	}


	/**
	 * @param window the window to set
	 */
	public void setWindow(int window) {
		this.window = window;
	}


	/**
	 * @return the importance
	 */
	public int getImportance() {
		return importance;
	}


	/**
	 * @param importance the importance to set
	 */
	public void setImportance(int importance) {
		this.importance = importance;
	}


	/**
	 * @return the resource_uri
	 */
	public String getResource_uri() {
		return resource_uri;
	}


	/**
	 * @param resource_uri the resource_uri to set
	 */
	public void setResource_uri(String resource_uri) {
		this.resource_uri = resource_uri;
	}


	/**
	 * @return the confidence
	 */
	public int getConfidence() {
		return confidence;
	}


	/**
	 * @param confidence the confidence to set
	 */
	public void setConfidence(int confidence) {
		this.confidence = confidence;
	}


	/**
	 * @return the threshold
	 */
	public int getThreshold() {
		return threshold;
	}


	/**
	 * @param threshold the threshold to set
	 */
	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}


	@Override
    public String toString()
    {
        return "ClassPojo [enabled = "+enabled+", window = "+window+", importance = "+importance+", resource_uri = "+resource_uri+", confidence = "+confidence+", threshold = "+threshold+"]";
    }
}