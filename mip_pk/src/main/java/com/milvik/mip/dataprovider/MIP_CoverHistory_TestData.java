package com.milvik.mip.dataprovider;

import org.testng.annotations.DataProvider;

public class MIP_CoverHistory_TestData {
	/**
	 * Data In the order:msisdn,channel
	 */
	@DataProvider(name = "coverHistoryPositive")
	public String[][] coverHistoryData() {
		String[][] data = { { "766554434", "GSM" }, { "766554434", "GSM" } };
		return data;
	}
}
