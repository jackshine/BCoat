function FindProxyForURL(url, host) {  
/*********************** URL MATCHING ***********************/
/************************************************************/

/*********************** HOST MATCHING ************************/
 if (shExpMatch(host, "*.dropbox.com") || host=="dropbox.com") { return "PROXY gw.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.dropboxusercontent.com") || host=="dropboxusercontent.com") { return "PROXY gw.elastica.net:443; DIRECT"; }
/*************************************************************/
 return "DIRECT";
}