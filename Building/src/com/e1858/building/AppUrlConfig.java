package com.e1858.building;

public class AppUrlConfig {

	public static final class api {
		public static final String	Host_URL_RELEASE	= "http://api.51mjmh.com/";					//正式   服务器

		public static final String	Host_URL_DEBUG		= "http://218.28.143.93:903/";					//测试服务器

		public static final String	BASE_URL			= Host_URL_RELEASE + "api/Building/Worker";
		public static final String	UPLOAD_URL			= Host_URL_RELEASE + "api/Building/Upload";

		//正式环境下的静态页面地址
		public final static String	PROTOCOL_URL		= Host_URL_RELEASE + "WebPage/Protocol.html";
		public final static String	ABOUT_URL			= Host_URL_RELEASE + "WebPage/AboutUs.html";
	}
}
