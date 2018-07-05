package com.milvik.mip.dataprovider;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.DataProvider;

import com.milvik.mip.utility.MIP_XLOperation;

public class MIP_RoleManagement_TestData {
	static Sheet s;

	/**
	 * This method will read the data from specified XL
	 * 
	 * @return
	 */
	@DataProvider(name = "addroleTestData")
	public static String[][] addRolePositiveScenario() {
		s = MIP_XLOperation.loadXL("MIP_Add_Role_positive_scenarios");
		return storeCellData();
	}

	/**
	 * This method will read the data from specified XL
	 * 
	 * @return
	 */
	@DataProvider(name = "addroleNegativeTestData")
	public static String[][] addRoleNegativeScenario() {
		s = MIP_XLOperation.loadXL("MIP_Add_Role_Negative_scenarios");
		return storeCellData();
	}

	/**
	 * Role
	 * 
	 * @return
	 */
	@DataProvider(name = "viewRoleTestData")
	public static String[][] ViewRoleTestData() {
		String[][] data = { { "Dialog Retailers" }, { "Role Name two" },
				{ "Human Resource" } };
		return data;
	}

	/**
	 * This method will read the data from specified XL
	 * 
	 * @return
	 */
	@DataProvider(name = "viewRoleUpdate")
	public static String[][] updateViewRole() {
		s = MIP_XLOperation.loadXL("MIP_View_Role_Positive_scenarios");
		return storeCellData();
	}

	public static String[][] storeCellData() {
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
