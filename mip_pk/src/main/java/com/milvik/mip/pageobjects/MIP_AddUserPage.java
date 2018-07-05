package com.milvik.mip.pageobjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import com.milvik.mip.constants.MIP_Constants;
import com.milvik.mip.pageutil.MIP_BasePage;
import com.milvik.mip.utility.MIP_DateFunctionality;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_AddUserPage extends MIP_BasePage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_AddUserPage");
	}

	public MIP_AddUserPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	@FindBy(id = "fname")
	public WebElement fname;
	@FindBy(id = "sname")
	public WebElement sname;
	@FindBy(id = "msisdn")
	public WebElement mobilenumber;
	@FindBy(id = "email")
	public WebElement emailID;
	@FindBy(className = "calendar-icon")
	public WebElement calenderIcon;
	@FindBy(xpath = "//input[@id='gender'][@value='Male']")
	public WebElement gender_male;
	@FindBy(xpath = "//input[@id='gender'][@value='Female']")
	public WebElement gender_female;
	@FindBy(id = "role")
	public WebElement role;
	@FindBy(id = "branch")
	public WebElement branch;
	@FindBy(xpath = "//input[@id='isSupervisor'][@value='0']")
	public WebElement teamlead_no;
	@FindBy(xpath = "//input[@id='isSupervisor'][@value='1']")
	public WebElement teamlead_yes;
	@FindBy(id = "saveBtn")
	public WebElement save;
	@FindBy(id = "clearBtn")
	public WebElement clear;
	@FindBy(id = "dob")
	public WebElement dob;
	@FindBy(id = "age")
	public WebElement age;

	public MIP_AddUserPage enterUserInfo(String fname, String sname,
			String mobilenum, String email, String dob, String gender,
			String role, String branch, String is_teamlead,
			String supervisor_name, String is_alert_enabled) {
		logger.info("Entering user information");
		for (int i = 0; i < 5; i++) {
			try {
				this.waitForElementToVisible(By.id("sname"));
				break;
			} catch (StaleElementReferenceException e) {
				logger.info("finding sname field after exception "
						+ e.getMessage());
			}
		}
		if (!fname.equals(""))
			this.fname.sendKeys(fname);
		if (!sname.equals(""))
			this.sname.sendKeys(sname);
		if (!mobilenumber.equals(""))
			this.mobilenumber.sendKeys(mobilenum);
		if (!emailID.equals(""))
			this.emailID.sendKeys(email);
		if (!gender.equals("")) {
			if (gender.equals("Male")) {
				gender_male.click();
			} else if (gender.equalsIgnoreCase("Female")) {
				gender_female.click();
			}
		}
		if (!role.equals(""))
			this.selectDropDownbyText(this.role, role);
		if (!branch.equals(""))
			this.selectDropDownbyText(this.branch, branch);
		if (!dob.equals("")) {
			logger.info("Selecting DOB");
			String[] date = MIP_DateFunctionality.getDate(dob,
					MIP_Constants.DOB_FORMAT);
			this.waitForElementToVisible(
					By.xpath("//div[@class='calendar-icon']")).click();

			Actions a = new Actions(driver);
			a.moveToElement(
					this.waitForElementToVisible(By
							.xpath("//table[@class='DynarchCalendar-titleCont']/tbody/tr/td/div/div")))
					.build().perform();
			this.waitForElementToVisible(
					By.xpath("//table[@class='DynarchCalendar-titleCont']/tbody/tr/td/div/div"))
					.click();
			WebElement ele = this.waitForElementToVisible(By
					.className("DynarchCalendar-menu-year"));
			ele.clear();
			ele.sendKeys(date[2]);
			this.waitForElementToVisible(
					By.xpath("//table[@class='DynarchCalendar-menu-mtable']/tbody//tr//td/div[contains(text(),'"
							+ date[1] + "')]")).click();
			if ((date[0].charAt(0) + "").equals("0")) {
				date[0] = date[0].charAt(1) + "";
			}

			this.waitForElementToVisible(
					By.xpath("//div[@class='DynarchCalendar-body']/table[@class='DynarchCalendar-bodyTable']/tbody//tr//td/div[@class='DynarchCalendar-day' or @class='DynarchCalendar-day DynarchCalendar-weekend'][contains(text(),'"
							+ date[0] + "')]")).click();

		}
		if (is_teamlead.equalsIgnoreCase("yes")) {
			this.waitForElementToVisible(
					By.xpath("//input[@id='isSupervisor'][@value='1']"))
					.click();
			if (is_alert_enabled.equalsIgnoreCase("yes"))
				this.waitForElementToVisible(By.id("isAlertEnabled")).click();
		} else if (is_teamlead.equalsIgnoreCase("no")) {
			this.waitForElementToVisible(
					By.xpath("//input[@id='isSupervisor'][@value='0']"))
					.click();
			WebElement ele = this
					.waitForElementToVisible(By.id("supervisorId"));
			this.selectDropDownbyText(ele, supervisor_name.trim());
		}
		logger.info("User information entered successfully");
		return this;
	}

	public void clickOnSave() {
		logger.info("Clicking on Save");
		save.click();
	}

	public MIP_AddUserPage confirmUser(String option) {
		logger.info("Confirming Add User pop-up");
		this.confirmPopUp(option);
		return this;
	}

	public void clickOnClear() {
		logger.info("Clicking on Clear Button");
		clear.click();
	}

	public String getmessage() {
		logger.info("Getting message in Add User module");
		return this.waitForElementToVisible(By.className("error-div-body"))
				.getText();
	}

	public String getSelectedOptions(WebElement ele) {
		Select s = new Select(ele);
		return s.getFirstSelectedOption().getText();
	}

	public String getValidationMsg() {
		logger.info("Getting Validation message in Add User   page");
		return this.waitForElementToPresent(By.id("validationMessages"))
				.getText();
	}
}
