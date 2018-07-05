package com.milvik.mip.dataprovider;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.DataProvider;

import com.milvik.mip.utility.MIP_XLOperation;

public class MIP_AddUser_TestData {

	/**
	 * Test Data in the order fname,sname,Mobile Number,Email
	 * ID,Dob,gender,Role,Branch,is_supervisor,supervisor,succ_msg
	 * 
	 */
	@DataProvider(name = "addUserData")
	public static String[][] addUserTestData() {
		String[][] data = {
				{ "user one", "one", "7218887", "test@auto.com", "28/05/1985",
						"Male", "Test", "HEAD OFFICE PORT MORESBY", "Yes",
						"Donald Mitchell", "yes",
						"The user details have been added successfully." },
				{ "user one", "one", "7218888", "test@auto.com", "28/06/1985",
						"Male", "Test", "HEAD OFFICE PORT MORESBY", "yes", "",
						"no", "The user details have been added successfully." } };
		return data;
	}

	/**
	 * This method will read the data from the given XL
	 * 
	 * @return
	 */
	@DataProvider(name = "addUserNegativeData")
	public static String[][] addUserNegativTestData() {
		Sheet s = MIP_XLOperation.loadXL("MIP_AddUser_Negative_Scenarios");
		int rownum = MIP_XLOperation.getNumRows();
		int cellnum = MIP_XLOperation.getNumCell();
		int DOB_col = 0;
		int rowcount = 0;
		String[][] data = new String[rownum - 1][cellnum];
		for (int i = 0; i < cellnum; i++) {
			if (s.getRow(0).getCell(i).getStringCellValue()
					.equalsIgnoreCase("DOB")) {
				DOB_col = i;
				break;
			}
		}
		for (int i = 1; i < rownum; i++) {
			Row r = s.getRow(i);
			for (int j = 0; j < r.getLastCellNum(); j++) {
				if (r.getCell(j) == null) {
					data[rowcount][j] = "";
				} else {
					try {
						data[rowcount][j] = r.getCell(j).getStringCellValue();
					} catch (Exception e) {
						if (j == DOB_col) {
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
