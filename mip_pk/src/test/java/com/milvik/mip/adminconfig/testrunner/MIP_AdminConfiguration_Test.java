package com.milvik.mip.adminconfig.testrunner;

import java.util.Map;

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
import com.milvik.mip.dataprovider.MIP_AdminConfiguration_TestData;
import com.milvik.mip.dbqueries.MIP_AdminConfig_Queries;
import com.milvik.mip.pageobjects.MIP_AdminConfigurationPage;
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

public class MIP_AdminConfiguration_Test {
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage = null;
	MIP_HomePage homepage = null;

	@BeforeTest
	@Parameters({ "browser", "flag", "platform" })
	public void test_setup(@Optional("firefox") String browser,
			@Optional("0") String flag, @Optional("windows") String platform) {
		log = MIP_Logging.logDetails("MIP_Admin Configuration_Test");
		report = new ExtentReports(
				"Test_Reports/MIP_AdminConfiguration_Test.html");
		if (flag.equals("0")) {
			try {
				Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe");
			} catch (Exception e) {
				log.info("Exception while killing exe");
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

		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.clickOnMenu(MIP_Menu_Constants.ADMIN_CONFIG);
		}
	}

	@Test(priority = 0, testName = "TC202")
	public void adminConfigOne() throws Throwable {
		MIP_AdminConfigurationPage adminconfig = null;
		try {
			logger = report.startTest("Admin Configuration-TC202");
			log.info("Running test case -TC202");

			homepage.clickOnMenu(MIP_Menu_Constants.ADMIN_CONFIG);
			adminconfig = new MIP_AdminConfigurationPage(
					MIP_Test_Configuration.driver);
			Assert.assertTrue(adminconfig.validateAdminConfigObjects());

		} catch (Throwable t) {
			log.info("TC202 Test Failed");
			log.info("Error while executing test case-TC202 " + t);
			throw t;
		}
	}

	@Test(priority = 1, testName = "TC203", dataProvider = "adminconfigdata", dataProviderClass = MIP_AdminConfiguration_TestData.class)
	public void adminConfigTwo(String password, String loginid,
			String passhist, String loginattempt, String idlestate,
			String announce, String msisdnCode, String msg) throws Throwable {
		MIP_AdminConfigurationPage adminconfig = null;
		try {
			logger = report.startTest("Admin Configuration-TC203");
			log.info("Running test case -TC203");

			homepage.clickOnMenu(MIP_Menu_Constants.ADMIN_CONFIG);
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Admin Configuration')]"));
			adminconfig = new MIP_AdminConfigurationPage(
					MIP_Test_Configuration.driver);
			adminconfig.editAdminConfigInfo(password, loginid, passhist,
					loginattempt, idlestate, announce, msisdnCode)
					.clickOnSave();
			Assert.assertTrue(adminconfig.getSuccessMessage().trim()
					.replaceAll("\\s", "")
					.equalsIgnoreCase(msg.trim().replaceAll("\\s", "")));
			adminconfig.gotoAdminConfigPage();
			Map<String, String> admindetils = MIP_AdminConfig_Queries
					.getAdminConfigInfo();
			Assert.assertTrue(admindetils
					.get("default_password")
					.trim()
					.equalsIgnoreCase(
							adminconfig.getAdminConfigInfo("default_password")
									.trim()));
			Assert.assertTrue(admindetils
					.get("user_login_prefix")
					.trim()
					.equalsIgnoreCase(
							adminconfig.getAdminConfigInfo("user_login_prefix")
									.trim()));
			Assert.assertTrue(admindetils
					.get("password_history_limit")
					.trim()
					.equalsIgnoreCase(
							adminconfig.getAdminConfigInfo(
									"password_history_limit").trim()));
			Assert.assertTrue(admindetils
					.get("max_login_attempts")
					.trim()
					.equalsIgnoreCase(
							adminconfig
									.getAdminConfigInfo("max_login_attempts")
									.trim()));

			Assert.assertTrue(admindetils
					.get("announcement_message")
					.trim()
					.equalsIgnoreCase(
							adminconfig.getAdminConfigInfo(
									"announcement_message").trim()));
			Assert.assertTrue(admindetils
					.get("max_idle_count")
					.trim()
					.equalsIgnoreCase(
							adminconfig.getAdminConfigInfo("max_idle_count")
									.trim()));
			Assert.assertTrue(admindetils
					.get("msisdn_code")
					.trim()
					.equalsIgnoreCase(
							adminconfig.getAdminConfigInfo("msisdn_code")
									.trim()));

		} catch (Throwable t) {
			log.info("TC203 Test Failed");
			log.info("Error while executing test case-TC203 " + t);
			throw t;
		}
	}

	@Test(priority = 10, testName = "TC204 to 210", dataProvider = "adminconfignegativecases", dataProviderClass = MIP_AdminConfiguration_TestData.class)
	public void adminConfigThree(String testcase, String fields, String value,
			String errmsg) throws Throwable {
		MIP_AdminConfigurationPage adminconfig = null;
		try {
			logger = report.startTest("Admin Configuration-TC204 to 210");
			log.info("Running test case -" + testcase);

			homepage.clickOnMenu(MIP_Menu_Constants.ADMIN_CONFIG);
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Admin Configuration')]"));
			adminconfig = new MIP_AdminConfigurationPage(
					MIP_Test_Configuration.driver);
			Thread.sleep(1000);
			if (fields.equalsIgnoreCase("all")) {
				adminconfig.clearAdminConfigInfo("default_password", value);
				adminconfig.clearAdminConfigInfo("user_login_prefix", value);
				adminconfig.clearAdminConfigInfo("password_history_limit", "");
				adminconfig.clearAdminConfigInfo("max_login_attempts", value);
				adminconfig.clearAdminConfigInfo("max_idle_count", value);
				adminconfig.clearAdminConfigInfo("announcement_message", value);
				adminconfig.clearAdminConfigInfo("msisdn_code", value);
				adminconfig.clickOnSave();
				String found_msg = adminconfig.getValidationMsg();
				log.info("Found and Expected Error Message: " + found_msg
						+ " and " + errmsg);
				Assert.assertTrue(found_msg.trim().replaceAll("\\s", "")
						.equalsIgnoreCase(errmsg.trim().replaceAll("\\s", "")));

			}
			if (fields.trim().equalsIgnoreCase("default_password")) {
				adminconfig.clearAdminConfigInfo("default_password", value);
				adminconfig.clickOnSave();
				Assert.assertTrue(adminconfig.getValidationMsg().trim()
						.replaceAll("\\s", "")
						.equalsIgnoreCase(errmsg.trim().replaceAll("\\s", "")));
				if (fields.trim().equalsIgnoreCase("user_login_prefix")) {
					adminconfig
							.clearAdminConfigInfo("user_login_prefix", value);
					Assert.assertTrue(adminconfig
							.getValidationMsg()
							.trim()
							.replaceAll("\\s", "")
							.equalsIgnoreCase(
									errmsg.trim().replaceAll("\\s", "")));
				}
				if (fields.trim().equalsIgnoreCase("password_history_limit")) {
					adminconfig.clearAdminConfigInfo("password_history_limit",
							value);
					Assert.assertTrue(adminconfig
							.getValidationMsg()
							.trim()
							.replaceAll("\\s", "")
							.equalsIgnoreCase(
									errmsg.trim().replaceAll("\\s", "")));
				}
				if (fields.trim().equalsIgnoreCase("max_login_attempts")) {
					adminconfig.clearAdminConfigInfo("max_login_attempts",
							value);
					Assert.assertTrue(adminconfig
							.getValidationMsg()
							.trim()
							.replaceAll("\\s", "")
							.equalsIgnoreCase(
									errmsg.trim().replaceAll("\\s", "")));
				}
				if (fields.trim().equalsIgnoreCase("max_idle_count")) {
					adminconfig.clearAdminConfigInfo("max_idle_count", value);
					Assert.assertTrue(adminconfig
							.getValidationMsg()
							.trim()
							.replaceAll("\\s", "")
							.equalsIgnoreCase(
									errmsg.trim().replaceAll("\\s", "")));
				}
				if (fields.trim().equalsIgnoreCase("announcement_message")) {
					adminconfig.clearAdminConfigInfo("announcement_message",
							value);
					Assert.assertTrue(adminconfig
							.getValidationMsg()
							.trim()
							.replaceAll("\\s", "")
							.equalsIgnoreCase(
									errmsg.trim().replaceAll("\\s", "")));
				}
				if (fields.trim().equalsIgnoreCase(
						"offer_subscription_last_day")) {
					adminconfig.clearAdminConfigInfo(
							"offer_subscription_last_day", value);
					Assert.assertTrue(adminconfig
							.getValidationMsg()
							.trim()
							.replaceAll("\\s", "")
							.equalsIgnoreCase(
									errmsg.trim().replaceAll("\\s", "")));
				}
				if (fields.trim().equalsIgnoreCase("commission_percentage")) {
					adminconfig.clearAdminConfigInfo("commission_percentage",
							value);
					Assert.assertTrue(adminconfig
							.getValidationMsg()
							.trim()
							.replaceAll("\\s", "")
							.equalsIgnoreCase(
									errmsg.trim().replaceAll("\\s", "")));
				}
				if (fields.trim().equalsIgnoreCase("msisdn_code")) {
					adminconfig.clearAdminConfigInfo("msisdn_code", value);
					Assert.assertTrue(adminconfig
							.getValidationMsg()
							.trim()
							.replaceAll("\\s", "")
							.equalsIgnoreCase(
									errmsg.trim().replaceAll("\\s", "")));
				}

			}
		} catch (Throwable t) {
			log.info(testcase + " Test Failed");
			log.info("Error while executing test case- " + testcase + t);
			throw t;
		}
	}

	@AfterMethod
	public void after_test(ITestResult res) {

		if (res.getStatus() == ITestResult.FAILURE) {
			MIP_ScreenShots.takeScreenShot(MIP_Test_Configuration.driver,
					res.getName());
			logger.log(LogStatus.FAIL, "Test Failed");
			logger.log(LogStatus.ERROR, res.getThrowable());
		} else if ((res.getStatus() == ITestResult.SUCCESS)) {
			logger.log(LogStatus.PASS, "Test passed");
		}
	}

	@AfterTest(alwaysRun = true)
	@Parameters("flag")
	public void tear_down(@Optional("0") String flag) {
		if (flag.equals("0"))
			MIP_BrowserFactory.closeDriver(MIP_Test_Configuration.driver);
		report.endTest(logger);
		report.flush();
	}

}
