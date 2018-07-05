package com.milvik.mip.usermanagement.testrunner;

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
import com.milvik.mip.dataprovider.MIP_ResetPassword_TestData;
import com.milvik.mip.pageobjects.MIP_ChangePasswordPage;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.pageobjects.MIP_ResetPasswordPage;
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

public class MIP_ResetPassword_Test {
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage = null;
	MIP_HomePage homepage = null;
	MIP_ResetPasswordPage resetpasspage;
	String platform_;
	String testname;

	@BeforeTest
	@Parameters({ "browser", "flag", "platform" })
	public void test_setup(@Optional("firefox") String browser,
			@Optional("0") String flag, @Optional("windows") String platform) {
		platform_ = platform;
		log = MIP_Logging.logDetails("MIP_ResetPassword_Test");
		report = new ExtentReports("Test_Reports/ResetPassword_Test.html");
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
			homepage.clickOnMenu(MIP_Menu_Constants.USER);
			homepage.clickOnMenu(MIP_Menu_Constants.RESET_PASSWORD);
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),\"Reset User Password\")]"));
			resetpasspage = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_ResetPasswordPage.class);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.clickOnMenu(MIP_Menu_Constants.USER);
			homepage.clickOnMenu(MIP_Menu_Constants.RESET_PASSWORD);
		}
	}

	@Test(priority = 1, testName = "TC62")
	public void resetPasswordOne() throws Throwable {
		testname = "resetPasswordOne";
		try {
			logger = report.startTest("ResetPassword-TC62");
			log.info("Running test case - TC62");
			resetpasspage = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_ResetPasswordPage.class);
			resetpasspage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Reset User Password')]"));
			Assert.assertTrue(resetpasspage.userId.isDisplayed());
			Assert.assertTrue(resetpasspage.searchIcon.isDisplayed());
			homepage.clickOnMenu(MIP_Menu_Constants.USER);
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.USER);
			log.info("Testcase TC62  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 3, testName = "TC63-TC66-TC67", dataProvider = "ResetPassword", dataProviderClass = MIP_ResetPassword_TestData.class)
	public void resetPasswordTwo(String userID, String succMsg,
			String defaultPass, String invalidPass, String errmsg,
			String validPass, String successMsg) throws Throwable {
		testname = userID;
		try {
			logger = report.startTest("ResetPassword-TC63-TC66-TC67");
			log.info("Running test case - TC63-TC66-TC67");
			resetpasspage = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_ResetPasswordPage.class);
			resetpasspage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Reset User Password')]"));
			resetpasspage.enterUserID(userID).clickOnSearchIcon()
					.clickOnResetPassword().confirmPopUp("yes");
			Assert.assertTrue(resetpasspage.getConfirmationMessage().trim()
					.equalsIgnoreCase(succMsg.trim()));

			resetpasspage.waitForElementToVisible(By.linkText("Logout"))
					.click();

			loginpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_LoginPage.class);
			loginpage.waitForElementToVisible(By.id("loginId"));
			loginpage.login(userID, defaultPass);
			MIP_ChangePasswordPage changepass = new MIP_ChangePasswordPage(
					MIP_Test_Configuration.driver);
			Assert.assertTrue(changepass.validateChangePassObjects());
			changepass.enterPassworddata(defaultPass, invalidPass, invalidPass)
					.clickOnSave();
			Assert.assertTrue(changepass.getValidationMsg().trim()
					.replaceAll("\\s", "")
					.equalsIgnoreCase(errmsg.trim().replaceAll("\\s", "")));
			changepass.clickOnClear()
					.enterPassworddata(defaultPass, validPass, validPass)
					.clickOnSave().confirmChangePass("yes");
			Assert.assertTrue(changepass.getSuccessMsg().trim()
					.equalsIgnoreCase(successMsg.trim()));
			MIP_LaunchApplication.openApplication(
					MIP_Test_Configuration.driver, platform_);
			loginpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_LoginPage.class);
			homepage = loginpage.login(
					MIP_ReadPropertyFile.getPropertyValue("username"),
					MIP_ReadPropertyFile.getPropertyValue("password"));
		} catch (Throwable t) {
			MIP_LaunchApplication.openApplication(
					MIP_Test_Configuration.driver, platform_);
			loginpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_LoginPage.class);
			homepage = loginpage.login(
					MIP_ReadPropertyFile.getPropertyValue("username"),
					MIP_ReadPropertyFile.getPropertyValue("password"));
			log.info("Testcase TC63-TC66-TC67  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 2, testName = "TC64-TC65", dataProvider = "ResetPassNegative", dataProviderClass = MIP_ResetPassword_TestData.class)
	public void resetPasswordThree(String userID, String errmsg)
			throws Throwable {
		testname = userID;
		try {
			logger = report.startTest("ResetPassword-TC63");
			log.info("Running test case - TC63");
			resetpasspage = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_ResetPasswordPage.class);
			resetpasspage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Reset User Password')]"));
			if (!userID.equals("")) {
				resetpasspage.enterUserID(userID).clickOnSearchIcon();
				Assert.assertTrue(resetpasspage.getSearchResulMessage().trim()
						.equalsIgnoreCase(errmsg.trim()));
			}
			if (userID.equals("")) {
				resetpasspage.enterUserID(userID).clickOnSearchIcon();
				Assert.assertTrue(resetpasspage.getValidationMessage().trim()
						.replaceAll("\\s", "")
						.equalsIgnoreCase(errmsg.trim().replaceAll("\\s", "")));
			}
			homepage.clickOnMenu(MIP_Menu_Constants.USER);
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.USER);
			log.info("Testcase TC64-TC65  Failed");
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
		} else {
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
