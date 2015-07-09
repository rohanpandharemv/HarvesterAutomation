package com.sprinklr.harvester.test;

import org.testng.annotations.Test;

import com.sprinklr.harvester.adminui.AddStubAdminUI;
import com.sprinklr.harvester.bulkRunner.BulkRunnerScript;
import com.sprinklr.harvester.mq.RabbitMQPullMessage;
import com.sprinklr.harvester.util.JdbcConnect;

public class testCtrip {

	@Test
	public void test001() {
		AddStubAdminUI addStub = new AddStubAdminUI();
		addStub.addStubInAdminUI();
		
		// Call create csv to fetch the stub ids from db.
		JdbcConnect.createCsvDataFile();
		
		// Call Bulk Runner to queue.
		BulkRunnerScript.callBulkRunner();
		
		// Start pulling the messages from Queue.
		RabbitMQPullMessage.pull();
	}
}
