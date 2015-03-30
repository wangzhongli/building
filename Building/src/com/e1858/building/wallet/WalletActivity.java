package com.e1858.building.wallet;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.common.utils.ToastUtil;
import com.e1858.building.MainApplication;
import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.bean.ServiceTypeBean;
import com.e1858.building.bean.UserInfo;
import com.e1858.building.httppackage.CheckMoneyPasswordRequest;
import com.e1858.building.httppackage.CheckMoneyPasswordResponse;
import com.e1858.building.httppackage.GetMyWalletInfoRequest;
import com.e1858.building.httppackage.GetMyWalletInfoResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.password.SetMoneyPwdActiivty;
import com.e1858.building.utils.Encrypt;
import com.e1858.building.utils.RequestUtils;
import com.e1858.building.utils.ResponseUtils;
import com.hg.android.widget.EditTextDialog;

/**
 * 日期: 2015年1月6日 下午5:24:22
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class WalletActivity extends BaseActivity {

	SparseArray<TextView>	textViews	= new SparseArray<TextView>();
	List<ServiceTypeBean>	alltypes;
	GetMyWalletInfoResponse	walletInfoResponse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallet);
		initView();
		loadData();
		RequestUtils.loadUserInfo();
	}

	/**
	 */
	private void initView() {
		View listitem_balance = findViewById(R.id.listitem_balance);
		listitem_balance.findViewById(R.id.imageView_arrow).setVisibility(View.GONE);
		((TextView) listitem_balance.findViewById(R.id.textView_title)).setText("可用余额");

		View listitem_bankCard = findViewById(R.id.listitem_bankCard);
		((TextView) listitem_bankCard.findViewById(R.id.textView_title)).setText("添加银行卡");
		listitem_bankCard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UserInfo userInfo = MainApplication.getInstance().getUserInfo();
				if (userInfo == null) {
					ToastUtil.show(getActivity(), "正在获取用户信息");
				} else if (userInfo.isSetMoneyPwd()) {
					verifyWalletPassword(new Runnable() {
						@Override
						public void run() {
							startActivity(new Intent(getActivity(), BankCardActivity.class));
						}
					});

				} else {
					new AlertDialog.Builder(getActivity()).setTitle("温馨提示").setMessage("为了您的账户安全,请先设置钱包密码")
							.setPositiveButton("设置", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									startActivity(new Intent(getActivity(), SetMoneyPwdActiivty.class));
								}
							}).setNegativeButton("取消", null).show();
				}
			}
		});

		View listitem_business = findViewById(R.id.listitem_business);
		((TextView) listitem_business.findViewById(R.id.textView_title)).setText("交易记录");
		listitem_business.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				verifyWalletPassword(new Runnable() {
					@Override
					public void run() {
						startActivity(new Intent(getActivity(), BusinessActivity.class));
					}
				});
			}
		});
	}

	void verifyWalletPassword(final Runnable runnable) {

		EditTextDialog dialog = new EditTextDialog(getActivity(), "验证钱包密码", "请输入钱包密码", "",
				new EditTextDialog.OnFinishedListener() {

					@Override
					public void onFinished(String text) {
						if (TextUtils.isEmpty(text)) {
							ToastUtil.show(getActivity(), "钱包密码不能为空");
							return;
						}
						ResponseHandler<CheckMoneyPasswordResponse> responseHandler = new ResponseHandler<CheckMoneyPasswordResponse>() {

							@Override
							public void onStart() {
								showProgressDialog(getActivity(), "正在验证");
							}

							@Override
							public void onFinish(CheckMoneyPasswordResponse response, String error) {
								hideProgressDialog();
								if (ResponseUtils.checkResponseAndToastError(response, error)) {
									runnable.run();
								}
							}
						};

						CheckMoneyPasswordRequest request = new CheckMoneyPasswordRequest();
						request.setMoneyPassword(Encrypt.MD5(text));
						HttpPacketClient.postPacketAsynchronous(request, CheckMoneyPasswordResponse.class,
								responseHandler, true);
					}
				});
		dialog.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		dialog.show();
	}

	/**
	 * @param userInfo
	 */
	private void updateView(GetMyWalletInfoResponse response) {
		View listitem_balance = findViewById(R.id.listitem_balance);
		((TextView) listitem_balance.findViewById(R.id.textView_detail)).setText(String.format("%.2f",
				response.getBalance()));

		View listitem_bankCard = findViewById(R.id.listitem_bankCard);
		if (response.getBankCardCount() == 0) {
			((TextView) listitem_bankCard.findViewById(R.id.textView_title)).setText("添加银行卡");
			((TextView) listitem_bankCard.findViewById(R.id.textView_detail)).setText("0张");
		} else {
			((TextView) listitem_bankCard.findViewById(R.id.textView_title)).setText("管理银行卡");
			((TextView) listitem_bankCard.findViewById(R.id.textView_detail))
					.setText(response.getBankCardCount() + "张");
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	private void loadData() {
		ResponseHandler<GetMyWalletInfoResponse> responseHandler = new ResponseHandler<GetMyWalletInfoResponse>() {

			@Override
			public void onStart() {}

			@Override
			public void onFinish(GetMyWalletInfoResponse response, String error) {

				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					updateView(response);
				}
			}
		};

		GetMyWalletInfoRequest request = new GetMyWalletInfoRequest();
		HttpPacketClient.postPacketAsynchronous(request, GetMyWalletInfoResponse.class, responseHandler, true);
	}

}
