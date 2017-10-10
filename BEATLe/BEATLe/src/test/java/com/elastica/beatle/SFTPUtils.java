package com.elastica.beatle;


import java.io.FileOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import org.testng.Reporter;


public class SFTPUtils {
	
	// For FTP server
	 
    private String hostName;
    private String hostPort;
    private String userName;
    private String passWord;
    private String destinationDir;
 
    // For sFTP server
    private ChannelSftp channelSftp = null;
    private Session session = null;
    private Channel channel = null;
 
    private int userGroupId = 0;
 
    public SFTPUtils() {
 
    }
 
    public String getHostName() {
        return hostName;
    }
 
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
 
    public String getHostPort() {
        return hostPort;
    }
 
    public void setHostPort(String hostPort) {
        this.hostPort = hostPort;
    }
 
    public String getUserName() {
        return userName;
    }
 
    public void setUserName(String userName) {
        this.userName = userName;
    }
 
    public String getPassWord() {
        return passWord;
    }
 
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
 
    public String getDestinationDir() {
        return destinationDir;
    }
 
    public void setDestinationDir(String destinationDir) {
        this.destinationDir = destinationDir;
    }
 
    public int getUserGroupId() {
        return userGroupId;
    }
 
    public void setUserGroupId(int userGroupId) {
        this.userGroupId = userGroupId;
    }
    
    public void initChannelSftp() {
        channelSftp = null;
        session = null;
        try {
 
            JSch jsch = new JSch();
            //
            session = jsch.getSession(userName, hostName,Integer.valueOf(hostPort));
            Reporter.log("session created.",true);
            session.setPassword(passWord);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
 
        } catch (Exception ex) {
            Reporter.log("exception at initChannelSftp::"+ex,true);
        }
        // }
    }
    public void initChannelSftp(String nfsCertificateFilePath) {
        channelSftp = null;
        session = null;
        try {
 
            JSch jsch = new JSch();
            jsch.addIdentity(FileHandlingUtils.getFileAbsolutePath(nfsCertificateFilePath));
            
            session = jsch.getSession(userName, hostName,Integer.valueOf(hostPort));
            Reporter.log("session created.",true);
            //session.setPassword(passWord);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
 
        } catch (Exception ex) {
            Reporter.log("exception at initChannelSftp::"+ex,true);
        }
        // }
    }
    
    /*
     * Upload file to ftp server that has configuration on sysConfig.properties
     * filename: name of file that will be stored on ftp fis: input stream of
     * file that will be stored on ftp enableLog: enable log return value: URN
     */
    public String uploadFileToFTP(String filename, InputStream fis,boolean enableLog) throws Exception{
		String result = "";

		initChannelSftp();
		String strChannel = "sftp";
		try {
			// Reporter.log("session connect begin");
			if (!session.isConnected())
				Thread.sleep(60000); //wait 1 min here to get the connection to aws instance otherwise u will get auth fail issues.
				session.connect();
			Reporter.log("session connect end",true);

			ChannelSftp channel = null;
			channel = (ChannelSftp) session.openChannel(strChannel);
			Reporter.log("channel connect begin",true);
			channel.connect();
			Reporter.log("channel connect end",true);
			channelSftp = (ChannelSftp) channel;
			Reporter.log("current sftp directory" + channelSftp.pwd(),true);

			Thread.sleep(180000); // wait 3 minutes here for sftp upload
			channelSftp.cd(destinationDir);

			channelSftp.put(fis, filename);
			Thread.sleep(2000);
			result = String.format("sftp://%s/%s/%s", hostName, destinationDir,
					filename);

			fis.close();

			SftpATTRS fileAttributes = null;
			fileAttributes = channel.lstat(destinationDir + "/" + filename);

			Reporter.log("fileAttributes..." + fileAttributes.getSize(),true);

			if (fileAttributes != null && fileAttributes.getSize() != 0) {
				result = result + "Upload Sucess"; // returns to FM

			} 
			else if (fileAttributes.getSize() == 0)
			{
				result = result + "SCP COMPLETED"; // returns to FM
			}
			else {

				result = result + "Upload fail"; // returns to FM

			}

		} catch (JSchException e) {
			Reporter.log(e.getMessage(),true);
			e.printStackTrace();
		} catch (SftpException e) {
			Reporter.log(e.getMessage(),true);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {

			if (channelSftp != null && channelSftp.isConnected())
				channelSftp.exit();
			if (channelSftp != null && channelSftp.isConnected())
				channelSftp.disconnect();
			if (session != null && session.isConnected())
				session.disconnect();
		}

		Reporter.log("Channel is connected? " + channelSftp.isConnected(),true); // returns
																				// false
																				// as
																				// i
																				// would
																				// expect
		Reporter.log("Channel is closed? " + channelSftp.isClosed(),true); // returns
																			// true
																			// as
																			// i
																			// would
																			// expect

		return result;
	}
    
    public String uploadNFSFile(String filename, InputStream fis,boolean enableLog, String pemCertificate) throws Exception{
		String result = "";

		initChannelSftp(pemCertificate);
		String strChannel = "sftp";
		try {
			// Reporter.log("session connect begin");
			if (!session.isConnected())
				Thread.sleep(60000); //wait 1 min here to get the connection to aws instance otherwise u will get auth fail issues.
				session.connect();
			Reporter.log("session connect end",true);

			ChannelSftp channel = null;
			channel = (ChannelSftp) session.openChannel(strChannel);
			Reporter.log("channel connect begin",true);
			channel.connect();
			Reporter.log("channel connect end",true);
			channelSftp = (ChannelSftp) channel;
			Reporter.log("current sftp directory" + channelSftp.pwd(),true);

			Thread.sleep(120000); // wait 2 minutes here for sftp upload
			channelSftp.cd(destinationDir);

			channelSftp.put(fis, filename);
			Thread.sleep(2000);
			result = String.format("sftp://%s/%s/%s", hostName, destinationDir,
					filename);

			fis.close();
			SftpATTRS fileAttributes = null;
			fileAttributes = channel.lstat(destinationDir + "/" + filename);

			Reporter.log("fileAttributes..." + fileAttributes.getSize(),true);

			if (fileAttributes != null && fileAttributes.getSize() != 0) {
				result = result + "Upload Sucess"; // returns to FM

			} 
			else if (fileAttributes.getSize() == 0)
			{
				result = result + "SCP COMPLETED"; // returns to FM
			}
			else {

				result = result + "Upload fail"; // returns to FM
			}

		} catch (JSchException e) {
			Reporter.log(e.getMessage(),true);
			e.printStackTrace();
		} catch (SftpException e) {
			Reporter.log(e.getMessage(),true);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {

			if (channelSftp != null && channelSftp.isConnected())
				channelSftp.exit();
			if (channelSftp != null && channelSftp.isConnected())
				channelSftp.disconnect();
			if (session != null && session.isConnected())
				session.disconnect();
		}

		Reporter.log("Channel is connected? " + channelSftp.isConnected(),true); // returns
																				// false
																				// as
																				// i
																				// would
																				// expect
		Reporter.log("Channel is closed? " + channelSftp.isClosed(),true); // returns
																			// true
																			// as
																			// i
																			// would
																			// expect

		return result;
	}
    
    public String uploadFileThroughHttps(String cmd, String filename, InputStream fis,boolean enableLog) throws Exception{
    	

    	String command=cmd;
    	Reporter.log("command:- "+command,true);


    	String result = "";

    	initChannelSftp();
    	String strChannel = "exec";
    	try {
    		// Reporter.log("session connect begin");
    		if (!session.isConnected())
    			session.connect();
    		Reporter.log("session connect end",true);

    		Channel channel=session.openChannel("exec");
    		((ChannelExec)channel).setCommand(command);

    		channel.setInputStream(null);
    		
    		Thread.sleep(60000);

    		((ChannelExec)channel).setErrStream(System.err);

    		InputStream in=channel.getInputStream();

    		Reporter.log("channel connect begin",true);
    		channel.connect();

    		byte[] tmp=new byte[1024];
    		while(true){
    			while(in.available()>0){
    				int i=in.read(tmp, 0, 1024);
    				if(i<0)break;
    				System.out.print(new String(tmp, 0, i));
    			}
    			if(channel.isClosed()){
    				if(in.available()>0) continue; 
    				System.out.println("exit-status: "+channel.getExitStatus());
    				break;
    			}
    			try{Thread.sleep(1000);}catch(Exception ee){}
    		}
    		channel.disconnect();
    		session.disconnect();
    	}
    	catch(Exception e){
    		System.out.println(e);
    	}

			
		return "";
	}
    
    public boolean checkFileExistIntheSftpServer(String fileName) {
        boolean existed = false;
 
        initChannelSftp();
        try {
            if (!session.isConnected())
                session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;
            try {
            	Thread.sleep(90000);//wait here to identify the destination

            	channelSftp.cd(destinationDir);
            } catch (SftpException e) {
               /* channelSftp.mkdir(destinationDir);
                channelSftp.cd(destinationDir);*/
            }
 
            Vector ls = channelSftp.ls(destinationDir);
            if (ls != null) {
                // Iterate listing.
            	Reporter.log(fileName,true);
                for (int i = 0; i < ls.size(); i++) {
                    LsEntry entry = (LsEntry) ls.elementAt(i);
                    String file_name = entry.getFilename();
                    if (!entry.getAttrs().isDir()) {
                        if (fileName.toLowerCase().startsWith(file_name)) {
                            existed = true;
                        }
                    }
                }
            }
 
            channelSftp.exit();
            channel.disconnect();
            session.disconnect();
        } catch (Exception ex) {
            existed = false;
            if (session.isConnected()) {
                session.disconnect();
            }
        }
 
        return existed;
    }
    
    public void fetchFile(String username, String host, String passwd) throws JSchException, SftpException, IOException {
    	 initChannelSftp();
         try {
             if (!session.isConnected())
                 session.connect();
             channel = session.openChannel("sftp");
             channel.connect();
             channelSftp = (ChannelSftp) channel;
             try {
             	Thread.sleep(90000);//wait here to identify the destination
                 channelSftp.cd(destinationDir);
             } catch (SftpException e) {
                /* channelSftp.mkdir(destinationDir);
                 channelSftp.cd(destinationDir);*/
             }
             
        InputStream in = channelSftp.get("testScp");
        // set local file
        String lf = "OBJECT_FILE";
        FileOutputStream tergetFile = new FileOutputStream(lf);

        // read containts of remote file to local
        int c;
        while ( (c= in.read()) != -1 ) {
            tergetFile.write(c);
        } 

        in.close();
        tergetFile.close();

        channelSftp.exit();
        channel.disconnect();
        session.disconnect();
    } catch (Exception ex) {
       // existed = false;
        if (session.isConnected()) {
            session.disconnect();
        }
    }  

    }
    
    public void deleteFile(String fileName) {
    	 
        initChannelSftp();
        try {
            if (!session.isConnected())
                session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;
            try {
            	Thread.sleep(90000);
                channelSftp.cd(destinationDir);
            } catch (SftpException e) {
               /* channelSftp.mkdir(destinationDir);
                channelSftp.cd(destinationDir);*/
            }
            channelSftp.rm(fileName);
            channelSftp.exit();
            channel.disconnect();
            session.disconnect();
        } catch (Exception ex) {
            Reporter.log(ex.getMessage(),true);
            if (session.isConnected()) {
                session.disconnect();
            }
        }
 
    }    

}
