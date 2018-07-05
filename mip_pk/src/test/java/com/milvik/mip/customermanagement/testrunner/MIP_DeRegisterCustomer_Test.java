package com.milvik.mip.customermanagement.testrunner;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.milvik.mip.constants.MIP_Menu_Constants;
import com.milvik.mip.dataprovider.MIP_CustomerManagement_TestData;
import com.milvik.mip.dataprovider.MIP_DeRegisterCustomer_TestData;
import com.milvik.mip.dataprovider.MIP_RegisterCustomer_TestData;
import com.milvik.mip.dbqueries.MIP_DeRegisterCustomer_Queries;
import com.milvik.mip.dbqueries.MIP_RegisterCustomer_Queries;
import com.milvik.mip.pageobjects.MIP_DeRegisterCustomerPage;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.pageobjects.MIP_QAConfirmationPage;
import com.milvik.mip.pageobjects.MIP_RegisterCustomerPage;
import com.milvik.mip.pageutil.MIP_CustomerManagementPage;
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

public class MIP_DeRegisterCustomer_Test {
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage;
	MIP_HomePage homepage;
	MIP_DeRegisterCustomerPage deregcust = null;
	MIP_QAConfirmationPage qaconfpage = null;
	String testcaseName;
	MIP_RegisterCustomerPage regpage = null;
	String username;
	String testcase_name = "";
	String platform_ = "";

	@BeforeTest
	@Parameters({ "flag", "browser", "platform" })
	public void test_setup(@Optional("firefox") String browser,
			@Optional("0") String flag, @Optional("windows") String platform) {
		platform_ = platform;
		log = MIP_Logging.logDetails("MIP_DeRegisterCustomer_Test");
		report = new ExtentReports(
				"Test_Reports/MIP_DeRegisterCustomer_Test.html");
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
			WebDriverWait w = new WebDriverWait(MIP_Test_Configuration.driver,
					600);
			w.until(ExpectedConditions.visibilityOfElementLocated((By
					.linkText(MIP_Menu_Constants.CUSTOMER))));
			homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.DE_REGISTER_CUSTOMER);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			WebDriverWait w = new WebDriverWait(MIP_Test_Configuration.driver,
					600);
			w.until(ExpectedConditions.visibilityOfElementLocated((By
					.linkText(MIP_Menu_Constants.CUSTOMER))));
			homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.DE_REGISTER_CUSTOMER);

		}

	}

	@Test(priority = 0, testName = "validateDeregisterPage")
	public void deRegCustOne() throws Throwable {
		testcase_name = "validateDeregisterPage";
		try {
			logger = report
					.startTest("DeRegister Customer-validateDeregisterPage");
			log.info("Running the test case validateDeregisterPage");

			deregcust = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_DeRegisterCustomerPage.class);

			deregcust.waitForElementToVisible(By.id("msisdn"));
			Assert.assertTrue(deregcust.msisdn.isDisplayed());
			Assert.assertTrue(deregcust.search_icon.isDisplayed());

			Assert.assertTrue(deregcust
					.waitForElementToVisible(By.id("slickbox"))
					.getText()
					.equalsIgnoreCase(
							"This will de-register the customer from the product. To avail the service again, customer has to register again for the product."));
		} catch (Throwable t) {
			log.info("validateDeregisterPage Test Failed");
			log.info("Error occured in the test case validateDeregisterPage", t);
			throw t;
		}
	}

	@Test(priority = 1, testName = "nic_msisdn_validation", dataProvider = "deRegisterNegativeTest", dataProviderClass = MIP_CustomerManagement_TestData.class)
	public void deRegCustTwo(String testname, String msisdn, String errormsg)
			throws Throwable {
		testcase_name = testname;

		try {
			logger = report
					.startTest("DeRegister Customer-nic_msisdn_validation");
			log.info("Running the test case nic_msisdn_validation");
			deregcust = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_DeRegisterCustomerPage.class);
			deregcust.waitForElementToVisible(By.id("msisdn"));

			deregcust.enterMSISDN(msisdn);
			deregcust.clickOnSeachIcon();
			Assert.assertTrue(deregcust.getValidationMessage()
					.equalsIgnoreCase(errormsg.trim()));

			deregcust.msisdn.clear();
		} catch (Throwable t) {
			log.info("nic_msisdn_validation Test Failed");
			log.info("Error occured in the test case nic_msisdn_validation", t);
			throw t;
		}
	}

	@Test(enabled = true, testName = "DeRegisterCustomer", dataProvider = "deRegisterCustomer", dataProviderClass = MIP_DeRegisterCustomer_TestData.class)
	public void deRegCustomer(String testcase, String mno, String msisdn,
			String prepostStatus, String prd_to_register,
			String accident_level, String cust_fname, String cust_sname,
			String age, String cust_cni, String dob, String gender,
			String relationship, String ben_fname, String ben_sname,
			String ben_age, String ben_msisdn, String ben_gender,
			String success_message, String prd_dereg, String conf_status,
			String reactivate_status, String prd_reactivate,
			String reactivate_accident_level,
			String is_confirm_after_reactivaton) throws Throwable {
		testcaseName = testcase + "--" + msisdn;
		String bima_accident_ben_level = "";
		try {
			username = MIP_ReadPropertyFile.getPropertyValue("username");
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
			logger = report.startTest("De-RegisterCustomer--" + testcase + "--"
					+ msisdn);
			regpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_RegisterCustomerPage.class);
			regpage.waitForElementToVisible(By
					.xpath("//h3[normalize-space(text())='Register Customer']"));
			regpage.waitForElementToVisible(By
					.xpath("//div[contains(text(),'All fields marked')]"));
			logger.log(LogStatus.INFO, "Registering the customer");
			if (mno.equalsIgnoreCase(MIP_CustomerManagementPage.BIMA_WARID))
				MIP_RegisterCustomer_Queries.connectToWaridDBAndInserRecord(
						platform_, cust_fname, msisdn, prepostStatus, cust_cni);
			Thread.sleep(1000);
			regpage.enterMSISDN(msisdn);
			regpage.clickOnSeachIcon();
			regpage.waitForElementToVisible(By.id("saveBtn"));

			regpage.enterCustomerInfo(mno, cust_fname, cust_sname, age,
					cust_cni, dob, gender);
			Thread.sleep(1000);
			regpage.selectAvailableInformation(prd_to_register);
			if (MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION
					.contains(prd_to_register))
				regpage.selectBimaAccidentProtecionBenefitLevel(accident_level);

			regpage.enterBeneficiaryInfo(relationship, ben_fname, ben_sname,
					ben_age, ben_msisdn, ben_gender).clickOnSave()
					.confirmPopUp("yes");

			Assert.assertTrue(regpage.getSuccessMsg().replaceAll("\\s", "")
					.equalsIgnoreCase(success_message.replaceAll("\\s", "")));

			Map<String, String> db_cust_details = MIP_RegisterCustomer_Queries
					.getCustomerDetails(msisdn);

			log.info("Expected and found cust fname:" + "DB Cust Fname="
					+ db_cust_details.get("cust_fname") + " and "
					+ "Input Cust Fname=" + cust_fname);
			Assert.assertEquals(db_cust_details.get("cust_fname").trim(),
					cust_fname.trim());
			log.info("Expected and found cust_sname:" + "DB cust_sname="
					+ db_cust_details.get("cust_sname") + " and "
					+ "Input cust_sname=" + cust_sname);
			Assert.assertEquals(db_cust_details.get("cust_sname").trim(),
					cust_sname.trim());
			if (!age.equals("")) {
				log.info("Expected and found  cust_age:" + "DB cust_age="
						+ db_cust_details.get("cust_age") + " and "
						+ "Input cust_age=" + age);
				Assert.assertEquals(db_cust_details.get("cust_age").trim(),
						age.trim());
			}
			if (!dob.equals("")) {
				log.info("Expected and found  cust_dob:" + "DB cust_dob="
						+ db_cust_details.get("cust_dob") + " and "
						+ "Input cust_dob=" + dob);

				Assert.assertEquals(db_cust_details.get("cust_dob").trim(),
						dob.trim());

			}
			log.info("Expected and found cust cust_gender:" + "DB cust_gender="
					+ db_cust_details.get("cust_gender") + " and "
					+ "Input cust_gender=" + gender);
			Assert.assertEquals(db_cust_details.get("cust_gender").trim(),
					gender.trim());
			log.info("Expected and found ben_fname:" + "DB ben_fname="
					+ db_cust_details.get("ben_fname") + " and " + "ben_fname="
					+ ben_fname);
			Assert.assertEquals(db_cust_details.get("ben_fname").trim(),
					ben_fname.trim());
			log.info("Expected and found ben_sname:" + "DB ben_sname="
					+ db_cust_details.get("ben_sname") + " and "
					+ "Input ben_sname=" + ben_sname);
			Assert.assertEquals(db_cust_details.get("ben_sname").trim(),
					ben_sname.trim());
			log.info("Expected and found ben_age:" + "DB ben_age="
					+ db_cust_details.get("ben_age") + " and "
					+ "Input ben_age=" + ben_age);
			Assert.assertEquals(db_cust_details.get("ben_age").trim(),
					ben_age.trim());
			log.info("Expected and found cust_relationship:"
					+ "DB cust_relationship="
					+ db_cust_details.get("cust_relationship") + " and "
					+ "Input cust_relationship=" + relationship);
			Assert.assertEquals(
					db_cust_details.get("cust_relationship").trim(),
					relationship.trim());
			log.info("Expected and found ben_gender:" + "DB ben_gender ="
					+ db_cust_details.get("ben_gender") + " and "
					+ "Input ben_gender=" + ben_gender);
			Assert.assertEquals(db_cust_details.get("ben_gender").trim(),
					ben_gender.trim());

			log.info("Expected and found ben_msisdn:" + "DB ben_msisdn ="
					+ db_cust_details.get("ben_msisdn") + " and "
					+ "Input ben_msisdn=" + ben_msisdn);
			Assert.assertEquals(db_cust_details.get("ben_msisdn").trim(),
					ben_msisdn.trim());

			if (!cust_cni.equals("")) {
				log.info("Expected and found NIC:" + "DB NIC number="
						+ db_cust_details.get("cust_cnic") + " and "
						+ "Input cust_NIC=" + cust_cni);
				Assert.assertEquals(db_cust_details.get("cust_cnic").trim(),
						cust_cni.trim());
				if (mno.equalsIgnoreCase(MIP_CustomerManagementPage.BIMA_WARID)) {
					log.info("Expected and found Warid NIC:"
							+ "DB Warid NIC number="
							+ db_cust_details.get("w_cnic") + " and "
							+ "Input Warid_NIC=" + cust_cni);
					Assert.assertEquals(db_cust_details.get("w_cnic").trim(),
							cust_cni.trim());
				}

			}
			log.info("Expected and found Created By:" + "DB Created By="
					+ db_cust_details.get("user") + " and "
					+ "Input Created By=" + username);
			Assert.assertEquals(db_cust_details.get("user").trim(), username);
			String date = MIP_DateFunctionality.getCurrentDate("YYYY-MM-dd");
			log.info("Expected and found Registered Date:"
					+ "DB Modified Date="
					+ db_cust_details.get("modified_date") + " and "
					+ "Registered Date=" + date);
			Assert.assertTrue(db_cust_details.get("modified_date").contains(
					date));
			String prepost_status = "";
			if (prepostStatus
					.equalsIgnoreCase(MIP_CustomerManagementPage.BIMA_PREPAID)) {
				prepost_status = MIP_CustomerManagementPage.BIMA_PREPAID_STATUS;
			} else if (prepostStatus
					.equalsIgnoreCase(MIP_CustomerManagementPage.BIMA_POSTPAID)) {
				prepost_status = MIP_CustomerManagementPage.BIMA_POSTPAID_STATUS;
			}
			log.info("Expected and found Prepost Status:"
					+ "DB Prepost Status =" + db_cust_details.get("is_prepaid")
					+ " and " + "Input Prepost Status=" + prepost_status);
			Assert.assertEquals(db_cust_details.get("is_prepaid"),
					prepost_status);
			String is_warid_status = "";
			if (mno.equalsIgnoreCase(MIP_CustomerManagementPage.BIMA_WARID)) {
				is_warid_status = MIP_CustomerManagementPage.BIMA_IS_WARID;
			} else if (mno
					.equalsIgnoreCase(MIP_CustomerManagementPage.BIMA_JAZZ)) {
				is_warid_status = MIP_CustomerManagementPage.BIMA_IS_JAZZ;
			}
			log.info("Expected and found is_Warid Status:" + "DB is_Warid ="
					+ db_cust_details.get("is_warid") + " and "
					+ "Input is_warid status=" + is_warid_status);
			Assert.assertEquals(db_cust_details.get("is_warid"),
					is_warid_status);
			regpage.waitForElementToVisible(By.id("search-icon"));
			regpage.enterMSISDN(msisdn);
			regpage.clickOnSeachIcon();
			List<String> db_reg_prd = MIP_RegisterCustomer_Queries
					.getRegisteredProduct(msisdn);
			List<String> dashboard_reg_prd = regpage.getRegisteredOffer();
			Assert.assertEquals(db_reg_prd, dashboard_reg_prd);
			Map<String, String> dashboard_cust_details = regpage
					.getCustomerInfo();
			Map<String, String> dashboard_ben_details = regpage
					.getBeneficiaryInfo();
			log.info("Expected and found cust_fname:" + "DB cust_fname="
					+ db_cust_details.get("cust_fname") + " and "
					+ "Dashboard Customer fname="
					+ dashboard_cust_details.get("cust_fname").trim());
			Assert.assertEquals(db_cust_details.get("cust_fname").trim(),
					dashboard_cust_details.get("cust_fname").trim());
			log.info("Expected and found cust_sname:" + "DB cust_sname="
					+ db_cust_details.get("cust_sname") + " and "
					+ "Dashboard cust_sname="
					+ dashboard_cust_details.get("cust_sname").trim());
			Assert.assertEquals(db_cust_details.get("cust_sname").trim(),
					dashboard_cust_details.get("cust_sname").trim());
			if (!age.equals("")) {
				log.info("Expected and found cust_age:" + "DB cust_age="
						+ db_cust_details.get("cust_age") + " and "
						+ "Dashboard cust_age="
						+ dashboard_cust_details.get("cust_age").trim());
				Assert.assertEquals(db_cust_details.get("cust_age").trim(),
						dashboard_cust_details.get("cust_age").trim());
			}
			if (!dob.equals("")) {
				log.info("Expected and found cust_dob:" + "DB cust_age="
						+ db_cust_details.get("cust_dob") + " and "
						+ "Dashboard cust_age="
						+ dashboard_cust_details.get("cust_dob").trim());
				Assert.assertEquals(db_cust_details.get("cust_dob").trim(),
						dashboard_cust_details.get("cust_dob").trim());
			}
			log.info("Expected and found cust_gender:" + "DB cust_gender="
					+ db_cust_details.get("cust_gender") + " and "
					+ "Dashboard cust_gender="
					+ dashboard_cust_details.get("cust_gender").trim());
			Assert.assertEquals(db_cust_details.get("cust_gender").trim(),
					dashboard_cust_details.get("cust_gender").trim());
			if (!cust_cni.equals("")) {
				log.info("Expected and found NIC:" + "DB NIC number="
						+ db_cust_details.get("cust_cnic") + " and "
						+ "Dashboard cust_NIC="
						+ dashboard_cust_details.get("cust_nic"));
				Assert.assertEquals(db_cust_details.get("cust_cnic").trim(),
						dashboard_cust_details.get("cust_nic").trim());

			}
			log.info("Expected and found ben_fname:" + "DB ben_fname="
					+ db_cust_details.get("ben_fname") + " and "
					+ "Dashboard ben_fname="
					+ dashboard_ben_details.get("ben_fname").trim());
			Assert.assertEquals(db_cust_details.get("ben_fname").trim(),
					dashboard_ben_details.get("ben_fname").trim());
			log.info("Expected and found ben_sname:" + "DB ben_sname="
					+ db_cust_details.get("ben_sname") + " and "
					+ "Dashboard ben_sname="
					+ dashboard_ben_details.get("ben_sname").trim());
			Assert.assertEquals(db_cust_details.get("ben_sname").trim(),
					dashboard_ben_details.get("ben_sname").trim());
			log.info("Expected and found ben_age:" + "DB ben_age="
					+ db_cust_details.get("ben_age") + " and "
					+ "Dashboard ben_age="
					+ dashboard_ben_details.get("ben_age").trim());
			Assert.assertEquals(db_cust_details.get("ben_age").trim(),
					dashboard_ben_details.get("ben_age").trim());
			log.info("Expected and found cust_relationship:"
					+ "DB cust_relationship="
					+ db_cust_details.get("cust_relationship") + " and "
					+ "Dashboard cust_relationship="
					+ dashboard_ben_details.get("ben_relationship").trim());
			Assert.assertEquals(
					db_cust_details.get("cust_relationship").trim(),
					dashboard_ben_details.get("ben_relationship").trim());
			log.info("Expected and found ben_msisdn:" + "DB ben_msisdn="
					+ db_cust_details.get("ben_msisdn") + " and "
					+ "Dashboard ben_msisdn="
					+ dashboard_ben_details.get("ben_msisdn").trim());
			Assert.assertEquals(db_cust_details.get("ben_msisdn").trim(),
					dashboard_ben_details.get("ben_msisdn").trim());
			log.info("Expected and found ben_gender:" + "DB ben_gender="
					+ db_cust_details.get("ben_gender") + " and "
					+ "Dashboard ben_gender="
					+ dashboard_ben_details.get("ben_gender").trim());
			Assert.assertEquals(db_cust_details.get("ben_gender").trim(),
					dashboard_ben_details.get("ben_gender").trim());

			Map<String, String> dashboard_benefit_level = regpage
					.getBenefitLevel();
			if (MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION
					.contains(prd_to_register)) {
				String ben_level = MIP_RegisterCustomer_Queries.getOfferCover(
						msisdn,
						MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION);

				log.info("Expected and found life benefit_level:"
						+ "Input benefit_level="
						+ ben_level
						+ " and "
						+ "Dashboard benefit_level="
						+ dashboard_benefit_level.get(
								"bima_accident_benefit_level").trim());
				Assert.assertTrue(dashboard_benefit_level
						.get("bima_accident_benefit_level").trim()
						.contains(ben_level.trim()));

			}

			if (prd_to_register.contains(MIP_CustomerManagementPage.BIMAHEALTH)) {
				String ben_level = MIP_RegisterCustomer_Queries.getOfferCover(
						msisdn, MIP_CustomerManagementPage.BIMAHEALTH);
				log.info("Expected and found  health benefit_level:"
						+ "Input benefit_level="
						+ ben_level
						+ " and "
						+ "Dashboard benefit_level="
						+ dashboard_benefit_level.get("health_benefit_level")
								.trim());
				Assert.assertTrue(dashboard_benefit_level.get(
						"health_benefit_level").contains(ben_level.trim()));
			}

			List<String> sms = MIP_RegisterCustomer_Queries.getRegistrationSMS(
					msisdn, prd_to_register);
			if (MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION
					.contains(prd_to_register)) {
				/*
				 * String offer_charges = MIP_RegisterCustomer_Queries
				 * .getOfferCoverLife(msisdn,
				 * MIP_CustomerManagementPage.BIMALIFE,
				 * bimalife_benefit_level.replaceAll("[$,]", "")); String
				 * detail_sms = MIP_RegCust_TestData.LIFE_REG_DETAILS
				 * .replaceAll("XXXXX",
				 * bimalife_benefit_level.replaceAll("[$,]", "")); detail_sms =
				 * detail_sms.replaceAll("YYYYY", offer_charges); detail_sms =
				 * detail_sms.replaceAll("ZZZZZ", ben_fname);
				 * log.info("Expected and found SMS:" + "DB SMS=[" + detail_sms
				 * + MIP_RegCust_TestData.LIFE_REG_SUCCESS + "]" + " and " +
				 * "Generated sms=" + sms.toString());
				 * Assert.assertTrue(sms.toString().contains(detail_sms));
				 * Assert.assertTrue(sms.toString().contains(
				 * MIP_RegCust_TestData.LIFE_REG_SUCCESS));
				 */
			}
			/*
			 * if (prd_to_register
			 * .equalsIgnoreCase(MIP_CustomerManagementPage.BIMAHEALTH)) {
			 * String ben_level = bimahealth_benefit_level.substring(
			 * bimahealth_benefit_level.indexOf('L'),
			 * bimahealth_benefit_level.length()); ben_level =
			 * ben_level.split(" ")[1].replaceAll("[$,]", ""); String
			 * cover_per_day = bimahealth_benefit_level.substring(
			 * bimahealth_benefit_level.indexOf('H'),
			 * bimahealth_benefit_level.indexOf('-')); cover_per_day =
			 * cover_per_day.split(" ")[1].replaceAll("[$]", ""); String
			 * offer_charges = MIP_RegisterCustomer_Queries
			 * .getOfferCoverLife(msisdn, MIP_CustomerManagementPage.BIMAHEALTH,
			 * ben_level); String detail_sms =
			 * MIP_RegCust_TestData.HEALTH_REG_DETAILS .replaceAll("XXXXX",
			 * cover_per_day); detail_sms = detail_sms.replaceAll("YYYYY",
			 * offer_charges); detail_sms = detail_sms.replaceAll("ZZZZZ",
			 * ben_fname); log.info("Expected and found SMS:" + "DB SMS=[" +
			 * detail_sms + MIP_RegCust_TestData.HEALTH_REG_SUCCESS + "]" +
			 * " and " + "Generated sms=" + sms.toString());
			 * Assert.assertTrue(sms.toString().contains(detail_sms));
			 * Assert.assertTrue(sms.toString().contains(
			 * MIP_RegCust_TestData.HEALTH_REG_SUCCESS)); } if
			 * (prd_to_register.contains(MIP_CustomerManagementPage.BIMALIFE) &&
			 * prd_to_register .contains(MIP_CustomerManagementPage.BIMAHEALTH))
			 * { String offer_charges = MIP_RegisterCustomer_Queries
			 * .getOfferCoverLife(msisdn, MIP_CustomerManagementPage.BIMALIFE,
			 * bimalife_benefit_level.replaceAll("[$,]", "")); String detail_sms
			 * = MIP_RegCust_TestData.LIFE_REG_DETAILS .replaceAll("XXXXX",
			 * bimalife_benefit_level.replaceAll("[$,]", "")); detail_sms =
			 * detail_sms.replaceAll("YYYYY", offer_charges); detail_sms =
			 * detail_sms.replaceAll("ZZZZZ", ben_fname);
			 * Assert.assertTrue(sms.toString().contains(detail_sms)); String
			 * ben_level = bimahealth_benefit_level.substring(
			 * bimahealth_benefit_level.indexOf('L'),
			 * bimahealth_benefit_level.length()); ben_level =
			 * ben_level.split(" ")[1].replaceAll("[$,]", ""); String
			 * cover_per_day = bimahealth_benefit_level.substring(
			 * bimahealth_benefit_level.indexOf('H'),
			 * bimahealth_benefit_level.indexOf('-')); cover_per_day =
			 * cover_per_day.split(" ")[1].replaceAll("[$]", ""); String
			 * offer_charge = MIP_RegisterCustomer_Queries
			 * .getOfferCoverLife(msisdn, MIP_CustomerManagementPage.BIMAHEALTH,
			 * ben_level); String detail_sms_ =
			 * MIP_RegCust_TestData.HEALTH_REG_DETAILS .replaceAll("XXXXX",
			 * cover_per_day); log.info("Expected and found SMS:" + "DB SMS=[" +
			 * detail_sms + MIP_RegCust_TestData.LIFE_HEALTH_REG_SUCCESS + "]" +
			 * " and " + "Generated sms=" + sms.toString()); detail_sms_ =
			 * detail_sms_.replaceAll("YYYYY", offer_charge); detail_sms_ =
			 * detail_sms_.replaceAll("ZZZZZ", ben_fname);
			 * Assert.assertTrue(sms.toString().contains(detail_sms_));
			 * Assert.assertTrue(sms.toString().contains(
			 * MIP_RegCust_TestData.LIFE_HEALTH_REG_SUCCESS));
			 * 
			 * }
			 */
			bima_accident_ben_level = MIP_RegisterCustomer_Queries
					.getOfferCover(msisdn,
							MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION);
			// /Customer Confirmation:
			if (conf_status.equals("1")) {
				logger.log(LogStatus.INFO, "Confirming the Customer After Registration");
				homepage.clickOnMenu(MIP_Menu_Constants.QA_CONFIRMAION);
				qaconfpage = PageFactory.initElements(
						MIP_Test_Configuration.driver,
						MIP_QAConfirmationPage.class);
				qaconfpage.waitForElementToVisible(By
						.xpath("//h3[contains(text(),'QA Confirmation')]"));
				qaconfpage.waitForElementToVisible(By.id("msisdn"));
				qaconfpage.enterMSISDN(msisdn);
				qaconfpage.clickOnSeachIcon();
				Map<String, String> qaconf_details = qaconfpage
						.getCustomerInormation();

				log.info("Expected and found cust_fname:" + "DB cust_fname="
						+ db_cust_details.get("cust_fname") + " and "
						+ "Dashboard Customer fname="
						+ qaconf_details.get("cust_fname").trim());
				Assert.assertEquals(db_cust_details.get("cust_fname").trim(),
						qaconf_details.get("cust_fname").trim());
				log.info("Expected and found cust_sname:" + "DB cust_sname="
						+ db_cust_details.get("cust_sname") + " and "
						+ "Dashboard cust_sname="
						+ qaconf_details.get("cust_sname").trim());
				Assert.assertEquals(db_cust_details.get("cust_sname").trim(),
						qaconf_details.get("cust_sname").trim());

				log.info("Expected and found cust_age:" + "DB cust_age="
						+ db_cust_details.get("cust_age") + " and "
						+ "Dashboard cust_age="
						+ qaconf_details.get("cust_age").trim());
				Assert.assertEquals(db_cust_details.get("cust_age").trim(),
						qaconf_details.get("cust_age").trim());

				if (!dob.equals("")) {
					log.info("Expected and found cust_dob:" + "DB cust_age="
							+ db_cust_details.get("cust_dob") + " and "
							+ "Dashboard cust_age="
							+ qaconf_details.get("cust_dob").trim());
					Assert.assertEquals(db_cust_details.get("cust_dob").trim(),
							qaconf_details.get("cust_dob").trim());
				}
				if (!cust_cni.equals("")) {
					log.info("Expected and found NIC:" + "DB NIC number="
							+ db_cust_details.get("cust_cnic") + " and "
							+ "Dashboard cust_NIC="
							+ qaconf_details.get("cust_nic"));
					Assert.assertEquals(
							db_cust_details.get("cust_cnic").trim(),
							qaconf_details.get("cust_nic").trim());

				}
				log.info("Expected and found ben_fname:" + "DB ben_fname="
						+ db_cust_details.get("ben_fname") + " and "
						+ "Dashboard ben_fname="
						+ qaconf_details.get("ben_fname").trim());
				Assert.assertEquals(db_cust_details.get("ben_fname").trim(),
						qaconf_details.get("ben_fname").trim());
				log.info("Expected and found ben_sname:" + "DB ben_sname="
						+ db_cust_details.get("ben_sname") + " and "
						+ "Dashboard ben_sname="
						+ qaconf_details.get("ben_sname").trim());
				Assert.assertEquals(db_cust_details.get("ben_sname").trim(),
						qaconf_details.get("ben_sname").trim());
				log.info("Expected and found ben_age:" + "DB ben_age="
						+ db_cust_details.get("ben_age") + " and "
						+ "Dashboard ben_age="
						+ qaconf_details.get("ben_age").trim());
				Assert.assertEquals(db_cust_details.get("ben_age").trim(),
						qaconf_details.get("ben_age").trim());
				log.info("Expected and found cust_relationship:"
						+ "DB cust_relationship="
						+ db_cust_details.get("cust_relationship") + " and "
						+ "Dashboard cust_relationship="
						+ qaconf_details.get("ben_relationship").trim());
				Assert.assertEquals(db_cust_details.get("cust_relationship")
						.trim(), qaconf_details.get("ben_relationship").trim());

				if (MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION
						.contains(prd_to_register)) {

					log.info("Expected and found life benefit_level:"
							+ "Input benefit_level=" + bima_accident_ben_level
							+ " and " + "Dashboard benefit_level="
							+ qaconf_details.get("cust_coverlevel").trim());
					Assert.assertTrue(qaconf_details.get("cust_coverlevel")
							.trim().contains(bima_accident_ben_level.trim()));
				}

				log.info("Expected and Found Reg Date details:"
						+ "DB reg_date=" + db_cust_details.get("modified_date")
						+ " reg_date =" + qaconf_details.get("reg_date"));
				String reg_date = MIP_DateFunctionality
						.converDateToDBDateFormat(qaconf_details
								.get("reg_date").split("\\s")[0]);
				Assert.assertTrue(db_cust_details.get("modified_date").trim()
						.contains(reg_date.trim()));
				log.info("Expected and Found dereg details:" + "DB dereg_by="
						+ qaconf_details.get("agent_id") + " Reg_by ="
						+ username);

				Assert.assertTrue(qaconf_details.get("agent_id").trim()
						.equalsIgnoreCase(username));

				String confirmation_status = MIP_DeRegisterCustomer_Queries
						.getConfirmationStatus(
								msisdn,
								MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION);
				log.info("Expected and Found confirmation_status:"
						+ "DB confirmation_status=" + confirmation_status
						+ " Dashboard confirmation_status ="
						+ qaconf_details.get("conf_status"));
				Assert.assertTrue(confirmation_status.trim().equalsIgnoreCase(
						qaconf_details.get("conf_status")));
				qaconfpage.clickOnConfirmButton();
				qaconfpage.confirmPopUp("yes");
				if (mno.equalsIgnoreCase(MIP_CustomerManagementPage.BIMA_WARID)) {
					MIP_DeRegisterCustomer_Queries.confirmTheCustomer(msisdn,
							username, prepost_status);
				}
				/*
				 * Assert.assertTrue(qaconfpage .getSuccessMessage()
				 * .equalsIgnoreCase(
				 * MIP_DeRegisterCustomer_TestData.BIMA_JAZZ_CONFIRMATION_SUCC_MSG
				 * ));
				 */

				Assert.assertTrue(MIP_DeRegisterCustomer_Queries
						.getConfirmationBy(
								msisdn,
								MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION)
						.trim().equalsIgnoreCase(username.trim()));
				confirmation_status = MIP_DeRegisterCustomer_Queries
						.getConfirmationStatus(
								msisdn,
								MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION);
				log.info("Expected and Found confirmation_status:"
						+ "DB confirmation_status=" + confirmation_status
						+ " Input confirmation_status ="
						+ MIP_DeRegisterCustomer_TestData.CONFIRMED_CUSTOMER);
				Assert.assertTrue(confirmation_status.trim().equalsIgnoreCase(
						MIP_DeRegisterCustomer_TestData.CONFIRMED_CUSTOMER));
				List<String> found_conf_sms = MIP_DeRegisterCustomer_Queries
						.getConfirmationSMS(msisdn, "");
				String offer_cover = accident_level.replaceAll("[a-zA-Z,\\s]",
						"");
				String charges = MIP_DeRegisterCustomer_Queries
						.getMonthlyChargesPerOffer(msisdn, "", offer_cover);
				String expected_conf_sms = MIP_DeRegisterCustomer_TestData.BIMA_ACCIDENT_CONFIRMATION_SMS
						.replaceAll("XXXXX", charges).replaceAll("YYYYY",
								offer_cover);
				Assert.assertTrue(found_conf_sms.toString().contains(
						expected_conf_sms));
			}
			homepage.clickOnMenu(MIP_Menu_Constants.DE_REGISTER_CUSTOMER);
			logger.log(LogStatus.INFO, "De-Registering the Customer");
			deregcust = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_DeRegisterCustomerPage.class);
			deregcust.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'De-Register Customer')]"));
			deregcust.waitForElementToVisible(By.id("msisdn"));
			deregcust.search_icon.isDisplayed();
			deregcust.enterMSISDN(msisdn);
			deregcust.clickOnSeachIcon();
			Map<String, String> dashboard_deregcust_details = deregcust
					.getCustomerInformation();
			log.info("Expected and found cust name:" + "DB cust name="
					+ db_cust_details.get("cust_fname") + " "
					+ db_cust_details.get("cust_sname") + " and "
					+ "Dashboard Customer name="
					+ dashboard_deregcust_details.get("cust_name").trim());
			Assert.assertEquals(
					db_cust_details.get("cust_fname").trim()
							+ " ".concat(db_cust_details.get("cust_sname")
									.trim()),
					dashboard_deregcust_details.get("cust_name").trim());
			log.info("Expected and found life benefit_level:"
					+ "Input benefit_level=" + bima_accident_ben_level
					+ " and " + "Dashboard benefit_level="
					+ dashboard_deregcust_details.get("cust_offer").trim());
			Assert.assertTrue(dashboard_deregcust_details.get("cust_offer")
					.trim().contains(bima_accident_ben_level.trim()));
			log.info("Expected and found MSISDN:" + "Input MSISDN=" + msisdn
					+ " and " + "Dashboard msisdn="
					+ dashboard_deregcust_details.get("cust_msisdn").trim());
			Assert.assertTrue(dashboard_deregcust_details.get("cust_msisdn")
					.trim().equals(msisdn.trim()));
			String confirmation_status = MIP_DeRegisterCustomer_Queries
					.getConfirmationStatus(msisdn,
							MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION);
			log.info("Expected and Found confirmation_status:"
					+ "DB confirmation_status=" + confirmation_status
					+ " Dashboard confirmation_status ="
					+ dashboard_deregcust_details.get("conf_status"));
			Assert.assertTrue(confirmation_status.trim().equalsIgnoreCase(
					dashboard_deregcust_details.get("conf_status")));
			Map<String, String> deducted_amt = MIP_DeRegisterCustomer_Queries
					.getDeductedAndCoverEaned(msisdn,
							MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION);
			log.info("Expected and Found Deducted Amount:"
					+ "DB Deducted Amount="
					+ deducted_amt.get("deducted_offer_amount")
					+ " Dashboard Deducted Amount ="
					+ dashboard_deregcust_details.get("deducted_amnt"));
			Assert.assertTrue(deducted_amt
					.get("deducted_offer_amount")
					.trim()
					.contains(
							dashboard_deregcust_details.get("deducted_amnt")
									.trim()));
			log.info("Expected and Found Cover Earned Amount:"
					+ "DB Cover Earned Amount="
					+ deducted_amt.get("cover_earned")
					+ " Dashboard Cover Earned Amount ="
					+ dashboard_deregcust_details.get("cover_earned"));
			Assert.assertTrue(deducted_amt
					.get("cover_earned")
					.trim()
					.contains(
							dashboard_deregcust_details.get("cover_earned")
									.trim()));
			// deregcust.selectAvailableOffersToDeregister(prd_dereg.trim());
			deregcust.clickOnDeregisterButton();
			deregcust.confirmPopUp("yes");
			log.info("Expected and Found Deregister Success Message:Expected="
					+ ";"
					+ deregcust.getDeregSuccessMessage()
					+ " Found ="
					+ MIP_DeRegisterCustomer_TestData.BIMA_DEREGISTER_SUCC_MSG
							.replace("XXXXX", msisdn).replaceAll("\\s", ""));
			Assert.assertTrue(deregcust
					.getDeregSuccessMessage()
					.replaceAll("\\s", "")
					.equalsIgnoreCase(
							MIP_DeRegisterCustomer_TestData.BIMA_DEREGISTER_SUCC_MSG
									.replace("XXXXX", msisdn).replaceAll("\\s",
											"")));
			if (conf_status.equals("1")) {
				Assert.assertTrue(MIP_DeRegisterCustomer_Queries
						.checkRecordExistAfterDereg(msisdn));
			} else {
				Assert.assertFalse(MIP_DeRegisterCustomer_Queries
						.checkRecordExistAfterDereg(msisdn));
			}
			/*
			 * Assert.assertTrue(deregcust.validateCustomerInfoTableHeading());
			 * List<String> db_reg_offer = MIP_DeRegisterCustomer_Queries
			 * .getProductToDeregister(msisdn); List<String> dashboard_reg_offer
			 * = deregcust.getProductOptions(); Collections.sort(db_reg_offer);
			 * Collections.sort(dashboard_reg_offer);
			 * 
			 * Assert.assertEquals(db_reg_offer, dashboard_reg_offer); for (int
			 * i = 0; i < db_reg_offer.size(); i++) {
			 * 
			 * Map<String, String> db_deregcust_details = //
			 * MIP_DeRegisterCustomer_Queries //
			 * .getDeregisterCustomerDetails(msisdn, db_reg_offer.get(i)); //
			 * Map<String, String> dashboard_deregcust_details = deregcust
			 * .getCustomerInfoBeforeDereg(db_reg_offer.get(i));
			 * 
			 * 
			 * Assert.assertEquals(db_deregcust_details.size(),
			 * dashboard_deregcust_details.size());
			 * log.info("Expected and Found Cust_name:" + "DB cust Name=" +
			 * db_deregcust_details.get("cust_name") + "Dashboard cust name=" +
			 * dashboard_deregcust_details.get("cust_name"));
			 * Assert.assertTrue(db_deregcust_details.get("cust_name")
			 * .equalsIgnoreCase(
			 * dashboard_deregcust_details.get("cust_name")));
			 * 
			 * log.info("Expected and Found offer_level:" + "DB offer_level=" +
			 * db_deregcust_details.get("offer_level") +
			 * "Dashboard offer_level =" +
			 * dashboard_deregcust_details.get("offer_level"));
			 * Assert.assertTrue
			 * (db_deregcust_details.get("offer_level").contains(
			 * dashboard_deregcust_details.get("offer_level")));
			 * 
			 * log.info("Expected and Found confirmation_Status:" +
			 * "DB confirmation_Status=" +
			 * db_deregcust_details.get("conf_status") +
			 * "Dashboard confirmation_Status =" +
			 * dashboard_deregcust_details.get("confirmation_Status"));
			 * Assert.assertTrue(db_deregcust_details.get("conf_status")
			 * .equalsIgnoreCase( dashboard_deregcust_details
			 * .get("confirmation_Status")));
			 * log.info("Expected and Found deduted_amount:" +
			 * "DB deducted_amt=" + db_deregcust_details.get("deducted_amt") +
			 * "Dashboard deducted_amt =" +
			 * dashboard_deregcust_details.get("deduted_amount"));
			 * Assert.assertTrue(db_deregcust_details.get("deducted_amt")
			 * .equalsIgnoreCase(
			 * dashboard_deregcust_details.get("deduted_amount")));
			 * log.info("Expected and Found bimalife_cover:" +
			 * "DB bimalife_cover=" + db_deregcust_details.get("offer_cover") +
			 * "Dashboard bimalife_cover =" +
			 * dashboard_deregcust_details.get("bimalife_cover"));
			 * Assert.assertTrue(db_deregcust_details.get("bimalife_cover")
			 * .equalsIgnoreCase(
			 * dashboard_deregcust_details.get("deduted_amount")));
			 * log.info("Expected and Found hospital_cover:" +
			 * "DB hospital_cover=" + db_deregcust_details.get("hospital_cover")
			 * + "Dashboard bimahealth_cover =" +
			 * dashboard_deregcust_details.get("bimahealth_cover"));
			 * Assert.assertTrue(db_deregcust_details .get("hospital_cover")
			 * .equalsIgnoreCase(
			 * dashboard_deregcust_details.get("bimahealth_cover")));
			 * 
			 * }
			 */

			String curr_date = MIP_DateFunctionality
					.getCurrentDate("yyyy-MM-dd");

			/*
			 * if ((prd_to_register
			 * .contains(MIP_CustomerManagementPage.BIMAHEALTH) &&
			 * (prd_to_register
			 * .contains(MIP_CustomerManagementPage.BIMALIFE)))) { if (prd_dereg
			 * .equalsIgnoreCase(MIP_CustomerManagementPage.BIMAHEALTH) ||
			 * prd_dereg .equalsIgnoreCase(MIP_CustomerManagementPage.BIMALIFE))
			 * { Assert.assertTrue(MIP_DeRegisterCustomer_Queries
			 * .checkRecordExistAfterDereg(msisdn)); } else if (prd_dereg
			 * .equalsIgnoreCase(MIP_CustomerManagementPage.BIMAHEALTH)) {
			 * Assert.assertFalse(MIP_DeRegisterCustomer_Queries
			 * .checkRecordExistAfterDereg(msisdn)); } else if ((prd_dereg
			 * .contains(MIP_CustomerManagementPage.BIMAHEALTH) && (prd_dereg
			 * .contains(MIP_CustomerManagementPage.BIMALIFE)))) { if
			 * (conf_status.equals("1"))
			 * Assert.assertTrue(MIP_DeRegisterCustomer_Queries
			 * .checkRecordExistAfterDereg(msisdn)); else
			 * Assert.assertFalse(MIP_DeRegisterCustomer_Queries
			 * .checkRecordExistAfterDereg(msisdn)); } } else { if
			 * (conf_status.equals("1"))
			 * Assert.assertTrue(MIP_DeRegisterCustomer_Queries
			 * .checkRecordExistAfterDereg(msisdn)); else
			 * Assert.assertFalse(MIP_DeRegisterCustomer_Queries
			 * .checkRecordExistAfterDereg(msisdn)); } List<String> dereg_sms =
			 * MIP_DeRegisterCustomer_Queries .getDeRegisteredSMS(msisdn,
			 * prd_dereg);
			 */
			if (MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION
					.contains(prd_to_register)) {
				// String dereg_status = MIP_DeRegisterCustomer_Queries
				// .getCustomerDeregisterStatus(
				// msisdn,
				// MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION);
				// log.info("Deactivated status after deregistration is for bima accident protection is :"
				// + dereg_status);
				// Assert.assertTrue(dereg_status
				// .equals(MIP_DeRegisterCustomer_TestData.DEACTIVATED_STATUS));
				Map<String, String> dereg_details = MIP_DeRegisterCustomer_Queries
						.getDeregisterCustomerDetails(
								msisdn,
								MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION);
				log.info("Expected and Found dereg details:" + "DB cust name="
						+ dereg_details.get("cust_name") + "Input cust fname ="
						+ cust_fname + " " + cust_sname);
				Assert.assertTrue(dereg_details
						.get("cust_name")
						.trim()
						.equalsIgnoreCase(
								cust_fname.trim() + " ".concat(cust_sname)));
				if (!age.equals("")) {
					log.info("Expected and Found dereg details:"
							+ "DB cust age=" + dereg_details.get("cust_age")
							+ "Input cust_age =" + age);
					Assert.assertTrue(dereg_details.get("cust_age").trim()
							.equalsIgnoreCase(age.trim()));
				}
				if (!dob.equals("")) {
					log.info("Expected and Found dereg details:" + "DB DOB="
							+ dereg_details.get("cust_dob")
							+ "Input cust DOB =" + dob);
					Assert.assertTrue(dereg_details.get("cust_dob").trim()
							.equals(dob));
				}
				log.info("Expected and Found dereg details:" + "DB gender="
						+ dereg_details.get("cust_gender")
						+ "Input cust gender =" + gender);
				Assert.assertTrue(dereg_details.get("cust_gender").trim()
						.equalsIgnoreCase(gender));
				log.info("Expected and Found dereg details:" + "DB ben name="
						+ dereg_details.get("ben_name") + "Input ben_name ="
						+ ben_fname + " " + ben_sname);
				Assert.assertTrue(dereg_details.get("ben_name").trim()
						.equalsIgnoreCase(ben_fname + " ".concat(ben_sname)));
				log.info("Expected and Found dereg details:" + "DB ben gender="
						+ dereg_details.get("ben_gender")
						+ "Input ben_gender =" + ben_gender);
				Assert.assertTrue(dereg_details.get("ben_gender").trim()
						.equalsIgnoreCase(ben_gender));
				log.info("Expected and Found dereg details:" + "DB ben_age="
						+ dereg_details.get("ben_age") + "Input ben_age ="
						+ ben_age);
				Assert.assertTrue(dereg_details.get("ben_age").trim()
						.equalsIgnoreCase(ben_age));

				log.info("Expected and Found dereg details:" + "DB dereg_date="
						+ dereg_details.get("dereg_date") + " dereg_date ="
						+ curr_date);
				Assert.assertTrue(dereg_details.get("dereg_date").trim()
						.contains(curr_date));
				log.info("Expected and Found dereg details:" + "DB dereg_by="
						+ dereg_details.get("dereg_by") + " deg_by ="
						+ username);
				Assert.assertTrue(dereg_details.get("dereg_by").trim()
						.equalsIgnoreCase(username));
				/*
				 * if (conf_status.equals("1")) { String record_deletion_date =
				 * MIP_DeRegisterCustomer_Queries .recorddeletiondate();
				 * log.info("Expected and Found dereg details:" +
				 * "DB record_deletion_date=" +
				 * dereg_details.get("record_deletion_date") +
				 * "record_deletion_date =" + record_deletion_date);
				 * Assert.assertTrue(dereg_details.get("record_deletion_date")
				 * .trim().contains(record_deletion_date)); } else {
				 * log.info("Expected and Found dereg details:" +
				 * "DB record_deletion_date=" +
				 * dereg_details.get("record_deletion_date") +
				 * "record_deletion_date =" + curr_date);
				 * Assert.assertTrue(dereg_details.get("record_deletion_date")
				 * .trim().contains(curr_date)); }
				 * log.info("De-registration SMS :" + "Expected SMS=" +
				 * MIP_DeRegisterCustomer_TestData.HEALTH_SMS + " and DB SMS=" +
				 * dereg_sms.toString());
				 * Assert.assertTrue(dereg_sms.toString().contains(
				 * MIP_DeRegisterCustomer_TestData.HEALTH_SMS)); }
				 */
				/*
				 * if (prd_dereg.contains(MIP_CustomerManagementPage.BIMALIFE))
				 * { String dereg_status = MIP_DeRegisterCustomer_Queries
				 * .getCustomerDeregisterStatus(msisdn,
				 * MIP_CustomerManagementPage.BIMAHEALTH); log.info(
				 * "Deactivated status after deregistration is for bimahealth is :"
				 * + dereg_status); Assert.assertTrue(dereg_status
				 * .equals(MIP_DeRegisterCustomer_TestData.DEACTIVATED_STATUS));
				 * Map<String, String> dereg_details =
				 * MIP_DeRegisterCustomer_Queries
				 * .getbimaCancellationRecord(msisdn,
				 * MIP_CustomerManagementPage.BIMALIFE);
				 * log.info("Expected and Found dereg details:" +
				 * "DB custfname=" + dereg_details.get("cust_fname") +
				 * " Input cust fname =" + cust_fname);
				 * Assert.assertTrue(dereg_details.get("cust_fname").trim()
				 * .equalsIgnoreCase(cust_fname.trim()));
				 * log.info("Expected and Found dereg details:" +
				 * "DB custsname=" + dereg_details.get("cust_sname") +
				 * " Input cust sname =" + cust_sname);
				 * Assert.assertTrue(dereg_details.get("cust_sname").trim()
				 * .equalsIgnoreCase(cust_sname.trim()));
				 * log.info("Expected and Found dereg details:" + "DB age=" +
				 * dereg_details.get("cust_age") + " Input cust age =" + age);
				 * Assert.assertTrue(dereg_details.get("cust_age").trim()
				 * .equals(age)); log.info("Expected and Found dereg details:" +
				 * "DB gender=" + dereg_details.get("cust_gender") +
				 * " Input cust gender =" + gender);
				 * Assert.assertTrue(dereg_details.get("cust_gender").trim()
				 * .equalsIgnoreCase(gender));
				 * log.info("Expected and Found dereg details:" +
				 * "DB ben fname=" + dereg_details.get("ben_fname") +
				 * " Input ben_fname =" + ben_fname);
				 * Assert.assertTrue(dereg_details.get("ben_fname").trim()
				 * .equalsIgnoreCase(ben_fname));
				 * log.info("Expected and Found dereg details:" +
				 * "DB ben sname=" + dereg_details.get("ben_sname") +
				 * " Input ben_sname =" + ben_sname);
				 * Assert.assertTrue(dereg_details.get("ben_sname").trim()
				 * .equalsIgnoreCase(ben_sname));
				 * log.info("Expected and Found dereg details:" + "DB ben_age="
				 * + dereg_details.get("ben_age") + " Input ben_age =" +
				 * ben_age);
				 * Assert.assertTrue(dereg_details.get("ben_age").trim()
				 * .equalsIgnoreCase(ben_age));
				 * 
				 * log.info("Expected and Found dereg details:" +
				 * "DB dereg_date=" + dereg_details.get("dereg_date") +
				 * " dereg_date =" + curr_date);
				 * Assert.assertTrue(dereg_details.get("dereg_date").trim()
				 * .contains(curr_date));
				 * log.info("Expected and Found dereg details:" + "DB dereg_by="
				 * + dereg_details.get("dereg_by") + " deg_by =" + username);
				 * Assert.assertTrue(dereg_details.get("dereg_by").trim()
				 * .equalsIgnoreCase(username)); if (conf_status.equals("1")) {
				 * String record_deletion_date = MIP_DeRegisterCustomer_Queries
				 * .recorddeletiondate();
				 * log.info("Expected and Found dereg details:" +
				 * "DB record_deletion_date=" +
				 * dereg_details.get("record_deletion_date") +
				 * "record_deletion_date =" + record_deletion_date);
				 * Assert.assertTrue(dereg_details.get("record_deletion_date")
				 * .trim().contains(record_deletion_date)); } else {
				 * log.info("Expected and Found dereg details:" +
				 * "DB record_deletion_date=" +
				 * dereg_details.get("record_deletion_date") +
				 * "record_deletion_date =" + curr_date);
				 * Assert.assertTrue(dereg_details.get("record_deletion_date")
				 * .trim().contains(curr_date)); }
				 * log.info("De-registration SMS :" + "Expected SMS=" +
				 * MIP_DeRegisterCustomer_TestData.LIFE_SMS + " and DB SMS=" +
				 * dereg_sms.toString());
				 * Assert.assertTrue(dereg_sms.toString().contains(
				 * MIP_DeRegisterCustomer_TestData.LIFE_SMS)); }
				 */
				List<String> dereg_sms = MIP_DeRegisterCustomer_Queries
						.getDeRegisteredSMS(msisdn, "");
				// Assert.assertTrue(dereg_sms.toString().contains(MIP_DeRegisterCustomer_TestData.BIMA_ACCIDENT_DEREG_SMS));
				System.out
						.println(dereg_sms
								.toString()
								.contains(
										MIP_DeRegisterCustomer_TestData.BIMA_ACCIDENT_DEREG_SMS));
				if (reactivate_status.equals("1") && conf_status.equals("1")) {
					logger.log(LogStatus.INFO, "Reactivating the Confirmed Customer After De-Registration");
					homepage.clickOnMenu(MIP_Menu_Constants.REACTIVATE_CUSTOMER);
					regpage.waitForElementToVisible(By
							.xpath("//h3[normalize-space(text())='Reactive Customer']"));
					regpage.waitForElementToVisible(By
							.xpath("//div[contains(text(),'All fields marked')]"));
					regpage.enterMSISDN(msisdn);
					regpage.clickOnSeachIcon();
					regpage.waitForElementToVisible(By.id("saveBtn"));
					// regpage.selectAvailableInformation(prd_reactivate);
					if (MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION
							.contains(prd_reactivate))
						regpage.selectBimaAccidentProtecionBenefitLevelForReactivation(reactivate_accident_level);
					if (prd_reactivate
							.contains(MIP_CustomerManagementPage.BIMAHEALTH))
						regpage.selectBimaHealthBenefitLevel("");
					regpage.clickOnSave().confirmPopUp("yes");

					Assert.assertTrue(regpage
							.getSuccessMsg()
							.replaceAll("\\s", "")
							.equalsIgnoreCase(
									MIP_DeRegisterCustomer_TestData.BIMA_REACTIVATE_SUCC_MSG
											.replace("XXXXX", msisdn)
											.replaceAll("\\s", "")));

					if (MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION
							.contains(prd_reactivate)) {
						Map<String, String> react_details = MIP_DeRegisterCustomer_Queries
								.checkReactivationStatus(msisdn,
										MIP_CustomerManagementPage.BIMAHEALTH);
						log.info("Expected and Found old offer:"
								+ "DB old offer =" + bima_accident_ben_level
								+ " Reactivated DB Details reactivated state ="
								+ react_details.get("prevoius_offer"));
						Assert.assertTrue(react_details.get("prevoius_offer")
								.trim().contains(bima_accident_ben_level));
						log.info("Expected and Found new offer:"
								+ "DB New offer =" + reactivate_accident_level
								+ " Reactivated DB Details reactivated state ="
								+ react_details.get("new_offer"));
						/*
						 * Assert.assertTrue(reactivate_accident_level
						 * .trim().contains(react_details.get("new_offer")));
						 */
						Assert.assertTrue(reactivate_accident_level.replaceAll(
								",", "").contains(
								react_details.get("new_offer").split("\\.")[0]));

						log.info("Expected and Found is_confirmed status  details:"
								+ "DB is_confirmed ="
								+ react_details.get("is_confirmed")
								+ " input is_confirmed  ="
								+ MIP_DeRegisterCustomer_TestData.CONFIRMATION_STATUS_BEFORECONFIRMATION);
						Assert.assertTrue(react_details
								.get("is_confirmed")
								.trim()
								.equalsIgnoreCase(
										MIP_DeRegisterCustomer_TestData.CONFIRMATION_STATUS_BEFORECONFIRMATION));

						log.info("Expected and Found created By  details:"
								+ "DB created ="
								+ react_details.get("created_by")
								+ " input created  =" + username);
						Assert.assertTrue(react_details.get("created_by")
								.equalsIgnoreCase(username));
						log.info("Expected and Found created_date  details:"
								+ "DB created_date ="
								+ react_details.get("created_date")
								+ " input created_date by =" + curr_date);
						Assert.assertTrue(react_details.get("created_date")
								.trim().contains(curr_date));
						prepost_status = "";
						if (prepostStatus
								.equalsIgnoreCase(MIP_CustomerManagementPage.BIMA_PREPAID)) {
							prepost_status = MIP_CustomerManagementPage.BIMA_PREPAID_STATUS;
						} else if (prepostStatus
								.equalsIgnoreCase(MIP_CustomerManagementPage.BIMA_POSTPAID)) {
							prepost_status = MIP_CustomerManagementPage.BIMA_POSTPAID_STATUS;
						}
						log.info("Expected and found Prepost Status:"
								+ "DB Prepost Status ="
								+ react_details.get("is_prepaid") + " and "
								+ "Input Prepost Status=" + prepost_status);
						Assert.assertEquals(react_details.get("is_prepaid"),
								prepost_status);
						if (is_confirm_after_reactivaton.equals("1")) {
							homepage.clickOnMenu(MIP_Menu_Constants.QA_CONFIRMAION);
							qaconfpage = PageFactory.initElements(
									MIP_Test_Configuration.driver,
									MIP_QAConfirmationPage.class);
							qaconfpage
									.waitForElementToVisible(By
											.xpath("//h3[contains(text(),'QA Confirmation')]"));
							qaconfpage.waitForElementToVisible(By.id("msisdn"));
							qaconfpage.enterMSISDN(msisdn);
							qaconfpage.clickOnSeachIcon();
							qaconfpage.clickOnConfirmButton();
							qaconfpage.confirmPopUp("yes");
							if (mno.equalsIgnoreCase(MIP_CustomerManagementPage.BIMA_WARID)) {
								MIP_DeRegisterCustomer_Queries
										.confirmTheCustomer(msisdn, username,
												prepost_status);
							}
							react_details = MIP_DeRegisterCustomer_Queries
									.checkReactivationStatus(
											msisdn,
											MIP_CustomerManagementPage.BIMAHEALTH);
							log.info("Expected and Found is_confirmed status  details:"
									+ "DB is_confirmed ="
									+ react_details.get("is_confirmed")
									+ " input is_confirmed  ="
									+ is_confirm_after_reactivaton);
							Assert.assertTrue(react_details
									.get("is_confirmed")
									.trim()
									.equalsIgnoreCase(
											is_confirm_after_reactivaton));
							log.info("Expected and Found is_confirmed By  details:"
									+ "DB confirmed_by ="
									+ react_details.get("confirmed_by")
									+ " input confirmed_by  =" + username);
							Assert.assertTrue(react_details.get("confirmed_by")
									.trim().equalsIgnoreCase(username));
							log.info("Expected and Found confirmation channel  details:"
									+ "DB onfirmation channel ="
									+ react_details.get("chn_name")
									+ " input onfirmation channel  ="
									+ "DASHBOARD");
							Assert.assertTrue(react_details.get("chn_name")
									.trim().equalsIgnoreCase("DASHBOARD"));

						}
					}
					Assert.assertTrue(MIP_RegisterCustomer_Queries
							.getRegistrationSMS(
									msisdn,
									MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION)
							.toString()
							.contains(
									MIP_RegisterCustomer_TestData.BIMA_ACCIDENT_REGISTRATION_SMS));

					/*
					 * if (prd_reactivate
					 * .contains(MIP_CustomerManagementPage.BIMALIFE)) {
					 * Map<String, String> react_details =
					 * MIP_DeRegisterCustomer_Queries
					 * .checkReactivationStatus(msisdn,
					 * MIP_CustomerManagementPage.BIMALIFE);
					 * log.info("Expected and Found reactivation  details:" +
					 * "DB reactivated state=" +
					 * react_details.get("is_reactivated") +
					 * " input reactivated state =" + reactivate);
					 * Assert.assertTrue(react_details.get("is_reactivated")
					 * .trim().equals(reactivate));
					 * log.info("Expected and Found reactivation  details:" +
					 * "DB reactivated by=" +
					 * react_details.get("reactivated_by") +
					 * " input reactivated by =" + username);
					 * Assert.assertTrue(react_details.get("reactivated_by")
					 * .trim().equalsIgnoreCase(username));
					 * log.info("Expected and Found reactivation  details:" +
					 * "DB reactivated date=" +
					 * react_details.get("reactivated_date") +
					 * " input reactivated by =" + curr_date);
					 * Assert.assertTrue(react_details.get("reactivated_date")
					 * .trim().contains(curr_date)); }
					 */
				}
			}

		} catch (Throwable t) {
			log.info("DeRegisterNonConfrimedPostPaidCustomer--" + testcase
					+ " Test Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@AfterMethod(alwaysRun = true)
	public void after_test(ITestResult res) {

		if (res.getStatus() == ITestResult.FAILURE) {
			String path = MIP_ScreenShots.takeScreenShot(
					MIP_Test_Configuration.driver, res.getName() + "_"
							+ testcaseName);
			logger.log(LogStatus.FAIL, testcaseName+"---Test Failed");
			logger.log(LogStatus.ERROR, res.getThrowable());
			logger.log(LogStatus.FAIL, logger.addScreenCapture(path));
			homepage.clickOnMenu(MIP_Menu_Constants.DE_REGISTER_CUSTOMER);
		} else {
			logger.log(LogStatus.PASS, testcaseName+"-----Test passed");
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
