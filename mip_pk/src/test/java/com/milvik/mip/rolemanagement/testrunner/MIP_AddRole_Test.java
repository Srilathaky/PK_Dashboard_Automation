package com.milvik.mip.rolemanagement.testrunner;

import java.util.List;
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
import com.milvik.mip.dataprovider.MIP_RoleManagement_TestData;
import com.milvik.mip.dbqueries.MIP_UserManagement_Queries;
import com.milvik.mip.dbqueries.MIP_AdminConfig_Queries;
import com.milvik.mip.dbqueries.MIP_RoleManagement_Queries;
import com.milvik.mip.pageobjects.MIP_AddRolePage;
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

public class MIP_AddRole_Test {
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage = null;
	MIP_HomePage homepage = null;
	MIP_AddRolePage addroleepage = null;

	/*@BeforeTest
	@Parameters({ "browser", "flag", "platform" })
	public void test_setup(@Optional("firefox") String browser,
			@Optional("0") String flag, @Optional("windows") String platform) {
		log = MIP_Logging.logDetails("MIP_AddRole_Test");
		report = new ExtentReports("Test_Reports/Add_Role_Test.html");
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
			homepage.clickOnMenu(MIP_Menu_Constants.ROLE);
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_ROLE);

		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.clickOnMenu(MIP_Menu_Constants.ROLE);
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_ROLE);
		}
	}

	@Test(priority = 1, testName = "TC192")
	public void roleManagement() throws Throwable {
		try {
			logger = report.startTest("Role Management-TC192");
			log.info("Running test case - TC192");
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Add Role')]"));
			addroleepage = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_AddRolePage.class);
			homepage.waitForElementToVisible(By
					.linkText(MIP_Menu_Constants.ADD_ROLE));
			Assert.assertTrue(MIP_Test_Configuration.driver.findElement(
					By.linkText(MIP_Menu_Constants.ADD_ROLE)).isDisplayed());
			homepage.waitForElementToVisible(By
					.linkText(MIP_Menu_Constants.VIEW_ROLE));
			Assert.assertTrue(MIP_Test_Configuration.driver.findElement(
					By.linkText(MIP_Menu_Constants.VIEW_ROLE)).isDisplayed());
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.ADD_ROLE);
			log.info("Testcase TC192  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 2, testName = "TC193")
	public void addRoleOne() throws Throwable {
		try {
		/*	logger = report.startTest("Add Role -TC193");
			log.info("Running test case - TC193");
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Add Role')]"));
			addroleepage = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_AddRolePage.class);
			Assert.assertTrue(addroleepage.roleName.isDisplayed());
			Assert.assertEquals(addroleepage.homeMenu.getText().trim(),
					MIP_Menu_Constants.HOME.trim());
			Assert.assertTrue(addroleepage.homeMenu_checkbox.getAttribute(
					"disabled").equalsIgnoreCase("true"));

			Assert.assertEquals(addroleepage.branchmanageMenu.getText().trim(),
					MIP_Menu_Constants.BRANCH.trim());
			Assert.assertTrue(addroleepage.branchmanageMenu_checkbox
					.isDisplayed());

			Assert.assertEquals(addroleepage.addBranchMenu.getText().trim(),
					MIP_Menu_Constants.ADD_BRANCH.trim());
			Assert.assertTrue(addroleepage.addBranch_checkbox.isDisplayed());

			Assert.assertEquals(addroleepage.listBranchMenu.getText().trim(),
					MIP_Menu_Constants.LIST_BRANCH.trim());
			Assert.assertTrue(addroleepage.listBranch_checkbox.isDisplayed());

			Assert.assertEquals(addroleepage.usermanageMenu.getText().trim(),
					MIP_Menu_Constants.USER.trim());
			Assert.assertTrue(addroleepage.usermanageMenu_checkbox
					.isDisplayed());

			Assert.assertEquals(addroleepage.adduserMenu.getText().trim(),
					MIP_Menu_Constants.ADD_USER.trim());
			Assert.assertTrue(addroleepage.addUser_checkbox.isDisplayed());

			Assert.assertEquals(addroleepage.listuserMenu.getText().trim(),
					MIP_Menu_Constants.LIST_USER.trim());
			Assert.assertTrue(addroleepage.listUser_checkbox.isDisplayed());

			Assert.assertEquals(addroleepage.searchuserMenu.getText().trim(),
					MIP_Menu_Constants.SEARCH_USER.trim());
			Assert.assertTrue(addroleepage.searchUser_checkbox.isDisplayed());

			Assert.assertEquals(
					addroleepage.resetPasswordMenu.getText().trim(),
					MIP_Menu_Constants.RESET_PASSWORD.trim());
			Assert.assertTrue(addroleepage.resetPassword_checkbox.isDisplayed());

			Assert.assertEquals(addroleepage.leavemanageMenu.getText().trim(),
					MIP_Menu_Constants.LEAVE.trim());
			Assert.assertTrue(addroleepage.leavemanageMenu_checkbox
					.isDisplayed());

			Assert.assertEquals(addroleepage.applyLeaveMenu.getText().trim(),
					MIP_Menu_Constants.APPLY_LEAVE.trim());
			Assert.assertTrue(addroleepage.applyLeave_checkbox.isDisplayed());

			Assert.assertEquals(addroleepage.viewLeaveMenu.getText().trim(),
					MIP_Menu_Constants.VIEW_LEAVE.trim());
			Assert.assertTrue(addroleepage.viewLeave_checkbox.isDisplayed());

			Assert.assertEquals(addroleepage.custmanageMenu.getText().trim(),
					MIP_Menu_Constants.CUSTOMER.trim());
			Assert.assertTrue(addroleepage.custmanageMenu_checkbox
					.isDisplayed());

			Assert.assertTrue(addroleepage.regCustMenu.getText().trim()
					.contains(MIP_Menu_Constants.REGISTER_CUSTOMER.trim()));
			Assert.assertTrue(addroleepage.regCust_checkbox.isDisplayed());

			Assert.assertEquals(addroleepage.searchCustMenu.getText().trim(),
					MIP_Menu_Constants.SEARCH_CUSTOMER.trim());
			Assert.assertTrue(addroleepage.searchCust_checkbox.isDisplayed());

			Assert.assertEquals(addroleepage.deRegCustMenu.getText().trim(),
					MIP_Menu_Constants.DE_REGISTER_CUSTOMER.trim());
			Assert.assertTrue(addroleepage.deRegCust_checkbox.isDisplayed());

			Assert.assertEquals(addroleepage.offermanageMenu.getText().trim(),
					MIP_Menu_Constants.OFFER.trim());
			Assert.assertTrue(addroleepage.offermanageMenu_checkbox
					.isDisplayed());

			Assert.assertEquals(addroleepage.print_policy_document.getText()
					.trim(), MIP_Menu_Constants.PRINT_POLICY_DOC.trim());
			Assert.assertTrue(addroleepage.print_policy_document_checkbox
					.isDisplayed());

			Assert.assertEquals(addroleepage.update_home_protection_cover_level
					.getText().trim(),
					MIP_Menu_Constants.UPDATE_HOME_PROTECTION_COVER_LEVEL
							.trim());
			Assert.assertTrue(addroleepage.update_home_protection_checkbox
					.isDisplayed());

			Assert.assertEquals(addroleepage.update_hp_cover_level.getText()
					.trim(), MIP_Menu_Constants.UPDATE_HP_COVER_LEVEL.trim());
			Assert.assertTrue(addroleepage.update_hp_cover_level_checkbox
					.isDisplayed());

			Assert.assertEquals(addroleepage.update_PA_2017_cover_level
					.getText().trim(),
					MIP_Menu_Constants.UPDATE_PA_2017_COVER_LEVEL.trim());
			Assert.assertTrue(addroleepage.update_PA_2017_checkbox
					.isDisplayed());

			Assert.assertEquals(addroleepage.update_life_2017_cover_level
					.getText().trim(),
					MIP_Menu_Constants.UPDATE_LIFE_2017_COVER_LEVEL.trim());
			Assert.assertTrue(addroleepage.update_life_2017_checkbox
					.isDisplayed());

			Assert.assertEquals(addroleepage.coverHistMenu.getText().trim(),
					MIP_Menu_Constants.COVER_HISTORY.trim());
			Assert.assertTrue(addroleepage.coverHist_checkbox.isDisplayed());

			Assert.assertEquals(addroleepage.smsmanageMenu.getText().trim(),
					MIP_Menu_Constants.SMS.trim());
			Assert.assertTrue(addroleepage.smsmanageMenu_checkbox.isDisplayed());

			Assert.assertEquals(addroleepage.editSmsMenu.getText().trim(),
					MIP_Menu_Constants.EDIT_SMS_TEMPLATE.trim());
			Assert.assertTrue(addroleepage.editSmsMenu.isDisplayed());

			Assert.assertEquals(addroleepage.rolemanageMenu.getText().trim(),
					MIP_Menu_Constants.ROLE.trim());
			Assert.assertTrue(addroleepage.rolemanageMenu_checkbox
					.isDisplayed());

			Assert.assertEquals(addroleepage.addRoleMenu.getText().trim(),
					MIP_Menu_Constants.ADD_ROLE.trim());
			Assert.assertTrue(addroleepage.addRole_checkbox.isDisplayed());

			Assert.assertEquals(addroleepage.viewRoleMenu.getText().trim(),
					MIP_Menu_Constants.VIEW_ROLE.trim());
			Assert.assertTrue(addroleepage.viewRole_checkbox.isDisplayed());

			Assert.assertEquals(addroleepage.reportmanageMenu.getText().trim(),
					MIP_Menu_Constants.REPORT.trim());
			Assert.assertTrue(addroleepage.reportmanageMenu_checkbox
					.isDisplayed());

			Assert.assertEquals(addroleepage.custReportMenu.getText().trim(),
					MIP_Menu_Constants.CUST_REPORT.trim());
			Assert.assertTrue(addroleepage.custReport_checkbox.isDisplayed());

			Assert.assertEquals(addroleepage.agentReportMenu.getText().trim(),
					MIP_Menu_Constants.AGENT_REPORT.trim());
			Assert.assertTrue(addroleepage.agentReport_checkbox.isDisplayed());

			Assert.assertEquals(addroleepage.progressReportMenu.getText()
					.trim(), MIP_Menu_Constants.PROGRESS_REPORT.trim());
			Assert.assertTrue(addroleepage.progressReport_checkbox
					.isDisplayed());

			Assert.assertEquals(addroleepage.adminConfigMenu.getText().trim(),
					MIP_Menu_Constants.ADMIN_CONFIG.trim());
			Assert.assertTrue(addroleepage.adminConfig_checkbox.isDisplayed());

			Assert.assertEquals(addroleepage.changePassMenu.getText().trim(),
					MIP_Menu_Constants.CHANGE_PASSWORD.trim());
			Assert.assertTrue(addroleepage.changePass_checkbox.getAttribute(
					"disabled").equalsIgnoreCase("true"));

			Assert.assertEquals(addroleepage.logoutMenu.getText().trim(),
					MIP_Menu_Constants.LOGOUT.trim());
			Assert.assertTrue(addroleepage.logout_checkbox.getAttribute(
					"disabled").equalsIgnoreCase("true"));
			Assert.assertTrue(addroleepage.save.isDisplayed());
			Assert.assertTrue(addroleepage.clear.isDisplayed());
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_ROLE);
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.ADD_ROLE);
			log.info("Testcase TC193  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 10, testName = "TC194", dataProvider = "addroleTestData", dataProviderClass = MIP_RoleManagement_TestData.class)
	public void addRoleTwo(String testcase, String roleName, String comission,
			String addbranch, String listbranch, String adduser,
			String listuser, String searchuser, String resetpassword,
			String applyleave, String viewleave, String regCust,
			String channel_Editable, String print_policy, String searchCust,
			String deregCust, String update_hp, String update_homeprotection,
			String update_PA, String update_Life, String coverHist,
			String editsms, String list_offer, String addRole, String viewRole,
			String custReport, String agentReport, String progressReport,
			String adminConfig, String msg, String fname, String sname,
			String mobilenum, String email, String dob, String gender,
			String role, String branch, String is_supervisor,
			String supervisor_name, String succmsg) throws Throwable {
		try {
			logger = report.startTest("Add Role-TC194");
			log.info("Running test case -" + testcase);
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Add Role')]"));
			addroleepage = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_AddRolePage.class);
			Thread.sleep(1000);
			addroleepage.enterRoleName(roleName);
			addroleepage.enterComission(comission);
			if (addbranch.equalsIgnoreCase("True"))
				addroleepage.selectAddBranch();
			if (listbranch.equalsIgnoreCase("True"))
				addroleepage.selectListBranch();
			if (adduser.equalsIgnoreCase("True"))
				addroleepage.selectAddUser();
			if (listuser.equalsIgnoreCase("True"))
				addroleepage.selectListUser();
			if (searchuser.equalsIgnoreCase("True"))
				addroleepage.selectSearchUser();
			if (resetpassword.equalsIgnoreCase("True"))
				addroleepage.selectresetPassword();
			if (applyleave.equalsIgnoreCase("True"))
				addroleepage.selectapplyLeave();
			if (viewleave.equalsIgnoreCase("True"))
				addroleepage.selectviewLeave();
			if (regCust.equalsIgnoreCase("True")) {
				addroleepage.selectregsCust();
				addroleepage.selectChannelEditable();
			}
			if (searchCust.equalsIgnoreCase("True"))
				;
			addroleepage.selectsearchCust();
			if (deregCust.equalsIgnoreCase("True"))
				addroleepage.selectdeRegsCust();
			if (print_policy.equalsIgnoreCase("True"))
				addroleepage.selectprintpolicy();
			if (coverHist.equalsIgnoreCase("True"))
				addroleepage.selectcoverHistry();
			if (update_hp.equalsIgnoreCase("True"))
				addroleepage.selectupdatehpCoverLevel();
			if (update_homeprotection.equalsIgnoreCase("True"))
				addroleepage.selectupdateHomeProtectionCoverLevel();
			if (update_PA.equalsIgnoreCase("True"))
				addroleepage.selectupdatePA2017CoverLevel();
			;
			if (update_Life.equalsIgnoreCase("True"))
				addroleepage.selectupdateLife2017CoverLevel();
			if (editsms.equalsIgnoreCase("True"))
				addroleepage.selecteditSms();
			if (addRole.equalsIgnoreCase("True"))
				addroleepage.selectaddRole();
			if (viewRole.equalsIgnoreCase("True"))
				addroleepage.selectviewRole();
			if (custReport.equalsIgnoreCase("True"))
				addroleepage.selectCustReport();
			if (agentReport.equalsIgnoreCase("True"))
				addroleepage.selectAgentReport();
			if (progressReport.equalsIgnoreCase("True"))
				addroleepage.selectProgressReport();
			if (list_offer.equalsIgnoreCase("True"))
				addroleepage.selectListOffer();
			if (adminConfig.equalsIgnoreCase("True"))
				addroleepage.selectAdminConfig();

			addroleepage.clickOnSave();
			addroleepage.confirmAddRole("yes");

			Assert.assertTrue(addroleepage.getSuccessMessage().trim()
					.replaceAll("\\s", "")
					.contains(msg.trim().replaceAll("\\s", "")));

			Assert.assertTrue(MIP_RoleManagement_Queries.getRoleName(roleName));
			addroleepage.waitForElementToVisible(By
					.linkText(MIP_Menu_Constants.USER));
			homepage.clickOnMenu(MIP_Menu_Constants.USER);
			addroleepage.waitForElementToVisible(By
					.linkText(MIP_Menu_Constants.ADD_USER));
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_USER);
			MIP_AddUserPage adduserpage = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_AddUserPage.class);

			adduserpage.enterUserInfo(fname, sname, mobilenum, email, dob,
					gender, role.trim(), branch.trim(), is_supervisor,
					supervisor_name).clickOnSave();
			adduserpage.confirmUser("yes");
			Assert.assertTrue(adduserpage.getmessage().contains(succmsg));
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
					branch.trim()));

			Assert.assertTrue(userDetails.get("user_uid").contains(
					MIP_AdminConfig_Queries.getLoginPrefix().trim()));
			Assert.assertTrue(userDetails.get("is_supervisor")
					.equalsIgnoreCase(is_supervisor));
			if (is_supervisor.equalsIgnoreCase("no"))
				Assert.assertTrue(userDetails.get("supervisor_name")
						.equalsIgnoreCase(supervisor_name));
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

			List<String> rolemap = MIP_RoleManagement_Queries
					.getMappedRoles(roleName);
			if (addbranch.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(
						MIP_Menu_Constants.ADD_BRANCH));
			if (listbranch.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(
						MIP_Menu_Constants.LIST_BRANCH));
			if (adduser.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(
						MIP_Menu_Constants.ADD_USER));
			if (listuser.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(
						MIP_Menu_Constants.LIST_USER));
			if (searchuser.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(
						MIP_Menu_Constants.SEARCH_USER));
			if (resetpassword.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(
						MIP_Menu_Constants.RESET_PASSWORD));
			if (applyleave.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(
						MIP_Menu_Constants.APPLY_LEAVE));
			if (viewleave.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(
						MIP_Menu_Constants.VIEW_LEAVE));
			if (regCust.equalsIgnoreCase("True")) {
				Assert.assertTrue(rolemap.toString().contains(
						MIP_Menu_Constants.REGISTER_CUSTOMER));
			}
			if (searchCust.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(
						MIP_Menu_Constants.SEARCH_CUSTOMER));
			if (deregCust.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(
						MIP_Menu_Constants.DE_REGISTER_CUSTOMER));
			if (print_policy.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(
						MIP_Menu_Constants.PRINT_POLICY_DOC));
			if (coverHist.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(
						MIP_Menu_Constants.COVER_HISTORY));
			if (update_hp.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(
						"Update HP CoverLevel")
						|| rolemap.toString().contains("Update HP Cover Level"));
			if (update_homeprotection.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(
						("Update Home Protection CoverLevel"))
						|| rolemap.toString().contains(
								("Update Home Protection Cover Level")));
			if (update_PA.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(
						"Update PA 2017 CoverLevel")
						|| rolemap.toString().contains(
								"Update PA 2017 Cover Level"));
			if (update_Life.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(
						"Update Life 2017 CoverLevel"));
			if (editsms.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(
						MIP_Menu_Constants.EDIT_SMS_TEMPLATE));
			if (addRole.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(
						MIP_Menu_Constants.ADD_ROLE));
			if (viewRole.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(
						MIP_Menu_Constants.VIEW_ROLE));

			if (custReport.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(
						MIP_Menu_Constants.CUST_REPORT));
			if (agentReport.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(
						MIP_Menu_Constants.AGENT_REPORT));
			if (progressReport.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(
						MIP_Menu_Constants.PROGRESS_REPORT));
			if (adminConfig.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(
						MIP_Menu_Constants.ADMIN_CONFIG));

			if (list_offer.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(
						MIP_Menu_Constants.LIST_OFFERS));
			homepage.clickOnMenu(MIP_Menu_Constants.ROLE);
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_ROLE);
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.ROLE);
			// homepage.clickOnMenu(MIP_Menu_Constants.ADD_ROLE);
			log.info("Testcase TC194  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 3, testName = "TC195", dataProvider = "addroleNegativeTestData", dataProviderClass = MIP_RoleManagement_TestData.class)
	public void addRoleThree(String testcase, String roleName,
			String comission, String addbranch, String listbranch,
			String adduser, String listuser, String searchuser,
			String resetpassword, String applyleave, String viewleave,
			String regCust, String channel_Editable, String print_policy,
			String searchCust, String deregCust, String update_hp,
			String update_homeprotection, String update_PA, String update_Life,
			String coverHist, String editsms, String list_offer,
			String addRole, String viewRole, String custReport,
			String agentReport, String progressReport, String adminConfig,
			String msg) throws Throwable {
		try {
			logger = report.startTest("Add Role-TC195");
			log.info("Running test case TC195");
			try {
				homepage.waitForElementToVisible(By
						.xpath("//h3[contains(text(),'Add Role')]"));
			} catch (Exception e) {
				homepage.clickOnMenu(MIP_Menu_Constants.ADD_ROLE);
			}
			addroleepage = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_AddRolePage.class);
			addroleepage.waitForElementToVisible(By.id("roleName"));
			addroleepage.enterRoleName(roleName);
			addroleepage.enterComission(comission);
			if (addbranch.equalsIgnoreCase("True"))
				addroleepage.selectAddBranch();
			if (listbranch.equalsIgnoreCase("True"))
				addroleepage.selectListBranch();
			if (adduser.equalsIgnoreCase("True"))
				addroleepage.selectAddUser();
			if (listuser.equalsIgnoreCase("True"))
				addroleepage.selectListUser();
			if (searchuser.equalsIgnoreCase("True"))
				addroleepage.selectSearchUser();
			if (resetpassword.equalsIgnoreCase("True"))
				addroleepage.selectresetPassword();
			if (applyleave.equalsIgnoreCase("True"))
				addroleepage.selectapplyLeave();
			if (viewleave.equalsIgnoreCase("True"))
				addroleepage.selectviewLeave();
			if (regCust.equalsIgnoreCase("True"))
				addroleepage.selectregsCust();

			if (searchCust.equalsIgnoreCase("True"))
				addroleepage.selectsearchCust();
			if (deregCust.equalsIgnoreCase("True"))
				addroleepage.selectdeRegsCust();
			if (print_policy.equalsIgnoreCase("True"))
				addroleepage.selectprintpolicy();
			if (update_hp.equalsIgnoreCase("True"))
				addroleepage.selectupdatehpCoverLevel();
			if (update_homeprotection.equalsIgnoreCase("True"))
				addroleepage.selectupdateHomeProtectionCoverLevel();
			if (update_PA.equalsIgnoreCase("True"))
				addroleepage.selectupdatePA2017CoverLevel();
			if (update_Life.equalsIgnoreCase("True"))
				addroleepage.selectupdateLife2017CoverLevel();
			if (coverHist.equalsIgnoreCase("True"))
				addroleepage.selectcoverHistry();
			if (editsms.equalsIgnoreCase("True"))
				addroleepage.selecteditSms();
			if (addRole.equalsIgnoreCase("True"))
				addroleepage.selectaddRole();
			if (viewRole.equalsIgnoreCase("True"))
				addroleepage.selectviewRole();
			if (custReport.equalsIgnoreCase("True"))
				addroleepage.selectCustReport();
			if (agentReport.equalsIgnoreCase("True"))
				addroleepage.selectAgentReport();
			if (progressReport.equalsIgnoreCase("True"))
				addroleepage.selectProgressReport();
			if (list_offer.equalsIgnoreCase("True"))
				addroleepage.selectListOffer();
			if (adminConfig.equalsIgnoreCase("True"))
				addroleepage.selectAdminConfig();
			addroleepage.clickOnClear();
			if (addbranch.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.addBranch_checkbox.isSelected());
			if (listbranch.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.listBranch_checkbox
						.isSelected());
			if (adduser.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.addUser_checkbox.isSelected());
			if (listuser.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.listUser_checkbox.isSelected());
			if (searchuser.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.searchCust_checkbox
						.isSelected());
			if (resetpassword.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.resetPassword_checkbox
						.isSelected());
			if (applyleave.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.applyLeave_checkbox
						.isSelected());
			if (viewleave.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.viewLeave_checkbox.isSelected());
			if (regCust.equalsIgnoreCase("True")) {
				Assert.assertFalse(addroleepage.regCust_checkbox.isSelected());
				if (channel_Editable.equalsIgnoreCase("true")) {
					Assert.assertFalse(addroleepage.waitForElementToVisible(
							By.id("isChannelAccess")).isSelected());
				}
			}
			if (searchCust.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.searchCust_checkbox
						.isSelected());
			if (deregCust.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.deRegCust_checkbox.isSelected());
			if (print_policy.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.print_policy_document_checkbox
						.isSelected());
			if (update_hp.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.update_hp_cover_level_checkbox
						.isSelected());
			if (update_homeprotection.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.update_home_protection_checkbox
						.isSelected());
			if (update_PA.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.update_PA_2017_checkbox
						.isSelected());
			if (update_Life.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.update_life_2017_checkbox
						.isSelected());
			if (coverHist.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.coverHist_checkbox.isSelected());
			if (editsms.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.editSms_checkbox.isSelected());
			if (addRole.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.addRole_checkbox.isSelected());
			if (viewRole.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.viewRole_checkbox.isSelected());
			if (custReport.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.custReport_checkbox
						.isSelected());
			if (progressReport.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.progressReport_checkbox
						.isSelected());
			if (list_offer.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.listoffer_checkbox.isSelected());
			if (adminConfig.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.adminConfig_checkbox
						.isSelected());
			Assert.assertTrue(addroleepage.roleName.getText().equals(""));
			Assert.assertTrue(addroleepage.homeMenu_checkbox.isSelected());
			Assert.assertTrue(addroleepage.changePass_checkbox.isSelected());
			Assert.assertTrue(addroleepage.logout_checkbox.isSelected());
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_ROLE);
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.ADD_ROLE);
			log.info("Testcase TC195  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC196-TC197", dataProvider = "addroleNegativeTestData", dataProviderClass = MIP_RoleManagement_TestData.class)
	public void addRoleFour(String testcase, String roleName, String comission,
			String addbranch, String listbranch, String adduser,
			String listuser, String searchuser, String resetpassword,
			String applyleave, String viewleave, String regCust,
			String channel_Editable, String print_policy, String searchCust,
			String deregCust, String update_hp, String update_homeprotection,
			String update_PA, String update_Life, String coverHist,
			String editsms, String list_offer, String addRole, String viewRole,
			String custReport, String agentReport, String progressReport,
			String adminConfig, String msg) throws Throwable {
		try {
			logger = report.startTest("Add Role-TC196-TC197");
			log.info("Running test case " + testcase);
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Add Role')]"));
			addroleepage = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_AddRolePage.class);
			addroleepage.enterRoleName(roleName);
			addroleepage.enterComission(comission);
			if (addbranch.equalsIgnoreCase("True"))
				addroleepage.selectAddBranch();
			if (listbranch.equalsIgnoreCase("True"))
				addroleepage.selectListBranch();
			if (adduser.equalsIgnoreCase("True"))
				addroleepage.selectAddUser();
			if (listuser.equalsIgnoreCase("True"))
				addroleepage.selectListUser();
			if (searchuser.equalsIgnoreCase("True"))
				addroleepage.selectSearchUser();
			if (resetpassword.equalsIgnoreCase("True"))
				addroleepage.selectresetPassword();
			if (applyleave.equalsIgnoreCase("True"))
				addroleepage.selectapplyLeave();
			if (viewleave.equalsIgnoreCase("True"))
				addroleepage.selectviewLeave();
			if (regCust.equalsIgnoreCase("True")) {
				addroleepage.selectregsCust();
				if (channel_Editable.equalsIgnoreCase("True")) {
					addroleepage.selectChannelEditable();
				}
			}
			if (searchCust.equalsIgnoreCase("True"))
				addroleepage.selectsearchCust();
			if (deregCust.equalsIgnoreCase("True"))
				addroleepage.selectdeRegsCust();
			if (print_policy.equalsIgnoreCase("True"))
				addroleepage.selectprintpolicy();
			if (update_hp.equalsIgnoreCase("True"))
				addroleepage.selectupdatehpCoverLevel();
			if (update_homeprotection.equalsIgnoreCase("True"))
				addroleepage.selectupdateHomeProtectionCoverLevel();
			if (update_PA.equalsIgnoreCase("True"))
				addroleepage.selectupdatePA2017CoverLevel();
			if (update_Life.equalsIgnoreCase("True"))
				addroleepage.selectupdateLife2017CoverLevel();
			if (coverHist.equalsIgnoreCase("True"))
				addroleepage.selectcoverHistry();
			if (editsms.equalsIgnoreCase("True"))
				addroleepage.selecteditSms();
			if (addRole.equalsIgnoreCase("True"))
				addroleepage.selectaddRole();
			if (viewRole.equalsIgnoreCase("True"))
				addroleepage.selectviewRole();
			if (custReport.equalsIgnoreCase("True"))
				addroleepage.selectCustReport();
			if (agentReport.equalsIgnoreCase("True"))
				addroleepage.selectAgentReport();
			if (progressReport.equalsIgnoreCase("True"))
				addroleepage.selectProgressReport();
			if (list_offer.equalsIgnoreCase("True"))
				addroleepage.selectListOffer();
			if (adminConfig.equalsIgnoreCase("True"))
				addroleepage.selectAdminConfig();
			addroleepage.clickOnSave();
			Thread.sleep(1000);
			Assert.assertTrue(addroleepage.getValidationMessage().trim()
					.replaceAll("\\s", "")
					.equalsIgnoreCase(msg.trim().replaceAll("\\s", "")));
		} catch (Throwable t) {
			// homepage.clickOnMenu(MIP_Menu_Constants.ADD_ROLE);
			log.info("Testcase TC196-TC197  Failed");
			log.info("Error occured in the test case", t);
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
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_ROLE);
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
			homepage.clickOnMenu(MIP_Menu_Constants.ROLE);
		report.endTest(logger);
		report.flush();
	}*/

}
