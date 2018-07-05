package com.milvik.mip.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class MIP_ReadPropertyFile {
	static Properties p;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_ReadPropertyFile");
	}

	public static void loadProperty(String filename) {
		try {
			logger.info("Reading the property file");

			FileInputStream file = new FileInputStream("PropertyFiles/"
					+ filename + ".properties");
			p = new Properties();
			p.load(file);
		} catch (IOException e) {
			logger.error("Error while reading the property file");

		}
	}

	/**
	 * This method reads the property files and returns the value for
	 * corresponding key.
	 *
	 */
	public static String getPropertyValue(String key) {
		return p.getProperty(key);
	}
}
