package com.mzoneapp.zjjmb.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.net.Uri;
import android.util.Log;

public class ApiConstants {

	private static final String LOG_TAG = ApiConstants.class.getName();
	// Main uri
	public static final String URL = "http://www.expressway.gov.cn/szjweb/Cont";
	
	// List params
	public static final String QUERY_LIST = "getList.action";
	public static final String PAGENO = "pageno";
	public static final String PAGESIZ = "pagesiz";

	// Info params
	public static final String QUERY_INFO = "getInfo.action";
	public static final String CONTENT = "content";

	// General params
	public static final String TYPE = "type";

	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String AUTHOR = "author";
	public static final String ISSURDATE = "issuedate";
	
	// type
	// 通知公告(默认)
	public static final String ANNOUNCEMENT = "1";
	// 局内动态
	public static final String IN_DYNAMIC = "2";
	// 工作动态
	public static final String WORK_DYNAMIC = "3";
	
	public static final int DEFAULT_SIZE = 10;

	private static ApiConstants instance;
	
	// List url
	public String getListUrl(String type){
		return getListUrl(type, 0 , DEFAULT_SIZE);
	}
	
	// List url
	public String getListUrl(String type,int pageno){
		return getListUrl(type, pageno, DEFAULT_SIZE);
	}
	
	// List url
	public String getListUrl(String type, int pageno, int pagesiz){
		HashMap<String, String> params = new HashMap<String,String>();
		params.put(TYPE, type);
		params.put(PAGENO, pageno+"");
		params.put(PAGESIZ, pagesiz+"");
		
		return createUrl(QUERY_LIST,params);
	}
	
	// Info url
	public String getInfoUrl(String id){
		HashMap<String, String> params = new HashMap<String,String>();
		params.put(ID, id+"");
		
		return createUrl(QUERY_INFO,params);
	}

	public String createUrl(String path, Map<String, String> params) {
		String uri = String.format("%s/%s?", URL, path);
		return addParams(uri, params);
	}

	public String addParams(String url, Map<String, String> params) {
		StringBuilder uri = new StringBuilder(url);
		for (Entry<String, String> param : params.entrySet()) {
			uri.append("&").append(Uri.encode(param.getKey())).append("=")
					.append(Uri.encode(param.getValue()));
		}
		Log.d(LOG_TAG, uri.toString());
		return uri.toString();
	}

	private ApiConstants() {
		// Force construction through static methods
	}

	public static void createInstance() {
		if (instance == null) {
			instance = new ApiConstants();
		}
	}

	public static ApiConstants instance() {
		return instance;
	}

}
