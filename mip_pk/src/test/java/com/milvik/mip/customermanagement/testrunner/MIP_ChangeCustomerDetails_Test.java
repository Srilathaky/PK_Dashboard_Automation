package com.milvik.mip.customermanagement.testrunner;

import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
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
import com.milvik.mip.dataprovider.MIP_ChangeCustomerDetails_TestData;
import com.milvik.mip.dbqueries.MIP_ChangeCustomerDetail_Queries;
import com.milvik.mip.dbqueries.MIP_RegisterCustomer_Queries;
import com.milvik.mip.pageobjects.MIP_ChangeCustomerDetailsPage;
import com.milvik.mip.pageobjects.MIP_ChangeHistoryDetailsPage;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.pageobjects.MIP_RegisterCustomerPage;
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

public class MIP_ChangeCustomerDetails_Test {
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage;
	MIP_HomePage homepage;
	MIP_ChangeCustomerDetailsPage changecustdetails = null;
	String testcaseName;
	MIP_RegisterCustomerPage regpage = null;
	String username;
	String testcase_name = "";

	@BeforeTest
	@Parameters({ "flag", "browser", "platform" })
	public void test_setup(@Optional("firefox") String browser,
			@Optional("0") String flag, @Optional("windows") String platform) {
		log = MIP_Logging.logDetails("MIP_ChangeCustomerDetails_Test");
		report = new ExtentReports(
				"Test_Reports/MIP_ChangeCustomerDetails_Test.html");
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
			username = MIP_ReadPropertyFile.getPropertyValue("username");
			homepage = loginpage.login(username,
					MIP_ReadPropertyFile.getPropertyValue("password"));
			homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_CUSTOMER_DETAILS);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			try {
				homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_CUSTOMER_DETAILS);
			} catch (ElementNotVisibleException e) {
				homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
				homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_CUSTOMER_DETAILS);
			}

		}

	}

	@Test(priority = 0, testName = "validatechangecustomerDetailsPage")
	public void changeCustDetailsOne() throws Throwable {
		testcase_name = "validateChangeCustomerPage";
		try {
			logger = report
					.startTest("Change Customer-validateChangeCustomerPage");
			log.info("Running the test case validatechangecustomerDetailsPage");

			changecustdetails = PageFactory.initElements(
					MIP_Test_Configuration.driver,
					MIP_ChangeCustomerDetailsPage.class);
			try {
				homepage.waitForElementToVisible(By
						.xpath("//h3[contains(text(),'Change Customer Details')]"));
			} catch (Exception e) {
				homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_CUSTOMER_DETAILS);
			}
			changecustdetails.waitForElementToVisible(By.id("msisdn"));
			Assert.assertTrue(changecustdetails.msisdn.isDisplayed());
			Assert.assertTrue(changecustdetails.search_icon.isDisplayed());

		} catch (Throwable t) {
			log.info("validatechangecustomerDetailsPage Test Failed");
			log.info(
					"Error occured in the test case validatechangecustomerDetailsPage",
					t);
			throw t;
		}
	}

	@Test(priority = 1, testName = "changecustdetails", dataProvider = "chagneCustDetails", dataProviderClass = MIP_ChangeCustomerDetails_TestData.class)
	public void changeCustDetails(String testcase, String msisdn,
			String cust_fname, String cust_sname, String cust_age,
			String cust_gender, String ben_relation, String ben_fname,
			String ben_sname, String ben_age, String guardian_name,
			String guardian_mobile) throws Throwable {
		testcase_name = "changecustdetails_" + msisdn;
		try {
			logger = report.startTest("changeCustDetails__" + msisdn);
			log.info("Running the test case changeCustDetails");

			changecustdetails = PageFactory.initElements(
					MIP_Test_Configuration.driver,
					MIP_ChangeCustomerDetailsPage.class);
			try {
				homepage.waitForElementToVisible(By
						.xpath("//h3[contains(text(),'Change Customer Details')]"));
			} catch (Exception e) {
				homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_CUSTOMER_DETAILS);
			}
			changecustdetails.waitForElementToVisible(By.id("msisdn"));
			username = MIP_ReadPropertyFile.getPropertyValue("username");

			changecustdetails.enterMSISDN(msisdn);
			changecustdetails.clickOnSeachIcon();
			changecustdetails
					.changeCustomerDetails(cust_fname, cust_sname, cust_age,
							cust_gender)
					.changeBeneficiaryDetails(ben_fname, ben_sname, ben_age,
							ben_relation, guardian_name, guardian_mobile)
					.clickOnSave();
			changecustdetails.confirmCustManagementPopup("yes");
			changecustdetails.waitForElementToVisible(By.linkText("here"))
					.click();
			changecustdetails.enterMSISDN(msisdn);
			changecustdetails.clickOnSeachIcon();
			Map<String, String> dashboard_cust_details = changecustdetails
					.getCustomerInfo();
			Map<String, String> dashboard_ben_details = changecustdetails
					.getBeneficiaryInfo();
			Map<String, String> db_cust_details = MIP_RegisterCustomer_Queries
					.getCustomerDetails(msisdn);
			if (!cust_fname.equals("")) {
				log.info("Dashboard,DB and Input cust_fname Details:"
						+ " Dashboard="
						+ dashboard_cust_details.get("cust_fname") + " DB="
						+ db_cust_details.get("cust_fname") + " Input="
						+ cust_fname);
				Assert.assertEquals(dashboard_cust_details.get("cust_fname")
						.trim(), db_cust_details.get("cust_fname").trim());
				Assert.assertEquals(dashboard_cust_details.get("cust_fname")
						.trim(), cust_fname.trim());
			}
			if (!cust_sname.equals("")) {
				log.info("Dashboard,DB and Input cust_sname Details:"
						+ " Dashboard="
						+ dashboard_cust_details.get("cust_sname") + " DB="
						+ db_cust_details.get("cust_sname") + " Input="
						+ cust_sname);
				Assert.assertEquals(dashboard_cust_details.get("cust_sname")
						.trim(), db_cust_details.get("cust_sname").trim());
				Assert.assertEquals(dashboard_cust_details.get("cust_sname")
						.trim(), cust_sname.trim());
			}
			if (!cust_age.equals("")) {
				log.info("Dashboard,DB and Input cust_age Details:"
						+ " Dashboard="
						+ dashboard_cust_details.get("cust_age") + " DB="
						+ db_cust_details.get("cust_age") + " Input="
						+ cust_age);
				Assert.assertEquals(dashboard_cust_details.get("cust_age")
						.trim(), db_cust_details.get("cust_age").trim());
				Assert.assertEquals(dashboard_cust_details.get("cust_age")
						.trim(), cust_age.trim());
			}
			if (!cust_gender.equals("")) {
				log.info("Dashboard,DB and Input cust_gender Details:"
						+ " Dashboard="
						+ dashboard_cust_details.get("cust_gender") + " DB="
						+ db_cust_details.get("cust_gender") + " Input="
						+ cust_gender);
				Assert.assertEquals(dashboard_cust_details.get("cust_gender")
						.trim(), db_cust_details.get("cust_gender").trim());
				Assert.assertEquals(dashboard_cust_details.get("cust_gender")
						.trim(), cust_gender.trim());
			}
			if (!ben_fname.equals("")) {
				log.info("Dashboard,DB and Input ben_fname Details:"
						+ " Dashboard="
						+ dashboard_ben_details.get("ben_fname") + " DB="
						+ db_cust_details.get("ben_fname") + " Input="
						+ ben_fname);
				Assert.assertEquals(dashboard_ben_details.get("ben_fname")
						.trim(), db_cust_details.get("ben_fname").trim());
				Assert.assertEquals(dashboard_ben_details.get("ben_fname")
						.trim(), ben_fname.trim());
			}
			if (!ben_sname.equals("")) {
				log.info("Dashboard,DB and Input ben_sname Details:"
						+ " Dashboard="
						+ dashboard_ben_details.get("ben_sname") + " DB="
						+ db_cust_details.get("ben_sname") + " Input="
						+ ben_sname);
				Assert.assertEquals(dashboard_ben_details.get("ben_sname")
						.trim(), db_cust_details.get("ben_sname").trim());
				Assert.assertEquals(dashboard_ben_details.get("ben_sname")
						.trim(), ben_sname.trim());
			}
			if (!ben_age.equals("")) {
				log.info("Dashboard,DB and Input ben_age Details:"
						+ " Dashboard=" + dashboard_ben_details.get("ben_age")
						+ " DB=" + db_cust_details.get("ben_age") + " Input="
						+ ben_age);
				Assert.assertEquals(
						dashboard_ben_details.get("ben_age").trim(),
						db_cust_details.get("ben_age").trim());
				Assert.assertEquals(
						dashboard_ben_details.get("ben_age").trim(),
						ben_age.trim());
			}
			if (!ben_relation.equals("")) {
				log.info("Dashboard,DB and Input ben_relaionship Details:"
						+ " Dashboard="
						+ dashboard_ben_details.get("ben_relationship")
						+ " DB=" + db_cust_details.get("cust_relationship")
						+ " Input=" + ben_relation);
				Assert.assertEquals(
						dashboard_ben_details.get("ben_relationship").trim(),
						db_cust_details.get("cust_relationship").trim());
				Assert.assertEquals(
						dashboard_ben_details.get("ben_relationship").trim(),
						ben_relation.trim());
			}
			if (!guardian_name.equals("")) {
				log.info("Expected and found guardian name:"
						+ "DB guardian name=" + db_cust_details.get("lg_name")
						+ " and " + "Input guardian name=" + guardian_name);
				Assert.assertEquals(db_cust_details.get("lg_name").trim(),
						guardian_name.trim());
				Assert.assertEquals(
						dashboard_ben_details.get("lg_name").trim(),
						guardian_name.trim());
			}
			if (!guardian_mobile.equals("")) {
				log.info("Expected and found guardian phone number:"
						+ "DB guardian phone number="
						+ db_cust_details.get("lg_msisdn") + " and "
						+ "Input guardian phone number=" + guardian_mobile);
				Assert.assertEquals(db_cust_details.get("lg_msisdn").trim(),
						guardian_mobile.trim());
				Assert.assertEquals(dashboard_ben_details.get("lg_msisdn")
						.trim(), guardian_mobile.trim());
			}
			homepage.clickOnMenu(MIP_Menu_Constants.DETAILS_CHANGE_HISTORY);
			MIP_ChangeHistoryDetailsPage changehistpage = new MIP_ChangeHistoryDetailsPage(
					MIP_Test_Configuration.driver);
			changecustdetails.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Details Change History')]"));
			changecustdetails.enterMSISDN(msisdn);
			changecustdetails.clickOnSeachIcon();
			changehistpage.validateChangesPerformedTableHeading();
			Map<String, String> old_cust_details = MIP_ChangeCustomerDetail_Queries
					.getchangeCustomerDetails(msisdn);
			Map<String, String> dashboard_cahnge_details = changehistpage
					.validateChangesPerformed();
			String curr_date = MIP_DateFunctionality
					.getCurrentDate("dd/MM/YYYY");

			Assert.assertTrue(dashboard_cahnge_details.get("Date").contains(
					curr_date));
			Assert.assertTrue(dashboard_cahnge_details.get("Modified By")
					.toUpperCase().contains(username.toUpperCase()));
			log.info("Change Details old cust_fname Details:" + " Dashboard="
					+ dashboard_cahnge_details.get("Old Customer Name")
					+ " DB=" + old_cust_details.get("old_fname"));
			Assert.assertEquals(
					dashboard_cahnge_details.get("Old Customer Name").trim(),
					old_cust_details.get("old_fname").trim());
			log.info("Change Details new cust_fname Details:" + " Dashboard="
					+ dashboard_cahnge_details.get("New Customer Name")
					+ " DB=" + old_cust_details.get("new_fname").trim());
			Assert.assertEquals(
					dashboard_cahnge_details.get("New Customer Name").trim(),
					old_cust_details.get("new_fname").trim());

			log.info("Change Details old cust_sname Details:" + " Dashboard="
					+ dashboard_cahnge_details.get("Old Customer Surname")
					+ " DB=" + old_cust_details.get("old_sname"));
			Assert.assertEquals(
					dashboard_cahnge_details.get("Old Customer Surname").trim(),
					old_cust_details.get("old_sname").trim());
			log.info("Change Details new cust_sname Details:" + " Dashboard="
					+ dashboard_cahnge_details.get("New Customer Surname")
					+ " DB=" + old_cust_details.get("new_sname").trim());
			Assert.assertEquals(
					dashboard_cahnge_details.get("New Customer Surname").trim(),
					old_cust_details.get("new_sname").trim());

			log.info("Change Details old cust_age Details:" + " Dashboard="
					+ dashboard_cahnge_details.get("Old Age") + " DB="
					+ old_cust_details.get("old_age"));
			Assert.assertEquals(dashboard_cahnge_details.get("Old Age").trim(),
					old_cust_details.get("old_age").trim());
			log.info("Change Details new cust_age Details:" + " Dashboard="
					+ dashboard_cahnge_details.get("New Age") + " DB="
					+ old_cust_details.get("new_age").trim());
			Assert.assertEquals(dashboard_cahnge_details.get("New Age").trim(),
					old_cust_details.get("new_age").trim());

			log.info("Change Details old cust_gender Details:" + " Dashboard="
					+ dashboard_cahnge_details.get("Old Gender") + " DB="
					+ old_cust_details.get("old_gender"));
			Assert.assertEquals(dashboard_cahnge_details.get("Old Gender")
					.trim(), old_cust_details.get("old_gender").trim());
			log.info("Change Details new cust_gender Details:" + " Dashboard="
					+ dashboard_cahnge_details.get("New Gender") + " DB="
					+ old_cust_details.get("new_gender").trim());
			Assert.assertEquals(dashboard_cahnge_details.get("New Gender")
					.trim(), old_cust_details.get("new_gender").trim());

			log.info("Change Details old ben_fname Details:" + " Dashboard="
					+ dashboard_cahnge_details.get("Old Beneficiary Name")
					+ " DB=" + old_cust_details.get("old_ben_fname"));
			Assert.assertEquals(
					dashboard_cahnge_details.get("Old Beneficiary Name").trim(),
					old_cust_details.get("old_ben_fname").trim());
			log.info("Change Details new cust_gender Details:" + " Dashboard="
					+ dashboard_cahnge_details.get("New Beneficiary Name")
					+ " DB=" + old_cust_details.get("new_ben_fname"));
			Assert.assertEquals(
					dashboard_cahnge_details.get("New Beneficiary Name").trim(),
					old_cust_details.get("new_ben_fname"));

			log.info("Change Details old ben_sname Details:" + " Dashboard="
					+ dashboard_cahnge_details.get("Old Beneficiary Surname")
					+ " DB=" + old_cust_details.get("old_ben_sname"));
			Assert.assertEquals(
					dashboard_cahnge_details.get("Old Beneficiary Surname")
							.trim(), old_cust_details.get("old_ben_sname")
							.trim());
			log.info("Change Details new cust_gender Details:" + " Dashboard="
					+ dashboard_cahnge_details.get("New Beneficiary Surname")
					+ " DB=" + old_cust_details.get("new_ben_sname"));
			Assert.assertEquals(
					dashboard_cahnge_details.get("New Beneficiary Surname")
							.trim(), old_cust_details.get("new_ben_sname"));

			log.info("Change Details old ben_age Details:" + " Dashboard="
					+ dashboard_cahnge_details.get("Old Beneficiary Age")
					+ " DB=" + old_cust_details.get("old_ben_age"));
			Assert.assertEquals(
					dashboard_cahnge_details.get("Old Beneficiary Age").trim(),
					old_cust_details.get("old_ben_age").trim());
			log.info("Change Details new cust_gender Details:" + " Dashboard="
					+ dashboard_cahnge_details.get("New Beneficiary Age")
					+ " DB=" + old_cust_details.get("new_ben_age"));
			Assert.assertEquals(
					dashboard_cahnge_details.get("New Beneficiary Age").trim(),
					old_cust_details.get("new_ben_age"));

			log.info("Change Details old ben_age Details:"
					+ " Dashboard="
					+ dashboard_cahnge_details
							.get("Old Beneficiary Relationship") + " DB="
					+ old_cust_details.get("old_cust_relationship"));
			Assert.assertEquals(
					dashboard_cahnge_details
							.get("Old Beneficiary Relationship").trim(),
					old_cust_details.get("old_cust_relationship").trim());
			log.info("Change Details new cust_gender Details:"
					+ " Dashboard="
					+ dashboard_cahnge_details
							.get("New Beneficiary Relationship") + " DB="
					+ old_cust_details.get("new_cust_relationship"));
			Assert.assertEquals(
					dashboard_cahnge_details
							.get("New Beneficiary Relationship").trim(),
					old_cust_details.get("new_cust_relationship").trim());

			log.info("Change Details old guardian name Details:"
					+ " Dashboard="
					+ dashboard_cahnge_details.get("Old Legal Guardian Name")
					+ " DB=" + old_cust_details.get("old_guardian_name"));
			Assert.assertEquals(
					dashboard_cahnge_details.get("Old Legal Guardian Name")
							.trim(), old_cust_details.get("old_guardian_name")
							.trim());
			log.info("Change Details new cust_gender Details:" + " Dashboard="
					+ dashboard_cahnge_details.get("New Legal Guardian Name")
					+ " DB=" + old_cust_details.get("new_guardian_name"));
			Assert.assertEquals(
					dashboard_cahnge_details.get("New Legal Guardian Name")
							.trim(), old_cust_details.get("new_guardian_name")
							.trim());
			log.info("Change Details old ben_age Details:" + " Dashboard="
					+ dashboard_cahnge_details.get("Old Legal Guardian MSISDN")
					+ " DB=" + old_cust_details.get("old_guardian_msisdn"));
			Assert.assertEquals(
					dashboard_cahnge_details.get("Old Legal Guardian MSISDN")
							.trim(), old_cust_details
							.get("old_guardian_msisdn").trim());
			log.info("Change Details new cust_gender Details:" + " Dashboard="
					+ dashboard_cahnge_details.get("New Legal Guardian MSISDN")
					+ " DB=" + old_cust_details.get("new_guardian_msisdn"));
			Assert.assertEquals(
					dashboard_cahnge_details.get("New Legal Guardian MSISDN")
							.trim(), old_cust_details
							.get("new_guardian_msisdn").trim());

			homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_CUSTOMER_DETAILS);

		} catch (Throwable t) {
			log.info("changeCustDetails Test Failed");
			log.info("Error occured in the test case changeCustDetails", t);
			throw t;
		}
	}

	@AfterMethod(alwaysRun = true)
	public void after_test(ITestResult res) {

		if (res.getStatus() == ITestResult.FAILURE) {
			String path = MIP_ScreenShots.takeScreenShot(
					MIP_Test_Configuration.driver, res.getName() + "_"
							+ testcase_name);
			logger.log(LogStatus.FAIL, "Test Failed");
			logger.log(LogStatus.ERROR, res.getThrowable());
			logger.log(LogStatus.FAIL, logger.addScreenCapture(path));
			homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_CUSTOMER_DETAILS);
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
			homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
		report.endTest(logger);
		report.flush();

	}
}
