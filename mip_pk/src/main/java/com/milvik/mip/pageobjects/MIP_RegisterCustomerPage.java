package com.milvik.mip.pageobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.milvik.mip.constants.MIP_Constants;
import com.milvik.mip.pageutil.MIP_CustomerManagementPage;
import com.milvik.mip.utility.MIP_DateFunctionality;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_RegisterCustomerPage extends MIP_CustomerManagementPage {
	private WebDriver driver;
	static Logger logger;

	static {
		logger = MIP_Logging.logDetails("MIP_RegisterCustomerPage");
	}

	public MIP_RegisterCustomerPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public MIP_RegisterCustomerPage enterCustomerInfo(String selection,
			String fname, String sname, String age, String custNIC, String dob,
			String gender) {
		logger.info("Entering the customer information:fname=" + fname
				+ ",sname" + sname + ",age=" + age + ",=" + " gender=" + gender
				+ "," + " dob=" + dob + ",custNIC=" + custNIC);
		if (selection.equalsIgnoreCase(MIP_CustomerManagementPage.BIMA_JAZZ))
			this.waitForElementToVisible(By.id("custName")).sendKeys(fname);
		this.selectDropDownbyText(
				this.waitForElementToVisible(By.id("gender")), gender);
		this.waitForElementToVisible(By.id("sname")).sendKeys(sname);
		if (!age.equals(""))
			this.waitForElementToVisible(By.id("age")).sendKeys(age);
		if (selection.equalsIgnoreCase(MIP_CustomerManagementPage.BIMA_JAZZ))
			this.waitForElementToVisible(By.id("cnic")).sendKeys(custNIC);
		if (!dob.equals("")) {
			this.selectDOB(dob);
		}
		logger.info("Entered the customer information");
		return this;
	}

	public String getFirstName() {
		logger.info("Getting fname from Customer Registration Page");
		return this.waitForElementToVisible(By.id("custName")).getAttribute(
				"value");
	}

	public MIP_RegisterCustomerPage selectBimaHealthBenefitLevel(
			String benefitLevel) {
		logger.info("Selecting Bima Health Benefit Level as " + benefitLevel);
		WebElement ele = this.waitForElementToVisible(By
				.id("offerCoverIdHospital"));
		if (benefitLevel.trim().equalsIgnoreCase(HEALTH_BENEFIT_LEVEL1)) {
			this.selectDropDownbyValue(ele, "4");
		} else if (benefitLevel.trim().equalsIgnoreCase(HEALTH_BENEFIT_LEVEL2)) {
			this.selectDropDownbyValue(ele, "5");
		} else if (benefitLevel.trim().equalsIgnoreCase(HEALTH_BENEFIT_LEVEL3)) {
			this.selectDropDownbyValue(ele, "6");
		}
		logger.info("Selected Bima Health Benefit Level as " + benefitLevel);
		return this;
	}

	public MIP_RegisterCustomerPage selectBimaAccidentProtecionBenefitLevel(
			String benefitLevel) {
		logger.info("Selecting Bima Life Benefit Level as " + benefitLevel);
		WebElement ele = this.waitForElementToVisible(By.id("offerCoverId"));
		this.selectDropDownbyText(ele, benefitLevel);
		logger.info("Selected Bima Life Benefit Level as " + benefitLevel);
		return this;
	}

	public MIP_RegisterCustomerPage selectBimaAccidentProtecionBenefitLevelForReactivation(
			String benefitLevel) {
		logger.info("Selecting Bima Life Benefit Level  for reactivation as"
				+ benefitLevel);
		WebElement ele = this.waitForElementToVisible(By.id("newOfferCoverId"));
		this.selectDropDownbyText(ele, benefitLevel);
		logger.info("Selected Bima Life Benefit Level for reactivation as "
				+ benefitLevel);
		return this;
	}

	public MIP_RegisterCustomerPage enterBeneficiaryInfo(String relationship,
			String fname, String sname, String age, String ben_msisdn,
			String ben_gender) {
		logger.info("Entering the customer Beneficiary information:relationship="
				+ relationship
				+ ",fname="
				+ fname
				+ ",sname="
				+ sname
				+ ",age="
				+ age
				+ ",ben_gender="
				+ ben_gender
				+ ",ben_msisdn="
				+ ben_msisdn);
		this.waitForElementToVisible(By.id("insRelName")).sendKeys(fname);
		this.selectDropDownbyText(
				this.waitForElementToVisible(By.id("insRelation")),
				relationship);
		this.waitForElementToVisible(By.id("insRelSurName")).sendKeys(sname);
		this.waitForElementToVisible(By.id("insRelAge")).sendKeys(age);
		this.waitForElementToVisible(By.id("insRelMsisdn"))
				.sendKeys(ben_msisdn);
		this.selectDropDownbyText(
				this.waitForElementToVisible(By.id("insRelGender")), ben_gender);
		logger.info("Entered the customer Beneficiary information");
		return this;
	}

	public List<String> getRegisteredOffer() {
		List<String> offer = new ArrayList<String>();
		logger.info("Getting Registered offer of the customer");
		WebElement ele = this.waitForElementToVisible(By
				.xpath("//input[@id='offerId'][@value='1']"));
		if (ele.isSelected()) {
			offer.add(MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION);
		}

		/*
		 * ele = this.waitForElementToVisible(By
		 * .xpath("//input[@id='offerId'][@value='2']")); if (ele.isSelected())
		 * { offer.add(MIP_CustomerManagementPage.BIMAHEALTH); }
		 */
		return offer;
	}

	public Map<String, String> getBenefitLevel() {
		Map<String, String> benefit_level = new HashMap<String, String>();
		logger.info("Getting Registered offer of the customer");
		WebElement ele = this.waitForElementToVisible(By
				.xpath("//input[@id='offerId'][@value='1']"));
		if (ele.isSelected()) {
			benefit_level
					.put("bima_accident_benefit_level",
							this.getSelectedOption(
									this.waitForElementToVisible(By
											.xpath("//select[@id='offerCoverId'][@disabled]")))
									.replaceAll("[,a-zAZ]", ""));
		}

		/*
		 * ele = this.waitForElementToVisible(By
		 * .xpath("//input[@id='offerId'][@value='2']")); if (ele.isSelected())
		 * { benefit_level .put("health_benefit_level", this.getSelectedOption(
		 * this.waitForElementToVisible(By
		 * .xpath("//select[@id='offerCoverIdHospital'][@disabled]")))
		 * .replaceAll("[,$]", "")); }
		 */
		return benefit_level;
	}

	public Map<String, String> getCustomerInfo() {
		Map<String, String> cust_details = new HashMap<String, String>();
		logger.info("Getting the customer information from Register Customer Page");
		cust_details.put(
				"cust_fname",
				this.waitForElementToVisible(By.id("custName")).getAttribute(
						"value"));
		cust_details.put(
				"cust_sname",
				this.waitForElementToVisible(By.id("sname")).getAttribute(
						"value"));
		cust_details.put("cust_age", this.waitForElementToVisible(By.id("age"))
				.getAttribute("value"));
		cust_details.put("cust_nic", this
				.waitForElementToVisible(By.id("cnic")).getAttribute("value"));
		cust_details.put("cust_dob", this.waitForElementToVisible(By.id("dob"))
				.getAttribute("value"));
		cust_details.put("cust_gender", this.getSelectedOption(this
				.waitForElementToVisible(By.id("gender"))));
		return cust_details;
	}

	public Map<String, String> getBeneficiaryInfo() {
		Map<String, String> ben_details = new HashMap<String, String>();
		logger.info("Getting the Beneficiary information from Register Customer Page");
		ben_details.put("ben_relationship", this.getSelectedOption(this
				.waitForElementToVisible(By.id("insRelation"))));
		ben_details.put(
				"ben_fname",
				this.waitForElementToVisible(By.id("insRelName")).getAttribute(
						"value"));
		ben_details.put(
				"ben_age",
				this.waitForElementToVisible(By.id("insRelAge")).getAttribute(
						"value"));

		ben_details.put("ben_msisdn",
				this.waitForElementToVisible(By.id("insRelMsisdn"))
						.getAttribute("value"));
		ben_details.put("ben_gender", this.getSelectedOption(this
				.waitForElementToVisible(By.id("insRelGender"))));

		ben_details.put("ben_sname",
				this.waitForElementToVisible(By.id("insRelSurName"))
						.getAttribute("value"));
		return ben_details;
	}

	public Map<String, String> getCoachingProgramInfo() {
		Map<String, String> coaching_details = new HashMap<String, String>();
		logger.info("Getting the Coaching Program information from Register Customer Page");

		coaching_details.put("date_program_Section", this
				.waitForElementToVisible(By.id("registered_date"))
				.getAttribute("value"));

		coaching_details.put("coaching_perogram", this.getSelectedOption(this
				.waitForElementToVisible(By.id("coachingProgram"))));

		coaching_details.put("coaching_program_status", this
				.getSelectedOption(this.waitForElementToVisible(By
						.id("healthTipStatus"))));
		return coaching_details;
	}

	public void clickOnClear() {
		this.clickOnElement(By.id("clearBtn"));
	}

	public void clickOnBack() {
		this.clickOnElement(By.id("backBtn"));
	}

	public String getSuccessMsg() {
		WebDriverWait w = new WebDriverWait(driver, 60);
		return w.until(
				ExpectedConditions.visibilityOfElementLocated(By
						.id("message_div"))).getText();
	}

	public String getErrorMsg() {
		return this.waitForElementToPresent(By.id("errormessage_div"))
				.getText();
	}

	public String getValidationMsg() {
		logger.info("Getting Validation message in Customer registration page");
		return this.waitForElementToVisible(
				By.xpath("//div[@id='validationMessages']//li")).getText();
	}

	public void clickOnSeachButton() {
		logger.info("clicking on search button");
		this.waitForElementTobeClickable(By.id("searchBtn")).click();
		logger.info("Clicked on Search button");
	}

	public MIP_RegisterCustomerPage editCustomerInfo(String fname,
			String sname, String age, String custNIC, String dob, String gender) {
		logger.info("Editing the customer information:fname=" + fname
				+ ",sname" + sname + ",age=" + age + ",=" + " gender=" + gender
				+ "," + " dob=" + dob + ",custNIC=" + custNIC);
		if (!fname.equals("")) {
			WebElement ele = this.waitForElementToVisible(By.id("custName"));
			ele.clear();
			ele.sendKeys(fname);
		}
		if (!gender.equals("")) {
			this.selectDropDownbyText(
					this.waitForElementToVisible(By.id("gender")), gender);
		}
		if (!sname.equals("")) {
			WebElement ele = this.waitForElementToVisible(By.id("sname"));
			ele.clear();
			ele.sendKeys(sname);
		}
		if (!age.equals("")) {
			WebElement ele = this.waitForElementToVisible(By.id("age"));
			ele.clear();
			ele.sendKeys(age);
		}
		if (!custNIC.equals("")) {

			WebElement ele = this.waitForElementToVisible(By.id("cnic"));
			ele.clear();
			ele.sendKeys(custNIC);
		}
		if (!dob.equals("")) {
			this.selectDOB(dob);
		}
		logger.info("Edited the customer information");
		return this;
	}

	public MIP_RegisterCustomerPage editBeneficiaryInfo(String relationship,
			String fname, String sname, String age, String ben_msisdn,
			String ben_gender) {
		logger.info("Editing the customer Beneficiary information:relationship="
				+ relationship
				+ ",fname="
				+ fname
				+ ",sname="
				+ sname
				+ ",age="
				+ age
				+ ",ben_gender="
				+ ben_gender
				+ ",ben_msisdn="
				+ ben_msisdn);
		if (!fname.equals("")) {
			WebElement ele = this.waitForElementToVisible(By.id("insRelName"));
			ele.clear();
			ele.sendKeys(fname);
		}
		if (!relationship.equals("")) {
			this.selectDropDownbyText(
					this.waitForElementToVisible(By.id("insRelation")),
					relationship);
		}
		if (!sname.equals("")) {
			WebElement ele = this.waitForElementToVisible(By
					.id("insRelSurName"));
			ele.clear();
			ele.sendKeys(sname);
		}
		if (!age.equals("")) {
			WebElement ele = this.waitForElementToVisible(By.id("insRelAge"));
			ele.clear();
			ele.sendKeys(age);
		}
		if (!ben_msisdn.equals("")) {
			WebElement ele = this
					.waitForElementToVisible(By.id("insRelMsisdn"));
			ele.clear();
			ele.sendKeys(ben_msisdn);
		}
		if (!ben_gender.equals("")) {
			this.selectDropDownbyText(
					this.waitForElementToVisible(By.id("insRelGender")),
					ben_gender);
			logger.info("Entered the customer Beneficiary information");
		}
		return this;
	}

	void selectDOB(String dob) {
		logger.info("Selecting DOB in Customer Registration Page");
		this.waitForElementToVisible(By.id("calBut1")).click();
		String[] date = MIP_DateFunctionality.getDate(dob,
				MIP_Constants.DOB_FORMAT);
		Actions a = new Actions(driver);
		a.moveToElement(
				this.waitForElementToVisible(By
						.xpath("//table/tbody/tr/td/div/div[1]/table/tbody/tr/td/div/div")))
				.build().perform();
		this.waitForElementToVisible(
				By.xpath("//table/tbody/tr/td/div/div[1]/table/tbody/tr/td/div/div"))
				.click();
		WebElement ele = this.waitForElementToVisible(By
				.className("DynarchCalendar-menu-year"));
		ele.clear();
		ele.sendKeys(date[2]);
		this.waitForElementToVisible(
				By.xpath("//table[@class='DynarchCalendar-menu-mtable']/tbody//tr//td/div[contains(text(),'"
						+ date[1] + "')]")).click();
		if ((date[0].charAt(0) + "").equals("0")) {
			date[0] = date[0].charAt(1) + "";
		}
		this.waitForElementToVisible(
				By.xpath("//div[@class='DynarchCalendar-body']/table[@class='DynarchCalendar-bodyTable']/tbody//tr//td/div[@class='DynarchCalendar-day' or @class='DynarchCalendar-day DynarchCalendar-weekend'][contains(text(),'"
						+ date[0] + "')]")).click();
		WebElement ele1=this.waitForElementToVisible(By.id("dob"));
		if (!ele1.getAttribute("value").equals(dob)) {
			this.waitForElementToVisible(
					By.xpath("//div[@class='DynarchCalendar-body']/table[@class='DynarchCalendar-bodyTable']/tbody//tr//td/div[@class='DynarchCalendar-day' or @class='DynarchCalendar-day DynarchCalendar-weekend'][contains(text(),'"
							+ date[0] + "')]")).click();
		}
		/*
		 * ((JavascriptExecutor)driver).executeScript("arguments[0].click();",this
		 * .waitForElementTobeClickable( By.xpath(
		 * "//div[@class='DynarchCalendar-body']/table[@class='DynarchCalendar-bodyTable']/tbody//tr//td/div[@class='DynarchCalendar-day' or @class='DynarchCalendar-day DynarchCalendar-weekend'][contains(text(),'"
		 * + date[0] + "')]")));
		 */

	}
}
