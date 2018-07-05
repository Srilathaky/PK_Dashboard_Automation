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

public class MIP_OfferManagement_Queries {
	static ResultSet result;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_ClaimManagement_Queries");
	}

	public static int getOfferCount() {
		logger.info("Executing  getOfferCount query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT count(*) as count FROM offer_details where is_active=1;");
			result.next();
			return result.getInt("count");

		} catch (SQLException e) {
			logger.error("Error while executing the getOfferCount queries", e);
		}
		return 0;
	}

	public static Map<String, String> getOfferNameAndDescription() {
		Map<String, String> offerDetails = new HashMap<String, String>();
		logger.info("Executing  getOfferNameAndDescription query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT offer_name,offer_description FROM offer_details where is_active=1;");
			while (result.next()) {
				offerDetails.put(
						result.getString("offer_name").replaceAll("\\s", ""),
						result.getString("offer_description").replaceAll("\\s",
								""));
			}
		} catch (SQLException e) {
			logger.error(
					"Error while executing the getOfferNameAndDescription queries",
					e);
		}
		return offerDetails;
	}

	public static List<String> getOfferdCover(String offerName) {
		List<String> offerdCover = new ArrayList<String>();
		logger.info("Executing  getOfferdCover query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT offer_cover FROM offer_cover_details where offer_id=(select offer_id from offer_details where offer_name='"
							+ offerName + "');");
			while (result.next()) {
				if (result.getString("offer_cover") == null) {
					offerdCover.add("");
				} else if (result.getString("offer_cover").split("\\.")[1]
						.contains("123456789")) {
					offerdCover.add(result.getString("offer_cover").replaceAll(
							"\\.", ""));
				} else {
					offerdCover.add(result.getString("offer_cover")
							.split("\\.")[0]);
				}

			}
		} catch (SQLException e) {
			logger.error("Error while executing the getOfferdCover queries", e);
		}
		return offerdCover;
	}

	public static List<String> getLifeCover(String offerName) {
		List<String> offerdCover = new ArrayList<String>();
		logger.info("Executing  getLifeCover query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT life_cover FROM offer_cover_details where offer_id=(select offer_id from offer_details where offer_name='"
							+ offerName + "');");
			while (result.next()) {
				if (result.getString("life_cover") == null) {
					break;
				} else if (result.getString("life_cover").split("\\.")[1]
						.contains("123456789")) {
					offerdCover.add(result.getString("life_cover").replaceAll(
							"\\.", ""));
				} else {
					offerdCover
							.add(result.getString("life_cover").split("\\.")[0]);

				}
			}
		} catch (SQLException e) {
			logger.error("Error while executing the getLifeCover queries", e);
		}
		return offerdCover;
	}

	public static List<String> getOfferCoverCharges(String offerName) {
		List<String> offerdCover = new ArrayList<String>();
		logger.info("Executing  getOfferCoverCharges query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT offer_cover_charges FROM offer_cover_details where offer_id=(select offer_id from offer_details where offer_name='"
							+ offerName + "');");
			while (result.next()) {
				if (result.getString("offer_cover_charges") == null) {
					break;
				} else {
					offerdCover.add(result.getString("offer_cover_charges")
							.replaceAll("\\.", ""));

				}
			}
		} catch (SQLException e) {
			logger.error(
					"Error while executing the getOfferCoverCharges queries", e);
		}
		return offerdCover;
	}
}