package com.mzoneapp.zjjmb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static String covertChinaDatetime(String datetime, boolean isShort) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dt = sdf.parse(datetime);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			StringBuilder sb = new StringBuilder();
			if (isShort) {
				sb.append(cal.get(Calendar.YEAR)).append("年")
						.append(cal.get(Calendar.MONTH)).append("月")
						.append(cal.get(Calendar.DAY_OF_MONTH)).append("日");
			} else {
				sb.append(cal.get(Calendar.YEAR)).append("年")
						.append(cal.get(Calendar.MONTH)).append("月")
						.append(cal.get(Calendar.DAY_OF_MONTH)).append("日 ")
						.append(cal.get(Calendar.HOUR_OF_DAY)).append("时")
						.append(cal.get(Calendar.MINUTE)).append("分")
						.append(cal.get(Calendar.MILLISECOND)).append("秒");
			}
			return sb.toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
