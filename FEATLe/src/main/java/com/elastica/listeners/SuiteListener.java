package com.elastica.listeners;

import org.testng.IExecutionListener;

import com.elastica.logger.Logger;

/**
 * Helper messages for tests suite level
 * @author Eldo Rajan
 *
 */
public class SuiteListener implements IExecutionListener {
	private long startTime;
	
	@Override
	public void onExecutionStart() {
		startTime = System.currentTimeMillis();
		Logger.info("**********************Test Suite is going to start**********************");
	}

	@Override
	public void onExecutionFinish() {
		Logger.info("**********************Test Suite has completed, took around " + ((System.currentTimeMillis() - startTime)/60000) + " minutes**********************");
	}
}