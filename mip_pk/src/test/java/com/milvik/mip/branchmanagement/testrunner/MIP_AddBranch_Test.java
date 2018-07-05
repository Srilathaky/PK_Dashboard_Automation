package com.milvik.mip.branchmanagement.testrunner;

import java.util.Map;

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
import com.milvik.mip.dataprovider.MIP_BranchManagement_TestData;
import com.milvik.mip.dbqueries.MIP_BranchManagement_Queries;
import com.milvik.mip.pageobjects.MIP_AddBranchPage;
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

public class MIP_AddBranch_Test {
	WebDriver driver;
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage = null;
	MIP_HomePage homepage = null;

	@BeforeTest
	@Parameters({ "browser", "flag", "platform" })
	public void test_setup(@Optional("firefox") String browser,
			@Optional("0") String flag, @Optional("windows") String platform) {
		log = MIP_Logging.logDetails("MIP_AddBranch_Test");
		report = new ExtentReports("Test_Reports/AddBranch_Test.html");
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
.openApplication(
					MIP_Test_Configuration.driver, platform);
			loginpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_LoginPage.class);
			homepage = loginpage.login(
					MIP_ReadPropertyFile.getPropertyValue("username"),
					MIP_ReadPropertyFile.getPropertyValue("password"));
			homepage.clickOnMenu(MIP_Menu_Constants.BRANCH);
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_BRANCH);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.clickOnMenu(MIP_Menu_Constants.BRANCH);
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_BRANCH);
		}
	}

	@Test(testName = "TC21")
	public void addBranchOne() throws Throwable {
		MIP_AddBranchPage addbranch = null;
		try {
			logger = report.startTest("Add Branch-TC21");
			log.info("Running test case -TC21");
			addbranch = new MIP_AddBranchPage(MIP_Test_Configuration.driver);
			addbranch.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Add Branch')]"));
			Assert.assertTrue(addbranch.validateAddBranchObjecs());
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.ADD_BRANCH);
			log.info("TC21 Test Failed");
			throw t;
		}
	}

	@Test(testName = "TC22", dataProvider = "addBranchData", dataProviderClass = MIP_BranchManagement_TestData.class)
	public void addBranchTwo(String name, String street, String region,
			String city, String msg) throws Throwable {
		MIP_AddBranchPage addbranch = null;
		int flag = 0;
		try {
			logger = report.startTest("Add Branch-TC22");
			log.info("Running test case -TC21");
			addbranch = new MIP_AddBranchPage(MIP_Test_Configuration.driver);
			addbranch.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Add Branch')]"));
			addbranch.enterBranchInfo(name, street, region, city)
					.clickOnClear();
			Assert.assertTrue(addbranch.getBranchInfo("name").equals(""));
			Assert.assertTrue(addbranch.getBranchInfo("street").equals(""));
			Assert.assertTrue(addbranch.getBranchInfo("region").equals(""));
			Assert.assertTrue(addbranch.getBranchInfo("city").equals(""));
			addbranch.enterBranchInfo(name, street, region, city).clickOnSave()
					.confirmAddBranch("yes");
			flag = 1;
			Assert.assertTrue(addbranch.getmessage().trim()
					.replaceAll("\\s", "")
					.equalsIgnoreCase(msg.trim().replaceAll("\\s", "")));
			Map<String, String> branchinfo = MIP_BranchManagement_Queries
					.getBranchDetails(name);
			Assert.assertTrue(branchinfo.get("name").equalsIgnoreCase(name));
			Assert.assertTrue(branchinfo.get("street").equalsIgnoreCase(street));
			Assert.assertTrue(branchinfo.get("region").equalsIgnoreCase(region));
			Assert.assertTrue(branchinfo.get("city").equalsIgnoreCase(city));
			homepage.clickOnMenu(MIP_Menu_Constants.BRANCH);
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_BRANCH);
		} catch (Throwable t) {
			if (flag == 1)
				homepage.clickOnMenu(MIP_Menu_Constants.BRANCH);
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_BRANCH);
			log.info("TC22 Test Failed");
			log.error("error--", t);
			throw t;
		}
	}

	@Test(testName = "TC24 to TC29", dataProvider = "addBranchNegative", dataProviderClass = MIP_BranchManagement_TestData.class)
	public void addBranchThree(String testcase, String name, String street,
			String region, String city, String msg) throws Throwable {
		MIP_AddBranchPage addbranch = null;
		try {
			logger = report.startTest("Add Branch-TC24 to TC29");
			log.info("Running test case -" + testcase);
			addbranch = new MIP_AddBranchPage(MIP_Test_Configuration.driver);
			addbranch.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Add Branch')]"));
			String errmsg = addbranch
					.enterBranchInfo(name, street, region, city).clickOnSave()
					.getValidationMsg();
			addbranch.clickOnClear();
			errmsg = errmsg.trim().replaceAll("\\s", "");
			msg = msg.trim().replaceAll("\\s", "").replace("  ", "");
			System.out.println(errmsg);
			System.out.println(msg);
			Assert.assertTrue(msg.equalsIgnoreCase(errmsg));
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.ADD_BRANCH);
			log.info(testcase + " Test Failed");
			log.error("error while executing testcase" + testcase, t);
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
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_BRANCH);
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
			homepage.clickOnMenu(MIP_Menu_Constants.BRANCH);
		report.endTest(logger);
		report.flush();
	}
}
