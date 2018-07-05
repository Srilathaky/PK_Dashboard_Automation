package com.milvik.mip.pageobjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import com.milvik.mip.pageutil.MIP_RoleManagementPage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_ViewRolePage extends MIP_RoleManagementPage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_ViewRolePage");
	}

	public MIP_ViewRolePage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	@FindBy(id = "role")
	public WebElement role;

	public MIP_ViewRolePage selectRole(String role) {

		this.selectDropDownbyText(this.waitForElementToVisible(By.id("role")),
				role);
		return this;
	}

	public String getSelectedOption() {
		this.waitForElementToVisible(By.id("role"));
		this.waitForElementToPresent(By.id("role"));
		Select s = new Select(this.role);
		return s.getFirstSelectedOption().getText();

	}

	public String getSuccessMessage() {
		return this.waitForElementToVisible(By.className("error-div-body"))
				.getText();
	}

	public void confirmViewRole(String role) {
		this.confirmPopUp(role);
	}

	public String getCommission() {
		logger.info("Getting getCommission in Add Role Page");
		return this.waitForElementToVisible(By.id("roleCommission"))
				.getAttribute("value");
	}

	public void updateCommission(String commission) {
		logger.info("Getting updateCommission in Add Role Page");
		WebElement ele = this.waitForElementToVisible(By.id("roleCommission"));
		ele.clear();
		ele.sendKeys(commission);
	}
}
