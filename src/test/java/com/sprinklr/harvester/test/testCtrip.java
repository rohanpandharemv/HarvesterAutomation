package com.sprinklr.harvester.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.testng.annotations.Test;

import com.sprinklr.harvester.adminui.AddStubAdminUI;
import com.sprinklr.harvester.bulkRunner.BulkRunnerScript;
import com.sprinklr.harvester.ctripHarvest.CtripExpectedDataFetch;
import com.sprinklr.harvester.model.InitialData;
import com.sprinklr.harvester.model.ReviewData;
import com.sprinklr.harvester.mq.RabbitMQPullMessage;
import com.sprinklr.harvester.util.JdbcConnect;

public class testCtrip {

	@Test
	public void test001() {
		AddStubAdminUI addStub = new AddStubAdminUI();
		addStub.addStubInAdminUI();

		// Call create csv to fetch the stub ids from db.
		HashMap<Integer, InitialData> testData = JdbcConnect
				.getHashMappedDBData();

		// Call Bulk Runner to queue the stubs in RabbitMQ.
		BulkRunnerScript.callBulkRunner(testData);

		// Start pulling the messages from Queue.
		HashMap<String, HashMap<String, ArrayList<ReviewData>>> actualData = RabbitMQPullMessage
				.pull();

		//
		HashMap<String, HashMap<String, ArrayList<ReviewData>>> expectedData = CtripExpectedDataFetch
				.getActualData(testData);

		// Comparing the actual and expected data
		Set<Integer> keySet = testData.keySet();
		Iterator<Integer> testDataIterator = keySet.iterator();
		while (testDataIterator.hasNext()) {
			String stubId = Integer.toString(testDataIterator.next());
			HashMap<String, ArrayList<ReviewData>> actualDataForStub = actualData
					.get(stubId);
			HashMap<String, ArrayList<ReviewData>> expectedDataForStub = expectedData
					.get(stubId);

			int actualHashMapSize = actualData.size();
			int expectedHashMapSize = expectedData.size();
			System.out.println("Actual hash map size = " + actualHashMapSize);
			System.out.println("Expected hash map size = "
					+ expectedHashMapSize);

			int size = 0;
			if (actualHashMapSize < expectedHashMapSize) {
				size = actualHashMapSize;
			} else {
				size = expectedHashMapSize;
			}

			for (int i = 0; i < size; i++) {
				
			}

		}

	}
}
