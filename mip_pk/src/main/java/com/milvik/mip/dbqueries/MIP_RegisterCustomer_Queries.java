package com.milvik.mip.dbqueries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.milvik.mip.pageutil.MIP_CustomerManagementPage;
import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_Logging;
import com.milvik.mip.utility.MIP_ReadPropertyFile;

public class MIP_RegisterCustomer_Queries {
	private static ResultSet result;
	private static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_RegisterCustomer_Queries");
	}
	private static Map<String, String> custDetails = new HashMap<String, String>();

	public static Map<String, String> getCustomerDetails(String msisdn) {
		logger.info("Executing getCustomerDetails query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("select cd.fname,cd.sname,cd.age,DATE_FORMAT(cd.dob,'%d/%m/%Y')as dob,cd.gender,cd.cnic,cd.w_cnic,(select user_uid from user_details where user_id=cd.modified_by) as user,cd.modified_date,cd.is_prepaid,cd.is_active,cd.current_bill_group,cd.is_warid,"
							+ " ird.fname as ben_fname,ird.sname as ben_sname,ird.age as ben_age,ird.gender as ben_gender,ird.msisdn as ben_msisdn,ird.cust_relationship "
							+ " from customer_details cd join insured_relative_details ird on ird.cust_id=cd.cust_id where cd.msisdn="
							+ msisdn + ";");
			result.next();
			custDetails.put("cust_fname", result.getString("fname"));
			custDetails.put("cust_sname", result.getString("sname"));
			custDetails.put("cust_age", result.getString("age"));
			if (result.getString("gender") == null) {
				custDetails.put("cust_gender", "");
			} else
				custDetails.put("cust_gender", result.getString("gender"));
			if (result.getString("dob") == null) {
				custDetails.put("cust_dob", "");
			} else
				custDetails.put("cust_dob", result.getString("dob"));
			if (result.getString("cnic") == null) {
				custDetails.put("cust_cnic", "");
			} else
				custDetails.put("cust_cnic", result.getString("cnic"));
			if (result.getString("w_cnic") == null) {
				custDetails.put("w_cnic", "");
			} else
				custDetails.put("w_cnic", result.getString("w_cnic"));
			custDetails.put("user", result.getString("user"));
			custDetails.put("modified_date", result.getString("modified_date"));
			custDetails.put("is_active", result.getString("is_active"));

			custDetails.put("is_prepaid", result.getString("is_prepaid"));
			custDetails.put("current_bill_group",
					result.getString("current_bill_group"));
			custDetails.put("is_warid", result.getString("is_warid"));

			custDetails.put("ben_fname", result.getString("ben_fname"));
			custDetails.put("ben_sname", result.getString("ben_sname"));
			custDetails.put("ben_age", result.getString("ben_age"));
			custDetails.put("cust_relationship",
					result.getString("cust_relationship"));

			if (result.getString("ben_gender") == null)
				custDetails.put("ben_gender", "");
			else
				custDetails.put("ben_gender", result.getString("ben_gender"));
			if (result.getString("ben_msisdn") == null)
				custDetails.put("ben_msisdn", "");
			else
				custDetails.put("ben_msisdn", result.getString("ben_msisdn"));

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getCustomerDetails queries", e);
		}
		return custDetails;
	}

	public static List<String> getRegisteredProduct(String msisdn) {
		List<String> offer = new ArrayList<String>();
		logger.info("Executing getRegisteredProduct query");
		try {

			result = MIP_DataBaseConnection.st
					.executeQuery("select od.offer_name from offer_details od join offer_subscription os on od.offer_id=os.offer_id"
							+ " join customer_details cd on cd.cust_id=os.cust_id where cd.msisdn="
							+ msisdn + ";");
			while (result.next()) {
				offer.add(result.getString("offer_name").trim());
			}

		} catch (Exception e) {
			logger.error(
					"Error while executing the getRegisteredProduct queries", e);
		}
		return offer;
	}

	public static String getOfferCover(String msisdn, String offerName) {
		String offer = "";
		logger.info("Executing getOfferCover query");
		try {

			result = MIP_DataBaseConnection.st
					.executeQuery("select  TRIM(TRAILING '.' FROM TRIM(TRAILING  '0' from ocd.offer_cover)) as offer_cover from offer_cover_details ocd join offer_subscription os on"
							+ " os.offer_cover_id=ocd.offer_cover_id join offer_details oc on os.offer_id=oc.offer_id  "
							+ " where os.cust_id=(Select cust_id from customer_details where msisdn="
							+ msisdn
							+ ") and oc.offer_name like '%Bima Accident Protection';");
			while (result.next()) {
				offer = result.getString("offer_cover").trim();
			}

		} catch (Exception e) {
			logger.error("Error while executing the getOfferCover queries", e);
		}
		return offer;
	}

	public static List<String> getRegistrationSMS(String msisdn,
			String offerName) {
		List<String> sms = new ArrayList<String>();
		logger.info("Executing getRegistrationSMS query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("select sms_text from sms_in_queue where sms_msisdn="
							+ msisdn
							+ " and sms_template_name like '%registration_success'  order by sms_queue_id;");

			/*
			 * if (offerName
			 * .equalsIgnoreCase(MIP_CustomerManagementPage.BIMAHEALTH)) {
			 * result = MIP_DataBaseConnection.st
			 * .executeQuery("select sms_text from sms_in_queue where sms_msisdn='"
			 * + msisdn +
			 * "' and (sms_template_name ='registration_details_sms_hp' " +
			 * " or sms_template_name='dashboard_tigo_registration_success_hp') order by sms_queue_id;"
			 * ); } if (offerName.contains(MIP_CustomerManagementPage.BIMALIFE)
			 * && (offerName .contains(MIP_CustomerManagementPage.BIMAHEALTH)))
			 * { result = MIP_DataBaseConnection.st
			 * .executeQuery("select sms_text from sms_in_queue where sms_msisdn='"
			 * + msisdn +
			 * "' and (sms_template_name like 'registration_details_sms%'" +
			 * " or sms_template_name='dashboard_tigo_registration_success_life_hp') order by sms_queue_id;"
			 * ); }
			 */

			while (result.next()) {
				sms.add(result.getString("sms_text"));
			}

		} catch (Exception e) {
			logger.error(
					"Error while executing the getRegistrationSMS queries", e);
		}
		return sms;
	}

	public static String getOfferCoverLife(String msisdn, String offerName,
			String ben_level) {
		String offer = "";
		logger.info("Executing getOfferCoverLife query");
		try {

			result = MIP_DataBaseConnection.st
					.executeQuery("select offer_cover_charges from offer_cover_details where offer_cover_life="
							+ ben_level
							+ " and offer_id=(select offer_id from offer_details where offer_name='"
							+ offerName + "')");
			while (result.next()) {
				offer = result.getString("offer_cover_charges").trim();
			}

		} catch (Exception e) {
			logger.error("Error while executing the getOfferCoverLife queries",
					e);
		}
		return offer;
	}

	public static void connectToWaridDBAndInserRecord(String platform,
			String name, String msisdn, String prepost_paid, String cni) {
		Connection con = null;
		Statement st = null;
		int status = 30;
		String billcycle = "30";
		String id_type = "N";
		try {
			logger.info("Connecting to Warid Database");
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			if (platform.equalsIgnoreCase("windows")) {
				con = DriverManager.getConnection("jdbc:mysql:"
						+ MIP_ReadPropertyFile.getPropertyValue("wariddburl"),
						MIP_ReadPropertyFile
								.getPropertyValue("wariddbusername"),
						MIP_ReadPropertyFile
								.getPropertyValue("wariddbpassword"));
			} else if (platform.equalsIgnoreCase("AWS")) {
				con = DriverManager.getConnection("jdbc:mysql:"
						+ MIP_ReadPropertyFile.getPropertyValue("awsdburl"),
						MIP_ReadPropertyFile.getPropertyValue("awsdbusername"),
						MIP_ReadPropertyFile.getPropertyValue("awsdbpassword"));
			}

			if (con != null) {
				logger.info("Connecting to Database is successfull");
			}
		} catch (ClassNotFoundException e) {
			logger.error("Error while connecting to DataBase", e);
		} catch (SQLException e) {
			logger.error("Error while connecting to DataBase", e);
		} catch (InstantiationException e) {
			logger.error("Error while connecting to DataBase", e);
		} catch (IllegalAccessException e) {
			logger.error("Error while connecting to DataBase", e);
		}
		try {
			st = con.createStatement();
		} catch (SQLException e) {
			logger.error("Error while creating statement", e);
		}
		try {
			//st.executeUpdate("ALTER table crm_user_info_vw  MODIFY VENDOR_INFO_ID INTEGER NOT NULL AUTO_INCREMENT;");
			st.executeUpdate("insert into crm_user_info_vw(NAME,subno,prepost_paid,CNIC,status,billcycle,id_type)"
					+ " values('"
					+ name
					+ "',"
					+ msisdn.substring(1)
					+ ",'"
					+ prepost_paid
					+ "',"
					+ cni
					+ ","
					+ status
					+ ","
					+ billcycle + ",'" + id_type + "');");
			if (prepost_paid
					.equalsIgnoreCase(MIP_CustomerManagementPage.BIMA_POSTPAID)) {
				st.executeUpdate("insert into vendor_info_vw (Name,SUBNO,STATUS,PREPOST_PAID,BILLGROUP) values('"
						+ name
						+ "',"
						+ msisdn.substring(1)
						+ ","
						+ status
						+ ",'" + prepost_paid + "'," + billcycle + ");");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		MIP_ReadPropertyFile.loadProperty("config");
		MIP_DataBaseConnection.connectToDatabase("windows");
		/*
		 * String str = getRegisteredProduct("03026566500").toString();
		 * System.out.println(str);
		 * System.out.println("Warid\\Jazz Bima Accident Protection");
		 * System.out
		 * .println(str.contains("Warid\\Jazz Bima Accident Protection"));
		 */
		connectToWaridDBAndInserRecord("windows", "test", "03026566501",
				"POST", "2132779103324");
		// System.out.println(str);
		String str = getRegisteredProduct("03026566500").toString();
		System.out.println(str);
	}
}
