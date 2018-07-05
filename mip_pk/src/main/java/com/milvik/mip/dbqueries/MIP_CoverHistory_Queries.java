package com.milvik.mip.dbqueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_CoverHistory_Queries {
	static ResultSet result;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_CoverHistory_Queries");
	}

	public static List<String> getOfferNameInCoverHistory(String msisdn) {
		List<String> offer_details = new ArrayList<String>();
		logger.info("Executing getOfferNameInCoverHistory query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("select distinct(offer_name) from offer_details od join cover_history ch on od.offer_id=ch.offer_id where msisdn="
							+ msisdn + ";");

			while (result.next()) {
				offer_details.add(result.getString("offer_name"));

			}

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getOfferNameInCoverHistory queries",
					e);
		}
		return offer_details;
	}

	public static int getCoverHistoryRowCount(String msisdn) {
		int count = 0;
		logger.info("Executing getCoverHistoryRowCount query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("select count(*) as count from cover_history  where msisdn="
							+ msisdn + ";");

			while (result.next()) {
				count = (result.getInt("count"));

			}

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getCoverHistoryRowCount queries",
					e);
		}
		return count;
	}

	public static List<String> getCoverHistoryDetails(String msisdn,
			String offer) {
		List<String> offer_details = new ArrayList<String>();
		logger.info("Executing getCoverHistoryDetails query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("select name,nic,concat(month,' ',year) as month,offer_charges,cover_earned,prior_month_charges,rolled_out_amount from cover_history  where msisdn="
							+ msisdn
							+ " and offer_id=(select offer_id from offer_details where offer_name='"
							+ offer + "');");

			while (result.next()) {
				offer_details.add(result.getString("name"));
				offer_details.add(result.getString("nic"));
				offer_details.add(result.getString("month"));
				offer_details.add(result.getString("offer_charges"));
				offer_details.add(result.getString("cover_earned"));
				offer_details.add(result.getString("prior_month_charges"));
				offer_details.add(result.getString("rolled_out_amount"));

			}

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getCoverHistoryDetails queries",
					e);
		}
		return offer_details;
	}

}
