package com.milvik.mip.dbqueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.milvik.mip.pageutil.MIP_CustomerManagementPage;
import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_Homepage_CustomerStatistics_Queries {
	static ResultSet result;
	static Logger logger;
	public static final String BUSSINESS_ADMINISTRATOR_ROLE = "Business Administrator";
	public static final String CALLCENTER_AGENT = "Sales agent";
	static {
		logger = MIP_Logging
				.logDetails("MIP_Homepage_CustomerStatistics_Queries");
	}

	public static String userDetails(String username) {
		String details = null;
		logger.info("Executing userDetails query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("select concat(fname,' ',sname) as name from  user_details where user_uid='"
							+ username + "';");
			result.next();
			details = result.getString("name");
		} catch (SQLException e) {
			logger.error("Error while executing the userDetails queries", e);
		}
		return details;
	}

	public static Map<String, String> getHomePageStatistics(String username) {
		String role_name = "";
		Map<String, String> details = new HashMap<String, String>();
		logger.info("Executing getHomePageStatistics query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("select role_name from role_master where role_id=(select role_id from user_details where user_uid='"
							+ username + "');");
			result.next();
			role_name = result.getString("role_name");
			details.put("role_name", role_name);
			if (role_name.equalsIgnoreCase(BUSSINESS_ADMINISTRATOR_ROLE)) {
				result = MIP_DataBaseConnection.st
						.executeQuery("select count(*) as count from offer_subscription os join user_details ud on os.created_by=ud.user_id"
								+ " join role_master rm on rm.role_id=ud.role_id "
								+ "where rm.role_name ='"
								+ CALLCENTER_AGENT
								+ "' and os.created_date between  concat(Year(CURRENT_DATE),'-',Month(CURRENT_DATE),'-01') and "
								+ " concat(Year(CURRENT_DATE),'-',Month(CURRENT_DATE)+1,'-01');");
				result.next();
				details.put("reg_by_call_center", result.getString("count"));

				result = MIP_DataBaseConnection.st
						.executeQuery("select count(*) as count from offer_subscription os join user_details ud on os.created_by=ud.user_id"
								+ " join role_master rm on rm.role_id=ud.role_id "
								+ "where rm.role_name ='"
								+ CALLCENTER_AGENT
								+ "' and os.created_date between  concat(Year(CURRENT_DATE),'-',Month(CURRENT_DATE),'-01') and "
								+ " concat(Year(CURRENT_DATE),'-',Month(CURRENT_DATE)+1,'-01') and os.is_confirmed=1;");
				result.next();
				details.put("confirmed_by_call_center",
						result.getString("count"));
			} else {
				result = MIP_DataBaseConnection.st
						.executeQuery("select count(*) as count from offer_subscription os join user_details ud on ud.user_id=os.created_by"
								+ " join offer_details od on od.offer_id=os.offer_id"
								+ " where ud.user_uid='"
								+ username
								+ "' and os.created_date between concat(year(CURRENT_DATE),'-',month(CURRENT_DATE),'-01') "
								+ " and concat(year(CURRENT_DATE),'-',month(CURRENT_DATE)+1,'-01') and od.offer_name='"
								+ MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION + "';");
				result.next();
				details.put("XL_Reg", result.getString("count"));
				result = MIP_DataBaseConnection.st
						.executeQuery("select count(*) as count from offer_subscription os join user_details ud on ud.user_id=os.created_by"
								+ " join offer_details od on od.offer_id=os.offer_id"
								+ " where ud.user_uid='"
								+ username
								+ "' and os.created_date between concat(year(CURRENT_DATE),'-',month(CURRENT_DATE),'-01') "
								+ " and concat(year(CURRENT_DATE),'-',month(CURRENT_DATE)+1,'-01') and od.offer_name='"
								+ MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION
								+ "' and os.is_confirmed=1;");
				result.next();
				details.put("XL_Confirmed", result.getString("count"));
				result = MIP_DataBaseConnection.st
						.executeQuery("select count(*) as count from offer_subscription os join user_details ud on ud.user_id=os.created_by"
								+ " join offer_details od on od.offer_id=os.offer_id"
								+ " where ud.user_uid='"
								+ username
								+ "' and os.created_date between concat(year(CURRENT_DATE),'-',month(CURRENT_DATE),'-01') "
								+ " and concat(year(CURRENT_DATE),'-',month(CURRENT_DATE)+1,'-01') and od.offer_name='"
								+ MIP_CustomerManagementPage.BIMAHEALTH + "';");
				result.next();
				details.put("HP_Reg", result.getString("count"));
				result = MIP_DataBaseConnection.st
						.executeQuery("select count(*) as count from offer_subscription os join user_details ud on ud.user_id=os.created_by"
								+ " join offer_details od on od.offer_id=os.offer_id"
								+ " where ud.user_uid='DA8' and os.created_date between concat(year(CURRENT_DATE),'-',month(CURRENT_DATE),'-01') "
								+ " and concat(year(CURRENT_DATE),'-',month(CURRENT_DATE)+1,'-01') and od.offer_name='"
								+ MIP_CustomerManagementPage.BIMAHEALTH
								+ "' and os.is_confirmed=1;");
				result.next();
				details.put("HP_Confirmed", result.getString("count"));
			}

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getHomePageStatistics queries",
					e);
		}
		return details;
	}
}
