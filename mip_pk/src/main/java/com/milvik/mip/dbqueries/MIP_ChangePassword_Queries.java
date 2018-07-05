package com.milvik.mip.dbqueries;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_ChangePassword_Queries {
	static ResultSet result;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_ChangePassword_Queries");
	}
	static String passwordDetails;

	public static String getPasswordDetails(String username) {
		logger.info("Executing  getPasswordDetails query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT user_hash FROM user_hash where user_id=(SELECT user_id FROM user_details where user_uid='"
							+ username + "')");
			result.next();
			passwordDetails = result.getString("user_hash");

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getPasswordDetails queries", e);
		}
		return passwordDetails;
	}

}
