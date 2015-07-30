package com.sprinklr.harvester.global;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.Assert;

import com.sprinklr.harvester.model.ReviewData;

public class CompareFunctions {

	public static void compareData(HashMap<String, HashMap<String, ArrayList<ReviewData>>> aData,
	        HashMap<String, HashMap<String, ArrayList<ReviewData>>> eData) {
		System.out.println("Actual Data Key sets = " + aData.size() + " - " + aData.keySet().toString());
		System.out.println("Expected Data Key sets = " + eData.size() + " - " + eData.keySet().toString());

		for (String expectedStubid : eData.keySet()) {
			Assert.assertTrue(aData.containsKey(expectedStubid), "Stub ID does not match: " + expectedStubid);

			HashMap<String, ArrayList<ReviewData>> actualData = aData.get(expectedStubid);
			HashMap<String, ArrayList<ReviewData>> expectedData = eData.get(expectedStubid);

			for (String expectedAuthor : expectedData.keySet()) {
				System.out.println("Expected Author ID = " + expectedAuthor);
				Assert.assertTrue(actualData.containsKey(expectedAuthor),
				        "Author ID does not found in actual data list: " + expectedAuthor);

				ArrayList<ReviewData> actualReviewList = actualData.get(expectedAuthor);
				ArrayList<ReviewData> expectedReviewList = expectedData.get(expectedAuthor);

				for (ReviewData expectedReviewData : expectedReviewList) {
					boolean flag = false;
					ReviewData reviewData = null;
					for (ReviewData actualReviewData : actualReviewList) {
						reviewData = actualReviewData;
						System.out.println("Expected Review Data AuthorID = " + expectedReviewData.getAuthorId());
						System.out.println("Actual Review Data Author ID = " + actualReviewData.getAuthorId());

						System.out.println("Expected Review Data Mention Date = "
						        + expectedReviewData.getMentionedDate());
						System.out.println("Actual Review Data Mention Date = " + actualReviewData.getMentionedDate());

						System.out.println("Expected Review Data Rating = " + expectedReviewData.getRatings());
						System.out.println("Actual Review Data Rating = " + actualReviewData.getRatings());

						System.out.println("Expected Review Data Comments = " + expectedReviewData.getComment());
						System.out.println("Actual Review Data Comments = " + actualReviewData.getComment());

						if (expectedReviewData.getAuthorId().equalsIgnoreCase(actualReviewData.getAuthorId())
						        && (expectedReviewData.getComment().contains(actualReviewData.getComment()) || actualReviewData
						                .getComment().contains(expectedReviewData.getComment()))) {
							System.out
							        .println("===============================================================================");
							flag = true;
						}
					}
					Assert.assertTrue(flag,
					        "Review not found: " + reviewData.getAuthorId() + " --> " + reviewData.getComment());
				}
			}
		}
	}
}
