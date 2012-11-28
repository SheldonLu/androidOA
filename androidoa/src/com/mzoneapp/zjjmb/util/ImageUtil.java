package com.mzoneapp.zjjmb.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageUtil {

	public static final String IMGPATTERN = "<IMG(?:.*)src=(\"{1}|\'{1})([^\\[^>]+[gif|jpg|jpeg|bmp|bmp]*)(\"{1}|\'{1})(?:.*)>";

	public static String[] getImages(String htmlStr){
		Pattern pattern = java.util.regex.Pattern.compile(IMGPATTERN,
				java.util.regex.Pattern.CASE_INSENSITIVE);
		Matcher m = pattern.matcher(htmlStr);
		String res[] = new String[m.groupCount()];
		int i = 0;
		while (m.find()) {
			res[i++] = getImgStr(m.group())[0];
		}
		return res;
	}
	
	public static String resetImages(String html){
		return html.replaceAll(IMGPATTERN, "");
	}
	
	public static String[] getImgStr(String htmlStr) {
		String img = "";
		java.util.regex.Pattern p_image;
		java.util.regex.Matcher m_image;

		String regEx_img = "http://[([a-z0-9]|.|/|\\-)]+.[(jpg)|(bmp)|(gif)|(png)]";// 图片链接地址
		p_image = java.util.regex.Pattern.compile(regEx_img,
				java.util.regex.Pattern.CASE_INSENSITIVE);
		m_image = p_image.matcher(htmlStr);
		while (m_image.find()) {
			img = img + m_image.group()+",";
		}
		if("".equals(img)){
			return null;
		}
		if (img.indexOf(",") >= 0)
			return img.substring(0, img.length()-1).split(",");
		else
			return new String[]{img};
	}
	
}
