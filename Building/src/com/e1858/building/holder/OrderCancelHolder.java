package com.e1858.building.holder;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.utils.ToastUtil;
import com.e1858.building.R;
import com.e1858.building.bean.OrderInfo;
import com.e1858.building.entity.OrderEntity;
import com.e1858.building.httppackage.DeleteCanceledOrderRequest;
import com.e1858.building.httppackage.DeleteCanceledOrderResponse;
import com.e1858.building.httppackage.PostOrderInfoRequest;
import com.e1858.building.httppackage.PostOrderInfoResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.order.OrderDetailActivity;
import com.e1858.building.utils.AppUtil;
import com.e1858.building.utils.DbOpenUtils;
import com.e1858.building.utils.DialogUtil;
import com.e1858.building.utils.ResponseUtils;
import com.hg.android.ormlite.extra.OrmLiteIteratorAdapterExt;

public class OrderCancelHolder extends BaseViewHolder {
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
	private TextView		roborder_list_item_tv_sn;

	public OrderCancelHolder(Context context, View view) {
		super(context, view, R.layout.cancel_content_layout);
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
		roborder_list_item_tv_sn = (TextView) findViewById(R.id.roborder_list_item_tv_sn);
	}

	public void bindDate(final OrderInfo orderInfo, final OrmLiteIteratorAdapterExt adapter) {
		if (orderInfo.getServiceType() != null) {
			order_content_tv_service_type.setText(orderInfo.getServiceType() + "");
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
		order_content_reserve_buyer_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogUtil.dialogOpen(context, "温馨提示", "确定要删除订单吗？", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteOrder(orderInfo.getOrderID());
					}
				});
			}
		});
//		if (orderInfo.getOrderStatus() == Constant.OrderStatus.CANCELED) {
//			order_content_cancel_order_ll.setEnabled(false);
//		}
		order_content_cancel_order_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				robOrder(orderInfo.getOrderID());
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

	/**
	 * 抢单按钮
	 * 
	 * @param vvoid
	 */
	public void robOrder(final String orderID) {
		ResponseHandler<PostOrderInfoResponse> responseHandler = new ResponseHandler<PostOrderInfoResponse>() {
			@Override
			public void onStart() {
				showProgressDialog(getContext(), "正在处理中");
			}

			@Override
			public void onFinish(PostOrderInfoResponse response, String error) {
				hideProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					DbOpenUtils.deleteEntityByID(orderID, OrderEntity.class);
					ToastUtil.show(context, "恭喜您抢单成功");
				}
			}
		};
		PostOrderInfoRequest request = new PostOrderInfoRequest();
		request.setOrderID(orderID);
		HttpPacketClient.postPacketAsynchronous(request, PostOrderInfoResponse.class, responseHandler, true);
	}

	/**
	 * 删除订单
	 * 
	 * @param vvoid
	 */
	public void deleteOrder(final String orderID) {
		ResponseHandler<DeleteCanceledOrderResponse> responseHandler = new ResponseHandler<DeleteCanceledOrderResponse>() {
			@Override
			public void onStart() {
				showProgressDialog(getContext(), "正在处理中");
			}

			@Override
			public void onFinish(DeleteCanceledOrderResponse response, String error) {
				hideProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					DbOpenUtils.deleteEntityByID(orderID, OrderEntity.class);
					ToastUtil.show(context, "删除成功！");
				}
			}
		};
		DeleteCanceledOrderRequest request = new DeleteCanceledOrderRequest();
		request.setOrderID(orderID);
		HttpPacketClient.postPacketAsynchronous(request, DeleteCanceledOrderResponse.class, responseHandler, true);
	}

}
