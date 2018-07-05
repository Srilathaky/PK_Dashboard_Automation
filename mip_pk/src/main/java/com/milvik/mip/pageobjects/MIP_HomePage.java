package com.milvik.mip.pageobjects;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.milvik.mip.dbqueries.MIP_Homepage_CustomerStatistics_Queries;
import com.milvik.mip.pageutil.MIP_BasePage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_HomePage extends MIP_BasePage {
	WebDriver driver;
	public WebElement Homelink;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_HomePage");
	}

	public MIP_HomePage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public boolean validateHomePageNavigation() {
		try {
			logger.info("Validating Home Page link");
			Homelink = this.waitForElementToPresent(By.linkText("Home"));
			return Homelink.isDisplayed();
		} catch (Exception e) {
			logger.error("Exception while validating the Home Page link", e);
			return false;
		}

	}

	public boolean validateHomePageObjects(String home, String branch,
			String user, String leave, String cust, String role, String admin,
			String pass, String loguot, String announ, String cust_stat,
			String cust_statistics) {
		try {
			logger.info("Validating Home Page Objects");
			Homelink = this.waitForElementToPresent(By.linkText(home));
			this.waitForElementToPresent(By.linkText(branch));
			this.waitForElementToPresent(By.linkText(user));
			this.waitForElementToPresent(By.linkText(leave));
			this.waitForElementToPresent(By.linkText(cust));
			this.waitForElementToPresent(By.linkText(role));
			this.waitForElementToPresent(By.linkText(admin));
			this.waitForElementToPresent(By.linkText(pass));
			this.waitForElementToPresent(By.linkText(loguot));
			this.waitForElementToPresent(By.xpath("//h3[contains(text(),'"
					+ cust_stat + "')]"));

			return true;
		} catch (Exception e) {
			logger.error("Exception while validating the Home Page link", e);
			return false;
		}
	}

	public String getUserDetails() {
		logger.info("Getting User Details");
		return this.waitForElementToVisible(
				By.xpath("//div[@class='user-stat']")).getText();

	}

	public void clickOnMenu(String menu) {
		logger.info("Clicking on Menu " + menu);
		try {
			this.waitForElementToVisible(By.linkText(menu));
			((JavascriptExecutor) driver).executeScript(
					"arguments[0].click();",
					this.waitForElementToVisible(By.linkText(menu)));
			logger.info("Clicked on Menu " + menu);
		} catch (Exception e) {
			logger.error("Eror while Clicking  on Menu " + menu, e);
		}
	}

	public Map<String, String> getHomeStatisticsData(String role) {
		Map<String, String> details = new HashMap<String, String>();
		logger.info("Gettng Home Statistics Data");
		if (role.equalsIgnoreCase(MIP_Homepage_CustomerStatistics_Queries.BUSSINESS_ADMINISTRATOR_ROLE)) {
			details.put(
					"reg_by_call_center",
					this.waitForElementToVisible(
							By.xpath("//div[contains(text(),'Total customers registered by call center agent:')]//following-sibling::div"))
							.getText());
			details.put(
					"confirmed_by_call_center",
					this.waitForElementToVisible(
							By.xpath("//div[contains(text(),'Total customers confirmed by call center agent:')]//following-sibling::div"))
							.getText());
		} else {
			details.put(
					"XL_Reg",
					this.waitForElementToVisible(
							By.xpath("//div[contains(text(),'Total XL Customers Registered :')]//following-sibling::div"))
							.getText());
			details.put(
					"XL_Confirmed",
					this.waitForElementToVisible(
							By.xpath("//div[contains(text(),'Total XL Customers Confirmed :')]//following-sibling::div"))
							.getText());
			details.put(
					"HP_Reg",
					this.waitForElementToVisible(
							By.xpath("//div[contains(text(),'Total HP Customers Registered :')]//following-sibling::div"))
							.getText());
			details.put(
					"HP_Confirmed",
					this.waitForElementToVisible(
							By.xpath("//div[contains(text(),'Total HP Customers Confirmed :')]//following-sibling::div"))
							.getText());
		}

		return details;
	}
}
