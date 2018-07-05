package com.milvik.mip.pageobjects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.milvik.mip.pageutil.MIP_CustomerManagementPage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_ChangeCoverLevelPage extends MIP_CustomerManagementPage {
	private static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_ChangeCoverLevelPage");
	}

	public MIP_ChangeCoverLevelPage(WebDriver driver) {
		super(driver);
	}

	public Map<String, String> validateChangeCoverLevelPage() {
		Map<String, String> details = new HashMap<String, String>();
		logger.info("validating Change Cover Level Page");
		this.waitForElementToVisible(By.id("saveBtn"));
		details.put(
				"msisdn",
				this.waitForElementToVisible(
						By.xpath("//input[@id='msisdn'][@readonly]"))
						.getAttribute("value"));
		List<WebElement> offers = this.selectDropDownOptions(this
				.waitForElementToVisible(By.id("offerId")));
		for (WebElement e : offers) {
			if (e.getText().equalsIgnoreCase(
					MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION)) {
				details.put("bimalife",
						MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION);
				this.selectDropDownbyText(
						this.waitForElementToVisible(By.id("offerId")),
						MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION);
				details.put(
						"bimalife_offer_level",
						this.waitForElementToVisible(
								By.id("oldOfferCoverIdInsuranceOfferCover"))
								.getAttribute("value"));
			} else if (e.getText().equalsIgnoreCase(
					MIP_CustomerManagementPage.BIMAHEALTH)) {
				details.put("bimahealth", MIP_CustomerManagementPage.BIMAHEALTH);
				this.selectDropDownbyText(
						this.waitForElementToVisible(By.id("offerId")),
						MIP_CustomerManagementPage.BIMAHEALTH);
				details.put(
						"bimahealth_offer_level",
						this.waitForElementToVisible(
								By.id("oldOfferCoverIdHospitalOfferCover"))
								.getAttribute("value"));

			}
		}
		return details;

	}

	public MIP_ChangeCoverLevelPage updateAccidentCoverLevel(String cover_level) {
		logger.info("Updating Accident Cover Level in Change Cover Level Page");
		// WebElement ele = this.waitForElementToVisible(By
		// .id("offerCoverIdInsurance"));
		// this.selectDropDownbyText(ele, MIP_CustomerManagementPage.BIMALIFE);
		this.selectDropDownbyText(
				this.waitForElementToVisible(By.id("offerCoverIdInsurance")),
				cover_level.trim());
		return this;
	}

	public MIP_ChangeCoverLevelPage updateHealthCoverLevel(String cover_level) {
		logger.info("Updating Life Cover Level in Change Cover Level Page");
		WebElement ele = this.waitForElementToVisible(By.id("offerId"));
		this.selectDropDownbyText(ele, MIP_CustomerManagementPage.BIMAHEALTH);
		this.selectDropDownbyText(
				this.waitForElementToVisible(By.id("offerCoverIdHospital")),
				cover_level.trim());
		return this;
	}

	public String getAccidentCoverLevelInfo() {
		logger.info("Getiing Bima Accident protection cover level info in Update Cover Level Page");
		this.waitForElementToVisible(By
				.xpath("//span[normalize-space(text())='Save Changes']"));

		return this
				.waitForElementToVisible(
						By.xpath("//input[@id='oldOfferCoverIdInsuranceOfferCover'][@readonly='readonly']"))
				.getAttribute("value").replaceAll("[a-zA-Z,]", "");
	}

	public String getSuccessMessage() {
		WebDriverWait w = new WebDriverWait(super.driver, 30);
		return w.until(
				ExpectedConditions.visibilityOfElementLocated(By
						.xpath("//div[@class='error-div-body']/div[2]")))
				.getText();
	}

}
