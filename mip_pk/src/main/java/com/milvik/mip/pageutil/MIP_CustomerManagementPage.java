package com.milvik.mip.pageutil;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.milvik.mip.utility.MIP_Logging;

public class MIP_CustomerManagementPage extends MIP_BasePage {

	public static final String BIMA_ACCIDENT_PROTECTION = "Warid\\Jazz Bima Accident Protection";
	public static final String BIMAHEALTH = "bimahealth";
	public static final String BIMA_PREPAID_STATUS = "1";
	public static final String BIMA_POSTPAID_STATUS = "0";
	public static final String BIMA_IS_WARID = "1";
	public static final String BIMA_IS_JAZZ = "0";
	public static final String HEALTH_BENEFIT_LEVEL1 = "HP $24 - Life $450";
	public static final String HEALTH_BENEFIT_LEVEL2 = "HP $48 - Life $900";
	public static final String HEALTH_BENEFIT_LEVEL3 = "HP $96 - Life $1,800";
	public static final String BIMA_JAZZ = "Jazz";
	public static final String BIMA_WARID = "Warid";
	public static final String BIMA_PREPAID = "PREP";
	public static final String BIMA_POSTPAID = "POST";

	protected WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_CustomerManagementPage");
	}

	public MIP_CustomerManagementPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	@FindBy(id = "msisdn")
	public WebElement msisdn;
	@FindBy(id = "search-icon")
	public WebElement search_icon;

	public void enterMSISDN(String msisdn) {
		logger.info("Entering msisdn value " + msisdn);
		for (int i = 0; i < 5; i++) {
			try {
				WebElement ele = this.waitForElementToVisible(By.id("msisdn"));
				ele.sendKeys(msisdn);
				if (ele.getAttribute("value").equals(msisdn.trim()))
					break;
			} catch (StaleElementReferenceException e) {
				logger.info("Entering msisdn after stale element reference exception");
			}
		}
		logger.info("Entered msisdn value");
	}

	public void clickOnSeachIcon() {
		logger.info("clicking on search Icon");
		this.waitForElementToVisible(By.id("search-icon")).click();
		logger.info("Clicked on Search Icon");
	}

	public void clickOnSeachButton() {
		logger.info("clicking on search button");
		this.waitForElementToVisible(By.id("searchBtn")).click();
		logger.info("Clicked on Search button");
	}

	public void selectAvailableInformation(String offer) {
		logger.info("Selecting  offer " + offer);
		if (MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION.contains(offer)) {
			this.waitForElementTobeClickable(By
					.xpath("//input[@id='offerId'][@value='1']"));
			this.waitForElementToVisible(
					By.xpath("//input[@id='offerId'][@value='1']")).click();
		}
		if (offer.contains(MIP_CustomerManagementPage.BIMAHEALTH)) {
			this.waitForElementTobeClickable(By
					.xpath("//input[@id='offerId'][@value='2']"));
			this.waitForElementToVisible(
					By.xpath("//input[@id='offerId'][@value='2']")).click();
		}
		logger.info("Selected the offer " + offer);
	}

	public String validateMsisdnField() {
		logger.info("validating MSISDN Field");
		return this.waitForElementToVisible(
				By.xpath("//input[@id='msisdn'][@readonly='readonly']"))
				.getAttribute("value");

	}

	public boolean validateSeachButton() {
		logger.info("validate search button");
		try {
			if (this.waitForElementToVisible(By.id("searchBtn")).isDisplayed())
				return true;

		} catch (Exception e) {
			logger.info("Search button is not displayed");
			return false;
		}
		return false;

	}

	public MIP_CustomerManagementPage clickOnSave() {
		logger.info("Clicking on Save button in Customer Registration page");
		this.waitForElementTobeClickable(By.id("saveBtn"));
		this.clickOnElement(By.id("saveBtn"));
		return this;
	}

	public boolean validateBackBtn() {
		return this.waitForElementToVisible(By.id("backBtn")).isDisplayed();
	}

	public void clickOnBackBtn() {
		this.waitForElementToVisible(By.id("backBtn")).click();
	}

	public String getValidationMessage() {
		String message = "";
		this.waitForElementToVisible(By.id("validationMessages"));
		List<WebElement> msg = driver.findElements(By
				.xpath("//div[@id='validationMessages']//li"));
		for (WebElement e : msg) {
			message = message + e.getText();
		}
		return message;
	}

	public void confirmCustManagementPopup(String option) {
		this.confirmPopUp(option);
	}

	public String getSuccessMessage() {
		WebDriverWait w = new WebDriverWait(driver, 50);
		return w.until(
				ExpectedConditions.visibilityOfElementLocated(By
						.id("message_div"))).getText();
	}

	public void clickOnSaveChanges() {
		logger.info("Clicking on Save Changes button");
		this.waitForElementTobeClickable(By.id("saveBtn")).click();
		logger.info("Clicked on Save Changes button");
	}

	public String getPrePostStatus() {
		logger.info("Getting PrePost Status information");
		WebElement ele_prepaid = this.waitForElementToVisible(By
				.xpath("//input[@id='isPrepaid'][@disabled]"));
		WebElement ele_postpaid = this.waitForElementToVisible(By
				.xpath("//input[@id='isPostpaid'][@disabled]"));
		if (ele_prepaid.isSelected()) {
			return BIMA_PREPAID;
		} else if (ele_postpaid.isSelected()) {
			return BIMA_POSTPAID;
		}
		return "";
	}
}
