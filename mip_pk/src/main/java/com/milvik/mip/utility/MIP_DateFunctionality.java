package com.milvik.mip.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MIP_DateFunctionality {
	public static int exractYear(String dob) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date parse = null;
		try {
			parse = sdf.parse(dob);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(parse);
		return c.get(Calendar.YEAR);
	}

	public static int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	public static String[] getDate(String date, String format) {
		String[] data = date.split("/");

		int mon = new Integer(data[1]);
		String month = "";
		switch (mon) {
		case 01:
			month = "Jan";
			break;
		case 02:
			month = "Feb";
			break;
		case 03:
			month = "Mar";
			break;
		case 04:
			month = "Apr";
			break;
		case 05:
			month = "May";
			break;
		case 06:
			month = "Jun";
			break;
		case 07:
			month = "Jul";
			break;
		case 8:
			month = "Aug";
			break;
		case 9:
			month = "Sep";
			break;
		case 10:
			month = "Oct";
			break;
		case 11:
			month = "Nov";
			break;
		case 12:
			month = "Dec";
			break;
		default:
			month = null;
			break;
		}
		data[1] = month;
		return data;
	}

	public static String converDateToDBDateFormat(String dob) {
		String[] date = dob.split("/");
		String newDate = date[2] + "-" + date[1] + "-" + date[0];
		return newDate;
	}

	public static String getCurrentDate(String format) {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		Date d = new Date();
		return sf.format(d);
	}

}
