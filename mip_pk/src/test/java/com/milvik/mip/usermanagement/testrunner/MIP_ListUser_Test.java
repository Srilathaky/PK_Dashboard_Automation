package com.milvik.mip.usermanagement.testrunner;

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

import com.milvik.mip.constants.MIP_Constants;
import com.milvik.mip.constants.MIP_Menu_Constants;
import com.milvik.mip.dataprovider.MIP_ListUser_TestData;
import com.milvik.mip.dbqueries.MIP_UserManagement_Queries;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_ListUserPage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
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

public class MIP_ListUser_Test {
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage = null;
	MIP_HomePage homepage = null;
	MIP_ListUserPage listuserpage = null;
	String testname;

	@BeforeTest
	@Parameters({ "browser", "flag", "platform" })
	public void test_setup(@Optional("firefox") String browser,
			@Optional("0") String flag, @Optional("windows") String platform) {
		log = MIP_Logging.logDetails("MIP_AddUser_Test");
		report = new ExtentReports("Test_Reports/Add_User_Test.html");
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
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_USER);

		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.clickOnMenu(MIP_Menu_Constants.USER);
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_USER);
		}
	}

	@Test(testName = "TC48")
	public void listUserOne() throws Throwable {
		testname = "listUserOne";
		try {
			logger = report.startTest("List User-TC48");
			log.info("Running test case - TC48");
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'List User')]"));
			listuserpage = new MIP_ListUserPage(MIP_Test_Configuration.driver);
			Assert.assertTrue(listuserpage.validateListUserHeader());
			Assert.assertTrue(listuserpage.waitForElementToVisible(
					By.id("exportToExcelBtn")).isDisplayed());
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.LIST_USER);
			log.info("Testcase TC48  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC50", dataProvider = "ListUserData", dataProviderClass = MIP_ListUser_TestData.class)
	public void listUserTwo(String mobileNumber) throws Throwable {
		testname = mobileNumber;
		try {
			logger = report.startTest("List User-TC50");
			log.info("Running test case - TC50");
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'List User')]"));
			listuserpage = new MIP_ListUserPage(MIP_Test_Configuration.driver);
			listuserpage.clickOnUser(mobileNumber);
			Map<String, String> userData = MIP_UserManagement_Queries
					.getUserInfo(mobileNumber);

			Assert.assertTrue(userData.get("user_uid").equalsIgnoreCase(
					listuserpage.getUserId().trim()));
			Assert.assertTrue((userData.get("fname") + " " + userData
					.get("sname")).equalsIgnoreCase((listuserpage
					.getFirstName().trim() + " " + listuserpage.getLastName()
					.trim())));

			Assert.assertTrue(userData.get("gender").equalsIgnoreCase(
					listuserpage.getGender().trim()));
			Assert.assertTrue(userData.get("role_name").equalsIgnoreCase(
					listuserpage.getRole().trim()));
			Assert.assertTrue(userData.get("branch_name").equalsIgnoreCase(
					listuserpage.getBranch().trim()));
			if (userData.get("dob") != null) {
				String[] date_data = MIP_DateFunctionality.getDate(
						listuserpage.getdob(), MIP_Constants.DOB_FORMAT);
				String date = userData.get("dob".trim());
				String date_format = date.replaceAll("-", "/");
				String[] date_bd = MIP_DateFunctionality.getDate(date_format,
						"yyyy/MM/dd");
				Assert.assertEquals(date_data[0], date_bd[2]);
				Assert.assertEquals(date_data[1], date_bd[1]);
				Assert.assertEquals(date_data[2], date_bd[0]);
			}
			Assert.assertTrue(MIP_Test_Configuration.driver.findElement(
					By.id("backBtn")).isDisplayed());
			listuserpage.clickOnBack();
			Assert.assertTrue(listuserpage.validateListUserHeader());
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.LIST_USER);
			log.info("Testcase TC50  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@AfterMethod(alwaysRun = true)
	public void after_test(ITestResult res) {

		if (res.getStatus() == ITestResult.FAILURE) {
			MIP_ScreenShots.takeScreenShot(MIP_Test_Configuration.driver,
					res.getName() + "--" + testname);
			logger.log(LogStatus.FAIL, "Test Failed");
			logger.log(LogStatus.ERROR, res.getThrowable());
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_USER);
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
			homepage.clickOnMenu(MIP_Menu_Constants.USER);
		report.endTest(logger);
		report.flush();
	}
}
