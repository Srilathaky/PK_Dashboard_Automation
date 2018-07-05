package com.milvik.mip.branchmanagement.testrunner;

import java.util.List;
import java.util.Map;

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
import com.milvik.mip.dataprovider.MIP_BranchManagement_TestData;
import com.milvik.mip.dbqueries.MIP_BranchManagement_Queries;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_ListBranchPage;
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

public class MIP_ListBranch_Test {
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage = null;
	MIP_HomePage homepage = null;
	MIP_ListBranchPage listbranch = null;

	@BeforeTest
	@Parameters({ "browser", "flag", "platform" })
	public void test_setup(@Optional("firefox") String browser,
			@Optional("0") String flag, @Optional("windows") String platform) {
		log = MIP_Logging.logDetails("MIP_ListBranch_Test");
		report = new ExtentReports("Test_Reports/ListBranch_Test.html");
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
			homepage.clickOnMenu(MIP_Menu_Constants.BRANCH);
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.clickOnMenu(MIP_Menu_Constants.BRANCH);
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
		}
	}

	@Test(testName = "TC30")
	public void listBranchOne() throws Throwable {

		try {
			logger = report.startTest("List Branch-TC30");
			log.info("Running test case -TC30");
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'List Branches')]"));
			listbranch = new MIP_ListBranchPage(MIP_Test_Configuration.driver);
			Assert.assertTrue(MIP_BranchManagement_Queries.getNumberOfBranch() == (listbranch
					.getNumberofBranch()));
			List<WebElement> tablecontent = listbranch
					.validatelistBranchObjects();
			Assert.assertTrue(tablecontent.get(0).getText()
					.equalsIgnoreCase("Branch Code"));
			Assert.assertTrue(tablecontent.get(1).getText()
					.equalsIgnoreCase("Branch Name"));
			Assert.assertTrue(tablecontent.get(2).getText()
					.equalsIgnoreCase("Street"));
			Assert.assertTrue(tablecontent.get(3).getText()
					.equalsIgnoreCase("Region"));
			Assert.assertTrue(tablecontent.get(4).getText()
					.equalsIgnoreCase("City"));
			Assert.assertTrue(tablecontent.get(5).getText()
					.equalsIgnoreCase("Registered Date"));

		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
			log.info("TC30 Test Failed");
			log.info("Error while executing test case-TC30 " + t);
			throw t;
		}
	}

	@Test(testName = "TC31", dataProvider = "branchName", dataProviderClass = MIP_BranchManagement_TestData.class)
	public void listBranchTwo(String branchname) throws Throwable {
		try {
			logger = report.startTest("List Branch-TC31");
			log.info("Running test case -TC31");
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'List Branches')]"));
			listbranch = new MIP_ListBranchPage(MIP_Test_Configuration.driver);
			Map<String, String> branchdetails = MIP_BranchManagement_Queries
					.getBranchDetails(branchname);
			Assert.assertTrue(branchdetails.get("branchcode").equalsIgnoreCase(
					listbranch.getBranchCode(branchname)));
			listbranch.clickOnBranch(branchname);
			Assert.assertTrue(branchname.equalsIgnoreCase(listbranch
					.getBranchInfo("name")));
			Assert.assertTrue(branchdetails.get("street").equalsIgnoreCase(
					listbranch.getBranchInfo("street")));
			Assert.assertTrue(branchdetails.get("region").equalsIgnoreCase(
					listbranch.getBranchInfo("region")));

			Assert.assertTrue(branchdetails.get("city").equalsIgnoreCase(
					listbranch.getBranchInfo("city")));
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
		} catch (Throwable t) {
			log.info("TC31 Test Failed");
			log.info("Error while executing test case-TC31 " + t);
			throw t;
		}
	}

	@Test(priority = 10, testName = "TC32", dataProvider = "editBranchData", dataProviderClass = MIP_BranchManagement_TestData.class)
	public void listBranchThree(String branchname, String newbranchname,
			String street, String region, String city) throws Throwable {
		try {
			logger = report.startTest("List Branch-TC32");
			log.info("Running test case -TC32");
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'List Branches')]"));
			listbranch = new MIP_ListBranchPage(MIP_Test_Configuration.driver);
			Map<String, String> branchdetails = MIP_BranchManagement_Queries
					.getBranchDetails(branchname);
			String branchCode = branchdetails.get("branchcode");
			Assert.assertTrue(branchCode.equalsIgnoreCase(listbranch
					.getBranchCode(branchname)));
			listbranch.clickOnBranch(branchname)
					.editBranchInfo(newbranchname, street, region, city)
					.clickOnSave().confirmOption("yes").gotoListPage();
			if (!newbranchname.equalsIgnoreCase(branchname)
					&& !(newbranchname.equals(""))) {
				branchname = newbranchname;
			}
			listbranch.clickOnBranch(branchname);
			branchdetails = MIP_BranchManagement_Queries
					.getBranchDetails(branchname);
			Assert.assertTrue(branchname.equalsIgnoreCase(listbranch
					.getBranchInfo("name")));
			Assert.assertTrue(branchdetails.get("street").equalsIgnoreCase(
					listbranch.getBranchInfo("street")));
			Assert.assertTrue(branchdetails.get("region").equalsIgnoreCase(
					listbranch.getBranchInfo("region")));
			Assert.assertTrue(branchdetails.get("city").equalsIgnoreCase(
					listbranch.getBranchInfo("city")));
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
			log.info("TC32 Test Failed");
			log.info("Error while executing test case-TC32 " + t);
			throw t;
		}
	}

	@Test(testName = "TC33", dataProvider = "branchName", dataProviderClass = MIP_BranchManagement_TestData.class)
	public void listBranchFour(String branchname) throws Throwable {
		try {
			logger = report.startTest("List Branch-TC33");
			log.info("Running test case -TC33");
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'List Branches')]"));
			listbranch = new MIP_ListBranchPage(MIP_Test_Configuration.driver);
			listbranch.clickOnBranch(branchname);
			listbranch.clickOnClear();
			Assert.assertTrue("".equalsIgnoreCase(listbranch
					.getBranchInfo("name")));
			Assert.assertTrue("".equalsIgnoreCase(listbranch
					.getBranchInfo("street")));
			Assert.assertTrue("".equalsIgnoreCase(listbranch
					.getBranchInfo("region")));
			Assert.assertTrue("".equalsIgnoreCase(listbranch
					.getBranchInfo("city")));
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
			log.info("TC33 Test Failed");
			log.info("Error while executing test case-TC33 " + t);
			throw t;
		}
	}

	@Test(testName = "TC34", dataProvider = "branchName", dataProviderClass = MIP_BranchManagement_TestData.class)
	public void listBranchFive(String branchname) throws Throwable {
		try {
			logger = report.startTest("List Branch-TC34");
			log.info("Running test case -TC34");
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'List Branches')]"));
			listbranch = new MIP_ListBranchPage(MIP_Test_Configuration.driver);
			listbranch.clickOnBranch(branchname);
			listbranch.clickOnBack();
			Assert.assertTrue("LIST BRANCHES".equalsIgnoreCase(listbranch
					.validatelistbranch()));
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
			log.info("TC34 Test Failed");
			log.info("Error while executing test case-TC34 " + t);
			throw t;
		}
	}

	@Test(enabled = false, testName = "TC35", dataProvider = "deactivateNonAssociatedBranch", dataProviderClass = MIP_BranchManagement_TestData.class)
	public void listBranchSix(String branchname) throws Throwable {
		int flag = 0;
		try {
			logger = report.startTest("List Branch-TC35");
			log.info("Running test case -TC35");
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'List Branches')]"));
			listbranch = new MIP_ListBranchPage(MIP_Test_Configuration.driver);
			listbranch.clickOnBranch(branchname);
			listbranch.clickOnDeactivate().confirmOption("yes");
			flag = 1;
			Map<String, String> branchdetails = MIP_BranchManagement_Queries
					.getBranchDetails(branchname);
			Assert.assertTrue(branchdetails.get("is_active").equalsIgnoreCase(
					"0"));
			homepage.clickOnMenu(MIP_Menu_Constants.BRANCH);
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
		} catch (Throwable t) {
			if (flag == 1)
				homepage.clickOnMenu(MIP_Menu_Constants.BRANCH);

			homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
			log.info("TC35 Test Failed");
			log.info("Error while executing test case-TC35 " + t);
			throw t;
		}
	}

	@Test(testName = "TC36", dataProvider = "deactivateassociatedbranch", dataProviderClass = MIP_BranchManagement_TestData.class)
	public void listBranchSeven(String branchname, String errormsg)
			throws Throwable {
		try {
			logger = report.startTest("List Branch-TC36");
			log.info("Running test case -TC36");
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'List Branches')]"));
			listbranch = new MIP_ListBranchPage(MIP_Test_Configuration.driver);
			listbranch.clickOnBranch(branchname);
			listbranch.clickOnDeactivate();

			Assert.assertTrue(listbranch.getValidationMsg().trim()
					.replaceAll("\\s", "")
					.equalsIgnoreCase(errormsg.trim().replaceAll("\\s", "")));
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
			log.info("TC36 Test Failed");
			log.info("Error while executing test case-TC36 " + t);
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
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
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
