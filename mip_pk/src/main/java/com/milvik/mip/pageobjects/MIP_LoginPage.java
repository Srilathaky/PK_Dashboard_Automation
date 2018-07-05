package com.milvik.mip.pageobjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.milvik.mip.pageutil.MIP_BasePage;
import com.milvik.mip.utility.MIP_Logging;

/**
 * This class contains the methods related to Login Page functionality
 *
 */
public class MIP_LoginPage extends MIP_BasePage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_LoginPage");
	}
	@FindBy(id = "loginId")
	public WebElement userid;
	@FindBy(id = "userHash")
	public WebElement password;
	@FindBy(id = "loginBtn")
	public WebElement login_button;
	@FindBy(id = "clearBtn")
	public WebElement clear_button;
	@FindBy(linkText = "Forgot Password?")
	public WebElement forgotPassword_link;

	public MIP_LoginPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public MIP_HomePage login(String test_username, String test_pass) {
		logger.info("Entering the userName");
		userid.sendKeys(test_username);
		logger.info("Entering the password");
		password.sendKeys(test_pass);
		login_button.click();
		logger.info("Clicked on Submit button");
		return new MIP_HomePage(driver);
	}

	public String getErrorMsg(String text) {
		String msg = "";
		try {
			logger.info("Getting the error message");
			this.waitForElementToPresent(By
					.xpath("//div[@class='error-msg-login']"));
			msg = driver.findElement(
					By.xpath("//div[@class='error-msg-login']")).getText();
		} catch (Exception e) {
			logger.error("Error while getting the error message", e);
		}
		return msg;
	}

	public void clickOnClear() {
		logger.info("Clicking on Clear button");
		this.clickOnElement(By.id("clearBtn"));
	}

	public MIP_ForgotPasswordPage clickOnForgotPassword() {
		logger.info("Clicking on forgot password link");
		this.forgotPassword_link.click();
		return new MIP_ForgotPasswordPage(driver);
	}
}
