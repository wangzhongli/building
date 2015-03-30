package com.e1858.building.holder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.e1858.building.R;
import com.e1858.building.bean.OrderInfo;
import com.e1858.building.order.OrderDetailActivity;
import com.e1858.building.persondata.CommentActivity;
import com.e1858.building.persondata.GetPhotoActivity;
import com.e1858.building.utils.AppUtil;
import com.e1858.building.utils.DialogUtil;
import com.hg.android.ormlite.extra.OrmLiteIteratorAdapterExt;

public class OrderCompleteHolder extends BaseViewHolder {
	private LinearLayout	content_layout;
	private LinearLayout	order_content_reserve_buyer_ll;
	private LinearLayout	order_content_cancel_order_ll;
	private TextView		order_content_tv_service_type;
	private TextView		order_content_tv_service_time;
	private TextView		order_content_tv_buyer_name;
	private TextView		order_content_tv_buyer_mobile;
	private TextView		order_content_tv_buyer_add;
	private TextView		order_content_tv_order_from;
	private TextView		order_content_tv_accept_time;
	private TextView		order_content_tv_photo;
	private ImageView		order_content_iv_photo;
	private TextView		roborder_list_item_tv_sn;

	public OrderCompleteHolder(Context context, View view, int layoutID) {
		super(context, view, layoutID);
	}

	@Override
	protected void initSubviews() {
		content_layout = (LinearLayout) findViewById(R.id.content_layout);
		order_content_reserve_buyer_ll = (LinearLayout) findViewById(R.id.order_content_reserve_buyer_ll);
		order_content_cancel_order_ll = (LinearLayout) findViewById(R.id.order_content_cancel_order_ll);
		order_content_tv_service_type = (TextView) findViewById(R.id.order_content_tv_service_type);
		order_content_tv_service_time = (TextView) findViewById(R.id.order_content_tv_service_time);
		order_content_tv_buyer_name = (TextView) findViewById(R.id.order_content_tv_buyer_name);
		order_content_tv_buyer_mobile = (TextView) findViewById(R.id.order_content_tv_buyer_mobile);
		order_content_tv_buyer_add = (TextView) findViewById(R.id.order_content_tv_buyer_add);
		order_content_tv_order_from = (TextView) findViewById(R.id.order_content_tv_order_from);
		order_content_tv_accept_time = (TextView) findViewById(R.id.order_content_tv_accept_time);
		order_content_tv_photo = (TextView) findViewById(R.id.order_content_tv_photo);
		order_content_iv_photo = (ImageView) findViewById(R.id.order_content_iv_photo);
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
		if (orderInfo.getOrderAddress() != null) {
			order_content_tv_buyer_add.setText(orderInfo.getOrderAddress() + "");
//			order_content_tv_buyer_add.setText(orderInfo.getTakesAddress());
		}
		if (orderInfo.getOrigin() != null) {
			order_content_tv_order_from.setText(orderInfo.getOrigin());
		}
		if (orderInfo.getOrderSN() != null) {
			roborder_list_item_tv_sn.setText("订单编号：  " + orderInfo.getOrderSN());
		}
//		if (orderInfo.isUploadPicture()) {
//			order_content_tv_photo.setText("查看照片");
//			order_content_iv_photo.setBackgroundResource(R.drawable.complete_picture);
//		} else {
//			order_content_tv_photo.setText("拍照");
//			order_content_iv_photo.setBackgroundResource(R.drawable.complete_take_pic);
//		}

		order_content_reserve_buyer_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				if (!orderInfo.isUploadPicture()) {
//					Intent intent = new Intent(context, UploadPicActivity.class);
//					intent.putExtra(UploadPicActivity.ACTIVITY_FROM, "OrderCompleteActivity");
//					intent.putExtra(UploadPicActivity.ORDER_ID, orderInfo.getOrderID());
//					int[] types = { 1, 2 };
//					intent.putExtra(UploadPicActivity.TYPES, (Serializable) types);
//					intent.putExtra(UploadPicActivity.FLAG, Constant.UPDATEPICFLAG.FLAG_COMPLETE);
//					context.startActivity(intent);
//				} else {
//					Intent intent = new Intent(context, GetPhotoActivity.class);
//					intent.putExtra(GetPhotoActivity.ORDERID, orderInfo.getOrderID());
//					context.startActivity(intent);
//				}
				Intent intent = new Intent(context, GetPhotoActivity.class);
				intent.putExtra(GetPhotoActivity.ORDERID, orderInfo.getOrderID());
				context.startActivity(intent);
			}
		});
		order_content_cancel_order_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, CommentActivity.class);
				intent.putExtra(CommentActivity.ORDERID, orderInfo.getOrderID());
				context.startActivity(intent);
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
				OrderDetailActivity.action(context, orderInfo.getOrderID(), OrderDetailActivity.TYPE_ACTION_NO_CANCEL);
			}
		});
	}
}
