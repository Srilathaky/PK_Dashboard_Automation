package com.milvik.mip.pageobjects;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import com.milvik.mip.pageutil.MIP_BasePage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_SearchUserPage extends MIP_BasePage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_SearchUserPage");
	}

	public MIP_SearchUserPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	@FindBy(id = "fname")
	public WebElement fnmae;
	@FindBy(id = "sname")
	public WebElement snmae;
	@FindBy(id = "msisdn")
	public WebElement msisdn;
	@FindBy(id = "role")
	public WebElement role;
	@FindBy(id = "searchBtn")
	public WebElement searchbtn;
	@FindBy(id = "clearBtn")
	public WebElement clearBtn;

	public MIP_SearchUserPage enterSearchCriteria(String firstName,
			String surNam, String mobileNumber, String role) {
		if (!firstName.equals(""))
			fnmae.sendKeys(firstName);
		if (!surNam.equals(""))
			snmae.sendKeys(surNam);
		if (!msisdn.equals(""))
			msisdn.sendKeys(mobileNumber);
		if (!role.equals("")) {
			this.selectDropDownbyText(this.role, role.trim());
		}
		return this;
	}

	public MIP_SearchUserPage clickOnSearch() {
		searchbtn.click();
		return this;
	}

	public MIP_SearchUserPage clickOnClear() {
		clearBtn.click();
		return this;
	}

	public List<WebElement> getUserName() {
		this.waitForElementToVisible(By.id("userList"));
		return driver.findElements(By
				.xpath("//table[@id='userList']/tbody/tr/td["
						+ this.getIndex("User Name") + "]"));
	}

	public List<WebElement> getUserNameUsingToolTip() {
		this.waitForElementToVisible(By.id("userList"));
		return driver.findElements(By
				.xpath("//table[@id='userList']/tbody/tr/td["
						+ this.getIndex("User Name") + "]"));
	}

	public WebElement getMsisdn() {
		this.waitForElementToVisible(By.id("userList"));
		return driver.findElement(By
				.xpath("//table[@id='userList']/tbody/tr/td["
						+ this.getIndex("MSISDN") + "]"));
	}

	public List<WebElement> getRole() {
		this.waitForElementToVisible(By.id("userList"));
		return driver.findElements(By
				.xpath("//table[@id='userList']/tbody/tr/td["
						+ this.getIndex("Role") + "]"));
	}

	public List<WebElement> getUSerId() {
		this.waitForElementToVisible(By.id("userList"));
		return driver.findElements(By
				.xpath("//table[@id='userList']/tbody/tr/td["
						+ this.getIndex("User ID") + "]"));
	}

	public int getIndex(String header) {
		int index = -1;
		this.waitForElementToVisible(By.id("userList"));
		List<WebElement> ele = driver.findElements(By
				.xpath("//table[@id='userList']/thead/tr/th"));
		for (int i = 0; i < ele.size(); i++) {
			if (ele.get(i).getText().equalsIgnoreCase(header.trim())) {
				index = i + 1;
			}
		}
		return index;
	}

	public String getRoleDropDownValue() {
		Select s = new Select(role);
		return s.getFirstSelectedOption().getText();
	}

	public String getResultMsg() {
		return this.waitForElementToVisible(By.id("div_searchResults"))
				.getText();
	}

	public String getValidationMsg() {
		logger.info("Getting Validation message in Search  User   page");
		return this.waitForElementToPresent(By.id("validationMessages"))
				.getText();
	}
}
