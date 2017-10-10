package com.elastica.listeners;

import java.io.File;

import org.openqa.grid.internal.utils.SelfRegisteringRemote;
import org.openqa.grid.web.Hub;
import org.testng.IExecutionListener;

import com.elastica.driver.GridLauncher;
import com.elastica.logger.Logger;

/**
 * Helper messages for tests suite level
 * @author Eldo Rajan
 *
 */
public class GridListener implements IExecutionListener {
	Hub hub = null;
	SelfRegisteringRemote node1 = null;
	SelfRegisteringRemote node2 = null;
	SelfRegisteringRemote node3 = null;
	SelfRegisteringRemote node4 = null;
	GridLauncher grid=null;
	
	public synchronized void onExecutionStart() {
		grid = new GridLauncher();
		if (System.getProperty("os.name").contains("Mac")) {
			System.setProperty("webdriver.chrome.driver", "/usr/local/selenium/chromedriver");
		} else {
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") +File.separator+"src"+File.separator+"test"+File.separator+
					"resources"+File.separator+"lib"+File.separator+"chromedriver.exe");
		}
		
		Logger.info("**********************Selenium Grid launching in progress**********************");
		Logger.info("**********************Selenium Grid Hub launching in progress**********************");
		try {
			hub = grid.launchHub(4444);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Logger.info("**********************Selenium Grid Hub launching completed**********************");
		
		Logger.info("**********************Selenium Grid Nodes launching in progress**********************");
		try {
		node1 = grid.launchNode(hub);
		node2 = grid.launchNode(hub);
		node3 = grid.launchNode(hub);
		node4 = grid.launchNode(hub);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Logger.info("**********************Selenium Grid Nodes launching completed**********************");
		
		Logger.info("**********************Selenium Grid launching completed**********************");
	}

	@Override
	public synchronized void onExecutionFinish() {
		Logger.info("**********************Selenium Grid shutdown in progress**********************");
		
		Logger.info("**********************Selenium Grid Nodes shutdown in progress**********************");
		try {
			grid.shutdownNode(node1);
			grid.shutdownNode(node2);
			grid.shutdownNode(node3);
			grid.shutdownNode(node4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Logger.info("**********************Selenium Grid Nodes shutdown completed**********************");
		
		Logger.info("**********************Selenium Grid Hub shutdown in progress**********************");
		try {
			grid.shutdownHub(hub);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Logger.info("**********************Selenium Grid Hub shutdown completed**********************");
		
		Logger.info("**********************Selenium Grid shutdown completed**********************");
	}
}