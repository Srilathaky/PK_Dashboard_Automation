package com.milvik.mip.dataprovider;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.DataProvider;

import com.milvik.mip.utility.MIP_XLOperation;

public class MIP_SearchCustomer_TestData {
	/**
	 * This method will read the data from specified XL
	 * 
	 * @return
	 */
	@DataProvider(name = "SearchCustomerNegative")
	public static String[][] searchCustomerNegative() {
		Sheet s = MIP_XLOperation
				.loadXL("MIP_Search_Customer_Negative_Scenarios");
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
	 * testcase fname,sname,mobile number
	 * 
	 * @return
	 */
	@DataProvider(name = "searchCriteria")
	public static String[][] searchData() {
		String[][] data = { { "Search_BY_Fname_Sname", "robert", "chris", "" },
				{ "Search_By_FName", "Esther", "", "" },
				{ "Search_By_Sname", "", "Dolodolotawake", "" },
				{ "Search_By_Mobile_Number", "", "", "7325808" },
				{ "Search_by_All", "Maria", "Vui", "7889582" } };
		return data;
	}

	/**
	 * testcase,msisdn,fname,sname
	 */
	@DataProvider(name = "searchAndValidateCust")
	public static String[][] searchAndValidateCust() {
		String[][] data = { { "Search_By_Msisdn", "", "", "7209655" },
				{ "Search_By_Name", "Verenaisi", "", "" },
				{ "Search_By_Msisdn", "", "", "7420112" }, };
		return data;
	}
}
