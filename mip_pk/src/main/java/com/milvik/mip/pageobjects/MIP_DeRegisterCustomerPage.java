package com.milvik.mip.pageobjects;

import java.util.ArrayList;
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

public class MIP_DeRegisterCustomerPage extends MIP_CustomerManagementPage {
	private WebDriver driver;
	private static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_DeRegisterCustomerPage");
	}

	public MIP_DeRegisterCustomerPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public Map<String, String> getCustomerInformation() {
		Map<String, String> dereg_details = new HashMap<String, String>();
		logger.info("Validating De-Registration Page");
		this.waitForElementToVisible(By
				.xpath("//span[contains(text(),'De-Register')]"));
		dereg_details.put("cust_name",
				this.waitForElementToVisible(By.id("customerName"))
						.getAttribute("value"));
		dereg_details.put("cust_msisdn",
				this.waitForElementToVisible(By.id("msisdNumber")).getAttribute("value"));
		dereg_details.put("cust_offer",
				this.waitForElementToVisible(By.id("customerOfferCover"))
						.getAttribute("value"));
		dereg_details.put("deducted_amnt",
				this.waitForElementToVisible(By.id("deductedOfferAmount"))
						.getAttribute("value"));
		dereg_details.put("cover_earned",
				this.waitForElementToVisible(By.id("earnedCover")).getAttribute("value"));
		dereg_details.put("conf_status",
				this.waitForElementToVisible(By.id("confirmed")).getAttribute("value"));
		this.waitForElementToVisible(By.id("backBtn")).isDisplayed();
		this.waitForElementToVisible(
				By.xpath("//span[contains(text(),'De-Register')]"))
				.isDisplayed();
		return dereg_details;
	}

	public List<String> getProductOptions() {
		List<String> product = new ArrayList<String>();
		this.waitForElementToVisible(By.id("offerId"));
		if (driver.findElements(By.xpath("//input[@id='offerId'][@value='1']"))
				.size() != 0) {
			product.add(MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION
					.trim());
		}
		if (driver.findElements(By.xpath("//input[@id='offerId'][@value='2']"))
				.size() != 0) {
			product.add(MIP_CustomerManagementPage.BIMAHEALTH.trim());
		}

		return product;
	}

	public boolean validateCustomerInfoTableHeading() {
		this.waitForElementToVisible(By.id("customerDetailsList"));
		String xpath = "//table[@id='customerDetailsList']/thead/tr/th[contains(text(),'";
		if (this.waitForElementToVisible(By.xpath(xpath + "Customer Name')]"))
				.isDisplayed()
				&& this.waitForElementToVisible(By.xpath(xpath + "MSISDN')]"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(xpath + "Confirmation Status')]"))
						.isDisplayed()
				&& this.waitForElementToVisible(By.xpath(xpath + "Product ')]"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(xpath + "Registered Product Level (Dollar)')]"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(xpath
								+ "Deducted Amount (as on date)(Dollar) ')]"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(xpath
								+ "bimalife Cover Earned in the current month (Dollar) ')]"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(xpath
								+ "bimahealth  Cover Earned in the current month (Dollar) ')]"))
						.isDisplayed()) {
			return true;

		}
		return false;
	}

	public int getTableHeadingIndex(String heading) {
		int count = -1;
		this.waitForElementToVisible(By.id("customerDetailsList"));
		List<WebElement> index = driver.findElements(By
				.xpath("//table[@id='customerDetailsList']/thead/tr/th"));
		for (int i = 0; i < index.size(); i++) {
			if (index.get(i).getText().equalsIgnoreCase(heading)) {
				return i + 1;

			}
		}
		return count;
	}

	public int getProductRow(String offer) {
		int count = -1;
		this.waitForElementToVisible(By.id("customerDetailsList"));
		List<WebElement> index = driver.findElements(By
				.xpath("//table[@id='customerDetailsList']/tbody//tr//td["
						+ getTableHeadingIndex("Product") + "]"));
		for (int i = 0; i < index.size(); i++) {
			if (index.get(i).getText().equalsIgnoreCase(offer)) {
				return i + 1;

			}
		}
		return count;
	}

	public Map<String, String> getCustomerInfoBeforeDereg(String offer) {
		Map<String, String> details = new HashMap<String, String>();
		this.waitForElementToVisible(By.id("customerDetailsList"));
		int row_count = getProductRow(offer);
		details.put(
				"cust_name",
				this.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/tbody//tr["
								+ row_count
								+ "]//td["
								+ getTableHeadingIndex("Customer Name") + "]"))
						.getText());

		details.put(
				"offer_level",
				this.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/tbody//tr["
								+ row_count
								+ "]//td["
								+ getTableHeadingIndex("Registered Product Level (Dollar)")
								+ "]")).getText());
		details.put(
				"confirmation_Status",
				this.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/tbody//tr["
								+ row_count
								+ "]//td["
								+ getTableHeadingIndex("Confirmation Status")
								+ "]")).getText());
		details.put(
				"deduted_amount",
				this.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/tbody//tr["
								+ row_count
								+ "]//td["
								+ getTableHeadingIndex("Deducted Amount (as on date)(Dollar)")
								+ "]")).getText());
		details.put(
				"bimalife_cover",
				this.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/tbody//tr["
								+ row_count
								+ "]//td["
								+ getTableHeadingIndex("bimalife Cover Earned in the current month (Dollar)")
								+ "]")).getText());
		details.put(
				"bimahealth_cover",
				this.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/tbody//tr["
								+ row_count
								+ "]//td["
								+ getTableHeadingIndex("bimahealth Cover Earned in the current month (Dollar)")
								+ "]")).getText());
		return details;
	}

	public MIP_DeRegisterCustomerPage selectAvailableOffersToDeregister(
			String product) throws InterruptedException {
		logger.info("Selecting available offers to deregister");
		if (product
				.toUpperCase()
				.trim()
				.contains(
						MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION
								.toUpperCase())) {
			this.waitForElementToPresent(By
					.xpath("//input[@id='offerId'][@value='1']"));
			this.waitForElementTobeClickable(
					By.xpath("//input[@id='offerId'][@value='1']")).click();
		}
		if (product.toUpperCase().trim()
				.contains(MIP_CustomerManagementPage.BIMAHEALTH.toUpperCase())) {
			this.waitForElementToPresent(By
					.xpath("//input[@id='offerId'][@value='2']"));
			this.waitForElementTobeClickable(
					By.xpath("//input[@id='offerId'][@value='2']")).click();
		}
		logger.info("Selected available offers to de register");
		return this;
	}

	public MIP_DeRegisterCustomerPage clickOnDeregisterButton() {
		this.waitForElementToVisible(
				By.xpath("//span[contains(text(),'De-Register')]")).click();
		return this;
	}

	public String getDeregSuccessMessage() {

		WebDriverWait w = new WebDriverWait(driver, 60);
		w.until(ExpectedConditions.visibilityOfElementLocated(By
				.id("message_div")));
		return this.waitForElementToVisible(By.id("message_div")).getText();

	}
}
