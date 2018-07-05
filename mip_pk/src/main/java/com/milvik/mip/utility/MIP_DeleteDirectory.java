package com.milvik.mip.utility;

import java.io.File;

import org.apache.log4j.Logger;

public class MIP_DeleteDirectory {
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_DeleteDirectory");
	}

	public static boolean deleteDirectory(File dir) {
		logger.info("Deleting folder " + dir.toString());
		if (dir.isDirectory()) {
			File[] childern = dir.listFiles();
			for (File f : childern) {
				boolean sucess = deleteDirectory(f);
				if (!sucess) {
					return false;
				}
			}
		}

		return dir.delete();
	}
}
