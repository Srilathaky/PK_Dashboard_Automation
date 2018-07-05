package com.milvik.mip.dataprovider;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.DataProvider;

import com.milvik.mip.utility.MIP_XLOperation;

public class MIP_SMSManagement_TestData {

	/**
	 * Data in the order Template Name,Content,SMS validity
	 * 
	 * @return
	 */
	@DataProvider(name = "EditSMSTestData")
	public static String[][] editSMS() {
		String[][] data = { {
				"confirmation_failure",
				"Dear customer, your registration could not be processed due to technical problems. Please try again in five minutes..",
				"4",
				"The SMS Template has been saved successfully. Click here to go back" } };
		return data;
	}

	/**
	 * Data in the order Template Name,Content,SMS validity
	 * 
	 * @return
	 */
	@DataProvider(name = "EditSMSErrorMessage")
	public static String[][] preview() {
		String[][] data = { { "confirmation_failure", "preview test" } };
		return data;
	}

	/**
	 * template name, content field validation message,
	 * 
	 * @return
	 */
	@DataProvider(name = "EditSMSContentErrorMessage")
	public static String[][] EditSMSContentErrorMessage() {
		String[][] data = { {
				"confirmation_failure",
				"Content :  Field should not be empty.",
				"Content :  SMS Content should contain all the place holders in the list provided." } };
		return data;
	}

	/**
	 * template Name,validity,error message
	 * 
	 * @return
	 */
	@DataProvider(name = "EditSMSValidtyErrorMessage")
	public static String[][] EditSMSValidtyErrorMessage() {
		String[][] data = { { "registration_first_reminder", "as",
				"SMS Validity :  Enter a valid number." } };
		return data;
	}

	public static String[][] storeData(String filename) {
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

	/**
	 * Msisdn,errormessage
	 * 
	 * @return
	 */

}
