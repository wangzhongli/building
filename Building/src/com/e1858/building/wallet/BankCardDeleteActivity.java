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
import com.e1858.building.bean.BankCardBean;
import com.e1858.building.entity.BankCardEntity;
import com.e1858.building.httppackage.DeleteBankCardRequest;
import com.e1858.building.httppackage.DeleteBankCardResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.utils.Encrypt;
import com.e1858.building.utils.ResponseUtils;
import com.hg.android.entitycache.EntityCacheHelper;

/**
 * 日期: 2015年1月7日 下午5:05:41
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class BankCardDeleteActivity extends BaseActivity {

	public static final String	IntentKey_BankCard	= "IntentKey_BankCard";
	BankCardBean				bankCard;
	EditText					editText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_bankcard_delete);
		if (getIntent() == null) {
			finish();
			return;
		}

		bankCard = (BankCardBean) getIntent().getSerializableExtra(IntentKey_BankCard);
		if (bankCard == null) {
			finish();
			return;
		}

		initView();
	}

	/**
	 * @param bankCard
	 */
	private void initView() {
		View listitem_password = findViewById(R.id.listitem_password);
		TextView textView_title = (TextView) listitem_password.findViewById(R.id.textView);
		editText = (EditText) listitem_password.findViewById(R.id.editText);
		editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		textView_title.setText("钱包密码");

		findViewById(R.id.button_submit).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onDeleteBankCard();
			}
		});
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

	/**
	 * 
	 */
	private void onDeleteBankCard() {
		String password = editText.getText().toString();
		if (TextUtils.isEmpty(password)) {
			ToastUtil.show(getActivity(), "请填写钱包密码");
			return;
		}

		ResponseHandler<DeleteBankCardResponse> responseHandler = new ResponseHandler<DeleteBankCardResponse>() {
			@Override
			public void onStart() {
				showProgressDialog(getActivity(), "正在删除");
			}

			@Override
			public void onFinish(DeleteBankCardResponse response, String error) {
				hideProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					EntityCacheHelper.sharedInstance().deleteEntityByID(bankCard.getID(), BankCardEntity.class);
					ToastUtil.show(getActivity(), "删除成功");
					Intent intent = new Intent();
					intent.putExtra(IntentKey_BankCard, bankCard);
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		};
		DeleteBankCardRequest request = new DeleteBankCardRequest();
		request.setMoneyPassword(Encrypt.MD5(password));
		request.setID(bankCard.getID());
		HttpPacketClient.postPacketAsynchronous(request, DeleteBankCardResponse.class, responseHandler, true);
	}

}
