package com.elastica.beatle.logreplay;



public class LogReplayTrigger {

	/*static String userName="usman";
	static String logFolderName="box";
	static String userNameFile="perf1_users.txt";
	*/
	
	public  static void triggerLogReplay(String userName, String logFolderName, String userNameFile) throws InterruptedException {
		// TODO Auto-generated method stub
		
		LogReplayServer  myThread1 = new LogReplayServer(userName, logFolderName);
		LogReplayClient  myThread2 = new LogReplayClient(userName,logFolderName, userNameFile);
		myThread1.start();
		myThread2.start();
		Thread.sleep(20000);
		myThread1.interrupt();
		//myThread2.interrupt();
	
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		 String userName="usman";
		 String logFolderName="box";
		 String userNameFile="perf1_users.txt";
		
		
		
		LogReplayServer  myThread1 = new LogReplayServer(userName, logFolderName);
		LogReplayClient  myThread2 = new LogReplayClient(userName,logFolderName, userNameFile);
		myThread1.start();
		myThread2.start();
		Thread.sleep(20000);
		myThread1.interrupt();
		//myThread2.interrupt();
	
	}
	
}
