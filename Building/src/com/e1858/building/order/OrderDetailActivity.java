package com.e1858.building.order;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.utils.DialogUtil;
import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.bean.CancelCause;
import com.e1858.building.bean.OrderInfo;
import com.e1858.building.httppackage.GetOrderInfoRequest;
import com.e1858.building.httppackage.GetOrderInfoResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.utils.ResponseUtils;

/**
 * 功能：订单详细信息
 * 日期: 2015-2-8 上午9:41:02
 * 作者: wangzhongli
 */
public class OrderDetailActivity extends BaseActivity {
	public static final String	ORDER_ID				= "orderid";
	public List<CancelCause>	cancelCauses;
	private String				orderId;

	public int					type					= TYPE_ACTION_NORMAL;
	public final static int		TYPE_ACTION_SPECIAL		= 1;
	public final static int		TYPE_ACTION_NORMAL		= 2;
	public final static int		TYPE_ACTION_NO_CANCEL	= 3;
	public final static String	TYPE_ACTION				= "type_action";
	private Button				order_detail_btn_special;
	private LinearLayout		order_detail_ll_special;
	private TextView			order_content_tv_service_type;
	private TextView			order_content_tv_accept_time;
	private TextView			order_content_tv_service_time;
	private TextView			order_info_tv_price;
//	private TextView			order_content_tv_order_from;
	private TextView			roborder_item_tv_name;
	private TextView			roborder_item_tv_mobile;
	private TextView			roborder_item_tv_address;
	private ListView			order_info_goods_list;
	private TextView			activity_orderinfo_empty_tv;
//	private TextView			roborder_item_tv_logistics;
//	private TextView			roborder_item_tv_logistics_num;
//	private TextView			roborder_item_tv_tihuo;
	private MenuItem			cancel_order;
	private TextView			roborder_list_item_tv_sn;

	public static void action(Context context, String order_id, int type) {
		Intent intent = new Intent(context, OrderDetailActivity.class);
		intent.putExtra(ORDER_ID, order_id);
		intent.putExtra(TYPE_ACTION, type);
		context.startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.cancel_order) {
			CancelOrderActivity.action(mContext, orderId, CancelOrderActivity.TYPE_NOT_RELEASE);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.cancel_order, menu);
		cancel_order = menu.findItem(R.id.cancel_order);
		if (type == TYPE_ACTION_NO_CANCEL)
			cancel_order.setVisible(false);
		if (type == TYPE_ACTION_SPECIAL)
			cancel_order.setVisible(false);
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);
		initView();
		if (null != getIntent()) {
			orderId = getIntent().getStringExtra(ORDER_ID);
			getOrderInfo(orderId);
			type = getIntent().getIntExtra(TYPE_ACTION, 0);
			switch (type) {
			case TYPE_ACTION_SPECIAL://有，异常情况，没有取消
				order_detail_ll_special.setVisibility(View.VISIBLE);
				if (null != cancel_order)
					cancel_order.setVisible(false);
				break;
			case TYPE_ACTION_NORMAL://没有异常情况，有取消
				order_detail_ll_special.setVisibility(View.GONE);
				break;
			case TYPE_ACTION_NO_CANCEL://没有异常，没有取消按钮
				order_detail_ll_special.setVisibility(View.GONE);
				if (null != cancel_order)
					cancel_order.setVisible(false);
				break;

			default:
				break;
			}
		}

	}

	public void initView() {
		order_detail_btn_special = (Button) this.findViewById(R.id.order_detail_btn_special);
		order_detail_ll_special = (LinearLayout) this.findViewById(R.id.order_detail_ll_special);
		roborder_list_item_tv_sn = (TextView) this.findViewById(R.id.roborder_list_item_tv_sn);

		order_content_tv_service_type = (TextView) this.findViewById(R.id.order_content_tv_service_type);
		order_content_tv_accept_time = (TextView) this.findViewById(R.id.order_content_tv_accept_time);
		order_content_tv_service_time = (TextView) this.findViewById(R.id.order_content_tv_service_time);

		order_info_tv_price = (TextView) this.findViewById(R.id.order_info_tv_price);
//		order_content_tv_order_from = (TextView) this.findViewById(R.id.order_content_tv_order_from);

		roborder_item_tv_name = (TextView) this.findViewById(R.id.roborder_item_tv_name);
		roborder_item_tv_mobile = (TextView) this.findViewById(R.id.roborder_item_tv_mobile);
		roborder_item_tv_address = (TextView) this.findViewById(R.id.roborder_item_tv_address);

		order_info_goods_list = (ListView) this.findViewById(R.id.order_info_goods_list);
		activity_orderinfo_empty_tv = (TextView) this.findViewById(R.id.activity_orderinfo_empty_tv);

//		roborder_item_tv_logistics = (TextView) this.findViewById(R.id.roborder_item_tv_logistics);
//		roborder_item_tv_logistics_num = (TextView) this.findViewById(R.id.roborder_item_tv_logistics_num);
//		roborder_item_tv_tihuo = (TextView) this.findViewById(R.id.roborder_item_tv_tihuo);

		RelativeLayout order_detail_logistic_rl = (RelativeLayout) this.findViewById(R.id.order_detail_logistic_rl);
		order_detail_btn_special.setOnClickListener(this);
		order_detail_logistic_rl.setOnClickListener(this);
	}

	/**
	 * 绑定数据
	 * 
	 * @param orderInfo
	 *            void
	 */
	protected void bindData(OrderInfo orderInfo) {
		order_content_tv_service_type.setText(orderInfo.getServiceType() + "");
		order_content_tv_accept_time.setText(orderInfo.getOrderTime() + "");
		order_content_tv_service_time.setText(orderInfo.getServiceTime() + "");

		order_info_tv_price.setText(String.format("￥%.2f", orderInfo.getServicePrice()));
//		order_content_tv_order_from.setText(orderInfo.getOrigin() + "");

		roborder_item_tv_name.setText(orderInfo.getBuyerName() + "");
		roborder_item_tv_mobile.setText(orderInfo.getBuyerMobile() + "");
		roborder_item_tv_address.setText(orderInfo.getOrderAddress() + "");
		order_info_goods_list.setAdapter(new GoodsAdapter(orderInfo.getGoodsInfos(), mContext));
		if (orderInfo.getGoodsInfos() != null && orderInfo.getGoodsInfos().size() != 0) {
			order_info_goods_list.setVisibility(View.VISIBLE);
			activity_orderinfo_empty_tv.setVisibility(View.GONE);
		} else {
			order_info_goods_list.setVisibility(View.GONE);
			activity_orderinfo_empty_tv.setVisibility(View.VISIBLE);
		}
		roborder_list_item_tv_sn.setText("订单编号: " + orderInfo.getOrderSN());
//		roborder_item_tv_logistics.setText(orderInfo.getLogisticsName() + "");
//		roborder_item_tv_logistics_num.setText(orderInfo.getAllGoodsNum() + "");
//		roborder_item_tv_tihuo.setText(orderInfo.getTakesAddress() + "");
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.order_detail_btn_special://弹框询问操作
			DialogUtil.show(mContext, "温馨提醒", "因特殊原因不能正常服务，请点击“取消订单”\n如需二次服务请点击“继续服务”", "继续服务", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					ContinueServiceActivity.action(mContext, orderId);
				}
			}, "取消订单", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					CancelOrderActivity.action(mContext, orderId, CancelOrderActivity.TYPE_NOT_COMPLETE);
				}
			});
			break;
		case R.id.order_detail_logistic_rl:
			LogisticInfosActivity.action(mContext, orderId);
		default:
			break;
		}
	}

	/**
	 * 获取订单详情
	 * void
	 */
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
					if (response.getOrderInfo() != null) {
						OrderInfo orderInfo = response.getOrderInfo();
						bindData(orderInfo);
					}
				}
			}
		};
		GetOrderInfoRequest request = new GetOrderInfoRequest();
		request.setOrderID(orderId);
		HttpPacketClient.postPacketAsynchronous(request, GetOrderInfoResponse.class, responseHandler, true);
	}
}
