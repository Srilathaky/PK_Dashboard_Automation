package com.milvik.mip.utility;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class MIP_ScreenShots {
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_ScreenShots");
	}

	public static String takeScreenShot(WebDriver driver, String filename) {
		File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		logger.info("Taking screen shot");
		try {
			FileUtils.copyFile(file, new File("Screen_Shots/" + filename
					+ ".png"));
		} catch (IOException e) {
			logger.error("Error while taking screen shot", e);
		}
		return new File("Screen_Shots/" + filename + ".png").getAbsolutePath();
	}
}
