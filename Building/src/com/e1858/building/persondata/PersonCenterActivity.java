package com.e1858.building.persondata;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.utils.SharedPreferencesUtils;
import com.e1858.building.AppManager;
import com.e1858.building.Constant;
import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.bean.UserInfo;
import com.e1858.building.httppackage.LogoutRequest;
import com.e1858.building.httppackage.LogoutResponse;
import com.e1858.building.manager.BadgeManager;
import com.e1858.building.mycase.MyCaseActivity;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.notice.NoticeListActivity;
import com.e1858.building.order.RobListActivity;
import com.e1858.building.setting.SettingActivity;
import com.e1858.building.utils.DialogUtil;
import com.e1858.building.utils.RequestUtils;
import com.e1858.building.utils.RequestUtils.OnUseInfoFinishListener;
import com.e1858.building.wallet.WalletActivity;
import com.hg.android.widget.CircularImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 功能：个人中心
 * 日期: 2015-2-9 下午7:09:28
 * 作者: wangzhongli
 */
public class PersonCenterActivity extends BaseActivity {
	BadgeManager				badgeManager;
	private CircularImage		head_portrait;
	private TextView			person_head_tv_nickname;
	private TextView			person_service_time;
	private TextView			person_tv_mobile;
	private TextView			person_tv_real_name;
	private TextView			person_tv_chengxin;
	private ImageView			person_iv_mobile;
	private ImageView			person_iv_real_name;
	private ImageView			person_iv_chengxin;
	private DisplayImageOptions	options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_center);
		initView();
		badgeManager = new BadgeManager(mContext);
	}

	public void initView() {
		//头像 姓名 服务次数 达人
		head_portrait = (CircularImage) this.findViewById(R.id.head_portrait);
		person_head_tv_nickname = (TextView) this.findViewById(R.id.person_head_tv_nickname);
		person_service_time = (TextView) this.findViewById(R.id.person_service_time);
		person_tv_mobile = (TextView) this.findViewById(R.id.person_tv_mobile);
		person_tv_real_name = (TextView) this.findViewById(R.id.person_tv_real_name);
		person_tv_chengxin = (TextView) this.findViewById(R.id.person_tv_chengxin);
		person_iv_mobile = (ImageView) this.findViewById(R.id.person_iv_mobile);
		person_iv_real_name = (ImageView) this.findViewById(R.id.person_iv_real_name);
		person_iv_chengxin = (ImageView) this.findViewById(R.id.person_iv_chengxin);

		//各个item
		RelativeLayout person_item_rl_not_reserve = (RelativeLayout) this.findViewById(R.id.person_item_rl_not_reserve);
		RelativeLayout person_item_rl_not_accept = (RelativeLayout) this.findViewById(R.id.person_item_rl_not_accept);
		RelativeLayout person_item_rl_not_complete = (RelativeLayout) this
				.findViewById(R.id.person_item_rl_not_complete);
		RelativeLayout person_item_rl_order_complete = (RelativeLayout) this
				.findViewById(R.id.person_item_rl_order_complete);
		RelativeLayout person_item_rl_cancel = (RelativeLayout) this.findViewById(R.id.person_item_rl_cancel);
		RelativeLayout person_item_rl_person_info = (RelativeLayout) this.findViewById(R.id.person_item_rl_person_info);
		RelativeLayout person_item_rl_service_info = (RelativeLayout) this
				.findViewById(R.id.person_item_rl_service_info);
		RelativeLayout person_item_rl_my_case = (RelativeLayout) this.findViewById(R.id.person_item_rl_my_case);
		RelativeLayout person_item_rl_money = (RelativeLayout) this.findViewById(R.id.person_item_rl_money);
		person_item_rl_not_reserve.setOnClickListener(this);
		person_item_rl_not_accept.setOnClickListener(this);
		person_item_rl_not_complete.setOnClickListener(this);
		person_item_rl_order_complete.setOnClickListener(this);
		person_item_rl_cancel.setOnClickListener(this);
		person_item_rl_person_info.setOnClickListener(this);
		person_item_rl_service_info.setOnClickListener(this);
		person_item_rl_my_case.setOnClickListener(this);
		person_item_rl_money.setOnClickListener(this);
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.personal_head_icon_def)
				.showImageForEmptyUri(R.drawable.personal_head_icon_def)
				.showImageOnFail(R.drawable.personal_head_icon_def).cacheInMemory(true).cacheOnDisk(true)
				.considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

	}

	@Override
	public void onResume() {
		RequestUtils.loadUserInfo(new OnUseInfoFinishListener() {

			@Override
			public void OnUseInfoFinish(UserInfo userInfo) {
				bindDate(userInfo);
			}

		});

//		UserInfo userInfo = MainApplication.getInstance().getUserInfo();

		super.onResume();
	}

	public void bindDate(UserInfo userInfo) {
		if (null != userInfo) {
			person_head_tv_nickname.setText(userInfo.getWorkerName() == null ? userInfo.getUserName() : userInfo
					.getWorkerName());
			person_service_time.setText(String.format("服务次数(%d)", userInfo.getServiceCount()));
			ImageLoader.getInstance().displayImage(userInfo.getHeadPortrait(), head_portrait, options);
			if (userInfo.getRealNameAuthState() == 1) {//实名达人
				person_tv_real_name.setTextColor(getResources().getColor(R.color.text_color_ffae00));
				person_iv_real_name.setBackgroundResource(R.drawable.shiming_focus);
			}
			if (userInfo.getMobileMan() == 1) {//手机达人
				person_tv_mobile.setTextColor(getResources().getColor(R.color.text_color_ffae00));
				person_iv_mobile.setBackgroundResource(R.drawable.shouji_focus);
			}
			if (userInfo.getHonestMan() == 1) {//诚信达人
				person_tv_chengxin.setTextColor(getResources().getColor(R.color.text_color_ffae00));
				person_iv_chengxin.setBackgroundResource(R.drawable.chengxin_focus);
			}
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.person_item_rl_not_reserve:
			startActivity(NotReserveActivity.class);
			break;
		case R.id.person_item_rl_not_accept:
			startActivity(NotAcceptActivity.class);
			break;
		case R.id.person_item_rl_not_complete:
			startActivity(NotCompleteActivity.class);
			break;
		case R.id.person_item_rl_order_complete:
			startActivity(OrderCompleteActivity.class);
			break;
		case R.id.person_item_rl_cancel:
			startActivity(OrderCancelActivity.class);
			break;
		case R.id.person_item_rl_person_info:
			startActivity(PersonInfoActivity.class);
			break;
		case R.id.person_item_rl_service_info:
			startActivity(ServiceInfoActivity.class);
			break;
		case R.id.person_item_rl_my_case:
			startActivity(MyCaseActivity.class);
			break;
		case R.id.person_item_rl_money:
			startActivity(WalletActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.setting) {
			startActivity(SettingActivity.class);
			return true;
		}
		if (item.getItemId() == R.id.notice_list) {
			startActivity(NoticeListActivity.class);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.setting, menu);
		//不显示通知列表
		menu.findItem(R.id.notice_list).setVisible(false);
		badgeManager.register(menu.findItem(R.id.notice_list), this);
		return true;
	}

	@Override
	protected void onDestroy() {
		badgeManager.unregister();
		super.onDestroy();
	}

	/**
	 * 退出登录
	 * 
	 * @param viewvoid
	 */
	public void logout(View view) {
		DialogUtil.dialogOpen(mContext, "温馨提示", "您确定要退出登录吗？", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				ResponseHandler<LogoutResponse> responseHandler = new ResponseHandler<LogoutResponse>() {
					@Override
					public void onStart() {
						SharedPreferencesUtils.setSpParam(mContext, Constant.KEY, "");
						SharedPreferencesUtils.setSpParam(mContext, Constant.USERID, "");
						UserInfo userInfo = new UserInfo();
						application.setUserName("");
						application.setPassword("");
						application.setUserInfo(userInfo);
						//程序退出
						AppManager.getAppManager().finishAllActivity();
						startActivity(RobListActivity.class);
//						startActivity(LoginActivity.class);
					}

					@Override
					public void onFinish(LogoutResponse response, String error) {
//						if (ResponseUtils.checkResponseAndToastError(response, error)) {
//							SharedPreferencesUtils.setSpParam(mContext, Constant.KEY, "");
//							SharedPreferencesUtils.setSpParam(mContext, Constant.USERID, "");
//							UserInfo userInfo = new UserInfo();
//							application.setUserName("");
//							application.setPassword("");
//							application.setUserInfo(userInfo);
//							//程序退出
//							AppManager.getAppManager().finishAllActivity();
//							startActivity(LoginActivity.class);
//						}
					}
				};

				LogoutRequest logoutRequest = new LogoutRequest();
				HttpPacketClient.postPacketAsynchronous(logoutRequest, LogoutResponse.class, responseHandler, true);

			}
		});
	}
}
