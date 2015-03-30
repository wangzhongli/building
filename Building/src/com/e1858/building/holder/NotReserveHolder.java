package com.e1858.building.holder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.utils.StringUtils;
import com.common.utils.ToastUtil;
import com.e1858.building.AppManager;
import com.e1858.building.R;
import com.e1858.building.bean.OrderInfo;
import com.e1858.building.entity.OrderEntity;
import com.e1858.building.httppackage.ConfirmReserveRequest;
import com.e1858.building.httppackage.ConfirmReserveResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.order.OrderDetailActivity;
import com.e1858.building.persondata.NotCompleteActivity;
import com.e1858.building.persondata.NotReserveActivity;
import com.e1858.building.persondata.ReserveActivity;
import com.e1858.building.utils.AppUtil;
import com.e1858.building.utils.DbOpenUtils;
import com.e1858.building.utils.DialogUtil;
import com.e1858.building.utils.ResponseUtils;
import com.hg.android.ormlite.extra.OrmLiteIteratorAdapterExt;

public class NotReserveHolder extends BaseViewHolder {
	private LinearLayout	content_layout;
	private LinearLayout	order_content_reserve_buyer_ll;
	private LinearLayout	order_content_reserve_modify_ll;
//	private LinearLayout	order_content_cancel_order_ll;
	private TextView		order_content_tv_service_type;
	private TextView		order_content_tv_service_time;
	private TextView		order_content_tv_buyer_name;
	private TextView		order_content_tv_buyer_mobile;
	private TextView		order_content_tv_buyer_add;
	private TextView		order_content_tv_order_from;
	private TextView		order_content_tv_accept_time;
	private ImageView		iv;
	private TextView		roborder_list_item_tv_sn;
	private TextView		not_reserve_en_tv;
	private ImageView		not_reserve_en_iv;

	public NotReserveHolder(Context context, View view) {
		super(context, view, R.layout.content_layout);
	}

	@Override
	protected void initSubviews() {
		content_layout = (LinearLayout) findViewById(R.id.content_layout);
		order_content_reserve_buyer_ll = (LinearLayout) findViewById(R.id.order_content_reserve_buyer_ll);
//		order_content_cancel_order_ll = (LinearLayout) findViewById(R.id.order_content_cancel_order_ll);
		order_content_reserve_modify_ll = (LinearLayout) findViewById(R.id.order_content_reserve_modify_ll);
		order_content_tv_service_type = (TextView) findViewById(R.id.order_content_tv_service_type);
		order_content_tv_service_time = (TextView) findViewById(R.id.order_content_tv_service_time);
		order_content_tv_buyer_name = (TextView) findViewById(R.id.order_content_tv_buyer_name);
		order_content_tv_buyer_mobile = (TextView) findViewById(R.id.order_content_tv_buyer_mobile);
		order_content_tv_buyer_add = (TextView) findViewById(R.id.order_content_tv_buyer_add);
		order_content_tv_order_from = (TextView) findViewById(R.id.order_content_tv_order_from);
		order_content_tv_accept_time = (TextView) findViewById(R.id.order_content_tv_accept_time);
		roborder_list_item_tv_sn = (TextView) findViewById(R.id.roborder_list_item_tv_sn);
		not_reserve_en_tv = (TextView) findViewById(R.id.not_reserve_en_tv);
		not_reserve_en_iv = (ImageView) findViewById(R.id.not_reserve_en_iv);

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
		}
		if (orderInfo.getOrigin() != null) {
			order_content_tv_order_from.setText(orderInfo.getOrigin());
		}
		if (orderInfo.getOrderSN() != null) {
			roborder_list_item_tv_sn.setText("订单编号：  " + orderInfo.getOrderSN());
		}
		if (StringUtils.isEmpty(orderInfo.getServiceTime())) {
			order_content_reserve_buyer_ll.setEnabled(false);
			not_reserve_en_iv.setBackgroundResource(R.drawable.reserve_ensure_icon_gray);
			not_reserve_en_tv.setTextColor(context.getResources().getColor(R.color.notes_textColor));
		} else {
			order_content_reserve_buyer_ll.setEnabled(true);
			not_reserve_en_iv.setBackgroundResource(R.drawable.reserve_ensure_icon);
			not_reserve_en_tv.setTextColor(context.getResources().getColor(R.color.bar_bottom_color));
		}
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
		order_content_reserve_buyer_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogUtil.dialogOpen(context, "预约确认", "确定和客户进行预约了吗", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						confirmReserve(orderInfo.getOrderID());
					}

				});
			}
		});
//		order_content_cancel_order_ll.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				DialogUtil.dialogOpen(context, "取消订单", "您确定要取消此订单？", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						cancelOrder(context, orderInfo.getOrderID());
//					}
//				});
//			}
//		});
		order_content_reserve_modify_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ReserveActivity.class);
				intent.putExtra(ReserveActivity.ORDERINFO, orderInfo);
				context.startActivity(intent);
			}
		});
		content_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				OrderDetailActivity.action(context, orderInfo.getOrderID(), OrderDetailActivity.TYPE_ACTION_NORMAL);
			}
		});
	}

//	public void cancelOrder(final Context context, final String orderID) {
//		ResponseHandler<CancelOrderResponse> responseHandler = new ResponseHandler<CancelOrderResponse>() {
//			@Override
//			public void onStart() {
//				showProgressDialog(context, "取消中，请稍后...");
//			}
//
//			@Override
//			public void onFinish(CancelOrderResponse response, String error) {
//				hideProgressDialog();
//				if (ResponseUtils.checkResponseAndToastError(response, error)) {
//					ToastUtil.show(context, "取消订单成功");
//					DbOpenUtils.deleteEntityByID(orderID, OrderEntity.class);
//				}
//			}
//		};
//		CancelOrderRequest request = new CancelOrderRequest();
//		request.setOrderID(orderID);
//		HttpPacketClient.postPacketAsynchronous(request, CancelOrderResponse.class, responseHandler, true);
//	}

	private void confirmReserve(final String orderID) {
		ResponseHandler<ConfirmReserveResponse> responseHandler = new ResponseHandler<ConfirmReserveResponse>() {
			@Override
			public void onStart() {
				showProgressDialog(context, "预约确认中，请稍后...");
			}

			@Override
			public void onFinish(ConfirmReserveResponse response, String error) {
				hideProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					ToastUtil.show(context, "预约订单成功");
					DbOpenUtils.deleteEntityByID(orderID, OrderEntity.class);
					AppManager.getAppManager().finishActivity(NotReserveActivity.class);
					Intent intent = new Intent(context, NotCompleteActivity.class);
					context.startActivity(intent);
				}
			}
		};
		ConfirmReserveRequest request = new ConfirmReserveRequest();
		request.setOrderID(orderID);
		request.setReserveTime(null);
		request.setRemark(null);
//		request.setRemark(StringUtils.isEmpty(reserve_et.getText().toString()) ? null : reserve_et.getText().toString());
		HttpPacketClient.postPacketAsynchronous(request, ConfirmReserveResponse.class, responseHandler, true);
	}
}
