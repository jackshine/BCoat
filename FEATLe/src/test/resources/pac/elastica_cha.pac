function FindProxyForURL(url, host) {  
/*********************** URL MATCHING ***********************/
/************************************************************/

/*********************** HOST MATCHING ************************/
 if (shExpMatch(host, "*.clients3.google.com") || host=="clients3.google.com") { return "PROXY 52.36.20.62:443"; }
 if (shExpMatch(host, "*.googledrive.com") || host=="googledrive.com") { return "PROXY 52.36.20.62:443"; }
 if (shExpMatch(host, "*.evernote.com") || host=="evernote.com") { return "PROXY 52.36.20.62:443"; }
 if (shExpMatch(host, "*.sharepoint.com") || host=="sharepoint.com") { return "PROXY 52.36.20.62:443"; }
 if (shExpMatch(host, "*.live.com") || host=="live.com") { return "PROXY 52.36.20.62:443"; }
 if (shExpMatch(host, "*.box.com") || host=="box.com") { return "PROXY 52.36.20.62:443"; }
 if (shExpMatch(host, "*.box.net") || host=="box.net") { return "PROXY 52.36.20.62:443"; }
 if (shExpMatch(host, "*.docs.google.com") || host=="docs.google.com") { return "PROXY 52.36.20.62:443"; }
 if (shExpMatch(host, "*.login.microsoftonline.com") || host=="login.microsoftonline.com") { return "PROXY 52.36.20.62:443"; }
 if (shExpMatch(host, "*.accounts.google.com") || host=="accounts.google.com") { return "PROXY 52.36.20.62:443"; }
 if (shExpMatch(host, "*.clients5.google.com") || host=="clients5.google.com") { return "PROXY 52.36.20.62:443"; }
 if (shExpMatch(host, "*.microsoftonline.com") || host=="microsoftonline.com") { return "PROXY 52.36.20.62:443"; }
 if (shExpMatch(host, "*.googleapis.com") || host=="googleapis.com") { return "PROXY 52.36.20.62:443"; }
 if (shExpMatch(host, "*.outlook.com") || host=="outlook.com") { return "PROXY 52.36.20.62:443"; }
 if (shExpMatch(host, "*.googleusercontent.com") || host=="googleusercontent.com") { return "PROXY 52.36.20.62:443"; }
 if (shExpMatch(host, "*.outlook.office365.com") || host=="outlook.office365.com") { return "PROXY 52.36.20.62:443"; }
 if (shExpMatch(host, "*.office.com") || host=="office.com") { return "PROXY 52.36.20.62:443"; }
 if (shExpMatch(host, "*.boxcdn.net") || host=="boxcdn.net") { return "PROXY 52.36.20.62:443"; }
 if (shExpMatch(host, "*.portal.office.com") || host=="portal.office.com") { return "PROXY 52.36.20.62:443"; }
 if (shExpMatch(host, "*.clients6.google.com") || host=="clients6.google.com") { return "PROXY 52.36.20.62:443"; }
 if (shExpMatch(host, "*.boxcloud.com") || host=="boxcloud.com") { return "PROXY 52.36.20.62:443"; }
 if (shExpMatch(host, "*.office365.com") || host=="office365.com") { return "PROXY 52.36.20.62:443"; }
 if (shExpMatch(host, "*.drive.google.com") || host=="drive.google.com") { return "PROXY 52.36.20.62:443"; }
/*************************************************************/
 return "DIRECT";
}