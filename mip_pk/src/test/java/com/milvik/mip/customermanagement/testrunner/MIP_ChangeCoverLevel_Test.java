package com.milvik.mip.customermanagement.testrunner;

import static org.testng.Assert.assertTrue;

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
import com.milvik.mip.dataprovider.MIP_ChangeCoverLevel_TestData;
import com.milvik.mip.dataprovider.MIP_DeRegisterCustomer_TestData;
import com.milvik.mip.dbqueries.MIP_ChangeCoverLevel_Queries;
import com.milvik.mip.dbqueries.MIP_DeRegisterCustomer_Queries;
import com.milvik.mip.dbqueries.MIP_RegisterCustomer_Queries;
import com.milvik.mip.pageobjects.MIP_ChangeCoverLevelPage;
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

public class MIP_ChangeCoverLevel_Test {
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage;
	MIP_HomePage homepage;
	MIP_ChangeCoverLevelPage changecoverlevel = null;
	MIP_DeRegisterCustomerPage deregcust = null;
	MIP_QAConfirmationPage qaconfpage = null;
	String testcaseName;
	MIP_RegisterCustomerPage regpage = null;
	String username;
	String platform_ = "";

	@BeforeTest
	@Parameters({ "browser", "flag", "platform" })
	public void test_setup(@Optional("firefox") String browser,
			@Optional("0") String flag, @Optional("windows") String platform) {
		platform_ = platform;
		log = MIP_Logging.logDetails("MIP_ChangeCoverLevel_Test");
		report = new ExtentReports(
				"Test_Reports/MIP_ChangeCoverLevel_Test.html");
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
			homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_COVER_LEVEL);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			WebDriverWait w = new WebDriverWait(MIP_Test_Configuration.driver,
					600);
			w.until(ExpectedConditions.visibilityOfElementLocated((By
					.linkText(MIP_Menu_Constants.CUSTOMER))));
			homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_COVER_LEVEL);

		}
	}

	@Test(priority = 0, enabled = false, testName = "validate_changeCoverLevel", dataProvider = "validate_changeCoverLevel", dataProviderClass = MIP_ChangeCoverLevel_TestData.class)
	public void validate_changeCoverLevel(String msisdn) throws Throwable {
		String testcase = "validate_changeCoverLevel";
		testcaseName = testcase + "_" + msisdn;
		/*try {
			logger = report.startTest("validate_changeCoverLevel_" + testcase
					+ "_" + msisdn);
			changecoverlevel = PageFactory.initElements(
					MIP_Test_Configuration.driver,
					MIP_ChangeCoverLevelPage.class);
			changecoverlevel.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Change Cover Level')]"));
			changecoverlevel.waitForElementToVisible(By
					.xpath("//div[contains(text(),'All fields marked')]"));
			Thread.sleep(1000);
			changecoverlevel.enterMSISDN(msisdn);
			changecoverlevel.clickOnSeachIcon();
			changecoverlevel.waitForElementToVisible(By.id("saveBtn"));
			Map<String, String> details = changecoverlevel
					.validateChangeCoverLevelPage();
			Assert.assertTrue(details.get("msisdn").equals(msisdn.trim()));
			List<String> db_offers = MIP_RegisterCustomer_Queries
					.getRegisteredProduct(msisdn);
			if (db_offers.toString().contains(
					MIP_CustomerManagementPage.BIMALIFE)) {
				Assert.assertTrue(db_offers.toString().contains(
						details.get("bimalife")));
				String offer_level = MIP_RegisterCustomer_Queries
						.getOfferCover(msisdn,
								MIP_CustomerManagementPage.BIMALIFE);
				String dashboard_offer = details.get("bimalife_offer_level")
						.replaceAll("[$,]".trim(), "").replaceAll("\\s", "");
				Assert.assertTrue(offer_level.contains(dashboard_offer));
			}
			if (db_offers.toString().contains(
					MIP_CustomerManagementPage.BIMAHEALTH)) {
				Assert.assertTrue(db_offers.toString().contains(
						details.get("bimahealth")));
				String offer_level = MIP_RegisterCustomer_Queries
						.getOfferCover(msisdn,
								MIP_CustomerManagementPage.BIMAHEALTH);
				String dashboard_offer = details.get("bimahealth_offer_level")
						.replaceAll("[$,]".trim(), "");
				Assert.assertTrue(dashboard_offer.contains(offer_level));
			}
			homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_COVER_LEVEL);
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_COVER_LEVEL);
			log.info("RegisterCustomer---" + testcase + " Test Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}*/
	}

	@Test(priority = 1, enabled = true, testName = "changeCoverLevel", dataProvider = "changeCoverLevel", dataProviderClass = MIP_ChangeCoverLevel_TestData.class)
	public void changeCoverLevel(String testcase, String mno, String msisdn,
			String prepostStatus, String prd_to_register,
			String accident_level, String cust_fname, String cust_sname,
			String age, String cust_cni, String dob, String gender,
			String relationship, String ben_fname, String ben_sname,
			String ben_age, String ben_msisdn, String ben_gender,
			String success_message, String conf_status,
			String prd_to_change_level, String new_Accident_offer_level,
			String change_cover_succ_msg) throws Throwable {
		testcaseName = testcase + "--" + msisdn;
		String bima_accident_ben_level = "";
		try {
			username = MIP_ReadPropertyFile.getPropertyValue("username");
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
			logger = report.startTest("Change Cover Level Test--" + testcase + "--"
					+ msisdn);
			regpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_RegisterCustomerPage.class);
			regpage.waitForElementToVisible(By
					.xpath("//h3[normalize-space(text())='Register Customer']"));
			regpage.waitForElementToVisible(By
					.xpath("//div[contains(text(),'All fields marked')]"));

			if (mno.equalsIgnoreCase(MIP_CustomerManagementPage.BIMA_WARID))
				MIP_RegisterCustomer_Queries.connectToWaridDBAndInserRecord(
						platform_, cust_fname, msisdn, prepostStatus, cust_cni);
			Thread.sleep(1000);
			regpage.enterMSISDN(msisdn);
			regpage.clickOnSeachIcon();
			regpage.waitForElementToVisible(By.id("saveBtn"));
			logger.log(LogStatus.INFO, "Registering the Customer");
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
				logger.log(LogStatus.INFO, "Confirming the Customer");
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
			}

			// /////CHANGE COVER LEVEL
			logger.log(LogStatus.INFO, "Changing the Customer Cover Level");
			homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_COVER_LEVEL);
			changecoverlevel = PageFactory.initElements(
					MIP_Test_Configuration.driver,
					MIP_ChangeCoverLevelPage.class);
			changecoverlevel.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Change Cover Level')]"));
			changecoverlevel.waitForElementToVisible(By
					.xpath("//div[contains(text(),'All fields marked')]"));
			changecoverlevel.enterMSISDN(msisdn);
			changecoverlevel.clickOnSeachIcon();
			int db_offer_level_before_change_level = MIP_ChangeCoverLevel_Queries
					.getOfferDetails(msisdn, new_Accident_offer_level);
			changecoverlevel.waitForElementToVisible(By.id("saveBtn"));
			if (MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION.contains(prd_to_change_level)) {
				changecoverlevel.updateAccidentCoverLevel(
						new_Accident_offer_level).clickOnSaveChanges();
				Assert.assertTrue(changecoverlevel.getSuccessMessage()
						.equalsIgnoreCase(change_cover_succ_msg.trim()));
				changecoverlevel.waitForElementToVisible(By.linkText("here"))
						.click();
				 changecoverlevel.enterMSISDN(msisdn);
				 changecoverlevel.clickOnSeachIcon();
				 String current_cover_level=changecoverlevel.getAccidentCoverLevelInfo();
				if (conf_status.equals("1")) {
					Assert.assertEquals(db_offer_level_before_change_level,
							MIP_ChangeCoverLevel_Queries.getOfferDetails(
									msisdn, new_Accident_offer_level));
					assertTrue(MIP_ChangeCoverLevel_Queries
							.checkRecordInUpdateChangeCoverLevel(msisdn,
									prd_to_change_level));
					Map<String, String> offer_cover = MIP_ChangeCoverLevel_Queries
							.getupdateCoverLevelForConfirmedCustomer(msisdn,
									prd_to_change_level);// product
					log.info("Old Offer Level:" + " In Offer_Subscription="
							+ accident_level + " In Update Cover Level="
							+ offer_cover.get("old_offer"));
					 Assert.assertTrue(offer_cover.get("old_offer").contains(
					 accident_level.replaceAll("[a-zA-Z,\\s]","")));
					 log.info("New Offer Level:" + " In Offer_Subscription="
								+ new_Accident_offer_level + " In Update Cover Level="
								+ offer_cover.get("new_offer"));
					 Assert.assertTrue(offer_cover.get("new_offer").contains(
							 new_Accident_offer_level.replaceAll("[a-zA-Z,\\s]","")));
					 log.info("Current Cover Level After Cover Change in Change Cover Level Page:" + " In Dashboard="
								+ current_cover_level
								+ " Input Update Cover Level="
								+ accident_level);
						
					Assert.assertTrue(accident_level.replaceAll("[a-zA-Z,]","").contains(current_cover_level.replaceAll("\\s","")));

				} else {
					Assert.assertNotEquals(db_offer_level_before_change_level,
							MIP_ChangeCoverLevel_Queries.getOfferDetails(
									msisdn, new_Accident_offer_level));
					Assert.assertFalse(MIP_ChangeCoverLevel_Queries
							.checkRecordInUpdateChangeCoverLevel(msisdn,
									prd_to_change_level));
					String updated_offer_level = MIP_RegisterCustomer_Queries
							.getOfferCover(
									msisdn,
									MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION);
					log.info("Updated Offer Level:" + " In Offer_Subscription="
							+ updated_offer_level
							+ " Input Update Cover Level="
							+ new_Accident_offer_level);
					
					  Assert.assertTrue(new_Accident_offer_level.contains(updated_offer_level));
					  log.info("Current Cover Level After Cover Change in Change Cover Level Page:" + " In Dashboard="
								+ current_cover_level
								+ " Input Update Cover Level="
								+ new_Accident_offer_level);
						
					  Assert.assertTrue(new_Accident_offer_level.replaceAll("[a-zA-Z,\\s]","").contains(current_cover_level.replaceAll("\\s", "")));
				}
				
					String cover_level_sms = MIP_ChangeCoverLevel_Queries
							.getChangeCoverLevelSMS(msisdn,
									prd_to_change_level);
					String exp_sms = MIP_ChangeCoverLevel_TestData.ACCIDENT_CHANGE_COVER_LEVEL_SMS
							.replaceAll("XXXXX", accident_level.replaceAll("[a-zA-z,\\s]",""));
					exp_sms=exp_sms.replaceAll("YYYYY",new_Accident_offer_level.replaceAll("[\\sa-zA-Z]", ""));
					log.info("Expected and Found SMS:" + "Expected=" + exp_sms
							+ " Found SMS=" + cover_level_sms);
					Assert.assertTrue(cover_level_sms.replaceAll("\\s", "").equalsIgnoreCase(exp_sms.replaceAll("\\s", "")));

				}
			/*	if (offer.contains(MIP_CustomerManagementPage.BIMAHEALTH)
						&& offer.contains(MIP_CustomerManagementPage.BIMALIFE)) {
					changecoverlevel.enterMSISDN(msisdn);
					changecoverlevel.clickOnSeachIcon();
				}
				if (offer.contains(MIP_CustomerManagementPage.BIMAHEALTH)) {
					String confirmed_status = MIP_ChangeCoverLevel_Queries
							.getConfirmedStatus(msisdn,
									MIP_CustomerManagementPage.BIMAHEALTH);
					String old_offer_level = MIP_RegisterCustomer_Queries
							.getOfferCover(msisdn,
									MIP_CustomerManagementPage.BIMAHEALTH);
					changecoverlevel.updateHealthCoverLevel(health_offer_level)
							.clickOnSaveChanges();
					Assert.assertTrue(changecoverlevel.getSuccessMessage()
							.equalsIgnoreCase(success_msg.trim()));
					if (confirmed_status
							.equals(MIP_CustomerManagementPage.CONFIRMATION_STATE_AFTER_CONFIRMATION_STATUS)
							|| confirmed_status
									.equals(MIP_CustomerManagementPage.CONFIRMATION_STATE_AFTER_REACTIVATE_STATUS)) {
						Map<String, String> offer_cover = MIP_ChangeCoverLevel_Queries
								.getupdateCoverLevelForConfirmedCustomer(
										msisdn,
										MIP_CustomerManagementPage.BIMAHEALTH);
						log.info("Old Offer Level:" + " In Offer_Subscription="
								+ old_offer_level + " In Update Cover Level="
								+ offer_cover.get("old_offer"));
						Assert.assertTrue(offer_cover.get("old_offer")
								.contains(old_offer_level));
						old_offer_level = old_offer_level.split("\\.")[0];
						Assert.assertTrue(health_offer_level
								.contains(offer_cover.get("new_offer").split(
										"\\.")[0]));

					} else {
						String updated_offer_level = MIP_RegisterCustomer_Queries
								.getOfferCover(msisdn,
										MIP_CustomerManagementPage.BIMAHEALTH);
						log.info("Updated Offer Level:"
								+ " In Offer_Subscription="
								+ updated_offer_level
								+ " Input Update Cover Level="
								+ health_offer_level);
						Assert.assertTrue(health_offer_level
								.contains(updated_offer_level));
					}
					String sms = MIP_ChangeCoverLevel_Queries
							.getChangeCoverLevelSMS(msisdn,
									MIP_CustomerManagementPage.BIMAHEALTH);
					String cover_per_night = "";
					if (MIP_CustomerManagementPage.HEALTH_BENEFIT_LEVEL1
							.contains(old_offer_level)) {
						cover_per_night = MIP_ChangeCoverLevel_TestData.LEVEL1_COVER_PER_NIGHT;
					} else if (MIP_CustomerManagementPage.HEALTH_BENEFIT_LEVEL2
							.contains(old_offer_level)) {
						cover_per_night = MIP_ChangeCoverLevel_TestData.LEVEL2_COVER_PER_NIGHT;
					} else if (MIP_CustomerManagementPage.HEALTH_BENEFIT_LEVEL3
							.replaceAll(",", "").contains(old_offer_level)) {
						cover_per_night = MIP_ChangeCoverLevel_TestData.LEVEL3_COVER_PER_NIGHT;
					}
					String exp_sms = MIP_ChangeCoverLevel_TestData.HEALTH_CHANGE_COVER_LEVEL_SMS
							.replace("XXXXX", cover_per_night);
					if (MIP_ChangeCoverLevel_TestData.HP_COVER_LEVEL1
							.equalsIgnoreCase(health_offer_level)) {
						cover_per_night = MIP_ChangeCoverLevel_TestData.LEVEL1_COVER_PER_NIGHT;
					} else if (MIP_ChangeCoverLevel_TestData.HP_COVER_LEVEL2
							.equalsIgnoreCase(health_offer_level)) {
						cover_per_night = MIP_ChangeCoverLevel_TestData.LEVEL2_COVER_PER_NIGHT;
					} else if (MIP_ChangeCoverLevel_TestData.HP_COVER_LEVEL3
							.equalsIgnoreCase(health_offer_level)) {
						cover_per_night = MIP_ChangeCoverLevel_TestData.LEVEL3_COVER_PER_NIGHT;
					}

					exp_sms = exp_sms.replace("YYYYY", cover_per_night);
					log.info("Expected and Found SMS:" + "Expected=" + exp_sms
							+ " Found SMS=" + sms);
					Assert.assertTrue(sms.equalsIgnoreCase(exp_sms));
					changecoverlevel.waitForElementToVisible(
							By.linkText("here")).click();
				}
			

			}
			*/
		} catch (Throwable t) {
			log.info("RegisterCustomer---" + testcaseName + " Test Failed");
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
			logger.log(LogStatus.FAIL, "----Test Failed");
			logger.log(LogStatus.ERROR, res.getThrowable());
			logger.log(LogStatus.FAIL, logger.addScreenCapture(path));
			homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_COVER_LEVEL);
		} else {
			logger.log(LogStatus.PASS, testcaseName+"----Test passed");
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
