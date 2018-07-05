package com.milvik.mip.dbqueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.Assert;

import com.milvik.mip.dataprovider.MIP_DeRegisterCustomer_TestData;
import com.milvik.mip.pageutil.MIP_CustomerManagementPage;
import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_Logging;
import com.milvik.mip.utility.MIP_ReadPropertyFile;

public class MIP_DeRegisterCustomer_Queries {
	static ResultSet result;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_DeRegisterCustomer_Queries");
	}
	static final int CONFIRMATION_STATUS = 1;
	static final String CONFIRMATION_SUCCESS = "SUCCESS";
	static Map<String, String> custDetails = new HashMap<String, String>();
	static Map<String, String> deregcustDetails = new HashMap<String, String>();

	public static List<String> getProductToDeregister(String msisdn) {
		List<String> product_name = new ArrayList<String>();
		logger.info("Executing getProductToDeregister query");
		try {

			result = MIP_DataBaseConnection.st
					.executeQuery("select distinct(offer_name) from offer_details od  join offer_subscription os on os.offer_id=od.offer_id"
							+ " join customer_details cd on cd.cust_id=os.cust_id where os.is_deactivated=0 and cd.msisdn="
							+ msisdn + ";");
			while (result.next()) {
				product_name.add(result.getString("offer_name").trim());
			}

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getProductToDeregister queries",
					e);
		}
		return product_name;
	}

	public static Map<String, String> getDeductedAndCoverEaned(String msisdn,
			String offer) {
		logger.info("Getting DeductedAndCoverEaned Amount");
		Map<String, String> amount = new HashMap<String, String>();
		try {

			result = MIP_DataBaseConnection.st
					.executeQuery("select deducted_offer_amount,offer_cover from offer_subscription where cust_id=(select cust_id from customer_details where msisdn="
							+ msisdn + ");");
			while (result.next()) {
				if (result.getString("deducted_offer_amount") == null)
					amount.put("deducted_offer_amount", "0");
				else
					amount.put("deducted_offer_amount",
							result.getString("deducted_offer_amount"));
				if (result.getString("offer_cover") == null)
					amount.put("cover_earned", "0");
				else
					amount.put("cover_earned", result.getString("offer_cover"));
			}
		} catch (SQLException e) {
			logger.error(
					"Error while executing the getProductToDeregister queries",
					e);
		}
		return amount;
	}

	public static String getConfirmationStatus(String msisdn, String offer) {
		logger.info("Getting Customer Confirmation Status");
		String conf_state = "";
		try {

			result = MIP_DataBaseConnection.st
					.executeQuery("select is_confirmed from offer_subscription where cust_id=(select cust_id from customer_details where msisdn="
							+ msisdn + ");");
			while (result.next()) {
				if (result.getString("is_confirmed").equals("0")) {
					conf_state = "Not Confirmed";
				} else if (result.getString("is_confirmed").equals("1")) {
					conf_state = "Confirmed";
				}
			}
		} catch (SQLException e) {
			logger.error(
					"Error while executing the getProductToDeregister queries",
					e);
		}
		return conf_state;
	}

	public static Map<String, String> getqaConfirmationrecord(String msisdn,
			String offer) {
		logger.info("Getting QA  Confirmation Record");
		Map<String, String> qa_details = new HashMap<String, String>();
		try {

			result = MIP_DataBaseConnection.st
					.executeQuery("select is_prepaid,(select offer_cover from offer_cover_details where offer_cover_id=cover_level) as offer_cover"
							+ " ,is_confirmed,(select user_uid from user_details where user_id=confirmed_by) as created_by from qa_confirmation"
							+ " where msisdn=" + msisdn + ";");
			while (result.next()) {
				qa_details
						.put("prepost_status", result.getString("is_prepaid"));
				qa_details.put("offer_cover", result.getString("offer_cover"));
				qa_details
						.put("is_confirmed", result.getString("is_confirmed"));
				qa_details.put("created_by", result.getString("created_by"));
			}
		} catch (SQLException e) {
			logger.error(
					"Error while executing the getqaConfirmationrecord queries",
					e);
		}
		return qa_details;
	}

	public static String getConfirmationBy(String msisdn, String offer) {
		logger.info("Getting Customer Confirmation Status");
		String conf_by = "";
		try {

			result = MIP_DataBaseConnection.st
					.executeQuery("select (select user_uid from user_details where user_id=confirmed_by) as confirmed_by from offer_subscription where cust_id=(select cust_id from customer_details where msisdn="
							+ msisdn + ");");
			while (result.next()) {
				return result.getString("confirmed_by");

			}
		} catch (SQLException e) {
			logger.error(
					"Error while executing the getProductToDeregister queries",
					e);
		}
		return conf_by;
	}

	public static List<String> confirmTheCustomer(String msisdn,
			String conf_by, String prepost) {
		logger.info("Executing confirmTheCustomer query");
		List<String> conf_status = new ArrayList<String>();
		try {

			result = MIP_DataBaseConnection.st
					.executeQuery("select user_id from user_details where user_uid='"
							+ conf_by + "';");
			result.next();
			int user = result.getInt("user_id");
			result = MIP_DataBaseConnection.st
					.executeQuery("call CONFIRM_CUSTOMER('" + msisdn + "'," + 1
							+ "," + user + ",0,'" + prepost + "'," + 0 + ","
							+ 1 + ");");
			result.next();
			Assert.assertTrue(result.getString("statusMessage")
					.equalsIgnoreCase(CONFIRMATION_SUCCESS));
			result = MIP_DataBaseConnection.st
					.executeQuery("select is_confirmed from offer_subscription where cust_id=(select cust_id from customer_details where msisdn="
							+ msisdn + ");");
			result.next();
			int is_confirmed = result.getInt("is_confirmed");
			if (is_confirmed == CONFIRMATION_STATUS) {
				logger.info("Confirmation is successfull");
			} else {
				logger.info("Confirmation is not successfull Test Failed");
			}
			Assert.assertEquals(is_confirmed, CONFIRMATION_STATUS);

		}

		catch (SQLException e) {
			logger.error(
					"Error while executing the confirmTheCustomer queries", e);
		}
		return conf_status;
	}

	public static Map<String, String> getDeregisterCustomerDetails(
			String msisdn, String offer) {
		logger.info("Executing getDeregisterCustomerDetails query");
		Map<String, String> cust_details = new HashMap<String, String>();
		try {

			result = MIP_DataBaseConnection.st
					.executeQuery("select dc.c_name,dc.c_age,DATE_FORMAT(dc.dob,'%d/%m/%Y')as dob,dc.cnic,dc.w_cnic,dc.c_gender,dc.is_prepaid,dc.ins_name,dc.ins_msisdn,"
							+ " dc.ins_age,dc.ins_gender,ocd.offer_cover,cc.chn_name,is_confirmed,ud.user_uid as dereg_by,dc.dc_date,dc.record_deletion_date,dc.is_warid"
							+ " from de_registered_customers dc join communication_channel cc on cc.chn_id=dc.registration_channel_id"
							+ " join user_details ud on ud.user_id=dc.dc_by"
							+ " join offer_details od on od.offer_id=dc.offer_id"
							+ " join offer_cover_details ocd on ocd.offer_cover_id=dc.offer_cover_id"
							+ " where dc.msisdn=" + msisdn + " limit 1;");

			while (result.next()) {
				cust_details.put("cust_name", result.getString("c_name"));
				if (result.getString("c_age") == null)
					cust_details.put("cust_age", "");
				else {
					cust_details.put("cust_age", result.getString("c_age"));
				}
				if (result.getString("dob") == null)
					cust_details.put("cust_dob", "");
				else {
					cust_details.put("cust_dob", result.getString("dob"));
				}
				cust_details.put("cust_gender", result.getString("c_gender"));
				cust_details.put("is_prepaid", result.getString("is_prepaid"));
				cust_details.put("ben_name", result.getString("ins_name"));
				cust_details.put("ben_msisdn", result.getString("ins_msisdn"));
				cust_details.put("ben_age", result.getString("ins_age"));
				cust_details.put("ben_gender", result.getString("ins_gender"));
				cust_details
						.put("offer_cover", result.getString("offer_cover"));
				cust_details.put("chn_name", result.getString("chn_name"));
				cust_details.put("dereg_by", result.getString("dereg_by"));
				cust_details.put("dereg_date", result.getString("dc_date"));
				cust_details.put("record_deletion_date",
						result.getString("record_deletion_date"));
				cust_details.put("is_warid", result.getString("is_warid"));
				if (result.getString("is_confirmed").equals("1")) {
					cust_details.put("conf_status", "Confirmed");
				} else if (result.getString("is_confirmed").equals("0")) {
					cust_details.put("conf_status", "Not confirmed");
				}

			}
		} catch (SQLException e) {
			logger.error(
					"Error while executing the getDeregisterCustomerDetails queries",
					e);
		}
		return cust_details;
	}

	public static String getCustomerDeregisterStatus(String msisdn,
			String productname) {
		String state = "";
		logger.info("Executing getCustomerDeregisterStatus query");
		try {

			result = MIP_DataBaseConnection.st
					.executeQuery("select is_deactivated from offer_subscription where cust_id=(select cust_id from customer_details  where msisdn=7307671)  and offer_id=(select offer_id"
							+ " from offer_details where offer_name='"
							+ productname + "');");
			while (result.next()) {
				state = (result.getString("is_deactivated"));
			}
		} catch (SQLException e) {
			logger.error(
					"Error while executing the getCustomerDeregisterStatus queries",
					e);
		}
		return state;
	}

	public static Map<String, String> getbimaCancellationRecord(String msisdn,
			String offer) {
		Map<String, String> cust_details = new HashMap<String, String>();
		logger.info("Executing getbimaCancellationRecord query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("select bc.cfname,bc.csname,bc.age,bc.gender,bc.irfname,bc.irsname,bc.irage,ud.user_uid,bc.bc_date,oc.record_deletion_date,oc.is_reactivated,oc.reactivated_date"
							+ " from bima_cancellations bc join user_details ud on bc.bc_by=ud.user_id join offer_cancellations oc"
							+ " on oc.bc_id=bc.bc_id where bc.msisdn="
							+ msisdn
							+ "  and oc.offer_id=(select offer_id from offer_details where offer_name='"
							+ offer + "');");

			while (result.next()) {
				cust_details.put("cust_fname", result.getString("cfname"));
				cust_details.put("cust_sname", result.getString("csname"));
				cust_details.put("cust_age", result.getString("age"));
				cust_details.put("cust_gender", result.getString("gender"));
				cust_details.put("ben_fname", result.getString("irfname"));
				cust_details.put("ben_sname", result.getString("irsname"));
				cust_details.put("ben_age", result.getString("irage"));
				cust_details.put("dereg_by", result.getString("user_uid"));
				cust_details.put("dereg_date", result.getString("bc_date"));

				if (result.getString("record_deletion_date") == null)
					cust_details.put("record_deletion_date", "");
				else
					cust_details.put("record_deletion_date",
							result.getString("record_deletion_date"));
				if (result.getString("is_reactivated") == null)
					cust_details.put("is_reactivated", "");
				else
					cust_details.put("is_reactivated",
							result.getString("is_reactivated"));
				if (result.getString("reactivated_date") == null)
					cust_details.put("reactivated_date", "");
				else
					cust_details.put("reactivated_date",
							result.getString("reactivated_date"));

			}
		} catch (SQLException e) {
			logger.error(
					"Error while executing the getbimaCancellationRecord queries",
					e);
		}
		return cust_details;
	}

	public static List<String> getDeRegisteredSMS(String msisdn, String product) {
		String sms_template = "";
		List<String> sms = new ArrayList<String>();
		logger.info("Executing getDeRegisteredSMS query");
		if (product.equalsIgnoreCase(MIP_CustomerManagementPage.BIMAHEALTH))
			sms_template = "dashboard_bima_hp_dereg_success";
		else if (MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION
				.contains(product))
			sms_template = "dashboard_bima_dereg_success";
		else if (product
				.contains(MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION)
				&& product.contains(MIP_CustomerManagementPage.BIMAHEALTH))
			sms_template = "dashboard_bima_%_dereg_success";
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("select sms_text from sms_in_queue where sms_msisdn="
							+ msisdn
							+ " and sms_template_name like '"
							+ sms_template + "';");
			while (result.next()) {
				if (result.getString("sms_text") == null)
					sms.add("");
				else
					sms.add(result.getString("sms_text"));

			}
		} catch (SQLException e) {
			logger.error(
					"Error while executing the getDeRegisteredSMS queries", e);
		}
		return sms;
	}

	public static List<String> getConfirmationSMS(String msisdn, String product) {
		String sms_template = "";
		List<String> sms = new ArrayList<String>();
		logger.info("Executing getConfirmationSMS query");
		if (product.equalsIgnoreCase(MIP_CustomerManagementPage.BIMAHEALTH))
			sms_template = "dashboard_bima_hp_dereg_success";
		else if (MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION
				.contains(product))
			sms_template = "confirmation_success";
		else if (product
				.contains(MIP_CustomerManagementPage.BIMA_ACCIDENT_PROTECTION)
				&& product.contains(MIP_CustomerManagementPage.BIMAHEALTH))
			sms_template = "dashboard_bima_%_dereg_success";
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("select sms_text from sms_in_queue where sms_msisdn="
							+ msisdn
							+ " and sms_template_name like '"
							+ sms_template + "';");
			while (result.next()) {
				if (result.getString("sms_text") == null)
					sms.add("");
				else
					sms.add(result.getString("sms_text"));

			}
		} catch (SQLException e) {
			logger.error(
					"Error while executing the getDeRegisteredSMS queries", e);
		}
		return sms;
	}

	public static boolean checkRecordExistAfterDereg(String msisdn) {
		logger.info("Executing checkRecordExistAfterDereg query");
		String status = "";
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT EXISTS(select * from customer_details cd join offer_subscription os on cd.cust_id=os.cust_id  where cd.msisdn="
							+ msisdn + ")as detail_exists;");
			result.next();
			if (result.getString("detail_exists") != null)
				status = result.getString("detail_exists");
			if (status.equals("0")) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			logger.error(
					"Error while executing the getDeRegisteredSMS queries", e);
		}
		return false;
	}

	public static String recorddeletiondate() {
		logger.info("Executing recorddeletiondate query");
		String status = "";
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT LAST_DAY(DATE_ADD(CURDATE(), INTERVAL 4 MONTH));");
			result.next();
			if (result.getString("recorddeletiondate") != null)
				status = result.getString("recorddeletiondate");

		} catch (SQLException e) {
			logger.error(
					"Error while executing the recorddeletiondate queries", e);
		}
		return status;
	}

	public static Map<String, String> checkReactivationStatus(String msisdn,
			String offer) {
		logger.info("Executing checkReactivationStatus query");
		Map<String, String> react_details = new HashMap<String, String>();

		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("select ocd.offer_cover as prevoius_offer,ocd1.offer_cover as new_offer,rc.is_confirmed,ud.user_uid as confirmed_by,cc.chn_name,rc.created_date,ud1.user_uid as created_by,rc.is_prepaid,is_attached"
							+ " from reactive_customer rc left join user_details ud on rc.confirmed_by=ud.user_id"
							+ " left join user_details ud1 on rc.created_by=ud1.user_id"
							+ " left join offer_cover_details ocd on rc.previous_offer_cover_id=ocd.offer_cover_id"
							+ " left join offer_cover_details ocd1 on rc.new_offer_cover_id=ocd1.offer_cover_id"
							+ " left join communication_channel cc on cc.chn_id=rc.confirmation_channel_id where rc.cust_id=(select cust_id from customer_details where msisdn="
							+ msisdn + ");");
			while (result.next()) {
				react_details.put("prevoius_offer",
						result.getString("prevoius_offer"));
				react_details.put("new_offer", result.getString("new_offer"));
				if (result.getString("is_confirmed") == null)
					react_details.put("is_confirmed", "");
				else
					react_details.put("is_confirmed",
							result.getString("is_confirmed"));
				if (result.getString("confirmed_by") == null)
					react_details.put("confirmed_by", "");
				else
					react_details.put("confirmed_by",
							result.getString("confirmed_by"));
				if (result.getString("chn_name") == null)
					react_details.put("chn_name", "");
				else
					react_details.put("chn_name", result.getString("chn_name"));
				if (result.getString("created_date") == null)
					react_details.put("created_date", "");
				else
					react_details.put("created_date",
							result.getString("created_date"));
				if (result.getString("created_by") == null)
					react_details.put("created_by", "");
				else
					react_details.put("created_by",
							result.getString("created_by"));
				if (result.getString("is_prepaid") == null)
					react_details.put("is_prepaid", "");
				else
					react_details.put("is_prepaid",
							result.getString("is_prepaid"));
				if (result.getString("is_attached") == null)
					react_details.put("is_attached", "");
				else
					react_details.put("is_attached",
							result.getString("is_attached"));
			}
		} catch (SQLException e) {
			logger.error(
					"Error while executing the checkReactivationStatus queries",
					e);
		}
		return react_details;
	}

	public static String getMonthlyChargesPerOffer(String msisdn, String offer,
			String offer_cover) {
		logger.info("Getting Customer Confirmation Status");
		String charges = "";
		try {

			result = MIP_DataBaseConnection.st
					.executeQuery("select (charges_per_deduction_with_tax*30) as charges from offer_cover_details where offer_cover like '"
							+ offer_cover + ".%';");
			while (result.next()) {
				charges = result.getString("charges");
			}
		} catch (SQLException e) {
			logger.error(
					"Error while executing the getProductToDeregister queries",
					e);
		}
		return charges;
	}

	public static void main(String[] args) throws SQLException {
		MIP_ReadPropertyFile.loadProperty("config");
		MIP_DataBaseConnection.connectToDatabase("windows");
		// getDeRegisteredSMS("197055556624", "pa 2017", "CDMA", "Sinhala");
		confirmTheCustomer("03026566513", "DASHBOARD", "PREP");
	}
}
