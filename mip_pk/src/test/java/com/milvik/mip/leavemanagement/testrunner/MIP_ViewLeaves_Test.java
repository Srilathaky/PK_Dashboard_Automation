package com.milvik.mip.leavemanagement.testrunner;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.milvik.mip.constants.MIP_Menu_Constants;
import com.milvik.mip.dataprovider.MIP_LeaveManagement_TestData;
import com.milvik.mip.dbqueries.MIP_LeaveManagemen_Queries;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.pageobjects.MIP_ViewLeavesPage;
import com.milvik.mip.testconfig.MIP_Test_Configuration;
import com.milvik.mip.utility.MIP_BrowserFactory;
import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_DateFunctionality;
import com.milvik.mip.utility.MIP_LaunchApplication;
import com.milvik.mip.utility.MIP_Logging;
import com.milvik.mip.utility.MIP_ReadPropertyFile;
import com.milvik.mip.utility.MIP_ScreenShots;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class MIP_ViewLeaves_Test {
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage = null;
	MIP_HomePage homepage = null;
	MIP_ViewLeavesPage viewleave = null;

	@BeforeTest
	@Parameters({ "browser", "flag", "platform" })
	public void test_setup(@Optional("firefox") String browser,
			@Optional("0") String flag, @Optional("windows") String platform) {
		log = MIP_Logging.logDetails("MIP_ApplyLeave_Test");
		report = new ExtentReports("Test_Reports/MIP_ViewLeaves_Test.html");
		if (flag.equals("0")) {
			try {
				Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe");
			} catch (Exception e) {
				log.info("No exe found");
			}
			MIP_Test_Configuration.driver = MIP_BrowserFactory.openBrowser(
					MIP_Test_Configuration.driver, browser, platform);
			MIP_ReadPropertyFile.loadProperty("config");
			MIP_DataBaseConnection.connectToDatabase(platform);
			MIP_LaunchApplication
					.openApplication(MIP_Test_Configuration.driver,platform);
			loginpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_LoginPage.class);
			homepage = loginpage.login(
					MIP_ReadPropertyFile.getPropertyValue("username"),
					MIP_ReadPropertyFile.getPropertyValue("password"));
			homepage.clickOnMenu(MIP_Menu_Constants.LEAVE);
			homepage.waitForElementToVisible(By
					.linkText(MIP_Menu_Constants.VIEW_LEAVE));
			homepage.clickOnMenu(MIP_Menu_Constants.VIEW_LEAVE);

		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.clickOnMenu(MIP_Menu_Constants.LEAVE);
			homepage.clickOnMenu(MIP_Menu_Constants.VIEW_LEAVE);
		}
	}

	@Test(priority = 1, testName = "TC83")
	public void viewLeavesOne() throws Throwable {
		try {
			logger = report.startTest("ViewLeaves Test Test -TC83");
			log.info("Running test case - TC83");
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'View Leaves')]"));
			viewleave = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_ViewLeavesPage.class);
			List<WebElement> option = viewleave.getLeaveRangeOptions();
			Assert.assertEquals(MIP_ViewLeavesPage.leaveRangesize,
					option.size());
			Assert.assertEquals(option.get(0).getText().trim(),
					MIP_ViewLeavesPage.leaveOption_today);
			Assert.assertEquals(option.get(1).getText().trim(),
					MIP_ViewLeavesPage.leaveOption_week);
			Assert.assertEquals(option.get(2).getText().trim(),
					MIP_ViewLeavesPage.leaveOption_month);
			Assert.assertEquals(option.get(3).getText().trim(),
					MIP_ViewLeavesPage.leaveOption_custom);

			Assert.assertTrue(viewleave.viewBtn.isDisplayed());
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.VIEW_LEAVE);
			log.info("Testcase TC83  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 2, testName = "TC84 to TC86")
	public void viewLeavesTwo() throws Throwable {
		try {
			logger = report.startTest("ViewLeaves Test Test -TC84 to TC86");
			log.info("Running test case - TC84 to TC86");
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'View Leaves')]"));
			viewleave = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_ViewLeavesPage.class);
			MIP_ViewLeavesPage viewleave = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_ViewLeavesPage.class);

			int count = MIP_LeaveManagemen_Queries.getTodayLeavedetails();
			if (count > 0) {
				Assert.assertTrue(viewleave.validateTableHeading());
				Assert.assertEquals(count, viewleave.getLeaveListDetails());
			} else {
				Assert.assertTrue(viewleave
						.getSearchresultText()
						.trim()
						.replaceAll("\\s", "")
						.equalsIgnoreCase(
								MIP_ViewLeavesPage.search_result_message.trim()
										.replaceAll("\\s", "")));
			}
			viewleave.selectLeaveRangeDropDownText(
					MIP_ViewLeavesPage.leaveOption_week).clickOnView();
			count = MIP_LeaveManagemen_Queries.getWeekLeavedetails();
			if (count > 0) {
				Assert.assertTrue(viewleave.validateTableHeading());
				System.out.println(viewleave.getLeaveListDetails());
				Assert.assertEquals(count, viewleave.getLeaveListDetails());
			} else {
				Assert.assertTrue(viewleave
						.getSearchresultText()
						.trim()
						.replaceAll("\\s", "")
						.equalsIgnoreCase(
								MIP_ViewLeavesPage.search_result_message.trim()
										.replaceAll("\\s", "")));
			}

			viewleave.selectLeaveRangeDropDownText(
					MIP_ViewLeavesPage.leaveOption_month).clickOnView();
			count = MIP_LeaveManagemen_Queries.getMonthLeavedetails();
			if (count > 0) {
				Assert.assertTrue(viewleave.validateTableHeading());
				Assert.assertEquals(count, viewleave.getLeaveListDetails());
			} else {
				Assert.assertTrue(viewleave
						.getSearchresultText()
						.trim()
						.replaceAll("\\s", "")
						.equalsIgnoreCase(
								MIP_ViewLeavesPage.search_result_message.trim()
										.replaceAll("\\s", "")));
			}
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.VIEW_LEAVE);
			log.info("Testcase TC84 to TC86  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 3, testName = "TC87", dataProvider = "viewLeaveTestData", dataProviderClass = MIP_LeaveManagement_TestData.class)
	public void viewLeavesThree(String fromDate, String Todate)
			throws Throwable {
		try {
			logger = report.startTest("ViewLeaves Test Test -TC87");
			log.info("Running test case - TC87");
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'View Leaves')]"));
			viewleave = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_ViewLeavesPage.class);
			viewleave
					.selectLeaveRangeDropDownText(MIP_ViewLeavesPage.leaveOption_custom);

			viewleave.selectFromLeaveDate(fromDate);
			viewleave.selectToDate(Todate);
			viewleave.clickOnView();
			viewleave.clickOnView();
			fromDate = MIP_DateFunctionality.converDateToDBDateFormat(fromDate);
			Todate = MIP_DateFunctionality.converDateToDBDateFormat(Todate);
			int count = MIP_LeaveManagemen_Queries.getCustomLeavedetails(
					fromDate, Todate);
			if (count > 0) {
				Assert.assertTrue(viewleave.validateTableHeading());
				Assert.assertEquals(count, viewleave.getLeaveListDetails());
			} else {
				Assert.assertTrue(viewleave
						.getSearchresultText()
						.trim()
						.replaceAll("\\s", "")
						.equalsIgnoreCase(
								MIP_ViewLeavesPage.search_result_message.trim()
										.replaceAll("\\s", "")));
			}

		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.VIEW_LEAVE);
			log.info("Testcase TC87  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@AfterMethod(alwaysRun = true)
	public void after_test(ITestResult res) {

		if (res.getStatus() == ITestResult.FAILURE) {
			MIP_ScreenShots.takeScreenShot(MIP_Test_Configuration.driver,
					res.getName());
			logger.log(LogStatus.FAIL, "Test Failed");
			logger.log(LogStatus.ERROR, res.getThrowable());
			homepage.clickOnMenu(MIP_Menu_Constants.VIEW_LEAVE);
		} else {
			logger.log(LogStatus.PASS, "Test passed");
		}
	}

	@AfterTest(alwaysRun = true)
	@Parameters("flag")
	public void tear_down(@Optional("0") String flag) {
		if (flag.equals("0"))
			MIP_BrowserFactory.closeDriver(MIP_Test_Configuration.driver);
		else
			homepage.clickOnMenu(MIP_Menu_Constants.LEAVE);
		report.endTest(logger);
		report.flush();
	}
}
