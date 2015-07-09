package com.sprinklr.harvester.bulkRunner;

import java.util.*;

import com.sprinklr.harvester.util.CsvParser;
import com.sprinklr.harvester.util.JdbcConnect;
import com.sprinklr.harvester.util.PropertyHandler;

public class BulkRunnerScript {

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
				+ cmdStubIdOption + stubIdString + cmdAmqHostOption
				+ PropertyHandler.getProperties().getProperty("mqhost");
		System.out.println(mainCommand);
		return mainCommand;
	}

	public static void callBulkRunner() {

		Map<Integer, String> stubUrlId = CsvParser.parseCsvWithIdUrl();
		Set<Integer> keySet = stubUrlId.keySet();
		Iterator<Integer> ksItr = keySet.iterator();

		if (PropertyHandler.getProperties().getProperty("harvestAll").trim()
				.equals("false")) {
			while (ksItr.hasNext()) {
				CommandExecutor.exec(commandMaker(ksItr.next()));
			}
		} else {
			CommandExecutor.exec(commandMaker(0));
		}
	}
}