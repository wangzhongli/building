package com.e1858.building.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.bean.BankCardBean;
import com.e1858.building.net.HttpDefine;
import com.e1858.building.utils.ImageCacheUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 日期: 2015年1月7日 下午5:05:41
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class BankCardDetailActivity extends BaseActivity {

	public static final String	IntentKey_BankCard	= "IntentKey_BankCard";
	private static final int	RequestCode_delete	= 111;
	BankCardBean				bankCard;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_bankcard_detail);
		if (getIntent() == null) {
			finish();
			return;
		}

		bankCard = (BankCardBean) getIntent().getSerializableExtra(IntentKey_BankCard);
		if (bankCard == null) {
			finish();
			return;
		}

		initView(bankCard);
	}

	/**
	 * @param bankCard
	 */
	private void initView(BankCardBean bean) {
		View listitem_bankcard = findViewById(R.id.listitem_bankCard);
		listitem_bankcard.findViewById(R.id.imageView_arrow).setVisibility(View.GONE);
		ImageView imageView = (ImageView) listitem_bankcard.findViewById(R.id.imageView);
		ImageLoader.getInstance().displayImage(bean.getCardUrl(), imageView, ImageCacheUtil.defaultOptions());
		TextView textView_title = (TextView) listitem_bankcard.findViewById(R.id.textView_title);
		TextView textView_detail = (TextView) listitem_bankcard.findViewById(R.id.textView_detail);
		textView_title.setText(bean.getBankName());
		String cardNumber = bean.getCardNumber();
		if (cardNumber != null && cardNumber.length() > 4) {
			cardNumber = cardNumber.substring(cardNumber.length() - 4);
		}
		textView_detail.setText("尾号" + cardNumber + " " + HttpDefine.getCardTypeName(bean.getCardType()));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == RequestCode_delete) {
				BankCardBean bankCard = (BankCardBean) data
						.getSerializableExtra(BankCardDeleteActivity.IntentKey_BankCard);
				if (bankCard.getID().equals(this.bankCard.getID())) {
					finish();
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.bankcarddetail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_delete) {
			onDeleteBankCard();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 
	 */
	private void onDeleteBankCard() {
		Intent intent = new Intent(getActivity(), BankCardDeleteActivity.class);
		intent.putExtra(BankCardDeleteActivity.IntentKey_BankCard, bankCard);
		startActivityForResult(intent, RequestCode_delete);
	}

}
