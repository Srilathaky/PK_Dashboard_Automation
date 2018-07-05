package com.milvik.mip.constants;

import com.milvik.mip.dbqueries.MIP_AdminConfig_Queries;

public class MIP_Constants {
	public static final long TIMEOUTINSECONDS = 30;
	public static final int MAX_LOGIN_ATTEMPTS = MIP_AdminConfig_Queries
			.getMaxLoginAttempt();
	public static final int BLOCKED_ACCOUNT_STATUS = 1;
	public static final String DOB_FORMAT = "dd/MM/yyyy";

}
