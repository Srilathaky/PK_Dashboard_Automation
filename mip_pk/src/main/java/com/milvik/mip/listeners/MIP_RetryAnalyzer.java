package com.milvik.mip.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.milvik.mip.utility.MIP_Logging;

public class MIP_RetryAnalyzer implements IRetryAnalyzer {
	int count = 0;
	int maxcount = 1;

	public boolean retry(ITestResult result) {
		MIP_Logging.logDetails("MIP_RetryAnalyzer").info(
				"Retrying the failed testcases");
		if (count < maxcount) {
			count++;
			return true;
		}
		return false;
	}

}
