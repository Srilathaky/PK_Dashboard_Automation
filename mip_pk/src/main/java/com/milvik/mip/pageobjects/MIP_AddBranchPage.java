package com.milvik.mip.pageobjects;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.milvik.mip.pageutil.MIP_BasePage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_AddBranchPage extends MIP_BasePage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_AddBranchPage");
	}

	public MIP_AddBranchPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	/**
	 * This method will validate the Add Branch page objects
	 * 
	 * @return
	 */
	public boolean validateAddBranchObjecs() {
		logger.info("validating the Add Branch page");
		boolean value1 = this
				.waitForElementToVisible(By.id("label_branchName")).getText()
				.trim().equalsIgnoreCase("Branch Name : *");
		boolean value2 = this.waitForElementToVisible(By.id("branchName"))
				.getAttribute("type").equalsIgnoreCase("text");

		boolean value3 = driver.findElement(By.id("label_branchStreet"))
				.getText().trim().equalsIgnoreCase("Street : *");
		boolean value4 = driver.findElement(By.id("branchStreet"))
				.getAttribute("type").equalsIgnoreCase("text");

		boolean value5 = driver.findElement(By.id("label_branchRegion"))
				.getText().trim().equalsIgnoreCase("Region : *");
		boolean value6 = this.waitForElementToVisible(By.id("branchRegion"))
				.getAttribute("type").equalsIgnoreCase("text");

		boolean value7 = this
				.waitForElementToVisible(By.id("label_branchCity")).getText()
				.trim().equalsIgnoreCase("City : *");
		boolean value8 = this.waitForElementToVisible(By.id("branchCity"))
				.getAttribute("type").equalsIgnoreCase("text");

		boolean value9 = this.waitForElementToVisible(By.id("saveBtn"))
				.getText().trim().equalsIgnoreCase("Save");
		boolean value10 = this.waitForElementToVisible(By.id("clearBtn"))
				.getText().trim().equalsIgnoreCase("Clear");
		if (value1 && value2 && value3 && value4 && value5 && value6 && value7
				&& value8 && value9 && value10) {
			return true;
		}
		return false;
	}

	/**
	 * This method will click on the "Save" button of Add Branch page
	 * 
	 * @return
	 */
	public MIP_AddBranchPage clickOnSave() {
		logger.info("Clicking on Save button in Add Branch page");
		this.clickOnElement(By.id("saveBtn"));
		return this;
	}

	/**
	 * This method will click on the "Clear" button of Add Branch page
	 * 
	 * @return
	 */
	public MIP_AddBranchPage clickOnClear() {
		logger.info("Clicking on Save button in Add Branch page");
		this.clickOnElement(By.id("clearBtn"));
		return this;
	}

	/**
	 * This method will return the validation message
	 * 
	 * @return
	 */
	public String getValidationMsg() {
		logger.info("Getting Validation message in Add Branch page");
		List<WebElement> msg = new ArrayList<WebElement>();
		String str = "";
		this.waitForElementToVisible(By
				.xpath("//div[@id='validationMessages']//li"));
		msg = driver.findElements(By
				.xpath("//div[@id='validationMessages']//li"));
		if (msg.size() > 1) {
			for (WebElement ele : msg) {
				str = str + ele.getText();
			}
		} else {
			str = this.waitForElementToVisible(
					By.xpath("//div[@id='validationMessages']//li")).getText();
		}

		return str;
	}

	/**
	 * This method will enter the branch information in Add Branch page
	 * 
	 * @param name
	 * @param street
	 * @param region
	 * @param city
	 * @return
	 */
	public MIP_AddBranchPage enterBranchInfo(String name, String street,
			String region, String city) {
		logger.info("Entering branch Information in Add Branch page");
		this.enterText(this.waitForElementToVisible(By.id("branchName")), name);
		this.enterText(this.waitForElementToVisible(By.id("branchStreet")),
				street);
		this.enterText(this.waitForElementToVisible(By.id("branchRegion")),
				region);
		this.enterText(this.waitForElementToVisible(By.id("branchCity")), city);

		return this;
	}

	/**
	 * This method will get the branch information
	 * 
	 * @param value
	 * @return
	 */
	public String getBranchInfo(String value) {
		logger.info("Getting Branch Information");
		if (value.equalsIgnoreCase("name")) {

			return this.waitForElementToPresent(By.id("branchName")).getText();
		} else if (value.equalsIgnoreCase("street")) {
			return this.waitForElementToPresent(By.id("branchStreet"))
					.getText();
		} else if (value.equalsIgnoreCase("region")) {
			return this.waitForElementToPresent(By.id("branchRegion"))
					.getText();
		} else if (value.equalsIgnoreCase("city")) {
			return this.waitForElementToPresent(By.id("branchCity")).getText();
		} else
			return null;
	}

	/**
	 * This method will confirm the Add Branch confirmation pop-up
	 * 
	 * @param option
	 * @return
	 */
	public MIP_AddBranchPage confirmAddBranch(String option) {
		logger.info("Confirming add Branch pop-up");
		this.confirmPopUp(option);
		return this;
	}

	/**
	 * This message will return the success message
	 * 
	 * @return
	 */
	public String getmessage() {
		logger.info("Getting success message of add branch");
		return this.waitForElementToPresent(By.className("error-div-body"))
				.getText();
	}
}
