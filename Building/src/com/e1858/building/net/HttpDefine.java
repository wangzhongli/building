package com.e1858.building.net;

/**
 * 请求响应的所有状态码
 * 
 * @author momo
 *         2014-12-23上午9:57:16
 */
public class HttpDefine {

	public static final String	TimeFormat			= "yyyy-MM-dd HH:mm:ss";
	public static final int		RESPONSE_SUCCESS	= 0;

	static int RESP(int code) {
		return code;
	}

	public static final int	REGIST						= 1;						//注册用户
	public static final int	REGIST_RESP					= 1;
	public static final int	FINDPASSWORD				= 2;						//登录
	public static final int	FINDPASSWORD_RESP			= 2;
	public static final int	VERIFY						= 3;						//获取验证码
	public static final int	VERIFY_RESP					= 3;
	public static final int	LOGIN						= 4;						//登录
	public static final int	LOGIN_RESP					= 4;
	public static final int	LOGIN_VERIFY				= 5;						//动态验证码登录
	public static final int	LOGIN_VERIFY_RESP			= 5;
	public static final int	GETROBORDERS				= 6;						//获取抢单信息
	public static final int	GETROBORDERS_RESP			= 6;
	public static final int	GETROBORDERINFO				= 7;						//获取抢单详细信息
	public static final int	GETROBORDERINFO_RESP		= 7;
	public static final int	POSTORDERINFOS				= 8;						//提交抢单
	public static final int	POSTORDERINFOS_RESP			= 8;
	public static final int	GETMYPROFILE				= 9;						//获取个人信息
	public static final int	GETMYPROFILE_RESP			= 9;
	public static final int	MODIFYMYPROFILE				= 10;						//修改个人信息
	public static final int	MODIFYMYPROFILE_RESP		= 10;
	public static final int	GETMYSERVICEINFO			= 11;						//获取服务信息
	public static final int	GETMYSERVICEINFO_RESP		= 11;
	public static final int	MODIFYMYSERVICEINFO			= 12;						//修改服务信息
	public static final int	MODIFYMYSERVICEINFO_RESP	= 12;
	public static final int	CHECKVERSION				= 13;						//检查新版本
	public static final int	CHECKVERSION_RESP			= 13;
	public static final int	ADDFEEDBACK					= 14;						//意见反馈
	public static final int	ADDFEEDBACK_RESP			= 14;
	public static final int	CHANGEPASSWORD				= 15;						//修改密码
	public static final int	CHANGEPASSWORD_RESP			= 15;
	public static final int	EDITHEADPORTRAIT			= 16;						//修改头像
	public static final int	EDITHEADPORTRAIT_RESP		= 16;
	public static final int	LOGOUT						= 17;						//退出登录
	public static final int	LOGOUT_RESP					= 17;
	public static final int	CANCELORDER					= 18;						//取消订单
	public static final int	CANCELORDER_RESP			= 18;
	public static final int	GETCANCELEDORDER			= 19;						//查看取消订单
	public static final int	GETCANCELEDORDER_RESP		= 19;
	public static final int	DELETECANCELEDORDER			= 20;						//删除已经取消订单
	public static final int	DELETECANCELEDORDER_RESP	= 20;
	public static final int	GETMYCASE					= 21;						//查询我的案列
	public static final int	GETMYCASE_RESP				= 21;
	public static final int	GETORDERPICTURE				= 22;						//查询完成订单接口
	public static final int	GETORDERPICTURE_RESP		= 22;
	public static final int	SERVICEDONE					= 23;						//服务完成接口
	public static final int	SERVICEDONE_RESP			= 23;
	public static final int	GETCOMMENTINFO				= 24;						//查看评价信息接口
	public static final int	GETCOMMENTINFO_RESP			= 24;
	public static final int	GETNOTRESERVEORDERS			= 25;						//查看待预约接口
	public static final int	GETNOTRESERVEORDERS_RESP	= 25;
	public static final int	GETNOTACCEPTORDERS			= 26;						//查看待收货接口
	public static final int	GETNOTACCEPTORDERS_RESP		= 26;
	public static final int	GETNOTCOMPLETEORDERS		= 27;						//查看待完成接口
	public static final int	GETNOTCOMPLETEORDERS_RESP	= 27;
	public static final int	GETCOMPLETEORDERS			= 28;						//查看已完成接口
	public static final int	GETCOMPLETEORDERS_RESP		= 28;
	public static final int	SETMONEYPASSWORD			= 29;
	public static final int	SETCHANGEMONEYPASSWORD_RESP	= RESP(SETMONEYPASSWORD);

	public static final int	GETMYMONEYINFO				= 30;
	public static final int	GETMYBANKCARDS				= 31;						//	19
	public static final int	ADDBANKCARD					= 32;						//	20
	public static final int	DELETEBANKCARD				= 33;						//	20
	public static final int	CHECKMONEYPASSWORD			= 34;						//=加密上传验证;//	21
	public static final int	GETMYBUSINESSINFOS			= 35;						//	21
	public static final int	GETSERVICETYPES				= 36;
	public static final int	GETSERVICETYPES_RESP		= RESP(GETSERVICETYPES);
	public static final int	GETBANKINFOS				= 37;						//	22
	public static final int	REALNAMEAUTH				= 38;

	public static final int	CONFIRMRESERVE				= 39;						//确认预约接口
	public static final int	CONFIRMRESERVE_RESP			= 39;
	public static final int	SERVICEDONEUPPIC			= 40;						//服务完成上传图片接口
	public static final int	SERVICEDONEUPPIC_RESP		= 40;

	public static final int	CHANGEMONEYPASSWORD			= 42;

	public static final int	SIGNADDRESS					= 43;						//签到接口
	public static final int	SIGNADDRESS_RESP			= 43;

	public static final int	GETSERVICEWORKERTYPES		= 44;						//获得工人工种分类接口
	public static final int	GETSERVICEWORKERTYPES_RESP	= 44;

	public static final int	GETCANCELCAUSES				= 45;						//获得取消原因接口
	public static final int	GETCANCELCAUSES_RESP		= 45;
	public static final int	GETNOTICES					= 46;						//获得通知消息接口
	public static final int	GETNOTICES_RESP				= 46;
	public static final int	CONTINUESERVICE				= 47;						//继续服务接口
	public static final int	CONTINUESERVICE_RESP		= 47;
	public static final int	READNOTICE					= 48;						//读取通知消息接口
	public static final int	READNOTICE_RESP				= 48;
	public static final int	SETGETUICLIENTID			= 49;						//读取通知消息接口
	public static final int	SETGETUICLIENTID_RESP		= 49;

	public static final int	GETLOGISTICINFOS			= 50;						//获取商品物流信息接口
	public static final int	GETLOGISTICINFOS_RESP		= 50;

	public static String getCardTypeName(String cardType) {
		if ("0".equals(cardType)) {
			return "储蓄卡";
		}
		if ("1".equals(cardType)) {
			return "信用卡";
		}
		return "未知卡";
	}

}
