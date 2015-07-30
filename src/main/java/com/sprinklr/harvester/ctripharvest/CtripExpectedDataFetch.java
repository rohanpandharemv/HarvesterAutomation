package com.sprinklr.harvester.ctripharvest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import com.sprinklr.harvester.model.InitialData;
import com.sprinklr.harvester.model.ReviewData;
import com.sprinklr.harvester.util.PropertyHandler;
import com.sprinklr.harvester.util.StaticUtils;

/**
 * Contain functions to fetch the data from the actual site -- Source - CTRIP
 *
 */
public class CtripExpectedDataFetch {

	public final static Logger LOGGER = Logger.getLogger(CtripExpectedDataFetch.class);
	
	public static HashMap<String, HashMap<String, ArrayList<ReviewData>>> getActualData(
	        HashMap<Integer, InitialData> testData) {

		LOGGER.info("CtripExpectedDataFetch - Inside get actual data method of class CtripExpectedDataFetch............");

		HashMap<String, HashMap<String, ArrayList<ReviewData>>> expectedReviewDataPerStub = new HashMap<String, HashMap<String, ArrayList<ReviewData>>>();
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

			String sort_dropdown = "//select[contains(@id,'selCommentSortType')] | //select[contains(@class,'select_sort')]";

			Select dropdown = new Select(driver.findElement(By.xpath(sort_dropdown)));
			dropdown.selectByIndex(1);
			StaticUtils.pause(5);

			List<WebElement> comments = driver.findElements(By.xpath(PropertyHandler.getSourceProperties().getProperty(
			        "content_xpath")));
			ArrayList<String> commentsText = new ArrayList<String>();
			for (int i = 0; i < comments.size(); i++) {
				commentsText.add(comments.get(i).getText());
			}

			List<WebElement> mentionDates = driver.findElements(By.xpath(PropertyHandler.getSourceProperties()
			        .getProperty("mentionTime_xpath")));
			ArrayList<String> mentionDatesText = new ArrayList<String>();
			for (int i = 0; i < mentionDates.size(); i++) {
				mentionDatesText.add(mentionDates.get(i).getText());
			}

			List<WebElement> authorIDs = driver.findElements(By.xpath(PropertyHandler.getSourceProperties()
			        .getProperty("author_xpath")));
			ArrayList<String> authorIDsText = new ArrayList<String>();
			for (int i = 0; i < authorIDs.size(); i++) {
				authorIDsText.add(authorIDs.get(i).getText());
			}

			List<WebElement> ratings = driver.findElements(By.xpath(PropertyHandler.getSourceProperties().getProperty(
			        "rating_xpath")));
			ArrayList<String> ratingsText = new ArrayList<String>();
			for (int i = 0; i < ratings.size(); i++) {
				ratingsText.add(ratings.get(i).getText());
			}

			System.out.println("Comments Size: " + commentsText.size());
			System.out.println("Mention Time Size: " + mentionDatesText.size());
			System.out.println("Author Size: " + authorIDsText.size());
			System.out.println("Rating Size: " + ratingsText.size());

			String givenDate = PropertyHandler.getProperties().getProperty("date");
			Date givenMentionDate = StaticUtils.convertCtripStringToDate(givenDate);

			for (int i = 0; i < commentsText.size(); i++) {
				Date date = StaticUtils.convertCtripStringToDate(mentionDatesText.get(i));
				if (date.before(givenMentionDate)) {
					break;
				}

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
		driver.close();
		return expectedReviewDataPerStub;
	}
}