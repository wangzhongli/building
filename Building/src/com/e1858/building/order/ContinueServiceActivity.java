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
import com.e1858.building.httppackage.ContinueServiceRequest;
import com.e1858.building.httppackage.ContinueServiceResponse;
import com.e1858.building.httppackage.GetCancelCausesRequest;
import com.e1858.building.httppackage.GetCancelCausesResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.persondata.NotReserveActivity;
import com.e1858.building.utils.DbOpenUtils;
import com.e1858.building.utils.DialogUtil;
import com.e1858.building.utils.ResponseUtils;
import com.hg.android.utils.HGUtils;

public class ContinueServiceActivity extends BaseActivity {
	public static final String	ORDER_ID		= "order_id";
	private String				order_id;
	public List<CancelCause>	cancelCauses	= new ArrayList<CancelCause>();
	public CancelCause			cancelCause;
	int							selectIndex		= 0;
	private TextView			continue_service_tv_cause;
	private TextView			continue_service_tv_time;
	private EditText			continue_service_et_cause;
	private String				continueTime;
	public static final int		TYPE			= 2;

	public static void action(Context context, String order_id) {
		Intent intent = new Intent(context, ContinueServiceActivity.class);
		intent.putExtra(ORDER_ID, order_id);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_continue_service);
		if (null != getIntent()) {
			order_id = getIntent().getStringExtra(ORDER_ID);
		}
		initView();
	}

	/**
	 * 初始化view
	 * void
	 */
	void initView() {
		Button continue_service_btn = (Button) this.findViewById(R.id.continue_service_btn);
		RelativeLayout continue_service_rl_cause = (RelativeLayout) this.findViewById(R.id.continue_service_rl_cause);
		RelativeLayout continue_service_rl_time = (RelativeLayout) this.findViewById(R.id.continue_service_rl_time);
		continue_service_tv_cause = (TextView) this.findViewById(R.id.continue_service_tv_cause);
		continue_service_tv_time = (TextView) this.findViewById(R.id.continue_service_tv_time);
		continue_service_et_cause = (EditText) this.findViewById(R.id.continue_service_et_cause);
		continue_service_btn.setOnClickListener(this);
		continue_service_rl_cause.setOnClickListener(this);
		continue_service_rl_time.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.continue_service_btn:
			String remark = continue_service_et_cause.getText().toString();
			if (cancelCause == null) {
				showToast("必须选择一个原因");
			} else {
				continueService(order_id, cancelCause, continueTime, remark);
			}
			break;
		case R.id.continue_service_rl_cause:
			getCancelCause();
			break;
		case R.id.continue_service_rl_time:
			selecteTime();
			break;

		default:
			break;
		}
	}

	private void selecteTime() {
		DialogUtil.showWithEditWithCancel(mContext, "请选择与买家协商的服务时间", new DialogUtil.OnEditFinishedListener() {

			@Override
			public void onFinished(String text) {
				continue_service_tv_time.setText(text);
				continueTime = text;
			}

		});
	}

	/**
	 * 继续工作接口
	 * void
	 */
	public void continueService(final String orderID, CancelCause cancelCause, String time, String remark) {
		ResponseHandler<ContinueServiceResponse> responseHandler = new ResponseHandler<ContinueServiceResponse>() {
			@Override
			public void onStart() {
				openProgressDialog("请稍后...");
			}

			@Override
			public void onFinish(ContinueServiceResponse response, String error) {
				closeProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					showToast("订单已经延迟，请在待预约列表中查看");
					DbOpenUtils.deleteEntityByID(orderID, OrderEntity.class);
					startActivity(NotReserveActivity.class);
					finish();
					AppManager.getAppManager().finishActivity(OrderDetailActivity.class);
				}
			}
		};
		ContinueServiceRequest request = new ContinueServiceRequest();
		request.setOrderID(orderID);
		request.setCancelCause(cancelCause);
		request.setReServiceTime(time);
		request.setRemark(remark);
		HttpPacketClient.postPacketAsynchronous(request, ContinueServiceResponse.class, responseHandler, true);
	}

	private void getCancelCause() {

		if (HGUtils.isListEmpty(cancelCauses)) {
			GetCancelCausesRequest request = new GetCancelCausesRequest();
			request.setType(TYPE);
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
							showToast("没有获取到再次预约原因");
						}
					}
				}
			};
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
					continue_service_tv_cause.setText(cancelCause.getCause());
				}
			}).setNegativeButton("取消", null).setTitle("再次预约原因").show();
		}

	}

}
