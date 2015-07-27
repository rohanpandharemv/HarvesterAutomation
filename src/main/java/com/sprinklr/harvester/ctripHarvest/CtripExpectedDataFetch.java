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
import org.openqa.selenium.support.ui.Select;

import com.itextpdf.text.log.SysoLogger;
import com.sprinklr.harvester.model.InitialData;
import com.sprinklr.harvester.model.ReviewData;
import com.sprinklr.harvester.util.PropertyHandler;

public class CtripExpectedDataFetch {

	public static HashMap<String, HashMap<String, ArrayList<ReviewData>>> getActualData(
			HashMap<Integer, InitialData> testData) {

		System.out
				.println("Inside get actual data method of class CtripExpectedDataFetch............");

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

			Select dropdown = new Select(driver.findElement(By
					.className("select_sort")));

			dropdown.selectByIndex(1);

			try {
				Thread.sleep(5000);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			List<WebElement> comments = driver.findElements(By
					.xpath(PropertyHandler.getCTripProperties().getProperty(
							"content_xpath")));
			ArrayList<String> commentsText = new ArrayList<String>();
			for (int i = 0; i < comments.size(); i++) {
				commentsText.add(comments.get(i).getText());
			}
			System.out.println(comments);

			List<WebElement> mentionDates = driver.findElements(By
					.xpath(PropertyHandler.getCTripProperties().getProperty(
							"mentionTime_xpath")));
			ArrayList<String> mentionDatesText = new ArrayList<String>();
			for (int i = 0; i < mentionDates.size(); i++) {
				mentionDatesText.add(mentionDates.get(i).getText());
			}
			System.out.println(mentionDates);

			List<WebElement> authorIDs = driver.findElements(By
					.xpath(PropertyHandler.getCTripProperties().getProperty(
							"author_xpath")));
			ArrayList<String> authorIDsText = new ArrayList<String>();
			for (int i = 0; i < authorIDs.size(); i++) {
				authorIDsText.add(authorIDs.get(i).getText());
			}
			System.out.println(authorIDs);

			List<WebElement> ratings = driver.findElements(By
					.xpath(PropertyHandler.getCTripProperties().getProperty(
							"rating_xpath")));
			ArrayList<String> ratingsText = new ArrayList<String>();
			for (int i = 0; i < ratings.size(); i++) {
				ratingsText.add(ratings.get(i).getText());
			}
			System.out.println(ratings);
			// driver.close();

			System.out.println("Comments Size: " + commentsText.size());
			System.out.println("Mention Time Size: " + mentionDatesText.size());
			System.out.println("Author Size: " + authorIDsText.size());
			System.out.println("Rating Size: " + ratingsText.size());

			for (int i = 1; i < commentsText.size(); i++) {
				ReviewData rdObject = new ReviewData();

				rdObject.setHarvesterID(stubID.toString());
				rdObject.setAuthorId(authorIDsText.get(i));
				rdObject.setComment(commentsText.get(i));
				rdObject.setMentionedDate(mentionDatesText.get(i));
				rdObject.setRatings(ratingsText.get(i));

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