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
import com.milvik.mip.dataprovider.MIP_RegisterCustomer_TestData;
import com.milvik.mip.dbqueries.MIP_RegisterCustomer_Queries;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
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

public class MIP_CustRegistration_Test {
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage;
	MIP_HomePage homepage;
	MIP_RegisterCustomerPage regpage = null;
	String username = "";
	String testcaseName = "";
	String platform_ = "";

	@BeforeTest
	@Parameters({ "browser", "flag", "platform" })
	public void test_setup(@Optional("firefox") String browser,
			@Optional("0") String flag, @Optional("windows") String platform) {
		log = MIP_Logging.logDetails("MIP_CustRegistration_Test");
		report = new ExtentReports("Test_Reports/RegisterCustomer_Test.html");
		platform_ = platform;
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
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			WebDriverWait w = new WebDriverWait(MIP_Test_Configuration.driver,
					600);
			w.until(ExpectedConditions.visibilityOfElementLocated((By
					.linkText(MIP_Menu_Constants.CUSTOMER))));
			homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);

		}
	}

	@Test(priority = 1, enabled = false, testName = "RegisterCustomer", dataProvider = "RegisterCustomer", dataProviderClass = MIP_RegisterCustomer_TestData.class)
	public void registrationTest(String testcase, String mno, String msisdn,
			String prepostStatus, String prd_to_register,
			String accident_level, String cust_fname, String cust_sname,
			String age, String cust_cni, String dob, String gender,
			String relationship, String ben_fname, String ben_sname,
			String ben_age, String ben_msisdn, String ben_gender,
			String success_message) throws Throwable {
		testcaseName = testcase + "--" + msisdn;
		try {
			username = MIP_ReadPropertyFile.getPropertyValue("username");
			logger = report.startTest("RegisterCustomer--" + testcase + "--"
					+ msisdn);
			regpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_RegisterCustomerPage.class);
			regpage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Register Customer')]"));
			regpage.waitForElementToVisible(By
					.xpath("//div[contains(text(),'All fields marked')]"));
			if (mno.equalsIgnoreCase(MIP_CustomerManagementPage.BIMA_WARID))
				MIP_RegisterCustomer_Queries.connectToWaridDBAndInserRecord(
						platform_, cust_fname, msisdn, prepostStatus, cust_cni);
			regpage.enterMSISDN(msisdn);
			regpage.clickOnSeachIcon();
			regpage.waitForElementToVisible(By.id("saveBtn"));

			regpage.enterCustomerInfo(mno, cust_fname, cust_sname, age,
					cust_cni, dob, gender);
			logger.log(LogStatus.INFO, "Entered Customer Information");
			Thread.sleep(1000);
			regpage.selectAvailableInformation(prd_to_register);
			logger.log(LogStatus.INFO, "Selected Available product--"
					+ prd_to_register);
			if (MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION
					.contains(prd_to_register)) {
				regpage.selectBimaAccidentProtecionBenefitLevel(accident_level);
				logger.log(LogStatus.INFO, "Selected "
						+ MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION
						+ " Cover Level " + accident_level);
			}

			regpage.enterBeneficiaryInfo(relationship, ben_fname, ben_sname,
					ben_age, ben_msisdn, ben_gender);
			logger.log(LogStatus.INFO, "Entered Beneficiary  Information");
			regpage.clickOnSave()
					.confirmPopUp("yes");
			logger.log(LogStatus.INFO, "Clicked On Register Button");
			Assert.assertTrue(regpage.getSuccessMsg().replaceAll("\\s", "")
					.equalsIgnoreCase(success_message.replaceAll("\\s", "")));
			logger.log(LogStatus.INFO, "Customer Registered Successfully");
			logger.log(LogStatus.INFO, "Validating the Registration Aganist DataBase");
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
			Map<String, String> coaching_pgm_info = regpage
					.getCoachingProgramInfo();
			Assert.assertTrue(coaching_pgm_info
					.get("coaching_perogram")
					.equalsIgnoreCase(
							MIP_RegisterCustomer_TestData.DEFAULT_COACHING_PROGRAM));
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
				List<String> sms = MIP_RegisterCustomer_Queries
						.getRegistrationSMS(
								msisdn,
								MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION);
				Assert.assertTrue(sms
						.toString()
						.contains(
								MIP_RegisterCustomer_TestData.BIMA_ACCIDENT_REGISTRATION_SMS));

			}
			/*
			 * if
			 * (prd_to_register.contains(MIP_CustomerManagementPage.BIMAHEALTH))
			 * { String ben_level = MIP_RegisterCustomer_Queries.getOfferCover(
			 * msisdn, MIP_CustomerManagementPage.BIMAHEALTH);
			 * log.info("Expected and found  health benefit_level:" +
			 * "Input benefit_level=" + ben_level + " and " +
			 * "Dashboard benefit_level=" +
			 * dashboard_benefit_level.get("health_benefit_level") .trim());
			 * Assert.assertTrue(dashboard_benefit_level.get(
			 * "health_benefit_level").contains(ben_level.trim())); }
			 */

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
			logger.log(LogStatus.INFO, "Validated Customer Registration Successfully.");
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
			log.info("RegisterCustomer---" + testcase + " Test Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 1, enabled = false, testName = "changeCustomerDetails", dataProvider = "changeCustomerDetails", dataProviderClass = MIP_RegisterCustomer_TestData.class)
	public void changeCustomerDetails(String testcase, String msisdn,
			String cust_fname, String cust_sname, String age, String cust_cni,
			String dob, String gender, String relationship, String ben_fname,
			String ben_sname, String ben_age, String ben_msisdn,
			String ben_gender, String success_message) throws Throwable {
		testcaseName = testcase + "--" + msisdn;
		try {
			username = MIP_ReadPropertyFile.getPropertyValue("username");
			logger = report.startTest("changeCustomerDetails--" + testcase
					+ "--" + msisdn);
			regpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_RegisterCustomerPage.class);
			regpage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Register Customer')]"));
			regpage.waitForElementToVisible(By
					.xpath("//div[contains(text(),'All fields marked')]"));
			Thread.sleep(1000);
			regpage.enterMSISDN(msisdn);
			regpage.clickOnSeachIcon();
			regpage.waitForElementToVisible(By.id("saveBtn"));
			logger.log(LogStatus.INFO, "Changing the Customer and Beneficiary Information");
			regpage.editCustomerInfo(cust_fname, cust_sname, age, cust_cni,
					dob, gender)
					.editBeneficiaryInfo(relationship, ben_fname, ben_sname,
							ben_age, ben_msisdn, ben_gender).clickOnSave()
					.confirmPopUp("yes");
			Assert.assertTrue(regpage.getSuccessMsg().replaceAll("\\s", "")
					.equalsIgnoreCase(success_message.replaceAll("\\s", "")));
			logger.log(LogStatus.INFO, "Customer and Beneficiary Information Changed Successfully.");
			Map<String, String> db_cust_details = MIP_RegisterCustomer_Queries
					.getCustomerDetails(msisdn);
			if (!cust_fname.equals("")) {
				log.info("Expected and found cust fname:" + "DB Cust Fname="
						+ db_cust_details.get("cust_fname") + " and "
						+ "Input Cust Fname=" + cust_fname);
				Assert.assertEquals(db_cust_details.get("cust_fname").trim(),
						cust_fname.trim());
			}
			if (!cust_sname.equals("")) {
				log.info("Expected and found cust_sname:" + "DB cust_sname="
						+ db_cust_details.get("cust_sname") + " and "
						+ "Input cust_sname=" + cust_sname);
				Assert.assertEquals(db_cust_details.get("cust_sname").trim(),
						cust_sname.trim());
			}
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
			if (!gender.equals("")) {
				log.info("Expected and found cust cust_gender:"
						+ "DB cust_gender="
						+ db_cust_details.get("cust_gender") + " and "
						+ "Input cust_gender=" + gender);
				Assert.assertEquals(db_cust_details.get("cust_gender").trim(),
						gender.trim());
			}
			if (!ben_fname.equals("")) {
				log.info("Expected and found ben_fname:" + "DB ben_fname="
						+ db_cust_details.get("ben_fname") + " and "
						+ "ben_fname=" + ben_fname);
				Assert.assertEquals(db_cust_details.get("ben_fname").trim(),
						ben_fname.trim());
			}
			if (!ben_sname.equals("")) {
				log.info("Expected and found ben_sname:" + "DB ben_sname="
						+ db_cust_details.get("ben_sname") + " and "
						+ "Input ben_sname=" + ben_sname);
				Assert.assertEquals(db_cust_details.get("ben_sname").trim(),
						ben_sname.trim());
			}
			if (!ben_age.equals("")) {
				log.info("Expected and found ben_age:" + "DB ben_age="
						+ db_cust_details.get("ben_age") + " and "
						+ "Input ben_age=" + ben_age);
				Assert.assertEquals(db_cust_details.get("ben_age").trim(),
						ben_age.trim());
			}
			if (!relationship.equals("")) {
				log.info("Expected and found cust_relationship:"
						+ "DB cust_relationship="
						+ db_cust_details.get("cust_relationship") + " and "
						+ "Input cust_relationship=" + relationship);
				Assert.assertEquals(db_cust_details.get("cust_relationship")
						.trim(), relationship.trim());
			}
			if (!ben_gender.equals("")) {
				log.info("Expected and found ben_gender:" + "DB ben_gender ="
						+ db_cust_details.get("ben_gender") + " and "
						+ "Input ben_gender=" + ben_gender);
				Assert.assertEquals(db_cust_details.get("ben_gender").trim(),
						ben_gender.trim());
			}
			if (!ben_msisdn.equals("")) {
				log.info("Expected and found ben_msisdn:" + "DB ben_msisdn ="
						+ db_cust_details.get("ben_msisdn") + " and "
						+ "Input ben_msisdn=" + ben_msisdn);
				Assert.assertEquals(db_cust_details.get("ben_msisdn").trim(),
						ben_msisdn.trim());
			}
			if (!cust_cni.equals("")) {
				log.info("Expected and found NIC:" + "DB NIC number="
						+ db_cust_details.get("cust_cnic") + " and "
						+ "Input cust_NIC=" + cust_cni);
				Assert.assertEquals(db_cust_details.get("cust_cnic").trim(),
						cust_cni.trim());

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

			String ben_level = MIP_RegisterCustomer_Queries
					.getOfferCover(msisdn,
							MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION);
			log.info("Expected and found life benefit_level:"
					+ "Input benefit_level="
					+ ben_level
					+ " and "
					+ "Dashboard benefit_level="
					+ dashboard_benefit_level
							.get("bima_accident_benefit_level").trim());
			Assert.assertTrue(dashboard_benefit_level
					.get("bima_accident_benefit_level").trim()
					.contains(ben_level.trim()));

			/*
			 * if
			 * (prd_to_register.contains(MIP_CustomerManagementPage.BIMAHEALTH))
			 * { String ben_level = MIP_RegisterCustomer_Queries.getOfferCover(
			 * msisdn, MIP_CustomerManagementPage.BIMAHEALTH);
			 * log.info("Expected and found  health benefit_level:" +
			 * "Input benefit_level=" + ben_level + " and " +
			 * "Dashboard benefit_level=" +
			 * dashboard_benefit_level.get("health_benefit_level") .trim());
			 * Assert.assertTrue(dashboard_benefit_level.get(
			 * "health_benefit_level").contains(ben_level.trim())); }
			 */
			logger.log(LogStatus.INFO, "Customer and Beneficiary Information Changes are validated Successfully.");
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
			log.info("RegisterCustomer---" + testcase + " Test Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 3, enabled = true, testName = "RegisterCustomer_Mandatory_Field_Validation", dataProvider = "registerCustomermandatoryFieldValidation", dataProviderClass = MIP_RegisterCustomer_TestData.class)
	public void mandatoryFieldValidationInregistration(String msisdn,
			String errmsg1, String errmsg2) throws Throwable {
		testcaseName = "MIP_webPortal_TC_Mandatory_Field_Validation_Customer_Registration--"
				+ msisdn;
		try {
			logger = report
					.startTest("RegisterCustomer_Mandatory_Field_Validation--"
							+ testcaseName);
			regpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_RegisterCustomerPage.class);
			regpage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Register Customer')]"));
			regpage.waitForElementToVisible(By
					.xpath("//div[contains(text(),'All fields marked')]"));
			Thread.sleep(1000);
			regpage.enterMSISDN(msisdn);
			regpage.clickOnSeachIcon();
			regpage.waitForElementToVisible(By.id("saveBtn"));
			regpage.clickOnSave();
			logger.log(LogStatus.INFO, "Clicked on Register Button without entering mandatory field values");
			String err_msg = regpage.getValidationMessage();
			log.info("Expected and found validation message: Expected="
					+ errmsg1 + " Found=" + err_msg);
			Assert.assertTrue(err_msg.replaceAll("\\s", "").equalsIgnoreCase(
					errmsg1.replaceAll("\\s", "")));
			regpage.selectAvailableInformation(MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION);
			// regpage.selectAvailableInformation(MIP_CustomerManagementPage.BIMALIFE);
			regpage.clickOnSave();
			err_msg = regpage.getValidationMessage();
			log.info("Expected and found validation message: Expected="
					+ errmsg2 + " Found=" + err_msg);
			Assert.assertTrue(err_msg.replaceAll("\\s", "").equalsIgnoreCase(
					errmsg2.replaceAll("\\s", "")));
			logger.log(LogStatus.INFO, "Error Message Validation is successfull");
		} catch (Throwable t) {
			log.info("RegisterCustomer_Mandatory_Field_Validation--"
					+ testcaseName + " Test Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 0, enabled = true, testName = "Field_Validation_negativeData", dataProvider = "RegisterCustomer_FieldValidation", dataProviderClass = MIP_RegisterCustomer_TestData.class)
	public void fieldValidationWithNegativeData_Postpaid(String testcase,
			String mno, String msisdn, String prepostStatus,
			String prd_to_register, String accident_level, String cust_fname,
			String cust_sname, String age, String cust_cni, String dob,
			String gender, String relationship, String ben_fname,
			String ben_sname, String ben_age, String ben_msisdn,
			String ben_gender, String err_msg) throws Throwable {
		testcaseName = testcase + "---" + msisdn;
		try {
			logger = report.startTest("fieldValidationWithNegativeData--"
					+ testcase);
			regpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_RegisterCustomerPage.class);
			regpage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Register Customer')]"));
			regpage.waitForElementToVisible(By
					.xpath("//div[contains(text(),'All fields marked')]"));
			if (mno.equalsIgnoreCase(MIP_CustomerManagementPage.BIMA_WARID))
				MIP_RegisterCustomer_Queries.connectToWaridDBAndInserRecord(
						platform_, cust_fname, msisdn, prepostStatus, cust_cni);
			Thread.sleep(1000);
			regpage.enterMSISDN(msisdn);
			regpage.clickOnSeachIcon();
			regpage.waitForElementToVisible(By.id("saveBtn"));

			regpage.enterCustomerInfo(mno, cust_fname, cust_sname, age,
					cust_cni, dob, gender);
			logger.log(LogStatus.INFO, "Entered Customer Information");
			if (!dob.equals(""))
				Thread.sleep(1000);
			regpage.selectAvailableInformation(prd_to_register);
			logger.log(LogStatus.INFO, "Selected Available product--"
					+ prd_to_register);
			if (MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION
					.contains(prd_to_register))
				regpage.selectBimaAccidentProtecionBenefitLevel(accident_level);

			regpage.enterBeneficiaryInfo(relationship, ben_fname, ben_sname,
					ben_age, ben_msisdn, ben_gender).clickOnSave();
			logger.log(LogStatus.INFO, "Entered Beneficiary Information");
			String err = regpage.getValidationMsg().trim();
			log.info("Actual Error message:" + err_msg
					+ " Error message found:" + err);
			logger.log(LogStatus.INFO, "Validating the Error Message");
			Assert.assertTrue(err.trim().replaceAll("\\s", "")
					.equalsIgnoreCase(err_msg.trim().replaceAll("\\s", "")));
			logger.log(LogStatus.INFO, "Validated Error Message");
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);

		} catch (Throwable t) {
			log.info("fieldValidationWithNegativeData--" + testcase
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
			logger.log(LogStatus.FAIL, testcaseName + "----Test Failed");
			logger.log(LogStatus.ERROR, res.getThrowable());
			logger.log(LogStatus.FAIL, logger.addScreenCapture(path));
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
		} else {
			logger.log(LogStatus.PASS, testcaseName + "---Test passed");
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