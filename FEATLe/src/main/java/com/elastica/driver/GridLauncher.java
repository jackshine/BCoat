package com.elastica.driver;

import java.io.File;

import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.common.SeleniumProtocol;
import org.openqa.grid.internal.utils.GridHubConfiguration;
import org.openqa.grid.internal.utils.SelfRegisteringRemote;
import org.openqa.grid.web.Hub;
import org.openqa.selenium.net.PortProber;
import org.openqa.selenium.server.SeleniumServer;

public class GridLauncher {

	public synchronized Hub launchHub(int port) throws Exception{
		
		GridHubConfiguration config = new GridHubConfiguration();
	    config.setHost("localhost");
	    config.setPort(port);
	    Hub hub = new Hub(config);
	    hub.start();

		return hub;
	}

	public synchronized void shutdownHub(Hub hub) throws Exception {
		hub.stop();
	}

	public synchronized SelfRegisteringRemote launchNode(Hub hub) throws Exception{
		RegistrationRequest req = RegistrationRequest.build("-role", "node" , "-host", "localhost");
	    req.getConfiguration().put(RegistrationRequest.PORT, PortProber.findFreePort());
	    String url =
	        "http://" + req.getConfiguration().get(RegistrationRequest.HOST) + ":"
	            + req.getConfiguration().get(RegistrationRequest.PORT);
	    req.getConfiguration().put(RegistrationRequest.REMOTE_HOST, url);
	    req.getConfiguration().put(RegistrationRequest.HUB_HOST, hub.getHost());
	    req.getConfiguration().put(RegistrationRequest.HUB_PORT, hub.getPort());
	    req.getConfiguration().put(RegistrationRequest.SELENIUM_PROTOCOL, SeleniumProtocol.WebDriver);
	   
	    
	    if (System.getProperty("os.name").contains("Mac")) {
			req.getConfiguration().put("webdriver.chrome.driver", "/usr/local/selenium/chromedriver");
		} else {
			req.getConfiguration().put("webdriver.chrome.driver", System.getProperty("user.dir") +File.separator+"src"+File.separator+"test"+File.separator+
						"resources"+File.separator+"lib"+File.separator+"chromedriver.exe");
		}
	   
	    SelfRegisteringRemote remote = new SelfRegisteringRemote(req);
		remote.setRemoteServer(new SeleniumServer(remote.getConfiguration()));
		remote.startRemoteServer();
		remote.startRegistrationProcess();
		
		return remote;
	}
	
	public synchronized void shutdownNode(SelfRegisteringRemote remote) throws Exception {
		remote.stopRemoteServer();
	}



}
