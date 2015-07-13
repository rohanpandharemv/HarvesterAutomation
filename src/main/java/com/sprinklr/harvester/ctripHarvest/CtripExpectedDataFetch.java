package com.sprinklr.harvester.ctripHarvest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.sprinklr.harvester.model.InitialData;
import com.sprinklr.harvester.model.ReviewData;
import com.sprinklr.harvester.util.PropertyHandler;

public class CtripExpectedDataFetch {

	public static HashMap<String, HashMap<String, ArrayList<ReviewData>>> getActualData(
			HashMap<Integer, InitialData> testData) {

		HashMap<String, HashMap<String, ArrayList<ReviewData>>> expectedReviewDataPerStub = new HashMap<String, HashMap<String, ArrayList<ReviewData>>>();

		// WebDriver driver = new HtmlUnitDriver();
		WebDriver driver = new FirefoxDriver();

		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		Set<Integer> endPointKeySet = testData.keySet();
		Iterator<Integer> endPointIterator = endPointKeySet.iterator();
		while (endPointIterator.hasNext()) {
			HashMap<String, ArrayList<ReviewData>> reviewContent = new HashMap<String, ArrayList<ReviewData>>();

			Integer stubID = endPointIterator.next();
			String endPointURL = testData.get(stubID).getStubEndpoint();

			driver.get(endPointURL);

			List<WebElement> comments = driver.findElements(By
					.xpath(PropertyHandler.getCTripProperties().getProperty(
							"content_xpath")));
			List<WebElement> mentionDates = driver.findElements(By
					.xpath(PropertyHandler.getCTripProperties().getProperty(
							"mentionTime_xpath")));
			List<WebElement> authorIDs = driver.findElements(By
					.xpath(PropertyHandler.getCTripProperties().getProperty(
							"author_xpath")));
			List<WebElement> ratings = driver.findElements(By
					.xpath(PropertyHandler.getCTripProperties().getProperty(
							"rating_xpath")));

			for (int i = 1; i < comments.size(); i++) {
				ReviewData rdObject = new ReviewData();

				rdObject.setHarvesterID(stubID.toString());
				rdObject.setAuthorId(authorIDs.get(i).getText());
				rdObject.setComment(comments.get(i).getText());
				rdObject.setMentionedDate(mentionDates.get(i).getText());
				rdObject.setRatings(ratings.get(i).getText());

				Set<String> reviewContentKeyset = reviewContent.keySet();
				if (reviewContentKeyset.contains(rdObject.getAuthorId())) {
					ArrayList<ReviewData> reviewData = new ArrayList<ReviewData>();
					reviewData = reviewContent.get(rdObject.getAuthorId());
					reviewData.add(rdObject);
					reviewContent.put(rdObject.getAuthorId(), reviewData);
				} else {
					ArrayList<ReviewData> reviewData = new ArrayList<ReviewData>();
					reviewData.add(rdObject);
					reviewContent.put(rdObject.getAuthorId(), reviewData);
				}
			}
			expectedReviewDataPerStub.put(stubID.toString(), reviewContent);
		}
		return expectedReviewDataPerStub;
	}
}