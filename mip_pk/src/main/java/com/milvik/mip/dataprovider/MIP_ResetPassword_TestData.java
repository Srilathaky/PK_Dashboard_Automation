package com.milvik.mip.dataprovider;

import org.testng.annotations.DataProvider;

import com.milvik.mip.dbqueries.MIP_AdminConfig_Queries;

public class MIP_ResetPassword_TestData {
	/**
	 * Test Data in the order "USer ID","Success Message",default
	 * password,invalid password,error message,valid password,success message
	 */
	@DataProvider(name = "ResetPassword")
	public String[][] resetPasswordData() {
		String defaultPass = MIP_AdminConfig_Queries.getDefaultPassword();
		String[][] data = { {
				"DA8",
				"The password has been reset and/or the account has been unlocked successfully. Click here to go back",
				defaultPass,
				"milvik1238!",
				"New Password : must match the following criteria,"
						+ "A minimum of 8 characters length."
						+ "At least one upper case alphabet."
						+ "At least one lower case alphabet."
						+ "At least one number."
						+ "At least one special character.", "MIilvik125!",
				"Your password has been changed successfully." } };
		return data;
	}

	/**
	 * User Name,Error Message
	 * 
	 * @return
	 */
	@DataProvider(name = "ResetPassNegative")
	public String[][] resetPassNegativeTestData() {
		String[][] data = {
				{ "BDERE", "No user record found for the entered User Id." },
				{ "", "User ID:Field should not be empty." } };
		return data;

	}
}
