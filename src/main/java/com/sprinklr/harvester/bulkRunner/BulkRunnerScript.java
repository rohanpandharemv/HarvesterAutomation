package com.sprinklr.harvester.bulkRunner;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.sprinklr.harvester.model.InitialData;
import com.sprinklr.harvester.util.JdbcConnect;
import com.sprinklr.harvester.util.PropertyHandler;

/**
 * Class of calling the Bulk Runner java file with all the necessary parameters.
 *
 */
public class BulkRunnerScript {

	public final static Logger LOGGER = Logger.getLogger(BulkRunnerScript.class);

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
		String mainCommand = cmdHeader + cmdClasspathOption + cmdJars + cmdScript + cmdHostOption
		        + PropertyHandler.getProperties().getProperty("host") + cmdUsernameOption
		        + PropertyHandler.getProperties().getProperty("user") + cmdPasswordOption
		        + PropertyHandler.getProperties().getProperty("pass") + cmdSourceIdOption + Integer.toString(sourceId)
		        + cmdClientName + PropertyHandler.getProperties().getProperty("client") + cmdTimeOption
		        + PropertyHandler.getProperties().getProperty("date") + cmdAmqHostOption
		        + PropertyHandler.getProperties().getProperty("mqhost");

		if (sid != 0) {
			mainCommand = mainCommand + cmdStubIdOption + stubIdString;
		}
		LOGGER.info("BulkRunnerSctipt.commandMaker() returning command : " + mainCommand);
		return mainCommand;
	}

	public static void callBulkRunner(HashMap<Integer, InitialData> testData) {
		Set<Integer> testDataKeys = testData.keySet();
		Iterator<Integer> testDataKey = testDataKeys.iterator();
		Integer stubId;
		while (testDataKey.hasNext()) {
			stubId = testDataKey.next();
			LOGGER.info("BulkRunnerSctipt.callBulkRunner() running command with stubId : " + stubId);
			CommandExecutor.exec(commandMaker(stubId));
		}
	}
}