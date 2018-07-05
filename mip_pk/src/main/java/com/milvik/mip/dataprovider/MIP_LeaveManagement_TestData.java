package com.milvik.mip.dataprovider;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.DataProvider;

import com.milvik.mip.utility.MIP_XLOperation;

public class MIP_LeaveManagement_TestData {
	static Sheet s;

	/**
	 * Data in the order username,from date(dd/MM/YYYY),To date,Reason
	 */
	@DataProvider(name = "applyLeaveTestData")
	public static String[][] validateApplyLeaveTestData() {
		String[][] data = { { "Carl Johan", "05/03/2018", "06/03/2018", "Sick" } };
		return data;
	}

	/**
	 * Data in the order username,from date(dd/MM/YYYY),To date,Reason,success
	 * message
	 */
	@DataProvider(name = "saveapplyLeaveTestData")
	public static String[][] saveApplyLeaveTestData() {
		String[][] data = { { "test test", "26/12/2017", "28/12/2017", "Leave",
				"" } };
		return data;
	}

	/**
	 * This method will read the data from mentioned XL
	 * 
	 * @return
	 */
	@DataProvider(name = "applyLeaveNegativeTestData")
	public static String[][] applyLeaveNegativeTestData() {
		s = MIP_XLOperation.loadXL("MIP_Apply_Leave_Negative_Scenarios");
		return storeCellData();
	}

	public static String[][] storeCellData() {
		int rownum = MIP_XLOperation.getNumRows();
		int cellnum = MIP_XLOperation.getNumCell();
		int From_Date = 0;
		int To_Date = 0;
		int rowcount = 0;
		String[][] data = new String[rownum - 1][cellnum];
		for (int i = 0; i < cellnum; i++) {
			if (s.getRow(0).getCell(i).getStringCellValue()
					.equalsIgnoreCase("From Date")) {
				From_Date = i;

			}
			if (s.getRow(0).getCell(i).getStringCellValue()
					.equalsIgnoreCase("To Date")) {
				To_Date = i;

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
						if (j == From_Date || j == To_Date) {
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

	@DataProvider(name = "viewLeaveTestData")
	public static String[][] viewLeaveTestData() {
		String[][] data = { { "01/01/2018", "28/02/2018" } };
		return data;
	}
}
