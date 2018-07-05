package com.milvik.mip.passwordmanagement.testrunner;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
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
import com.milvik.mip.dataprovider.MIP_ChangePassword_TestData;
import com.milvik.mip.dbqueries.MIP_ChangePassword_Queries;
import com.milvik.mip.pageobjects.MIP_ChangePasswordPage;
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

public class MIP_ChangePassword_Test {
	WebDriver driver;
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage = null;
	MIP_HomePage homepage = null;
	String platform_;

	@BeforeTest
	@Parameters({ "browser", "flag", "platform" })
	public void test_setup(@Optional("firefox") String browser,
			@Optional("0") String flag, @Optional("windows") String platform) {
		platform_ = platform;
		log = MIP_Logging.logDetails("MIP_ChangePassword_Test");
		report = new ExtentReports("Test_Reports/ChangePassword_Test.html");
		if (flag.equals("0")) {
			try {
				Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe");
			} catch (Exception e) {
				log.info("No exe found");
			}
			MIP_Test_Configuration.driver = MIP_BrowserFactory.openBrowser(
					MIP_Test_Configuration.driver, browser, platform);
		}
		MIP_ReadPropertyFile.loadProperty("config");
		MIP_DataBaseConnection.connectToDatabase(platform);
		MIP_LaunchApplication.openApplication(MIP_Test_Configuration.driver,
				platform);
		loginpage = PageFactory.initElements(MIP_Test_Configuration.driver,
				MIP_LoginPage.class);
		homepage = loginpage.login(
				MIP_ReadPropertyFile.getPropertyValue("username"),
				MIP_ReadPropertyFile.getPropertyValue("password"));

	}

	@Test(testName = "TC211")
	public void changePassOne() throws Throwable {
		MIP_ChangePasswordPage changepasspage = null;
		try {
			logger = report.startTest("Search Customer-TC211");
			log.info("Running test case - TC211");
			homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_PASSWORD);
			if (!homepage.waitForElementToVisible(
					By.xpath("//h3[contains(text(),'Change Password')]"))
					.isDisplayed())
				homepage.clickOnMenu("Change Password");
			changepasspage = new MIP_ChangePasswordPage(
					MIP_Test_Configuration.driver);

			Assert.assertTrue(changepasspage.validateChangePassObjects());
		} catch (Throwable t) {
			log.info("Testcase TC211  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC214-TC215,TC216", dataProvider = "negativeTestData", dataProviderClass = MIP_ChangePassword_TestData.class)
	public void changePassThree(String testcase, String oldpass,
			String newpass, String confpass, String errmsg) throws Throwable {
		MIP_ChangePasswordPage changepasspage = null;
		try {
			logger = report.startTest("Search Customer-TC214-TC215,TC216");
			log.info("Running test case - " + testcase);

			homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_PASSWORD);

			if (!homepage.waitForElementToVisible(
					By.xpath("//h3[contains(text(),'Change Password')]"))
					.isDisplayed())
				homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_PASSWORD);

			changepasspage = PageFactory
					.initElements(MIP_Test_Configuration.driver,
							MIP_ChangePasswordPage.class);
			Thread.sleep(1000);
			changepasspage.enterPassworddata(oldpass, newpass, confpass)
					.clickOnSave();
			Assert.assertTrue(changepasspage.getValidationMsg().trim()
					.replaceAll("\\s", "")
					.equalsIgnoreCase(errmsg.trim().replaceAll("\\s", "")));

		} catch (Throwable t) {
			log.info("Testcase " + testcase + "  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 10, testName = "TC212", dataProvider = "ChangePAsswordData", dataProviderClass = MIP_ChangePassword_TestData.class)
	public void changePassFour(String username, String password,
			String oldpass, String newpass, String confpass, String errmsg)
			throws Throwable {
		MIP_ChangePasswordPage changepasspage = null;
		try {
			logger = report.startTest("Search Customer-TC212");
			log.info("Running test case - TC212");
			MIP_LaunchApplication.openApplication(
					MIP_Test_Configuration.driver, platform_);
			MIP_LoginPage loginpage = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_LoginPage.class);
			MIP_HomePage homepage = loginpage.login(username, password);
			homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_PASSWORD);
			String beforepass = MIP_ChangePassword_Queries
					.getPasswordDetails(username);
			changepasspage = new MIP_ChangePasswordPage(
					MIP_Test_Configuration.driver);
			changepasspage.enterPassworddata(oldpass, newpass, confpass)
					.clickOnClear();
			Assert.assertTrue(changepasspage.getOldPassword().equals(""));
			Assert.assertTrue(changepasspage.getNewPassword().equals(""));
			Assert.assertTrue(changepasspage.getConfPassword().equals(""));
			changepasspage.enterPassworddata(oldpass, newpass, confpass)
					.clickOnSave().confirmChangePass("yes");
			Assert.assertTrue(changepasspage.getSuccessMsg().trim()
					.replaceAll("\\s", "")
					.equalsIgnoreCase(errmsg.trim().replaceAll("\\s", "")));
			String afterpass = MIP_ChangePassword_Queries
					.getPasswordDetails(username);
			Assert.assertNotEquals(beforepass.trim(), afterpass.trim());

		} catch (Throwable t) {
			log.info("Testcase TC212  Failed");
			log.info("Error occured in the test case", t);
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
