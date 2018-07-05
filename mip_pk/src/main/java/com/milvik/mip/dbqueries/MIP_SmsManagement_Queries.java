package com.milvik.mip.dbqueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_SmsManagement_Queries {

	static ResultSet result;
	static Logger logger;
	static Map<String, String> editSMS = new HashMap<String, String>();
	static {
		logger = MIP_Logging.logDetails("MIP_SmsManagement_Queries");
	}

	public static Map<String, String> geteditSmsInfo(String templateName) {
		try {
			logger.info("Executing geteditSmsInfo queries");
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT sms_template,sms_validity FROM sms_template_master where sms_template_name='"
							+ templateName + "';");
			result.next();
			editSMS.put("content", result.getString("sms_template"));
			editSMS.put("validity", result.getString("sms_validity"));

		} catch (SQLException e) {
			logger.error("Error while executing the geteditSmsInfo queries", e);
		}
		return editSMS;
	}

	public static int getplaceHolderCount(String templateName) {
		int count = 0;
		try {
			logger.info("Executing getplaceHolderCount queries");
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT sms_place_holders_count FROM sms_template_master where sms_template_name='"
							+ templateName + "';");
			result.next();
			count = result.getInt("sms_place_holders_count");
		} catch (SQLException e) {
			logger.error(
					"Error while executing the getplaceHolderCount queries", e);
		}
		return count;
	}

}
