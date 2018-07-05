package com.milvik.mip.dataprovider;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.DataProvider;

import com.milvik.mip.utility.MIP_XLOperation;

/**
 * @author srilatha_yajnanaraya
 *
 */
public class MIP_RegisterCustomer_TestData {
	public static final String BIMA_ACCIDENT_REGISTRATION_SMS = "Moaziz Sarif, apni Bima hadsati tahafuz ki registeration ki tasdeeq aur qawaid o zawabid qabool karnay kay liye is SMS ka reply karein aur milvikpk.com par jain";
	public static final String DEFAULT_COACHING_PROGRAM ="Stay Healthy";
	@DataProvider(name = "RegisterCustomer")
	public String[][] registerCustomer() {
		return storeCellData("MIP_RegisterCustomer");
	}

	
	/**
	 * error message,
	 * 
	 * @return
	 */
	@DataProvider(name = "RegisterCustomer_FieldValidation")
	public String[][] RegisterCustomer_FieldValidation() {
		return storeCellData("RegisterCustomer_FieldValidation");
	}

	@DataProvider(name = "changeCustomerDetails")
	public String[][] changeCustomerDetails() {
		return storeCellData("MIP_ChangeCustomerDetails");
	}

	/**
	 * Pass invalid data data to all the fields. testcase name,channel, invalid
	 * data,error message
	 * 
	 * @return
	 */

	@DataProvider(name = "registerCustomermandatoryFieldValidation")
	public String[][] registerCustomermandatoryFieldValidation() {
		String validation_msg = "First Name  :  Field should not be empty."
				+ "Surname  :  Field should not be empty."
				+ "Gender : Please choose a value from the Drop down."
				+ "Beneficiary Gender : Please choose a value from the Drop down."
				+ "Beneficiary MSISDN  :  Field should not be empty."
				+ "Age  :  Field should not be empty."
				+ "Relationship with Customer : Please choose a value from the Drop down."
				+ "Beneficiary Name  :  Field should not be empty."
				+ "Beneficiary Surname  :  Field should not be empty."
				+ "Beneficiary Age  :  Field should not be empty."
				+ "Available Offers  :  Please select a CheckBox.";
		String msg2 = "First Name  :  Field should not be empty."
				+ "Surname  :  Field should not be empty."
				+ "Gender : Please choose a value from the Drop down."
				+ "Beneficiary Gender : Please choose a value from the Drop down."
				+ "Beneficiary MSISDN  :  Field should not be empty."
				+ "Age  :  Field should not be empty."
				+ "Relationship with Customer : Please choose a value from the Drop down."
				+ "Beneficiary Name  :  Field should not be empty."
				+ "Beneficiary Surname  :  Field should not be empty."
				+ "Beneficiary Age  :  Field should not be empty."
				+ "Life Benefit Level  :  Please choose a value from the Drop down.";
		String[][] data = { { "03028987987", validation_msg, msg2 } };
		return data;
	}

	public static String[][] storeCellData(String filename) {
		Sheet s = MIP_XLOperation.loadXL(filename);
		int numRows = MIP_XLOperation.getNumRows();
		int numcell = MIP_XLOperation.getNumCell();
		int rowcount = 0;
		List<String> DOB_col = new ArrayList<String>();
		for (int i = 0; i < numcell; i++) {

			if (s.getRow(0).getCell(i).getStringCellValue().toUpperCase()
					.contains("DOB")) {
				DOB_col.add(i + "");
			}
		}
		String[][] data = new String[numRows - 1][numcell];
		for (int i = 1; i < numRows; i++) {
			Row r = s.getRow(i);
			for (int j = 0; j < numcell; j++) {
				if (r.getCell(j) == null) {
					data[rowcount][j] = "";
				} else {
					try {
						data[rowcount][j] = r.getCell(j).getStringCellValue();
					} catch (Exception ex) {
						if (DOB_col.contains(j + "")) {
							DataFormatter df = new DataFormatter();
							data[rowcount][j] = df
									.formatCellValue(r.getCell(j));
						} else {
							data[rowcount][j] = new Double(r.getCell(j)
									.getNumericCellValue()).longValue() + "";
						}

					}
				}

			}
			rowcount++;
		}
		return data;
	}

}
