package com.milvik.mip.dbqueries;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_LeaveManagemen_Queries {
	static ResultSet result;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_LeaveManagemen_Queries");
	}

	public static int getLeavedetails(String userName) {
		logger.info("Executing getLeavedetails query");
		int count = 0;
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT count(*) as count FROM user_leave_details where "
							+ "user_id=(select user_details.user_id from user_details where concat(user_details.fname,' ',user_details.sname)  ='"
							+ userName + "');");
			result.next();
			count = result.getInt("count");

		} catch (SQLException e) {
			logger.error("Error while executing the getLeavedetails queries", e);
		}
		return count;
	}

	public static int getTodayLeavedetails() {
		logger.info("Executing getTodayLeavedetails query");
		int count = 0;
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT count(*) as count FROM user_leave_details where Date(leave_date)=Date(now());");
			result.next();
			count = result.getInt("count");

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getTodayLeavedetails queries", e);
		}
		return count;
	}

	public static int getWeekLeavedetails() {
		logger.info("Executing getWeekLeavedetails query");
		int count = 0;
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("select count(*) as count from user_leave_details where Week(leave_date) = Week(CURDATE());");
			result.next();
			count = result.getInt("count");

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getWeekLeavedetails queries", e);
		}
		return count;
	}

	public static int getMonthLeavedetails() {
		logger.info("Executing getMonthLeavedetails query");
		int count = 0;
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("select count(*) as count from user_leave_details where MONTH(leave_date) = MONTH(CURDATE()) ;");
			result.next();
			count = result.getInt("count");

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getMonthLeavedetails queries", e);
		}
		return count;
	}

	public static int getCustomLeavedetails(String fromDate, String ToDate) {
		logger.info("Executing getCustomLeavedetails query");
		int count = 0;
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("select count(*) as count from user_leave_details where date(leave_date) between '"
							+ fromDate + "' and '" + ToDate + "';");
			result.next();
			count = result.getInt("count");

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getCustomLeavedetails queries",
					e);
		}
		return count;
	}
}
