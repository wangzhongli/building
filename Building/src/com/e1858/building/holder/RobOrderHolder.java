package com.e1858.building.holder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.utils.StringUtils;
import com.common.utils.ToastUtil;
import com.e1858.building.MainApplication;
import com.e1858.building.R;
import com.e1858.building.bean.RobOrder;
import com.e1858.building.entity.RobOrderEntity;
import com.e1858.building.httppackage.PostOrderInfoRequest;
import com.e1858.building.httppackage.PostOrderInfoResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.order.OrderInfoActivity;
import com.e1858.building.persondata.NotReserveActivity;
import com.e1858.building.utils.DbOpenUtils;

public class RobOrderHolder extends BaseViewHolder {

	private TextView		roborder_list_item_tv_servicetype;
	private TextView		roborder_list_item_tv_address;
	private TextView		roborder_list_item_tv_time;
	private RelativeLayout	roborder_list_item_arrow_rl;
	private LinearLayout	roborder_list_item_ll_rob;
	private TextView		roborder_list_item_tv_goods_type;

	public RobOrderHolder(Context context, View view) {
		super(context, view, R.layout.item_roborder_list);
	}

	@Override
	protected void initSubviews() {
		roborder_list_item_tv_servicetype = (TextView) findViewById(R.id.roborder_list_item_tv_servicetype);
		roborder_list_item_tv_address = (TextView) findViewById(R.id.roborder_list_item_tv_address);
		roborder_list_item_tv_time = (TextView) findViewById(R.id.roborder_list_item_tv_time);
		roborder_list_item_tv_goods_type = (TextView) findViewById(R.id.roborder_list_item_tv_goods_type);
		roborder_list_item_arrow_rl = (RelativeLayout) findViewById(R.id.roborder_list_item_arrow_rl);
		roborder_list_item_ll_rob = (LinearLayout) findViewById(R.id.roborder_list_item_ll_rob);
	}

	public void bindDate(final RobOrder robOrder, final Activity activity) {
		roborder_list_item_tv_servicetype.setText(robOrder.getServiceType());
		roborder_list_item_tv_address.setText(robOrder.getRobOrderAddress());
		roborder_list_item_tv_goods_type.setText(robOrder.getGoodsType());
		roborder_list_item_tv_time.setText(robOrder.getServiceTime());
		getView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, OrderInfoActivity.class);
				intent.putExtra(OrderInfoActivity.ROBORDERID, robOrder.getRobOrderID());
				context.startActivity(intent);
			}
		});
		roborder_list_item_ll_rob.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				robOrder(robOrder.getRobOrderID(), activity);
			}
		});
	}

	/**
	 * 抢单按钮
	 * 
	 * @param vvoid
	 */
	public void robOrder(final String orderID, Activity activity) {
		if (MainApplication.getInstance().verifyLoggedInAndGoToLogin(activity)
				&& MainApplication.getInstance().isPassAuth(activity)) {
			ResponseHandler<PostOrderInfoResponse> responseHandler = new ResponseHandler<PostOrderInfoResponse>() {
				@Override
				public void onStart() {
					showProgressDialog(getContext(), "请稍后...");
				}

				@Override
				public void onFinish(PostOrderInfoResponse response, String error) {
					hideProgressDialog();
					if (response != null && response.isSuccess() && StringUtils.isEmpty(response.getError())) {
						DbOpenUtils.deleteEntityByID(orderID, RobOrderEntity.class);
						ToastUtil.showAtCenterShort(context, "恭喜您，抢单成功！");
						Intent intent = new Intent(context, NotReserveActivity.class);
						context.startActivity(intent);
					} else {
						ToastUtil.showAtCenterShort(context, response.getError());
					}
				}
			};
			PostOrderInfoRequest request = new PostOrderInfoRequest();
			request.setOrderID(orderID);
			HttpPacketClient.postPacketAsynchronous(request, PostOrderInfoResponse.class, responseHandler, true);
		}
	}
}
