package com.sprinklr.harvester.test;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.sprinklr.harvester.bulkRunner.BulkRunnerScript;
import com.sprinklr.harvester.ctripharvest.CtripExpectedDataFetch;
import com.sprinklr.harvester.global.CompareFunctions;
import com.sprinklr.harvester.model.InitialData;
import com.sprinklr.harvester.model.ReviewData;
import com.sprinklr.harvester.mq.RabbitMQPullMessage;
import com.sprinklr.harvester.util.JdbcConnect;

public class testCtrip {

	public final static Logger logger = Logger.getLogger(testCtrip.class);

	@Test
	public void test001() {
		logger.info("**1** testCtrip.test001() Initialising the testing process...");
		// AddStubAdminUI addStub = new AddStubAdminUI();
		// addStub.addStubInAdminUI();
		logger.info("**2** testCtrip.test001() completed adding stub/url from adminUI");

		// Call create csv to fetch the stub ids from db.
		HashMap<Integer, InitialData> testData = JdbcConnect.getHashMappedDBData();
		logger.info("**3** testCtrip.test001() completed fetching data from database & convert into HashMap<Integer, InitialData> testData ");

		// Call Bulk Runner to queue the stubs in RabbitMQ.
		BulkRunnerScript.callBulkRunner(testData);
		logger.info("**4** testCtrip.test001() completed pushing all the stubUrls to queue from testData");

		// Start pulling the messages from Queue.
		HashMap<String, HashMap<String, ArrayList<ReviewData>>> actualData = RabbitMQPullMessage.pull();
		logger.info("**5** testCtrip.test001() completed pulling messages from MQ for provided stubs");

		// SiteData
		HashMap<String, HashMap<String, ArrayList<ReviewData>>> expectedData = CtripExpectedDataFetch
		        .getActualData(testData);
		logger.info("**6** testCtrip.test001() completed fetching data from actual webpage");

		logger.info("**7** testCtrip.test001() starting comparison of database data with actual site data");
		// Comparing the actual and expected data
		CompareFunctions.compareData(actualData, expectedData);
	}

	

}
