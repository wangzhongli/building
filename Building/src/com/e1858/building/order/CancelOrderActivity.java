package com.e1858.building.order;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.e1858.building.AppManager;
import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.bean.CancelCause;
import com.e1858.building.entity.OrderEntity;
import com.e1858.building.httppackage.CancelOrderRequest;
import com.e1858.building.httppackage.CancelOrderResponse;
import com.e1858.building.httppackage.GetCancelCausesRequest;
import com.e1858.building.httppackage.GetCancelCausesResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.utils.DbOpenUtils;
import com.e1858.building.utils.ResponseUtils;
import com.hg.android.utils.HGUtils;

/**
 * 功能：取消订单界面
 * 日期: 2015-2-8 下午2:11:09
 * 作者: wangzhongli
 */
public class CancelOrderActivity extends BaseActivity {
	public static final String	ORDER_ID			= "order_id";
	public static final String	TYPE				= "type";
	public static final int		TYPE_NOT_RELEASE	= 1;
	public static final int		TYPE_NOT_COMPLETE	= 2;
	private String				order_id;
	private RelativeLayout		cancel_order_rl_cause;
	private TextView			cancel_order_tv_cause;
	private EditText			cancel_order_et_cause;
	private Button				cancel_order_btn;
	public int					type;

	public List<CancelCause>	cancelCauses		= new ArrayList<CancelCause>();
	public CancelCause			cancelCause;

	public static void action(Context context, String order_id, int type) {
		Intent intent = new Intent(context, CancelOrderActivity.class);
		intent.putExtra(ORDER_ID, order_id);
		intent.putExtra(TYPE, type);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cancel_order);
		if (null != getIntent()) {
			order_id = getIntent().getStringExtra(ORDER_ID);
			type = getIntent().getIntExtra(TYPE, 1);
		}
		initView();
	}

	/**
	 * 初始化view
	 * void
	 */
	void initView() {
		cancel_order_rl_cause = (RelativeLayout) this.findViewById(R.id.cancel_order_rl_cause);
		cancel_order_tv_cause = (TextView) this.findViewById(R.id.cancel_order_tv_cause);
		cancel_order_et_cause = (EditText) this.findViewById(R.id.cancel_order_et_cause);
		cancel_order_btn = (Button) this.findViewById(R.id.cancel_order_btn);
		cancel_order_rl_cause.setOnClickListener(this);
		cancel_order_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.cancel_order_rl_cause://谈对话框
			getCancelCause();
			break;
		case R.id.cancel_order_btn:
			String remark = cancel_order_et_cause.getText().toString().trim();
			if (cancelCause == null) {
				showToast("必须选择一个原因");
			} else {
				cancelOrder(order_id, cancelCause, remark);
			}
			break;

		default:
			break;
		}
	}

	int	selectIndex	= 0;

	private void getCancelCause() {

		if (HGUtils.isListEmpty(cancelCauses)) {
			GetCancelCausesRequest request = new GetCancelCausesRequest();
			ResponseHandler<GetCancelCausesResponse> responseHandler = new ResponseHandler<GetCancelCausesResponse>() {

				@Override
				public void onStart() {
					openProgressDialog("请稍后...");
				}

				@Override
				public void onFinish(GetCancelCausesResponse response, String error) {
					closeProgressDialog();
					if (ResponseUtils.checkResponseAndToastError(response, error)) {
						cancelCauses = response.getCancelCauses();
						if (!HGUtils.isListEmpty(cancelCauses)) {
							getCancelCause();
						} else {
							showToast("没有获取到取消原因");
						}
					}
				}
			};
			request.setType(type);
			HttpPacketClient.postPacketAsynchronous(request, GetCancelCausesResponse.class, responseHandler, true);
		} else {
			final CharSequence[] items = new CharSequence[cancelCauses.size()];

			for (int i = 0; i < items.length; i++) {
				items[i] = cancelCauses.get(i).getCause();
			}

			new AlertDialog.Builder(getActivity()).setSingleChoiceItems(items, 0, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					selectIndex = which;
				}
			}).setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					cancelCause = cancelCauses.get(selectIndex);
					cancel_order_tv_cause.setText(cancelCause.getCause());
				}
			}).setNegativeButton("取消", null).setTitle("请选择您的取消原因").show();
		}

	}

	/**
	 * 取消订单
	 * void
	 */
	public void cancelOrder(final String orderID, CancelCause cancelCause, String remark) {
		ResponseHandler<CancelOrderResponse> responseHandler = new ResponseHandler<CancelOrderResponse>() {
			@Override
			public void onStart() {
				openProgressDialog("请稍后...");
			}

			@Override
			public void onFinish(CancelOrderResponse response, String error) {
				closeProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					showToast("取消订单成功");
					DbOpenUtils.deleteEntityByID(orderID, OrderEntity.class);
					finish();
					AppManager.getAppManager().finishActivity(OrderDetailActivity.class);
				}
			}
		};
		CancelOrderRequest request = new CancelOrderRequest();
		request.setOrderID(orderID);
		request.setCancelCause(cancelCause);
		request.setRemark(remark);

		HttpPacketClient.postPacketAsynchronous(request, CancelOrderResponse.class, responseHandler, true);
	}

}
