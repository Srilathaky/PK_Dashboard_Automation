package com.milvik.mip.dbqueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_BranchManagement_Queries {
	static ResultSet result;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_ReadPropertyFile");
	}
	static Map<String, String> branchDetails = new HashMap<String, String>();

	public static Map<String, String> getBranchDetails(String branchname) {
		logger.info("Executing getBranchDetails query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT branch_code,is_active,branch_name,branch_street,branch_region,branch_city  FROM branch_details where branch_name='"
							+ branchname + "';");
			result.next();

			branchDetails.put("name", result.getString("branch_name"));
			branchDetails.put("street", result.getString("branch_street"));
			branchDetails.put("region", result.getString("branch_region"));
			branchDetails.put("city", result.getString("branch_city"));
			branchDetails.put("branchcode", result.getString("branch_code"));
			branchDetails.put("is_active", result.getString("is_active"));

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getCustomerDetails queries", e);
		}
		return branchDetails;
	}

	public static int getNumberOfBranch() {
		int count = 0;
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT count(*) as count FROM branch_details where is_active=1;");
			result.next();
			return count = result.getInt("count");
		} catch (SQLException e) {
			logger.error("Error while executing the getNumberOfBranch queries",
					e);
		}
		return count;
	}

	public static int getAssociatedUser(String branchname) {
		int count = 0;
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT count(branch_id) as count FROM user_details where branch_id=(SELECT branch_id  FROM branch_details where branch_name='"
							+ branchname + "');");
			result.next();
			return count = result.getInt("count");
		} catch (SQLException e) {
			logger.error("Error while executing the getAssociatedUser queries",
					e);
		}
		return count;
	}

}
