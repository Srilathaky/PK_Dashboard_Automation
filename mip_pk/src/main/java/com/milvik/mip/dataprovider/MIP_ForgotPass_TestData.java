package com.milvik.mip.dataprovider;

import org.testng.annotations.DataProvider;

public class MIP_ForgotPass_TestData {
	/**
	 * Logi Id,Error Message
	 * 
	 * @return
	 */
	@DataProvider
	public String[][] invaliduser() {
		String[][] data = { { "invalid",
				"The login id entered is invalid." } };
		return data;
	}

	/**
	 * Logi Id
	 * 
	 * @return
	 */
	@DataProvider
	public String[][] validinvaliduser() {
		String[][] data = { { "invalid", "DASHBOARD" } };
		return data;
	}

	/**
	 * Error message
	 * 
	 * @return
	 */
	@DataProvider
	public String[][] withoutuserid() {
		String[][] data = { { "Please enter Login ID" } };
		return data;
	}

	/**
	 * Login ID,Error message
	 * 
	 * @return
	 */
	@DataProvider
	public String[][] withoutEmail() {
		String[][] data = { {
				"DA45",
				"Sorry, we do not have your E-mail ID. Kindly contact the help-desk to reset your password." } };
		return data;
	}

}
