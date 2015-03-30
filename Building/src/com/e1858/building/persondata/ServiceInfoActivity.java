package com.e1858.building.persondata;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.common.utils.StringUtils;
import com.common.utils.ToastUtil;
import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.bean.ServiceInfoBean;
import com.e1858.building.bean.ServiceTypeBean;
import com.e1858.building.httppackage.GetMyServiceInfoRequest;
import com.e1858.building.httppackage.GetMyServiceInfoResponse;
import com.e1858.building.httppackage.GetServiceTypesRequest;
import com.e1858.building.httppackage.GetServiceTypesResponse;
import com.e1858.building.httppackage.GetServiceWorkerTypesRequest;
import com.e1858.building.httppackage.GetServiceWorkerTypesResponse;
import com.e1858.building.httppackage.ModifyMyServiceInfoRequest;
import com.e1858.building.httppackage.ModifyMyServiceInfoResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.utils.ResponseUtils;
import com.e1858.building.view.ServiceTimeDialog;
import com.hg.android.utils.HGUtils;
import com.hg.android.widget.CityDBManager;
import com.hg.android.widget.CityDBManager.AreaEntity;
import com.hg.android.widget.CityPickDialog;
import com.hg.android.widget.CityPickDialog.OnMultiPickedListener;
import com.hg.android.widget.EditTextDialog;

/**
 * 日期: 2015年1月6日 下午5:24:22
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class ServiceInfoActivity extends BaseActivity {

	static final int[]		ListItemIds	= { R.id.listitem_type, //
			R.id.listitem_area,//
			R.id.listitem_service_worker, //
			R.id.listitem_time,//
			R.id.listitem_persons,//
			R.id.listitem_cars,//
			R.id.listitem_carTonnage,//
			R.id.listitem_pickup		//
										};
	SparseArray<TextView>	textViews	= new SparseArray<TextView>();
	List<ServiceTypeBean>	alltypes;
	List<String>			serviceWorkersTypes;						//工人工种集合

	ServiceInfoBean			serviceInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_info);
		initView();
		loadServiceInfo();
	}

	/**
	 */
	private void initView() {
		String[] titles = getResources().getStringArray(R.array.service_info_titles);

		for (int i = 0; i < ListItemIds.length; i++) {
			int id = ListItemIds[i];
			View listitem = findViewById(id);
			((TextView) listitem.findViewById(R.id.textView_title)).setText(titles[i]);
			textViews.put(id, (TextView) listitem.findViewById(R.id.textView_detail));
			listitem.setOnClickListener(onClickListener);
		}
	}

	/**
	 * @param userInfo
	 */
	private void updateView(ServiceInfoBean info) {
		if (info == null) {
			return;
		}
		this.serviceInfo = info;
		textViews.get(R.id.listitem_type).setText(StringUtils.join("|", serviceInfo.getServiceTypes()));
		textViews.get(R.id.listitem_service_worker)
				.setText(StringUtils.join("|", serviceInfo.getServiceWorkersTypes()));

		CityDBManager manager = CityDBManager.sharedInstance(getActivity());
		manager.openDatabase();
		AreaEntity province = manager.areaForCode(info.getServiceProvince());

		AreaEntity city = manager.areaForCode(info.getServiceCity());
		List<String> districts = new ArrayList<String>();
		if (!HGUtils.isListEmpty(info.getServiceDistricts())) {
			for (String code : info.getServiceDistricts()) {
				AreaEntity district = manager.areaForCode(code);
				if (district != null) {
					districts.add(district.getName());
				}
			}
		}
		manager.closeDatabase();

		textViews.get(R.id.listitem_area).setText(
				(city != null ? city.getName() : "") + StringUtils.join("|", districts));
		textViews.get(R.id.listitem_time).setText(ServiceTimeDialog.fmtText2ShowText(serviceInfo.getServiceTime()));
		textViews.get(R.id.listitem_persons).setText("" + serviceInfo.getServicePersons());
		if (serviceInfo.getServiceCarCount() > 0) {
			textViews.get(R.id.listitem_cars).setText("有" /* + info.getServiceCarCount() */);
		} else {
			textViews.get(R.id.listitem_cars).setText("无");
		}
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		textViews.get(R.id.listitem_carTonnage).setText(df.format(serviceInfo.getServiceCarPower()));
		textViews.get(R.id.listitem_pickup).setText(info.getPickupAddress());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.personinfo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_accept) {
			submitMyServiceInfo();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 
	 */
	void submitMyServiceInfo() {
		if (serviceInfo == null) {
			return;
		}
		if (HGUtils.isListEmpty(serviceInfo.getServiceTypes())) {
			ToastUtil.show(getActivity(), "请选择服务类型");
			return;
		}
		ModifyMyServiceInfoRequest request = new ModifyMyServiceInfoRequest();
		request.setServiceInfo(serviceInfo);
		ResponseHandler<ModifyMyServiceInfoResponse> responseHandler = new ResponseHandler<ModifyMyServiceInfoResponse>() {

			@Override
			public void onStart() {
				showProgressDialog(getActivity(), "正在保存");
			}

			@Override
			public void onFinish(ModifyMyServiceInfoResponse response, String error) {
				hideProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					ToastUtil.show(getActivity(), "保存成功");
					finish();
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(request, ModifyMyServiceInfoResponse.class, responseHandler, true);
	}

	/**
	 * 
	 */
	private void loadServiceInfo() {
		GetMyServiceInfoRequest request = new GetMyServiceInfoRequest();
		ResponseHandler<GetMyServiceInfoResponse> responseHandler = new ResponseHandler<GetMyServiceInfoResponse>() {

			@Override
			public void onStart() {
				beginEmptyProgress("");
			}

			@Override
			public void onFinish(GetMyServiceInfoResponse response, String error) {
				String errorText = ResponseUtils.checkResponseAndReturnError(response, error);
				stopEmptyProgress(errorText);
				if (TextUtils.isEmpty(errorText)) {
					updateView(response.getServiceInfo());
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(request, GetMyServiceInfoResponse.class, responseHandler, true);
	}

	/**
	 * 
	 */
	protected void clickOnArea() {
		new CityPickDialog(getActivity(), new OnMultiPickedListener() {
			@Override
			public void onMultiPicked(AreaEntity province, AreaEntity city, List<AreaEntity> districts) {
				List<String> districtCodes = new ArrayList<String>();
				if (!HGUtils.isListEmpty(districts)) {
					for (AreaEntity code : districts) {
						districtCodes.add(code.getCode());
					}
				}

				String text = city.getName() + StringUtils.join("|", districts);
				textViews.get(R.id.listitem_area).setText(text);
				serviceInfo.setServiceProvince(province.getCode());
				serviceInfo.setServiceCity(city.getCode());
				serviceInfo.setServiceDistricts(districtCodes);
			}
		}).show();
	}

	/**
	 * 
	 */
	protected void clickOnTime() {

		new ServiceTimeDialog(getActivity(), new ServiceTimeDialog.OnFinishedListener() {
			@Override
			public void onFinished(String showText, String fmtText) {
				textViews.get(R.id.listitem_time).setText(showText);
				serviceInfo.setServiceTime(fmtText);
			}
		}).show();
	}

	/**
	 * 
	 */
	protected void clickOnPickup() {
		EditTextDialog dialog = new EditTextDialog(getActivity(), "推荐提货地址", "请输入提货地址", textViews.get(
				R.id.listitem_pickup).getText(), new EditTextDialog.OnFinishedListener() {

			@Override
			public void onFinished(String text) {
				textViews.get(R.id.listitem_pickup).setText(text);
				serviceInfo.setPickupAddress(text);
			}
		});
		dialog.show();
	}

	/**
	 * 
	 */
	protected void clickOnCarTonnage() {
		if (serviceInfo.getServiceCarCount() > 0) {

			final DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			EditTextDialog dialog = new EditTextDialog(getActivity(), "货车吨数", "请输入货车吨数", df.format(serviceInfo
					.getServiceCarPower()), new EditTextDialog.OnFinishedListener() {

				@Override
				public void onFinished(String text) {
					float number = -1;
					try {
						number = Float.parseFloat(text);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					if (number <= 0) {
						ToastUtil.show(getActivity(), "请输入大于0的数字");
					} else {
						textViews.get(R.id.listitem_carTonnage).setText(df.format(number));
						serviceInfo.setServiceCarPower(number);
					}
				}
			});
			dialog.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
			dialog.show();
		} else {
			ToastUtil.show(mContext, "请确定您是否有货车");
			textViews.get(R.id.listitem_carTonnage).setText(0 + "");
		}
//		final DecimalFormat df = new DecimalFormat();
//		df.setMaximumFractionDigits(2);
//		EditTextDialog dialog = new EditTextDialog(getActivity(), "货车吨数", "请输入货车吨数", df.format(serviceInfo
//				.getServiceCarPower()), new EditTextDialog.OnFinishedListener() {
//			
//			@Override
//			public void onFinished(String text) {
//				float number = -1;
//				try {
//					number = Float.parseFloat(text);
//				}
//				catch (Exception e) {
//					e.printStackTrace();
//				}
//				if (number <= 0) {
//					ToastUtil.show(getActivity(), "请输入大于0的数字");
//				} else {
//					textViews.get(R.id.listitem_carTonnage).setText(df.format(number));
//					serviceInfo.setServiceCarPower(number);
//				}
//			}
//		});
//		dialog.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//		dialog.show();
	}

	/**
	 * 
	 */
	int	selectIndex	= 0;

	protected void clickOnCars() {
//		EditTextDialog dialog = new EditTextDialog(getActivity(), "货车数量", "请输入货车数量", ""
//				+ serviceInfo.getServiceCarCount(), new EditTextDialog.OnFinishedListener() {
//
//			@Override
//			public void onFinished(String text) {
//				int number = -1;
//				try {
//					number = Integer.parseInt(text);
//				}
//				catch (Exception e) {
//					e.printStackTrace();
//				}
//				if (number < 0) {
//					ToastUtil.show(getActivity(), "请输入大于等于0的数字");
//				} else {
//					textViews.get(R.id.listitem_cars).setText(text);
//					serviceInfo.setServiceCarCount(number);
//				}
//			}
//		});
//		dialog.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
//		dialog.show();

		final CharSequence[] items = { "无", "有" };

		new AlertDialog.Builder(getActivity()).setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectIndex = which;
			}

		}).setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				textViews.get(R.id.listitem_cars).setText(items[selectIndex]);
				serviceInfo.setServiceCarCount(selectIndex);
				if (!(serviceInfo.getServiceCarCount() > 0)) {
					textViews.get(R.id.listitem_carTonnage).setText(0 + "");
				}
			}
		}).setNegativeButton("取消", null).setTitle("您有货车吗").show();

	}

	private void clickOnType() {
		if (!verifyInfoLoaded()) {
			return;
		}
		if (HGUtils.isListEmpty(alltypes)) {
			GetServiceTypesRequest request = new GetServiceTypesRequest();
			ResponseHandler<GetServiceTypesResponse> responseHandler = new ResponseHandler<GetServiceTypesResponse>() {

				@Override
				public void onStart() {
					showProgressDialog(getActivity(), "正在加载");
				}

				@Override
				public void onFinish(GetServiceTypesResponse response, String error) {
					hideProgressDialog();
					if (ResponseUtils.checkResponseAndToastError(response, error)) {
						alltypes = response.getServiceTypes();
						if (!HGUtils.isListEmpty(alltypes)) {
							clickOnType();
						}
					}
				}
			};
			HttpPacketClient.postPacketAsynchronous(request, GetServiceTypesResponse.class, responseHandler, true);
		} else {
			CharSequence[] items = new CharSequence[alltypes.size()];
			final boolean[] checkedItems = new boolean[alltypes.size()];
			for (int i = 0; i < items.length; i++) {
				items[i] = alltypes.get(i).getName();
				if (HGUtils.isListEmpty(serviceInfo.getServiceTypes())) {
					checkedItems[i] = false;
				} else {
					checkedItems[i] = serviceInfo.getServiceTypes().contains(items[i]);
				}
			}

			new AlertDialog.Builder(getActivity())
					.setMultiChoiceItems(items, checkedItems, new OnMultiChoiceClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which, boolean isChecked) {
							checkedItems[which] = isChecked;
						}
					}).setPositiveButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							List<ServiceTypeBean> selectedTypes = new ArrayList<ServiceTypeBean>();
							for (int i = 0; i < checkedItems.length; i++) {
								if (checkedItems[i]) {
									selectedTypes.add(alltypes.get(i));
								}
							}
							textViews.get(R.id.listitem_type).setText(StringUtils.join("|", selectedTypes));
							serviceInfo.setServiceTypes(selectedTypes);
						}
					}).setNegativeButton("取消", null).setTitle("选择服务类型").show();
		}
	}

	boolean verifyInfoLoaded() {
		if (serviceInfo == null) {
			ToastUtil.show(getActivity(), "请稍后");
			return false;
		}
		return true;
	}

	/**
	 * 
	 */
	protected void clickOnPersons() {
		EditTextDialog dialog = new EditTextDialog(getActivity(), "人数", "请输入服务人数",
				"" + serviceInfo.getServicePersons(), new EditTextDialog.OnFinishedListener() {

					@Override
					public void onFinished(String text) {
						int number = -1;
						try {
							number = Integer.parseInt(text);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
						if (number < 0) {
							ToastUtil.show(getActivity(), "请输入大于等于0的数字");
						} else {
							textViews.get(R.id.listitem_persons).setText(text);
							serviceInfo.setServicePersons(number);
						}
					}
				});
		dialog.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
		dialog.show();
	}

	OnClickListener	onClickListener	= new OnClickListener() {

										@Override
										public void onClick(View view) {
											switch (view.getId()) {
											case R.id.listitem_type:
												clickOnType();
												break;
											case R.id.listitem_area:
												clickOnArea();
												break;
											case R.id.listitem_time:
												clickOnTime();
												break;
											case R.id.listitem_persons:
												clickOnPersons();
												break;
											case R.id.listitem_cars:
												clickOnCars();
												break;
											case R.id.listitem_carTonnage:
												clickOnCarTonnage();
												break;
											case R.id.listitem_pickup:
												clickOnPickup();
												break;
											case R.id.listitem_service_worker:
												clickOnServiceWorker();
												break;

											}
										}
									};

	/**
	 * 工人工种选择
	 * void
	 */
	protected void clickOnServiceWorker() {

		if (!verifyInfoLoaded()) {
			return;
		}
		if (HGUtils.isListEmpty(serviceWorkersTypes)) {
			GetServiceWorkerTypesRequest request = new GetServiceWorkerTypesRequest();
			ResponseHandler<GetServiceWorkerTypesResponse> responseHandler = new ResponseHandler<GetServiceWorkerTypesResponse>() {

				@Override
				public void onStart() {
					showProgressDialog(getActivity(), "正在加载");
				}

				@Override
				public void onFinish(GetServiceWorkerTypesResponse response, String error) {
					hideProgressDialog();
					if (ResponseUtils.checkResponseAndToastError(response, error)) {
						serviceWorkersTypes = response.getServiceWorkersTypes();
						if (!HGUtils.isListEmpty(serviceWorkersTypes)) {
							clickOnServiceWorker();
						}
					}
				}
			};
			HttpPacketClient
					.postPacketAsynchronous(request, GetServiceWorkerTypesResponse.class, responseHandler, true);
		} else {
			CharSequence[] items = new CharSequence[serviceWorkersTypes.size()];
			final boolean[] checkedItems = new boolean[serviceWorkersTypes.size()];
			for (int i = 0; i < items.length; i++) {
				items[i] = serviceWorkersTypes.get(i);
				if (HGUtils.isListEmpty(serviceInfo.getServiceWorkersTypes())) {
					checkedItems[i] = false;
				} else {
					checkedItems[i] = serviceInfo.getServiceWorkersTypes().contains(items[i]);
				}
			}

			new AlertDialog.Builder(getActivity())
					.setMultiChoiceItems(items, checkedItems, new OnMultiChoiceClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which, boolean isChecked) {
							checkedItems[which] = isChecked;
						}
					}).setPositiveButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							List<String> selectedWorkerTypes = new ArrayList<String>();
							for (int i = 0; i < checkedItems.length; i++) {
								if (checkedItems[i]) {
									selectedWorkerTypes.add(serviceWorkersTypes.get(i));
								}
							}
							textViews.get(R.id.listitem_service_worker).setText(
									StringUtils.join("|", selectedWorkerTypes));
							serviceInfo.setServiceWorkersTypes(selectedWorkerTypes);
						}
					}).setNegativeButton("取消", null).setTitle("选择工人工种").show();
		}

	}
}
