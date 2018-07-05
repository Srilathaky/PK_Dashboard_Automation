package com.milvik.mip.dbqueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_Logging;
import com.milvik.mip.utility.MIP_ReadPropertyFile;

public class MIP_UserManagement_Queries {
	static ResultSet result;
	static Logger logger;
	static Map<String, String> userdetails = new HashMap<String, String>();
	static {
		logger = MIP_Logging.logDetails("MIP_AddUser_Queries");
	}

	public static Map<String, String> getUserInfo(String mobilenum) {
		try {
			logger.info("Executing the getUserInfo queries");
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT user_uid,fname,sname,dob,gender,email_id,role_name,branch_name,is_supervisor,is_alert_enabled FROM user_details ud join role_master rm on ud.role_id=rm.role_id join branch_details bd on ud.branch_id=bd.branch_id where ud.msisdn='"
							+ mobilenum + "';");
			while (result.next()) {
				userdetails.put("user_uid", result.getString("user_uid"));
				userdetails.put("fname", result.getString("fname"));
				userdetails.put("sname", result.getString("sname"));
				userdetails.put("dob", result.getString("dob"));
				userdetails.put("gender", result.getString("gender"));
				userdetails.put("email_id", result.getString("email_id"));
				userdetails.put("role_name", result.getString("role_name"));
				userdetails.put("branch_name", result.getString("branch_name"));
				if (result.getString("is_supervisor").equals("1"))
					userdetails.put("is_supervisor", "yes");
				else {
					userdetails.put("is_supervisor", "no");

				}
				if (result.getString("is_alert_enabled").equals("1"))
					userdetails.put("is_alert_enabled", "yes");
				else {
					userdetails.put("is_alert_enabled", "no");

				}
			}
			if (userdetails.get("is_supervisor").equalsIgnoreCase("no")) {
				result = MIP_DataBaseConnection.st
						.executeQuery("select concat(fname,' ',sname) as name from user_details where user_id=(select supervisor_id from user_details ud where is_supervisor=0 and ud.msisdn="
								+ mobilenum + ");");

				result.next();
				userdetails.put("supervisor_name", result.getString("name"));
			}
		} catch (SQLException e) {
			logger.error("Error while executing the getUserInfo queries", e);
		}
		return userdetails;
	}

	public static int getActiveUser() {
		int count = 0;
		try {
			logger.info("Executing the getUserInfo queries");
			String prefix = MIP_AdminConfig_Queries.getLoginPrefix();
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT count(*) as count  FROM user_details where is_active=1 and user_uid like '"
							+ prefix + "%';");
			result.next();
			count = result.getInt("count");
		} catch (Exception e) {
			logger.error("Error while executing the getActiveUser queries", e);
		}
		return count;
	}

	public static void main(String[] args) {
		MIP_ReadPropertyFile.loadProperty("config");
		MIP_DataBaseConnection.connectToDatabase("windows");
		getUserInfo("7218882");
	}
}