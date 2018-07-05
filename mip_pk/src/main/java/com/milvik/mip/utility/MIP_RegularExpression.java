package com.milvik.mip.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MIP_RegularExpression {

	public static boolean custNameMatcher(String matcher) {
		String pattern = "^[a-zA-Z '-]*$";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(matcher);
		if (!m.find()) {
			return true;
		}
		return false;

	}

	public static boolean idMatcher(String matcher) {
		String pattern = "^[\\w\\-]+$";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(matcher);
		if (!m.find()) {
			return true;
		}
		return false;

	}

}
