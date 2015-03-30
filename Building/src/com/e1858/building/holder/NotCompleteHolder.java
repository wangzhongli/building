package com.e1858.building.holder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import com.e1858.building.Constant;
import com.e1858.building.R;
import com.e1858.building.bean.OrderInfo;
import com.e1858.building.entity.OrderEntity;
import com.e1858.building.httppackage.ServiceDoneReponse;
import com.e1858.building.httppackage.ServiceDoneRequest;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.order.OrderDetailActivity;
import com.e1858.building.persondata.NotCompleteActivity;
import com.e1858.building.persondata.OrderCompleteActivity;
import com.e1858.building.upload.UploadPicActivity;
import com.e1858.building.utils.AppUtil;
import com.e1858.building.utils.DbOpenUtils;
import com.e1858.building.utils.DialogUtil;
import com.e1858.building.utils.RequestUtils;
import com.e1858.building.utils.RequestUtils.OnFinishListener;
import com.e1858.building.utils.ResponseUtils;
import com.hg.android.ormlite.extra.OrmLiteIteratorAdapterExt;

public class NotCompleteHolder extends BaseViewHolder {
	private LinearLayout	content_layout;
	private LinearLayout	order_content_reserve_buyer_ll, order_content_sign_address, order_content_check_accept;
	private TextView		order_content_tv_service_type;
	private TextView		order_content_tv_service_time;
	private TextView		order_content_tv_buyer_name;
	private TextView		order_content_tv_buyer_mobile;
	private TextView		order_content_tv_buyer_add;
	private TextView		order_content_tv_order_from;
	private TextView		order_content_tv_accept_time;
	private ImageView		not_complete_ensure_iv;
	private ImageView		not_complete_accept_iv;
	private TextView		not_complete_accept_tv;
	private TextView		not_complete_ensure_tv;
	private TextView		not_complete_sign_tv;
	private ImageView		not_complete_sign_iv;
	private List<String>	isarrives		= new ArrayList<String>();
	private List<String>	isEnsureGoods	= new ArrayList<String>();												//已经验货拍照的
	private TextView		roborder_list_item_tv_sn;

	public NotCompleteHolder(Context context, View view, int layoutID) {
		super(context, view, layoutID);
	}

	@Override
	protected void initSubviews() {
		content_layout = (LinearLayout) findViewById(R.id.content_layout);
		content_layout.setPressed(false);
		order_content_reserve_buyer_ll = (LinearLayout) findViewById(R.id.order_content_reserve_buyer_ll);
		order_content_sign_address = (LinearLayout) findViewById(R.id.order_content_sign_address);
		order_content_check_accept = (LinearLayout) findViewById(R.id.order_content_check_accept);
		order_content_tv_service_type = (TextView) findViewById(R.id.order_content_tv_service_type);
		order_content_tv_service_time = (TextView) findViewById(R.id.order_content_tv_service_time);
		order_content_tv_buyer_name = (TextView) findViewById(R.id.order_content_tv_buyer_name);
		order_content_tv_buyer_mobile = (TextView) findViewById(R.id.order_content_tv_buyer_mobile);
		order_content_tv_buyer_add = (TextView) findViewById(R.id.order_content_tv_buyer_add);
		order_content_tv_order_from = (TextView) findViewById(R.id.order_content_tv_order_from);
		order_content_tv_accept_time = (TextView) findViewById(R.id.order_content_tv_accept_time);
		not_complete_ensure_iv = (ImageView) findViewById(R.id.not_complete_ensure_iv);
		not_complete_accept_iv = (ImageView) findViewById(R.id.not_complete_accept_iv);
		not_complete_sign_iv = (ImageView) findViewById(R.id.not_complete_sign_iv);
		not_complete_accept_tv = (TextView) findViewById(R.id.not_complete_accept_tv);
		not_complete_ensure_tv = (TextView) findViewById(R.id.not_complete_ensure_tv);
		not_complete_sign_tv = (TextView) findViewById(R.id.not_complete_sign_tv);
		roborder_list_item_tv_sn = (TextView) findViewById(R.id.roborder_list_item_tv_sn);
	}

	public void bindDate(final OrderInfo orderInfo, final OrmLiteIteratorAdapterExt adapter) {
		if (orderInfo.isArrive) {
			isarrives.add(orderInfo.getOrderID());
		}
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
//			order_content_tv_buyer_add.setText(orderInfo.getTakesAddress());
			order_content_tv_buyer_add.setText(orderInfo.getOrderAddress() + "");
		}
		if (orderInfo.getOrigin() != null) {
			order_content_tv_order_from.setText(orderInfo.getOrigin());
		}
		if (orderInfo.getOrderSN() != null) {
			roborder_list_item_tv_sn.setText("订单编号：  " + orderInfo.getOrderSN());
		}
		if (isarrives.contains(orderInfo.getOrderID())) {//上门签到
//			order_content_reserve_buyer_ll.setEnabled(true);
//			order_content_check_accept.setEnabled(true);
//			not_complete_ensure_iv.setBackgroundResource(R.drawable.complete_take_pic);
//			not_complete_accept_iv.setBackgroundResource(R.drawable.ensure_tihuo);
//			not_complete_accept_tv.setTextColor(context.getResources().getColor(R.color.bar_bottom_color));
//			not_complete_ensure_tv.setTextColor(context.getResources().getColor(R.color.bar_bottom_color));

			order_content_sign_address.setEnabled(false);
			not_complete_sign_tv.setTextColor(context.getResources().getColor(R.color.notes_textColor));
			not_complete_sign_iv.setBackgroundResource(R.drawable.sign_address_icon_gray);
			not_complete_sign_tv.setText("已签到");

			if (orderInfo.isConfirmGoods()) {//商品验货
				order_content_check_accept.setEnabled(false);
				not_complete_accept_iv.setBackgroundResource(R.drawable.ensure_tihuo_gray);
				not_complete_accept_tv.setTextColor(context.getResources().getColor(R.color.notes_textColor));
			} else {
				order_content_check_accept.setEnabled(true);
				not_complete_accept_iv.setBackgroundResource(R.drawable.ensure_tihuo);
				not_complete_accept_tv.setTextColor(context.getResources().getColor(R.color.bar_bottom_color));
			}
			if (orderInfo.isUploadPicture()) {//上传过照片，
				order_content_reserve_buyer_ll.setEnabled(true);
				not_complete_ensure_tv.setText("确认完成");
				not_complete_ensure_tv.setTextColor(context.getResources().getColor(R.color.bar_bottom_color));
				not_complete_ensure_iv.setBackgroundResource(R.drawable.service_complete_icon);
			} else {
				order_content_reserve_buyer_ll.setEnabled(true);
				not_complete_ensure_tv.setText("上传照片");
				not_complete_ensure_tv.setTextColor(context.getResources().getColor(R.color.bar_bottom_color));
				not_complete_ensure_iv.setBackgroundResource(R.drawable.complete_take_pic);
			}
		} else {
			order_content_reserve_buyer_ll.setEnabled(false);
			order_content_check_accept.setEnabled(false);
			not_complete_ensure_iv.setBackgroundResource(R.drawable.complete_take_pic_gray);
			not_complete_accept_iv.setBackgroundResource(R.drawable.ensure_tihuo_gray);
			not_complete_accept_tv.setTextColor(context.getResources().getColor(R.color.notes_textColor));
			not_complete_ensure_tv.setTextColor(context.getResources().getColor(R.color.notes_textColor));

			order_content_sign_address.setEnabled(true);
			not_complete_sign_tv.setTextColor(context.getResources().getColor(R.color.bar_bottom_color));
			not_complete_sign_iv.setBackgroundResource(R.drawable.sign_address_icon);
			not_complete_sign_tv.setText("上门签到");
		}

		order_content_reserve_buyer_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (orderInfo.isUploadPicture()) {//上传过照片，
					DialogUtil.showWithEdit(context, "请输入买家给你提供的服务码", "请输入服务确认码",
							new DialogUtil.OnEditFinishedListener() {

								@Override
								public void onFinished(String text) {
									if (!StringUtils.isEmpty(text))
										serviceDone(orderInfo.getOrderID(), text);
								}
							});
				} else {
					Intent intent = new Intent(context, UploadPicActivity.class);
					intent.putExtra(UploadPicActivity.ORDER_ID, orderInfo.getOrderID());
					int[] types = { 6 };
					intent.putExtra(UploadPicActivity.TYPES, (Serializable) types);
					intent.putExtra(UploadPicActivity.FLAG, Constant.UPDATEPICFLAG.FLAG_COMPLETE);
					intent.putExtra(UploadPicActivity.ACTIVITY_FROM, "OrderCompleteActivity");
					context.startActivity(intent);
				}

			}
		});
		order_content_sign_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogUtil.dialogOpenSignAddress(context, "上门签到", new DialogUtil.OnSignAddFinishedListener() {

					@Override
					public void onFinished(String longitude, String latitude, String address) {
						if (StringUtils.isEmpty(address) && StringUtils.isEmpty(longitude)
								&& StringUtils.isEmpty(longitude)) {
							ToastUtil.show(context, "获取不到定位地址，请稍后再试！");
						} else {
							RequestUtils.signAddress(context, orderInfo.getOrderID(), longitude, latitude, address,
									Constant.SIGNADDRESSTYPE.ISARRIVE, new OnFinishListener() {

										@Override
										public void onSuccussFinish() {
											isarrives.add(orderInfo.getOrderID());
											adapter.notifyDataSetChanged();
										}
									});
						}
					}
				});
			}

		});
		order_content_check_accept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, UploadPicActivity.class);
				intent.putExtra(UploadPicActivity.ORDER_ID, orderInfo.getOrderID());
				int[] types = { 1, 2 };
				intent.putExtra(UploadPicActivity.TYPES, (Serializable) types);
				intent.putExtra(UploadPicActivity.FLAG, Constant.UPDATEPICFLAG.FLAG_NOT_COMPLETE);
				intent.putExtra(UploadPicActivity.ACTIVITY_FROM, "OrderCompleteActivity");
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
				OrderDetailActivity.action(context, orderInfo.getOrderID(), OrderDetailActivity.TYPE_ACTION_SPECIAL);
			}
		});
	}

	/**
	 * 服务完成接口
	 * 
	 * @param orderIDvoid
	 */
	public void serviceDone(final String orderID, String code) {
		ResponseHandler<ServiceDoneReponse> responseHandler = new ResponseHandler<ServiceDoneReponse>() {
			@Override
			public void onStart() {
				showProgressDialog(context, "请稍后...");
			}

			@Override
			public void onFinish(ServiceDoneReponse response, String error) {
				hideProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					ToastUtil.show(context, "恭喜您，订单完成！");
					DbOpenUtils.deleteEntityByID(orderID, OrderEntity.class);
					//关闭自己打开下一级
					AppManager.getAppManager().finishActivity(NotCompleteActivity.class);
					Intent intent = new Intent(context, OrderCompleteActivity.class);
					context.startActivity(intent);
				}
			}
		};
		ServiceDoneRequest request = new ServiceDoneRequest();
		request.setOrderID(orderID);
		request.setServiceCode(code);
		HttpPacketClient.postPacketAsynchronous(request, ServiceDoneReponse.class, responseHandler, true);
	}

}
