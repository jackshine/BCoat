package com.elastica.gw.utils;

import org.testng.annotations.Test;

import com.elastica.action.backend.AWSBEActions;
import com.elastica.common.GWCommonTest;
import com.elastica.logger.Logger;



public class AWSMachines{
	
	@Test(groups ={"START"})
	public void startAWSInstance() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Starting the AWS machines");
		Logger.info("==================================================================================");
		AWSBEActions aws = new AWSBEActions();
		Logger.info("==================================================================================");
		Logger.info(" Started the AWS machines");
		Logger.info("==================================================================================");
		aws.startAWSInstance();
	}
	
	@Test(groups ={"STOP"})
	public void stopAWSInstance() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Stoping the AWS machines");
		Logger.info("==================================================================================");
		AWSBEActions aws = new AWSBEActions();
		Logger.info("==================================================================================");
		Logger.info(" Stoped the AWS machines");
		Logger.info("==================================================================================");
		aws.stopAWSInstance();
	}
	
}