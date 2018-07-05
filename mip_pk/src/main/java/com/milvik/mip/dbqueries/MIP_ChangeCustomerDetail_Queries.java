package com.milvik.mip.dbqueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_ChangeCustomerDetail_Queries {
	static ResultSet result;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_ChangeCustomerDetail_Queries");
	}

	public static Map<String, String> getchangeCustomerDetails(String msisdn) {
		Map<String, String> details = new HashMap<String, String>();
		logger.info("Executing getchangeCustomerDetails query");
		try {

			result = MIP_DataBaseConnection.st
					.executeQuery("select  ca.new_fname,ca.old_fname,ca.new_sname,ca.old_sname,ca.new_age,ca.old_age,ca.new_gender,ca.old_gender,"
							+ " ca.new_ird_fname,ca.old_ird_fname,ca.new_ird_sname,"
							+ " ca.old_ird_sname,ca.new_ird_age,ca.old_ird_age,ca.new_cust_relationship,ca.old_cust_relationship,"
							+ " ca.new_lg_name,ca.old_lg_name,ca.new_lg_msisdn,"
							+ " ca.old_lg_msisdn,ud.user_uid from customer_audit ca join user_details ud on ud.user_id=ca.modified_by where ca.msisdn= "
							+ msisdn + " order by  ca_id desc limit 1;");
			while (result.next()) {
				if (result.getString("new_fname") == null) {
					details.put("new_fname", "");
				} else {
					details.put("new_fname", result.getString("new_fname"));
				}
				if (result.getString("old_fname") == null) {
					details.put("old_fname", "");
				} else {
					details.put("old_fname", result.getString("old_fname"));
				}
				if (result.getString("new_sname") == null) {
					details.put("new_sname", "");
				} else {
					details.put("new_sname", result.getString("new_sname"));
				}
				if (result.getString("old_sname") == null) {
					details.put("old_sname", "");
				} else {
					details.put("old_sname", result.getString("old_sname"));
				}
				if (result.getString("new_age") == null) {
					details.put("new_age", "");
				} else {
					details.put("new_age", result.getString("new_age"));
				}
				if (result.getString("old_age") == null) {
					details.put("old_age", "");
				} else {
					details.put("old_age", result.getString("old_age"));
				}
				if (result.getString("new_gender") == null) {
					details.put("new_gender", "");
				} else {
					details.put("new_gender", result.getString("new_gender"));
				}
				if (result.getString("old_gender") == null) {
					details.put("old_gender", "");
				} else {
					details.put("old_gender", result.getString("old_gender"));
				}
				if (result.getString("new_ird_fname") == null) {
					details.put("new_ben_fname", "");
				} else {
					details.put("new_ben_fname",
							result.getString("new_ird_fname"));
				}
				if (result.getString("old_ird_fname") == null) {
					details.put("old_ben_fname", "");
				} else {
					details.put("old_ben_fname",
							result.getString("old_ird_fname"));
				}
				if (result.getString("new_ird_sname") == null) {
					details.put("new_ben_sname", "");
				} else {
					details.put("new_ben_sname",
							result.getString("new_ird_sname"));
				}
				if (result.getString("old_ird_sname") == null) {
					details.put("old_ben_sname", "");
				} else {
					details.put("old_ben_sname",
							result.getString("old_ird_sname"));
				}
				if (result.getString("new_ird_age") == null) {
					details.put("new_ben_age", "");
				} else {
					details.put("new_ben_age", result.getString("new_ird_age"));
				}
				if (result.getString("old_ird_age") == null) {
					details.put("old_ben_age", "");
				} else {
					details.put("old_ben_age", result.getString("old_ird_age"));
				}
				if (result.getString("new_cust_relationship") == null) {
					details.put("new_cust_relationship", "");
				} else {
					details.put("new_cust_relationship",
							result.getString("new_cust_relationship"));
				}
				if (result.getString("old_cust_relationship") == null) {
					details.put("old_cust_relationship", "");
				} else {
					details.put("old_cust_relationship",
							result.getString("old_cust_relationship"));
				}
				if (result.getString("new_lg_name") == null) {
					details.put("new_guardian_name", "");
				} else {
					details.put("new_guardian_name",
							result.getString("new_lg_name"));
				}
				if (result.getString("old_lg_name") == null) {
					details.put("old_guardian_name", "");
				} else {
					details.put("old_guardian_name",
							result.getString("old_lg_name"));
				}
				if (result.getString("old_lg_msisdn") == null) {
					details.put("old_guardian_msisdn", "");
				} else {
					details.put("old_guardian_msisdn",
							result.getString("old_lg_msisdn"));
				}
				if (result.getString("new_lg_msisdn") == null) {
					details.put("new_guardian_msisdn", "");
				} else {
					details.put("new_guardian_msisdn",
							result.getString("new_lg_msisdn"));
				}
				if (result.getString("user_uid") == null) {
					details.put("modified_by", "");
				} else {
					details.put("modified_by", result.getString("user_uid"));
				}
			}

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getchangeCustomerDetails queries",
					e);
		}
		return details;
	}

}
