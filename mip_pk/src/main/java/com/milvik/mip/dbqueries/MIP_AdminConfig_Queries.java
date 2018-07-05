package com.milvik.mip.dbqueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_AdminConfig_Queries {
	static ResultSet result;
	static Logger logger;
	static Map<String, String> adminConfigdetails = new HashMap<String, String>();
	static {
		logger = MIP_Logging.logDetails("MIP_AdminConfig_Queries");
	}

	/**
	 * This methosd will return the max_login_attempts from admin_config table
	 * 
	 * @return
	 */
	public static int getMaxLoginAttempt() {
		int maxCount = 0;
		try {
			logger.info("Executing getMaxLoginAttempt queries");
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT data_val FROM admin_config where param='max_login_attempts';");
			result.next();
			maxCount = result.getInt("data_val");

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getMaxLoginAttempt queries", e);
		}
		return maxCount;
	}

	/**
	 * This methosd will account locked status from user_hash table
	 * 
	 * @return
	 */
	public static int getUserStatus(String userparam) {
		int accstatus = 0;
		try {
			logger.info("Executing getUserStatus queries");
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT is_account_locked FROM user_hash uh "
							+ "where uh.user_id=(select ud.user_id from user_details ud where ud.user_uid='"
							+ userparam + "');");
			result.next();
			accstatus = result.getInt("is_account_locked");

		} catch (SQLException e) {
			logger.error("Error while executing the getUserStatus queries", e);
		}
		return accstatus;
	}

	/**
	 * This method will return admin_config table data
	 * 
	 * @return
	 */
	public static Map<String, String> getAdminConfigInfo() {
		try {
			logger.info("Executing the getAdminConfigInfo queries");
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT data_val FROM admin_config where param='default_password';");
			result.next();
			adminConfigdetails.put("default_password",
					result.getString("data_val"));
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT data_val FROM admin_config where param='user_login_prefix';");
			result.next();
			adminConfigdetails.put("user_login_prefix",
					result.getString("data_val"));
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT data_val FROM admin_config where param='password_history_limit';");
			result.next();
			adminConfigdetails.put("password_history_limit",
					result.getString("data_val"));
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT data_val FROM admin_config where param='max_login_attempts';");
			result.next();
			adminConfigdetails.put("max_login_attempts",
					result.getString("data_val"));
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT data_val FROM admin_config where param='max_idle_count';");
			result.next();
			adminConfigdetails.put("max_idle_count",
					result.getString("data_val"));
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT data_val FROM admin_config where param='announcement_message';");
			result.next();
			adminConfigdetails.put("announcement_message",
					result.getString("data_val"));
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT data_val FROM admin_config where param='msisdn_codes';");
			result.next();
			adminConfigdetails.put("msisdn_code", result.getString("data_val"));

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getAdminConfigInfo queries", e);
		}
		return adminConfigdetails;
	}

	/**
	 * This method will return the Login prefix from admin_config table
	 * 
	 * @return
	 */
	public static String getLoginPrefix() {
		logger.info("Executing the getAdminConfigInfo queries");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT data_val FROM admin_config where param='user_login_prefix';");
			result.next();
			return result.getString("data_val");
		} catch (SQLException e) {
			logger.error("Error while executing the sql queries", e);
		}
		return "";
	}

	/**
	 * This method will return the default password from admin_config table
	 * 
	 * @return
	 */
	public static String getDefaultPassword() {
		logger.info("Executing the getDefaultPassword queries");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT data_val FROM admin_config where param='default_password';");
			result.next();
			return result.getString("data_val");
		} catch (SQLException e) {
			logger.error(
					"Error while executing the getDefaultPassword queries", e);
		}
		return "";
	}
}
