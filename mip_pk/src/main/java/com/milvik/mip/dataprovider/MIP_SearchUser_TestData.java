package com.milvik.mip.dataprovider;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.DataProvider;

import com.milvik.mip.utility.MIP_XLOperation;

public class MIP_SearchUser_TestData {

	public static String withoutsearchcriteria = "Search User:At least one search criteria is required.";

	@DataProvider(name = "searchUserDataPositive")
	public static String[][] getSearchUserPosiive() {
		Sheet s = MIP_XLOperation.loadXL("MIP_SearchUser_Positive_Scenarios");
		return storeCellData(s);
	}

	@DataProvider(name = "searchUserDataNegative")
	public static String[][] getSearchUserNegative() {
		Sheet s = MIP_XLOperation.loadXL("MIP_SearchUser_Negative_Scenarios");
		return storeCellData(s);
	}

	/**
	 * fname,lname,msisdn,userid,role
	 * 
	 * @return
	 */
	@DataProvider(name = "SearcUserTestData")
	public static String[][] searchUSerData() {
		String[][] data = { { "test", "test", "02088811", "Role One",
				"Business Administrator" } };
		return data;
	}

	public static String[][] storeCellData(Sheet s) {
		int numrow = MIP_XLOperation.getNumRows();
		int numcell = MIP_XLOperation.getNumCell();
		int rowCount = 0;
		String[][] data = new String[numrow - 1][numcell];
		for (int i = 1; i < numrow; i++) {
			Row r = s.getRow(i);
			for (int j = 0; j < numcell; j++) {
				if (r.getCell(j) == null) {
					data[rowCount][j] = "";
				} else {
					try {
						data[rowCount][j] = r.getCell(j).getStringCellValue();
					} catch (Exception e) {
						data[rowCount][j] = new Double(r.getCell(j)
								.getNumericCellValue()).longValue() + "";
					}
				}
			}
			rowCount++;
		}
		return data;
	}
}
