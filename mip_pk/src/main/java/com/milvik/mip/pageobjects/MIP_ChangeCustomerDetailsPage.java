package com.milvik.mip.pageobjects;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.milvik.mip.pageutil.MIP_CustomerManagementPage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_ChangeCustomerDetailsPage extends MIP_CustomerManagementPage {
	private static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_DeRegisterCustomerPage");
	}

	public MIP_ChangeCustomerDetailsPage(WebDriver driver) {
		super(driver);
	}

	public MIP_ChangeCustomerDetailsPage changeCustomerDetails(String fname,
			String sname, String age, String gender) {
		logger.info("changing customer details in changed customer details");
		WebElement ele = null;
		if (!fname.equals("")) {
			ele = this.waitForElementToVisible(By.id("custName"));
			ele.clear();
			ele.sendKeys(fname);
		}
		if (!sname.equals("")) {
			ele = this.waitForElementToVisible(By.id("sname"));
			ele.clear();
			ele.sendKeys(sname);
		}
		if (!age.equals("")) {
			ele = this.waitForElementToVisible(By.id("age"));
			ele.clear();
			ele.sendKeys(age);
		}
		if (!gender.equals(""))
			this.selectDropDownbyText(
					this.waitForElementToVisible(By.id("gender")),
					gender.trim());
		logger.info("changed customer details in changed customer details");
		return this;
	}

	public MIP_ChangeCustomerDetailsPage changeBeneficiaryDetails(String fname,
			String sname, String age, String relationship,
			String guardian_name, String guardian_phnum) {
		WebElement ele = null;
		logger.info("changing customer details in changed customer details");
		if (!fname.equals("")) {
			ele = this.waitForElementToVisible(By.id("insRelName"));
			ele.clear();
			ele.sendKeys(fname);
		}
		if (!sname.equals("")) {
			ele = this.waitForElementToVisible(By.id("insRelSurName"));
			ele.clear();
			ele.sendKeys(sname);
		}
		if (!age.equals("")) {
			ele = this.waitForElementToVisible(By.id("insRelAge"));
			ele.clear();
			ele.sendKeys(age);
		}
		if (new Integer(age) <= BENEFICIARYAGELESSFORLEAGALGUARDIAN) {
			this.waitForElementToVisible(By.id("insRelSurName")).click();
			if (!guardian_name.equals("")) {
				ele = this.waitForElementToVisible(By.id("lgName"));
				ele.clear();
				ele.sendKeys(guardian_name);
			}
			if (!guardian_phnum.equals("")) {
				ele = this.waitForElementToVisible(By.id("lgMsisdn"));
				ele.clear();
				ele.sendKeys(guardian_phnum);
			}
		}
		if (!relationship.equals(""))
			this.selectDropDownbyText(
					this.waitForElementToVisible(By.id("insRelation")),
					relationship.trim());
		logger.info("changed customer details in changed customer details");
		return this;
	}

	public Map<String, String> getCustomerInfo() {
		Map<String, String> cust_details = new HashMap<String, String>();
		logger.info("Getting the customer information from change Customer details Page");
		cust_details.put(
				"cust_fname",
				this.waitForElementToVisible(
						By.xpath("//input[@id='custName']")).getAttribute(
						"value"));
		cust_details.put("cust_sname",
				this.waitForElementToVisible(By.xpath("//input[@id='sname']"))
						.getAttribute("value"));
		cust_details.put("cust_age",
				this.waitForElementToVisible(By.xpath("//input[@id='age']"))
						.getAttribute("value"));
		cust_details.put("cust_gender", this.getSelectedOption(this
				.waitForElementToVisible(By.id("gender"))));
		return cust_details;
	}

	public Map<String, String> getBeneficiaryInfo() {
		Map<String, String> ben_details = new HashMap<String, String>();
		logger.info("Getting the Beneficiary information from Change Customer Details Page");
		ben_details.put("ben_relationship", this.getSelectedOption(this
				.waitForElementToVisible(By.id("insRelation"))));
		ben_details.put(
				"ben_fname",
				this.waitForElementToVisible(
						By.xpath("//input[@id='insRelName']")).getAttribute(
						"value"));
		ben_details.put(
				"ben_age",
				this.waitForElementToVisible(
						By.xpath("//input[@id='insRelAge']")).getAttribute(
						"value"));
		if (new Integer(ben_details.get("ben_age")) <= BENEFICIARYAGELESSFORLEAGALGUARDIAN) {
			ben_details.put(
					"lg_name",
					this.waitForElementToVisible(
							By.xpath("//input[@id='lgName']")).getAttribute(
							"value"));
			ben_details.put(
					"lg_msisdn",
					this.waitForElementToVisible(
							By.xpath("//input[@id='lgMsisdn']")).getAttribute(
							"value"));
		}
		ben_details.put(
				"ben_sname",
				this.waitForElementToVisible(
						By.xpath("//input[@id='insRelSurName']")).getAttribute(
						"value"));
		return ben_details;
	}

}
