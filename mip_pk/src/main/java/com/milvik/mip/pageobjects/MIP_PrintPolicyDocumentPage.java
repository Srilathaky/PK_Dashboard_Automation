package com.milvik.mip.pageobjects;

import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.milvik.mip.pageutil.MIP_CustomerManagementPage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_PrintPolicyDocumentPage extends MIP_CustomerManagementPage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_LoyaltyPage");
	}
	@FindBy(id = "nic")
	public WebElement nic;
	@FindBy(id = "msisdn")
	public WebElement msisdn;
	@FindBy(id = "subsChnlId")
	public WebElement channel;
	@FindBy(id = "searchBtn")
	public WebElement search;
	@FindBy(id = "clearBtn")
	public WebElement clear;

	public MIP_PrintPolicyDocumentPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public String getErrorMessage() {
		return this.waitForElementToVisible(By.id("message_div")).getText();
	}

	public MIP_PrintPolicyDocumentPage selectSubscribedChannel(String channel) {
		WebElement ele = this.waitForElementToVisible(By.id("subsChnlId"));
		this.selectDropDownbyText(ele, channel);
		return this;
	}

	public boolean switchToPrintPolicyWindow() {
		WebDriverWait w = new WebDriverWait(driver, 10);
		w.until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> windows = driver.getWindowHandles();
		String parentwindow = driver.getWindowHandle();
		for (String s : windows) {
			if (!s.equalsIgnoreCase(parentwindow)) {
				driver.switchTo().window(s);
			}
		}
		driver.manage().window().maximize();
		try {
			boolean val = driver.findElement(By.linkText("www.dialog.lk"))
					.isDisplayed();

			driver.close();
			driver.switchTo().window(parentwindow);
			return val;
		} catch (Exception e) {
			driver.close();
			driver.switchTo().window(parentwindow);
			return false;
		}

	}
}
