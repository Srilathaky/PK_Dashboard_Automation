package com.milvik.mip.pageobjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.milvik.mip.pageutil.MIP_BasePage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_ChangePasswordPage extends MIP_BasePage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_ChangePasswordPage");
	}

	public MIP_ChangePasswordPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	@FindBy(id = "currentHash")
	WebElement old_pass;
	@FindBy(id = "newHash")
	WebElement new_pass;
	@FindBy(id = "confirmHash")
	WebElement conf_pass;

	public boolean validateChangePassObjects() {
		logger.info("Validating change password objects");
		if (this.waitForElementToVisible(By.id("currentHash")).isDisplayed()
				&& this.waitForElementToVisible(By.id("newHash")).isDisplayed()
				&& this.waitForElementToVisible(By.id("confirmHash"))
						.isDisplayed()
				&& this.waitForElementToVisible(By.id("saveBtn")).isDisplayed()
				&& this.waitForElementToVisible(By.id("clearBtn"))
						.isDisplayed()) {
			return true;
		}
		return false;
	}

	public MIP_ChangePasswordPage clickOnClear() {
		this.clickOnElement(By.id("clearBtn"));
		return this;
	}

	public MIP_ChangePasswordPage enterPassworddata(String oldpass,
			String newpass, String confpass) {
		logger.info("Changing the password");
		this.waitForElementToVisible(By.id("currentHash")).sendKeys(oldpass);
		logger.info("Entered  old  password");
		this.waitForElementToVisible(By.id("newHash")).sendKeys(newpass);
		logger.info("Entered  new  password");
		this.waitForElementToVisible(By.id("confirmHash")).sendKeys(confpass);
		logger.info("Entered  confirmed  password");
		return this;
	}

	public String getOldPassword() {
		return this.waitForElementToVisible(By.id("currentHash")).getAttribute(
				"value");
	}

	public String getNewPassword() {
		return this.waitForElementToVisible(By.id("newHash")).getAttribute(
				"value");
	}

	public String getConfPassword() {
		return this.waitForElementToVisible(By.id("confirmHash")).getAttribute(
				"value");
	}

	public String getValidationMsg() {
		logger.info("Getting Validation message in Change Password page");
		return this.waitForElementToVisible(
				By.xpath("//div[@id='validationMessages']/li")).getText();
	}

	public MIP_ChangePasswordPage clickOnSave() {
		logger.info("Clicking on Save Changed button in Change Password page");
		this.clickOnElement(By.id("saveBtn"));
		return this;
	}

/*	public MIP_SearchCustomerPage confirmChangePass(String option) {
		logger.info("Confirming the ChangePassword pop-up Page");
		this.confirmPopUp(option);
		return new MIP_SearchCustomerPage(driver);
	}*/

	public String getSuccessMsg() {
		logger.info("Getting Success message in ChangePasseord Page");
		return this.waitForElementToVisible(
				By.xpath("//div[@class='error-div-body']/div[2]")).getText();
	}
}
