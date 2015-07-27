package com.sprinklr.harvester.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.testng.annotations.Test;

import com.itextpdf.text.log.SysoLogger;
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
		//AddStubAdminUI addStub = new AddStubAdminUI();
		//addStub.addStubInAdminUI();
		System.out.println("JdbcConnects getHashMappedDBData().............");
		// Call create csv to fetch the stub ids from db.
		HashMap<Integer, InitialData> testData = JdbcConnect.getHashMappedDBData();

		// Call Bulk Runner to queue the stubs in RabbitMQ.
		BulkRunnerScript.callBulkRunner(testData);

		// Start pulling the messages from Queue.
		HashMap<String, HashMap<String, ArrayList<ReviewData>>> actualData = RabbitMQPullMessage.pull();
		System.out.println("Done with getting review from Queue.................");
		// SiteData
		HashMap<String, HashMap<String, ArrayList<ReviewData>>> expectedData = CtripExpectedDataFetch.getActualData(testData);
		System.out.println("Done with getting reviews from actual website.........");
		// Comparing the actual and expected data
		if (actualData.size() > expectedData.size()) {
			compareData(actualData, expectedData);
		}else{
			compareData(expectedData, actualData);
		}
	}
	
	public static void compareData(HashMap<String, HashMap<String,ArrayList<ReviewData>>> aData, HashMap<String, HashMap<String,ArrayList<ReviewData>>> eData)
	{
		System.out.println("Inside testCtrip.compareData().......");
		
		Set<String> aDataKeyset = aData.keySet();
		Set<String> eDataKeyset = eData.keySet();
		
		ArrayList<String> eDataKeysetStubId = new ArrayList<String>();
		Iterator<String> eDataKeysetItr = eDataKeyset.iterator();
		
		while (eDataKeysetItr.hasNext()) {
			eDataKeysetStubId.add(eDataKeysetItr.next());
		}
		
		Iterator<String> stubIdIterator = eDataKeysetStubId.iterator();
		while(stubIdIterator.hasNext()){			
			String eDataStubId = stubIdIterator.next();	
			if(aDataKeyset.contains(eDataStubId)){
				HashMap<String, ArrayList<ReviewData>>  aaData = aData.get(eDataStubId);
				HashMap<String, ArrayList<ReviewData>>  eeData = eData.get(eDataStubId);
								
				Set<String> aaDataSet = aaData.keySet();
				Set<String> eeDataSet = eeData.keySet();
				
				Collection<ArrayList<ReviewData>> aaDataList = aaData.values();
				Collection<ArrayList<ReviewData>> eeDataList = eeData.values();
				
				Iterator<ArrayList<ReviewData>> aaDataListItr = aaDataList.iterator();
				Iterator<ArrayList<ReviewData>> eeDataListItr = eeDataList.iterator();
				
				while(aaDataListItr.hasNext()){
					ArrayList<ReviewData> aaList = aaDataListItr.next();
					Iterator<ArrayList<ReviewData>> aaListItr = aaDataList.iterator();
					while(aaListItr.hasNext()){
						ArrayList<ReviewData> rdObjList = aaListItr.next();
						Iterator<ReviewData> rdObjListItr = rdObjList.iterator();
						while(rdObjListItr.hasNext()){
							ReviewData rd = rdObjListItr.next();
							System.out.println("**************************************************************************");
							
							while(eeDataListItr.hasNext()){
								ArrayList<ReviewData> eeList = eeDataListItr.next();
								Iterator<ReviewData> eeListItr = eeList.iterator();
								while(eeListItr.hasNext()){
									ReviewData rd1 = eeListItr.next();
									if(rd.equals(rd1)){
										System.out.println(rd.toString() +"<====Comparing to====>"+rd1.toString());
									}
								}
							}
							System.out.println("******************************");
						}
					}					
				}
				
			}
		}
	}	
}

