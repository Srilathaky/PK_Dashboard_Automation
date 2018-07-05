package com.milvik.mip.passwordmanagement.testrunner;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.milvik.mip.dataprovider.MIP_ForgotPass_TestData;
import com.milvik.mip.pageobjects.MIP_ForgotPasswordPage;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.testconfig.MIP_Test_Configuration;
import com.milvik.mip.utility.MIP_BrowserFactory;
import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_LaunchApplication;
import com.milvik.mip.utility.MIP_ReadPropertyFile;
import com.milvik.mip.utility.MIP_ScreenShots;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class MIP_ForgotPassword_Test {
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
		report = new ExtentReports("Test_Reports/Forgotpassword_Test.html");
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
	}

	@BeforeMethod
	public void startTest() {
		try {
			MIP_LaunchApplication.openApplication(
					MIP_Test_Configuration.driver, platform_);
			loginpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_LoginPage.class);
		} catch (Exception e) {

		}

	}

	@Test(priority = 0, testName = "TC11")
	public void forgotPassOne() throws Throwable {
		try {
			logger = report.startTest("Forgotpassword-TC11");

			Assert.assertTrue(loginpage.clickOnForgotPassword()
					.getForgotPassObjects());

		} catch (Throwable t) {
			log.info("Testcase TC11  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}

	}

	@Test(priority = 1, testName = "TC12", dataProvider = "withoutEmail", dataProviderClass = MIP_ForgotPass_TestData.class)
	public void forgotPassTwo(String userid, String errormsg) throws Throwable {
		try {
			logger = report.startTest("Forgotpassword-TC12");
			MIP_ForgotPasswordPage forgotpasspage = loginpage
					.clickOnForgotPassword().enterUserId(userid);
			forgotpasspage.clickOnSubmit();
			Assert.assertTrue(forgotpasspage.getErrorMsg(errormsg).equals(
					errormsg));
		} catch (Throwable t) {
			log.info("Testcase TC12  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC14", dataProvider = "invaliduser", dataProviderClass = MIP_ForgotPass_TestData.class)
	public void forgotPassThree(String userid, String errormsg)
			throws Throwable {
		try {
			logger = report.startTest("Forgotpassword-TC14");
			MIP_ForgotPasswordPage forgotpasspage = loginpage
					.clickOnForgotPassword().enterUserId(userid);
			forgotpasspage.clickOnSubmit();
			Assert.assertTrue(forgotpasspage.getErrorMsg(errormsg).equals(
					errormsg));
		} catch (Throwable t) {
			log.info("Testcase TC14  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC15", dataProvider = "withoutEmail", dataProviderClass = MIP_ForgotPass_TestData.class)
	public void forgotPassFour(String invaliduserid, String validuserid)
			throws Throwable {
		try {
			logger = report.startTest("Forgotpassword-TC15");
			MIP_ForgotPasswordPage forgotpasspage = null;
			forgotpasspage = loginpage.clickOnForgotPassword().enterUserId(
					invaliduserid);
			forgotpasspage.clickOnCancel();
			Assert.assertTrue(MIP_Test_Configuration.driver.getCurrentUrl()
					.contains("login.jsp"));
			loginpage.clickOnForgotPassword().enterUserId(validuserid);
			forgotpasspage.clickOnCancel();
			Assert.assertTrue(MIP_Test_Configuration.driver.getCurrentUrl()
					.contains("login.jsp"));
		} catch (Throwable t) {
			log.info("Testcase TC15  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC16", dataProvider = "withoutuserid", dataProviderClass = MIP_ForgotPass_TestData.class)
	public void forgotPassFive(String errormsg) throws Throwable {
		try {
			logger = report.startTest("Forgotpassword-TC16");
			MIP_ForgotPasswordPage forgotpasspage = loginpage
					.clickOnForgotPassword();
			forgotpasspage.clickOnSubmit();
			Assert.assertTrue(forgotpasspage.getAlertText().equals(errormsg));
		} catch (Throwable t) {
			log.info("Testcase TC16  Failed");
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

		MIP_LaunchApplication.openApplication(MIP_Test_Configuration.driver,
				platform_);
		loginpage = PageFactory.initElements(MIP_Test_Configuration.driver,
				MIP_LoginPage.class);
		homepage = loginpage.login(
				MIP_ReadPropertyFile.getPropertyValue("username"),
				MIP_ReadPropertyFile.getPropertyValue("password"));
		if (flag.equals("0"))
			MIP_BrowserFactory.closeDriver(MIP_Test_Configuration.driver);
		report.endTest(logger);
		report.flush();
	}

}
