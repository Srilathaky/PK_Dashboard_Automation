package com.milvik.mip.usermanagement.testrunner;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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
import com.milvik.mip.dataprovider.MIP_SearchUser_TestData;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.pageobjects.MIP_SearchUserPage;
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

public class MIP_SearchUser_Test {
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage = null;
	MIP_HomePage homepage = null;
	MIP_SearchUserPage searchuser = null;
	String testname;

	@BeforeTest
	@Parameters({ "browser", "flag", "platform" })
	public void test_setup(@Optional("firefox") String browser,
			@Optional("0") String flag, @Optional("windows") String platform) {
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
			homepage.clickOnMenu(MIP_Menu_Constants.SEARCH_USER);

		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.clickOnMenu(MIP_Menu_Constants.USER);
			homepage.clickOnMenu(MIP_Menu_Constants.SEARCH_USER);
		}
	}

	@Test(testName = "TC52")
	public void searchUserOne() throws Throwable {
		testname = "searchUserOne";
		try {
			logger = report.startTest("List User-TC52");
			log.info("Running test case - TC52");
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Search User')]"));
			searchuser = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_SearchUserPage.class);
			Assert.assertTrue(searchuser.fnmae.isDisplayed());
			Assert.assertTrue(searchuser.snmae.isDisplayed());
			Assert.assertTrue(searchuser.msisdn.isDisplayed());
			Assert.assertTrue(searchuser.role.isDisplayed());
			Assert.assertTrue(searchuser.searchbtn.isDisplayed());
			Assert.assertTrue(searchuser.clearBtn.isDisplayed());
		} catch (Throwable t) {
			log.info("Testcase TC52  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC53 to TC57", dataProvider = "searchUserDataPositive", dataProviderClass = MIP_SearchUser_TestData.class)
	public void searchUserTwo(String testcase, String fname, String sname,
			String msisdn, String role) throws Throwable {
		testname = testcase;
		try {
			logger = report.startTest("Search User-TC53 to TC57");
			log.info("Running test case - " + testcase);
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Search User')]"));
			searchuser = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_SearchUserPage.class);
			searchuser.enterSearchCriteria(fname, sname, msisdn, role)
					.clickOnSearch();
			boolean val1 = false;
			boolean val2 = false;
			if ((!fname.equals("")) || (!sname.equals(""))) {
				List<WebElement> namelist = searchuser.getUserName();
				for (int i = 0; i < namelist.size(); i++) {
					if (!fname.equals("")) {
						val1 = namelist.get(i).getText().toUpperCase()
								.contains(fname.toUpperCase().trim());
						Assert.assertTrue(val1);
					}
					if (!sname.equals("")) {
						if (namelist.get(i).getText().contains("...")) {
							(new Actions(MIP_Test_Configuration.driver))
									.moveToElement(namelist.get(i)).build()
									.perform();
							String text = namelist.get(i).getAttribute("title");
							val2 = text.toUpperCase().contains(
									sname.toUpperCase().trim());
						} else {
							val2 = namelist.get(i).getText().toUpperCase()
									.contains(sname.toUpperCase().trim());
						}
						Assert.assertTrue(val2);

					}
					if (val1 == false && val2 == false) {
						Assert.assertTrue(false);
					}
				}
			}
			if (!msisdn.equals("")) {
				Assert.assertTrue(searchuser.getMsisdn().getText()
						.equalsIgnoreCase(msisdn.trim()));
			}
			if (!role.equals("")) {
				List<WebElement> rolelist = searchuser.getRole();
				for (int i = 0; i < rolelist.size(); i++) {
					Assert.assertTrue(rolelist.get(i).getText()
							.contains(role.trim()));
				}
			}

		} catch (Throwable t) {
			log.info("Testcase " + testcase + " Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC58", dataProvider = "SearcUserTestData", dataProviderClass = MIP_SearchUser_TestData.class)
	public void searchUserThree(String fname, String sname, String msisdn,
			String userid, String role) throws Throwable {
		testname = msisdn;
		try {
			logger = report.startTest("List User-TC58");
			log.info("Running test case - TC58--" + msisdn);
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Search User')]"));
			searchuser = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_SearchUserPage.class);
			searchuser.enterSearchCriteria(fname, sname, msisdn, role)
					.clickOnClear();
			Assert.assertTrue(searchuser.fnmae.getText().equals(""));
			Assert.assertTrue(searchuser.snmae.getText().equals(""));
			Assert.assertTrue(searchuser.msisdn.getText().equals(""));
			Assert.assertTrue(searchuser.getRoleDropDownValue().equals(""));
		} catch (Throwable t) {
			log.info("Testcase TC58  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC59 to TC60", dataProvider = "searchUserDataNegative", dataProviderClass = MIP_SearchUser_TestData.class)
	public void searchUserFour(String testacse, String fname, String sname,
			String msisdn, String errmsg) throws Throwable {
		testname = testacse;
		try {
			logger = report.startTest("List User-TC59 to TC60");
			log.info("Running test case - " + testacse + "--" + msisdn);
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Search User')]"));
			searchuser = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_SearchUserPage.class);
			searchuser.enterSearchCriteria(fname, sname, msisdn, "")
					.clickOnSearch();

			Assert.assertTrue(searchuser.getResultMsg().trim()
					.equalsIgnoreCase(errmsg.trim()));

			searchuser.clickOnClear();
		} catch (Throwable t) {
			log.info("Testcase TC59 -TC60  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC61")
	public void searchUserFive() throws Throwable {
		testname = "searchUserFive";
		try {
			logger = report.startTest("List User-TC61");
			log.info("Running test case - TC61");
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Search User')]"));
			searchuser = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_SearchUserPage.class);
			searchuser.clickOnSearch();
			Assert.assertTrue(searchuser
					.getValidationMsg()
					.trim()
					.replaceAll("\\s", "")
					.equalsIgnoreCase(
							MIP_SearchUser_TestData.withoutsearchcriteria
									.trim().replaceAll("\\s", "")));
		} catch (Throwable t) {
			log.info("Testcase TC61  Failed");
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
