package com.milvik.mip.utility;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author srilatha
 *
 */
public class MIP_Logging {
	static Logger logger;

	/**
	 * This method initializes the log objects
	 * 
	 * @return
	 */
	public static Logger logDetails(String classname) {
		logger = Logger.getLogger(classname);
		PropertyConfigurator.configure("log4j.properties");
		return logger;

	}
}
