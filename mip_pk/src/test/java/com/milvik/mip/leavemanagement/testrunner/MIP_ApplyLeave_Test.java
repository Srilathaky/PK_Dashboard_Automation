package com.milvik.mip.leavemanagement.testrunner;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
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
import com.milvik.mip.dbqueries.MIP_UserManagement_Queries;
import com.milvik.mip.listeners.MIP_RetryAnalyzer;
import com.milvik.mip.pageobjects.MIP_ApplyLeavePage;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.testconfig.MIP_Test_Configuration;
import com.milvik.mip.utility.MIP_BrowserFactory;
import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_LaunchApplication;
import com.milvik.mip.utility.MIP_Logging;
import com.milvik.mip.utility.MIP_ReadPropertyFile;
import com.milvik.mip.utility.MIP_ScreenShots;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class MIP_ApplyLeave_Test {
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage = null;
	MIP_HomePage homepage = null;
	MIP_ApplyLeavePage applyleave = null;

	@BeforeTest
	@Parameters({ "browser", "flag", "platform" })
	public void test_setup(@Optional("firefox") String browser,
			@Optional("0") String flag, @Optional("windows") String platform) {
		log = MIP_Logging.logDetails("MIP_AddRole_Test");
		report = new ExtentReports("Test_Reports/Add_Role_Test.html");
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
			MIP_LaunchApplication.openApplication(
					MIP_Test_Configuration.driver, platform);
			loginpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_LoginPage.class);
			homepage = loginpage.login(
					MIP_ReadPropertyFile.getPropertyValue("username"),
					MIP_ReadPropertyFile.getPropertyValue("password"));
			homepage.clickOnMenu(MIP_Menu_Constants.LEAVE);
			homepage.clickOnMenu(MIP_Menu_Constants.APPLY_LEAVE);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.clickOnMenu(MIP_Menu_Constants.LEAVE);
			homepage.clickOnMenu(MIP_Menu_Constants.APPLY_LEAVE);
		}
	}

	@Test(priority = 1, testName = "TC68", retryAnalyzer = MIP_RetryAnalyzer.class)
	public void leaveManagement() throws Throwable {
		try {
			logger = report.startTest("Leave Management Test -TC68");
			log.info("Running test case - TC68");
			homepage.clickOnMenu(MIP_Menu_Constants.LEAVE);
			homepage.clickOnMenu(MIP_Menu_Constants.LEAVE);
			homepage.waitForElementToVisible(By.linkText("Apply Leave"));
			Assert.assertTrue(MIP_Test_Configuration.driver.findElement(
					By.linkText(MIP_Menu_Constants.APPLY_LEAVE)).isDisplayed());
			homepage.waitForElementToVisible(By
					.linkText(MIP_Menu_Constants.VIEW_LEAVE));
			Assert.assertTrue(MIP_Test_Configuration.driver.findElement(
					By.linkText("View Leaves")).isDisplayed());
		} catch (Throwable t) {
			log.info("Testcase TC68  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 2, testName = "TC69")
	public void applyLeaveOne() throws Throwable {
		try {
			logger = report.startTest("Aply Leave  Test -TC69");
			log.info("Running test case - TC69");
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Apply Leave')]"));
			MIP_ApplyLeavePage applyleave = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_ApplyLeavePage.class);

			Assert.assertTrue(applyleave.userName.isDisplayed());
			Assert.assertTrue(applyleave.fromDate.isDisplayed());
			Assert.assertTrue(applyleave.toDate.isDisplayed());
			Assert.assertTrue(applyleave.reason.isDisplayed());
			Assert.assertTrue(applyleave.fromCalender.isDisplayed());
			Assert.assertTrue(applyleave.toDate.isDisplayed());
			Assert.assertTrue(applyleave.applyBtn.isDisplayed());
			Assert.assertTrue(applyleave.clearBtn.isDisplayed());
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.APPLY_LEAVE);
			log.info("Testcase TC69  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 3, testName = "TC70 to TC75", dataProvider = "applyLeaveTestData", dataProviderClass = MIP_LeaveManagement_TestData.class)
	public void applyLeaveTwo(String userName, String fromDate, String toDate,
			String reason) throws Throwable {
		try {
			logger = report.startTest("Aply Leave  Test -TC70 to TC75");
			log.info("Running test case - TC70 to TC75");
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Apply Leave')]"));
			MIP_ApplyLeavePage applyleave = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_ApplyLeavePage.class);

			Assert.assertEquals(applyleave.getUserDetails(),
					MIP_UserManagement_Queries.getActiveUser());
			applyleave.selectUserName(userName);
			Assert.assertEquals(applyleave
					.getSelectedOptionFromUserNameDropDown().trim(), userName
					.trim());
			applyleave.clickOnFromDate().selectFromLeaveDate(fromDate);
			Assert.assertEquals(applyleave.fromDate.getAttribute("value")
					.trim(), fromDate.trim());
			applyleave.selectReason(reason);
			Assert.assertEquals(applyleave
					.getSelectedOptionFromReasonDropDown().trim(), reason
					.trim());
			applyleave.clickOnToDate();
			applyleave.selectToLeaveDate(toDate);
			Assert.assertEquals(applyleave.toDate.getAttribute("value").trim(),
					toDate.trim());
			applyleave.clearFromDate();
			Assert.assertEquals(applyleave.fromDate.getAttribute("value"), "");
			applyleave.clearToDate();
			Assert.assertEquals(applyleave.toDate.getAttribute("value"), "");
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.APPLY_LEAVE);
			log.info("Testcase TC70 to TC75  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 4, testName = "TC77", dataProvider = "applyLeaveTestData", dataProviderClass = MIP_LeaveManagement_TestData.class, retryAnalyzer = MIP_RetryAnalyzer.class)
	public void applyLeaveThree(String userName, String fromDate,
			String toDate, String reason) throws Throwable {
		try {
			logger = report.startTest("Aply Leave  Test -TC77");
			log.info("Running test case - TC77");
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Apply Leave')]"));
			MIP_ApplyLeavePage applyleave = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_ApplyLeavePage.class);
			Assert.assertEquals(applyleave.getUserDetails(),
					MIP_UserManagement_Queries.getActiveUser());
			Thread.sleep(1000);
			applyleave.selectUserName(userName);
			applyleave.clickOnFromDate().selectFromLeaveDate(fromDate);
			applyleave.selectReason(reason);
			applyleave.clickOnToDate().selectToLeaveDate(toDate);
			applyleave.clickOnClear();
			Assert.assertEquals(applyleave
					.getSelectedOptionFromUserNameDropDown().trim(), "");
			Assert.assertEquals(applyleave
					.getSelectedOptionFromReasonDropDown().trim(), "");
			Assert.assertEquals(applyleave.fromDate.getAttribute("value"), "");
			Assert.assertEquals(applyleave.toDate.getAttribute("value"), "");
			homepage.clickOnMenu(MIP_Menu_Constants.APPLY_LEAVE);
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.APPLY_LEAVE);
			log.info("Testcase TC77  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 5, testName = "TC79 to TC82", dataProvider = "applyLeaveNegativeTestData", dataProviderClass = MIP_LeaveManagement_TestData.class, retryAnalyzer = MIP_RetryAnalyzer.class)
	public void applyLeaveFour(String testcase, String userName,
			String fromDate, String toDate, String reason, String errormsg)
			throws Throwable {
		try {
			logger = report.startTest("Aply Leave  Test -TC79 to TC82");
			log.info("Running test case - " + testcase);
			homepage.clickOnMenu(MIP_Menu_Constants.APPLY_LEAVE);
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Apply Leave')]"));
			MIP_ApplyLeavePage applyleave = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_ApplyLeavePage.class);
			Thread.sleep(1000);
			applyleave.selectUserName(userName);
			if (!fromDate.equals(""))
				applyleave.clickOnFromDate().selectFromLeaveDate(fromDate);
			if (!toDate.equals(""))
				applyleave.clickOnToDate().selectToLeaveDate(toDate);

			applyleave.selectReason(reason);
			applyleave.clickOnApply();
			Assert.assertTrue(applyleave.getValidationMessage().trim()
					.replaceAll("\\s", "")
					.equalsIgnoreCase(errormsg.trim().replaceAll("\\s", "")));
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.APPLY_LEAVE);
			log.info("Testcase TC79 to TC82  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(enabled = false, priority = 10, testName = "TC76", dataProvider = "applyLeaveTestData", dataProviderClass = MIP_LeaveManagement_TestData.class)
	public void applyLeaveFive(String userName, String fromDate, String toDate,
			String reason) throws Throwable {
		try {
			logger = report.startTest("Aply Leave  Test -TC76");
			log.info("Running test case - TC76");
			homepage.clickOnMenu(MIP_Menu_Constants.APPLY_LEAVE);
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Apply Leave')]"));
			MIP_ApplyLeavePage applyleave = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_ApplyLeavePage.class);
			int count = MIP_LeaveManagemen_Queries.getLeavedetails(userName);
			applyleave.selectUserName(userName);
			applyleave.clickOnFromDate().selectFromLeaveDate(fromDate);
			applyleave.selectReason(reason);
			applyleave.clickOnToDate().selectToLeaveDate(toDate);
			applyleave.clickOnApply();
			applyleave.confirmPopUp("yes");
			Assert.assertTrue(applyleave
					.getSuccessMessage()
					.trim()
					.replaceAll("\\s", "")
					.equalsIgnoreCase(
							"The leave details have been updated successfully."
									.trim().replaceAll("\\s", "")));
			Assert.assertTrue(count < MIP_LeaveManagemen_Queries
					.getLeavedetails(userName));

		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.APPLY_LEAVE);
			log.info("Testcase TC76  Failed");
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
			homepage.clickOnMenu(MIP_Menu_Constants.APPLY_LEAVE);
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
