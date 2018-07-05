package com.milvik.mip.pageutil;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.milvik.mip.constants.MIP_Constants;
import com.milvik.mip.utility.MIP_Logging;

abstract public class MIP_BasePage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_BasePage");
	}

	public MIP_BasePage(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * This method will wait for the element to be present on the DOM by
	 * specified time.
	 * 
	 * @param by
	 * @return
	 */
	public WebElement waitForElementToPresent(By by) {
		WebDriverWait w = new WebDriverWait(driver,
				MIP_Constants.TIMEOUTINSECONDS);
		return w.until(ExpectedConditions.presenceOfElementLocated(by));
	}

	/**
	 * This method will wait for the element to be visible on the DOM by
	 * specified time.
	 * 
	 * @param by
	 * @return
	 */
	public WebElement waitForElementToVisible(By by) {
		WebDriverWait w = new WebDriverWait(driver,
				MIP_Constants.TIMEOUTINSECONDS);
		return w.until(ExpectedConditions.visibilityOfElementLocated(by));
	}

	public Boolean waitForElementToInvisible(By by) {
		WebDriverWait w = new WebDriverWait(driver,
				MIP_Constants.TIMEOUTINSECONDS);
		return w.until(ExpectedConditions.invisibilityOfElementLocated(by));
	}

	/**
	 * This method will wait for the element to be clickable on the DOM by
	 * specified time.
	 * 
	 * @param by
	 * @return
	 */
	public WebElement waitForElementTobeClickable(By by) {
		WebDriverWait w = new WebDriverWait(driver,
				MIP_Constants.TIMEOUTINSECONDS);
		return w.until(ExpectedConditions.elementToBeClickable(by));
	}

	/**
	 * This method will wait for the text to be present in the element on the
	 * DOM by specified time.
	 * 
	 * @param by
	 *            .text
	 * @return
	 */
	public void waitForTextToPresent(By by, String text) {
		WebDriverWait w = new WebDriverWait(driver,
				MIP_Constants.TIMEOUTINSECONDS);
		w.until(ExpectedConditions.textToBePresentInElementLocated(by, text));
	}

	/**
	 * This method will click on the given element
	 * 
	 * @param by
	 * @return
	 */
	public void clickOnElement(By by) {
		WebDriverWait w = new WebDriverWait(driver,
				MIP_Constants.TIMEOUTINSECONDS);
		w.until(ExpectedConditions.elementToBeClickable(by)).click();
	}

	/**
	 * This method will return the text present in the alert.
	 * 
	 * @return
	 */
	public String getAlertText() {
		try {
			logger.info("Getting the alert text");
			WebDriverWait w = new WebDriverWait(driver,
					MIP_Constants.TIMEOUTINSECONDS);
			Alert alert = w.until(ExpectedConditions.alertIsPresent());
			String str = alert.getText();
			alert.accept();
			return str;
		} catch (Exception e) {
			logger.error("Error while handling the alerts", e);
			return "";
		}

	}

	/**
	 * This method will check wheather alert is present or not
	 * 
	 * @return
	 */
	public boolean isAlertPresent() {
		try {
			logger.info("Switching the alert");
			driver.switchTo().alert();
			return true;
		} catch (Exception e) {
			logger.error("Error while handling the alerts", e);
			return false;
		}
	}

	/**
	 * This method will send the text to the web element specified
	 * 
	 * @param ele
	 * @param text
	 */
	public void enterText(WebElement ele, String text) {
		ele.sendKeys(text);
	}

	/**
	 * This method will click on the search button
	 * 
	 */
	public void clickOnSearchButton() {
		try {
			logger.info("Clicking on search button");
			this.waitForElementToVisible(By.id("search-icon")).click();
			logger.info("Clicked on search button");
		} catch (Exception e) {
			logger.error("Error while clicking on search button", e);
		}
	}

	/**
	 * This method will select the dropdown value by text
	 * 
	 * @param ele
	 * @param text
	 */
	public void selectDropDownbyText(WebElement ele, String text) {
		Select sel = new Select(ele);
		sel.selectByVisibleText(text);
	}

	public void selectDropDownbyValue(WebElement ele, String value) {
		Select sel = new Select(ele);
		sel.selectByValue(value);
	}

	/**
	 * This method will return all the options of the dropdown
	 * 
	 * @param ele
	 * @return
	 */
	public List<WebElement> selectDropDownOptions(WebElement ele) {
		Select sel = new Select(ele);
		return sel.getOptions();
	}

	/**
	 * This method will return the selected value in the dropdown
	 * 
	 * @param ele
	 * @return
	 */
	public String getSelectedOption(WebElement ele) {
		Select s = new Select(ele);
		return s.getFirstSelectedOption().getText();

	}

	/**
	 * This method will click on Logout option present in the application
	 */
	public void clickonLogout() {
		this.waitForElementToVisible(By.linkText("Logout")).click();

	}

	/**
	 * This method will confirm the pop-up options depending on the option given
	 * 
	 * @param option
	 */
	public void confirmPopUp(String option) {
		this.waitForElementToVisible(By.id("popup_message"));
		if (option.equalsIgnoreCase("yes")) {
			driver.findElement(By.id("popup_ok")).click();
		} else {
			driver.findElement(By.id("popup_cancel")).click();
		}
	}

}
