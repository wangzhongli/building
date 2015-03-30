package com.e1858.building.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.common.utils.ToastUtil;
import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.bean.BankBean;
import com.e1858.building.bean.BankCardBean;
import com.e1858.building.entity.BankCardEntity;
import com.e1858.building.httppackage.AddBankCardRequest;
import com.e1858.building.httppackage.AddBankCardResponse;
import com.e1858.building.net.HttpDefine;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.utils.ResponseUtils;
import com.hg.android.entitycache.EntityCacheHelper;

/**
 * 日期: 2015年1月7日 下午5:05:41
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class BankCardAddActivity extends BaseActivity {

	protected static final int	RequestCode_Bank	= 123;

	TextView					editText_bankName;
	EditText					editText_cardOwner;
	EditText					editText_cardNumber;

	BankBean					selectedBank;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bankcard_add);
		initView();
	}

	/**
 */
	private void initView() {

		View listitem_cardType = findViewById(R.id.listitem_cardType);
		{
			TextView editText = (TextView) listitem_cardType.findViewById(R.id.textView2);
			((TextView) listitem_cardType.findViewById(R.id.textView)).setText("银行卡类型");
			//editText.setEnabled(false);
			//editText.setKeyListener(null);
			//editText.setFocusable(false);
			listitem_cardType.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					startActivityForResult(new Intent(getActivity(), BankActivity.class), RequestCode_Bank);
				}
			});
			editText_bankName = editText;
		}
		View listitem_cardOwner = findViewById(R.id.listitem_cardOwner);
		((TextView) listitem_cardOwner.findViewById(R.id.textView)).setText("持卡人姓名");
		editText_cardOwner = (EditText) listitem_cardOwner.findViewById(R.id.editText);

		View listitem_cardNumber = findViewById(R.id.listitem_cardNumber);
		((TextView) listitem_cardNumber.findViewById(R.id.textView)).setText("卡号");
		editText_cardNumber = (EditText) listitem_cardNumber.findViewById(R.id.editText);
		editText_cardNumber.setInputType(InputType.TYPE_CLASS_NUMBER);

		findViewById(R.id.button_submit).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickSubmit();
			}
		});

	}

	/**
	 * 
	 */
	protected void onClickSubmit() {
//		CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);
//		if (!checkBox.isChecked()) {
//			ToastUtil.show(getActivity(), "请同意注册许可");
//			return;
//		}
		if (selectedBank == null) {
			ToastUtil.show(getActivity(), "请选择银行");
			return;
		}

		String owner = editText_cardOwner.getText().toString();
		if (TextUtils.isEmpty(owner)) {
			ToastUtil.show(getActivity(), "请输入持卡人姓名");
			return;
		}

		String number = editText_cardNumber.getText().toString();
		if (TextUtils.isEmpty(number)) {
			ToastUtil.show(getActivity(), "请输入卡号");
			return;
		}
		if (number.length() < 5) {
			ToastUtil.show(getActivity(), "请输入正确的卡号");
			return;
		}

		final BankCardBean bankCard = new BankCardBean();
		bankCard.setBankName(selectedBank.getBankName());
		bankCard.setCardType(selectedBank.getCardType());
		bankCard.setCardNumber(number);
		bankCard.setCardOwner(owner);

		ResponseHandler<AddBankCardResponse> responseHandler = new ResponseHandler<AddBankCardResponse>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(AddBankCardResponse response, String error) {
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					EntityCacheHelper.sharedInstance().saveCacheEntity(BankCardEntity.class, bankCard);
					finish();
				}
			}
		};
		AddBankCardRequest request = new AddBankCardRequest();
		request.setBankCard(bankCard);
		HttpPacketClient.postPacketAsynchronous(request, AddBankCardResponse.class, responseHandler, true);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		if (requestCode == RequestCode_Bank) {
			selectedBank = (BankBean) data.getSerializableExtra(BankActivity.IntentKey_Bank);
			editText_bankName.setText(selectedBank.getBankName() + " "
					+ HttpDefine.getCardTypeName(selectedBank.getCardType()));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

}
