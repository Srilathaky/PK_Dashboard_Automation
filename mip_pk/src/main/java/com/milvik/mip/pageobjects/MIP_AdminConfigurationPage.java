package com.milvik.mip.pageobjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.milvik.mip.pageutil.MIP_BasePage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_AdminConfigurationPage extends MIP_BasePage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_AdminConfigurationPage");
	}

	public MIP_AdminConfigurationPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public boolean validateAdminConfigObjects() {
		logger.info("validating admin configuration objects");
		boolean value1 = this
				.waitForElementToVisible(By.id("label_defaultPwd")).getText()
				.equalsIgnoreCase("Default Password : *");
		boolean value2 = this.waitForElementToVisible(By.id("defaultPwd"))
				.getAttribute("type").equalsIgnoreCase("text");
		boolean value3 = this
				.waitForElementToVisible(By.id("label_userLoginPrefix"))
				.getText().equalsIgnoreCase("Prefix for User Login ID : *");
		boolean value4 = this.waitForElementToVisible(By.id("userLoginPrefix"))
				.getAttribute("type").equalsIgnoreCase("text");
		boolean value5 = driver.findElement(By.id("label_pwdHistoryLimit"))
				.getText()
				.equalsIgnoreCase("Maximum Password History Limit : *");
		boolean value6 = driver.findElement(By.id("pwdHistoryLimit"))
				.getAttribute("type").equalsIgnoreCase("text");
		boolean value7 = driver.findElement(By.id("label_maxLoginAttempts"))
				.getText().equalsIgnoreCase("Maximum Login Attempts : *");
		boolean value8 = driver.findElement(By.id("maxLoginAttempts"))
				.getAttribute("type").equalsIgnoreCase("text");
		boolean value9 = driver.findElement(By.id("label_msisdnCodes"))
				.getText().trim().equalsIgnoreCase("MSISDN Codes : *".trim());
		boolean value10 = driver.findElement(By.id("msisdnCodes")).getTagName()
				.equalsIgnoreCase("textarea");
		boolean value11 = driver
				.findElement(By.id("label_announcementMessage")).getText()
				.equalsIgnoreCase("Home screen announcements : *");
		boolean value12 = driver.findElement(By.id("announcementMessage"))
				.getTagName().equalsIgnoreCase("textarea");
		boolean value13 = driver
				.findElement(By.id("label_maxIdleCount"))
				.getText()
				.equalsIgnoreCase(
						"Maximum number of months the customer remains idle (Idle Status) : *");
		boolean value14 = driver.findElement(By.id("maxIdleCount"))
				.getAttribute("type").equalsIgnoreCase("text");
		boolean value15 = driver.findElement(By.id("saveBtn")).isDisplayed();
		if (value1 && value2 && value3 && value4 && value5 && value6 && value7
				&& value8 && value9 && value10 && value11 && value12 && value15
				&& value13 && value14) {
			return true;
		}
		logger.info("validating admin configuration objects failed");
		return false;
	}

	public MIP_AdminConfigurationPage editAdminConfigInfo(String password,
			String loginid, String passhist, String loginattempt,
			String idlestate, String announce, String msisdnCode) {
		logger.info("Entering Admin Configure Information");
		if (!password.equalsIgnoreCase("")) {
			WebElement ele = this.waitForElementToVisible(By.id("defaultPwd"));
			ele.clear();
			ele.sendKeys(password);
		}
		if (!loginid.equalsIgnoreCase("")) {
			WebElement ele = this.waitForElementToVisible(By
					.id("userLoginPrefix"));
			ele.clear();
			ele.sendKeys(loginid);
		}
		if (!passhist.equalsIgnoreCase("")) {
			WebElement ele = this.waitForElementToVisible(By
					.id("pwdHistoryLimit"));
			ele.clear();
			ele.sendKeys(passhist);
		}
		if (!loginattempt.equalsIgnoreCase("")) {
			WebElement ele = null;
			for (int i = 0; i < 3; i++) {
				try {
					ele = this.waitForElementToVisible(By
							.id("maxLoginAttempts"));
					ele.clear();
					ele.sendKeys(loginattempt);
					break;
				} catch (StaleElementReferenceException e) {
					logger.info("Entering loginattempt after " + e.getMessage());
				}
			}
		}
		if (!idlestate.equalsIgnoreCase("")) {
			WebElement ele = null;
			for (int i = 0; i < 3; i++) {
				try {
					ele = this.waitForElementToVisible(By.id("maxIdleCount"));
					ele.clear();
					ele.sendKeys(idlestate);
					break;
				} catch (StaleElementReferenceException e) {
					logger.info("Entering maxIdleCount after " + e.getMessage());
				}
			}

		}
		if (!announce.equalsIgnoreCase("")) {
			WebElement ele = null;
			for (int i = 0; i < 3; i++) {
				try {
					ele = this.waitForElementToVisible(By
							.id("announcementMessage"));
					ele.clear();
					ele.sendKeys(announce);
					break;
				} catch (StaleElementReferenceException e) {
					logger.info("Entering announce after " + e.getMessage());
				}
			}
		}
		if (!msisdnCode.equalsIgnoreCase("")) {
			WebElement ele = null;
			for (int i = 0; i < 3; i++) {
				try {
					ele = this.waitForElementToVisible(By.id("msisdnCodes"));
					ele.clear();
					ele.sendKeys(msisdnCode);
					break;
				} catch (StaleElementReferenceException e) {
					logger.info("Entering announce after " + e.getMessage());
				}
			}
		}

		return this;
	}

	public String getSuccessMessage() {
		return this.waitForElementToVisible(
				By.xpath("//div[@class='error-div-body']/div[2]")).getText();
	}

	public MIP_AdminConfigurationPage clickOnSave() {
		logger.info("Clicking on Save button in Admin Config page");
		this.clickOnElement(By.id("saveBtn"));
		return this;
	}

	public MIP_AdminConfigurationPage gotoAdminConfigPage() {
		this.waitForElementToVisible(By.linkText("here")).click();
		return this;
	}

	public String getAdminConfigInfo(String value) {
		logger.info("Getting Admin Config Info");
		this.waitForElementToVisible(By.id("maxIdleCount"));
		if (value.equalsIgnoreCase("default_password")) {

			return this.waitForElementToVisible(By.id("defaultPwd"))
					.getAttribute("value");
		} else if (value.equalsIgnoreCase("user_login_prefix")) {
			return this.waitForElementToVisible(By.id("userLoginPrefix"))
					.getAttribute("value");
		} else if (value.equalsIgnoreCase("password_history_limit")) {
			return this.waitForElementToVisible(By.id("pwdHistoryLimit"))
					.getAttribute("value");
		} else if (value.equalsIgnoreCase("max_login_attempts")) {
			return this.waitForElementToVisible(By.id("maxLoginAttempts"))
					.getAttribute("value");
		} else if (value.equalsIgnoreCase("max_idle_count")) {
			return this.waitForElementToVisible(By.id("maxIdleCount"))
					.getAttribute("value");
		} else if (value.equalsIgnoreCase("announcement_message")) {
			return this.waitForElementToVisible(By.id("announcementMessage"))
					.getText();
		} else if (value.equalsIgnoreCase("msisdn_code")) {
			return this.waitForElementToVisible(By.id("msisdnCodes")).getText();
		}

		else
			return null;
	}

	public MIP_AdminConfigurationPage clearAdminConfigInfo(String key,
			String value) {
		logger.info("Clearing  Admin Config Info");
		if (key.equalsIgnoreCase("default_password")) {

			WebElement ele = this.waitForElementToVisible(By.id("defaultPwd"));
			ele.clear();
			ele.sendKeys(value);
		}
		if (key.equalsIgnoreCase("user_login_prefix")) {
			WebElement ele = this.waitForElementToVisible(By
					.id("userLoginPrefix"));
			ele.clear();
			ele.sendKeys(value);
		}
		if (key.equalsIgnoreCase("password_history_limit")) {
			WebElement ele = this.waitForElementToVisible(By
					.id("pwdHistoryLimit"));
			ele.clear();
			ele.sendKeys(value);
		}
		if (key.equalsIgnoreCase("max_login_attempts")) {
			WebElement ele = this.waitForElementToVisible(By
					.id("maxLoginAttempts"));
			ele.clear();
			ele.sendKeys(value);
		}
		if (key.equalsIgnoreCase("max_idle_count")) {
			WebElement ele = this
					.waitForElementToVisible(By.id("maxIdleCount"));
			ele.clear();
			ele.sendKeys(value);
		}
		if (key.equalsIgnoreCase("announcement_message")) {
			WebElement ele = this.waitForElementToVisible(By
					.id("announcementMessage"));
			ele.clear();
			ele.sendKeys(value);
		}
		if (key.equalsIgnoreCase("msisdn_code")) {
			WebElement ele = this.waitForElementToVisible(By.id("msisdnCodes"));
			ele.clear();
			ele.sendKeys(value);
		}
		return this;
	}

	public String getValidationMsg() {
		logger.info("Getting Validation message in Admin Configuration page");
		return this.waitForElementToVisible(By.id("validationMessages"))
				.getText();
	}
}
