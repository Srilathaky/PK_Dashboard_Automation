package com.milvik.mip.utility;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class MIP_XLOperation {
	public static FileInputStream file;
	public static Workbook w;
	public static Sheet s;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_XLOperation");
	}

	public static Sheet loadXL(String filename) {
		try {
			logger.info("Reading the XL data");
			file = new FileInputStream("src/test/resources/TestData/"
					+ filename + ".xls");

			w = WorkbookFactory.create(file);
		} catch (EncryptedDocumentException e) {
			logger.error("Error while Reading the XL data", e);
		} catch (InvalidFormatException e) {
			logger.error("Error while Reading the XL data", e);
		} catch (IOException e) {
			logger.error("Error while Reading the XL data", e);
		}
		s = w.getSheetAt(0);
		return s;
	}

	public static int getNumRows() {
		return s.getPhysicalNumberOfRows();

	}

	public static int getNumCell() {
		return s.getRow(0).getPhysicalNumberOfCells();
	}

}
