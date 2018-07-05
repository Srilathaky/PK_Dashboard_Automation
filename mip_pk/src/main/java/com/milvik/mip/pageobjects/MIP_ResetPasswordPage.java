package com.milvik.mip.pageobjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.milvik.mip.pageutil.MIP_BasePage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_ResetPasswordPage extends MIP_BasePage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_ResetPasswordPage");
	}

	public MIP_ResetPasswordPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	@FindBy(id = "userUid")
	public WebElement userId;
	@FindBy(id = "search-icon")
	public WebElement searchIcon;

	public MIP_ResetPasswordPage enterUserID(String userid) {
		logger.info("Entering user id to Reset the password");
		userId.sendKeys(userid);
		return this;
	}

	public MIP_ResetPasswordPage clickOnSearchIcon() {
		logger.info("Clicking on search Icon");
		searchIcon.click();
		return this;
	}

	public MIP_ResetPasswordPage clickOnResetPassword() {
		logger.info("Clicking on Reset Password click");
		this.waitForElementToVisible(By.id("resetBtn")).click();
		return this;
	}

	public MIP_ResetPasswordPage confirmResetPassword(String option) {
		logger.info("Confirming the Reset Password pop-up");
		this.confirmPopUp(option);
		return this;
	}

	public String getConfirmationMessage() {
		logger.info("Getting Confirmation message for Reset Password");
		return this.waitForElementToVisible(
				By.xpath("//div[@class='error-div-body']/div[2]")).getText();
	}

	public String getSearchResulMessage() {
		logger.info("Getting Search Result message for Reset Password");
		return this.waitForElementToVisible(By.id("div_searchResults"))
				.getText();
	}

	public String getValidationMessage() {
		logger.info("Getting Validation message for Reset Password");
		return this.waitForElementToVisible(By.id("validationMessages"))
				.getText();
	}
}
