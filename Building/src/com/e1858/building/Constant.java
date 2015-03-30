package com.e1858.building;

public class Constant {
	public static final int		FetchCount								= 10;
	public static final int		PICTURE_MAX_SIZE						= 1024 * 1024;			//像素
	public static final int		PICTURE_MIN_SIDE_LENGTH					= 800;

	public static final String	Service_Phone							= "4007007737";
	public final static String	ENCODING								= "UTF-8";
	public final static int		CONNECT_TIMEOUT							= 60 * 1000;
	public final static int		NETWORK_CHECK_INTERVAL					= 5 * 1000;
	public final static int		NETWORK_AVAILABLE_SOCKET_OPEN_INTERVAL	= 5000;
	public final static int		LOCATION_LISTEN_INTERVAL				= 60 * 1000;

	public final static int		FAIL_CODE								= 300;
	public static final String	IMAGE_TYPE								= "image/*";

	public final static String	USER_INFO								= "UserInfo";
	public final static String	KEY										= "key";
	public final static String	USERID									= "userID";
	public final static String	USERNAME								= "username";
	public final static String	PASSWORD								= "password";
	public static final String	PUSHSWITCH								= "PushSwitch";
	public static final String	HEADURL									= "headurl";

	public static final String	NOTICE_NUM								= "notice_num";
	//用户位置相关
	public final static String	LATITUDE								= "latitude";
	public final static String	LONGITUDE								= "longitude";

	//定位的省市信息
	public final static String	PROVINCE_LOCATION						= "province_location";
	public final static String	CITY_LOCATION							= "city_location";
	//修改的省市信息
	public static final String	PROVINCE								= "province";
	public final static String	CITY									= "city";
	public final static String	DISTRICT								= "district";

	public static final class DIRCONFIG {
		public static String		ROOT_PATH		= "";
		public final static String	ROOT_NAME		= "building";
		public static String		CRASH_PATH		= "";
		public static String		CACHE_PATH		= "data/building/cache/";
		public static String		DATABASE_PATH	= "data/building/database/";
	}

	/**
	 * 缓存
	 * 
	 * @author momo
	 *         2014-12-26下午4:43:59
	 */
	public static final class CACHE {
		public final static String	CACHE_DB_NAME	= "build_cache.db";
	}

	/**
	 * 判断引导页是否显示，sp
	 */
	public final static String	VERSION_CODE	= "version_code";

	/**
	 * 验证码类型
	 * date: 2015-1-23 下午3:57:17
	 * 
	 * @author wangzhongli
	 */
	public final static class ACTION_TYPE {
		public final static int	REGISTE					= 1;	//注册
		public final static int	RESET_PASSWORD			= 2;	//忘记密码
		public final static int	VERIFY_LOGIN			= 3;	//动态登录验证码
		public final static int	SET_MONEY_PASSWORD		= 4;	//设置钱包密码
		public final static int	CHANGE_MONEY_PASSWORD	= 6;	//修改钱包密码
	}

	/**
	 * Toast 提示语
	 * date: 2015-1-23 下午3:57:40
	 * 
	 * @author wangzhongli
	 */
	public final static class ToastMessage {
		public final static String	EMPTY_USERNAME			= "请输入用户名";
		public final static String	EMPTY_PASSWORD			= "请输入密码";
		public final static String	CONFIRM_PASSWORD		= "请再次确认密码";
		public final static String	USED_USERNAME			= "您输入的用户名已存在,请重新输入.";
		public final static String	NO_EQUAL_PASSWORD		= "您输入的密码不一致,请重新输入";
		public final static String	EQUAL_OLDPASSWORD		= "您输入的新密码和原来的密码重复,请重新输入";
		public final static String	USERNAME_LACK_LENGTH	= "用户名长度低于6个字符,请重新输入";
		public final static String	PASSWORD_LACK_LENGTH	= "请输入6-24位数字或字母";
		public final static String	EMPTY_VERIFY			= "请输入验证码";
		public final static String	SUCCESS_VERIFY			= "验证码发送成功!";
		public final static String	FAIL_VERIFY				= "验证码发送失败,请稍后重试!";
		public final static String	RESETPASS_SUCCESS		= "重置密码成功!";
		public final static String	RESETPASS_SUCCESS1		= "重置密码成功，请重新登录！";
		public final static String	RESETPASS_FAIL			= "重置密码失败,请稍后重试";
		public final static String	REGIST_FAIL				= "注册失败!";
		public final static String	AGREEMENT				= "请同意注册许可及服务协议";
		public final static String	MOBILE_REGP_ERROR		= "请输入有效的手机号码";
	}

	/**
	 * 订单状态
	 * date: 2015-1-23 下午3:57:55
	 * 
	 * @author wangzhongli
	 */
	public static final class OrderStatus {
		//待预约  0         待确认3            待提货  4                     待完成5                          已完成 6                           已取消7                            已过期8
		public static final int	UNKNOW		= -1;
		public static final int	NOTRESERVE	= 0;
		public static final int	NOTCONFIRM	= 3;
		public static final int	NOTACCEPT	= 4;
		public static final int	NOTCOMPLETE	= 5;
		public static final int	COMPLETE	= 6;
		public static final int	CANCELED	= 7;
		public static final int	OVERDUE		= 8;
	}

	/**
	 * 签到类型
	 * date: 2015-1-23 下午3:58:09
	 * 
	 * @author wangzhongli
	 */
	public static final class SIGNADDRESSTYPE {
		public static final int	ISARRIVE	= 1;	//上门签到
		public static final int	ISACCEPT	= 2;	//提货签到
	}

	/**
	 * 功能：上传照片标记
	 * 日期: 2015-1-30 下午3:04:47
	 * 作者: wangzhongli
	 */
	public static final class UPDATEPICFLAG {
		public static final int	FLAG_ACCEPT			= 1;	//提货完成上传照片
		public static final int	FLAG_COMPLETE		= 2;	//订单完成上传照片
		public static final int	FLAG_NOT_ACCEPT		= 3;	//待提货上传照片
		public static final int	FLAG_NOT_COMPLETE	= 4;	//待确认完成上传照片
	}

	/**
	 * 功能：实名认证状态
	 * 日期: 2015-2-8 下午4:39:23
	 * 作者: wangzhongli
	 */
	public static final class AUTH {
		public static final int	SUCCESS		= 1;
		public static final int	NOT_UPDATE	= -1;
		public static final int	AUTHING		= 0;
		public static final int	AUTH_FAIL	= 2;
	}

	/**
	 * 功能： 通知类型
	 * 日期: 2015-2-9 下午3:22:07
	 * 作者: wangzhongli
	 */
	public static final class NOTICE_TYPE {
		public static final int	TYPE_COMMON		= 1;
		public static final int	TYPE_PRIVATE	= 2;
		public static final int	TYPE_ALL		= 3;
	}

	/**
	 * 功能： 通知类型
	 * 日期: 2015-2-9 下午3:22:07
	 * 作者: wangzhongli
	 */
	public static final class PUSH_TYPE {
		public static final int	TYPE_PAIDAN		= 1;
		public static final int	TYPE_RELEASE	= 2;
		public static final int	TYPE_ARRIVE		= 3;
	}
}
