package com.e1858.building.password;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.e1858.building.MainApplication;
import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.bean.UserInfo;

/**
 * 日期: 2015年1月6日 下午5:24:22
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class PasswordManagerActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_manager);
		initView();
	}

	/**
	 */
	private void initView() {
		View listitem_login = findViewById(R.id.listitem_login);
		((TextView) listitem_login.findViewById(R.id.textView_title)).setText("登录密码");
		listitem_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
			}
		});
		View listitem_wallet = findViewById(R.id.listitem_wallet);
		((TextView) listitem_wallet.findViewById(R.id.textView_title)).setText("钱包密码");
		listitem_wallet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UserInfo userInfo = MainApplication.getInstance().getUserInfo();
				if (userInfo == null) {
					return;
				} else {
					if (userInfo.isSetMoneyPwd()) {
						startActivity(new Intent(getActivity(), ChangeMoneyPwdActiivty.class));
					} else {
						startActivity(new Intent(getActivity(), SetMoneyPwdActiivty.class));
					}
				}
			}
		});
	}
}
