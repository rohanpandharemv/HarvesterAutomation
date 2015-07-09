package com.sprinklr.harvester.adminui;

import java.util.*;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import com.sprinklr.harvester.util.JdbcConnect;
import com.sprinklr.harvester.util.PropertyHandler;

public class AddStubAdminUI {

	public void addStubInAdminUI() {

		WebDriver driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		String url = PropertyHandler.getProperties().getProperty("adminURL");
		String client = PropertyHandler.getProperties().getProperty("client");
		driver.get(url + "/" + client);

		driver.findElement(By.name("uname")).sendKeys("kam");
		driver.findElement(By.name("passcode")).sendKeys("nba2012");
		driver.findElement(By.name("submit")).click();

		List<String> urlListToInsert = getUrlList();
		Iterator<String> urlListIterator = urlListToInsert.iterator();

		while (urlListIterator.hasNext()) {
			driver.findElement(By.id("extdd-18")).click();
			driver.switchTo().frame(0);
			clickManageDropDownBox(driver);
			driver.findElement(By.id("ext-gen57")).click();
			driver.switchTo().defaultContent();

			driver.switchTo().frame(1);
			driver.findElement(By.name("url")).sendKeys(urlListIterator.next());

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
	}

	/**
	 * Method to fetch list of stub url from csv file
	 * 
	 * @return List<String> list of stub url from csv file
	 */
	public static List<String> getUrlList() {
		return JdbcConnect.csvParser();
	}

	/**
	 * Method to click Manage Drop Down Box
	 */
	public void clickManageDropDownBox(WebDriver driver) {
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
		try {
			Thread.sleep(i * 1000);
		} catch (Exception ex) {

		}
	}
}