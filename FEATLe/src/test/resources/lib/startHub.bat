start java -jar selenium-server-standalone-2.53.0.jar -role hub -port 4444
start java -jar selenium-server-standalone-2.53.0.jar -port 5555 -role node -hub http://localhost:4444/grid/register -browser browserName=chrome,maxInstances=5 platform=WINDOWS, ensureCleanSession=true, javascriptEnabled=true, acceptSslCerts=true, ignoreProtectedModeSettings=true, ignoreZoomSetting=true, takesScreenshot=true -Dwebdriver.chrome.driver="chromedriver.exe"
start java -jar selenium-server-standalone-2.53.0.jar -port 5556 -role node -hub http://localhost:4444/grid/register -browser browserName=chrome,maxInstances=5 platform=WINDOWS, ensureCleanSession=true, javascriptEnabled=true, acceptSslCerts=true, ignoreProtectedModeSettings=true, ignoreZoomSetting=true, takesScreenshot=true -Dwebdriver.chrome.driver="chromedriver.exe"
start java -jar selenium-server-standalone-2.53.0.jar -port 5557 -role node -hub http://localhost:4444/grid/register -browser browserName=chrome,maxInstances=5 platform=WINDOWS, ensureCleanSession=true, javascriptEnabled=true, acceptSslCerts=true, ignoreProtectedModeSettings=true, ignoreZoomSetting=true, takesScreenshot=true -Dwebdriver.chrome.driver="chromedriver.exe"