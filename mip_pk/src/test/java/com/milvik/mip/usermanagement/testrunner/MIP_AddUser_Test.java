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
import com.milvik.mip.dataprovider.MIP_AddUser_TestData;
import com.milvik.mip.dbqueries.MIP_UserManagement_Queries;
import com.milvik.mip.dbqueries.MIP_AdminConfig_Queries;
import com.milvik.mip.pageobjects.MIP_AddUserPage;
import com.milvik.mip.pageobjects.MIP_HomePage;
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

public class MIP_AddUser_Test {
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage = null;
	MIP_HomePage homepage = null;
	MIP_AddUserPage adduserpage = null;
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
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_USER);

		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.clickOnMenu(MIP_Menu_Constants.USER);
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_USER);
		}
	}

	@Test(testName = "TC37 TC40", dataProvider = "addUserData", dataProviderClass = MIP_AddUser_TestData.class)
	public void addUserOne(String fname, String sname, String mobilenum,
			String email, String dob, String gender, String role,
			String branch, String is_supervisor, String supervisorname,
			String alert_enabled, String succmsg) throws Throwable {
		testname = "addUserOne---" + mobilenum;
		try {
			logger = report.startTest("Add User-TC37");
			log.info("Running test case - TC37");
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Add User')]"));
			adduserpage = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_AddUserPage.class);
			adduserpage.waitForElementToVisible(By.id("fname"));
			Assert.assertTrue(adduserpage.fname.isDisplayed());
			Assert.assertTrue(adduserpage.sname.isDisplayed());
			Assert.assertTrue(adduserpage.mobilenumber.isDisplayed());
			Assert.assertTrue(adduserpage.calenderIcon.isDisplayed());
			Assert.assertTrue(adduserpage.emailID.isDisplayed());
			Assert.assertTrue(adduserpage.gender_male.isDisplayed());
			Assert.assertTrue(adduserpage.gender_female.isDisplayed());
			Assert.assertTrue(adduserpage.role.isDisplayed());
			Assert.assertTrue(adduserpage.branch.isDisplayed());
			Assert.assertTrue(adduserpage.teamlead_yes.isDisplayed());
			Assert.assertTrue(adduserpage.teamlead_no.isDisplayed());
			Assert.assertTrue(adduserpage.save.isDisplayed());
			Assert.assertTrue(adduserpage.clear.isDisplayed());
			adduserpage.enterUserInfo(fname, sname, mobilenum, email, dob,
					gender, role, branch, is_supervisor, supervisorname,
					alert_enabled).clickOnClear();
			Assert.assertTrue(adduserpage.fname.getText().equals(""));
			Assert.assertTrue(adduserpage.sname.getText().equals(""));
			Assert.assertTrue(adduserpage.mobilenumber.getText().equals(""));
			Assert.assertTrue(adduserpage.dob.getText().equals(""));
			Assert.assertTrue(adduserpage.emailID.getText().equals(""));
			Assert.assertTrue(!adduserpage.gender_male.isSelected());
			Assert.assertTrue(!adduserpage.gender_female.isSelected());
			Assert.assertTrue(adduserpage.getSelectedOptions(adduserpage.role)
					.equals(""));
			Assert.assertTrue(adduserpage
					.getSelectedOptions(adduserpage.branch).equals(""));
			Assert.assertTrue(!adduserpage.teamlead_no.isSelected());
			Assert.assertTrue(!adduserpage.teamlead_yes.isSelected());
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.ADD_USER);
			log.info("Testcase TC37  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC38", dataProvider = "addUserData", dataProviderClass = MIP_AddUser_TestData.class)
	public void addUserPassTwo(String fname, String sname, String mobilenum,
			String email, String dob, String gender, String role,
			String branch, String is_supervisor, String supervisorname,
			String alert_enabled, String succmsg) throws Throwable {
		testname = "addUserTwo---" + mobilenum;
		try {
			logger = report.startTest("Add User-TC38");
			log.info("Running test case - TC38");

			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Add User')]"));
			adduserpage = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_AddUserPage.class);
			adduserpage.enterUserInfo(fname, sname, mobilenum, email, dob,
					gender, role, branch, is_supervisor, supervisorname,
					alert_enabled).clickOnSave();
			adduserpage.confirmUser("yes");
			Assert.assertTrue(adduserpage.getmessage().contains(succmsg));
			adduserpage.waitForElementToVisible(By.linkText("here")).click();
			Map<String, String> userDetails = MIP_UserManagement_Queries
					.getUserInfo(mobilenum);
			Assert.assertTrue(userDetails.get("fname").equalsIgnoreCase(fname));
			Assert.assertTrue(userDetails.get("sname").equalsIgnoreCase(sname));
			Assert.assertTrue(userDetails.get("gender")
					.equalsIgnoreCase(gender));
			Assert.assertTrue(userDetails.get("email_id").equalsIgnoreCase(
					email));

			Assert.assertTrue(userDetails.get("role_name").equalsIgnoreCase(
					role));
			Assert.assertTrue(userDetails.get("branch_name").equalsIgnoreCase(
					branch));

			Assert.assertTrue(userDetails.get("user_uid").contains(
					MIP_AdminConfig_Queries.getLoginPrefix().trim()));
			if (!dob.equals("")) {
				String[] date_data = MIP_DateFunctionality.getDate(dob,
						MIP_Constants.DOB_FORMAT);
				String date = userDetails.get("dob".trim());
				String date_format = date.replaceAll("-", "/");
				String[] date_bd = MIP_DateFunctionality.getDate(date_format,
						"yyyy/MM/dd");
				Assert.assertEquals(date_data[0], date_bd[2]);
				Assert.assertEquals(date_data[1], date_bd[1]);
				Assert.assertEquals(date_data[2], date_bd[0]);
			}

			Assert.assertTrue(userDetails.get("is_supervisor")
					.equalsIgnoreCase(is_supervisor));
			if (is_supervisor.equalsIgnoreCase("yes")) {
				Assert.assertTrue(userDetails.get("is_alert_enabled")
						.equalsIgnoreCase(alert_enabled));
			}
			if (is_supervisor.equalsIgnoreCase("no"))
				Assert.assertTrue(userDetails.get("supervisor_name")
						.equalsIgnoreCase(supervisorname));
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.ADD_USER);
			log.info("Testcase TC38  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC39-TC41 to TC47", dataProvider = "addUserNegativeData", dataProviderClass = MIP_AddUser_TestData.class)
	public void addUserPassThree(String testcase, String fname, String sname,
			String mobilenum, String email, String dob, String gender,
			String role, String branch, String is_teamlead,
			String supervisorname, String alert_ebabled, String errmsg)
			throws Throwable {
		testname = testcase;
		try {
			logger = report.startTest("Add User-TC39-TC41 to TC47");
			log.info("Running test case - " + testcase);
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Add User')]"));
			adduserpage = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_AddUserPage.class);
			adduserpage.enterUserInfo(fname, sname, mobilenum, email, dob,
					gender, role.trim(), branch.trim(), is_teamlead,
					supervisorname, alert_ebabled).clickOnSave();
			String errorMsg = adduserpage.getValidationMsg();
			adduserpage.clickOnClear();
			adduserpage.emailID.clear();
			log.info("Expected Error Message " + errmsg);
			log.info("Found Error Message " + errorMsg);
			Assert.assertTrue(errorMsg.trim().replaceAll("\\s", "")
					.equalsIgnoreCase(errmsg.trim().replaceAll("\\s", "")));
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.ADD_USER);
			log.info("Testcase" + testcase + " Failed");
			log.info("Error occured in the test case " + testcase, t);
			throw t;
		}
	}

	@AfterMethod(alwaysRun = true)
	public void after_test(ITestResult res) {

		if (res.getStatus() == ITestResult.FAILURE) {
			MIP_ScreenShots.takeScreenShot(MIP_Test_Configuration.driver,
					res.getName() + "_" + testname);
			logger.log(LogStatus.FAIL, "Test Failed");
			logger.log(LogStatus.ERROR, res.getThrowable());
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_USER);
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
