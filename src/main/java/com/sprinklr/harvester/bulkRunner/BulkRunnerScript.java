package com.sprinklr.harvester.bulkRunner;

import java.util.*;

import org.apache.log4j.Logger;

import com.sprinklr.harvester.model.InitialData;
import com.sprinklr.harvester.test.testCtrip;
import com.sprinklr.harvester.util.CsvParser;
import com.sprinklr.harvester.util.JdbcConnect;
import com.sprinklr.harvester.util.PropertyHandler;

public class BulkRunnerScript {
	
	public static Logger bulkRunnerSctiptLogger = testCtrip.logger;
			
	public static String commandMaker(Integer sid) {

		String cmdHeader = "java ";
		String cmdClasspathOption = " -cp ";
		String cmdJars = " \"lib/harvester-1.0.0.jar;lib/*\" ";
		String cmdScript = " com.newbrandanalytics.script.BulkRunner ";
		String cmdHostOption = " -h ";
		String cmdUsernameOption = " -u ";
		String cmdPasswordOption = " -p ";
		String cmdSourceIdOption = " -s ";
		String cmdClientName = " -c ";
		String cmdTimeOption = " -t ";
		String cmdStubIdOption = " -z ";
		String cmdAmqHostOption = " --amq-host ";
		String stubIdString = null;
		Integer sourceId = JdbcConnect.getSourceId();

		if (sid == 0) {
			stubIdString = "";
		} else {
			stubIdString = Integer.toString(sid);
		}
		String mainCommand = cmdHeader + cmdClasspathOption + cmdJars
				+ cmdScript + cmdHostOption
				+ PropertyHandler.getProperties().getProperty("host")
				+ cmdUsernameOption
				+ PropertyHandler.getProperties().getProperty("user")
				+ cmdPasswordOption
				+ PropertyHandler.getProperties().getProperty("pass")
				+ cmdSourceIdOption + Integer.toString(sourceId)
				+ cmdClientName
				+ PropertyHandler.getProperties().getProperty("client")
				+ cmdTimeOption
				+ PropertyHandler.getProperties().getProperty("date")
				+ cmdAmqHostOption
				+ PropertyHandler.getProperties().getProperty("mqhost");

		if (sid != 0) {
			mainCommand = mainCommand + cmdStubIdOption + stubIdString;
		}
		bulkRunnerSctiptLogger.info("BulkRunnerSctipt.commandMaker() returning command : " + mainCommand);
		//System.out.println(mainCommand);
		return mainCommand;
	}

	public static void callBulkRunner(HashMap<Integer, InitialData> testData) {
		
		Set<Integer> testDataKeys = testData.keySet();
		Iterator<Integer> testDataKey = testDataKeys.iterator();
		Integer stubId;
		while (testDataKey.hasNext()) {
			stubId = testDataKey.next();
			bulkRunnerSctiptLogger.info("BulkRunnerSctipt.callBulkRunner() running command with stubId : " + stubId);
			CommandExecutor.exec(commandMaker(stubId));
		}

		/**
		 * Commenting below code as we do not want to push all the stubs in
		 * queue. For all we need to have a End-point for all the stubs in csv/db.
		 */

		/*
		 * if (PropertyHandler.getProperties().getProperty("harvestAll").trim()
		 * .equals("false")) { while (ksItr.hasNext()) {
		 * CommandExecutor.exec(commandMaker(ksItr.next())); } } else {
		 * CommandExecutor.exec(commandMaker(0)); }
		 */
	}
}