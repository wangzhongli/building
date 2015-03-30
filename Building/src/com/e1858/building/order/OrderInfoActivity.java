package com.e1858.building.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.common.utils.StringUtils;
import com.common.utils.ToastUtil;
import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.bean.OrderInfo;
import com.e1858.building.entity.RobOrderEntity;
import com.e1858.building.httppackage.GetOrderInfoRequest;
import com.e1858.building.httppackage.GetOrderInfoResponse;
import com.e1858.building.httppackage.PostOrderInfoRequest;
import com.e1858.building.httppackage.PostOrderInfoResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.persondata.NotReserveActivity;
import com.e1858.building.utils.DbOpenUtils;
import com.e1858.building.utils.ResponseUtils;

/**
 * 功能：抢单详细信息
 * 日期: 2015-3-20 上午10:28:49
 * 作者: wangzhongli
 */
public class OrderInfoActivity extends BaseActivity {
	private TextView			roborder_list_item_tv_servicetype;
	private TextView			roborder_list_item_tv_sn;
	private TextView			roborder_list_item_tv_address;
	private TextView			roborder_list_item_tv_time;
	private TextView			roborder_item_tv_name;
	private TextView			roborder_item_tv_mobile;
	private TextView			roborder_item_tv_address;

//	private TextView			roborder_item_tv_logistics;
//	private TextView			roborder_item_tv_logistics_num;
//	private TextView			roborder_item_tv_tihuo;
	OrderInfo					orderInfo;
	public static final String	ROBORDERID	= "RobOrderID";
	private String				orderId;
	private ListView			order_info_goods_list;
	private TextView			activity_orderinfo_empty_tv;
	private TextView			order_info_tv_price;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_info);
		if (null != getIntent()) {
			orderId = getIntent().getStringExtra(ROBORDERID);
		}
		intiView();
	}

	private void intiView() {
		roborder_list_item_tv_servicetype = (TextView) this.findViewById(R.id.roborder_list_item_tv_servicetype);
		roborder_list_item_tv_sn = (TextView) this.findViewById(R.id.roborder_list_item_tv_sn);
		roborder_list_item_tv_address = (TextView) this.findViewById(R.id.roborder_list_item_tv_address);
		roborder_list_item_tv_time = (TextView) this.findViewById(R.id.roborder_list_item_tv_time);
		roborder_item_tv_name = (TextView) this.findViewById(R.id.roborder_item_tv_name);
		roborder_item_tv_mobile = (TextView) this.findViewById(R.id.roborder_item_tv_mobile);
		roborder_item_tv_address = (TextView) this.findViewById(R.id.roborder_item_tv_address);

		order_info_goods_list = (ListView) this.findViewById(R.id.order_info_goods_list);
//		roborder_item_tv_logistics = (TextView) this.findViewById(R.id.roborder_item_tv_logistics);
//		roborder_item_tv_logistics_num = (TextView) this.findViewById(R.id.roborder_item_tv_logistics_num);
//		roborder_item_tv_tihuo = (TextView) this.findViewById(R.id.roborder_item_tv_tihuo);
		activity_orderinfo_empty_tv = (TextView) this.findViewById(R.id.activity_orderinfo_empty_tv);
		order_info_tv_price = (TextView) this.findViewById(R.id.order_info_tv_price);
		getOrderInfo(orderId);
	}

	public void bindData(OrderInfo robOrderInfo) {
		//如果没有抢单的话。敏感信息使用*代替
		String buyerName = StringUtils.replaceByStar(robOrderInfo.getBuyerName(), 1);
		String buyerMobile = StringUtils.replaceByStar(robOrderInfo.getBuyerMobile(), 3);
		String fullAddress = StringUtils.replaceAdd(robOrderInfo.getOrderFullAddress());

		roborder_item_tv_name.setText(buyerName);
		roborder_item_tv_mobile.setText(buyerMobile);
		roborder_item_tv_address.setText(fullAddress);
		order_info_tv_price.setText(String.format("￥%.2f", robOrderInfo.getServicePrice()));
//		order_info_tv_price.setText(String.format("￥%.2f",
//				robOrderInfo.getServicePrice() == 0 ? 0 : robOrderInfo.getServicePrice()));
		//TODO 判断是否是信息完整状态，如果是，显示全部信息 ，否 ，隐藏部分关键信息
		roborder_list_item_tv_servicetype.setText(robOrderInfo.getServiceType() + "");
		roborder_list_item_tv_address.setText(robOrderInfo.getOrderAddress() + "");
		roborder_list_item_tv_time.setText(robOrderInfo.getServiceTime() + "");
		order_info_goods_list.setAdapter(new GoodsAdapter(robOrderInfo.getGoodsInfos(), mContext));
		if (robOrderInfo.getGoodsInfos() != null && robOrderInfo.getGoodsInfos().size() != 0) {
			order_info_goods_list.setVisibility(View.VISIBLE);
			activity_orderinfo_empty_tv.setVisibility(View.GONE);
		} else {
			order_info_goods_list.setVisibility(View.GONE);
			activity_orderinfo_empty_tv.setVisibility(View.VISIBLE);
		}
//		roborder_item_tv_logistics.setText(robOrderInfo.getLogisticsName() + "");
//		roborder_item_tv_logistics_num.setText(robOrderInfo.getAllGoodsNum() + "");
//		roborder_item_tv_tihuo.setText(robOrderInfo.getTakesAddress() + "");
		roborder_list_item_tv_sn.setText("订单编号:" + robOrderInfo.getOrderSN());
	}

	/**
	 * 抢单按钮
	 * 
	 * @param vvoid
	 */
	public void robOrder(View v) {
		if (application.verifyLoggedInAndGoToLogin(getActivity()) && application.isPassAuth(getActivity())) {
			ResponseHandler<PostOrderInfoResponse> responseHandler = new ResponseHandler<PostOrderInfoResponse>() {
				@Override
				public void onStart() {
					openProgressDialog("请稍后...");
				}

				@Override
				public void onFinish(PostOrderInfoResponse response, String error) {
					closeProgressDialog();
					if (response != null && response.isSuccess() && StringUtils.isEmpty(response.getError())) {
						DbOpenUtils.deleteEntityByID(orderInfo.getOrderID(), RobOrderEntity.class);
						ToastUtil.showAtCenterShort(mContext, "恭喜您，抢单成功！");
						Intent intent = new Intent(mContext, NotReserveActivity.class);
						mContext.startActivity(intent);
						finish();
					} else {
						ToastUtil.showAtCenterShort(mContext, response.getError());
					}
				}
			};
			PostOrderInfoRequest request = new PostOrderInfoRequest();
			if (null != orderInfo)
				request.setOrderID(orderInfo.getOrderID());
			HttpPacketClient.postPacketAsynchronous(request, PostOrderInfoResponse.class, responseHandler, true);
		}
	}

	public void getOrderInfo(String orderId) {

		ResponseHandler<GetOrderInfoResponse> responseHandler = new ResponseHandler<GetOrderInfoResponse>() {
			@Override
			public void onStart() {
				openProgressDialog("请稍后...");
			}

			@Override
			public void onFinish(GetOrderInfoResponse response, String error) {
				closeProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					if (response.getOrderInfo() != null)
						orderInfo = response.getOrderInfo();
					bindData(orderInfo);
				}
			}
		};
		GetOrderInfoRequest request = new GetOrderInfoRequest();
		request.setOrderID(orderId);
		HttpPacketClient.postPacketAsynchronous(request, GetOrderInfoResponse.class, responseHandler, true);
	}

}
