package com.e1858.building.persondata;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.utils.StringUtils;
import com.common.utils.ToastUtil;
import com.e1858.building.Constant;
import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.bean.CancelCause;
import com.e1858.building.bean.OrderInfo;
import com.e1858.building.entity.OrderEntity;
import com.e1858.building.httppackage.ConfirmReserveRequest;
import com.e1858.building.httppackage.ConfirmReserveResponse;
import com.e1858.building.httppackage.GetCancelCausesRequest;
import com.e1858.building.httppackage.GetCancelCausesResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.utils.AppUtil;
import com.e1858.building.utils.DbOpenUtils;
import com.e1858.building.utils.DialogUtil;
import com.e1858.building.utils.ResponseUtils;
import com.hg.android.utils.HGUtils;

public class ReserveActivity extends BaseActivity {
	public final static String	ORDERINFO					= "orderInfo";
	private OrderInfo			orderInfo;
	private String				reserveTime					= "";
	private TextView			reserve_tv_talk;
	private EditText			reserve_et;
	private LinearLayout		reserve_ll;
	private RelativeLayout		reserve_rl_talk;
	public List<CancelCause>	cancelCauses				= new ArrayList<CancelCause>();
	public CancelCause			cancelCause;
	int							selectIndex					= 0;
	private TextView			cancel_order_tv_cause;
	public static final int		TYPE_NOT_RELEASE_SUCCESS	= 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reserve);
		if (null != getIntent()) {
			orderInfo = (OrderInfo) getIntent().getSerializableExtra(ORDERINFO);
		}
		initView();
	}

	private void initView() {
		reserve_tv_talk = (TextView) this.findViewById(R.id.reserve_tv_talk);
		TextView reserve_tv_kefu = (TextView) this.findViewById(R.id.reserve_tv_kefu);
		reserve_rl_talk = (RelativeLayout) this.findViewById(R.id.reserve_rl_talk);
		RelativeLayout reserve_rl_kefu = (RelativeLayout) this.findViewById(R.id.reserve_rl_kefu);
//		RelativeLayout cancel_order_rl_cause = (RelativeLayout) this.findViewById(R.id.cancel_order_rl_cause);
//		cancel_order_tv_cause = (TextView) this.findViewById(R.id.cancel_order_tv_cause);
		reserve_tv_talk.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		reserve_ll = (LinearLayout) this.findViewById(R.id.reserve_ll);
		reserve_et = (EditText) this.findViewById(R.id.reserve_et);
//		DialogUtil.showWithEditWithCancel(mContext, "请选择与买家协商的服务时间", new DialogUtil.OnEditFinishedListener() {
//
//			@Override
//			public void onFinished(String text) {
//				reserve_tv_talk.setText(text);
//				reserveTime = text;
//			}
//
//		});
		reserve_tv_kefu.setText(String.format("温馨提示：如果联系不到买家，请联系客服\n（电话:%s）", Constant.Service_Phone));
		reserve_rl_kefu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppUtil.dial(mContext, Constant.Service_Phone);
			}
		});
		reserve_rl_talk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogUtil.showWithEditWithCancel(mContext, "请选择与买家协商的服务时间", new DialogUtil.OnEditFinishedListener() {

					@Override
					public void onFinished(String text) {
						reserve_tv_talk.setText(text);
						reserveTime = text;
					}

				});
			}
		});
//		cancel_order_rl_cause.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				getCancelCause();
//			}
//		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.personinfo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_accept) {
			if (!StringUtils.isEmpty(reserveTime)) {
				confirmReserve(orderInfo.getOrderID());
			} else {
				if (null != cancelCause) {
					confirmReserve(orderInfo.getOrderID());
				} else {
					showToast("请选择预约失败的原因");
				}
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void confirmReserve(final String orderID) {
		ResponseHandler<ConfirmReserveResponse> responseHandler = new ResponseHandler<ConfirmReserveResponse>() {
			@Override
			public void onStart() {
				showProgressDialog(mContext, "预约确认中，请稍后...");
			}

			@Override
			public void onFinish(ConfirmReserveResponse response, String error) {
				hideProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					ToastUtil.show(mContext, "已修改服务时间请再次联系买家确认预约");
					DbOpenUtils.deleteEntityByID(orderID, OrderEntity.class);
					finish();
				}
			}
		};
		ConfirmReserveRequest request = new ConfirmReserveRequest();
		request.setOrderID(orderID);
		request.setReserveTime(StringUtils.isEmpty(reserveTime) ? null : reserveTime);
		request.setRemark(reserve_et.getText().toString());
		request.setCause(cancelCause);
//		request.setRemark(StringUtils.isEmpty(reserve_et.getText().toString()) ? null : reserve_et.getText().toString());
		HttpPacketClient.postPacketAsynchronous(request, ConfirmReserveResponse.class, responseHandler, true);
	}

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
							showToast("没有获取到预约失败原因");
						}
					}
				}
			};
			request.setType(TYPE_NOT_RELEASE_SUCCESS);
			HttpPacketClient.postPacketAsynchronous(request, GetCancelCausesResponse.class, responseHandler, true);
		} else {
			final CharSequence[] items = new CharSequence[cancelCauses.size()];

			for (int i = 0; i < items.length; i++) {
				items[i] = cancelCauses.get(i).getCause();
			}

			new AlertDialog.Builder(getActivity())
					.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

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
}
