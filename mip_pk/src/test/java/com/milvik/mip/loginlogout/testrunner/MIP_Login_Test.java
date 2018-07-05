package com.milvik.mip.loginlogout.testrunner;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.milvik.mip.constants.MIP_Constants;
import com.milvik.mip.dataprovider.MIP_Login_TestData;
import com.milvik.mip.dbqueries.MIP_AdminConfig_Queries;
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

public class MIP_Login_Test {
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	String platform_;

	@BeforeTest
	@Parameters({ "browser", "flag", "platform" })
	public void test_setup(@Optional("firefox") String browser,
			@Optional("0") String flag, @Optional("windows") String platform) {
		platform_ = platform;
		log = MIP_Logging.logDetails("MIP_Login_Test");
		report = new ExtentReports("Test_Reports/Login_Test.html");
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
		}
	}

	@Test(testName = "TC01-TC02")
	public void loginTestOne() throws Throwable {
		MIP_LoginPage loginpage = null;
		try {
			logger = report.startTest("Login-TC01-TC02");
			log.info("Running test case - TC01-TC02");
			loginpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_LoginPage.class);
			Assert.assertTrue(loginpage.userid.isDisplayed());
			Assert.assertTrue(loginpage.password.isDisplayed());
			Assert.assertTrue(loginpage.login_button.isDisplayed());
			Assert.assertTrue(loginpage.clear_button.isDisplayed());
			Assert.assertTrue(loginpage.forgotPassword_link.isDisplayed());
			Assert.assertEquals(loginpage.userid.getAttribute("type"), "text");
			Assert.assertEquals(loginpage.password.getTagName(), "input");
			Assert.assertTrue(loginpage.forgotPassword_link.isDisplayed());
			Assert.assertEquals(loginpage.forgotPassword_link.getTagName(), "a");
			loginpage.clickOnClear();
			Assert.assertEquals(loginpage.userid.getText(), "");
			Assert.assertEquals(loginpage.password.getText(), "");
			MIP_HomePage homepage = loginpage.login(
					MIP_ReadPropertyFile.getPropertyValue("username"),
					MIP_ReadPropertyFile.getPropertyValue("password"));
			Assert.assertTrue(homepage.validateHomePageNavigation());
			homepage.clickonLogout();
		} catch (Throwable t) {
			log.info("Testcase TC01-TC02  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}

	}

	@Test(enabled = false, testName = "TC02")
	public void loginTestTwo() throws Throwable {
		MIP_LoginPage loginpage = null;
		try {
			logger = report.startTest("Login-TC02");
			log.info("Running test case - TC02");
			loginpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_LoginPage.class);
			MIP_HomePage homepage = loginpage.login(
					MIP_ReadPropertyFile.getPropertyValue("username"),
					MIP_ReadPropertyFile.getPropertyValue("password"));
			Assert.assertTrue(homepage.validateHomePageNavigation());
			homepage.clickonLogout();
		} catch (Throwable t) {
			new MIP_HomePage(MIP_Test_Configuration.driver).clickonLogout();
			log.info("Testcase TC02  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}

	}

	@Test(testName = "TC03")
	public void loginTestThree() throws Throwable {
		MIP_LoginPage loginpage = null;
		try {
			logger = report.startTest("Login-TC03");
			log.info("Running test case - TC03");
			loginpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_LoginPage.class);
			loginpage.userid.sendKeys(MIP_ReadPropertyFile
					.getPropertyValue("username"));
			loginpage.password.sendKeys(MIP_ReadPropertyFile
					.getPropertyValue("password"));
			loginpage.clickOnClear();
			Assert.assertEquals(loginpage.userid.getText(), "");
			Assert.assertEquals(loginpage.password.getText(), "");
		} catch (Throwable t) {
			log.info("Testcase TC03  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC04 to TC08", dataProvider = "invalid_login", dataProviderClass = MIP_Login_TestData.class)
	public void loginTestFour(String username, String password, String errormsg)
			throws Throwable {
		MIP_LoginPage loginpage = null;
		try {
			logger = report.startTest("Login-TC04 to TC08");
			log.info("Running test case - TC04 to TC08");
			MIP_LaunchApplication.openApplication(
					MIP_Test_Configuration.driver, platform_);
			loginpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_LoginPage.class);
			loginpage.login(username, password);
			if (username.equalsIgnoreCase("") || password.equalsIgnoreCase("")) {
				Assert.assertTrue((loginpage.getAlertText()
						.equalsIgnoreCase(errormsg.trim())));
			} else {

				Assert.assertTrue(loginpage.getErrorMsg(errormsg).trim()
						.equalsIgnoreCase(errormsg.trim()));
			}
		} catch (Throwable t) {
			log.info("Testcase TC04 to TC08  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 10, testName = "TC09", dataProvider = "useaccount_block", dataProviderClass = MIP_Login_TestData.class)
	public void loginTestFive(String username, String password, String errormsg)
			throws Throwable {
		MIP_LoginPage loginpage = null;
		try {
			logger = report.startTest("Login-TC09");
			log.info("Running test case - TC09");
			loginpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_LoginPage.class);
			for (int i = 0; i <= MIP_Constants.MAX_LOGIN_ATTEMPTS; i++) {
				loginpage.login(username, password);
			}
			String res_one = errormsg.replaceAll("\\s", "").trim();
			String res_two = loginpage
					.getErrorMsg(loginpage.getErrorMsg(errormsg))
					.replaceAll("\\s", "").trim();
			Assert.assertTrue(res_one.equalsIgnoreCase(res_two));
		} catch (Throwable t) {
			log.info("Testcase TC09  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC10", dataProvider = "login_blockAccount", dataProviderClass = MIP_Login_TestData.class)
	public void loginTestSix(String username, String password, String errormsg)
			throws Throwable {
		MIP_LoginPage loginpage = null;
		try {
			logger = report.startTest("Login-TC10");
			log.info("Running test case - TC010");
			loginpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_LoginPage.class);
			loginpage.login(username, password);
			String res_one = errormsg.replaceAll("\\s", "").trim();
			String res_two = loginpage
					.getErrorMsg(loginpage.getErrorMsg(errormsg))
					.replaceAll("\\s", "").trim();
			Assert.assertTrue(res_one.equalsIgnoreCase(res_two));
			Assert.assertEquals(MIP_Constants.BLOCKED_ACCOUNT_STATUS,
					MIP_AdminConfig_Queries.getUserStatus(username));
		} catch (Throwable t) {
			log.info("Testcase TC10  Failed");
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
