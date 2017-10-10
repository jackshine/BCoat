package com.elastica.driver;

/**
 * Browser Types
 * @author Eldo Rajan
 *
 */
public enum BrowserType {
	Firefox("firefox"),
	InternetExplorer("ie"),
	Chrome("chrome"),
	Safari("safari"),
	HtmlUnit("htmlunit"),	
	PhantomJS("phantomjs"),
	Opera("opera"),
	Android("android"),
	IPhone("iphone"),
	IPad("ipad")
	;

	public static BrowserType getBrowserType(final String browserType) {
		BrowserType bType=null;
		try{
			if (browserType.equalsIgnoreCase("firefox")) {
				bType=BrowserType.Firefox;
			} else if (browserType.equalsIgnoreCase("ie")) {
				bType=BrowserType.InternetExplorer;
			} else if (browserType.equalsIgnoreCase("chrome")) {
				bType=BrowserType.Chrome;
			} else if (browserType.equalsIgnoreCase("safari")) {
				bType=BrowserType.Safari;
			} else if (browserType.equalsIgnoreCase("htmlunit")) {
				bType=BrowserType.HtmlUnit;
			} else if (browserType.equalsIgnoreCase("phantomjs")) {
				bType=BrowserType.PhantomJS;
			} else if (browserType.equalsIgnoreCase("android")) {
				bType=BrowserType.Android;
			} else if (browserType.equalsIgnoreCase("iphone")) {
				bType=BrowserType.IPhone;
			} else if (browserType.equalsIgnoreCase("ipad")) {
				bType=BrowserType.IPad;
			} else if (browserType.equalsIgnoreCase("opera")) {
				bType=BrowserType.Opera;
			} else {
				bType=BrowserType.Firefox;
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		return bType;
	}

	private String browserType;

	BrowserType(final String type) {
		this.browserType = type;
	}

	public String getBrowserType() {
		return this.browserType;
	}

}
