/**
 * 
 */
package com.elastica.beatle.audit;

import java.io.IOException;
import java.util.List;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.exec.ShutdownHookProcessDestroyer;
import org.apache.commons.lang.StringUtils;

import com.elastica.beatle.logger.Logger;

/**
 * @author anuvrath
 *
 */
public class ProcessHandler {

	public int executeCommand(final String ip, final int port, final String firewallLogPath,final long printJobTimeout, final boolean printInBackground) throws IOException, InterruptedException {
		
		int exitValue = -1;
		CommandLine commandLine = new CommandLine("python");
		commandLine.addArgument(AuditTestUtils.getPythonScriptPath().trim());
		commandLine.addArgument("tcp");
		commandLine.addArgument(ip.trim());
		commandLine.addArgument(String.valueOf(port).trim());
		commandLine.addArgument(firewallLogPath.trim());
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Executing the command: " + commandLine.toString());
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

		Executor executor = new DefaultExecutor();
		executor.setProcessDestroyer(new ShutdownHookProcessDestroyer());
		ExecuteWatchdog watchdog = new ExecuteWatchdog(printJobTimeout);
		executor.setWatchdog(watchdog);
		ProcessOutputStream outAndErr = new ProcessOutputStream();
		//DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		
		try {
			PumpStreamHandler streamHandler = new PumpStreamHandler(outAndErr);
			executor.setStreamHandler(streamHandler);
			exitValue = executor.execute(commandLine);
			/*executor.execute(commandLine,resultHandler);
			resultHandler.waitFor();
			exitValue = resultHandler.getExitValue();
		    if(resultHandler.getException() != null){
		    	throw resultHandler.getException();
		    }*/		    
			Logger.info("Execution completed successfully. Process output was : " + outAndErr.getOutput());
		}catch (ExecuteException e) {
			Logger.info("Something went wrong while executing the python script: "+e.getMessage());
			exitValue = e.getExitValue();
		}
		catch (IOException e) {
			if (watchdog.killedProcess()) {
				Logger.info("Timed out while executing commandLine : '" + commandLine + "'");
			}			
		}		
		return exitValue;
	}
}

class ProcessOutputStream extends LogOutputStream {

	private List<String> output = new java.util.LinkedList<String>();

	String getOutput() {
		return StringUtils.join(output, "\n");
	}

	@Override
	protected void processLine(final String line, final int level) {
		output.add(line);
		Logger.info(line);
	}
}