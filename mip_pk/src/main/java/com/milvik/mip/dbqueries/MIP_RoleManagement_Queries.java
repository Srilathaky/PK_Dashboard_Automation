package com.milvik.mip.dbqueries;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_RoleManagement_Queries {

	static ResultSet result;
	static Logger logger;
	static Map<String, String> userdetails = new HashMap<String, String>();
	static {
		logger = MIP_Logging.logDetails("MIP_RoleManagement_Queries");
	}

	public static boolean getRoleName(String rolename) {
		try {
			logger.info("Executing the getRoleName queries");
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT role_name FROM role_master where role_name='"
							+ rolename + "';");
			result.next();
			if (result.getString("role_name") == null) {
				return false;
			} else {
				if (result.getString("role_name").equalsIgnoreCase(rolename))
					return true;
				else
					return false;
			}
		} catch (Exception e) {
			logger.error("Error while executing the getRoleName queries", e);
			return false;
		}

	}

	public static List<String> getRoleAcess(String rolename) {
		List<String> roleaccess = new ArrayList<String>();
		try {
			logger.info("Executing the getRoleAcess queries");
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT m.menu_description FROM menu m JOIN menu_role_mapping mp on m.menu_id=mp.menu_id where mp.role_id=("
							+ "SELECT role_id FROM role_master where role_name='"
							+ rolename + "') ;");
			while (result.next()) {
				roleaccess.add(result.getString("menu_description"));
			}

		} catch (Exception e) {
			logger.error("Error while executing the getRoleAcess queries", e);
		}
		return roleaccess;
	}

	public static List<String> getMappedRoles(String roleName) {
		List<String> rolemap = new ArrayList<String>();
		try {
			logger.info("Executing the getMappedRoles queries");
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT me.menu_description FROM menu me join menu_role_mapping mr on mr.menu_id=me.menu_id where mr.role_id=(select role_id from role_master where role_name='"
							+ roleName + "');");
			while (result.next()) {
				rolemap.add(result.getString("menu_description"));
			}

		} catch (Exception e) {
			logger.error("Error while executing the getRoleAcess queries", e);
		}
		return rolemap;

	}

	public static String getComission(String roleName) {
		String res = "";
		try {
			logger.info("Executing the getComission queries");
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT commission FROM role_master where role_name='"
							+ roleName + "';");
			while (result.next()) {
				if (result.getString("commission") == null) {
					return res;
				} else {
					res = result.getString("commission");
					res=res.split("\\.")[0];
				}
			}

		} catch (Exception e) {
			logger.error("Error while executing the getRoleAcess queries", e);
		}
		return res;
		

	}
}
