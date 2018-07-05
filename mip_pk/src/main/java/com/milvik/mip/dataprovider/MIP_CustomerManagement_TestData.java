package com.milvik.mip.dataprovider;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.DataProvider;

import com.milvik.mip.utility.MIP_XLOperation;

public class MIP_CustomerManagement_TestData {
	/**
	 * Test Data in the order -->Mobile Number
	 * 
	 */
	@DataProvider(name = "deRegisterCust")
	public String[][] deRegisterCustomerData() {
		String[][] data = { { "25185881" }, { "05145896" }, { "77027666" } };
		return data;
	}

	/**
	 * This method will read the data from the given XL
	 */
	@DataProvider(name = "msisdnValidation")
	public String[][] validateEnteredMsisdn() {
		return storecellData("MIP_CustomerManagement_MSISDN_Validation_Scenarios");
	}

	/**
	 * Mobile Number,Error message
	 * 
	 * @return
	 */
	@DataProvider(name = "coverHistoryNegativeTest")
	public String[][] coverHistoryTestData() {
		String[][] data = {
				{ "123456789", "CDMA", "No customer records found." },
				{ "123456789", "",
						"Subscriber Channel Type  :  Please select an option." } };
		return data;
	}

	/**
	 * nic,Mobile Number,Error message
	 * 
	 * @return
	 */
	@DataProvider(name = "deRegisterNegativeTest")
	public String[][] deRegisterestData() {
		String[][] data = {
				{ "invalid_msisdn", "dgf$^$D",
						"MSISDN  :  Enter a valid number." },
				{ "invalid_msisdn_length", "74859",
						"MSISDN  :  Entered text length must be of 7 characters" },
				{ "empty_msisdn", "", "MSISDN  :  Field should not be empty." },
				{ "msisdn_doent_exists", "7897897",
						"MSISDN  :  Entered value does not exist." } };
		return data;
	}

	/**
	 * Mobile Number
	 * 
	 * @return
	 */
	@DataProvider(name = "coverHistoryPositiveTest")
	public String[][] coverHistoryPositiveTest() {
		String[][] data = { { "89750980" }, { "84632100" } };
		return data;
	}

	@DataProvider(name = "msisdnSwitchProductLevel")
	public String[][] switchProductLevel() {
		String[][] data = { { "89750983" }, { "77027666" } };
		return data;
	}

	@DataProvider(name = "printpolicyDocument")
	public String[][] printpolicyDocument() {
		String[][] data = { { "196758529663", "", "GSM" } };
		return data;
	}

	@DataProvider(name = "SwitchProductLevelPositive")
	public String[][] switchProductLevelPositive() {
		String[][] data = storecellData("MIP_SwitchProductLevel_Positive_Scenario");
		return data;
	}

	@DataProvider(name = "SwitchProductLevelmsisdnValidation")
	public String[][] SwitchProductLevelmsisdnValidation() {
		String[][] data = storecellData("MIP_SwitchProductLevel_MSISDN_Validation");
		return data;
	}

	public static String[][] storecellData(String filename) {
		Sheet s = MIP_XLOperation.loadXL(filename);
		int num_row = MIP_XLOperation.getNumRows();
		int num_cell = MIP_XLOperation.getNumCell();
		int rowcount = 0;
		String[][] data = new String[num_row - 1][num_cell];
		for (int i = 1; i < num_row; i++) {
			Row r = s.getRow(i);
			for (int j = 0; j < r.getLastCellNum(); j++) {
				if (r.getCell(j) == null) {
					data[rowcount][j] = "";
				} else {
					try {
						data[rowcount][j] = r.getCell(j).getStringCellValue();
					} catch (Exception e) {
						data[rowcount][j] = new Double(r.getCell(j)
								.getNumericCellValue()).longValue() + "";
					}
				}
			}
			rowcount++;
		}
		return data;
	}

}
