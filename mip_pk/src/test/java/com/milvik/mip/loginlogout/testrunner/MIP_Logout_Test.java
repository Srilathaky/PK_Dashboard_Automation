package com.milvik.mip.loginlogout.testrunner;

import org.apache.log4j.Logger;
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

import com.milvik.mip.dataprovider.MIP_Login_TestData;
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

public class MIP_Logout_Test {
	WebDriver driver;
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage = null;

	@BeforeTest
	@Parameters({ "browser", "flag", "platform" })
	public void test_setup(@Optional("firefox") String browser,
			@Optional("0") String flag, @Optional("windows") String platform) {
		log = MIP_Logging.logDetails("MIP_Logout_Test");
		report = new ExtentReports("Test_Reports/Logout_Test.html");
		if (flag.equals("0")) {
			try {
				Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe");
			} catch (Exception e) {
				System.out.println("No exe found");
			}
			MIP_Test_Configuration.driver = MIP_BrowserFactory.openBrowser(
					MIP_Test_Configuration.driver, browser, platform);
		}
		MIP_ReadPropertyFile.loadProperty("config");
		MIP_DataBaseConnection.connectToDatabase(platform);
		MIP_LaunchApplication.openApplication(MIP_Test_Configuration.driver,platform);

	}

	@Test(testName = "TC217")
	public void logoutTestOne() throws Throwable {
		try {
			logger = report.startTest("Login-TC217");
			log.info("Running test case - TC217");
			loginpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_LoginPage.class);
			MIP_HomePage homepage = loginpage.login(
					MIP_ReadPropertyFile.getPropertyValue("username"),
					MIP_ReadPropertyFile.getPropertyValue("password"));
			homepage.clickonLogout();
			Assert.assertTrue(loginpage.userid.isDisplayed());
			Assert.assertTrue(loginpage.password.isDisplayed());
			Assert.assertTrue(loginpage.login_button.isDisplayed());
			Assert.assertTrue(loginpage.clear_button.isDisplayed());
			Assert.assertTrue(loginpage.forgotPassword_link.isDisplayed());
			Assert.assertEquals(loginpage.userid.getAttribute("type"), "text");
			Assert.assertEquals(loginpage.password.getTagName(), "input");
			Assert.assertTrue(loginpage.forgotPassword_link.isDisplayed());
			Assert.assertEquals(loginpage.forgotPassword_link.getTagName(), "a");
		} catch (Throwable t) {
			// loginpage.clickonLogout();
			log.info("Testcase TC217  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}

	}

	@Test(testName = "TC218", dataProvider = "logoutdata", dataProviderClass = MIP_Login_TestData.class)
	public void logoutTestTwo(String menu) throws Throwable {

		try {
			logger = report.startTest("Login-TC218");
			log.info("Running test case - TC218");
			loginpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_LoginPage.class);
			MIP_HomePage homepage = loginpage.login(
					MIP_ReadPropertyFile.getPropertyValue("username"),
					MIP_ReadPropertyFile.getPropertyValue("password"));
			homepage.clickOnMenu(menu);
			homepage.clickonLogout();
			Assert.assertTrue(loginpage.userid.isDisplayed());
			Assert.assertTrue(loginpage.password.isDisplayed());
			Assert.assertTrue(loginpage.login_button.isDisplayed());
			Assert.assertTrue(loginpage.clear_button.isDisplayed());
			Assert.assertTrue(loginpage.forgotPassword_link.isDisplayed());
			Assert.assertEquals(loginpage.userid.getAttribute("type"), "text");
			Assert.assertEquals(loginpage.password.getTagName(), "input");
			Assert.assertTrue(loginpage.forgotPassword_link.isDisplayed());
			Assert.assertEquals(loginpage.forgotPassword_link.getTagName(), "a");
		} catch (Throwable t) {
			// loginpage.clickonLogout();
			log.info("Testcase TC218  Failed");
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
			loginpage.clickonLogout();
		} else if ((res.getStatus() == ITestResult.SUCCESS)) {
			logger.log(LogStatus.PASS, "Test passed");
		}
	}

	@AfterTest(alwaysRun = true)
	@Parameters("flag")
	public void tear_down(@Optional("0") String flag) {
		if (flag.equals("0"))
			MIP_BrowserFactory.closeDriver(MIP_Test_Configuration.driver);
		else
			PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_LoginPage.class).login(
					MIP_ReadPropertyFile.getPropertyValue("username"),
					MIP_ReadPropertyFile.getPropertyValue("password"));
		report.endTest(logger);
		report.flush();
	}
}
