package com.milvik.mip.pageobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.milvik.mip.pageutil.MIP_BasePage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_ListOfferPage extends MIP_BasePage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_SearchAndModifyPage");
	}

	public MIP_ListOfferPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	@FindBy(id = "listOffers")
	public WebElement listoffer_table;

	public boolean validateListOffersTable() {
		logger.info("validating list offer table heading");
		this.waitForElementToVisible(By.id("listOffers"));
		if (this.waitForElementToVisible(
				By.xpath("//table[@id='listOffers']/thead/tr/th[contains(text(),'Offer Name')]"))
				.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath("//table[@id='listOffers']/thead/tr/th[contains(text(),'Offer Description')]"))
						.isDisplayed()) {
			logger.info("validated list offer table heading ad true");
			return true;
		}
		logger.info("validating list offer table heading as false");
		return false;
	}

	public int getListOfferCount() {
		logger.info("Getting offer count available");
		this.waitForElementToVisible(By
				.xpath("//table[@id='listOffers']/tbody/tr"));
		return driver.findElements(
				By.xpath("//table[@id='listOffers']/tbody/tr")).size();
	}

	public Map<String, String> getOfferNameAndDescription() {
		Map<String, String> offerDetails = new HashMap<String, String>();
		logger.info("Getting getOfferNameAndDescription in list offer page");
		this.waitForElementToVisible(By
				.xpath("//table[@id='listOffers']/tbody/tr"));
		int size = driver.findElements(
				By.xpath("//table[@id='listOffers']/tbody/tr")).size();
		for (int i = 1; i <= size; i++) {
			offerDetails.put(
					driver.findElement(
							By.xpath("//table[@id='listOffers']/tbody/tr[" + i
									+ "]/td[1]/a")).getText().trim()
							.replaceAll("\\s", ""),
					driver.findElement(
							By.xpath("//table[@id='listOffers']/tbody/tr[" + i
									+ "]/td[2]")).getText().trim()
							.replaceAll("\\s", ""));
		}
		logger.info("Fetched getOfferNameAndDescription in list offer page");
		return offerDetails;
	}

	public void clickOnOffer(String offerName) {
		logger.info("Clicking onoffer name " + offerName);
		this.waitForElementToVisible(By
				.xpath("//table[@id='listOffers']/tbody/tr/td/a"));
		driver.findElement(By.linkText(offerName)).click();
		logger.info("cliced on offer " + offerName);
	}

	public String getOfferNameInViewOfferDetails() {
		logger.info("Getting offer Name in View Offer DEtails page");
		this.waitForElementToVisible(By
				.xpath("//div[contains(text(),'Offer Name')]"));
		return this
				.waitForElementToVisible(
						By.xpath("//div[contains(text(),'Offer Name')]//following-sibling::div"))
				.getText();
	}

	public String getOfferDescriptionInViewOfferDetails() {
		logger.info("Getting offer description in View Offer DEtails page");
		this.waitForElementToVisible(By
				.xpath("//div[contains(text(),'Offer Description')]"));
		return this
				.waitForElementToVisible(
						By.xpath("//div[contains(text(),'Offer Description')]//following-sibling::div"))
				.getText();
	}

	public boolean validateViewOfferDetails() {
		logger.info("validating view offer details");
		this.waitForElementToVisible(By
				.xpath("//h3[contains(text(),'View Offer Details')]"));
		boolean value1 = this.waitForElementToVisible(
				By.xpath("//div[contains(text(),'Offer Name')]")).isDisplayed();
		boolean value2 = this.waitForElementToVisible(
				By.xpath("//div[contains(text(),'Offer Description')]"))
				.isDisplayed();
		boolean value3 = this.waitForElementToVisible(
				By.xpath("//*[contains(text(),'Offer Range (LKR)')]"))
				.isDisplayed();
		if (value1 && value2 && value3
				&& driver.findElement(By.id("backBtn")).isDisplayed()) {
			return true;
		}
		return false;
	}

	public List<String> getOfferedCover() {
		logger.info("Getting offered cover values  in View Offer Details page");
		List<String> offered_Cover = new ArrayList<String>();
		this.waitForElementToVisible(By.id("mainDiv"));
		List<WebElement> details = driver.findElements(By
				.xpath("//div[contains(text(),'Offered Cover')]"));
		if (details.size() != 0) {
			List<WebElement> cover_details = driver.findElements(By
					.name("offerCover"));
			for (WebElement e : cover_details) {
				offered_Cover.add(e.getAttribute("value")
						.replaceAll("[.,]", ""));
			}
		}
		return offered_Cover;
	}

	public void clickOnBack() {
		this.waitForElementToVisible(By.id("backBtn")).click();
	}

	public List<String> getNaturalDeathCover() {
		logger.info("Getting Natural Death cover values  in View Offer Details page");
		List<String> offered_Cover = new ArrayList<String>();
		this.waitForElementToVisible(By.id("mainDiv"));
		List<WebElement> details = driver.findElements(By
				.xpath("//div[contains(text(),'Natural Death Cover')]"));
		if (details.size() != 0) {
			List<WebElement> cover_details = driver.findElements(By
					.name("lifeCover"));
			for (WebElement e : cover_details) {
				offered_Cover.add(e.getAttribute("value")
						.replaceAll("[,.]", ""));
			}
		}
		return offered_Cover;
	}

	public List<String> getOfferedCoverCharges() {
		logger.info("Getting offered cover charges values  in View Offer Details page");
		List<String> offered_Cover = new ArrayList<String>();
		this.waitForElementToVisible(By.id("mainDiv"));
		List<WebElement> details = driver.findElements(By
				.xpath("//div[contains(text(),'Offered Cover Charges')]"));
		if (details.size() != 0) {
			List<WebElement> cover_details = driver.findElements(By
					.name("offerCoverCharges"));
			for (WebElement e : cover_details) {
				offered_Cover.add(e.getAttribute("value")
						.replaceAll("[,.]", ""));
			}
		}
		return offered_Cover;
	}
}
