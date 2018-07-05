package com.milvik.mip.pageobjects;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.milvik.mip.pageutil.MIP_CustomerManagementPage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_QAConfirmationPage extends MIP_CustomerManagementPage {
	private static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_QAConfirmationPage");
	}

	public MIP_QAConfirmationPage(WebDriver driver) {
		super(driver);
	}

	public Map<String, String> getCustomerInormation() {
		logger.info("Getting customer Information from QA Confirmation Page");
		Map<String, String> cust_details = new HashMap<String, String>();
		cust_details
				.put("cust_fname",
						this.waitForElementToVisible(
								By.xpath("//input[@id='custName'][@readonly='readonly']"))
								.getAttribute("value"));
		cust_details.put(
				"cust_sname",
				this.waitForElementToVisible(
						By.xpath("//input[@id='sname'][@readonly='readonly']"))
						.getAttribute("value"));
		cust_details.put(
				"cust_age",
				this.waitForElementToVisible(
						By.xpath("//input[@id='age'][@readonly='readonly']"))
						.getAttribute("value"));
		cust_details.put(
				"cust_dob",
				this.waitForElementToVisible(
						By.xpath("//input[@id='dob'][@readonly='readonly']"))
						.getAttribute("value"));
		cust_details.put(
				"cust_nic",
				this.waitForElementToVisible(
						By.xpath("//input[@id='cnic'][@readonly='readonly']"))
						.getAttribute("value"));
		cust_details
				.put("ben_relationship",
						this.waitForElementToVisible(
								By.xpath("//input[@id='insRelation'][@readonly='readonly']"))
								.getAttribute("value"));
		cust_details
				.put("ben_fname",
						this.waitForElementToVisible(
								By.xpath("//input[@id='insRelName'][@readonly='readonly']"))
								.getAttribute("value"));
		cust_details
				.put("ben_sname",
						this.waitForElementToVisible(
								By.xpath("//input[@id='insRelSurName'][@readonly='readonly']"))
								.getAttribute("value"));
		cust_details
				.put("ben_age",
						this.waitForElementToVisible(
								By.xpath("//input[@id='insRelAge'][@readonly='readonly']"))
								.getAttribute("value"));
		cust_details
				.put("cust_coverlevel",
						this.waitForElementToVisible(
								By.xpath("//input[@id='coverLevel'][@readonly='readonly']"))
								.getAttribute("value"));
		cust_details
				.put("reg_by",
						this.waitForElementToVisible(
								By.xpath("//input[@id='createdBy'][@readonly='readonly']"))
								.getAttribute("value"));
		cust_details.put(
				"agent_id",
				this.waitForElementToVisible(
						By.xpath("//input[@id='regBy'][@readonly='readonly']"))
						.getAttribute("value"));
		cust_details
				.put("reg_date",
						this.waitForElementToVisible(
								By.xpath("//input[@id='regDate'][@readonly='readonly']"))
								.getAttribute("value"));
		cust_details
				.put("conf_status",
						this.waitForElementToVisible(
								By.xpath("//div[contains(text(),'Confirmation Status')]//following-sibling::div"))
								.getText());
		cust_details
				.put("coaching_program",
						this.waitForElementToVisible(
								By.xpath("//input[@id='healthTipSMSFrequency'][@readonly='readonly']"))
								.getAttribute("value"));
		cust_details
				.put("date_program",
						this.waitForElementToVisible(
								By.xpath("//input[@id='healthTipDate'][@readonly='readonly']"))
								.getAttribute("value"));
		cust_details
				.put("coaching_program_status",
						this.waitForElementToVisible(
								By.xpath("//input[@id='healthTipStatus'][@readonly='readonly']"))
								.getAttribute("value"));
		this.waitForElementToVisible(By.id("backBtn")).isDisplayed();
		this.waitForElementToVisible(By.id("confirmBtn")).isDisplayed();
		return cust_details;

	}

	public MIP_QAConfirmationPage clickOnConfirmButton() {
		logger.info("Clicking on Confirm Button");
		this.waitForElementTobeClickable(By.id("confirmBtn")).click();
		return this;
	}

}
