package com.milvik.mip.pageobjects;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.milvik.mip.dbqueries.MIP_UserManagement_Queries;
import com.milvik.mip.pageutil.MIP_BasePage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_ListUserPage extends MIP_BasePage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_ListUserPage");
	}

	public MIP_ListUserPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public boolean validateListUserHeader() {
		this.waitForElementToVisible(By.id("userList"));
		boolean value1 = this
				.waitForElementToVisible(
						By.xpath("//table[@id='userList']/thead/tr/th[contains(text(),'User ID')]"))
				.isDisplayed();
		boolean value2 = this
				.waitForElementToVisible(
						By.xpath("//table[@id='userList']/thead/tr/th[contains(text(),'User Name')]"))
				.isDisplayed();
		boolean value3 = this
				.waitForElementToVisible(
						By.xpath("//table[@id='userList']/thead/tr/th[contains(text(),'MSISDN')]"))
				.isDisplayed();
		boolean value4 = this
				.waitForElementToVisible(
						By.xpath("//table[@id='userList']/thead/tr/th[contains(text(),'Age')]"))
				.isDisplayed();
		boolean value5 = this
				.waitForElementToVisible(
						By.xpath("//table[@id='userList']/thead/tr/th[contains(text(),'Role')]"))
				.isDisplayed();
		boolean value6 = this
				.waitForElementToVisible(
						By.xpath("//table[@id='userList']/thead/tr/th[contains(text(),'Branch')]"))
				.isDisplayed();
		boolean value7 = this
				.waitForElementToVisible(
						By.xpath("//table[@id='userList']/thead/tr/th[contains(text(),'Registered Date')]"))
				.isDisplayed();
		boolean value8 = this
				.waitForElementToVisible(
						By.xpath("//table[@id='userList']/thead/tr/th[contains(text(),'Current Month Leave')]"))
				.isDisplayed();
		boolean value9 = this
				.waitForElementToVisible(
						By.xpath("//table[@id='userList']/thead/tr/th[contains(text(),'Current Year Leave')]"))
				.isDisplayed();
		boolean value10 = this.waitForElementToVisible(
				By.id("exportToExcelBtn")).isDisplayed();
		if (value1 && value2 && value3 && value4 && value5 && value6 && value7
				&& value8 && value9 && value10) {
			return true;
		}
		return false;
	}

	public int getUserDataIndex(String header) {
		int index = -1;
		this.waitForElementToVisible(By.id("userList"));
		List<WebElement> tableHearder = driver.findElements(By
				.xpath("//table[@id='userList']/thead/tr/th"));
		for (int i = 0; i < tableHearder.size(); i++) {
			if (tableHearder.get(i).getText().equalsIgnoreCase(header)) {
				index = i + 1;
				break;

			}
		}

		return index;

	}

	public boolean validateUserData() {
		int index = this.getUserDataIndex("Mobile Number");
		if (!(index < 0)) {
			this.waitForElementToVisible(By.id("userList"));
			String mobileNumber = driver.findElement(
					By.xpath("//table[@id='userList']/tbody/tr/td[" + index
							+ "]")).getText();

			Map<String, String> userDB = MIP_UserManagement_Queries
					.getUserInfo(mobileNumber);
			System.out.println();
			boolean value1 = driver
					.findElement(
							By.xpath("//table[@id='userList']/tbody/tr/td["
									+ this.getUserDataIndex("User ID") + "]"))
					.getText().equalsIgnoreCase(userDB.get("user_uid"));
			boolean value2 = driver
					.findElement(
							By.xpath("//table[@id='userList']/tbody/tr/td["
									+ this.getUserDataIndex("User Name") + "]"))
					.getText()
					.equalsIgnoreCase(
							userDB.get("fname") + " " + userDB.get("sname"));
			boolean value3 = driver
					.findElement(
							By.xpath("//table[@id='userList']/tbody/tr/td["
									+ this.getUserDataIndex("Role") + "]"))
					.getText().equalsIgnoreCase(userDB.get("role_name"));
			boolean value4 = driver
					.findElement(
							By.xpath("//table[@id='userList']/tbody/tr/td["
									+ this.getUserDataIndex("Branch") + "]"))
					.getText().equalsIgnoreCase(userDB.get("branch_name"));
			if (value1 && value2 && value3 && value4) {
				return true;
			}

		} else {
			logger.info("Error While Getting User Data in List User Page");
		}
		return false;
	}

	public String getListUserresultText() {
		return this.waitForElementToVisible(By.id("div_searchResults"))
				.getText();
	}

	public int getNumberofUser() {
		logger.info("Getting Branch Count");
		int count = 0;
		this.waitForElementToVisible(By.id("userList"));
		String[] number = this.getListUserresultText().split("\\s");
		if (!number[0].equalsIgnoreCase("one")) {
			count = Integer.parseInt(number[0]);
		} else if (number[0].equalsIgnoreCase("one"))
			count = 1;
		return count;
	}

	public void clickOnUser(String mobileNumber) {
		int j = 1;
		this.waitForElementToVisible(By.xpath("//table[@id='userList']/tbody"));
		WebDriverWait w = new WebDriverWait(driver, 2);
		for (int i = 0; i < j; i++) {
			try {
				w.until(ExpectedConditions.visibilityOfElementLocated(By
						.xpath("//table[@id='userList']/tbody//tr//td[contains(text(),'"
								+ mobileNumber + "')]//preceding-sibling::td/a")))
						.click();
				break;
			} catch (Exception e) {
				driver.findElement(By.linkText("Next")).click();
				j++;
			}
		}

		/*
		 * int flag = 0; for (int i = 0; i < this.getNumberofUser() / 10; i++) {
		 * this.waitForElementToVisible(By
		 * .xpath("//table[@id='userList']/tbody")); List<WebElement> mobilenum
		 * = driver.findElements(By
		 * .xpath("//table[@id='userList']/tbody/tr/td[3]")); for (int j = 0; j
		 * < mobilenum.size(); j++) {
		 * 
		 * if (mobilenum.get(j).getText().equals(mobileNumber)) {
		 * this.waitForElementToVisible(
		 * (By.xpath("//table[@id='userList']/tbody/tr/td[contains(text(),'" +
		 * mobileNumber + "')]//preceding-sibling::td/a"))).click(); flag = 1;
		 * break; } } if (flag == 1) { break; } if (flag == 0) { try {
		 * driver.findElement(By.linkText("Next")).click(); } catch (Exception
		 * e) { logger.info("User with mobilenumber " + mobileNumber +
		 * " is not there in the list"); } } }
		 */

	}

	public String getUserId() {
		return this.waitForElementToVisible(
				By.xpath("//div[@id='label_user_uid']/following-sibling::div"))
				.getText();
	}

	public String getFirstName() {
		return this
				.waitForElementToVisible(
						By.xpath("//div[@id='label_fname']/following-sibling::div/div"))
				.getText();
	}

	public String getLastName() {
		return this
				.waitForElementToVisible(
						By.xpath("//div[@id='label_sname']/following-sibling::div/div"))
				.getText();
	}

	public String getEmailId() {
		return this
				.waitForElementToVisible(
						By.xpath("//div[@id='label_email']/following-sibling::div/div"))
				.getText();
	}

	public String getdob() {
		return this.waitForElementToVisible(
				By.xpath("//div[@id='label_dob']/following-sibling::div"))
				.getText();
	}

	public String getGender() {
		return this
				.waitForElementToVisible(
						By.xpath("//div[@id='label_gender']/following-sibling::div/div"))
				.getText();
	}

	public String getRole() {
		return this.waitForElementToVisible(
				By.xpath("//div[@id='label_role']/following-sibling::div/div"))
				.getText();
	}

	public String getBranch() {
		return this
				.waitForElementToVisible(
						By.xpath("//div[@id='label_branch']/following-sibling::div/div"))
				.getText();
	}

	public void clickOnBack() {
		this.clickOnElement(By.id("backBtn"));
	}

}
