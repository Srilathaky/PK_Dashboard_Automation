package com.milvik.mip.pageobjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.milvik.mip.pageutil.MIP_RoleManagementPage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_AddRolePage extends MIP_RoleManagementPage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_AddRolePage");
	}

	public MIP_AddRolePage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	@FindBy(id = "roleName")
	public WebElement roleName;

	public void enterRoleName(String roleName) {
		logger.info("Entering the Role Name");
		WebElement ele = this.waitForElementToVisible(By.id("roleName"));
		ele.clear();
		ele.sendKeys(roleName);
		if (!ele.getAttribute("value").equalsIgnoreCase(roleName)) {
			ele.sendKeys(roleName);
		}
	}

	public void enterComission(String comission) {
		logger.info("Entering the commissiom");
		if (!comission.equals(""))
			this.waitForElementToVisible(By.id("roleCommission")).sendKeys(
					comission);

	}

	public void confirmAddRole(String role) {
		logger.info("Confirming the Add Role Pop-up");
		this.confirmPopUp(role);
	}

	public String getSuccessMessage() {
		logger.info("Getting the success message in Add Role Page");
		return this.waitForElementToVisible(By.className("error-div-body"))
				.getText();
	}

}
