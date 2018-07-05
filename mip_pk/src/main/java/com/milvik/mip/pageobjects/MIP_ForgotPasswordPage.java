package com.milvik.mip.pageobjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.milvik.mip.pageutil.MIP_BasePage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_ForgotPasswordPage extends MIP_BasePage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_ForgotPasswordPage");
	}

	public MIP_ForgotPasswordPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public boolean getForgotPassObjects() {
		try {
			logger.info("Validating the forgot password objects");
			this.waitForElementToPresent(By.id("userUid"));
			this.waitForElementToPresent(By.id("submitBtn"));
			this.waitForElementToPresent(By.id("goBackBtn"));
			return true;
		} catch (Exception e) {
			logger.error("Error while valdating forgot password objects", e);
			return false;
		}
	}

	public MIP_ForgotPasswordPage enterUserId(String userId) {
		logger.info("Entering user id");
		this.waitForElementToPresent(By.id("userUid")).sendKeys(userId);
		return this;

	}

	public String getErrorMsg(String text) {
		String msg = "";
		try {
			logger.info("Getting error message");
			this.waitForElementToPresent(By.id("message_div"));
			msg = driver.findElement(By.id("message_div")).getText();
		} catch (Exception e) {
			logger.error("Error while getting error message");
		}
		return msg;
	}

	public void clickOnSubmit() {
		logger.info("Clicking on Submit button");
		this.clickOnElement(By.id("submitBtn"));
	}

	public MIP_LoginPage clickOnCancel() {
		logger.info("Clicking On Cancel");
		this.clickOnElement(By.id("goBackBtn"));
		return new MIP_LoginPage(driver);
	}
}
