package com.milvik.mip.dbqueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.milvik.mip.pageutil.MIP_CustomerManagementPage;
import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_ChangeCoverLevel_Queries {
	static ResultSet result;
	static Logger logger;

	static {
		logger = MIP_Logging.logDetails("MIP_UpdateCoverLevel_Queries");
	}

	public static int getOfferDetails(String msisdn, String offer) {
		int offer_cover_id = -1;
		try {
			logger.info("Executing the getConfirmedStatus queries");
			result = MIP_DataBaseConnection.st
					.executeQuery("select offer_cover_id from offer_subscription where cust_id=(Select cust_id from customer_details where msisdn="
							+ msisdn
							+ ")"
							+ " and offer_id=(select offer_id from offer_details where offer_name like '% Bima Accident Protection'"
							+ ");");
			while (result.next()) {
				return result.getInt("offer_cover_id");
			}

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getConfirmedStatus queries", e);
		}
		return offer_cover_id;
	}

	public static boolean checkRecordInUpdateChangeCoverLevel(String msisdn,
			String offer) {
		// need to change query if product is added
		logger.info("Executing checkRecordExistAfterDereg query");
		String status = "";
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("select exists (Select * from updated_cover_level where cust_id=(select cust_id from customer_details where msisdn="
							+ msisdn + ")) as detail_exists;");
			result.next();
			if (result.getString("detail_exists") != null)
				status = result.getString("detail_exists");
			if (status.equals("0")) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			logger.error(
					"Error while executing the getDeRegisteredSMS queries", e);
		}
		return false;
	}

	public static Map<String, String> getupdateCoverLevelForConfirmedCustomer(
			String msisdn, String offer) {
		Map<String, String> offer_cover = new HashMap<String, String>();
		try {
			logger.info("Executing the getupdateCoverLevelForConfirmedCustomer queries");
			result = MIP_DataBaseConnection.st
					.executeQuery("select (select offer_cover from offer_cover_details where offer_cover_id= new_offer_cover_id)as new_offer,"
							+ " (select offer_cover from offer_cover_details where offer_cover_id= previous_offer_cover_id)as old_offer from updated_cover_level up where cust_id=(select cust_id from customer_details where msisdn="
							+ msisdn
							+ ")and  offer_id=(select offer_id from offer_details where offer_name like '%Bima Accident Protection') order by ucl_id limit 1;");
			while (result.next()) {
				if (result.getString("new_offer") == null) {
					offer_cover.put("new_offer", "");
				} else {
					offer_cover.put("new_offer", result.getString("new_offer"));
				}
				if (result.getString("old_offer") == null) {
					offer_cover.put("old_offer", "");
				} else {
					offer_cover.put("old_offer", result.getString("old_offer"));
				}
			}

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getupdateCoverLevelForConfirmedCustomer queries",
					e);
		}
		return offer_cover;
	}

	public static String getChangeCoverLevelSMS(String msisdn, String product) {
		String sms_template = "";
		String sms = "";
		logger.info("Executing getChangeCoverLevelSMS query");
		if (MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION
				.contains(product))
			sms_template = "switch_benefit_level_sms";
		else if (product
				.equalsIgnoreCase(MIP_CustomerManagementPage.BIMAHEALTH))
			sms_template = "switch_benefit_level_sms";
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("select sms_text from sms_in_queue where sms_msisdn="
							+ msisdn
							+ " and sms_template_name='"
							+ sms_template
							+ "' order by  sms_queue_id desc limit 1;");
			while (result.next()) {
				if (result.getString("sms_text") == null)
					sms = "";
				else
					sms = result.getString("sms_text");

			}
		} catch (SQLException e) {
			logger.error(
					"Error while executing the getChangeCoverLevelSMS queries",
					e);
		}
		return sms;
	}
}
