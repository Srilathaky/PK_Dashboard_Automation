package com.milvik.mip.utility;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * This Class Contains the methods for browser operation.
 *
 */
public class MIP_BrowserFactory {
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_BrowserFactory");
	}

	public static WebDriver openBrowser(WebDriver driver, String browser,
			String platform) {
		if (platform.equalsIgnoreCase("windows")) {
			if (browser.equalsIgnoreCase("chrome")) {
				System.setProperty("webdriver.chrome.driver",
						".\\Browser_Exe\\chromedriver.exe");
				logger.info("Opening the browser");
				driver = new ChromeDriver();
			} else {
				System.setProperty("webdriver.gecko.driver",
						".\\Browser_Exe\\geckodriver.exe");
				logger.info("Opening the browser");
				driver = new FirefoxDriver();
			}
		} else if (platform.equalsIgnoreCase("AWS")) {
			if (browser.equalsIgnoreCase("firefox")) {
				FirefoxBinary firefoxBinary = new FirefoxBinary();
				firefoxBinary.addCommandLineOptions("--headless");
				System.setProperty("webdriver.gecko.driver",
						"Browser_Exe/geckodriver");
				FirefoxOptions firefoxOptions = new FirefoxOptions();
				firefoxOptions.setBinary(firefoxBinary);
				driver = new FirefoxDriver(firefoxOptions);
			}
		}
		logger.info(browser + " opened ");
		driver.manage().window().maximize();
		// driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		return driver;

	}

	public static void closeDriver(WebDriver driver) {
		logger.info("Closing the browser");
		driver.close();
		logger.info("browser closed");
	}
}
