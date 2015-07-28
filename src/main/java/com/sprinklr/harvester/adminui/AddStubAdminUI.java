package com.sprinklr.harvester.adminui;

import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.sprinklr.harvester.test.testCtrip;
import com.sprinklr.harvester.util.JdbcConnect;
import com.sprinklr.harvester.util.PropertyHandler;

public class AddStubAdminUI {
	public static final Logger AdminUILogger = testCtrip.logger;
	
	public void addStubInAdminUI() {
		
		AdminUILogger.info("AddStubAdminUI.addStubInAdminUI() Initialising the FirefoxDriver for adding new stub...");
		WebDriver driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		
		
		String url = PropertyHandler.getProperties().getProperty("adminURL");
		AdminUILogger.info("AddStubAdminUI.addStubInAdminUI() Admin url to open : " + url );
		String client = PropertyHandler.getProperties().getProperty("client");
		AdminUILogger.info(" AddStubAdminUI.addStubInAdminUI() Client : " + client);
		
		driver.get(url + "/" + client + "/login.jsp");
		AdminUILogger.info("AddStubAdminUI.addStubInAdminUI() opening page : " + url + "/" + client + "/login.jsp");
		
		driver.findElement(By.name("uname")).sendKeys("kam");
		AdminUILogger.info("AddStubAdminUI.addStubInAdminUI() Passing username : Kam");
		driver.findElement(By.name("passcode")).sendKeys("nba2012");
		AdminUILogger.info("AddStubAdminUI.addStubInAdminUI() Passing password : nba2012");
		driver.findElement(By.name("submit")).click();
		
		
		List<String> urlListToInsert = getUrlList();
		AdminUILogger.info("AddStubAdminUI.addStubInAdminUI() getting list of stubs/urls to add, from csv file");
		Iterator<String> urlListIterator = urlListToInsert.iterator();

		
		while (urlListIterator.hasNext()) {
			String stubURL = urlListIterator.next();
			
			AdminUILogger.info("AddStubAdminUI.addStubInAdminUI() checking for stubs existance : " + stubURL);
			
			if (JdbcConnect.getStubID(stubURL) != 0) {
				//System.out.println("Stub alrady exists in DB! " + stubURL);
				AdminUILogger.warn("AddStubAdminUI.addStubInAdminUI() stub already exists in DB! " + stubURL);
				continue;
			}
			
			driver.findElement(By.id("extdd-18")).click();
			driver.switchTo().frame(0);
			clickManageDropDownBox(driver);
			driver.findElement(By.id("ext-gen57")).click();
			
			driver.switchTo().defaultContent();
			driver.switchTo().frame(1);
			driver.findElement(By.name("url")).sendKeys(stubURL);
			

			Select select = new Select(driver.findElement(By.name("source_id")));
			select.selectByVisibleText(PropertyHandler.getProperties()
					.getProperty("source"));

			Select selectOrg = new Select(driver.findElement(By.name("org_id")));
			selectOrg.selectByVisibleText(PropertyHandler.getProperties()
					.getProperty("location"));

			driver.findElement(By.name("Submit")).click();
			pause(5);
			driver.switchTo().defaultContent();
		}
		AdminUILogger.info("AddStubAdminUI.addStubInAdminUI() closing FirefoxDriver");
		driver.close();
	}

	/**
	 * Method to fetch list of stub url from csv file
	 * 
	 * @return List<String> list of stub url from csv file
	 */
	public static List<String> getUrlList() {
		AdminUILogger.info("AddStubAdminUI.getUrlList() returning list of Urls from csv file");
		return JdbcConnect.csvParser();
	}

	/**
	 * Method to click Manage Drop Down Box
	 */
	public void clickManageDropDownBox(WebDriver driver) {
		AdminUILogger.info("AddStubAdminUI.clickManageDropDownBox() clicking manage drop down box");
		Actions builder1 = new Actions(driver);
		WebElement e = driver.findElement(By
				.xpath("//em[@class='x-btn-split']"));
		builder1.moveToElement(e);

		int h = e.getSize().getHeight();
		int w = e.getSize().getWidth();

		Actions builder2 = new Actions(driver);
		builder2.moveToElement(e, w - 5, h).click().build().perform();
		pause(2);
	}

	/**
	 * 
	 * @param i
	 */
	private void pause(int i) {
		AdminUILogger.info("AddStubAdminUI.pause() pausing for time : " + i);
		try {
			Thread.sleep(i * 1000);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}