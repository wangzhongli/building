package com.e1858.building.holder;

import java.io.Serializable;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.e1858.building.Constant;
import com.e1858.building.R;
import com.e1858.building.bean.OrderInfo;
import com.e1858.building.order.OrderDetailActivity;
import com.e1858.building.upload.UploadPicActivity;
import com.e1858.building.utils.AppUtil;
import com.e1858.building.utils.DialogUtil;
import com.hg.android.ormlite.extra.OrmLiteIteratorAdapterExt;

public class NotAcceptHolder extends BaseViewHolder {
	private LinearLayout	content_layout;
	private LinearLayout	order_content_reserve_buyer_ll, order_content_sign_address;
	private TextView		order_content_tv_service_type;
	private TextView		order_content_tv_service_time;
	private TextView		order_content_tv_buyer_name;
	private TextView		order_content_tv_buyer_mobile;
	private TextView		order_content_tv_buyer_add;
	private TextView		order_content_tv_order_from;
	private TextView		order_content_tv_accept_time;
	private ImageView		iv_arrow;
	private TextView		not_accept_tv;
	private ImageView		not_accept_iv;
	private TextView		not_accept_sign_tv;
	private TextView		roborder_list_item_tv_sn;

	public NotAcceptHolder(Context context, View view) {
		super(context, view, R.layout.not_accept_content_layout);
	}

	@Override
	protected void initSubviews() {
		content_layout = (LinearLayout) findViewById(R.id.content_layout);
		order_content_reserve_buyer_ll = (LinearLayout) findViewById(R.id.order_content_reserve_buyer_ll);
		order_content_sign_address = (LinearLayout) findViewById(R.id.order_content_sign_address);
		order_content_tv_service_type = (TextView) findViewById(R.id.order_content_tv_service_type);
		order_content_tv_service_time = (TextView) findViewById(R.id.order_content_tv_service_time);
		order_content_tv_buyer_name = (TextView) findViewById(R.id.order_content_tv_buyer_name);
		order_content_tv_buyer_mobile = (TextView) findViewById(R.id.order_content_tv_buyer_mobile);
		order_content_tv_buyer_add = (TextView) findViewById(R.id.order_content_tv_buyer_add);
		order_content_tv_order_from = (TextView) findViewById(R.id.order_content_tv_order_from);
		order_content_tv_accept_time = (TextView) findViewById(R.id.order_content_tv_accept_time);
		not_accept_sign_tv = (TextView) findViewById(R.id.not_accept_sign_tv);
		not_accept_tv = (TextView) findViewById(R.id.not_accept_tv);
		not_accept_iv = (ImageView) findViewById(R.id.not_accept_iv);
		roborder_list_item_tv_sn = (TextView) findViewById(R.id.roborder_list_item_tv_sn);
	}

	public void bindDate(final OrderInfo orderInfo, final OrmLiteIteratorAdapterExt adapter) {
		if (orderInfo.getServiceType() != null) {
			order_content_tv_service_type.setText(orderInfo.getServiceType());
		}
		if (orderInfo.getServiceTime() != null) {
			order_content_tv_service_time.setText(orderInfo.getServiceTime());
		}
		if (orderInfo.getOrderTime() != null) {
			order_content_tv_accept_time.setText(orderInfo.getOrderTime());
		}
		if (orderInfo.getBuyerName() != null) {
			order_content_tv_buyer_name.setText(orderInfo.getBuyerName());
		}
		if (orderInfo.getBuyerMobile() != null) {
			order_content_tv_buyer_mobile.setText(orderInfo.getBuyerMobile());
		}
		if (orderInfo.getTakesAddress() != null) {
			order_content_tv_buyer_add.setText(orderInfo.getTakesAddress());
		}
		if (orderInfo.getOrigin() != null) {
			order_content_tv_order_from.setText(orderInfo.getOrigin());
		}
		if (orderInfo.getOrderSN() != null) {
			roborder_list_item_tv_sn.setText("订单编号：  " + orderInfo.getOrderSN());
		}
		if (orderInfo.isAccept()) {
			order_content_reserve_buyer_ll.setEnabled(true);
			not_accept_iv.setBackgroundResource(R.drawable.ensure_tihuo);
			not_accept_tv.setTextColor(context.getResources().getColor(R.color.bar_bottom_color));
			not_accept_sign_tv.setText("已签到");
		} else {
			not_accept_iv.setBackgroundResource(R.drawable.ensure_tihuo_gray);
			not_accept_tv.setTextColor(context.getResources().getColor(R.color.notes_textColor));
			order_content_reserve_buyer_ll.setEnabled(false);
			not_accept_sign_tv.setText("提货签到");
		}

		order_content_reserve_buyer_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, UploadPicActivity.class);
				int[] types = { 3, 4, 5 };
				intent.putExtra(UploadPicActivity.FLAG, Constant.UPDATEPICFLAG.FLAG_ACCEPT);
				intent.putExtra(UploadPicActivity.TYPES, (Serializable) types);
				intent.putExtra(UploadPicActivity.ORDER_ID, orderInfo.getOrderID());
				context.startActivity(intent);
			}
		});
		order_content_sign_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogUtil.dialogOpenSignAddress(context, "提货签到", new DialogUtil.OnSignAddFinishedListener() {

					@Override
					public void onFinished(String longitude, String latitude, String address) {

					}

				});
			}
		});
		order_content_tv_buyer_mobile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogUtil.dialogOpen(context, "呼叫确认", "您确定要拨打" + orderInfo.getBuyerMobile(),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								AppUtil.call(context, orderInfo.getBuyerMobile());
							}

						});
			}
		});
		content_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				OrderDetailActivity.action(context, orderInfo.getOrderID(), OrderDetailActivity.TYPE_ACTION_NORMAL);
			}
		});
	}
}
