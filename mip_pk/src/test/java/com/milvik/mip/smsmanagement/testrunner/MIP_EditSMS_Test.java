package com.milvik.mip.smsmanagement.testrunner;

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

import com.milvik.mip.constants.MIP_Menu_Constants;
import com.milvik.mip.dataprovider.MIP_SMSManagement_TestData;
import com.milvik.mip.dbqueries.MIP_SmsManagement_Queries;
import com.milvik.mip.listeners.MIP_RetryAnalyzer;
import com.milvik.mip.pageobjects.MIP_EditSMSPage;
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

public class MIP_EditSMS_Test {
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage = null;
	MIP_HomePage homepage = null;
	MIP_EditSMSPage editsmspage = null;

	@BeforeTest
	@Parameters({ "browser", "flag", "platform" })
	public void test_setup(@Optional("firefox") String browser,
			@Optional("0") String flag, @Optional("windows") String platform) {
		log = MIP_Logging.logDetails("MIP_EditSMS_Test");
		report = new ExtentReports("Test_Reports/MIP_EditSMS_Test.html");
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
			homepage.clickOnMenu(MIP_Menu_Constants.SMS);
			homepage.clickOnMenu(MIP_Menu_Constants.EDIT_SMS_TEMPLATE);

		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.clickOnMenu(MIP_Menu_Constants.SMS);
			homepage.clickOnMenu(MIP_Menu_Constants.EDIT_SMS_TEMPLATE);
		}
	}

	@Test(testName = "TC219")
	public void editSMSOne() throws Throwable {
		try {
			logger = report.startTest("Edit SMS Template-TC219");
			log.info("Running test case - TC219");
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Edit SMS Template')]"));
			editsmspage = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_EditSMSPage.class);
			Assert.assertTrue(editsmspage.templateName.isDisplayed());
			homepage.clickOnMenu(MIP_Menu_Constants.EDIT_SMS_TEMPLATE);
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.EDIT_SMS_TEMPLATE);
			log.info("Testcase TC219 Failed");
			log.info("Error occured in the test case TC219", t);
			throw t;
		}
	}

	@Test(testName = "TC220", dataProvider = "EditSMSTestData", dataProviderClass = MIP_SMSManagement_TestData.class)
	public void editSMSTwo(String... templateName) throws Throwable {
		try {
			logger = report.startTest("Edit SMS Template-TC220");
			log.info("Running test case - TC220");
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Edit SMS Template')]"));
			editsmspage = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_EditSMSPage.class);
			editsmspage.selectTemplateName(templateName[0]);
			Assert.assertTrue(editsmspage.validateEditSMSTemplate());
			homepage.clickOnMenu(MIP_Menu_Constants.EDIT_SMS_TEMPLATE);
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.EDIT_SMS_TEMPLATE);
			log.info("Testcase TC220 Failed");
			log.info("Error occured in the test case TC220", t);
			throw t;
		}
	}

	@Test(priority = 10, testName = "TC221 to TC223", dataProvider = "EditSMSTestData", dataProviderClass = MIP_SMSManagement_TestData.class, retryAnalyzer = MIP_RetryAnalyzer.class)
	public void editSMSThree(String templateName, String content,
			String validity, String message) throws Throwable {
		int flag = 0;
		try {
			logger = report.startTest("Edit SMS Template-TC221 to TC223");
			log.info("Running test case - TC221 to TC223");
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Edit SMS Template')]"));
			editsmspage = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_EditSMSPage.class);
			editsmspage.selectTemplateName(templateName);
			editsmspage.clearContent().enterContent(content).clearSmsValidity()
					.enterSmsValidity(validity).clikOnSave()
					.confirmEditSMS("yes");
			flag = 1;
			Assert.assertTrue(editsmspage.getSuccessMessage().trim()
					.contains(message.trim()));
			Map<String, String> editsms = MIP_SmsManagement_Queries
					.geteditSmsInfo(templateName);
			Assert.assertTrue(editsms.get("content").equalsIgnoreCase(content));
			Assert.assertTrue(editsms.get("validity")
					.equalsIgnoreCase(validity));
			homepage.clickOnMenu(MIP_Menu_Constants.SMS);
			homepage.clickOnMenu(MIP_Menu_Constants.EDIT_SMS_TEMPLATE);
		} catch (Throwable t) {
			if (flag == 1)
				homepage.clickOnMenu(MIP_Menu_Constants.SMS);
			homepage.clickOnMenu(MIP_Menu_Constants.EDIT_SMS_TEMPLATE);
			log.info("Testcase TC221 to TC223 Failed");
			log.info("Error occured in the test case TC221 to TC223", t);
			throw t;
		}
	}

	@Test(testName = "TC224", dataProvider = "EditSMSErrorMessage", dataProviderClass = MIP_SMSManagement_TestData.class)
	public void editSMSFour(String templateName, String content)
			throws Throwable {
		try {
			logger = report.startTest("Edit SMS Template-TC224");
			log.info("Running test case -TC224");
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Edit SMS Template')]"));
			editsmspage = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_EditSMSPage.class);
			editsmspage.waitForElementToVisible(By.id("smsTemplateId"));
			editsmspage.selectTemplateName(templateName);
			editsmspage.clearContent().enterContent(content).clickOnPreview();
			Assert.assertTrue(editsmspage.getPreviewContent().contains(content));
			editsmspage.clickOnPreviewOk();
			homepage.clickOnMenu(MIP_Menu_Constants.EDIT_SMS_TEMPLATE);
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.EDIT_SMS_TEMPLATE);
			log.info("Testcase TC224 Failed");
			log.info("Error occured in the test case TC224", t);
			throw t;
		}
	}

	@Test(testName = "TC225", dataProvider = "EditSMSContentErrorMessage", dataProviderClass = MIP_SMSManagement_TestData.class)
	public void editSMSFive(String templateName, String contenterror,
			String contentplaceholdererror) throws Throwable {
		try {
			logger = report.startTest("Edit SMS Template-TC225");
			log.info("Running test case -TC225");
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Edit SMS Template')]"));
			editsmspage = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_EditSMSPage.class);
			editsmspage.selectTemplateName(templateName);
			editsmspage.clearContent().clikOnSave();
			String errormsg = editsmspage.getvalidationMessage();
			Assert.assertTrue(errormsg.replaceAll("\\s", "").contains(
					contenterror.replaceAll("\\s", "")));
			if (MIP_SmsManagement_Queries.getplaceHolderCount(templateName) > 0) {
				Assert.assertTrue(errormsg.contains(contenterror));
			}
			homepage.clickOnMenu(MIP_Menu_Constants.EDIT_SMS_TEMPLATE);
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.EDIT_SMS_TEMPLATE);
			log.info("Testcase TC225 Failed");
			log.info("Error occured in the test case TC225", t);
			throw t;
		}
	}

	@Test(testName = "TC226-TC227", dataProvider = "EditSMSValidtyErrorMessage", dataProviderClass = MIP_SMSManagement_TestData.class)
	public void editSMSSix(String templateName, String validity,
			String validityrerror) throws Throwable {
		try {
			logger = report.startTest("Edit SMS Template-TC226-TC227");
			log.info("Running test case -TC226-TC227");
			editsmspage = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_EditSMSPage.class);
			editsmspage.selectTemplateName(templateName);
			editsmspage.clearSmsValidity().enterSmsValidity(validity)
					.clikOnSave();
			String errormsg = editsmspage.getvalidationMessage();
			Assert.assertTrue(errormsg
					.trim()
					.replaceAll("\\s", "")
					.equalsIgnoreCase(
							validityrerror.trim().replaceAll("\\s", "")));
			homepage.clickOnMenu(MIP_Menu_Constants.EDIT_SMS_TEMPLATE);
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.EDIT_SMS_TEMPLATE);
			log.info("Testcase TC226-TC227 Failed");
			log.info("Error occured in the test case TC226-TC227", t);
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
			homepage.clickOnMenu(MIP_Menu_Constants.EDIT_SMS_TEMPLATE);
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
			homepage.clickOnMenu(MIP_Menu_Constants.SMS);
		report.endTest(logger);
		report.flush();
	}

}
