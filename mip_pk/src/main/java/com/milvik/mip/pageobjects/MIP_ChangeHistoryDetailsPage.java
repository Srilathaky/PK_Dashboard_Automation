package com.milvik.mip.pageobjects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.milvik.mip.pageutil.MIP_CustomerManagementPage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_ChangeHistoryDetailsPage extends MIP_CustomerManagementPage {
	private static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_ChangeHistoryDetailsPage");
	}

	public MIP_ChangeHistoryDetailsPage(WebDriver driver) {
		super(driver);
	}

	public Map<String, String> getCustomerInfo() {
		Map<String, String> cust_details = new HashMap<String, String>();
		logger.info("Getting the customer information from Register Customer Page");
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
		cust_details.put("cust_gender", this.getSelectedOption(this
				.waitForElementToVisible(By
						.xpath("//input[@id='gender'][@disabled]"))));
		return cust_details;
	}

	public Map<String, String> getBeneficiaryInfo() {
		Map<String, String> ben_details = new HashMap<String, String>();
		logger.info("Getting the Beneficiary information from Change Customer Details Page");
		ben_details.put("ben_relationship", this.getSelectedOption(this
				.waitForElementToVisible(By
						.xpath("//input[@id='insRelation'][disabled]"))));
		ben_details
				.put("ben_fname",
						this.waitForElementToVisible(
								By.xpath("//input[@id='insRelName'][@readonly='readonly']"))
								.getAttribute("value"));
		ben_details
				.put("ben_age",
						this.waitForElementToVisible(
								By.xpath("//input[@id='insRelAge'][@readonly='readonly']"))
								.getAttribute("value"));
		if (new Integer(ben_details.get("ben_age")) <= BENEFICIARYAGELESSFORLEAGALGUARDIAN) {
			ben_details
					.put("lg_name",
							this.waitForElementToVisible(
									By.xpath("//input[@id='lgName'][@readonly='readonly']"))
									.getAttribute("value"));
			ben_details
					.put("lg_msisdn",
							this.waitForElementToVisible(
									By.xpath("//input[@id='lgMsisdn'][@readonly='readonly']"))
									.getAttribute("value"));
		}
		ben_details
				.put("ben_sname",
						this.waitForElementToVisible(
								By.xpath("//input[@id='insRelSurName'][@readonly='readonly']"))
								.getAttribute("value"));
		return ben_details;
	}

	public boolean validateChangesPerformedTableHeading() {
		String xpath = "//table[@id='summaryDetailsChangesList']/thead/tr//th[normalize-space(text())='";
		logger.info("validating changes performed Table Heading in change customer detail history page");
		this.waitForElementToVisible(By
				.xpath("//*[contains(text(),'Changes Performed')]"));
		this.waitForElementToVisible(By.id("summaryDetailsChangesList"));
		if (this.waitForElementToVisible(By.xpath(xpath + "Date']"))
				.isDisplayed()
				&& this.waitForElementToVisible(By.xpath(xpath + "MSISDN']"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(xpath + "Modified By']")).isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(xpath + "New Customer Name']")).isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(xpath + "Old Customer Name']")).isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(xpath + "New Customer Surname']"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(xpath + "Old Customer Surname']"))
						.isDisplayed()
				&& this.waitForElementToVisible(By.xpath(xpath + "New Age']"))
						.isDisplayed()
				&& this.waitForElementToVisible(By.xpath(xpath + "Old Age']"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(xpath + "New Gender']")).isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(xpath + "Old Gender']")).isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(xpath + "New Beneficiary Name']"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(xpath + "Old Beneficiary Name']"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(xpath + "New Beneficiary Surname']"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(xpath + "Old Beneficiary Surname']"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(xpath + "New Beneficiary Age']"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(xpath + "Old Beneficiary Age']"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(xpath + "New Beneficiary Relationship']"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(xpath + "Old Beneficiary Relationship']"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(xpath + "New Legal Guardian Name']"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(xpath + "Old Legal Guardian Name']"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(xpath + "New Legal Guardian MSISDN']"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(xpath + "Old Legal Guardian MSISDN']"))
						.isDisplayed()) {
			return true;
		}
		return false;
	}

	public int getTableIndex(String heading) {
		logger.info("Getting Table heading "
				+ heading
				+ " validating changes performed in change customer detail history page");
		this.waitForElementToVisible(By.id("summaryDetailsChangesList"));
		List<WebElement> index = super.driver
				.findElements(By
						.xpath("//table[@id='summaryDetailsChangesList']/thead/tr//th['"
								+ heading + "']"));
		for (int i = 0; i < index.size(); i++) {
			if (index.get(i).getText().equalsIgnoreCase(heading)) {
				return i + 1;
			}
		}
		return -1;
	}

	public Map<String, String> validateChangesPerformed() {
		Map<String, String> change_details = new HashMap<String, String>();
		logger.info("validating changes performed in change customer detail history page");
		this.waitForElementToVisible(By
				.xpath("//*[contains(text(),'Changes Performed')]"));
		change_details.put(
				"Date",
				this.waitForElementToVisible(
						By.xpath("//table/tbody/tr//td["
								+ getTableIndex("Date") + "]")).getText());
		change_details.put(
				"Modified By",
				this.waitForElementToVisible(
						By.xpath("//table/tbody/tr//td["
								+ getTableIndex("Modified By") + "]"))
						.getText());
		change_details.put(
				"New Customer Name",
				this.waitForElementToVisible(
						By.xpath("//table/tbody/tr//td["
								+ getTableIndex("New Customer Name") + "]"))
						.getText());
		change_details.put(
				"Old Customer Name",
				this.waitForElementToVisible(
						By.xpath("//table/tbody/tr//td["
								+ getTableIndex("Old Customer Name") + "]"))
						.getText());
		change_details.put(
				"New Customer Surname",
				this.waitForElementToVisible(
						By.xpath("//table/tbody/tr//td["
								+ getTableIndex("New Customer Surname") + "]"))
						.getText());
		change_details.put(
				"Old Customer Surname",
				this.waitForElementToVisible(
						By.xpath("//table/tbody/tr//td["
								+ getTableIndex("Old Customer Surname") + "]"))
						.getText());
		change_details.put(
				"New Age",
				this.waitForElementToVisible(
						By.xpath("//table/tbody/tr//td["
								+ getTableIndex("New Age") + "]")).getText());
		change_details.put(
				"Old Age",
				this.waitForElementToVisible(
						By.xpath("//table/tbody/tr//td["
								+ getTableIndex("Old Age") + "]")).getText());
		change_details
				.put("New Gender",
						this.waitForElementToVisible(
								By.xpath("//table/tbody/tr//td["
										+ getTableIndex("New Gender") + "]"))
								.getText());
		change_details
				.put("Old Gender",
						this.waitForElementToVisible(
								By.xpath("//table/tbody/tr//td["
										+ getTableIndex("Old Gender") + "]"))
								.getText());
		change_details.put(
				"New Beneficiary Name",
				this.waitForElementToVisible(
						By.xpath("//table/tbody/tr//td["
								+ getTableIndex("New Beneficiary Name") + "]"))
						.getText());
		change_details.put(
				"Old Beneficiary Name",
				this.waitForElementToVisible(
						By.xpath("//table/tbody/tr//td["
								+ getTableIndex("Old Beneficiary Name") + "]"))
						.getText());
		change_details.put(
				"New Beneficiary Surname",
				this.waitForElementToVisible(
						By.xpath("//table/tbody/tr//td["
								+ getTableIndex("New Beneficiary Surname")
								+ "]")).getText());

		change_details.put(
				"Old Beneficiary Surname",
				this.waitForElementToVisible(
						By.xpath("//table/tbody/tr//td["
								+ getTableIndex("Old Beneficiary Surname")
								+ "]")).getText());
		change_details.put(
				"New Beneficiary Age",
				this.waitForElementToVisible(
						By.xpath("//table/tbody/tr//td["
								+ getTableIndex("New Beneficiary Age") + "]"))
						.getText());
		change_details.put(
				"Old Beneficiary Age",
				this.waitForElementToVisible(
						By.xpath("//table/tbody/tr//td["
								+ getTableIndex("Old Beneficiary Age") + "]"))
						.getText());
		change_details.put(
				"New Beneficiary Relationship",
				this.waitForElementToVisible(
						By.xpath("//table/tbody/tr//td["
								+ getTableIndex("New Beneficiary Relationship")
								+ "]")).getText());
		change_details.put(
				"Old Beneficiary Relationship",
				this.waitForElementToVisible(
						By.xpath("//table/tbody/tr//td["
								+ getTableIndex("Old Beneficiary Relationship")
								+ "]")).getText());
		change_details.put(
				"New Legal Guardian Name",
				this.waitForElementToVisible(
						By.xpath("//table/tbody/tr//td["
								+ getTableIndex("New Legal Guardian Name")
								+ "]")).getText());
		change_details.put(
				"Old Legal Guardian Name",
				this.waitForElementToVisible(
						By.xpath("//table/tbody/tr//td["
								+ getTableIndex("Old Legal Guardian Name")
								+ "]")).getText());
		change_details.put(
				"New Legal Guardian MSISDN",
				this.waitForElementToVisible(
						By.xpath("//table/tbody/tr//td["
								+ getTableIndex("New Legal Guardian MSISDN")
								+ "]")).getText());
		change_details.put(
				"Old Legal Guardian MSISDN",
				this.waitForElementToVisible(
						By.xpath("//table/tbody/tr//td["
								+ getTableIndex("Old Legal Guardian MSISDN")
								+ "]")).getText());

		return change_details;
	}
}
