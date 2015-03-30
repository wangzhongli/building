package com.e1858.building.wallet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.bean.BankCardBean;
import com.e1858.building.entity.BankCardEntity;
import com.e1858.building.entity.EntityOpenHelper;
import com.e1858.building.httppackage.GetBankCardsRequest;
import com.e1858.building.httppackage.GetBankCardsResponse;
import com.e1858.building.net.HttpDefine;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.utils.ImageCacheUtil;
import com.e1858.building.utils.ResponseUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hg.android.entitycache.EntityCacheHelper;
import com.hg.android.ormlite.extra.OrmLiteIteratorAdapterExt;
import com.j256.ormlite.stmt.QueryBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 日期: 2015年1月7日 下午4:42:07
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class BankCardActivity extends BaseActivity {

	MyAdapter				adapter;
	PullToRefreshListView	listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.pulltorefresh_listview);
		initViews();
		loadDatas();
	}

	@Override
	public void onDestroy() {
		if (adapter != null) {
			adapter.unload();
		}
		super.onDestroy();
	}

	void initViews() {

		listView = (PullToRefreshListView) findViewById(R.id.pf_listView);
		listView.setBackgroundColor(getResources().getColor(R.color.listview_bg_dark));
		listView.setMode(Mode.PULL_FROM_START);
		listView.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);
		listView.setAdapter(adapter = new MyAdapter(this));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				BankCardEntity entity = (BankCardEntity) parent.getAdapter().getItem(position);
				Intent intent = new Intent(getActivity(), BankCardDetailActivity.class);
				intent.putExtra(BankCardDetailActivity.IntentKey_BankCard, entity.getBean());
				startActivity(intent);
			}
		});
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				loadDatas();
			}
		});

		adapter.load(getSupportLoaderManager(), EntityOpenHelper.class, BankCardEntity.class);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	protected void loadDatas() {

		ResponseHandler<GetBankCardsResponse> responseHandler = new ResponseHandler<GetBankCardsResponse>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(GetBankCardsResponse response, String error) {
				listView.onRefreshComplete();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					EntityCacheHelper.sharedInstance().saveCacheEntities(BankCardEntity.class, response.getBankCards(),
							0);
				}
			}
		};

		GetBankCardsRequest request = new GetBankCardsRequest();
		HttpPacketClient.postPacketAsynchronous(request, GetBankCardsResponse.class, responseHandler, true);
	}

	////////
	private class MyAdapter extends OrmLiteIteratorAdapterExt<BankCardEntity> {

		public MyAdapter(Context context) {
			super(context);
		}

		@Override
		public View newView(Context context, BankCardEntity entity, ViewGroup parent) {
			View view = View.inflate(context, R.layout.listitem_bankcard, null);
			ViewHolder holder = new ViewHolder();
			holder.textView_title = (TextView) view.findViewById(R.id.textView_title);
			holder.textView_detail = (TextView) view.findViewById(R.id.textView_detail);
			holder.imageView = (ImageView) view.findViewById(R.id.imageView);
			view.setTag(holder);
			return view;
		}

		@Override
		public void bindView(View view, Context context, BankCardEntity entity, int position) {
			ViewHolder holder = (ViewHolder) view.getTag();
			BankCardBean bean = entity.getBean();
			holder.textView_title.setText(bean.getBankName());
			String cardNumber = bean.getCardNumber();
			if (cardNumber != null && cardNumber.length() > 4) {
				cardNumber = cardNumber.substring(cardNumber.length() - 4);
			}
			holder.textView_detail.setText("尾号" + cardNumber + " " + HttpDefine.getCardTypeName(bean.getCardType()));
			ImageLoader.getInstance().displayImage(bean.getCardUrl(), holder.imageView, ImageCacheUtil.emptyOptions());
		}

		@Override
		public void buildQueryBuilder(QueryBuilder<BankCardEntity, ?> queryBuilder) {
			queryBuilder.orderBy("sortIndex", true);
		}
	}

	class ViewHolder {
		TextView	textView_title;
		TextView	textView_detail;
		ImageView	imageView;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.bankcard, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_new) {
			startActivity(new Intent(getActivity(), BankCardAddActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}
