package com.milvik.mip.dataprovider;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.DataProvider;

import com.milvik.mip.utility.MIP_XLOperation;

public class MIP_ChangeCoverLevel_TestData {

	public static final String ACCIDENT_CHANGE_COVER_LEVEL_SMS = "Muaziz Sarif, hum nay aap ki darkhuwast par Bima hadsati tahafuz cover XXXXX say YYYYY kar dia hai. Agar yeh drust he to humay  9878 per SMS karein.";
	public static final String HEALTH_CHANGE_COVER_LEVEL_SMS = "Dear bimahealth member,we have modified your cover level from $XXXXX to $YYYYY accordingly to your request. Please call 2462 if this is incorrect.";
	public static final String LEVEL1_COVER_PER_NIGHT = "24";
	public static final String LEVEL2_COVER_PER_NIGHT = "48";
	public static final String LEVEL3_COVER_PER_NIGHT = "96";
	public static final String HP_COVER_LEVEL1 = "Life:$ 450 & HP:$ 720";
	public static final String HP_COVER_LEVEL2 = "Life:$ 900 & HP:$ 1440";
	public static final String HP_COVER_LEVEL3 = "Life:$ 1800 & HP:$ 2880";

	/**
	 * Data in the order nic,
	 */
	@DataProvider(name = "validate_changeCoverLevel")
	public String[][] validate_changeCoverLevel() {
		String[][] data = { { "7188215" } };
		return data;
	}

	@DataProvider(name = "changeCoverLevel")
	public String[][] updatehpCoverLevel() {
		Sheet s = MIP_XLOperation.loadXL("MIP_ChangeCoverLevel");
		return storeCellData(s);
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
