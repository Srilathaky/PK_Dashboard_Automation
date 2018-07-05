package com.milvik.mip.dbqueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_Loyalty_Queries {
	static ResultSet result;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_Loyalty_Queries");
	}

	public static List<String> getLoyaltyDetails(String msisdn) {
		List<String> details = new ArrayList<String>();
		logger.info("Executing getLoyaltyDetails query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT distinct(ly.product_id),ly.is_data,concat(cd.fname,' ',cd.sname) as name,cd.msisdn,ly.is_loyalty_provided,cs.conf_date,ly.loyalty_provided_date,pr.product_name "
							+ "from loyalty_customers ly inner join customer_details cd  on cd.cust_id=ly.cust_id "
							+ "inner join customer_subscription cs on cs.cust_id=cd.cust_id "
							+ "join product_details pr on pr.product_id=ly.product_id "
							+ "where cd.cust_id=(select cust_id  from customer_details where msisdn="
							+ msisdn + ") and cs.product_id<>1 ;");
			while (result.next()) {
				if (result.getString("is_data").equals("1")) {
					details.add("Data");
				} else {
					details.add("SMS");
				}
				details.add(result.getString("name"));
				details.add(result.getString("msisdn"));

				if (result.getString("is_loyalty_provided").equals("1")) {
					details.add("NO");
				} else {
					details.add("YES");
				}
				if (result.getString("conf_date") == null) {
					details.add("");
				} else {
					details.add(result.getString("conf_date"));
				}

				details.add("CONFIRMED");
				details.add(result.getString("loyalty_provided_date"));
				details.add(result.getString("product_name"));
			}

		} catch (SQLException e) {
			logger.error("Error while executing the getLoyaltyDetails queries",
					e);
		}
		return details;
	}

	public static Map<String, String> getLoyaltyDetailsAfterLoyaltyCredit(
			String msisdn, String productName) {
		logger.info("Executing getLoyaltyDetailsAfterLoyaltyCredit query");
		Map<String, String> details = new HashMap<String, String>();
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT is_data,response_code,is_processed,is_loyalty_provided,attempt_count,date(loyalty_provided_date) as date FROM loyalty_customers "
							+ "where msisdn="
							+ msisdn
							+ " and product_id=(select product_id from product_details where product_name='"
							+ productName + "');");
			while (result.next()) {
				if (result.getString("is_data").equals("1")) {
					details.put("Data", "Data");
				} else {
					details.put("Data", "SMS");
				}
				details.put("is_processed", result.getString("is_processed"));
				details.put("is_loyalty_provided",
						result.getString("is_loyalty_provided"));
				details.put("attempt_count", result.getString("attempt_count"));
				details.put("loyalty_provided_date", result.getString("date"));
				details.put("response_code", result.getString("response_code"));
			}

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getLoyaltyDetailsAfterLoyaltyCredit queries",
					e);
		}
		return details;
	}

	public static String getLoyaltySMS(String msisdn) {
		logger.info("Executing getLoyaltySMS query");
		String sms = "";
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT sms_text FROM sms_in_queue where sms_msisdn="
							+ msisdn + " order by  sms_queue_id desc limit 1;");
			result.next();

			return sms = result.getString("sms_text");

		} catch (SQLException e) {
			logger.error("Error while executing the getLoyaltySMS queries", e);
		}
		return sms;
	}

	public static String getcustFName(String msisdn) {
		logger.info("Executing getcustFName query");
		String fname = "";
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT cd.fname FROM customer_details cd where msisdn="
							+ msisdn + ";");
			result.next();

			return fname = result.getString("fname");

		} catch (SQLException e) {
			logger.error("Error while executing the getcustFName queries", e);
		}
		return fname;
	}
}
