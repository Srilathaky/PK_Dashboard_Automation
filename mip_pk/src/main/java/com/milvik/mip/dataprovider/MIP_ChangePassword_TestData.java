package com.milvik.mip.dataprovider;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.DataProvider;

import com.milvik.mip.utility.MIP_XLOperation;

public class MIP_ChangePassword_TestData {
	/**
	 * Data in the order UserName,Password,old password,New PAssword,Confirm
	 * Password,Success Message
	 * 
	 * @return
	 */
	@DataProvider(name = "ChangePAsswordData")
	public static String[][] chabgePassData() {
		String[][] data = { { "DA42", "Tigo1232@", "Tigo1232@", "Tigo1233@",
				"Tigo1233@", "Your password has been changed successfully." } };
		return data;
	}

	/**
	 * This method will read the data from the given XL
	 * 
	 * @return
	 */
	@DataProvider(name = "negativeTestData")
	public static String[][] changePassNegativeData() {
		Sheet s = MIP_XLOperation
				.loadXL("MIP_ChangePasword_Negative_Scenarios");
		int numRows = MIP_XLOperation.getNumRows();
		int numCell = MIP_XLOperation.getNumCell();
		String data[][] = new String[numRows - 1][numCell];
		int rowcount = 0;
		for (int i = 1; i < numRows; i++) {
			Row r = s.getRow(i);
			for (int j = 0; j < r.getLastCellNum(); j++) {
				if (r.getCell(j) == null) {
					data[rowcount][j] = "";
				} else {
					try {
						data[rowcount][j] = r.getCell(j).getStringCellValue();
					} catch (Exception e) {
						data[rowcount][j] = r.getCell(j).getNumericCellValue()
								+ "";
					}
				}
			}
			rowcount++;
		}
		return data;
	}
}
