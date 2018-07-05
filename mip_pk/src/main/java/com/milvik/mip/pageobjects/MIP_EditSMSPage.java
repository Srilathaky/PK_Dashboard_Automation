package com.milvik.mip.pageobjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.milvik.mip.pageutil.MIP_BasePage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_EditSMSPage extends MIP_BasePage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_EditSMSPage");
	}

	public MIP_EditSMSPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	@FindBy(id = "smsTemplateId")
	public WebElement templateName;

	public MIP_EditSMSPage selectTemplateName(String text) {
		for (int i = 0; i < 3; i++) {
			try {
				this.selectDropDownbyText(
						this.waitForElementToVisible(By.id("smsTemplateId")),
						text);
				break;
			} catch (Exception e) {
				logger.info("selectTemplateName after exception"
						+ e.getMessage());
			}
		}

		return this;
	}

	public boolean validateEditSMSTemplate() {
		logger.info("validating EditSMS Template");
		boolean value1 = false;
		boolean value2 = false;
		boolean value3 = false;
		boolean value4 = false;
		boolean value5 = false;

		String content = this.waitForElementToVisible(By.id("smsTemplate"))
				.getTagName();
		if (content.equalsIgnoreCase("textarea")) {
			value1 = true;
		}
		if (driver.findElement(By.id("txtLen1")).getAttribute("type")
				.equalsIgnoreCase("text")) {
			value2 = true;
		}
		if (driver.findElement(By.id("smsValidity")).getAttribute("type")
				.equalsIgnoreCase("text")) {
			value3 = true;
		}
		value4 = driver.findElement(By.id("previewBtn")).isDisplayed();
		value5 = driver.findElement(By.id("saveBtn")).isDisplayed();
		if (value1 && value2 && value3 && value4 && value5) {
			return true;
		}
		return false;
	}

	public MIP_EditSMSPage enterContent(String content) {
		logger.info("validating EditSMS Template");
		this.waitForElementToVisible(By.id("smsTemplate")).sendKeys(content);
		return this;
	}

	public MIP_EditSMSPage clearContent() {
		logger.info("Clearing the sms Template");
		this.waitForElementToVisible(By.id("smsTemplate")).clear();
		return this;
	}

	public MIP_EditSMSPage enterSmsValidity(String validity) {
		logger.info("Enterng validity SMS");
		this.waitForElementToVisible(By.id("smsValidity")).sendKeys(validity);
		return this;
	}

	public MIP_EditSMSPage clearSmsValidity() {
		logger.info("Clearing validity SMS");
		this.waitForElementToVisible(By.id("smsValidity")).clear();
		return this;

	}

	public MIP_EditSMSPage confirmEditSMS(String option) {
		logger.info("Confirming the edit SMS pop-up");
		this.confirmPopUp(option);
		return this;
	}

	public MIP_EditSMSPage clikOnSave() {
		logger.info("Click on Save");
		this.clickOnElement(By.id("saveBtn"));
		return this;
	}

	public MIP_EditSMSPage clickOnPreview() {
		logger.info("Click on Preview");
		this.clickOnElement(By.id("previewBtn"));
		return this;
	}

	public MIP_EditSMSPage clickOnPreviewOk() {
		logger.info("Click on PreviewOk");
		this.clickOnElement(By.id("confirmBtn"));
		return this;
	}

	public String getSuccessMessage() {
		logger.info("Getting Success Message");
		return this.waitForElementToVisible(By.className("error-div-body"))
				.getText();
	}

	public String getPreviewContent() {
		logger.info("Getting Preview Content");
		return this.waitForElementToVisible(By.id("contactArea")).getText();
	}

	public String getvalidationMessage() {
		logger.info("Getting validation messages");
		return this.waitForElementToVisible(By.id("validationMessages"))
				.getText();
	}

}
