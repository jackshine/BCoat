function FindProxyForURL(url, host) {  
/*********************** URL MATCHING ***********************/
/************************************************************/

/*********************** HOST MATCHING ************************/
 if (shExpMatch(host, "*.clients3.google.com") || host=="clients3.google.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.googledrive.com") || host=="googledrive.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.windowsazure.com") || host=="windowsazure.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.sharepoint.com") || host=="sharepoint.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.live.com") || host=="live.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.box.com") || host=="box.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.box.net") || host=="box.net") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.afx.ms") || host=="afx.ms") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.docs.google.com") || host=="docs.google.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.login.microsoftonline.com") || host=="login.microsoftonline.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.accounts.google.com") || host=="accounts.google.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.livefilestore.com") || host=="livefilestore.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.clients5.google.com") || host=="clients5.google.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.microsoftonline.com") || host=="microsoftonline.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.users.storage.live.com") || host=="users.storage.live.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.files.1drv.com") || host=="files.1drv.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.googleapis.com") || host=="googleapis.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.content.force.com") || host=="content.force.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.outlook.com") || host=="outlook.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.googleusercontent.com") || host=="googleusercontent.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.salesforce.com") || host=="salesforce.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.skyapi.live.net") || host=="skyapi.live.net") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.outlook.office365.com") || host=="outlook.office365.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.office.com") || host=="office.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.login.live.com") || host=="login.live.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.onedrive.live.com") || host=="onedrive.live.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.boxcdn.net") || host=="boxcdn.net") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.portal.office.com") || host=="portal.office.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.clients6.google.com") || host=="clients6.google.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.boxcloud.com") || host=="boxcloud.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.office365.com") || host=="office365.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
 if (shExpMatch(host, "*.drive.google.com") || host=="drive.google.com") { return "PROXY gw-dub.elastica.net:443; DIRECT"; }
/*************************************************************/
 return "DIRECT";
}