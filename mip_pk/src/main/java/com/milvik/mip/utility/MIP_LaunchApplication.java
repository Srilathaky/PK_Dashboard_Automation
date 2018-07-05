package com.milvik.mip.utility;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class MIP_LaunchApplication {
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_LaunchApplication");
	}

	/**
	 * This method launches the application
	 */
	public static void openApplication(WebDriver driver, String platform) {
		logger.info("Launching the application");
		if (platform.equalsIgnoreCase("windows"))
			driver.get(MIP_ReadPropertyFile.getPropertyValue("appurl"));
		else if (platform.equalsIgnoreCase("AWS"))
			driver.get(MIP_ReadPropertyFile.getPropertyValue("awsappurl"));
		logger.info("Application Launched successfuly");

	}
}
