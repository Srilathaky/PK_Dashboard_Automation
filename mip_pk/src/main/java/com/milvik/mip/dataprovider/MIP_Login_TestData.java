package com.milvik.mip.dataprovider;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.DataProvider;

import com.milvik.mip.utility.MIP_XLOperation;

public class MIP_Login_TestData {
	/**
	 * This method will read the data from given XL
	 * 
	 * @return
	 */
	@DataProvider(name = "invalid_login")
	public String[][] testdataInvalidLogin() {
		Sheet s = MIP_XLOperation.loadXL("MIP_Regression_InvalidLogin");
		int numRows = MIP_XLOperation.getNumRows();
		int numcell = MIP_XLOperation.getNumCell();
		int rowcount = 0;
		String[][] data = new String[numRows - 1][numcell];
		for (int i = 1; i < numRows; i++) {
			Row r = s.getRow(i);
			for (int j = 0; j < r.getLastCellNum(); j++) {
				if (r.getCell(j) == null) {
					data[rowcount][j] = "";
				} else {
					data[rowcount][j] = r.getCell(j).getStringCellValue();
				}

			}
			rowcount++;
		}
		return data;
	}

	/**
	 * User Name,password,Error Message
	 * 
	 * @return
	 */
	@DataProvider(name = "useaccount_block")
	public String[][] testdataaccountBlock() {
		String data[][] = { {
				"DA41",
				"invalid",
				"Your login attempt was not successful, try again."
						+ "Reason: User account is locked" } };
		return data;
	}

	/**
	 * User Name,password,Error Message
	 * 
	 * @return
	 */
	@DataProvider(name = "login_blockAccount")
	public String[][] testloginaccountBlock() {
		String data[][] = { {
				"DA41",
				"Tigo123!",
				"Your login attempt was not successful, try again."
						+ "Reason: User account is locked" } };
		return data;
	}

	/**
	 * DashBoard Menu
	 * 
	 * @return
	 */
	@DataProvider(name = "logoutdata")
	public String[][] testlogoutdata() {
		String data[][] = { { "Branch Management" }, { "User Management" },
				{ "Leave Management" }, { "Customer Management" },
				{ "SMS Management" }, { "Role Management" },
				{ "Claims Management" }, { "Report Management" },
				{ "Admin Configuration" }, { "Change Password" } };
		return data;
	}

}
