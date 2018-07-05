package com.milvik.mip.pageobjects;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.milvik.mip.pageutil.MIP_BasePage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_ListBranchPage extends MIP_BasePage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_ListBranchPage");
	}

	public MIP_ListBranchPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public String getSearchresultText() {
		return this.waitForElementToVisible(By.id("div_searchResults"))
				.getText();
	}

	public int getNumberofBranch() {
		logger.info("Getting Branch Count");
		int count = 0;
		this.waitForElementToVisible(By.id("branchList"));
		String[] number = this.getSearchresultText().split("\\s");
		if (!number[0].equalsIgnoreCase("one")) {
			count = Integer.parseInt(number[0]);
		} else if (number[0].equalsIgnoreCase("one"))
			count = 1;
		return count;
	}

	public List<WebElement> validatelistBranchObjects() {
		this.waitForElementToVisible(By.id("branchList"));
		return driver.findElements(By
				.xpath("//table[@id='branchList']/thead/tr/th"));
	}

	public MIP_ListBranchPage clickOnBranch(String branchname) {
		// int count = getNumberofBranch();
		this.waitForElementToVisible(By.id("branchList"));
		int j = 1;
		WebDriverWait w = new WebDriverWait(driver, 5);
		for (int i = 0; i <= j; i++) {
			try {

				/*
				 * w.until(ExpectedConditions.visibilityOfElementLocated(By
				 * .xpath
				 * ("//table[@id='branchList']/tbody//tr//td/a[contains(text(),'"
				 * + branchname.trim() + "')]")));
				 */
				w.until(ExpectedConditions.visibilityOfElementLocated(By
						.linkText(branchname.trim()))).click();
				/*
				 * driver.findElement( By.xpath(
				 * "//table[@id='branchList']/tbody//tr//td/a[contains(text(),'"
				 * + branchname.trim() + "')]")).click();
				 */

				break;
			} catch (Exception e) {

				this.waitForElementToVisible(By.linkText("Next")).click();
				j++;

			}
		}

		return this;
	}

	public String getBranchCode(String branchname) {
		// int count = getNumberofBranch();
		String text = "";
		this.waitForElementToVisible(By.id("branchList"));
		this.waitForElementToVisible(By
				.xpath("//table[@id='branchList']/tbody//tr//td/a"));
		int j = 1;
		WebDriverWait w = new WebDriverWait(driver, 1);
		for (int i = 0; i < j; i++) {
			try {
				w.until(ExpectedConditions.visibilityOfElementLocated(By
						.linkText(branchname.trim())));
				text = w.until(
						ExpectedConditions.visibilityOfElementLocated(By
								.xpath("//table[@id='branchList']/tbody//tr//td/a[text()='"
										+ branchname
										+ "']//parent::td//preceding-sibling::td")))
						.getText();
				break;
			} catch (Exception e) {
				this.waitForElementToVisible(By.linkText("Next")).click();
				j++;
			}
		}
		/*
		 * String text = ""; for (int i = 0; i < count / 4; i++) { try {
		 * driver.findElement(By
		 * .xpath("//table[@id='branchList']/tbody//tr//td/a[contains(text(),'"
		 * + branchname + "')]")); text = driver .findElement(
		 * By.xpath("//table[@id='branchList']/tbody//tr//td/a[contains(text(),'"
		 * + branchname + "')]//parent::td//preceding-sibling::td")) .getText();
		 * break; } catch (Exception e) {
		 * driver.findElement(By.linkText("Next")).click(); } }
		 */
		return text;
	}

	public String getBranchInfo(String value) {
		if (value.equalsIgnoreCase("name")) {

			return this.waitForElementToVisible(By.id("branchName"))
					.getAttribute("value");
		} else if (value.equalsIgnoreCase("street")) {
			return this.waitForElementToVisible(By.id("branchStreet"))
					.getAttribute("value");
		} else if (value.equalsIgnoreCase("region")) {
			return this.waitForElementToVisible(By.id("branchRegion"))
					.getAttribute("value");
		} else if (value.equalsIgnoreCase("city")) {
			return this.waitForElementToVisible(By.id("branchCity"))
					.getAttribute("value");
		} else
			return null;
	}

	public MIP_ListBranchPage editBranchInfo(String branchname, String street,
			String region, String city) {
		if (!branchname.equalsIgnoreCase("")) {
			WebElement ele = this.waitForElementToVisible(By.id("branchName"));
			ele.clear();
			ele.sendKeys(branchname);
		}
		if (!street.equalsIgnoreCase("")) {
			WebElement ele = this
					.waitForElementToVisible(By.id("branchStreet"));
			ele.clear();
			ele.sendKeys(street);
		}
		if (!region.equalsIgnoreCase("")) {
			WebElement ele = this
					.waitForElementToVisible(By.id("branchRegion"));
			ele.clear();
			ele.sendKeys(region);
		}
		if (!city.equalsIgnoreCase("")) {
			WebElement ele = this.waitForElementToVisible(By.id("branchCity"));
			ele.clear();
			ele.sendKeys(city);
		}
		return this;
	}

	public MIP_ListBranchPage clickOnSave() {
		logger.info("Clicking on Save button in Lis Branch page");
		this.clickOnElement(By.id("saveBtn"));
		return this;
	}

	public MIP_ListBranchPage clickOnClear() {
		logger.info("Clicking on Clear button in Lis Branch page");
		this.waitForElementToVisible(By.id("clearBtn")).click();
		return this;
	}

	public MIP_ListBranchPage clickOnBack() {
		logger.info("Clicking on Back button in Lis Branch page");
		this.waitForElementToVisible(By.id("backBtn")).click();
		return this;
	}

	public MIP_ListBranchPage clickOnDeactivate() {
		logger.info("Clicking on De-Activate button in Lis Branch page");
		this.waitForElementToVisible(By.id("deactivateBtn")).click();
		return this;
	}

	public MIP_ListBranchPage confirmOption(String option) {
		this.confirmPopUp(option);
		return this;
	}

	public MIP_ListBranchPage gotoListPage() {
		this.waitForElementToVisible(By.linkText("here")).click();
		return this;
	}

	public String validatelistbranch() {
		return this.waitForElementToVisible(
				By.xpath("//div[@class='pagetitle']")).getText();

	}

	public String getValidationMsg() {
		logger.info("Getting Validation message in List Branch page");
		return this.waitForElementToVisible(By.id("validationMessages"))
				.getText();
	}
}
