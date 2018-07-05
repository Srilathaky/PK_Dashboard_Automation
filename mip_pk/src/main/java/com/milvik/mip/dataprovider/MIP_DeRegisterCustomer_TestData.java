package com.milvik.mip.dataprovider;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.DataProvider;

import com.milvik.mip.utility.MIP_XLOperation;

public class MIP_DeRegisterCustomer_TestData {

	/**
	 * This method will read the data from the given XL
	 * 
	 * @return
	 */
	public static final String DEACTIVATED_STATUS = "1";
	public static final String BIMA_JAZZ_CONFIRMATION_SUCC_MSG = "Your confirmation request for JAZZ is success.";
	public static final String BIMA_DEREGISTER_SUCC_MSG = "Your request to de-register bima customer - mobile number: XXXXX is successfully processed.";
	public static final String CONFIRMATION_STATUS_AFTERCONFIRMATION = "1";
	public static final String CONFIRMATION_STATUS_BEFORECONFIRMATION = "0";
	public static final String CONFIRMED_CUSTOMER = "Confirmed";
	public static final String BIMA_REACTIVATE_SUCC_MSG = "Your request to reactivation bima customer - mobile number: XXXXX is successfully processed.";
	public static final String BIMA_ACCIDENT_DEREG_SMS = "Muaziz Sarif, aap ki darkhuast per Bima hadsati tahafuz khatam kar di gayi hai. Dubara register karwanay k liye 9878 par SMS karein.";
	public static final String BIMA_ACCIDENT_CONFIRMATION_SMS ="Bima hadsati tahafuz mai Khush Amdeed. Ap ki RsXXXXX/month mai RsYYYYY wala cover laga dia gaya hai. Cancel ya claim kernay k liye 9878 par SMS karein";
	@DataProvider(name = "deRegisterCustomer")
	public String[][] deRegisterPostpaidCustomer() {
		return MIP_RegisterCustomer_TestData
				.storeCellData("MIP_Customer_DeRegistration_Scenario");
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
