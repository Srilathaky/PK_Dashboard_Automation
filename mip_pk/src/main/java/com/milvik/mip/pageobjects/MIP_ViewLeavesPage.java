package com.milvik.mip.pageobjects;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.milvik.mip.constants.MIP_Constants;
import com.milvik.mip.pageutil.MIP_BasePage;
import com.milvik.mip.utility.MIP_DateFunctionality;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_ViewLeavesPage extends MIP_BasePage {
	public static String leaveOption_today = "Today";
	public static String leaveOption_week = "This week";
	public static String leaveOption_month = "This month";
	public static String leaveOption_custom = "Custom";
	public static int leaveRangesize = 4;
	public static String search_result_message = "No user records found matching the specified criteria.";

	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_ViewLeavesPage");
	}

	public MIP_ViewLeavesPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	@FindBy(id = "leaveRange")
	public WebElement leaveDetailDopdown;
	@FindBy(id = "viewBtn")
	public WebElement viewBtn;

	public List<WebElement> getLeaveRangeOptions() {
		return this.selectDropDownOptions(this.waitForElementToVisible(By
				.id("leaveRange")));
	}

	public String getSearchresultText() {
		return this.waitForElementToVisible(By.id("div_searchResults"))
				.getText();
	}

	public int getLeaveListDetails() {
		this.waitForElementToVisible(By.id("userLeaveList"));
		String[] number = this.getSearchresultText().split("\\s");
		int count = driver.findElements(
				By.xpath("//table[@id='userLeaveList']/tbody//tr")).size();
		if (!number[0].equalsIgnoreCase("one")) {
			int numpage = Integer.parseInt(number[0]);
			numpage = (int) Math.ceil(numpage / 10);
			for (int i = 0; i < numpage; i++) {
				driver.findElement(By.linkText("Next")).click();

				count = count
						+ driver.findElements(
								By.xpath("//table[@id='userLeaveList']/tbody//tr"))
								.size();
			}
		}

		return count;
	}

	public MIP_ViewLeavesPage selectLeaveRangeDropDownText(String option) {
		this.waitForElementToVisible(By.id("leaveRange"));
		this.selectDropDownbyText(leaveDetailDopdown, option);
		return this;
	}

	public boolean validateTableHeading() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("validating leave details table heading");

		boolean value1 = driver
				.findElement(
						By.xpath("//table[@id='userLeaveList']/thead/tr/th[1][contains(text(),'User Name')]"))
				.isDisplayed();
		boolean value2 = driver
				.findElement(
						By.xpath("//table[@id='userLeaveList']/thead/tr/th[2][contains(text(),'Leave Date')]"))
				.isDisplayed();
		boolean value3 = driver
				.findElement(
						By.xpath("//table[@id='userLeaveList']/thead/tr/th[3][contains(text(),'Leave Reason')]"))
				.isDisplayed();
		if (value1 && value2 && value3) {
			logger.info("leave details table heading are validated correctly");
			return true;
		} else {
			logger.info("leave details table heading are not validated correctly");
			return false;
		}
	}

	public void clickOnView() {
		this.clickOnElement(By.id("viewBtn"));
	}

	public void selectFromLeaveDate(String dob) {
		if (!dob.equals("")) {
			String[] date = MIP_DateFunctionality.getDate(dob,
					MIP_Constants.DOB_FORMAT);
			this.waitForElementToVisible(By.xpath("//div[@id='calBut1']"))
					.click();
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
			this.waitForElementTobeClickable(
					By.xpath("//table[@class='DynarchCalendar-menu-mtable']/tbody//tr//td/div[contains(text(),'"
							+ date[1] + "')]")).click();
			if ((date[0].charAt(0) + "").equals("0")) {
				date[0] = date[0].charAt(1) + "";
			}

			this.waitForElementToVisible(
					By.xpath("//div[@class='DynarchCalendar-body']/table[@class='DynarchCalendar-bodyTable']/tbody//tr//td/div[@class='DynarchCalendar-day' ][contains(text(),'"
							+ date[0] + "')]")).click();
		}
	}

	public void selectToDate(String dob) {
		if (!dob.equals("")) {
			String[] date = MIP_DateFunctionality.getDate(dob,
					MIP_Constants.DOB_FORMAT);
			driver.findElement(
					By.xpath("//div[@class='calendar-icon'][@id='calBut2']"))
					.click();
			Actions a = new Actions(driver);
			WebElement ele;

			a.moveToElement(
					this.waitForElementToVisible(By
							.xpath("/html/body/table[2]/tbody/tr/td/div/div[1]/table/tbody/tr/td/div/div")))
					.build().perform();
			this.waitForElementToVisible(
					By.xpath("/html/body/table[2]/tbody/tr/td/div/div[1]/table/tbody/tr/td/div/div"))
					.click();
			ele = this
					.waitForElementToVisible(By
							.xpath("/html/body/table[2]/tbody/tr/td/div/div[4]/table/tbody/tr/td/table[1]/tbody/tr[1]/td/input"));
			ele.clear();
			ele.sendKeys(date[2]);
			this.waitForElementToVisible(
					By.xpath("/html/body/table[2]/tbody/tr/td/div/div[4]/table/tbody/tr/td/table[2]/tbody//tr//td/div[contains(text(),'"
							+ date[1] + "')]")).click();
			if ((date[0].charAt(0) + "").equals("0")) {
				date[0] = date[0].charAt(1) + "";
			}
			this.waitForElementToVisible(
					By.xpath("/html/body/table[2]/tbody/tr/td/div/div[2]/table/tbody//tr//td/div[@class='DynarchCalendar-day' ][contains(text(),'"
							+ date[0] + "')]")).click();

		}

	}
}
