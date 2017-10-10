
Webdriver Automation Framework
 
	wdframework is automation framework for webpages using Webdriver,Maven,testNG and developed on JAVA 

	Download the project and build the project using "mvn clean install"

	Default settings(browser,mode,baseurl) are taken from config.properties.
	Settings are taken in highest precedence from pom file,testng file and then config.properties.

	For grid mode, hub and nodes must be instantiated first using startHub.bat.

	Browsers supported: Firefox,Chrome,Internet Explorer,Opera,Safari,PhantomJS,HtmlUnit

	Modes supported:Local,Grid,Cloud(sauce labs support)

	Listeners: Retry Listener,Priority Listener and etc..
	
