package com.milvik.mip.dataprovider;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.DataProvider;

import com.milvik.mip.dbqueries.MIP_BranchManagement_Queries;
import com.milvik.mip.utility.MIP_XLOperation;

public class MIP_BranchManagement_TestData {

	/**
	 * Data in the order Branch Name,Street,Region,City,conf_msg
	 */
	@DataProvider(name = "addBranchData")
	public String[][] addBranchDetails() {
		String[][] data = { { "BC066",
				"Edificio Ofiplaza, Torre 5, Piso 3, #531", "Managua", "Nicau",
				"The branch details have been added successfully. Click here to go back" } };
		return data;
	}

	/**
	 * Data in the order Branch Name,new branch name,new Street,new Region,new
	 * City,
	 */
	@DataProvider(name = "editBranchData")
	public String[][] listBranchDetails() {
		String[][] data = { { "Dialog branch Jaffna",
				"BIMA Dialog branch Jaffna",
				"No. 141, Stanley Road, Jaffna old", "Northern old",
				"Jaffna old" } };
		return data;
	}

	/**
	 * Branch Name
	 * 
	 * @return
	 */
	@DataProvider(name = "branchName")
	public String[][] BranchName() {
		String[][] data = { { "Dialog branch badulla" },
				{ "Dialog branch co-3" } };

		return data;
	}

	@DataProvider(name = "deactivateNonAssociatedBranch")
	public String[][] deactivateNonAssociatedBranch() {
		String[][] data = { { "Dialog coner shop" } };
		return data;
	}

	/**
	 * Branch Name,Error message
	 * 
	 * @return
	 */
	@DataProvider(name = "deactivateassociatedbranch")
	public String[][] deactivateBranch() {
		String branchName = "Dialog dematagoda";
		int count = MIP_BranchManagement_Queries.getAssociatedUser(branchName);
		String[][] data = { {
				branchName,
				"Branch Name : The selected Branch cannot be de-activated. "
						+ count + " user(s) are associated with this branch." } };
		return data;
	}

	/**
	 * This method will read the data from the given XL
	 * 
	 * @return
	 */
	@DataProvider(name = "addBranchNegative")
	public static String[][] addBranchNegative() {
		Sheet s = MIP_XLOperation.loadXL("MIP_Add_Branch");
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
					data[rowcount][j] = r.getCell(j).getStringCellValue();
				}
			}
			rowcount++;
		}
		return data;
	}
}
